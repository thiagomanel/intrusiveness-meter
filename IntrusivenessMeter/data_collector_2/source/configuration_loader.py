#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# The class Loader is used to load configurations from a file 
# and provide access to the loaded properties
#

class Loader:
    '''
         Creates a loader.

         Argument Description:
              conf_file_name -> the file from which the properties will be loaded
    '''
    def __init__(self, conf_file_name):
        self.conf_file = open(conf_file_name, "r")
        self.properties = {}
        self.separator = "="        

        for line in self.conf_file.readlines():
            if not line.startswith("#") and not line == "\n":
                tokens = line.split(self.separator)
                key = tokens[0]
                value = tokens[1]
                self.properties[key] = value
        
        
    def has_property(self, property):
        return self.properties.has_key(property)

    def get_property(self, property):
        return self.properties[property]
            
