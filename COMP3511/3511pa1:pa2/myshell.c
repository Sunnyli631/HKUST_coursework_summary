/*
    COMP3511 Fall 2023
    PA1: Simplified Linux Shell (MyShell)

    Your name:
    Your ITSC email:hyliav@connect.ust.hk

    Declaration:

    I declare that I am not involved in plagiarism
    I understand that both parties (i.e., students providing the codes and students copying the codes) will receive 0 marks.

*/

/*
    Header files for MyShell
    Necessary header files are included.
    Do not include extra header files
*/
#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h> // For constants that are required in open/read/write/close syscalls
#include <sys/wait.h> // For wait() - suppress warning messages
#include <fcntl.h>    // For open/read/write/close syscalls
#include <signal.h>   // For signal handling

// Define template strings so that they can be easily used in printf
//
// Usage: assume pid is the process ID
//
//  printf(TEMPLATE_MYSHELL_START, pid);
//
#define TEMPLATE_MYSHELL_START "Myshell (pid=%d) starts\n"
#define TEMPLATE_MYSHELL_END "Myshell (pid=%d) ends\n"
#define TEMPLATE_MYSHELL_TERMINATE "Myshell (pid=%d) terminates by Ctrl-C\n"

// Assume that each command line has at most 256 characters (including NULL)
#define MAX_CMDLINE_LENGTH 256

// Assume that we have at most 8 arguments
#define MAX_ARGUMENTS 8

// Assume that we only need to support 2 types of space characters:
// " " (space) and "\t" (tab)
#define SPACE_CHARS " \t"

// The pipe character
#define PIPE_CHAR "|"

// Assume that we only have at most 8 pipe segements,
// and each segment has at most 256 characters
#define MAX_PIPE_SEGMENTS 8

// Assume that we have at most 8 arguments for each segment
// We also need to add an extra NULL item to be used in execvp
// Thus: 8 + 1 = 9
//
// Example:
//   echo a1 a2 a3 a4 a5 a6 a7
//
// execvp system call needs to store an extra NULL to represent the end of the parameter list
//
//   char *arguments[MAX_ARGUMENTS_PER_SEGMENT];
//
//   strings stored in the array: echo a1 a2 a3 a4 a5 a6 a7 NULL
//
#define MAX_ARGUMENTS_PER_SEGMENT 9

// Define the standard file descriptor IDs here
#define STDIN_FILENO 0  // Standard input
#define STDOUT_FILENO 1 // Standard output

// This function will be invoked by main()
// This function is given
int get_cmd_line(char *command_line)
{
    int i, n;
    if (!fgets(command_line, MAX_CMDLINE_LENGTH, stdin))
        return -1;
    // Ignore the newline character
    n = strlen(command_line);
    command_line[--n] = '\0';
    i = 0;
    while (i < n && command_line[i] == ' ')
    {
        ++i;
    }
    if (i == n)
    {
        // Empty command
        return -1;
    }
    return 0;
}

// read_tokens function is given
// This function helps you parse the command line
//
// Suppose the following variables are defined:
//
// char *pipe_segments[MAX_PIPE_SEGMENTS]; // character array buffer to store the pipe segements
// int num_pipe_segments; // an output integer to store the number of pipe segment parsed by this function
// char command_line[MAX_CMDLINE_LENGTH]; // The input command line
//
// Sample usage:
//
//  read_tokens(pipe_segments, command_line, &num_pipe_segments, "|");
//
void read_tokens(char **argv, char *line, int *numTokens, char *delimiter)
{
    int argc = 0;
    char *token = strtok(line, delimiter);
    while (token != NULL)
    {
        argv[argc++] = token;
        token = strtok(NULL, delimiter);
    }
    *numTokens = argc;
}

