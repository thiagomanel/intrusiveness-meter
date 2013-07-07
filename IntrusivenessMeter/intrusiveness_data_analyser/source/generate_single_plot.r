require("ggplot2")
require("reshape")

args <- commandArgs()
DATA_FILE_NAME <- args[4]
# in KB
machine_total_memory <- as.numeric(args[5])
number_of_CPUs <- as.numeric(args[6])
data <- read.csv(DATA_FILE_NAME, strip.white=TRUE)

# filter results by related_discomfort
filtered_by_hadoop_cpu_usage <- subset(data, related_discomfort == "true", select = c(hadoop_cpu_usage))$hadoop_cpu_usage
filtered_by_hadoop_memory_usage <- subset(data, related_discomfort == "true", select = c(hadoop_memory_usage))$hadoop_memory_usage
filtered_by_system_idle_cpu <- subset(data, related_discomfort == "true", select = c(system_idle_cpu))$system_idle_cpu
filtered_by_system_user_cpu <- subset(data, related_discomfort == "true", select = c(system_user_cpu))$system_user_cpu
filtered_by_system_memory <- subset(data, related_discomfort == "true", select = c(system_memory))$system_memory
filtered_by_system_read_number <- subset(data, related_discomfort == "true", select = c(system_read_number))$system_read_number
filtered_by_system_read_sectors <- subset(data, related_discomfort == "true", select = c(system_read_sectors))$system_read_sectors
filtered_by_system_write_number <- subset(data, related_discomfort == "true", select = c(system_write_number))$system_write_number
filtered_by_system_write_attempts <- subset(data, related_discomfort == "true", select = c(system_write_attempts))$system_write_attempts

# gets numeric data from the read data
execution_hadoop_cpu_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_cpu_usage[1]), " ")[[1]]))/number_of_CPUs
execution_hadoop_memory_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_memory_usage[1]), " ")[[1]]))*100/machine_total_memory
execution_system_idle_cpu <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_idle_cpu[1]), " ")[[1]]))
execution_system_user_cpu <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_user_cpu[1]), " ")[[1]]))
execution_system_memory <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_memory[1]), " ")[[1]]))
execution_system_read_number <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_read_number[1]), " ")[[1]]))
execution_system_read_sectors <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_read_sectors[1]), " ")[[1]]))
execution_system_write_number <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_write_number[1]), " ")[[1]]))
execution_system_write_attempts <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_write_attempts[1]), " ")[[1]]))

# creates an adequate dataframe with the data
hadoop_cpu_usage_final_dataframe <- data.frame(c(1:nrow(execution_hadoop_cpu_usage)), execution_hadoop_cpu_usage, rep("1", nrow(execution_hadoop_cpu_usage)))
hadoop_memory_usage_final_dataframe <- data.frame(c(1:nrow(execution_hadoop_memory_usage)), execution_hadoop_memory_usage, rep("1", nrow(execution_hadoop_memory_usage)))
system_idle_cpu_final_dataframe <- data.frame(c(1:nrow(execution_system_idle_cpu)), execution_system_idle_cpu, rep("1", nrow(execution_system_idle_cpu)))
system_user_cpu_final_dataframe <- data.frame(c(1:nrow(execution_system_user_cpu)), execution_system_user_cpu, rep("1", nrow(execution_system_user_cpu)))
system_memory_final_dataframe <- data.frame(c(1:nrow(execution_system_memory)), execution_system_memory, rep("1", nrow(execution_system_memory)))
system_read_number_final_dataframe <- data.frame(c(1:nrow(execution_system_read_number)), execution_system_read_number, rep("1", nrow(execution_system_read_number)))
system_read_sectors_final_dataframe <- data.frame(c(1:nrow(execution_system_read_sectors)), execution_system_read_sectors, rep("1", nrow(execution_system_read_sectors)))
system_write_number_final_dataframe <- data.frame(c(1:nrow(execution_system_write_number)), execution_system_write_number, rep("1", nrow(execution_system_write_number)))
system_write_attempts_final_dataframe <- data.frame(c(1:nrow(execution_system_write_attempts)), execution_system_write_attempts, rep("1", nrow(execution_system_write_attempts)))

