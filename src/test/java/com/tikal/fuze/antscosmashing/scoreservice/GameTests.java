package com.tikal.fuze.antscosmashing.scoreservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikal.fuze.antscosmashing.scoreservice.handler.hittrials.KinesisHitTrialEventsHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.hittrials.PostHitTrialHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.response.ApiGatewayResponse;
import com.tikal.fuze.antscosmashing.scoreservice.handler.scores.GetPlayersScoresHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.scores.GetTeamsScoresHandler;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.GamesRepository;
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
public class GameTests {
    private static final Logger logger = LogManager.getLogger(GameTests.class);
    private GamesRepository gamesRepository = new GamesRepository();


    @Test
    public void testPostHitTrialWebApi() throws IOException {
        gamesRepository.put(88);
    }





}

