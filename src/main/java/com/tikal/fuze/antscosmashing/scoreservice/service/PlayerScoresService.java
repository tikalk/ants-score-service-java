package com.tikal.fuze.antscosmashing.scoreservice.service;

import com.tikal.fuze.antscosmashing.scoreservice.domain.HitTrial;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.PlayersScoresRepository;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.SmashedAntsRepository;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.TeamsScoresRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Integer.valueOf;
import static java.lang.System.getenv;

public class PlayerScoresService {
    private static final Logger logger = LogManager.getLogger(PlayerScoresService.class);

    private PlayersScoresRepository playersScoresRepository = new PlayersScoresRepository();
    private TeamsScoresRepository teamsScoresRepository = new TeamsScoresRepository();
    private SmashedAntsRepository smashedAntsRepository = new SmashedAntsRepository();
    private PublishService publishService = new PublishService();

    public HitTrial savePlayerScore(HitTrial hitTrial) {
        logger.debug("Handling hitTrialStr: {}", hitTrial);
        int score = calculateScore(hitTrial);

        logger.debug("Start putScores...");
        playersScoresRepository.put(hitTrial,score);
        teamsScoresRepository.put(hitTrial,score);
        logger.debug("Finished putScores.");

        if(score!=0 && getenv("PUSHER_PUBLISH").equals("true"))
            publishScores(hitTrial,score);
        return hitTrial;
    }

    private void publishScores(HitTrial hitTrial, int score) {
        logger.debug("Start getScores...");
        int previousPlayerScore = playersScoresRepository.getScore(hitTrial.getPlayerId());
        int previousTeamScore = teamsScoresRepository.getScore(hitTrial.getTeamId());
        logger.debug("Finished getScores.");
        publishService.publishScores(hitTrial,previousPlayerScore,previousTeamScore);
    }

    private int calculateScore(HitTrial hitTrial) {
        logger.debug("Start calc scores...");
        int score=0;
        if (hitTrial.getType().equals("miss"))
            score = valueOf(getenv("MISS"));
        else if (hitTrial.getType().equals("hit"))
            score = calculateScore(hitTrial,  false);
        else if (hitTrial.getType().equals("selfHit"))
            score = calculateScore(hitTrial,  true);
        logger.debug("Finished calc scores.");
        return score;
    }

    private int calculateScore(HitTrial hitTrial, boolean self) {
        int score;
        if (isSmashedNow(hitTrial.getAntId())) {
            smashedAntsRepository.put(hitTrial.getAntId(), hitTrial.getPlayerId());
            score = (self) ? valueOf(getenv("FIRST_SELF_HIT")) : valueOf(getenv("FIRST_HIT"));
            logger.debug("firstHit event:{}" , hitTrial.getPlayerId());
        } else {
            score = (self) ? valueOf(getenv("SELF_HIT")) : valueOf(getenv("HIT"));
            logger.debug("hit event:{}" , hitTrial.getPlayerId());
        }
        return score;
    }

    private boolean isSmashedNow(String antId) {
        return !(smashedAntsRepository.isExist(antId));
    }



}
