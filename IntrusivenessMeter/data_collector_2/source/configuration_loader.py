class Loader:
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
            
