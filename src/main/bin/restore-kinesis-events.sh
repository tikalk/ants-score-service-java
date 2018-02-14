#!/usr/bin/env bash

export event_uuid=`aws lambda list-event-source-mappings --function-name ants-score-service-java-dev-processHitTrialKinesisEvent | jq .EventSourceMappings[0].UUID | sed s/\"//g`
aws lambda delete-event-source-mapping --uuid $event_uuid
aws lambda create-event-source-mapping --function-name ants-score-service-java-dev-processHitTrialKinesisEvent \
--event-source  arn:aws:kinesis:us-west-2:329054710135:stream/Ants-Smashing-HitTrials \
--starting-position TRIM_HORIZON