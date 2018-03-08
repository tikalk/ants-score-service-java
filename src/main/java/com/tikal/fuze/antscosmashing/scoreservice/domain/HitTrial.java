package com.tikal.fuze.antscosmashing.scoreservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class HitTrial {

    private static ObjectMapper om = new ObjectMapper();

    @JsonProperty
    private String type;
    @JsonProperty
    private String antId;
    @JsonProperty
    private int playerId;
    @JsonProperty
    private String playerName;
    @JsonProperty
    private int gameId;

    @JsonProperty
    private int teamId;
    @JsonProperty
    private String teamName;

    @JsonProperty
    private int antSpeciesId;
    @JsonProperty
    private String antSpeciesName;

    @JsonProperty
    private int date;
    @JsonProperty
    private int time;

    @JsonProperty
    private int userId;

    @JsonProperty
    private int playerScore;

    @JsonProperty
    private int teamScore;

    public String getType() {
        return type;
    }

    public String getAntId() {
        return antId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getGameId() {
        return gameId;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getAntSpeciesId() {
        return antSpeciesId;
    }

    public String getAntSpeciesName() {
        return antSpeciesName;
    }

    public int getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public int getUserId() {
        return userId;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getTeamScore() {
        return teamScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
    }

    public static HitTrial createHitTrial(String json){
        try {
            return om.readValue(json, HitTrial.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        try {
            return om.writeValueAsString(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
