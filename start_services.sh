#!/bin/bash -e

sm2 --start PHONE_NUMBER_INSIGHTS_PROXY PHONE_NUMBER_INSIGHTS PHONE_NUMBER_GATEWAY CIP_RISK INTERNAL_AUTH --appendArgs '{
        "PHONE_NUMBER_INSIGHTS_PROXY": [
            "-J-Dauditing.consumer.baseUri.port=6001",
            "-J-Dauditing.consumer.baseUri.host=localhost",
            "-J-Dauditing.enabled=false",
            "-J-Dmicroservice.services.access-control.enabled=true",
            "-J-Dmicroservice.services.access-control.allow-list.0=phone-number-gateway",
            "-J-Dmicroservice.services.access-control.allow-list.1=phone-number-insights-acceptance-tests"
        ],
        "PHONE_NUMBER_INSIGHTS": [
            "-J-Dapplication.router=testOnlyDoNotUseInAppConf.Routes",
            "-J-Dmicroservice.phone-number-insights.database.dbName=postgres:postgres",
            "-J-Dauditing.enabled=false"
        ],
        "CIP_RISK": [
            "-J-Dauditing.enabled=false"
        ]
    }'