void process_cmd(char *command_line)
{
    // Uncomment this line to check the cmdline content
    // printf("Debug: The command line is [%s]\n", command_line);
    // execvp(command_line[0],command_line);
    char *args[MAX_ARGUMENTS_PER_SEGMENT]={};
    char *args_pipe[MAX_PIPE_SEGMENTS+1]={};
    int num=0;
    int agg=0;
    int count_0=0;
    int count_1=0;
    int numpipe=0;
    int nopipe=0;
    char *arguments[MAX_ARGUMENTS_PER_SEGMENT]={};
    char cline[MAX_CMDLINE_LENGTH];
    strcpy(cline,command_line);
    //printf("Debug: The command line is [%s]\n", cline);
    
    read_tokens(args_pipe,cline,&nopipe,PIPE_CHAR);
    read_tokens(args,command_line,&num,SPACE_CHARS);
    //printf("Debug: The command line is [%s]\n", args_pipe[1]);
    args[num]=NULL;
    for(int i=0;i<num;i++){
    	if(count_0==0&&strcmp(args[i],"<")==0){
		//printf("\"%s\"",args[i]);
		int fdr= open(args[i+1], O_RDONLY, S_IRUSR | S_IWUSR);
		//close(0)
		dup2(fdr,STDIN_FILENO);
		close(fdr);
		count_0=1;
	}
	if(count_1==0&&strcmp(args[i],">")==0){
		int fdw= open(args[i+1], O_CREAT | O_WRONLY, S_IRUSR | S_IWUSR);
		//close(1);
		dup2(fdw,STDOUT_FILENO);
		close(fdw);
		count_1=1;
	}
		
    }
    for(int i=0;i<num;i++){
    	if(strcmp(args[i],"<")==0||strcmp(args[i],">")==0){
		    break;
	    }
        if(strcmp(args[i],"|")==0){
            numpipe++;
        }
	    arguments[agg]=args[i];
	    agg++;
    }

    int i;
    int pfds[2];
    int fd=STDIN_FILENO;
    int num_2=0;
    char *arrg[MAX_ARGUMENTS_PER_SEGMENT]={};
    //int num_1=0;
    //char *sta[MAX_ARGUMENTS_PER_SEGMENT]={};
    if(numpipe>0){
        for (i=0;i<nopipe;i++){
            read_tokens(arrg,args_pipe[i],&num_2,SPACE_CHARS);
            arrg[num_2]=NULL;
            if(i+1!=nopipe){
                pipe(pfds);
                pid_t pid= fork();
                if(pid==0){
                    if(fd!=STDIN_FILENO){
                        dup2(fd,STDIN_FILENO);
                        close(fd);
                    }
                    dup2(pfds[1],STDOUT_FILENO);
                    close(pfds[1]);
    
                    execvp(arrg[0],arrg);
                    //perror("error :");
                    exit(1);
                }else{
                    close(fd);
                    fd=pfds[0];
                    close(pfds[1]);
                    wait(NULL);
                }
            }else{
                
                //read_tokens(arrg,args_pipe[i],&num_2,SPACE_CHARS);
                //arrg[num_2]=NULL;
                if(fd!=STDIN_FILENO){
                dup2(fd,STDIN_FILENO);
                close(fd);
                }
                execvp(arrg[0],arrg);
                exit(1);
            }
        }
    }else{
        arguments[agg]=NULL;
        execvp(arguments[0],arguments);
        exit(1);
    }
    
}

void control_c(int sig)
{
	printf(TEMPLATE_MYSHELL_TERMINATE,getpid());
	exit(1);
}

/* The main function implementation */
int main()
{
    // TODO: replace the shell prompt with your ITSC account name
    // For example, if you ITSC account is cspeter@connect.ust.hk
    // You should replace ITSC with cspeter
    char *prompt = "hyliav";
    char command_line[MAX_CMDLINE_LENGTH];
    
    // TODO:
    // The main function needs to be modified
    // For example, you need to handle the exit command inside the main function
    //signal(SIGINT,control_c);
    printf(TEMPLATE_MYSHELL_START, getpid());
    signal(SIGINT,control_c);
    // The main event loop
    while (1)
    {

        printf("%s> ", prompt);
        if (get_cmd_line(command_line) == -1)
            continue; /* empty line handling */
	
        if(strcmp(command_line,"exit")==0){
            printf(TEMPLATE_MYSHELL_END,getpid());
            exit(0);
        }
		    	
        pid_t pid = fork();
        if (pid == 0)
        {
            // the child process handles the command
	    // printf(TEMPLATE_MYSHELL_START,getpid());
	   
	    process_cmd(command_line);
            //execvp(args[0],args);
	    // execvp();
        }
        else
        {
            // the parent process simply wait for the child and do nothing
            wait(0);
	    // signal(SIGINT,control_c);
        }
    }

    return 0;
}

