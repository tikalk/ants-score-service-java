package com.tikal.fuze.antscosmashing.scoreservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pusher.rest.Pusher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;

import static java.util.Collections.singletonMap;

public class PublishService {
    private static final Logger logger = LogManager.getLogger(PublishService.class);
    private Pusher pusher;
    private ObjectMapper mapper= new ObjectMapper();

    public PublishService(){
        pusher = new Pusher("480094", "ee30a7dbb762fb045133", "979cc7a705030939aa0f");
        pusher.setEncrypted(true);
    }


    public void publishTeamScore(int teamId, int gameId, String teamName, int antSpeciesId, String antSpeciesName, int score) {
        ObjectNode objectNode = mapper.createObjectNode()
                .put("teamId", teamId)
                .put("gameId", gameId)
                .put("teamName", teamName)
                .put("antSpeciesId", antSpeciesId)
                .put("antSpeciesName", antSpeciesName)
                .put("score", score);
        try {
            pusher.trigger("scores", "teamScore", singletonMap("message", objectNode.toString()));
        }catch (Exception e){
            logger.error(e);
        }
    }

    public void publishPlayerScore(int playerId, int gameId, String playerName, int score) {
        ObjectNode objectNode = mapper.createObjectNode()
                .put("playerId", playerId)
                .put("gameId", gameId)
                .put("playerName", playerName)
                .put("score", score);
        try{
            pusher.trigger("scores", "playerScore", singletonMap("message", objectNode.toString()));
        }catch (Exception e){
            logger.error(e);
        }
    }
}
