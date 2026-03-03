#!/bin/bash -e

sm2 --start PHONE_NUMBER_INSIGHTS_PROXY PHONE_NUMBER_INSIGHTS PHONE_NUMBER_GATEWAY DATASTREAM CIP_RISK INTERNAL_AUTH --appendArgs '{
        "PHONE_NUMBER_INSIGHTS_PROXY": [
            "-Dauditing.consumer.baseUri.port=6001",
            "-Dauditing.consumer.baseUri.host=localhost",
            "-Dauditing.enabled=false",
            "-Dmicroservice.services.access-control.enabled=true",
            "-Dmicroservice.services.access-control.allow-list.0=phone-number-gateway",
            "-Dmicroservice.services.access-control.allow-list.1=phone-number-insights-acceptance-tests"
        ],
        "PHONE_NUMBER_INSIGHTS": [
            "-Dapplication.router=testOnlyDoNotUseInAppConf.Routes",
            "-Ddb.phonenumberinsights.url=jdbc:postgresql://localhost:5432/",
            "-Dplay.evolutions.db.phonenumberinsights.autoApplyDowns=true",
            "-Dauditing.enabled=false"
        ],
        "CIP_RISK": [
            "-Dapplication.router=testOnlyDoNotUseInAppConf.Routes",
            "-Dplay.evolutions.db.risk.autoApplyDowns=true",
            "-Dauditing.enabled=false"
        ]
    }'
