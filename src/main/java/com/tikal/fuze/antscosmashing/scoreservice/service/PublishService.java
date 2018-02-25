package com.tikal.fuze.antscosmashing.scoreservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pusher.rest.Pusher;
import com.tikal.fuze.antscosmashing.scoreservice.domain.HitTrial;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.DbManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Collections.singletonMap;

public class PublishService {
    private static final Logger logger = LogManager.getLogger(PublishService.class);
    private Pusher pusher = createPusher();
    private ObjectMapper mapper= new ObjectMapper();

    public void publishScores(HitTrial hitTrial, int playerScore,int teamScore){
        publishTeamScore(hitTrial,playerScore);
        publishTeamScore(hitTrial,teamScore);

    }


    private void publishTeamScore(HitTrial hitTrial, int score) {
        ObjectNode objectNode = mapper.createObjectNode()
                .put("teamId", hitTrial.getTeamId())
                .put("gameId", hitTrial.getGameId())
                .put("teamName", hitTrial.getTeamName())
                .put("antSpeciesId", hitTrial.getAntSpeciesId())
                .put("antSpeciesName", hitTrial.getAntSpeciesName())
                .put("score", score);
        try {
            pusher.trigger("scores", "teamScore", singletonMap("message", objectNode.toString()));
        }catch (Exception e){
            logger.error(e);
        }
    }

    private void publishPlayerScore(HitTrial hitTrial, int score) {
        ObjectNode objectNode = mapper.createObjectNode()
                .put("playerId", hitTrial.getPlayerId())
                .put("gameId", hitTrial.getGameId())
                .put("playerName", hitTrial.getPlayerName())
                .put("score", score);
        try{
            pusher.trigger("scores", "playerScore", singletonMap("message", objectNode.toString()));
        }catch (Exception e){
            logger.error(e);
        }
    }

    private Pusher createPusher() {
        try {
            Properties prop = new Properties();
            InputStream inputStream = PublishService.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(inputStream);

            pusher = new Pusher(prop.getProperty("pusher_appId"),  prop.getProperty("pusher_key"), prop.getProperty("pusher_secret"));
            pusher.setEncrypted(true);
            return pusher;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
