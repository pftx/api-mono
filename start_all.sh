#!/bin/bash
# This is the main script the start all services.
#     author: Lex Xie
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Usage:
#     ./start_all.sh
#
###############################################################################

SLEEP_TIME=15

mvn clean install
./service.sh install

./service.sh config start
sleep $SLEEP_TIME

./service.sh eureka start
sleep $SLEEP_TIME

./service.sh auth start
sleep $SLEEP_TIME

./service.sh account start
sleep $SLEEP_TIME

./service.sh gateway start
