#!/usr/bin/env bash

aws dynamodb create-table --table-name Ants-Smashing-PlayersScore \
--attribute-definitions AttributeName=playerId,AttributeType=N AttributeName=score,AttributeType=N AttributeName=gameId,AttributeType=N \
--key-schema AttributeName=playerId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=10,WriteCapacityUnits=10 \
--global-secondary-indexes 'IndexName=gameId_score_idx,KeySchema=[{AttributeName=gameId,KeyType=HASH},{AttributeName=score,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=10,WriteCapacityUnits=10}'

aws dynamodb create-table --table-name Ants-Smashing-TeamsScore \
--attribute-definitions AttributeName=teamId,AttributeType=N AttributeName=score,AttributeType=N AttributeName=gameId,AttributeType=N AttributeName=updateDate,AttributeType=N AttributeName=updateTime,AttributeType=N \
--key-schema AttributeName=teamId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=10,WriteCapacityUnits=10 \
--global-secondary-indexes '[
       { "IndexName":"gameId_score_idx", "KeySchema":[ { "AttributeName":"gameId", "KeyType":"HASH" }, { "AttributeName":"score", "KeyType":"RANGE" } ], "Projection":{ "ProjectionType":"ALL" }, "ProvisionedThroughput":{ "ReadCapacityUnits":10, "WriteCapacityUnits":10 } },
       { "IndexName":"date_time_idx", "KeySchema":[ { "AttributeName":"updateDate", "KeyType":"HASH" }, { "AttributeName":"updateTime", "KeyType":"RANGE" } ], "Projection":{ "ProjectionType":"ALL" }, "ProvisionedThroughput":{ "ReadCapacityUnits":10, "WriteCapacityUnits":10 } }
]'

aws dynamodb create-table --table-name Ants-Smashing-smashedAnts \
--attribute-definitions AttributeName=antId,AttributeType=S \
--key-schema AttributeName=antId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=10,WriteCapacityUnits=10
