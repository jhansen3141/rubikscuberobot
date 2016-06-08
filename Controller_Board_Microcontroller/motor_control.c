//Josh Hansen
//Rubik's Cube Robot
//April 2016
#include "common.h"

void fpgaWrite(uint8_t address, uint16_t data) {
	GPIOSetValue(3, 0, 0); // pull latch low
	uint8_t highByte = (data & 0xFF00) >> 8;
	uint8_t lowByte = data;
	SPIWrite(address);
	SPIWrite(highByte);
	SPIWrite(lowByte);
	GPIOSetValue(3,0,1); // latch high
	delay_microseconds(0,50);
	GPIOSetValue(3,0,0); // latch low
}


void motorInterfaceInit() {
	GPIOSetDir(2, 1, 1); // address B0 as output
	GPIOSetDir(2, 2, 1); // address B1 as output
	GPIOSetDir(2, 3, 1); // address B2 as output
	GPIOSetDir(2, 6, 1); // address B3 as output

	GPIOSetDir(2,7,0); // serial data in
	GPIOSetDir(2,8,1); // serial data in clock
	GPIOSetDir(2,9,1); // serial data in load
	GPIOSetDir(2,10,1);

	GPIOSetDir(3, 0, 1); // FPGA latch as output

	GPIOSetDir(1, 8, 0); // Accelerometer interrupt as input
}

void latchMotor(uint8_t motor) {
	switch(motor) {
	case MOTOR_1:
		GPIOSetValue(2, 1, 1);
		GPIOSetValue(2, 1, 0);
		break;
	case MOTOR_2:
		GPIOSetValue(2, 2, 1);
		GPIOSetValue(2, 2, 0);
		break;
	case MOTOR_3:
		GPIOSetValue(2, 3, 1);
		GPIOSetValue(2, 3, 0);
		break;
	case MOTOR_4:
		GPIOSetValue(2, 6, 1);
		GPIOSetValue(2, 6, 0);
		break;
	}
}

void motorEnable(uint8_t motorNumber, uint8_t enable) {
	switch(motorNumber) {
		case MOTOR_1:
			fpgaWrite(M1_EN_ADDRESS,enable); // enable active low
			break;
		case MOTOR_2:
			fpgaWrite(M2_EN_ADDRESS,enable); // enable active low
			break;
		case MOTOR_3:
			fpgaWrite(M3_EN_ADDRESS,enable); // enable active low
			break;
		case MOTOR_4:
			fpgaWrite(M4_EN_ADDRESS,enable); // enable active low
			break;
	}
}

void setMotorPos(uint8_t motorNumber, uint16_t pos, uint8_t direction) {
	switch(motorNumber) {
		case MOTOR_1:
			fpgaWrite(M1_DIR_ADDRESS,direction);
			fpgaWrite(M1_POS_ADDRESS,pos);
			latchMotor(MOTOR_1);
			break;
		case MOTOR_2:
			fpgaWrite(M2_DIR_ADDRESS,direction);
			fpgaWrite(M2_POS_ADDRESS,pos);
			latchMotor(MOTOR_2);
			break;
		case MOTOR_3:
			fpgaWrite(M3_DIR_ADDRESS,direction);
			fpgaWrite(M3_POS_ADDRESS,pos);
			latchMotor(MOTOR_3);
			break;
		case MOTOR_4:
			fpgaWrite(M4_DIR_ADDRESS,direction);
			fpgaWrite(M4_POS_ADDRESS,pos);
			latchMotor(MOTOR_4);
			break;
	}
}

void setMotorSpeed(uint8_t motorNumber, uint16_t speed) {
	switch(motorNumber) {
		case MOTOR_1:
			fpgaWrite(M1_SPEED_ADDRESS,speed);
			break;
		case MOTOR_2:
			fpgaWrite(M2_SPEED_ADDRESS,speed);
			break;
		case MOTOR_3:
			fpgaWrite(M3_SPEED_ADDRESS,speed);
			break;
		case MOTOR_4:
			fpgaWrite(M4_SPEED_ADDRESS,speed);
			break;
	}
}

void setMotorMicroStep(uint8_t motorNumber, uint16_t micoStep) {
	switch(motorNumber) {
		case MOTOR_1:
			fpgaWrite(M1_MICRO_ADDRESS,micoStep);
			break;
		case MOTOR_2:
			fpgaWrite(M2_MICRO_ADDRESS,micoStep);
			break;
		case MOTOR_3:
			fpgaWrite(M3_MICRO_ADDRESS,micoStep);
			break;
		case MOTOR_4:
			fpgaWrite(M4_MICRO_ADDRESS,micoStep);
			break;
	}
}

void setServoPos(uint8_t servoNumber, uint16_t pos) {
	uint16_t mappedPos = (uint16_t)(map(pos,0,1000,1000,2000));
	switch(servoNumber) {
		case SERVO_1:
			fpgaWrite(S1_POS_ADDRESS,mappedPos);
			break;
		case SERVO_2:
			fpgaWrite(S2_POS_ADDRESS,mappedPos);
			break;
		case SERVO_3:
			fpgaWrite(S3_POS_ADDRESS,mappedPos);
			break;
		case SERVO_4:
			fpgaWrite(S4_POS_ADDRESS,mappedPos);
			break;
	}
}
