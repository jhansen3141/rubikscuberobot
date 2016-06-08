//Josh Hansen
//Rubik's Cube Robot
//April 2016
#include "LPC13xx.h"                        /* LPC13xx definitions */
#include "timer16.h"
#include "timer32.h"
#include "clkconfig.h"
#include "gpio.h"

#include <stdio.h>
#include <stdlib.h>
#include <String.h>
#include <cr_section_macros.h>
#include <NXP/crp.h>

#include "motor_control.h"
#include "math.h"
#include "cmd.h"
#include "utils.h"
#include "uart.h"
#include "spi.h"
#include "cube.h"



#define FORWARD 1
#define REVERSE 0
#define STEP1 0
#define STEP2 1
#define STEP4 2
#define STEP8 3
#define STEP16 4
#define STEP32 5
#define ENABLE 0
#define DISABLE 1


#define MOTOR_1 1
#define MOTOR_2 2
#define MOTOR_3 3
#define MOTOR_4 4

#define SERVO_1 1
#define SERVO_2 2
#define SERVO_3 3
#define SERVO_4 4













