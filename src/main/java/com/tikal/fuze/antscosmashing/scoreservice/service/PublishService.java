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

    public void publishScores(HitTrial hitTrial, int playerScore,int teamScore){
        try {
            hitTrial.setPlayerScore(playerScore);
            hitTrial.setTeamScore(teamScore);
            pusher.trigger("scores", "score", hitTrial);
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
