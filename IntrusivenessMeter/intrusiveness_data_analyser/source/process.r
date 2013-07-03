#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program gets a result.csv file generated by the MainAnalyser and
# produces some graphics from it.
#

library("ggplot2")

DATA_FILE_NAME <- "../result.csv"
data <- read.csv(DATA_FILE_NAME, strip.white=TRUE)
benchmarks <- subset(data, related_discomfort == "true", select = c(benchmark))

filtered_by_hadoop_cpu_usage <- subset(data, related_discomfort == "true", select = c(hadoop_cpu_usage))$hadoop_cpu_usage
filtered_by_hadoop_memory_usage <- subset(data, related_discomfort == "true", select = c(hadoop_memory_usage))$hadoop_memory_usage
filtered_by_system_idle_cpu <- subset(data, related_discomfort == "true", 
select = c(system_idle_cpu))$system_idle_cpu
filtered_by_system_user_cpu <- subset(data, related_discomfort == "true", 
select = c(system_user_cpu))$system_user_cpu
filtered_by_system_memory <- subset(data, related_discomfort == "true", 
select = c(system_memory))$system_memory
filtered_by_system_read_number <- subset(data, related_discomfort == "true", 
select = c(system_read_number))$system_read_number
filtered_by_system_read_sectors <- subset(data, related_discomfort == "true", 
select = c(system_read_sectors))$system_read_sectors
filtered_by_system_write_number <- subset(data, related_discomfort == "true", 
select = c(system_write_number))$system_write_number
filtered_by_system_write_attempts <- subset(data, related_discomfort == "true", select = c(system_write_attempts))$system_write_attempts

png_image <- function(subject, index) {
	image_name <- paste(subject, "_", index, ".png", sep="")
	png(file=image_name)
}

cpu_image <- function(index) {
	png_image("hadoop_cpu", index)
}

memory_image <- function(index) {
	png_image("memory_image", index)
}

system_idle_cpu_image <- function(index) {
	png_image("system_idle_cpu", index)
}

system_user_cpu_image <- function(index) {
	png_image("system_user_cpu", index)
}

system_memory_image <- function(index) {
	png_image("system_memory", index)
}

system_read_number_image <- function(index) {
	png_image("system_read_number", index)
}

system_read_sectors_image <- function(index) {
	png_image("system_read_sectors", index)
}

system_write_number_image <- function(index) {
	png_image("system_write_number", index)
}

system_write_attempts_image <- function(index) {
	png_image("system_write_attempts", index)
}

print_dataframe <- function(plot, plot_name, benchmark) {
	obj <- plot + geom_line() + ggtitle(sprintf("%s:%s", plot_name, benchmark))
	print(obj)
}

