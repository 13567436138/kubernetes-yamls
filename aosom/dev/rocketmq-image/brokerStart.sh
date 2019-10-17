#!/bin/bash

./brokerGenConfig.sh
./mqbroker -n $NAMESRV_ADDRESS  autoCreateTopicEnable=true
