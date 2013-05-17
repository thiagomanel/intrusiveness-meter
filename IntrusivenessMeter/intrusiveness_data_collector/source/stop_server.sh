#!/bin/bash

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Intrusiveness Meter Server Stopper
#
# This script stops all the programs of the Intrusiveness Meter Server
#

PID="`ps xau | grep controller.py | awk '{ print $2}'`"
kill $PID
