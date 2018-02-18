package com.tikal.fuze.antscosmashing.scoreservice.repositories;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;

import java.time.Instant;

public class GamesRepository {

    private DynamoDB dynamoDb;
    private String tableName;

    public GamesRepository(){
        dynamoDb=DbManager.getInstance().getDynamoDb();
        tableName =DbManager.getInstance().getGamesTableName();
    }


    public void put(int gameId){
        dynamoDb.getTable(tableName)
                .putItem(new PutItemSpec().withItem(new Item()
                        .withInt("gameId", gameId)
                        .withLong("timestamp", Instant.now().toEpochMilli())));
    }

}
