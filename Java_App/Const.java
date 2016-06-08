//Josh Hansen
//Rubik's Cube Robot
//April 2016
package application;

import org.opencv.core.Scalar;

public class Const {
	public static Scalar colorRed = new Scalar(0,0,255);
	public static Scalar colorGreen = new Scalar(0,255,0);
	public static Scalar colorBlue = new Scalar(255,0,0);
	public static Scalar colorOrange = new Scalar(0,128,255);
	public static Scalar colorYellow = new Scalar(0,255,255);
	public static Scalar colorWhite = new Scalar(255,255,255);
	public static Scalar colorBlack = new Scalar(0,0,0);
	public static enum Direction {CCW,CW};
	public static enum Color{WHITE,RED,GREEN,BLUE,YELLOW,ORANGE};
	public static enum Face{FRONT,BACK,LEFT,RIGHT,TOP,BOTTOM};
	public static enum Move{F,B,U,D,L,R,FC,BC,UC,DC,LC,RC,F2,B2,U2,D2,L2,R2,FC2,BC2,UC2,DC2,LC2,RC2};
	public static enum Corner{FTR,FTL,FBR,FBL,BTR,BTL,BBL,BBR};
	public static enum LR{R,L};
	public static int PHASE1_MAX_LEVEL = 1082565;
	public static int AREA = 75;
	public static String showFront = "sfnt\r";
	public static String showBack = "sbck\r";
	public static String showTop = "stop\r";
	public static String showBottom = "sbtm\r";
	public static String showLeft = "slft\r";
	public static String showRight = "srht\r";
	
	public static String moveFrontP = "mf1 1\r";
	public static String moveBackP = "mb1 1\r";
	public static String moveLeftP = "ml1 1\r";
	public static String moveRightP = "mr1 1\r";
	
	public static String moveFrontN = "mf1 2\r";
	public static String moveBackN = "mb1 2\r";
	public static String moveLeftN = "ml1 2\r";
	public static String moveRightN = "mr1 2\r";
	
	public static String B = "rqbck 1\r";
	public static String BN = "rqbck 2\r";
	
	public static String F = "rqfnt 1\r";
	public static String FN = "rqfnt 2\r";
	
	public static String L = "rqlft 1\r";
	public static String LN = "rqlft 2\r";
	
	public static String R = "rqrht 1\r";
	public static String RN = "rqrht 2\r";
	
	public static String U = "rqtop 1\r";
	public static String UN = "rqtop 2\r";
	
	public static String D = "rqbtm 1\r";
	public static String DN = "rqbtm 2\r";
	
	public static String F2 = "rhfnt\r";
	public static String B2 = "rhbck\r";
	public static String L2 = "rhlft\r";
	public static String R2 = "rhrht\r";
	public static String U2 = "rhtop\r";
	public static String D2 = "rhbtm\r";
	
	public static char FRONT = 'F';
	public static char TOP = 'U';
	public static char BOTTOM = 'D';
	public static char LEFT = 'L';
	public static char RIGHT = 'R';
	public static char BACK = 'B';
	
}
