require("ggplot2")
require("reshape")

args <- commandArgs()
DATA_FILE_NAME <- args[4]
data <- read.csv(DATA_FILE_NAME, strip.white=TRUE)

filtered_by_hadoop_cpu_usage <- subset(data, related_discomfort == "true", select = c(hadoop_cpu_usage))$hadoop_cpu_usage
filtered_by_hadoop_memory_usage <- subset(data, related_discomfort == "true", select = c(hadoop_memory_usage))$hadoop_memory_usage

execution_hadoop_cpu_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_cpu_usage[1]), " ")[[1]]))
execution_hadoop_memory_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_memory_usage[1]), " ")[[1]]))

hadoop_cpu_usage_final_dataframe <- data.frame(c(1:nrow(execution_hadoop_cpu_usage)), execution_hadoop_cpu_usage, rep("1", nrow(execution_hadoop_cpu_usage)))
hadoop_memory_usage_final_dataframe <- data.frame(c(1:nrow(execution_hadoop_memory_usage)), execution_hadoop_memory_usage, rep("1", nrow(execution_hadoop_memory_usage)))

colnames(hadoop_cpu_usage_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(hadoop_memory_usage_final_dataframe) <- c('X0', 'X1', 'X2')

for (i in 2:length(filtered_by_hadoop_cpu_usage)) {
	execution_hadoop_cpu_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_cpu_usage[i]), " ")[[1]]))
	hadoop_cpu_usage_next_dataframe <- data.frame(c(1:nrow(execution_hadoop_cpu_usage)), execution_hadoop_cpu_usage, rep(as.character(i), nrow(execution_hadoop_cpu_usage)))
	colnames(hadoop_cpu_usage_next_dataframe) <- c('X0', 'X1', 'X2')
	hadoop_cpu_usage_final_dataframe <-rbind(hadoop_cpu_usage_final_dataframe, hadoop_cpu_usage_next_dataframe) 

	execution_hadoop_memory_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_memory_usage[i]), " ")[[1]]))
	hadoop_memory_usage_next_dataframe <- data.frame(c(1:nrow(execution_hadoop_memory_usage)), execution_hadoop_memory_usage, rep(as.character(i), nrow(execution_hadoop_memory_usage)))
	colnames(hadoop_memory_usage_next_dataframe) <- c('X0', 'X1', 'X2')
	hadoop_memory_usage_final_dataframe <-rbind(hadoop_memory_usage_final_dataframe, hadoop_memory_usage_next_dataframe) 
}

png("hadoop_cpu.png", width=1000, height=1000, res=120)
plot <- ggplot(hadoop_cpu_usage_final_dataframe, aes(x=hadoop_cpu_usage_final_dataframe$X0, y=hadoop_cpu_usage_final_dataframe$X1, colour=hadoop_cpu_usage_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Consumo de CPU")
print(plot)

png("hadoop_memory.png", width=1000, height=1000, res=120)
plot <- ggplot(hadoop_memory_usage_final_dataframe, aes(x=hadoop_memory_usage_final_dataframe$X0, y=hadoop_memory_usage_final_dataframe$X1, colour=hadoop_memory_usage_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Consumo de Memória")
print(plot)
