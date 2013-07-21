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

height = 1000
width = 1000
res = 150

cpu_data <- read.csv(source_file_cpu, sep = " ", strip.white=TRUE)
memory_data <- read.csv(source_file_memory, sep = " ", strip.white=TRUE)

discomfort_true <- subset(cpu_data, discomfort == "true", select = c(cpu, interval))
png("cpu_facet_grid_histograms.png", height = height, width = width, res = res)
plot <- ggplot(discomfort_true, aes(cpu, fill = ..count..)) + geom_histogram(binwidth=10)
plot <- plot + facet_grid(. ~ interval)
plot <- plot + xlab("Uso de CPU (%)") + ylab("Ocorrências")
plot <- plot + ggtitle("Uso de CPU associado ao desconforto")
print(plot)

discomfort_true <- subset(memory_data, discomfort == "true", select = c(memory, interval))
png("memory_facet_grid_histograms.png", height = height, width = width, res = res)
plot <- ggplot(discomfort_true, aes(memory, fill = ..count..)) + geom_histogram(binwidth=10)
plot <- plot + facet_grid(. ~ interval)
plot <- plot + xlab("Uso de Memória (%)") + ylab("Ocorrências")
plot <- plot + ggtitle("Uso de Memória associado ao desconforto")
print(plot)
