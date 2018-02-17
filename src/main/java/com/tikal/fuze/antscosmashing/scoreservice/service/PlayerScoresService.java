package com.tikal.fuze.antscosmashing.scoreservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.PlayersScoresRepository;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.SmashedAntsRepository;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.TeamsScoresRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static java.lang.System.getenv;

public class PlayerScoresService {
    private static final Logger logger = LogManager.getLogger(PlayerScoresService.class);

    private ObjectMapper mapper = new ObjectMapper();


    private PlayersScoresRepository playersScoresRepository;
    private TeamsScoresRepository teamsScoresRepository;
    private SmashedAntsRepository smashedAntsRepository;

    private int missScore;
    private int hitScore;
    private int firstHitScore;
    private int selfHitScore;
    private int firstSelfHitScore;

    public PlayerScoresService(int hitScore, int firstHitScore, int selfHitScore, int firstSelfHitScore,int missScore) {
        this();
        this.missScore=missScore;
        this.hitScore = hitScore;
        this.firstHitScore = firstHitScore;
        this.selfHitScore = selfHitScore;
        this.firstSelfHitScore = firstSelfHitScore;
    }

    public PlayerScoresService() {
        playersScoresRepository = new PlayersScoresRepository();
        teamsScoresRepository = new TeamsScoresRepository();
        smashedAntsRepository = new SmashedAntsRepository();
    }

    public List<String> getPlayersScores(String gameId)  {
        return playersScoresRepository.getPlayersScoresByGameId(Integer.valueOf(gameId));
    }

    public List<String> getTeamsScores(String gameId)  {
        return teamsScoresRepository.getTeamsScoresByGameId(Integer.valueOf(gameId));
    }

    public void savePlayerScore(String hitTrialStr) {
        try {
            logger.debug("Handling hitTrialStr: {}", hitTrialStr);
            setEnvVariables();
            JsonNode hitTrial;
            hitTrial = mapper.readTree(hitTrialStr);

            String type = hitTrial.get("type").textValue();
            String antId = null;
            if (!type.equals("miss"))
                antId = hitTrial.get("antId").textValue();
            int playerId = hitTrial.get("playerId").intValue();
            int gameId = hitTrial.get("gameId").intValue();


            int score=0;
            if (type.equals("miss"))
                score = calcMissScore(hitTrialStr);
            else if (type.equals("hit"))
                score = calcHitOrFirstHitScore(playerId, antId,  false);
            else if (type.equals("selfHit"))
                score = calcHitOrFirstHitScore(playerId, antId,  true);

            String playerName = hitTrial.get("playerName").textValue();
            playersScoresRepository.put(playerId ,gameId,playerName,score);

            int teamId = hitTrial.get("teamId").intValue();
            String teamName = hitTrial.get("teamName").textValue();
            int antSpeciesId = hitTrial.get("antSpeciesId").intValue();
            String antSpeciesName = hitTrial.get("antSpeciesName").textValue();
            teamsScoresRepository.put(teamId ,gameId,teamName,antSpeciesId,antSpeciesName,score);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void setEnvVariables() {
        if(getenv("HIT")==null){
            logger.warn("HIT score is missing. we assume a testing mode so we fallback to default");
            logger.debug("hitScore:{}, firstHitScore:{}, selfHitScore:{}, firstSelfHitScore:{}",hitScore,firstHitScore,selfHitScore,selfHitScore);
            return;
        }

        missScore = Integer.valueOf(getenv("MISS"));
        hitScore = Integer.valueOf(getenv("HIT"));
        firstHitScore = Integer.valueOf(getenv("FIRST_HIT"));
        selfHitScore =  Integer.valueOf(getenv("SELF_HIT"));
        firstSelfHitScore = Integer.valueOf(getenv("FIRST_SELF_HIT"));
        logger.debug("hitScore:{}, firstHitScore:{}, selfHitScore:{}, firstSelfHitScore:{}",hitScore,firstHitScore,selfHitScore,selfHitScore);
    }

    private int calcMissScore(String hitTrialStr) {
        logger.debug("return miss score of {} for the input {}",missScore,hitTrialStr);
        return missScore;
    }

    private int calcHitOrFirstHitScore(int playerId , String antId, boolean self) {
        int score;
        if (isSmashedNow(antId)) {
            smashedAntsRepository.put(antId, playerId);
            score = (self) ? firstSelfHitScore : firstHitScore;
            logger.debug("firstHit event:{}" , playerId);
        } else {
            score = (self) ? selfHitScore : hitScore;
            logger.debug("hit event:{}" , playerId);
        }
        return score;
    }



    private boolean isSmashedNow(String antId) {
        return !(smashedAntsRepository.isExist(antId));
    }


}
