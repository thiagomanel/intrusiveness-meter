#!/bin/bash

ROOT=$1

SOURCE_DIRECTORY="$1/source"
CONF_DIRECTORY="$1/conf"
RESULTS_DIRECTORY="$1/results"

zip -r intrusiveness_meter.zip $SOURCE_DIRECTORY $CONF_DIRECTORY $RESULTS_DIRECTORY
