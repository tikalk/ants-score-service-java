package com.tikal.fuze.antscosmashing.scoreservice.repositories;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikal.fuze.antscosmashing.scoreservice.domain.HitTrial;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder.N;
import static com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder.S;
import static com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder.if_not_exists;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static java.util.stream.Collectors.toList;

public class TeamsScoresRepository {
    private static final Logger logger = LogManager.getLogger(TeamsScoresRepository.class);

    private DynamoDB dynamoDb;
    private String tableName;
    private String hashKeyName="teamId";


    public TeamsScoresRepository(){
        dynamoDb=DbManager.getInstance().getDynamoDb();
        tableName =DbManager.getInstance().getTeamsScoreTableName();
    }


    private Table getTable() {
        return dynamoDb.getTable(tableName);
    }


    public void put(HitTrial hitTrial, int score) {
        Item item = getTable().getItem(hashKeyName, hitTrial.getTeamId());
        if (item == null){
            //put a new record with score 0 in case it doesn't exist
            getTable().updateItem(new UpdateItemSpec()
                    .withPrimaryKey("teamId", hitTrial.getTeamId())
                    .withExpressionSpec(
                            new ExpressionSpecBuilder()
                                    .addUpdate(N("score").set(if_not_exists("score", 0)))
                                    .addUpdate(N("gameId").set(hitTrial.getGameId()))
                                    .addUpdate(S("teamName").set(hitTrial.getTeamName()))
                                    .addUpdate(N("antSpeciesId").set(hitTrial.getAntSpeciesId()))
                                    .addUpdate(S("antSpeciesName").set(hitTrial.getAntSpeciesName()))
                                    .addUpdate(N("updateDate").set(hitTrial.getDate()))
                                    .addUpdate(N("updateTime").set(hitTrial.getTime()))
                                    .buildForUpdate())
            );
        }

        //Add the new score
        getTable().updateItem(new UpdateItemSpec()
                .withPrimaryKey("teamId",hitTrial.getTeamId())
                .withExpressionSpec(
                        new ExpressionSpecBuilder()
                                .addUpdate(N("score").set(N("score").plus(score)))
                                .addUpdate(N("updateDate").set(hitTrial.getDate()))
                                .addUpdate(N("updateTime").set(hitTrial.getTime()))
                                .buildForUpdate())
        );


    }




    public int getScore(int teamId) {
        Item item = getTable().getItem(hashKeyName, teamId);
        if(item==null)
            return 0;
        return item.getInt("score");
    }
}