# TODO add a division in the cpu usage data by the machines cpus
for (i in 1:length(filtered_by_hadoop_cpu_usage)) {
	# get a data frame with the hadoop cpu usage
	execution_hadoop_cpu_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_cpu_usage[i]), " ")[[1]]))
	execution_hadoop_memory_usage <- data.frame(as.numeric(strsplit(as.character(filtered_by_hadoop_memory_usage[i]), " ")[[1]]))	
	execution_system_idle_cpu <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_idle_cpu[i]), " ")[[1]]))
	execution_system_user_cpu <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_user_cpu[i]), " ")[[1]]))
	execution_system_memory <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_memory[i]), " ")[[1]]))
	execution_system_read_number <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_read_number[i]), " ")[[1]]))
	execution_system_read_sectors <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_read_sectors[i]), " ")[[1]]))
	execution_system_write_number <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_write_number[i]), " ")[[1]]))
	execution_system_write_attempts <- data.frame(as.numeric(strsplit(as.character(filtered_by_system_write_attempts[i]), " ")[[1]]))

	# the generated data frame has only one column so
	# add a index to the data frame (to plot correctly)
	execution_hadoop_cpu_usage <- data.frame(c(1:nrow(execution_hadoop_cpu_usage)), execution_hadoop_cpu_usage)
	execution_hadoop_memory_usage <- data.frame(c(1:nrow(execution_hadoop_memory_usage)), execution_hadoop_memory_usage)
	execution_system_idle_cpu <- data.frame(c(1:nrow(execution_system_idle_cpu)), execution_system_idle_cpu)
	execution_system_user_cpu <- data.frame(c(1:nrow(execution_system_user_cpu)), execution_system_user_cpu)
	execution_system_memory <- data.frame(c(1:nrow(execution_system_memory)), execution_system_memory)
	execution_system_read_number <- data.frame(c(1:nrow(execution_system_read_number)), execution_system_read_number)
	execution_system_read_sectors <- data.frame(c(1:nrow(execution_system_read_sectors)), execution_system_read_sectors)
	execution_system_write_number <- data.frame(c(1:nrow(execution_system_write_number)), execution_system_write_number)
	execution_system_write_attempts <- data.frame(c(1:nrow(execution_system_write_attempts)), execution_system_write_attempts)

	# name the columns
	colnames(execution_hadoop_cpu_usage) <- c('X0', 'X1')
	colnames(execution_hadoop_memory_usage) <- c('X0', 'X1')
	colnames(execution_system_idle_cpu) <- c('X0', 'X1')
	colnames(execution_system_user_cpu) <- c('X0', 'X1')
	colnames(execution_system_memory) <- c('X0', 'X1')
	colnames(execution_system_read_number) <- c('X0', 'X1')
	colnames(execution_system_read_sectors) <- c('X0', 'X1')
	colnames(execution_system_write_number) <- c('X0', 'X1')
	colnames(execution_system_write_attempts) <- c('X0', 'X1')

	cpu <- ggplot(execution_hadoop_cpu_usage, aes(x=execution_hadoop_cpu_usage$X0, y=execution_hadoop_cpu_usage$X1))
	cpu <- cpu + xlab("Tempo")
	cpu <- cpu + ylab("Consumo de CPU")
	cpu_image(i)
	print_dataframe(cpu, "Hadoop CPU Usage", benchmarks[[1]][[i]])
	
	memory <- ggplot(execution_hadoop_memory_usage, aes(execution_hadoop_memory_usage$X0, execution_hadoop_memory_usage$X1))
	memory <- memory + xlab("Tempo")
	memory <- memory + ylab("Consumo de Memória")
	memory_image(i)
	print_dataframe(memory, "Hadoop Memory Usage", benchmarks[[1]][[i]])

	system_idle_cpu <- ggplot(execution_system_idle_cpu, aes(execution_system_idle_cpu$X0, execution_system_idle_cpu$X1))
	system_idle_cpu <- system_idle_cpu + xlab("Tempo")
	system_idle_cpu <- system_idle_cpu + ylab("Idle CPU")
	system_idle_cpu_image(i)
	print_dataframe(system_idle_cpu, "System Idle CPU", benchmarks[[1]][[i]])

	system_user_cpu <- ggplot(execution_system_user_cpu, aes(execution_system_user_cpu$X0, execution_system_user_cpu$X1))
	system_user_cpu <- system_user_cpu + xlab("Tempo")
	system_user_cpu <- system_user_cpu + ylab("User CPU")
	system_user_cpu_image(i)
	print_dataframe(system_user_cpu, "System User CPU", benchmarks[[1]][[i]])
	
	system_memory <- ggplot(execution_system_memory, aes(execution_system_memory$X0, execution_system_memory$X1))
	system_memory <- system_memory + xlab("Tempo")
	system_memory <- system_memory + ylab("Consumo de Memória")
	system_memory_image(i)
	print_dataframe(system_memory, "System Memory", benchmarks[[1]][[i]])

	system_read_number <- ggplot(execution_system_read_number, aes(execution_system_read_number$X0, execution_system_read_number$X1))
	system_read_number <- system_read_number + xlab("Tempo")
	system_read_number <- system_read_number + ylab("Número de Leituras")
	system_read_number_image(i)
	print_dataframe(system_read_number, "System Read Number", benchmarks[[1]][[i]])
	
	system_read_sectors <- ggplot(execution_system_read_sectors, aes(execution_system_read_sectors$X0, execution_system_read_sectors$X1))
	system_read_sectors <- system_read_sectors + xlab("Tempo")
	system_read_sectors <- system_read_sectors + ylab("Read Sectors")
	system_read_sectors_image(i)
	print_dataframe(system_read_sectors, "System Read Sectors", benchmarks[[1]][[i]])

	system_write_number <- ggplot(execution_system_write_number, aes(execution_system_write_number$X0, execution_system_write_number$X1))
	system_write_number <- system_write_number + xlab("Tempo")
	system_write_number <- system_write_number + ylab("Número de Escritas")
	system_write_number_image(i)
	print_dataframe(system_write_number, "System Write Number", benchmarks[[1]][[i]])

	system_write_attempts <- ggplot(execution_system_write_attempts, aes(execution_system_write_attempts$X0, execution_system_write_attempts$X1))
	system_write_attempts <- system_write_attempts + xlab("Tempo")
	system_write_attempts <- system_write_attempts + ylab("Write Attempts")
	system_write_attempts_image(i)
	print_dataframe(system_write_attempts, "System Write Attempts", benchmarks[[1]][[i]])
}

