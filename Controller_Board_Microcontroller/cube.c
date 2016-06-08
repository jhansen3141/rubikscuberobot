//Josh Hansen
//Rubik's Cube Robot
//April 2016
#include "common.h"


volatile uint16_t motor1Pos = 500;
volatile uint16_t motor2Pos = 500;
volatile uint16_t motor3Pos = 500;
volatile uint16_t motor4Pos = 500;

void align() {
	setMotorPos(MOTOR_2,100,CW);
	delay_ms(250);
	setMotorPos(MOTOR_2,100,CCW);
	delay_ms(250);

	setMotorPos(MOTOR_1,100,CW);
	delay_ms(250);
	setMotorPos(MOTOR_1,100,CCW);
	delay_ms(250);

	setMotorPos(MOTOR_4,100,CW);
	delay_ms(250);
	setMotorPos(MOTOR_4,100,CCW);
	delay_ms(250);

	setMotorPos(MOTOR_3,100,CW);
	delay_ms(250);
	setMotorPos(MOTOR_3,100,CCW);
	delay_ms(250);
}

void rotateHRightFace() {
	setMotorPos(MOTOR_1,STEPPER_180_DEGREE,CW);
	delay_ms(STEPPER_H_DELAY);
	SEND_ACKB();
}

void rotateHLeftFace() {
	setMotorPos(MOTOR_3,STEPPER_180_DEGREE,CW);
	delay_ms(STEPPER_H_DELAY);
	SEND_ACKB();
}

void rotateHFrontFace() {
	setMotorPos(MOTOR_2,STEPPER_180_DEGREE,CW);
	delay_ms(STEPPER_H_DELAY);
	SEND_ACKB();
}

void rotateHBackFace() {
	setMotorPos(MOTOR_4,STEPPER_180_DEGREE,CW);
	delay_ms(STEPPER_H_DELAY);
	SEND_ACKB();
}

void rotateCubeTF() {
	setMotorPos(MOTOR_1,STEPPER_90_DEGREE,CW);
	setMotorPos(MOTOR_3,STEPPER_90_DEGREE,CCW);
	delay_ms(STEPPER_Q_DELAY);
}

void rotateCubeFT() {
	setMotorPos(MOTOR_1,STEPPER_90_DEGREE,CCW);
	setMotorPos(MOTOR_3,STEPPER_90_DEGREE,CW);
	delay_ms(STEPPER_Q_DELAY);
}

void rotateCubeLR() {
	setMotorPos(MOTOR_2,STEPPER_90_DEGREE,CW);
	setMotorPos(MOTOR_4,STEPPER_90_DEGREE,CCW);
	delay_ms(STEPPER_Q_DELAY);
}

void rotateCubeRL() {
	setMotorPos(MOTOR_2,STEPPER_90_DEGREE,CCW);
	setMotorPos(MOTOR_4,STEPPER_90_DEGREE,CW);
	delay_ms(STEPPER_Q_DELAY);
}

void rotateHBottomFace() {
	setServoPos(SERVO_2,SERVO_2_OPEN);
	setServoPos(SERVO_4,SERVO_4_OPEN); // open motors
	delay_ms(SERVO_DELAY);

	rotateCubeTF();		// rotate cube quater turn

	setServoPos(SERVO_2,SERVO_2_CLOSED);
	setServoPos(SERVO_4,SERVO_4_CLOSED); // close the motors
	delay_ms(SERVO_DELAY);

	setServoPos(SERVO_1,SERVO_1_OPEN);
	setServoPos(SERVO_3,SERVO_3_OPEN); // open 1 and 3
	delay_ms(SERVO_DELAY);

	rotateCubeFT();

	setServoPos(SERVO_1,SERVO_1_CLOSED);
	setServoPos(SERVO_3,SERVO_3_CLOSED); // close 1 and 3
	delay_ms(SERVO_DELAY);


	setMotorPos(MOTOR_2,STEPPER_180_DEGREE,CW);
	delay_ms(STEPPER_H_DELAY);


	setServoPos(SERVO_2,SERVO_2_OPEN);
	setServoPos(SERVO_4,SERVO_4_OPEN); // open motors
	delay_ms(SERVO_DELAY);


	rotateCubeFT();

	setServoPos(SERVO_2,SERVO_2_CLOSED);
	setServoPos(SERVO_4,SERVO_4_CLOSED); // close the motors
	delay_ms(SERVO_DELAY);

	setServoPos(SERVO_1,SERVO_1_OPEN);
	setServoPos(SERVO_3,SERVO_3_OPEN); // open 1 and 3
	delay_ms(SERVO_DELAY);

	rotateCubeTF();

	setServoPos(SERVO_1,SERVO_1_CLOSED);
	setServoPos(SERVO_3,SERVO_3_CLOSED); // close 1 and 3
	delay_ms(SERVO_DELAY);
	SEND_ACKB();
}

