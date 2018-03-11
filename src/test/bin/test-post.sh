#!/usr/bin/env bash
sls invoke -f postHitTrial --data '{"body":"{ \"type\":\"hit\", \"antId\":\"11122\", \"playerId\":10, \"playerName\":\"yanai\",\"gameId\":6, \"userId\":5, \"teamId\":7, \"teamName\":\"Data\", \"antSpeciesId\":3, \"antSpeciesName\":\"Mirmica\", \"date\":20180307, \"time\":161612 }"}'

