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
png("cpu_histograms.png")
qplot(cpu, data=discomfort_true, geom="histogram", binwidth=10)

discomfort_true <- subset(memory_data, discomfort == "true", select = c(memory))
png("memory_histograms.png")
qplot(memory, data=discomfort_true, geom="histogram", binwidth=10)