void rotateHTopFace() {

	setServoPos(SERVO_2,SERVO_2_OPEN);
	setServoPos(SERVO_4,SERVO_4_OPEN); // open motors
	delay_ms(SERVO_DELAY);

	rotateCubeFT();

	setServoPos(SERVO_2,SERVO_2_CLOSED);
	setServoPos(SERVO_4,SERVO_4_CLOSED); // close the motors
	delay_ms(SERVO_DELAY);

	setServoPos(SERVO_1,SERVO_1_OPEN);
	setServoPos(SERVO_3,SERVO_3_OPEN); // open 1 and 3
	delay_ms(SERVO_DELAY);

	rotateCubeTF();

	setServoPos(SERVO_1,SERVO_1_CLOSED);
	setServoPos(SERVO_3,SERVO_3_CLOSED); // close 1 and 3
	delay_ms(SERVO_DELAY);

	setMotorPos(MOTOR_2,STEPPER_180_DEGREE,CW);
	delay_ms(STEPPER_H_DELAY);

	setServoPos(SERVO_2,SERVO_2_OPEN);
	setServoPos(SERVO_4,SERVO_4_OPEN); // open motors
	delay_ms(SERVO_DELAY);


	rotateCubeTF();

	setServoPos(SERVO_2,SERVO_2_CLOSED);
	setServoPos(SERVO_4,SERVO_4_CLOSED); // close the motors
	delay_ms(SERVO_DELAY);

	setServoPos(SERVO_1,SERVO_1_OPEN);
	setServoPos(SERVO_3,SERVO_3_OPEN); // open 1 and 3
	delay_ms(SERVO_DELAY);

	rotateCubeFT();

	setServoPos(SERVO_1,SERVO_1_CLOSED);
	setServoPos(SERVO_3,SERVO_3_CLOSED); // close 1 and 3
	delay_ms(SERVO_DELAY);

	SEND_ACKB();
}

void moveFront1(Direction direction) {
	setMotorPos(MOTOR_2,1,direction);
}

void moveBack1(Direction direction) {
	setMotorPos(MOTOR_4,1,direction);
}

void moveLeft1(Direction direction) {
	setMotorPos(MOTOR_3,1,direction);
}

void moveRight1(Direction direction) {
	setMotorPos(MOTOR_1,1,direction);
}


void rotateQRightFace(Direction direction) {

	setMotorPos(MOTOR_1,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_H_DELAY);

	setServoPos(SERVO_1,SERVO_4_OPEN);
	delay_ms(SERVO_DELAY);
	setMotorPos(MOTOR_1,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_Q_DELAY);
	setServoPos(SERVO_1,SERVO_1_CLOSED);
	delay_ms(SERVO_DELAY);

	SEND_ACKB();
}

void rotateQLeftFace(Direction direction) {

	setMotorPos(MOTOR_3,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_H_DELAY);

	setServoPos(SERVO_3,SERVO_3_OPEN);
	delay_ms(SERVO_DELAY);
	setMotorPos(MOTOR_3,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_Q_DELAY);
	setServoPos(SERVO_3,SERVO_3_CLOSED);
	delay_ms(SERVO_DELAY);

	SEND_ACKB();
}

