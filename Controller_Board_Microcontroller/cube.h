//Josh Hansen
//Rubik's Cube Robot
//April 2016

#ifndef CUBE_H_
#define CUBE_H_

#define SERVO_1_OPEN 910
#define SERVO_2_OPEN 920
#define SERVO_3_OPEN 920
#define SERVO_4_OPEN 950

#define SERVO_1_CLOSED 190
#define SERVO_2_CLOSED 190
#define SERVO_3_CLOSED 190
#define SERVO_4_CLOSED 190

#define SERVO_DELAY 350
#define STEPPER_H_DELAY 1200
#define STEPPER_Q_DELAY 900
#define SHOW_DELAY 1000

#define STEPPER_90_DEGREE 200
#define STEPPER_180_DEGREE 400

#define SETTLE_DELAY 0

#define ACK "A"
#define SEND_ACK() printf("A")
#define SEND_ACKB() printf("B")

typedef enum{CW = 0,CCW = 1} Direction;
typedef enum{FRONT,BACK,TOP,BOTTOM,LEFT,RIGHT} Face;

void rotateHRightFace();
void rotateHLeftFace();
void rotateHFrontFace();
void rotateHBackFace();
void rotateHTopFace();
void rotateHBottomFace();
void rotateQRightFace(Direction direction);
void rotateQLeftFace(Direction direction);
void rotateQFrontFace(Direction direction);
void rotateQBackFace(Direction direction);
void rotateQTopFace(Direction direction);
void rotateQBottomFace(Direction direction);
void showFrontFace();
void showBackFace();
void showRightFace();
void showLeftFace();
void showTopFace();
void showBottomFace();
void close1and3();
void open1and3();
void close2and4();
void open2and4();
void rotateCubeRL();
void rotateCubeLR();
void align();
void moveFront1(Direction direction);
void moveBack1(Direction direction);
void moveLeft1(Direction direction);
void moveRight1(Direction direction);

#endif /* CUBE_H_ */
