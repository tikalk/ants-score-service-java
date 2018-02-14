#!/usr/bin/env bash

#Drop event mapping to the stream only if it exists
export event_uuid=`aws lambda list-event-source-mappings --function-name ants-score-service-java-dev-processHitTrialKinesisEvent | jq .EventSourceMappings[0].UUID | sed s/\"//g`
if [ ! -z "$event_uuid" ] && [ "$event_uuid" != "null" ]
then
    echo "Removing event mapping $event_uuid"
    aws lambda delete-event-source-mapping --uuid $event_uuid
else
    echo "Event mapping does NOT exist - No need to remove it"
fi

aws kinesis delete-stream --stream-name Ants-Smashing-HitTrials