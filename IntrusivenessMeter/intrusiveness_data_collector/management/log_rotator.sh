#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Log Rotator Script
#
# This script copies the logs and the results to the given directory.
# The copied files be put in a directory whose name is the current time.
#
# Usage:
# bash log_rotator.sh ROTATION_DIRECTORY
# 
# Parameters:
# ROTATION_DIRECTORY: The directory where the copied files will be placed.
#

LOGS_DIRECTORY="$INTRUSIVENESS_METER_HOME/logs"
RESULTS_DIRECTORY="$INTRUSIVENESS_METER_HOME/results"
ROTATION_DIRECTORY="$1"

CURRENT_DATE="`date "+%d-%m-%Y-%H-%M-%S"`"
NEW_DATA_DIRECTORY=$ROTATION_DIRECTORY/$CURRENT_DATE

mkdir $NEW_DATA_DIRECTORY
cp -r $LOGS_DIRECTORY $NEW_DATA_DIRECTORY
cp -r $RESULTS_DIRECTORY $NEW_DATA_DIRECTORY
