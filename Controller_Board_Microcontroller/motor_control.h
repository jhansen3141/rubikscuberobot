//Josh Hansen
//Rubik's Cube Robot
//April 2016
#define M1_SPEED_ADDRESS 1
#define M2_SPEED_ADDRESS 2
#define M3_SPEED_ADDRESS 3
#define M4_SPEED_ADDRESS 4
#define M1_MICRO_ADDRESS 5
#define M2_MICRO_ADDRESS 6
#define M3_MICRO_ADDRESS 7
#define M4_MICRO_ADDRESS 8
#define M1_POS_ADDRESS 9
#define M2_POS_ADDRESS 10
#define M3_POS_ADDRESS 11
#define M4_POS_ADDRESS 12
#define M1_EN_ADDRESS 13
#define M2_EN_ADDRESS 14
#define M3_EN_ADDRESS 15
#define M4_EN_ADDRESS 16
#define S1_POS_ADDRESS 17
#define S2_POS_ADDRESS 18
#define S3_POS_ADDRESS 19
#define S4_POS_ADDRESS 20
#define M1_DIR_ADDRESS 21
#define M2_DIR_ADDRESS 22
#define M3_DIR_ADDRESS 23
#define M4_DIR_ADDRESS 24


void fpgaWrite(uint8_t address, uint16_t data);
void motorEnable(uint8_t motorNumber, uint8_t enable);
void setMotorSpeed(uint8_t motorNumber, uint16_t speed);
void setMotorMicroStep(uint8_t motorNumber, uint16_t micoStep);
void motorInterfaceInit();
void setMotorPos(uint8_t motorNumber, uint16_t pos, uint8_t direction);
void setServoPos(uint8_t servoNumber, uint16_t pos);
void latchMotor(uint8_t motor);

