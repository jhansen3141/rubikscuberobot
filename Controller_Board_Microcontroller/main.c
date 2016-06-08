//Josh Hansen
//Rubik's Cube Robot
//April 2016

#include "common.h"

__CRP const unsigned int CRP_WORD = CRP_NO_CRP ;

//---------------GLOBAL VARIABLES-------------------------------//
typedef enum {PHASE1,PHASE2,PHASE3,COMPLETE} LoadState;

uint8_t moves[15] = {1,4,5,4,3,2,6,5,1,3,6,3,1,4,5};
LoadState loadState = PHASE1;
//---------------FUNCTION PROTOTYPES---------------------------//
void boardInit();
void cmdSetMotorSpeed(int arg_cnt, char **args);
void cmdSetMotorPos(int arg_cnt, char **args);
void cmdSetMotorEn(int arg_cnt, char **args);
void cmdSetServoPos(int arg_cnt, char **args);
void cmdRotateFrontFaceQ(int arg_cnt, char **args);
void cmdRotateBackFaceQ(int arg_cnt, char **args);
void cmdRotateLeftFaceQ(int arg_cnt, char **args);
void cmdRotateRightFaceQ(int arg_cnt, char **args);
void cmdRotateTopFaceQ(int arg_cnt, char **args);
void cmdRotateBottomFaceQ(int arg_cnt, char **args);
void cmdMoveRight(int arg_cnt, char **args);
void cmdMoveFront(int arg_cnt, char **args);
void cmdMoveBack(int arg_cnt, char **args);
void cmdMoveLeft(int arg_cnt, char **args);
void scramble();
void unscramble();


void boardInit() {

	GPIOInit();
	SPIInit();

	UARTInit(9600);
	cmdAdd("setMS",cmdSetMotorSpeed);
	cmdAdd("setMP",cmdSetMotorPos);
	cmdAdd("setME",cmdSetMotorEn);
	cmdAdd("setSP",cmdSetServoPos);

	cmdAdd("rhbck",rotateHBackFace);
	cmdAdd("rhfnt",rotateHFrontFace);
	cmdAdd("rhrht",rotateHRightFace);
	cmdAdd("rhlft",rotateHLeftFace);
	cmdAdd("rhtop",rotateHTopFace);
	cmdAdd("rhbtm",rotateHBottomFace);

	cmdAdd("rqbck",cmdRotateBackFaceQ);
	cmdAdd("rqfnt",cmdRotateFrontFaceQ);
	cmdAdd("rqrht",cmdRotateRightFaceQ);
	cmdAdd("rqlft",cmdRotateLeftFaceQ);
	cmdAdd("rqtop",cmdRotateTopFaceQ);
	cmdAdd("rqbtm",cmdRotateBottomFaceQ);

	cmdAdd("sc",scramble);
	cmdAdd("us",unscramble);
	cmdAdd("sfnt",showFrontFace);
	cmdAdd("sbtm",showBottomFace);
	cmdAdd("sbck",showBackFace);
	cmdAdd("srht",showRightFace);
	cmdAdd("slft",showLeftFace);
	cmdAdd("stop",showTopFace);

	cmdAdd("mf1",cmdMoveFront);
	cmdAdd("mb1",cmdMoveBack);
	cmdAdd("ml1",cmdMoveLeft);
	cmdAdd("mr1",cmdMoveRight);

	cmdInit();
	motorInterfaceInit();

	setMotorMicroStep(1,STEP4);
	setMotorMicroStep(2,STEP4);
	setMotorMicroStep(3,STEP4);
	setMotorMicroStep(4,STEP4);

	LPC_SYSCON->SYSAHBCLKCTRL |= (1<<7); // timerB0 enable clock
	GPIOSetDir(3, 1, 0); // Enable button as input
	GPIOSetDir(3, 3, 1); // loop counter pin
}


void cmdMoveFront(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			moveFront1(CW);
			break;
		case 2:
			moveFront1(CCW);
			break;
		}
}

void cmdMoveRight(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			moveRight1(CW);
			break;
		case 2:
			moveRight1(CCW);
			break;
		}
}

void cmdMoveBack(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			moveBack1(CW);
			break;
		case 2:
			moveBack1(CCW);
			break;
		}
}

void cmdMoveLeft(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			moveLeft1(CW);
			break;
		case 2:
			moveLeft1(CCW);
			break;
		}
}

void cmdRotateFrontFaceQ(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			rotateQFrontFace(CW);
			break;
		case 2:
			rotateQFrontFace(CCW);
			break;
	}
}

void cmdRotateBackFaceQ(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			rotateQBackFace(CW);
			break;
		case 2:
			rotateQBackFace(CCW);
			break;
	}
}

void cmdRotateLeftFaceQ(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			rotateQLeftFace(CW);
			break;
		case 2:
			rotateQLeftFace(CCW);
			break;
	}
}

void cmdRotateRightFaceQ(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			rotateQRightFace(CW);
			break;
		case 2:
			rotateQRightFace(CCW);
			break;
	}
}

void cmdRotateTopFaceQ(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			rotateQTopFace(CW);
			break;
		case 2:
			rotateQTopFace(CCW);
			break;
	}
}

void cmdRotateBottomFaceQ(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case 1:
			rotateQBottomFace(CW);
			break;
		case 2:
			rotateQBottomFace(CCW);
			break;
	}
}

