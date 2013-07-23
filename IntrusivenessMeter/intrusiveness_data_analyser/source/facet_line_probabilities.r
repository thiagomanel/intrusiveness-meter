require("ggplot2")

args <- commandArgs()
source_file_cpu <- args[4] 
source_file_memory <- args[5]

height = 1000
width = 1000
res = 150

cpu_data <- read.csv(source_file_cpu, sep = ",", strip.white=TRUE)
memory_data <- read.csv(source_file_memory, sep = ",", strip.white=TRUE)

plot_cpu <- ggplot(cpu_data, aes(x=cpu, y=probability))
plot_cpu <- plot_cpu + geom_line(aes(colour = probability), colour = "blue")
plot_cpu <- plot_cpu + facet_grid(. ~ interval)
plot_cpu <- plot_cpu + xlab("Uso de CPU (%)")
plot_cpu <- plot_cpu + ylab("Probabilidade")
plot_cpu <- plot_cpu + ggtitle("Probabilidade de desconforto relacionado ao uso de CPU")
png("cpu_probability.png")
print(plot_cpu)

plot_memory <- ggplot(memory_data, aes(x=memory, y=probability))
plot_memory <- plot_memory + geom_line(aes(colour = probability), colour = "blue")
plot_memory <- plot_memory + facet_grid(. ~ interval)
plot_memory <- plot_memory + xlab("Uso de Memória (%)")
plot_memory <- plot_memory + ylab("Probabilidade")
plot_memory <- plot_memory + ggtitle("Probabilidade de desconforto relacionado ao uso de Memória")
png("memory_probability.png")
print(plot_memory)