colnames(hadoop_cpu_usage_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(hadoop_memory_usage_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(system_idle_cpu_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(system_user_cpu_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(system_memory_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(system_read_number_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(system_read_sectors_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(system_write_number_final_dataframe) <- c('X0', 'X1', 'X2')
colnames(system_write_attempts_final_dataframe) <- c('X0', 'X1', 'X2')

for (i in 2:length(filtered_by_hadoop_cpu_usage)) {
	execution_hadoop_cpu_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_cpu_usage[i]), " ")[[1]]))/number_of_CPUs
	hadoop_cpu_usage_next_dataframe <- data.frame(c(1:nrow(execution_hadoop_cpu_usage)), execution_hadoop_cpu_usage, rep(as.character(i), nrow(execution_hadoop_cpu_usage)))
	colnames(hadoop_cpu_usage_next_dataframe) <- c('X0', 'X1', 'X2')
	hadoop_cpu_usage_final_dataframe <-rbind(hadoop_cpu_usage_final_dataframe, hadoop_cpu_usage_next_dataframe) 

	execution_hadoop_memory_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_memory_usage[i]), " ")[[1]]))*100/machine_total_memory
	hadoop_memory_usage_next_dataframe <- data.frame(c(1:nrow(execution_hadoop_memory_usage)), execution_hadoop_memory_usage, rep(as.character(i), nrow(execution_hadoop_memory_usage)))
	colnames(hadoop_memory_usage_next_dataframe) <- c('X0', 'X1', 'X2')
	hadoop_memory_usage_final_dataframe <-rbind(hadoop_memory_usage_final_dataframe, hadoop_memory_usage_next_dataframe) 

	execution_system_idle_cpu <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_idle_cpu[i]), " ")[[1]]))
	system_idle_cpu_next_dataframe <- data.frame(c(1:nrow(execution_system_idle_cpu)), execution_system_idle_cpu, rep(as.character(i), nrow(execution_system_idle_cpu)))
	colnames(system_idle_cpu_next_dataframe) <- c('X0', 'X1', 'X2')
	system_idle_cpu_final_dataframe <-rbind(system_idle_cpu_final_dataframe, system_idle_cpu_next_dataframe) 

	execution_system_user_cpu <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_user_cpu[i]), " ")[[1]]))
	system_user_cpu_next_dataframe <- data.frame(c(1:nrow(execution_system_user_cpu)), execution_system_user_cpu, rep(as.character(i), nrow(execution_system_user_cpu)))
	colnames(system_user_cpu_next_dataframe) <- c('X0', 'X1', 'X2')
	system_user_cpu_final_dataframe <-rbind(system_user_cpu_final_dataframe, system_user_cpu_next_dataframe) 

	execution_system_memory <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_memory[i]), " ")[[1]]))
	system_memory_next_dataframe <- data.frame(c(1:nrow(execution_system_memory)), execution_system_memory, rep(as.character(i), nrow(execution_system_memory)))
	colnames(system_memory_next_dataframe) <- c('X0', 'X1', 'X2')
	system_memory_final_dataframe <-rbind(system_memory_final_dataframe, system_memory_next_dataframe) 

	execution_system_read_number <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_read_number[i]), " ")[[1]]))
	system_read_number_next_dataframe <- data.frame(c(1:nrow(execution_system_read_number)), execution_system_read_number, rep(as.character(i), nrow(execution_system_read_number)))
	colnames(system_read_number_next_dataframe) <- c('X0', 'X1', 'X2')
	system_read_number_final_dataframe <-rbind(system_read_number_final_dataframe, system_read_number_next_dataframe) 

	execution_system_read_sectors <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_read_sectors[i]), " ")[[1]]))
	system_read_sectors_next_dataframe <- data.frame(c(1:nrow(execution_system_read_sectors)), execution_system_read_sectors, rep(as.character(i), nrow(execution_system_read_sectors)))
	colnames(system_read_sectors_next_dataframe) <- c('X0', 'X1', 'X2')
	system_read_sectors_final_dataframe <-rbind(system_read_sectors_final_dataframe, system_read_sectors_next_dataframe) 

	execution_system_read_sectors <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_read_sectors[i]), " ")[[1]]))
	system_read_sectors_next_dataframe <- data.frame(c(1:nrow(execution_system_read_sectors)), execution_system_read_sectors, rep(as.character(i), nrow(execution_system_read_sectors)))
	colnames(system_read_sectors_next_dataframe) <- c('X0', 'X1', 'X2')
	system_read_sectors_final_dataframe <-rbind(system_read_sectors_final_dataframe, system_read_sectors_next_dataframe) 

	execution_system_write_number <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_write_number[i]), " ")[[1]]))
	system_write_number_next_dataframe <- data.frame(c(1:nrow(execution_system_write_number)), execution_system_write_number, rep(as.character(i), nrow(execution_system_write_number)))
	colnames(system_write_number_next_dataframe) <- c('X0', 'X1', 'X2')
	system_write_number_final_dataframe <-rbind(system_write_number_final_dataframe, system_write_number_next_dataframe) 

	execution_system_write_attempts <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_write_number[i]), " ")[[1]]))
	system_write_attempts_next_dataframe <- data.frame(c(1:nrow(execution_system_write_attempts)), execution_system_write_attempts, rep(as.character(i), nrow(execution_system_write_attempts)))
	colnames(system_write_attempts_next_dataframe) <- c('X0', 'X1', 'X2')
	system_write_attempts_final_dataframe <-rbind(system_write_attempts_final_dataframe, system_write_attempts_next_dataframe) 
}

