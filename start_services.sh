#!/bin/bash -e

sm2 --start PHONE_NUMBER_INSIGHTS_PROXY PHONE_NUMBER_INSIGHTS PHONE_NUMBER_GATEWAY DATASTREAM CIP_RISK INTERNAL_AUTH --appendArgs '{
        "PHONE_NUMBER_INSIGHTS_PROXY": [
            "-J-Dauditing.enabled=true",
            "-J-Dmicroservice.services.access-control.enabled=true",
            "-J-Dmicroservice.services.access-control.allow-list.0=phone-number-gateway",
            "-J-Dmicroservice.services.access-control.allow-list.1=phone-number-insights-acceptance-tests"
        ],
        "PHONE_NUMBER_INSIGHTS": [
            "-J-Dapplication.router=testOnlyDoNotUseInAppConf.Routes",
            "-J-Ddb.phonenumberinsights.url=jdbc:postgresql://localhost:5432/",
            "-J-Dauditing.enabled=true"
        ],
        "CIP_RISK": [
            "-J-Dauditing.enabled=true"
        ]
    }'
