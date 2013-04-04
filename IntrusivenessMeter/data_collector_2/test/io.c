# include <stdio.h>
# include <stdlib.h>

# define BYTES_TO_WRITE 1024 * 1024 * 1024
# define NUMBER_OF_WRITES 10

# define BYTES_TO_READ 1024 * 1024 * 1024
# define NUMBER_OF_READS 10

int main(void)
{
	char *data = (char *) malloc(BYTES_TO_WRITE);

	if (data == NULL)
	{
		printf("data could not be allocated");
		exit(1);
	}
	
	FILE *file = fopen("test.txt", "w+");

	if (file == NULL)
	{
		printf("File could not be created\n");
		exit(1);
	}

	int writes = 0;
	int reads = 0;

	for (; writes < NUMBER_OF_WRITES; writes++)
	{
		fwrite(data, BYTES_TO_WRITE, 1, file);
	}

	for (; reads <  NUMBER_OF_READS; reads++)
	{
		fread(data, BYTES_TO_READ, 1, file);
	}

	remove("test.txt");
}
