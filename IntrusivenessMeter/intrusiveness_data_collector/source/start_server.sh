#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Intrusiveness Meter Server Starter
#
# This script starts all the programs of the Intrusiveness Meter Server
#

INTRUSIVENESS_METER_HOME=$INTRUSIVENESS_METER_HOME

CONTROLLER_SCRIPT="$INTRUSIVENESS_METER_HOME/source/controller.py"
CONTROLLER_SCRIPT_ERROR_FILE="$INTRUSIVENESS_METER_HOME/logs/controller.error"

if [ $INTRUSIVENESS_METER_HOME ]; then
	python $CONTROLLER_SCRIPT 2> $CONTROLLER_SCRIPT_ERROR_FILE &
else
	echo "INTRUSIVENESS_METER_HOME is not defined."
fi

