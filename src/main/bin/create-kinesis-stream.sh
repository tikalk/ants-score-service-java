#!/usr/bin/env bash

aws kinesis create-stream --stream-name Ants-Smashing-HitTrials --shard-count 2


export func_config=`aws lambda get-function --function-name ants-score-service-java-dev-processHitTrialKinesisEvent`
export event_uuid=`aws lambda list-event-source-mappings --function-name ants-score-service-java-dev-processHitTrialKinesisEvent | jq .EventSourceMappings[0].UUID | sed s/\"//g`

#Create event mapping to the stream only if the function exists and there is no event mapping for the function
if [ ! -z "$func_config" ] && [ "$func_config" != "null" ]
then
    echo "Function exists - We will check if there is an event mapping for it..."
    if [ -z "$event_uuid" ] || [ "$event_uuid" == "null" ]
    then
        echo "Creating event mapping"
        echo "Sleeping for 30 sec, let the strem to be created, before we create the mapping..."
        sleep 30
        aws lambda create-event-source-mapping --function-name ants-score-service-java-dev-processHitTrialKinesisEvent --event-source  arn:aws:kinesis:us-west-2:329054710135:stream/Ants-Smashing-HitTrials --starting-position LATEST
    else
        echo "Event mapping exists. Will not create it"
    fi
else
    echo "Function does NOT exist - We will NOT create event mapping"
fi
