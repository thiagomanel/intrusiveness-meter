#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Intrusiveness Meter Client Stopper
#
# This script stops the client programs in the machines
# whose names are stored in the clients configuration file
#

INTRUSIVENESS_METER_HOME=$INTRUSIVENESS_METER_HOME

CONF_DIRECTORY="$INTRUSIVENESS_METER_HOME/conf"
CLIENTS_FILE="$CONF_DIRECTORY/clients"

CLIENTS="`cat $CLIENTS_FILE`"

for HOST in $CLIENTS; do
        ssh $HOST "$INTRUSIVENESS_METER_HOME/source/stop_client.sh"
done
