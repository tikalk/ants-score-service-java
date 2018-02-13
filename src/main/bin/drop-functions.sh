#!/usr/bin/env bash

aws lambda delete-function --function-name ants-score-service-java-dev-kinesisHitTrialEventsJava
aws lambda delete-function --function-name ants-score-service-java-dev-postHitTrialJava
aws lambda delete-function --function-name scores-service-node-dev-getPlayersScores
aws lambda delete-function --function-name scores-service-node-dev-getTeamsScores