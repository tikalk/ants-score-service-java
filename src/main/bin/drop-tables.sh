#!/usr/bin/env bash

aws dynamodb delete-table --table-name Ants-Smashing-PlayersScore
aws dynamodb delete-table --table-name Ants-Smashing-TeamsScore
aws dynamodb delete-table --table-name Ants-Smashing-smashedAnts