png("hadoop_cpu.png", width=1000, height=1000, res=120)
plot <- ggplot(hadoop_cpu_usage_final_dataframe, aes(x=hadoop_cpu_usage_final_dataframe$X0, y=hadoop_cpu_usage_final_dataframe$X1, colour=hadoop_cpu_usage_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Consumo de CPU pelo Hadoop %")
print(plot)

png("hadoop_memory.png", width=1000, height=1000, res=120)
plot <- ggplot(hadoop_memory_usage_final_dataframe, aes(x=hadoop_memory_usage_final_dataframe$X0, y=hadoop_memory_usage_final_dataframe$X1, colour=hadoop_memory_usage_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Consumo de Memória pelo Hadoop %")
print(plot)

png("system_idle_cpu.png", width=1000, height=1000, res=120)
plot <- ggplot(system_idle_cpu_final_dataframe, aes(x=system_idle_cpu_final_dataframe$X0, y=system_idle_cpu_final_dataframe$X1, colour=system_idle_cpu_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("CPU ociosa %")
print(plot)

png("system_user_cpu.png", width=1000, height=1000, res=120)
plot <- ggplot(system_user_cpu_final_dataframe, aes(x=system_user_cpu_final_dataframe$X0, y=system_user_cpu_final_dataframe$X1, colour=system_user_cpu_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Consumo de CPU pelo usuário %")
print(plot)

png("system_memory.png", width=1000, height=1000, res=120)
plot <- ggplot(system_memory_final_dataframe, aes(x=system_memory_final_dataframe$X0, y=system_memory_final_dataframe$X1, colour=system_memory_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Consumo de Memória %")
print(plot)

png("system_read_number.png", width=1000, height=1000, res=120)
plot <- ggplot(system_read_number_final_dataframe, aes(x=system_read_number_final_dataframe$X0, y=system_read_number_final_dataframe$X1, colour=system_read_number_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Número de Leituras")
print(plot)

png("system_read_sectors.png", width=1000, height=1000, res=120)
plot <- ggplot(system_read_sectors_final_dataframe, aes(x=system_read_sectors_final_dataframe$X0, y=system_read_sectors_final_dataframe$X1, colour=system_read_sectors_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Read Sectors")
print(plot)

png("system_write_number.png", width=1000, height=1000, res=120)
plot <- ggplot(system_write_number_final_dataframe, aes(x=system_write_number_final_dataframe$X0, y=system_write_number_final_dataframe$X1, colour=system_write_number_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Número de Escritas")
print(plot)

png("system_write_attempts.png", width=1000, height=1000, res=120)
plot <- ggplot(system_write_attempts_final_dataframe, aes(x=system_write_attempts_final_dataframe$X0, y=system_write_attempts_final_dataframe$X1, colour=system_write_attempts_final_dataframe$X2)) + geom_line()
plot <- plot + xlab("Tempo")
plot <- plot + ylab("Write Attempts")
print(plot)
