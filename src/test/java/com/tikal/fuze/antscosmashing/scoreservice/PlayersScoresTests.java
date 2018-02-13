package com.tikal.fuze.antscosmashing.scoreservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikal.fuze.antscosmashing.scoreservice.handler.hittrials.KinesisHitTrialEventsHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.hittrials.PostHitTrialHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.response.ApiGatewayResponse;
import com.tikal.fuze.antscosmashing.scoreservice.handler.scores.GetPlayersScoresHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.scores.GetTeamsScoresHandler;
import com.tikal.fuze.antscosmashing.scoreservice.service.PlayerScoresService;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

@Ignore
public class PlayersScoresTests {
    private static final Logger logger = LogManager.getLogger(PlayersScoresTests.class);


    private ObjectMapper om = new ObjectMapper();

    private PlayerScoresService playerScoresService = new PlayerScoresService(2,4,-4,-2);



    @Test
    public void testPostHitTrialWebApi() throws IOException {
        HashMap<String, Object> input = toMap("post-hitTrial.json");
        new PostHitTrialHandler(playerScoresService).handleRequest(input,null);
    }


    @Test
    public void testPlayerScoreServicePostToKinesis() throws IOException {
        HashMap<String, Object> input = toMap("post-hitTrial.json");
        String body = input.get("body").toString();
        new KinesisHitTrialEventsHandler(playerScoresService).handleKinesisData(body);
    }

    @Test
    public void testGetScoresWebApi() throws IOException {
        HashMap<String, Object> input = toMap("get-scores-by-gameId.json");
        ApiGatewayResponse apiGatewayResponse = new GetPlayersScoresHandler().handleRequest(input, null);
        logger.debug(apiGatewayResponse.getBody());

        apiGatewayResponse = new GetTeamsScoresHandler().handleRequest(input, null);
        logger.debug(apiGatewayResponse.getBody());
    }




    public HashMap<String, Object> toMap(String fileName) throws IOException {
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


}

