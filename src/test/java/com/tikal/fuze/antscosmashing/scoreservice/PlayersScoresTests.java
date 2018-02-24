package com.tikal.fuze.antscosmashing.scoreservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikal.fuze.antscosmashing.scoreservice.handler.hittrials.KinesisHitTrialEventsHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.hittrials.PostHitTrialHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.response.ApiGatewayResponse;
import com.tikal.fuze.antscosmashing.scoreservice.handler.scores.GetLatestGameHandler;
import com.tikal.fuze.antscosmashing.scoreservice.service.PlayerScoresService;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Ignore
public class PlayersScoresTests {
    private static final Logger logger = LogManager.getLogger(PlayersScoresTests.class);


    private ObjectMapper om = new ObjectMapper();

    private PlayerScoresService playerScoresService = new PlayerScoresService();

    static {
        Map<String,String> env = new HashMap<>();
        env.put("FIRST_SELF_HIT","-4");
        env.put("FIRST_HIT","4");
        env.put("SELF_HIT","-2");
        env.put("HIT","2");
        env.put("MISS","0");
        try {
            setEnv(env);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testPostHitTrialWebApi() throws IOException {
        HashMap<String, Object> input = getHandlerInput("post-hitTrial.json");
        new PostHitTrialHandler(playerScoresService).handleRequest(input,null);
    }


    @Test
    public void testPlayerScoreServicePostToKinesis() throws IOException {
        HashMap<String, Object> input = getHandlerInput("post-hitTrial.json");
        String body = input.get("body").toString();
        new KinesisHitTrialEventsHandler(playerScoresService).handleKinesisData(body);
    }


    @Test
    public void testGetLatestGame() throws IOException {
        ApiGatewayResponse apiGatewayResponse = new GetLatestGameHandler().handleRequest(null, null);
        logger.debug(apiGatewayResponse.getBody());
    }



    public HashMap<String, Object> getHandlerInput(String fileName) throws IOException {
        String data = getStringFromInputFile(fileName);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        TypeReference<HashMap<String,Object>> typeRef
                = new TypeReference<HashMap<String,Object>>() {};

        HashMap<String,Object> input = om.readValue(inputStream, typeRef);
        return input;
    }


    private String getStringFromInputFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }


    protected static void setEnv(Map<String, String> newenv)  throws Exception{
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }


}

