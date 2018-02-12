#!/usr/bin/env bash

export uuid=`aws lambda list-event-source-mappings --function-name ants-score-service-java-dev-kinesisHitTrialEventsJava | jq .EventSourceMappings[0].UUID | sed s/\"//g`
echo event_uuid is $event_uuid
if [ ! -z "$event_uuid" ]
then
    echo "Removing event mapping $event_uuid"
    aws lambda delete-event-source-mapping --uuid $event_uuid
fi

aws kinesis delete-stream --stream-name Ants-Smashing-HitTrials