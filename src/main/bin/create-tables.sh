#!/usr/bin/env bash

aws dynamodb create-table --table-name Ants-Smashing-PlayersScore \
--attribute-definitions AttributeName=playerId,AttributeType=N AttributeName=score,AttributeType=N AttributeName=gameId,AttributeType=N \
--key-schema AttributeName=playerId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
--global-secondary-indexes 'IndexName=gameId_score_idx,KeySchema=[{AttributeName=gameId,KeyType=HASH},{AttributeName=score,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}'


aws dynamodb create-table --table-name Ants-Smashing-TeamsScore \
--attribute-definitions AttributeName=teamId,AttributeType=N AttributeName=score,AttributeType=N AttributeName=gameId,AttributeType=N \
--key-schema AttributeName=teamId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
--global-secondary-indexes 'IndexName=gameId_score_idx,KeySchema=[{AttributeName=gameId,KeyType=HASH},{AttributeName=score,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}'


aws dynamodb create-table --table-name Ants-Smashing-smashedAnts \
--attribute-definitions AttributeName=antId,AttributeType=S \
--key-schema AttributeName=antId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5