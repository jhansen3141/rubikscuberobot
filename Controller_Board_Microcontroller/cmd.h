//Josh Hansen
//Rubik's Cube Robot
//April 2016
#ifndef CMD_H_
#define CMD_H_

#define MAX_MSG_SIZE    128
#include "common.h"

// command line structure
typedef struct _cmd_t
{
    char *cmd;
    void (*func)(int argc, char **argv);
    struct _cmd_t *next;
} cmd_t;

void cmdInit();
void cmdPoll();
void cmd_handler(char c);
void cmdAdd(char *name, void (*func)(int argc, char **argv));
uint32_t cmdStr2Num(char *str, uint8_t base);



#endif /* CMD_H_ */
