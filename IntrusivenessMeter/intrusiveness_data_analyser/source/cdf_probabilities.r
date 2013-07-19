require("ggplot2")

#
# cpu file format 
# timestamp cpu     discomfort
#           [0-100]  [false-true] 
#
# memory file format
# timestamp memory discomfort
#           [0-100]  [false-true]  
#

args <- commandArgs()
source_file_cpu <- args[4]
source_file_memory <- args[5]

cpu_data <- read.csv(source_file_cpu, sep = " ", strip.white=TRUE)
memory_data <- read.csv(source_file_memory, sep = " ", strip.white=TRUE)

discomfort_true <- subset(cpu_data, discomfort == "true", select = c(cpu))
png("cpu_cdf.png")
qplot(discomfort_true$cpu, ecdf(discomfort_true$cpu)(discomfort_true$cpu), geom="step") + scale_x_continuous("Uso de CPU (%)") + scale_y_continuous("") + ggtitle("ECDF do uso de CPU relacionado ao desconforto") + expand_limits(y = 0)

discomfort_true <- subset(memory_data, discomfort == "true", select = c(memory))
png("memory_cdf.png")
qplot(discomfort_true$memory, ecdf(discomfort_true$memory)(discomfort_true$memory), geom="step") + scale_x_continuous("Uso de Memória (%)") + scale_y_continuous("") + ggtitle("ECDF do uso de memória relacionado ao desconforto") + expand_limits(y = 0)
