//Josh Hansen
//Rubik's Cube Robot
//April 2016
package application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class FXController {
	
	private boolean isScanned = false;
	
	@FXML
	private Button solveButton;
	
	@FXML
	private Button startButton;
	
	@FXML
	private Button startHSVButton;
	
	@FXML
	private Button startCaptureButton;
	
	@FXML
	private Button connectButton;
	
	@FXML
	private Button disconnectButton;
	
	@FXML
	private Button serRefreshButton;
	
	@FXML
	private ChoiceBox<String> comBox;
	
	@FXML
	private Button frontPButton;
	
	@FXML
	private Button frontNButton;
	
	@FXML
	private Button backPButton;
	
	@FXML
	private Button backNButton;
	
	@FXML
	private Button leftPButton;
	
	@FXML
	private Button leftNButton;
	
	@FXML
	private Button rightPButton;
	
	@FXML
	private Button rightNButton;
	
	@FXML
	private ImageView currentFrame;
	
	@FXML
	private ImageView topImage;
	
	@FXML
	private ImageView frontImage;
	
	@FXML
	private ImageView leftImage;
	
	@FXML
	private ImageView rightImage;
	
	@FXML
	private ImageView bottomImage;
	
	@FXML
	private ImageView backImage;
	
	private ScheduledExecutorService timer;
	private VideoCapture capture = new VideoCapture();
	private boolean cameraActive = false;
	
	private SerialCom serCom;
		
	private Mat currentFrameMat = new Mat();
	private Point recStart = new Point(70,50);
	private Point recEnd = new Point(410,390);
	private Rect cubeRec = new Rect(recStart,recEnd);
	private Mat frontROI;
	private Mat backROI;
	private Mat topROI;
	private Mat bottomROI;
	private Mat rightROI;
	private Mat leftROI;
	
	private StringBuilder frontString;
	private StringBuilder bottomString;
	private StringBuilder leftString;
	private StringBuilder rightString;
	private StringBuilder topString;
	private StringBuilder backString;
	
	private ArrayList<Scalar> colorList;
	private int colorCycler = 0;
	private int cameraNum = 2;

	@FXML
	protected void initialize() {
		serCom = new SerialCom();
		comBox.setItems(serCom.listPorts());
		frontString = new StringBuilder("XXXXXXXXX");
		backString = new StringBuilder("XXXXXXXXX");
		topString = new StringBuilder("XXXXXXXXX");
		bottomString = new StringBuilder("XXXXXXXXX");
		leftString = new StringBuilder("XXXXXXXXX");
		rightString = new StringBuilder("XXXXXXXXX");
		
		colorList = new ArrayList<>();
		colorList.add(Const.colorRed);
		colorList.add(Const.colorGreen);
		colorList.add(Const.colorBlue);
		colorList.add(Const.colorWhite);
		colorList.add(Const.colorOrange);
		colorList.add(Const.colorYellow);
		
		char[] colorToChar = {'R','D','U','F','L','B'};
		
		topImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	 if(isScanned) {
		    		 if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 0 && event.getY() < 50)) { // TL
		    			 Imgproc.circle(topROI,intToCenter(0),10,colorList.get(colorCycler),35);
		    			 topImage.setImage(mat2Image(topROI));	
		    			 topString.setCharAt(0, colorToChar[colorCycler]);
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 0 && event.getY() < 50)) { // TM
		    			 Imgproc.circle(topROI,intToCenter(1),10,colorList.get(colorCycler),35);
		    			 topImage.setImage(mat2Image(topROI));	
		    			 topString.setCharAt(1, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 0 && event.getY() < 50)) { // TR
		    			 Imgproc.circle(topROI,intToCenter(2),10,colorList.get(colorCycler),35);
		    			 topImage.setImage(mat2Image(topROI));	
		    			 topString.setCharAt(2, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 50 && event.getY() < 100)) { // ML
		    			 Imgproc.circle(topROI,intToCenter(3),10,colorList.get(colorCycler),35);
		    			 topImage.setImage(mat2Image(topROI));	
		    			 topString.setCharAt(3, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 50 && event.getY() < 100)) { // MR
		    			 Imgproc.circle(topROI,intToCenter(4),10,colorList.get(colorCycler),35);
		    			 topImage.setImage(mat2Image(topROI));	
		    			 topString.setCharAt(5, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 100 && event.getY() < 150)) { // BL
		    			 Imgproc.circle(topROI,intToCenter(5),10,colorList.get(colorCycler),35);
		    			 topImage.setImage(mat2Image(topROI));	
		    			 topString.setCharAt(6, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 100 && event.getY() < 150)) { // BM
		    			 Imgproc.circle(topROI,intToCenter(6),10,colorList.get(colorCycler),35);
		    			 topImage.setImage(mat2Image(topROI));	
		    			 topString.setCharAt(7, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 100 && event.getY() < 150)) { // BR
		    			 Imgproc.circle(topROI,intToCenter(7),10,colorList.get(colorCycler),35);
		    			 topImage.setImage(mat2Image(topROI));	
		    			 topString.setCharAt(8, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 
		    		 colorCycler++;
		    		 if(colorCycler == 6) {
		    			colorCycler = 0;
		    		 }
		    	 }
//		         System.out.println("X" + event.getX() + " Y" + event.getY());
		         event.consume();
		     }
		});
		
		frontImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	 if(isScanned) {
		    		 if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 0 && event.getY() < 50)) { // TL
		    			 Imgproc.circle(frontROI,intToCenter(0),10,colorList.get(colorCycler),35);
		    			 frontImage.setImage(mat2Image(frontROI));	
		    			 frontString.setCharAt(0, colorToChar[colorCycler]);
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 0 && event.getY() < 50)) { // TM
		    			 Imgproc.circle(frontROI,intToCenter(1),10,colorList.get(colorCycler),35);
		    			 frontImage.setImage(mat2Image(frontROI));	
		    			 frontString.setCharAt(1, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 0 && event.getY() < 50)) { // TR
		    			 Imgproc.circle(frontROI,intToCenter(2),10,colorList.get(colorCycler),35);
		    			 frontImage.setImage(mat2Image(frontROI));	
		    			 frontString.setCharAt(2, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 50 && event.getY() < 100)) { // ML
		    			 Imgproc.circle(frontROI,intToCenter(3),10,colorList.get(colorCycler),35);
		    			 frontImage.setImage(mat2Image(frontROI));	
		    			 frontString.setCharAt(3, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 50 && event.getY() < 100)) { // MR
		    			 Imgproc.circle(frontROI,intToCenter(4),10,colorList.get(colorCycler),35);
		    			 frontImage.setImage(mat2Image(frontROI));	
		    			 frontString.setCharAt(5, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 100 && event.getY() < 150)) { // BL
		    			 Imgproc.circle(frontROI,intToCenter(5),10,colorList.get(colorCycler),35);
		    			 frontImage.setImage(mat2Image(frontROI));	
		    			 frontString.setCharAt(6, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 100 && event.getY() < 150)) { // BM
		    			 Imgproc.circle(frontROI,intToCenter(6),10,colorList.get(colorCycler),35);
		    			 frontImage.setImage(mat2Image(frontROI));	
		    			 frontString.setCharAt(7, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 100 && event.getY() < 150)) { // BR
		    			 Imgproc.circle(frontROI,intToCenter(7),10,colorList.get(colorCycler),35);
		    			 frontImage.setImage(mat2Image(frontROI));	
		    			 frontString.setCharAt(8, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 
		    		 colorCycler++;
		    		 if(colorCycler == 6) {
		    			colorCycler = 0;
		    		 }
		    	 }
//		         System.out.println("X" + event.getX() + " Y" + event.getY());
		         event.consume();
		     }
		});
		
		backImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	 if(isScanned) {
		    		 if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 0 && event.getY() < 50)) { // TL
		    			 Imgproc.circle(backROI,intToCenter(0),10,colorList.get(colorCycler),35);
		    			 backImage.setImage(mat2Image(backROI));	
		    			 backString.setCharAt(0, colorToChar[colorCycler]);
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 0 && event.getY() < 50)) { // TM
		    			 Imgproc.circle(backROI,intToCenter(1),10,colorList.get(colorCycler),35);
		    			 backImage.setImage(mat2Image(backROI));	
		    			 backString.setCharAt(1, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 0 && event.getY() < 50)) { // TR
		    			 Imgproc.circle(backROI,intToCenter(2),10,colorList.get(colorCycler),35);
		    			 backImage.setImage(mat2Image(backROI));	
		    			 backString.setCharAt(2, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 50 && event.getY() < 100)) { // ML
		    			 Imgproc.circle(backROI,intToCenter(3),10,colorList.get(colorCycler),35);
		    			 backImage.setImage(mat2Image(backROI));	
		    			 backString.setCharAt(3, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 50 && event.getY() < 100)) { // MR
		    			 Imgproc.circle(backROI,intToCenter(4),10,colorList.get(colorCycler),35);
		    			 backImage.setImage(mat2Image(backROI));	
		    			 backString.setCharAt(5, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 100 && event.getY() < 150)) { // BL
		    			 Imgproc.circle(backROI,intToCenter(5),10,colorList.get(colorCycler),35);
		    			 backImage.setImage(mat2Image(backROI));	
		    			 backString.setCharAt(6, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 100 && event.getY() < 150)) { // BM
		    			 Imgproc.circle(backROI,intToCenter(6),10,colorList.get(colorCycler),35);
		    			 backImage.setImage(mat2Image(backROI));	
		    			 backString.setCharAt(7, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 100 && event.getY() < 150)) { // BR
		    			 Imgproc.circle(backROI,intToCenter(7),10,colorList.get(colorCycler),35);
		    			 backImage.setImage(mat2Image(backROI));	
		    			 backString.setCharAt(8, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 
		    		 colorCycler++;
		    		 if(colorCycler == 6) {
		    			colorCycler = 0;
		    		 }
		    	 }
//		         System.out.println("X" + event.getX() + " Y" + event.getY());
		         event.consume();
		     }
		});

		rightImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	 if(isScanned) {
		    		 if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 0 && event.getY() < 50)) { // TL
		    			 Imgproc.circle(rightROI,intToCenter(0),10,colorList.get(colorCycler),35);
		    			 rightImage.setImage(mat2Image(rightROI));	
		    			 rightString.setCharAt(0, colorToChar[colorCycler]);
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 0 && event.getY() < 50)) { // TM
		    			 Imgproc.circle(rightROI,intToCenter(1),10,colorList.get(colorCycler),35);
		    			 rightImage.setImage(mat2Image(rightROI));	
		    			 rightString.setCharAt(1, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 0 && event.getY() < 50)) { // TR
		    			 Imgproc.circle(rightROI,intToCenter(2),10,colorList.get(colorCycler),35);
		    			 rightImage.setImage(mat2Image(rightROI));	
		    			 rightString.setCharAt(2, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 50 && event.getY() < 100)) { // ML
		    			 Imgproc.circle(rightROI,intToCenter(3),10,colorList.get(colorCycler),35);
		    			 rightImage.setImage(mat2Image(rightROI));	
		    			 rightString.setCharAt(3, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 50 && event.getY() < 100)) { // MR
		    			 Imgproc.circle(rightROI,intToCenter(4),10,colorList.get(colorCycler),35);
		    			 rightImage.setImage(mat2Image(rightROI));	
		    			 rightString.setCharAt(5, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 100 && event.getY() < 150)) { // BL
		    			 Imgproc.circle(rightROI,intToCenter(5),10,colorList.get(colorCycler),35);
		    			 rightImage.setImage(mat2Image(rightROI));	
		    			 rightString.setCharAt(6, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 100 && event.getY() < 150)) { // BM
		    			 Imgproc.circle(rightROI,intToCenter(6),10,colorList.get(colorCycler),35);
		    			 rightImage.setImage(mat2Image(rightROI));	
		    			 rightString.setCharAt(7, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 100 && event.getY() < 150)) { // BR
		    			 Imgproc.circle(rightROI,intToCenter(7),10,colorList.get(colorCycler),35);
		    			 rightImage.setImage(mat2Image(rightROI));	
		    			 rightString.setCharAt(8, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 
		    		 colorCycler++;
		    		 if(colorCycler == 6) {
		    			colorCycler = 0;
		    		 }
		    	 }
//		         System.out.println("X" + event.getX() + " Y" + event.getY());
		         event.consume();
		     }
		});

		leftImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	 if(isScanned) {
		    		 if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 0 && event.getY() < 50)) { // TL
		    			 Imgproc.circle(leftROI,intToCenter(0),10,colorList.get(colorCycler),35);
		    			 leftImage.setImage(mat2Image(leftROI));	
		    			 leftString.setCharAt(0, colorToChar[colorCycler]);
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 0 && event.getY() < 50)) { // TM
		    			 Imgproc.circle(leftROI,intToCenter(1),10,colorList.get(colorCycler),35);
		    			 leftImage.setImage(mat2Image(leftROI));	
		    			 leftString.setCharAt(1, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 0 && event.getY() < 50)) { // TR
		    			 Imgproc.circle(leftROI,intToCenter(2),10,colorList.get(colorCycler),35);
		    			 leftImage.setImage(mat2Image(leftROI));	
		    			 leftString.setCharAt(2, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 50 && event.getY() < 100)) { // ML
		    			 Imgproc.circle(leftROI,intToCenter(3),10,colorList.get(colorCycler),35);
		    			 leftImage.setImage(mat2Image(leftROI));	
		    			 leftString.setCharAt(3, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 50 && event.getY() < 100)) { // MR
		    			 Imgproc.circle(leftROI,intToCenter(4),10,colorList.get(colorCycler),35);
		    			 leftImage.setImage(mat2Image(leftROI));	
		    			 leftString.setCharAt(5, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 100 && event.getY() < 150)) { // BL
		    			 Imgproc.circle(leftROI,intToCenter(5),10,colorList.get(colorCycler),35);
		    			 leftImage.setImage(mat2Image(leftROI));	
		    			 leftString.setCharAt(6, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 100 && event.getY() < 150)) { // BM
		    			 Imgproc.circle(leftROI,intToCenter(6),10,colorList.get(colorCycler),35);
		    			 leftImage.setImage(mat2Image(leftROI));	
		    			 leftString.setCharAt(7, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 100 && event.getY() < 150)) { // BR
		    			 Imgproc.circle(leftROI,intToCenter(7),10,colorList.get(colorCycler),35);
		    			 leftImage.setImage(mat2Image(leftROI));	
		    			 leftString.setCharAt(8, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 
		    		 colorCycler++;
		    		 if(colorCycler == 6) {
		    			colorCycler = 0;
		    		 }
		    	 }
//		         System.out.println("X" + event.getX() + " Y" + event.getY());
		         event.consume();
		     }
		});
		
		bottomImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	 if(isScanned) {
		    		 if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 0 && event.getY() < 50)) { // TL
		    			 Imgproc.circle(bottomROI,intToCenter(0),10,colorList.get(colorCycler),35);
		    			 bottomImage.setImage(mat2Image(bottomROI));	
		    			 bottomString.setCharAt(0, colorToChar[colorCycler]);
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 0 && event.getY() < 50)) { // TM
		    			 Imgproc.circle(bottomROI,intToCenter(1),10,colorList.get(colorCycler),35);
		    			 bottomImage.setImage(mat2Image(bottomROI));	
		    			 bottomString.setCharAt(1, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 0 && event.getY() < 50)) { // TR
		    			 Imgproc.circle(bottomROI,intToCenter(2),10,colorList.get(colorCycler),35);
		    			 bottomImage.setImage(mat2Image(bottomROI));	
		    			 bottomString.setCharAt(2, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 50 && event.getY() < 100)) { // ML
		    			 Imgproc.circle(bottomROI,intToCenter(3),10,colorList.get(colorCycler),35);
		    			 bottomImage.setImage(mat2Image(bottomROI));	
		    			 bottomString.setCharAt(3, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 50 && event.getY() < 100)) { // MR
		    			 Imgproc.circle(bottomROI,intToCenter(4),10,colorList.get(colorCycler),35);
		    			 bottomImage.setImage(mat2Image(bottomROI));	
		    			 bottomString.setCharAt(5, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 0 && event.getX() < 50)  && (event.getY() >= 100 && event.getY() < 150)) { // BL
		    			 Imgproc.circle(bottomROI,intToCenter(5),10,colorList.get(colorCycler),35);
		    			 bottomImage.setImage(mat2Image(bottomROI));	
		    			 bottomString.setCharAt(6, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 50 && event.getX() < 100)  && (event.getY() >= 100 && event.getY() < 150)) { // BM
		    			 Imgproc.circle(bottomROI,intToCenter(6),10,colorList.get(colorCycler),35);
		    			 bottomImage.setImage(mat2Image(bottomROI));	
		    			 bottomString.setCharAt(7, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 else if((event.getX() >= 100 && event.getX() < 150)  && (event.getY() >= 100 && event.getY() < 150)) { // BR
		    			 Imgproc.circle(bottomROI,intToCenter(7),10,colorList.get(colorCycler),35);
		    			 bottomImage.setImage(mat2Image(bottomROI));	
		    			 bottomString.setCharAt(8, colorToChar[colorCycler]);
		    			 
		    		 }
		    		 
		    		 colorCycler++;
		    		 if(colorCycler == 6) {
		    			colorCycler = 0;
		    		 }
		    	 }
//		         System.out.println("X" + event.getX() + " Y" + event.getY());
		         event.consume();
		     }
		});
	
	}
		
	@FXML
	protected void solveCube() {

		StringBuilder topStringRotated = new StringBuilder(topString);
		StringBuilder frontStringRotated = new StringBuilder(frontString);
		StringBuilder bottomStringRotated = new StringBuilder(bottomString);
		StringBuilder leftStringRotated = new StringBuilder(leftString);
		StringBuilder rightStringRotated = new StringBuilder(rightString);
		
		rotateString180(topStringRotated);

		rotateString180(frontStringRotated);

		rotateString180(bottomStringRotated);

		rotateString90CCW(leftStringRotated);
	
		rotateString90CW(rightStringRotated);
		
		
		System.out.println("Top " + topString.toString() );
		System.out.println("Front " + frontString.toString() );			
		System.out.println("Left " + leftString.toString() );
		System.out.println("Right " + rightString.toString() );				
		System.out.println("Bottom " + bottomString.toString() );
		System.out.println("Back " + backString.toString() );

		Runnable runCapture = new Runnable() {
		@Override
		public void run() {			
			Search search = new Search();
			 if (!Search.isInited()) {
		            Search.init();
			 }
			 String cubeString = topStringRotated.toString() + rightStringRotated.toString() + frontStringRotated.toString() + bottomStringRotated.toString() + leftStringRotated.toString() + backString.toString();
			 System.out.println(cubeString);
			 int mask = 0;
			 long t = System.nanoTime();
			 String result = search.solution(cubeString, 21, 100, 0, mask);;
	
	        while (result.startsWith("Error 8") && ((System.nanoTime() - t) < 5 * 1.0e9)) {
	            result = search.next(100, 0, mask);
	        }
	        t = System.nanoTime() - t;
		        
	        if (result.contains("Error")) {
	            switch (result.charAt(result.length() - 1)) {
	            case '1':
	                result = "There are not exactly nine facelets of each color!";
	                break;
	            case '2':
	                result = "Not all 12 edges exist exactly once!";
	                break;
	            case '3':
	                result = "Flip error: One edge has to be flipped!";
	                break;
	            case '4':
	                result = "Not all 8 corners exist exactly once!";
	                break;
	            case '5':
	                result = "Twist error: One corner has to be twisted!";
	                break;
	            case '6':
	                result = "Parity error: Two corners or two edges have to be exchanged!";
	                break;
	            case '7':
	                result = "No solution exists for the given maximum move number!";
	                break;
	            case '8':
	                result = "Timeout, no solution found within given maximum time!";
	                break;                	
	            }
	            System.out.println(result);
	        }
	        else {
	        	System.out.println(result);
	        	String[] moves = result.split("\\s+");
	        	for(int i=0;i<moves.length;i++) {
	        		if(moves[i].equals("F")) {        			
						serCom.write(Const.F);	
						System.out.println(Const.F);
						while(serCom.read() != 'B');       		
	        		}
	        		else if(moves[i].equals("B")) {
						serCom.write(Const.B);	
						System.out.println(Const.B);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("L")) {
						serCom.write(Const.L);	
						System.out.println(Const.L);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("R")) {
						serCom.write(Const.R);	
						System.out.println(Const.R);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("U")) {
						serCom.write(Const.U);
						System.out.println(Const.U);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("D")) {
						serCom.write(Const.D);	
						System.out.println(Const.D);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("F'")) {
						serCom.write(Const.FN);	
						System.out.println(Const.FN);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("B'")) {
						serCom.write(Const.BN);	
						System.out.println(Const.BN);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("L'")) {
						serCom.write(Const.LN);	
						System.out.println(Const.LN);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("R'")) {
						serCom.write(Const.RN);	
						System.out.println(Const.RN);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("U'")) {
						serCom.write(Const.UN);	
						System.out.println(Const.UN);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("D'")) {
						serCom.write(Const.DN);	
						System.out.println(Const.DN);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("F2")) {
						serCom.write(Const.F2);	
						System.out.println(Const.F2);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("B2")) {
						serCom.write(Const.B2);	
						System.out.println(Const.B2);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("L2")) {
						serCom.write(Const.L2);	
						System.out.println(Const.L2);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("R2")) {
						serCom.write(Const.R2);	
						System.out.println(Const.R2);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("U2")) {
						serCom.write(Const.U2);	
						System.out.println(Const.U2);
						while(serCom.read() != 'B');    	        			
	        		}
	        		else if(moves[i].equals("D2")) {
						serCom.write(Const.D2);	
						System.out.println(Const.D2);
						while(serCom.read() != 'B');    	        			
	        		}

	        	}
	        	
	        }
	        
		}
	};
	Thread captureThread = new Thread(runCapture);
	captureThread.start();
		
	}
		
	@FXML
	protected void startHSV(ActionEvent event) {
		try {
			Parent hsvParent = FXMLLoader.load(getClass().getResource("HSVFX.fxml"));
			Scene hsvScene = new Scene(hsvParent);
			Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			theStage.setScene(hsvScene);
			theStage.show();
			
		}catch(IOException ioe) {
			//ioe.printStackTrace();
		}
	}
		
	@FXML
	protected void moveFrontP(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.moveFrontP);	
		}
	}
	
	@FXML
	protected void moveFrontN(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.moveFrontN);	
		}
	}
	
	@FXML
	protected void moveBackP(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.moveBackP);	
		}
	}
	
	@FXML
	protected void moveBackN(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.moveBackN);	
		}
	}
	
	@FXML
	protected void moveLeftP(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.moveLeftP);	
		}
	}
	
	@FXML
	protected void moveLeftN(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.moveLeftN);	
		}
	}
	
	@FXML
	protected void moveRightP(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.moveRightP);	
		}
	}
	
	@FXML
	protected void moveRightN(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.moveRightN);	
		}
	}
	
	@FXML
	protected void serConnect(ActionEvent event) {
		System.out.println("Connecting");
		String com = comBox.getValue();	
		if(!serCom.isConnected()) {
			serCom.connect(com);
		}
		if(serCom.isConnected()) {
			connectButton.setDisable(true);
		}
	}
	
	@FXML
	protected void serDisconnect(ActionEvent event) {
		System.out.println("Disconnecting");
		if(serCom.isConnected()) {
			serCom.disconnect();
		}
		connectButton.setDisable(false);
	}
	
	@FXML
	protected void serRefresh(ActionEvent event) {
		comBox.setItems(serCom.listPorts());
	}
	
	private void imageViewProperties(ImageView image, int dimension) {
		// set a fixed width for the given ImageView
		image.setFitWidth(dimension);
		// preserve the image ratio
		image.setPreserveRatio(true);
	}
	
	@FXML
	protected void startCapture(ActionEvent event) {
		frontString.setCharAt(4, 'F');
		backString.setCharAt(4,'B');
		leftString.setCharAt(4,'L');
		rightString.setCharAt(4,'R');
		topString.setCharAt(4,'U');
		bottomString.setCharAt(4,'D');
		Runnable runCapture = new Runnable() {
			@Override
			public void run() {			
				if(capture.isOpened() && serCom.isConnected()) {
					serCom.write(Const.showTop);			
					while(serCom.read() != 'A');       		
					topROI = new Mat(currentFrameMat,cubeRec);			
					topROI = detectColor(topROI,topString);
					onFXThread(topImage.imageProperty(), mat2Image(topROI));
					while(serCom.read() != 'A');  
					
					serCom.write(Const.showFront);
					while(serCom.read() != 'A');  
					frontROI = new Mat(currentFrameMat,cubeRec);
					frontROI = detectColor(frontROI,frontString);
					onFXThread(frontImage.imageProperty(), mat2Image(frontROI));
					while(serCom.read() != 'A');  
					
					serCom.write(Const.showLeft);
					while(serCom.read() != 'A');  
					leftROI = new Mat(currentFrameMat,cubeRec);
					leftROI = detectColor(leftROI,leftString);
					onFXThread(leftImage.imageProperty(), mat2Image(leftROI));
					while(serCom.read() != 'A');  
					
					serCom.write(Const.showRight);
					while(serCom.read() != 'A');  
					rightROI = new Mat(currentFrameMat,cubeRec);
					rightROI = detectColor(rightROI,rightString);
					onFXThread(rightImage.imageProperty(), mat2Image(rightROI));
					while(serCom.read() != 'A');  
					
					serCom.write(Const.showBottom);
					while(serCom.read() != 'A');  
					bottomROI = new Mat(currentFrameMat,cubeRec);
					bottomROI = detectColor(bottomROI,bottomString);
					onFXThread(bottomImage.imageProperty(), mat2Image(bottomROI));
					while(serCom.read() != 'A');  
					
					serCom.write(Const.showBack);
					while(serCom.read() != 'A');  
					backROI = new Mat(currentFrameMat,cubeRec);
					backROI = detectColor(backROI,backString);
					onFXThread(backImage.imageProperty(), mat2Image(backROI));
					while(serCom.read() != 'A');  						
					
				}

				isScanned = true;
			}
		};
		Thread captureThread = new Thread(runCapture);
		captureThread.start();
		
	}
		
	void rotateString180(StringBuilder string) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(string.toString().charAt(8));
		sb.append(string.toString().charAt(7));
		sb.append(string.toString().charAt(6));
		sb.append(string.toString().charAt(5));
		sb.append(string.toString().charAt(4));
		sb.append(string.toString().charAt(3));
		sb.append(string.toString().charAt(2));
		sb.append(string.toString().charAt(1));
		sb.append(string.toString().charAt(0));
		
		for(int i=0;i<string.length();i++) {
			string.setCharAt(i, sb.charAt(i));
		}
		
	}
	
	void rotateString90CW(StringBuilder string) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(string.toString().charAt(2));
		sb.append(string.toString().charAt(5));
		sb.append(string.toString().charAt(8));
		sb.append(string.toString().charAt(1));
		sb.append(string.toString().charAt(4));
		sb.append(string.toString().charAt(7));
		sb.append(string.toString().charAt(0));
		sb.append(string.toString().charAt(3));
		sb.append(string.toString().charAt(6));
		
		for(int i=0;i<string.length();i++) {
			string.setCharAt(i, sb.charAt(i));
		}
		
	}
		
	void rotateString90CCW(StringBuilder string) {
		StringBuilder sb = new StringBuilder();
				
		sb.append(string.toString().charAt(6));
		sb.append(string.toString().charAt(3));
		sb.append(string.toString().charAt(0));
		sb.append(string.toString().charAt(7));
		sb.append(string.toString().charAt(4));
		sb.append(string.toString().charAt(1));
		sb.append(string.toString().charAt(8));
		sb.append(string.toString().charAt(5));
		sb.append(string.toString().charAt(2));
		
		for(int i=0;i<string.length();i++) {
			string.setCharAt(i, sb.charAt(i));
		}
		
	}
	
	@FXML
	protected void startCamera(ActionEvent event) {	
		this.imageViewProperties(this.currentFrame, 500);
		//this.imageViewProperties(this.cubeImage, 200);
	
		if (!this.cameraActive) {
			// start the video capture
			this.capture.open(cameraNum);
			this.capture.set(11, 30);

			// is the video stream available?
			if (this.capture.isOpened())
			{
				this.cameraActive = true;
				
				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run()
					{
						Image imageToShow = grabFrame();
						currentFrame.setImage(imageToShow);
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				// update the button content
				this.startButton.setText("Stop Camera");
			}
			else
			{
				// log the error
				System.err.println("Unable to open the camera connection...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.startButton.setText("Start Camera");
			
			// stop the timer
			try
			{
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log the exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
			
			// release the camera
			this.capture.release();
			// clean the frame
			this.currentFrame.setImage(null);
		}
	}
	
	private Image grabFrame() {
		
		Image imageToShow = null;
		if (this.capture.isOpened()) {
			try {
				this.capture.read(currentFrameMat);				
				if (!currentFrameMat.empty()) {
					Scalar recColor = new Scalar(255,0,255);
					Imgproc.rectangle(currentFrameMat, recStart, recEnd, recColor, 4);
					imageToShow = mat2Image(currentFrameMat);
				}			
			}
			catch (Exception e) {
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return imageToShow;
	}
	
	Mat getHSVAvg(Mat cubeFrame) {
		int cubieH = 25; //113 58
		int cubieW = 25;
		int xOffset = (113 - cubieW)/2; // 26
		int yOffset = (113 - cubieH)/2;
		Mat detectedMat = new Mat();
		cubeFrame.copyTo(detectedMat);
	
		Rect TLRec = new Rect(1+xOffset,1+yOffset,cubieH,cubieW);
		Rect TMRec = new Rect(113+xOffset,1+yOffset,cubieH,cubieW);
		Rect TRRec = new Rect(226+xOffset,1+yOffset,cubieH,cubieW);
		Rect MLRec = new Rect(1+xOffset,113+yOffset,cubieH,cubieW);
		Rect MRRec = new Rect(226+xOffset,113+yOffset,cubieH,cubieW);		
		Rect BLRec = new Rect(1+xOffset,226+yOffset,cubieH,cubieW);
		Rect BMRec = new Rect(113+xOffset,226+yOffset,cubieH,cubieW);
		Rect BRRec = new Rect(226+xOffset,226+yOffset,cubieH,cubieW);
		
		Mat TLRoi = new Mat(cubeFrame,TLRec);
		Mat TMRoi = new Mat(cubeFrame,TMRec);
		Mat TRRoi = new Mat(cubeFrame,TRRec);		
		Mat MLRoi = new Mat(cubeFrame,MLRec);
		Mat MRRoi = new Mat(cubeFrame,MRRec);	
		Mat BLRoi = new Mat(cubeFrame,BLRec);
		Mat BMRoi = new Mat(cubeFrame,BMRec);
		Mat BRRoi = new Mat(cubeFrame,BRRec);
		
		Imgproc.medianBlur(TLRoi, TLRoi, 21);
		Imgproc.medianBlur(TMRoi, TMRoi, 21);
		Imgproc.medianBlur(TRRoi, TRRoi, 21);
		Imgproc.medianBlur(MLRoi, MLRoi, 21);
		Imgproc.medianBlur(MRRoi, MRRoi, 21);
		Imgproc.medianBlur(BLRoi, BLRoi, 21);
		Imgproc.medianBlur(BMRoi, BMRoi, 21);
		Imgproc.medianBlur(BRRoi, BRRoi, 21);
		
		ArrayList<Mat> cubieList = new ArrayList<Mat>();
		cubieList.add(TLRoi);
		cubieList.add(TMRoi);
		cubieList.add(TRRoi);
		cubieList.add(MLRoi);
		cubieList.add(MRRoi);
		cubieList.add(BLRoi);
		cubieList.add(BMRoi);
		cubieList.add(BRRoi);
		
		for(int i=0;i<cubieList.size();i++) {
			Mat hsvImage = new Mat();
			
			Imgproc.cvtColor(cubieList.get(i), hsvImage, Imgproc.COLOR_BGR2HSV);
			Scalar avgH = Core.mean(hsvImage);
			
			double hueValue = avgH.val[0];
			double lValue = avgH.val[2];
			double sValue = avgH.val[1];
			System.out.println(hueValue + "   " + sValue + "   "+ lValue);
		}
		return detectedMat;
	}
	
	Mat detectColor(Mat cubeFrame, StringBuilder cubeString) {
		int cubieH = 25; //113 58
		int cubieW = 25;
		int xOffset = (113 - cubieW)/2; // 26
		int yOffset = (113 - cubieH)/2;
		
		
		Point whiteAvg = new Point(57.19,41.69);
		Point orangeAvg = new Point(13,255);
		Point yellowAvg = new Point(27.7,208.0);
		Point redAvg = new Point(1.0,246.68);
		Point blueAvg = new Point(107.52,208.52);
		Point greenAvg = new Point(79.77,200.06);
		
		ArrayList<Point> avgPointList = new ArrayList<Point>();
		avgPointList.add(whiteAvg);
		avgPointList.add(orangeAvg);
		avgPointList.add(yellowAvg);
		avgPointList.add(redAvg);
		avgPointList.add(blueAvg);
		avgPointList.add(greenAvg);
		
		Mat detectedMat = new Mat();
		cubeFrame.copyTo(detectedMat);

		Rect TLRec = new Rect(1+xOffset,1+yOffset,cubieH,cubieW);
		Rect TMRec = new Rect(113+xOffset,1+yOffset,cubieH,cubieW);
		Rect TRRec = new Rect(226+xOffset,1+yOffset,cubieH,cubieW);
		Rect MLRec = new Rect(1+xOffset,113+yOffset,cubieH,cubieW);
		Rect MRRec = new Rect(226+xOffset,113+yOffset,cubieH,cubieW);		
		Rect BLRec = new Rect(1+xOffset,226+yOffset,cubieH,cubieW);
		Rect BMRec = new Rect(113+xOffset,226+yOffset,cubieH,cubieW);
		Rect BRRec = new Rect(226+xOffset,226+yOffset,cubieH,cubieW);
		
			
		Mat TLRoi = new Mat(cubeFrame,TLRec);
		Mat TMRoi = new Mat(cubeFrame,TMRec);
		Mat TRRoi = new Mat(cubeFrame,TRRec);		
		Mat MLRoi = new Mat(cubeFrame,MLRec);
		Mat MRRoi = new Mat(cubeFrame,MRRec);	
		Mat BLRoi = new Mat(cubeFrame,BLRec);
		Mat BMRoi = new Mat(cubeFrame,BMRec);
		Mat BRRoi = new Mat(cubeFrame,BRRec);
		
		Imgproc.medianBlur(TLRoi, TLRoi, 21);
		Imgproc.medianBlur(TMRoi, TMRoi, 21);
		Imgproc.medianBlur(TRRoi, TRRoi, 21);
		Imgproc.medianBlur(MLRoi, MLRoi, 21);
		Imgproc.medianBlur(MRRoi, MRRoi, 21);
		Imgproc.medianBlur(BLRoi, BLRoi, 21);
		Imgproc.medianBlur(BMRoi, BMRoi, 21);
		Imgproc.medianBlur(BRRoi, BRRoi, 21);
		
		ArrayList<Mat> cubieList = new ArrayList<Mat>();
		cubieList.add(TLRoi);
		cubieList.add(TMRoi);
		cubieList.add(TRRoi);
		cubieList.add(MLRoi);
		cubieList.add(MRRoi);
		cubieList.add(BLRoi);
		cubieList.add(BMRoi);
		cubieList.add(BRRoi);
		
		for(int i=0;i<cubieList.size();i++) {
			Mat hsvImage = new Mat();
			
			Imgproc.cvtColor(cubieList.get(i), hsvImage, Imgproc.COLOR_BGR2HSV);
			Scalar avgH = Core.mean(hsvImage);
			
			double hueValue = avgH.val[0];
			double sValue = avgH.val[1];
			double lValue = avgH.val[2];
			Point avg = new Point(hueValue,sValue);
			System.out.println(hueValue + "   " + sValue + "    "+ lValue + "    " + intToCenter(i).toString());
			double oldDistance = 9999.99;
			int color = 0;
			
			if(hueValue >= 24.8 && hueValue <= 40 && sValue >= 50) { // its yellow
				Imgproc.circle(detectedMat,intToCenter(i),10,Const.colorYellow,35);	
				if(i < 4) {
					cubeString.setCharAt(i, Const.BACK);
				}
				else if(i >= 4) {
					cubeString.setCharAt(i+1, Const.BACK);
				}
			}
			else {
				for(int j=0;j<avgPointList.size();j++) {
					double currentDistance = calcDistance(avg,avgPointList.get(j));
					if(currentDistance < oldDistance) {
						oldDistance = currentDistance;
						color = j;
					}
				}
				switch(color) {
					case 0: // white
						Imgproc.circle(detectedMat,intToCenter(i),10,Const.colorWhite,35);	// it's white
						if(i < 4) {
							cubeString.setCharAt(i, Const.FRONT);
						}
						else if(i >= 4) {
							cubeString.setCharAt(i+1, Const.FRONT);
						}
						break;
					case 1: // orange
						Imgproc.circle(detectedMat,intToCenter(i),10,Const.colorOrange,35);	
						if(i < 4) {
							cubeString.setCharAt(i, Const.LEFT);
						}
						else if(i >= 4) {
							cubeString.setCharAt(i+1, Const.LEFT);
						}
						break;
					case 2: // yellow
						Imgproc.circle(detectedMat,intToCenter(i),10,Const.colorYellow,35);	
						if(i < 4) {
							cubeString.setCharAt(i, Const.BACK);
						}
						else if(i >= 4) {
							cubeString.setCharAt(i+1, Const.BACK);
						}
						break;
					case 3: // red
						Imgproc.circle(detectedMat,intToCenter(i),10,Const.colorRed,35);
						if(i < 4) {
							cubeString.setCharAt(i, Const.RIGHT);
						}
						else if(i >= 4) {
							cubeString.setCharAt(i+1, Const.RIGHT);
						}
						break;
					case 4: // blue
						Imgproc.circle(detectedMat,intToCenter(i),10,Const.colorBlue,35);	
						if(i < 4) {
							cubeString.setCharAt(i, Const.TOP);
						}
						else if(i >= 4) {
							cubeString.setCharAt(i+1, Const.TOP);
						}
						break;
					case 5: // green
						Imgproc.circle(detectedMat,intToCenter(i),10,Const.colorGreen,35);	
						if(i < 4) {
							cubeString.setCharAt(i, Const.BOTTOM);
						}
						else if(i >= 4) {
							cubeString.setCharAt(i+1, Const.BOTTOM);
						}
						break;
				}	
			}
			
		}
		System.out.println();
		return detectedMat;
	}
		
	void printHSVValues(Scalar minValues, Scalar maxValues, String colorStr) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		String valuesToPrint = colorStr + " :Hue range: " + df.format(minValues.val[0]) + "-" + df.format(maxValues.val[0])
		+ "\tSaturation range: " + df.format(minValues.val[1]) + "-" + df.format(maxValues.val[1]) + "\tValue range: "
		+ df.format(minValues.val[2]) + "-" + df.format(maxValues.val[2]);
		System.out.println(valuesToPrint);
	}
	
	double calcDistance(Point point1, Point point2){
		double diffX = point2.x - point1.x;
		double diffY = point2.y - point1.y;		
		return Math.sqrt((diffX*diffX) + (diffY*diffY));
	}
	
	Const.Color scalarToColor(Scalar color) {
		Const.Color returnColor = null;
		if(color == Const.colorBlue) {
			returnColor = Const.Color.BLUE;
		}
		else if(color == Const.colorGreen) {
			returnColor = Const.Color.GREEN;
		}
		else if(color == Const.colorWhite) {
			returnColor = Const.Color.WHITE;
		}
		else if(color == Const.colorOrange) {
			returnColor = Const.Color.ORANGE;
		}
		else if(color == Const.colorRed) {
			returnColor = Const.Color.RED;
		}
		else {
			returnColor = Const.Color.YELLOW;
		}
		
		return returnColor;
	}
	
	Scalar intToColor(int color) {
		Scalar colorIn = Const.colorWhite;
		switch(color) {
			case 0: // red
				colorIn = Const.colorRed;
				break;
			case 1: // green
				colorIn = Const.colorGreen;
				break;
			case 2: // blue
				colorIn =  Const.colorBlue;
				break;
			case 3: // white
				colorIn = Const.colorWhite;
				break;
			case 4: // yellow
				colorIn = Const.colorYellow;
				break;
			case 5: // orange
				colorIn = Const.colorOrange;
				break;
		}
		return colorIn;
	}
	
	Point intToCenter(int cubie) {
		Point center = new Point(0,0);
		switch(cubie) {
			case 0: // TL
				center = new Point(57,57);
				break;
			case 1: // TM
				center = new Point(170,57);
				break;
			case 2: // TR
				center = new Point(283,57);
				break;
			case 3: // ML
				center = new Point(57,170);
				break;
			case 4: // MR
				center = new Point(283,170);
				break;
			case 5: // BL
				center = new Point(57,283);
				break;
			case 6: // BM
				center = new Point(170,283);
				break;
			case 7: // BR
				center = new Point(283,283);	
				break; 
		}
		return center;
	}
	
	private <T> void onFXThread(final ObjectProperty<T> property, final T value) {
		Platform.runLater(new Runnable() {		
			@Override
			public void run() {
				property.set(value);
			}
		});
	}
		
	private Image mat2Image(Mat frame) {
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".png", frame, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
}