void cmdSetMotorSpeed(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case MOTOR_1:
			setMotorSpeed(MOTOR_1,atoi(args[2]));
			break;
		case MOTOR_2:
			setMotorSpeed(MOTOR_2,atoi(args[2]));
			break;
		case MOTOR_3:
			setMotorSpeed(MOTOR_3,atoi(args[2]));
			break;
		case MOTOR_4:
			setMotorSpeed(MOTOR_4,atoi(args[2]));
			break;
		default:
			printf("Invalid motor number\n");
		}
}

void cmdSetMotorPos(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
			case MOTOR_1:
				setMotorPos(MOTOR_1,atoi(args[2]),CW);
				break;
			case MOTOR_2:
				setMotorPos(MOTOR_2,atoi(args[2]),CW);
				break;
			case MOTOR_3:
				setMotorPos(MOTOR_3,atoi(args[2]),CW);
				break;
			case MOTOR_4:
				setMotorPos(MOTOR_4,atoi(args[2]),CW);
				break;
			default:
				printf("Invalid motor number\n");
		}
}

void cmdSetMotorEn(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case MOTOR_1:
			motorEnable(MOTOR_1,atoi(args[2]));
			break;
		case MOTOR_2:
			motorEnable(MOTOR_2,atoi(args[2]));
			break;
		case MOTOR_3:
			motorEnable(MOTOR_3,atoi(args[2]));
			break;
		case MOTOR_4:
			motorEnable(MOTOR_4,atoi(args[2]));
			break;
		default:
			printf("Invalid motor number\n");
	}
}

void cmdSetServoPos(int arg_cnt, char **args) {
	switch(atoi(args[1])) {
		case SERVO_1:
			setServoPos(SERVO_1,atoi(args[2]));
			break;
		case SERVO_2:
			setServoPos(SERVO_2,atoi(args[2]));
			break;
		case SERVO_3:
			setServoPos(SERVO_3,atoi(args[2]));
			break;
		case SERVO_4:
			setServoPos(SERVO_4,atoi(args[2]));
			break;
		default:
			printf("Invalid servo number\n");
	}
}

void scramble() {
	// {1,4,5,4,3,2,6,5,1,3,6,3,1,4,5};
	uint8_t i;
	for(i=0;i<15;i++) {
		switch(moves[i]) {
			case 1:
				rotateHFrontFace();
				break;
			case 2:
				rotateHBackFace();
				break;
			case 3:
				rotateHLeftFace();
				break;
			case 4:
				rotateHRightFace();
				break;
			case 5:
				rotateHTopFace();
				break;
			case 6:
				rotateHBottomFace();
				break;
		}
	}
}

void unscramble() {
	int8_t i;
	for(i=14;i>=0;i--) {
		switch(moves[i]) {
			case 1:
				rotateHFrontFace();
				break;
			case 2:
				rotateHBackFace();
				break;
			case 3:
				rotateHLeftFace();
				break;
			case 4:
				rotateHRightFace();
				break;
			case 5:
				rotateHTopFace();
				break;
			case 6:
				rotateHBottomFace();
				break;
		}
	}
}

int main (void) {

	boardInit();
	printf("Init Complete\n");


	motorEnable(MOTOR_1,DISABLE);
	motorEnable(MOTOR_2,DISABLE);
	motorEnable(MOTOR_3,DISABLE);
	motorEnable(MOTOR_4,DISABLE);
	setMotorSpeed(MOTOR_1,30);
	setMotorSpeed(MOTOR_2,30);
	setMotorSpeed(MOTOR_3,30);
	setMotorSpeed(MOTOR_4,30);
	setServoPos(SERVO_1,SERVO_1_OPEN);
	setServoPos(SERVO_2,SERVO_2_OPEN);
	setServoPos(SERVO_3,SERVO_3_OPEN);
	setServoPos(SERVO_4,SERVO_4_OPEN);

	while (1)	{

		if(GPIORead(3,1)) { // if button is pressed
			delay_ms(300);	// debounce
			switch(loadState) {
				case PHASE1: // Phase 1 is to close two opposite motors
					setServoPos(SERVO_1,SERVO_1_CLOSED);
					setServoPos(SERVO_3,SERVO_1_CLOSED);
					delay_ms(SERVO_DELAY);
					loadState = PHASE2;
					break;
				case PHASE2: // Phase 2 is to close remaining two motors
					setServoPos(SERVO_2,SERVO_2_CLOSED);
					setServoPos(SERVO_4,SERVO_4_CLOSED);
					delay_ms(SERVO_DELAY);
					loadState = PHASE3;
					break;
				case PHASE3: // Phase 3 is to set starting stepper loc to 500 and then enable steppers
					motorEnable(MOTOR_1,ENABLE);
					motorEnable(MOTOR_2,ENABLE);
					motorEnable(MOTOR_3,ENABLE);
					motorEnable(MOTOR_4,ENABLE);
					loadState = COMPLETE;
					break;
				case COMPLETE: // Complete is to open all motors and disable steppers
					setServoPos(SERVO_1,SERVO_1_OPEN);
					setServoPos(SERVO_2,SERVO_2_OPEN);
					setServoPos(SERVO_3,SERVO_3_OPEN);
					setServoPos(SERVO_4,SERVO_4_OPEN);
					motorEnable(MOTOR_1,DISABLE);
					motorEnable(MOTOR_2,DISABLE);
					motorEnable(MOTOR_3,DISABLE);
					motorEnable(MOTOR_4,DISABLE);
					delay_ms(SERVO_DELAY);
					loadState = PHASE1;
					break;
			}
		}
	}
}