void rotateQFrontFace(Direction direction) {

	setMotorPos(MOTOR_2,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_H_DELAY);

	setServoPos(SERVO_2,SERVO_2_OPEN);
	delay_ms(SERVO_DELAY);
	setMotorPos(MOTOR_2,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_Q_DELAY);
	setServoPos(SERVO_2,SERVO_2_CLOSED);
	delay_ms(SERVO_DELAY);

	SEND_ACKB();
}

void rotateQBackFace(Direction direction) {
	setMotorPos(MOTOR_4,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_H_DELAY);

	setServoPos(SERVO_4,SERVO_4_OPEN);
	delay_ms(SERVO_DELAY);
	setMotorPos(MOTOR_4,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_Q_DELAY);
	setServoPos(SERVO_4,SERVO_4_CLOSED);
	delay_ms(SERVO_DELAY);

	SEND_ACKB();
}

void rotateQBottomFace(Direction direction) {
	setServoPos(SERVO_2,SERVO_2_OPEN);
	setServoPos(SERVO_4,SERVO_4_OPEN); // open motors
	delay_ms(SERVO_DELAY);

	rotateCubeTF();		// rotate cube quater turn

	setServoPos(SERVO_2,SERVO_2_CLOSED);
	setServoPos(SERVO_4,SERVO_4_CLOSED); // close the motors
	delay_ms(SERVO_DELAY);

	setServoPos(SERVO_1,SERVO_1_OPEN);
	setServoPos(SERVO_3,SERVO_3_OPEN); // open 1 and 3
	delay_ms(SERVO_DELAY);

	rotateCubeFT();

	setServoPos(SERVO_1,SERVO_1_CLOSED);
	setServoPos(SERVO_3,SERVO_3_CLOSED); // close 1 and 3
	delay_ms(SERVO_DELAY);


	setMotorPos(MOTOR_2,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_H_DELAY);

	setServoPos(SERVO_2,SERVO_2_OPEN);
	delay_ms(SERVO_DELAY);
	setMotorPos(MOTOR_2,STEPPER_90_DEGREE,direction);
	delay_ms(STEPPER_Q_DELAY);
	setServoPos(SERVO_2,SERVO_2_CLOSED);
	delay_ms(SERVO_DELAY);


	setServoPos(SERVO_2,SERVO_2_OPEN);
	setServoPos(SERVO_4,SERVO_4_OPEN); // open motors
	delay_ms(SERVO_DELAY);


	rotateCubeFT();

	setServoPos(SERVO_2,SERVO_2_CLOSED);
	setServoPos(SERVO_4,SERVO_4_CLOSED); // close the motors
	delay_ms(SERVO_DELAY);

	setServoPos(SERVO_1,SERVO_1_OPEN);
	setServoPos(SERVO_3,SERVO_3_OPEN); // open 1 and 3
	delay_ms(SERVO_DELAY);

	rotateCubeTF();

	setServoPos(SERVO_1,SERVO_1_CLOSED);
	setServoPos(SERVO_3,SERVO_3_CLOSED); // close 1 and 3
	delay_ms(SERVO_DELAY);
	SEND_ACKB();
}

void rotateQTopFace(Direction direction) {

		setServoPos(SERVO_2,SERVO_2_OPEN);
		setServoPos(SERVO_4,SERVO_4_OPEN); // open motors
		delay_ms(SERVO_DELAY);

		rotateCubeFT();

		setServoPos(SERVO_2,SERVO_2_CLOSED);
		setServoPos(SERVO_4,SERVO_4_CLOSED); // close the motors
		delay_ms(SERVO_DELAY);

		setServoPos(SERVO_1,SERVO_1_OPEN);
		setServoPos(SERVO_3,SERVO_3_OPEN); // open 1 and 3
		delay_ms(SERVO_DELAY);

		rotateCubeTF();

		setServoPos(SERVO_1,SERVO_1_CLOSED);
		setServoPos(SERVO_3,SERVO_3_CLOSED); // close 1 and 3
		delay_ms(SERVO_DELAY);

		setMotorPos(MOTOR_2,STEPPER_90_DEGREE,direction);
		delay_ms(STEPPER_H_DELAY);

		setServoPos(SERVO_2,SERVO_2_OPEN);
		delay_ms(SERVO_DELAY);
		setMotorPos(MOTOR_2,STEPPER_90_DEGREE,direction);
		delay_ms(STEPPER_Q_DELAY);
		setServoPos(SERVO_2,SERVO_2_CLOSED);
		delay_ms(SERVO_DELAY);

		setServoPos(SERVO_2,SERVO_2_OPEN);
		setServoPos(SERVO_4,SERVO_4_OPEN); // open motors
		delay_ms(SERVO_DELAY);


		rotateCubeTF();

		setServoPos(SERVO_2,SERVO_2_CLOSED);
		setServoPos(SERVO_4,SERVO_4_CLOSED); // close the motors
		delay_ms(SERVO_DELAY);

		setServoPos(SERVO_1,SERVO_1_OPEN);
		setServoPos(SERVO_3,SERVO_3_OPEN); // open 1 and 3
		delay_ms(SERVO_DELAY);

		rotateCubeFT();

		setServoPos(SERVO_1,SERVO_1_CLOSED);
		setServoPos(SERVO_3,SERVO_3_CLOSED); // close 1 and 3
		delay_ms(SERVO_DELAY);

		SEND_ACKB();
}

