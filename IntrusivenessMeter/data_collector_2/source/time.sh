#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Time Script
#
# It's used to synchronise the time format of the logs
#

echo -n "`date "+%d-%m-%Y-%H-%M-%S"`" "`date "+%s%N"` "
