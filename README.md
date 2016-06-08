# Rubik's Cube Solving Robot

<B>Overview</B>

&emsp;This project consists of a custom built robot that is used to solve a Rubik’s cube.  There are several designs for Rubik’s cube solving robots that already exists and can readily be found by searching the internet. Most of these designs use some type of robot building kit such as a Lego Mindstorms kit or Vex Robotics kit. For this project a commercial robotics kit was not used because the robots and structures built from these kits are not meant to be permanent and can be damaged more easily which was something that was not desired for this project. Because of the need for a more robust and long lasting platform, the mechanical portion of this project was designed from the ground up.

&emsp;The physical robot that was designed consists of a mechanical platform that uses four stepper motors to turn the faces of the Rubik’s cube. It also contains four servo motors that are used to engage and disengage the stepper motors from the cube faces. The four stepper motors and four servos are controlled by a custom motor control board that consists of a 32-bit microcontroller, CPLD (Programmable Complex Logic Device), and four stepper motor driver integrated circuits (ICs). The microcontroller communicates with a PC application over USB.


&emsp;The Java based PC application uses the openCV library to retrieve images from a web camera that is mounted above the Rubik’s cube and pointed at the top face of the cube. These images are then processed using openCV functions and the colors of the nine small cubes that make up each face are determined. Once all six faces have been scanned and processed a solution to solve the cube is calculated and then the moves required to implement that solution are sent from the PC to the microcontroller. The microcontroller then communicates with the CPLD to control the four stepper motors and four server motors in order to execute the required moves on the cube. 