void showFrontFace() {
	open2and4();
	rotateCubeTF();
	delay_ms(SETTLE_DELAY);
	SEND_ACK();
	delay_ms(SHOW_DELAY);
	rotateCubeFT();
	close2and4();
	align();
	SEND_ACK();
}

void showBackFace() {
	open2and4();
	rotateCubeFT();
	delay_ms(SETTLE_DELAY);
	SEND_ACK();
	delay_ms(SHOW_DELAY);
	rotateCubeTF();
	close2and4();
	align();
	SEND_ACK();
}

void showLeftFace() {
	open1and3();
	rotateCubeLR();
	delay_ms(SETTLE_DELAY);
	SEND_ACK();
	delay_ms(SHOW_DELAY);

	rotateCubeRL();
	close1and3();
	align();
	SEND_ACK();
}

void showRightFace() {
	open1and3();
	rotateCubeRL();
	delay_ms(SETTLE_DELAY);
	SEND_ACK();
	delay_ms(SHOW_DELAY);

	rotateCubeLR();
	close1and3();
	align();
	SEND_ACK();
}

void open2and4() {
	setServoPos(SERVO_2,SERVO_2_OPEN);
	setServoPos(SERVO_4,SERVO_4_OPEN);
	delay_ms(SERVO_DELAY);
}

void close2and4() {
	setServoPos(SERVO_2,SERVO_2_CLOSED);
	setServoPos(SERVO_4,SERVO_4_CLOSED);
	delay_ms(SERVO_DELAY);
}

void open1and3() {
	setServoPos(SERVO_1,SERVO_1_OPEN);
	setServoPos(SERVO_3,SERVO_3_OPEN);
	delay_ms(SERVO_DELAY);
}

void close1and3() {
	setServoPos(SERVO_1,SERVO_1_CLOSED);
	setServoPos(SERVO_3,SERVO_3_CLOSED);
	delay_ms(SERVO_DELAY);
}

void showTopFace() {
	open2and4();
	rotateCubeFT();
	close2and4();
	open1and3();
	rotateCubeTF();
	close1and3();
	open2and4();
	rotateCubeTF();

	delay_ms(SETTLE_DELAY);
	SEND_ACK();
	delay_ms(SHOW_DELAY);

	close2and4();
	open1and3();
	rotateCubeTF();
	close1and3();
	align();
	SEND_ACK();
}

void showBottomFace() {
	open2and4();
	rotateCubeFT();
	close2and4();
	open1and3();
	rotateCubeFT();
	close1and3();
	open2and4();
	rotateCubeFT();

	delay_ms(SETTLE_DELAY);
	SEND_ACK();
	delay_ms(SHOW_DELAY);

	close2and4();
	open1and3();
	rotateCubeTF();
	close1and3();
	open2and4();
	setMotorPos(MOTOR_1,STEPPER_180_DEGREE,CCW);
	setMotorPos(MOTOR_3,STEPPER_180_DEGREE,CW);
	delay_ms(STEPPER_H_DELAY);
	close2and4();
	align();
	SEND_ACK();
}
