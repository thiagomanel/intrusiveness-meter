#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program removes the header and tail of the result files 
# generated by the scripts of data_collector, so the data can be
# used as input for programs like R, etc.
# The user must pass the data as the standard input and it will 
# print the data with the header and the tail removed.
#

import sys

lines=sys.stdin.readlines()
lines=lines[3:-1]

for line in lines:
    print line,
