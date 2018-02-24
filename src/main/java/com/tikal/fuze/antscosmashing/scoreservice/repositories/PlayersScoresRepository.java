package com.tikal.fuze.antscosmashing.scoreservice.repositories;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikal.fuze.antscosmashing.scoreservice.domain.HitTrial;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder.N;
import static com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder.S;
import static com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder.if_not_exists;
import static java.util.stream.Collectors.toList;

public class PlayersScoresRepository {
    private static final Logger logger = LogManager.getLogger(PlayersScoresRepository.class);

    private DynamoDB dynamoDb;
    private String tableName;

    public PlayersScoresRepository(){
        dynamoDb=DbManager.getInstance().getDynamoDb();
        tableName =DbManager.getInstance().getPlayersScoreTableName();
    }


    private Table getTable() {
        return dynamoDb.getTable(tableName);
    }


    public void put(HitTrial hitTrial, int score){
        //put a new record with score 0 in case it doesn't exist
        getTable().updateItem(new UpdateItemSpec()
                .withPrimaryKey("playerId",hitTrial.getPlayerId())
                .withExpressionSpec(
                        new ExpressionSpecBuilder()
                                .addUpdate(N("score").set(if_not_exists("score", 0)))
                                .addUpdate(N("gameId").set(hitTrial.getGameId()))
                                .addUpdate(S("playerName").set(hitTrial.getPlayerName()))
                                .buildForUpdate())
        );

        //Add the new score
        getTable().updateItem(new UpdateItemSpec()
                .withPrimaryKey("playerId",hitTrial.getPlayerId())
                .withExpressionSpec(
                        new ExpressionSpecBuilder()
                                .addUpdate(N("score").set(N("score").plus(score))).buildForUpdate())
        );


    }



}
