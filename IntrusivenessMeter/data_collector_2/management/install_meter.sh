#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Intrusiveness Meter Quick Installer
# 
# This program copies the Intrusiveness Meter distribution to
# the specified machine and directory and unzips it.
#

INSTALLATION_HOME=$1
HOST=$2
METER_DISTRIBUTION="intrusiveness_meter.zip"

scp $METER_DISTRIBUTION $HOST:$INSTALLATION_HOME
ssh $HOST "unzip $INSTALLATION_HOME/$METER_DISTRIBUTION -d $INSTALLATION_HOME"
