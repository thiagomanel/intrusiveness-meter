#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

import random

GENERATE_LOWER_LIMIT = 0
GENERATE_HIGHER_LIMIT = 2147483647

class IDGenerator:
    '''
       This class provides methods for randomic identifiers generation
    '''
    def generate(self):
        '''
          Generates a string containing a random identifier
        '''
        return str(random.randint(GENERATE_LOWER_LIMIT, GENERATE_HIGHER_LIMIT)) 
