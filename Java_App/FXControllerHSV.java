//Josh Hansen
//Rubik's Cube Robot
//April 2016
package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FXControllerHSV {
	
	@FXML
	private Button connectButton;
	
	@FXML
	private Button disconnectButton;
	
	@FXML
	private Button serRefreshButton;

	@FXML
	private Button backButton;
	
	@FXML
	private Button startButton;
	
	@FXML
	private Button setRedButton;
	
	@FXML
	private Button setBlueButton;
	
	@FXML
	private Button setGreenButton;
	
	@FXML
	private Button setWhiteButton;
	
	@FXML
	private Button setOrangeButton;
	
	@FXML
	private Button setYellowButton;
	
	@FXML
	private Button loadRedButton;
	
	@FXML
	private Button loadBlueButton;
	
	@FXML
	private Button loadGreenButton;
	
	@FXML
	private Button loadWhiteButton;
	
	@FXML
	private Button loadOrangeButton;
	
	@FXML
	private Button loadYellowButton;
	
	@FXML
	private Button showFrontButton;
	
	@FXML
	private Button showBackButton;
	
	@FXML
	private Button showLeftButton;
	
	@FXML
	private Button showRightButton;
	
	@FXML
	private Button showTopButton;
	
	@FXML
	private Button showBottomButton;
	
	@FXML
	private ImageView originalFrame;
	
	@FXML
	private ImageView maskImage;
	
	@FXML
	private ImageView morphImage;
	
	@FXML 
	private Slider hStartSlider;
	
	@FXML 
	private Slider hStopSlider;
	
	@FXML 
	private Slider sStartSlider;
	
	@FXML 
	private Slider sStopSlider;
	
	@FXML 
	private Slider vStartSlider;
	
	@FXML 
	private Slider vStopSlider;
	
	@FXML
	private Label hsvCurrentValues;
	
	@FXML
	private ChoiceBox<String> comBox;
	
	private SerialCom serCom;
	private ScheduledExecutorService timer;
	private VideoCapture capture = new VideoCapture();
	private boolean cameraActive;
	private ObjectProperty<String> hsvValuesProp;
	
	private String redHSVFileName = "redHSV.hsv";
	private String greenHSVFileName = "greenHSV.hsv";
	private String blueHSVFileName = "blueHSV.hsv";
	private String orangeHSVFileName = "orangeHSV.hsv";
	private String whiteHSVFileName = "whiteHSV.hsv";
	private String yellowHSVFileName = "yellowHSV.hsv";
	
	@FXML
	protected void serConnect(ActionEvent event) {
		System.out.println("Connecting");
		String com = comBox.getValue();	
		if(!serCom.isConnected()) {
			serCom.connect(com);
		}
	}
	
	@FXML
	protected void serDisconnect(ActionEvent event) {
		System.out.println("Disconnecting");
		if(serCom.isConnected()) {
			serCom.disconnect();
		}
	}
	
	@FXML
	protected void serRefresh(ActionEvent event) {
		comBox.setItems(serCom.listPorts());
	}
	
	@FXML
	protected void showFront(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.showFront);	
		}	
	}
	
	@FXML
	protected void showBack(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.showBack);	
		}
	}
	
	@FXML
	protected void showLeft(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.showLeft);	
		}
	}
	
	@FXML
	protected void showRight(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.showRight);	
		}
	}
	
	@FXML
	protected void showTop(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.showTop);	
		}
	}
	
	@FXML
	protected void showBottom(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.write(Const.showBottom);	
		}
	}
	
	@FXML
	protected void initialize() {
		serCom = new SerialCom();
		comBox.setItems(serCom.listPorts());
	}
	
	@FXML
	protected void startCamera(ActionEvent event) {
		hsvValuesProp = new SimpleObjectProperty<>();
		this.hsvCurrentValues.textProperty().bind(hsvValuesProp);
		
		this.imageViewProperties(this.originalFrame, 400);
		this.imageViewProperties(this.maskImage, 200);
		this.imageViewProperties(this.morphImage, 200);
		
		if (!this.cameraActive) {
			// start the video capture
			this.capture.open(0);
			this.capture.set(11, 30);
			// is the video stream available?
			if (this.capture.isOpened()) {
				this.cameraActive = true;
				
				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run() {
						Image imageToShow = grabFrame();
						originalFrame.setImage(imageToShow);
					}
				};
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				// update the button content
				this.startButton.setText("Stop Camera");
			}
			else{
				// log the error
				System.err.println("Failed to open the camera connection...");
			}
		}
		else {
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.startButton.setText("Start Camera");
			
			// stop the timer
			try {
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				// log the exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
			// release the camera
			this.capture.release();
		}
	
	}
	
	private Image grabFrame() {
		// init everything
		Image imageToShow = null;
		Mat frame = new Mat();
		DecimalFormat df = new DecimalFormat("0.00"); 
		
		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);
				
				// if the frame is not empty, process it
				if (!frame.empty()) {
					// init
					Mat blurredImage = new Mat();
					Mat hsvImage = new Mat();
					Mat mask = new Mat();
					Mat morphOutput = new Mat();
					
					// remove some noise
					//Imgproc.medianBlur(frame, blurredImage, 21);
					
					
					// convert the frame to HSV
					Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);
					
					// get thresholding values from the UI
					// remember: H ranges 0-180, S and V range 0-255
					Scalar minValues = new Scalar(this.hStartSlider.getValue(), this.sStartSlider.getValue(),
							this.vStartSlider.getValue());
					Scalar maxValues = new Scalar(this.hStopSlider.getValue(), this.sStopSlider.getValue(),
							this.vStopSlider.getValue());
					
					// show the current selected HSV range					
				
					String valuesToPrint = "Hue range: " + df.format(minValues.val[0]) + "-" + df.format(maxValues.val[0])
							+ "\tSaturation range: " + df.format(minValues.val[1]) + "-" + df.format(maxValues.val[1]) + "\tValue range: "
							+ df.format(minValues.val[2]) + "-" + df.format(maxValues.val[2]);
					this.onFXThread(this.hsvValuesProp, valuesToPrint);
			
					Core.inRange(hsvImage, minValues, maxValues, mask);
					// show the partial output
					this.onFXThread(this.maskImage.imageProperty(), this.mat2Image(mask));
					
					// morphological operators
					// dilate with large element, erode with small ones
					Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)); // 24 24
					Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)); // 12 12
					
					Imgproc.erode(mask, morphOutput, erodeElement);
					Imgproc.erode(mask, morphOutput, erodeElement);
					
					Imgproc.dilate(mask, morphOutput, dilateElement);
					Imgproc.dilate(mask, morphOutput, dilateElement);
					
					// show the partial output
					this.onFXThread(this.morphImage.imageProperty(), this.mat2Image(morphOutput));
					
					// find the tennis ball(s) contours and show them
					//frame = this.findAndDrawBalls(morphOutput, frame);
					
					// convert the Mat object (OpenCV) to Image (JavaFX)
					imageToShow = mat2Image(frame);
				}
				
			}
			catch (Exception e) {
				// log the (full) error
				System.err.print("ERROR");
				e.printStackTrace();
			}
		}
		
		return imageToShow;
	}
	
	private void imageViewProperties(ImageView image, int dimension) {
		// set a fixed width for the given ImageView
		image.setFitWidth(dimension);
		// preserve the image ratio
		image.setPreserveRatio(true);
	}
	
	private Image mat2Image(Mat frame) {
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
	private <T> void onFXThread(final ObjectProperty<T> property, final T value) {
		Platform.runLater(new Runnable() {		
			@Override
			public void run()
			{
				property.set(value);
			}
		});
	}
	
	@FXML
	protected void goBack(ActionEvent event) {
		if(serCom.isConnected()) {
			serCom.disconnect();
		}
		try {
			Parent mainParent = FXMLLoader.load(getClass().getResource("RubikFX.fxml"));
			Scene mainScene = new Scene(mainParent);
			Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		
			theStage.setScene(mainScene);
			theStage.show();
			
		}catch(IOException ioe) {
			//ioe.printStackTrace();
		}
	}
	
	@FXML
	protected void setRed(ActionEvent event) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		String hStart = df.format(hStartSlider.getValue());
		String hStop = df.format(hStopSlider.getValue());
		String sStart = df.format(sStartSlider.getValue());
		String sStop = df.format(sStopSlider.getValue());
		String vStart = df.format(vStartSlider.getValue());
		String vStop = df.format(vStopSlider.getValue());
		try {
            FileWriter fileWriter = new FileWriter(redHSVFileName);
            BufferedWriter bufferedWriter =  new BufferedWriter(fileWriter);
            bufferedWriter.write(hStart);
            bufferedWriter.newLine();
            bufferedWriter.write(hStop);
            bufferedWriter.newLine();
            bufferedWriter.write(sStart);
            bufferedWriter.newLine();
            bufferedWriter.write(sStop);
            bufferedWriter.newLine();
            bufferedWriter.write(vStart);
            bufferedWriter.newLine();
            bufferedWriter.write(vStop);
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"+ redHSVFileName + "'");
        }
	}
	
	@FXML
	protected void setGreen(ActionEvent event) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		String hStart = df.format(hStartSlider.getValue());
		String hStop = df.format(hStopSlider.getValue());
		String sStart = df.format(sStartSlider.getValue());
		String sStop = df.format(sStopSlider.getValue());
		String vStart = df.format(vStartSlider.getValue());
		String vStop = df.format(vStopSlider.getValue());
		try {
            FileWriter fileWriter = new FileWriter(greenHSVFileName);
            BufferedWriter bufferedWriter =  new BufferedWriter(fileWriter);
            bufferedWriter.write(hStart);
            bufferedWriter.newLine();
            bufferedWriter.write(hStop);
            bufferedWriter.newLine();
            bufferedWriter.write(sStart);
            bufferedWriter.newLine();
            bufferedWriter.write(sStop);
            bufferedWriter.newLine();
            bufferedWriter.write(vStart);
            bufferedWriter.newLine();
            bufferedWriter.write(vStop);
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"+ greenHSVFileName + "'");
        }	
	}
	
	@FXML
	protected void setBlue(ActionEvent event) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		String hStart = df.format(hStartSlider.getValue());
		String hStop = df.format(hStopSlider.getValue());
		String sStart = df.format(sStartSlider.getValue());
		String sStop = df.format(sStopSlider.getValue());
		String vStart = df.format(vStartSlider.getValue());
		String vStop = df.format(vStopSlider.getValue());
		try {
            FileWriter fileWriter = new FileWriter(blueHSVFileName);
            BufferedWriter bufferedWriter =  new BufferedWriter(fileWriter);
            bufferedWriter.write(hStart);
            bufferedWriter.newLine();
            bufferedWriter.write(hStop);
            bufferedWriter.newLine();
            bufferedWriter.write(sStart);
            bufferedWriter.newLine();
            bufferedWriter.write(sStop);
            bufferedWriter.newLine();
            bufferedWriter.write(vStart);
            bufferedWriter.newLine();
            bufferedWriter.write(vStop);
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"+ blueHSVFileName + "'");
        }	
	}
	
	@FXML
	protected void setOrange(ActionEvent event) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		String hStart = df.format(hStartSlider.getValue());
		String hStop = df.format(hStopSlider.getValue());
		String sStart = df.format(sStartSlider.getValue());
		String sStop = df.format(sStopSlider.getValue());
		String vStart = df.format(vStartSlider.getValue());
		String vStop = df.format(vStopSlider.getValue());
		try {
            FileWriter fileWriter = new FileWriter(orangeHSVFileName);
            BufferedWriter bufferedWriter =  new BufferedWriter(fileWriter);
            bufferedWriter.write(hStart);
            bufferedWriter.newLine();
            bufferedWriter.write(hStop);
            bufferedWriter.newLine();
            bufferedWriter.write(sStart);
            bufferedWriter.newLine();
            bufferedWriter.write(sStop);
            bufferedWriter.newLine();
            bufferedWriter.write(vStart);
            bufferedWriter.newLine();
            bufferedWriter.write(vStop);
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"+ orangeHSVFileName + "'");
        }	
	}
	
	@FXML
	protected void setYellow(ActionEvent event) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		String hStart = df.format(hStartSlider.getValue());
		String hStop = df.format(hStopSlider.getValue());
		String sStart = df.format(sStartSlider.getValue());
		String sStop = df.format(sStopSlider.getValue());
		String vStart = df.format(vStartSlider.getValue());
		String vStop = df.format(vStopSlider.getValue());
		try {
            FileWriter fileWriter = new FileWriter(yellowHSVFileName);
            BufferedWriter bufferedWriter =  new BufferedWriter(fileWriter);
            bufferedWriter.write(hStart);
            bufferedWriter.newLine();
            bufferedWriter.write(hStop);
            bufferedWriter.newLine();
            bufferedWriter.write(sStart);
            bufferedWriter.newLine();
            bufferedWriter.write(sStop);
            bufferedWriter.newLine();
            bufferedWriter.write(vStart);
            bufferedWriter.newLine();
            bufferedWriter.write(vStop);
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"+ yellowHSVFileName + "'");
        }	
	}

	@FXML
	protected void setWhite(ActionEvent event) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		String hStart = df.format(hStartSlider.getValue());
		String hStop = df.format(hStopSlider.getValue());
		String sStart = df.format(sStartSlider.getValue());
		String sStop = df.format(sStopSlider.getValue());
		String vStart = df.format(vStartSlider.getValue());
		String vStop = df.format(vStopSlider.getValue());
		try {
            FileWriter fileWriter = new FileWriter(whiteHSVFileName);
            BufferedWriter bufferedWriter =  new BufferedWriter(fileWriter);
            bufferedWriter.write(hStart);
            bufferedWriter.newLine();
            bufferedWriter.write(hStop);
            bufferedWriter.newLine();
            bufferedWriter.write(sStart);
            bufferedWriter.newLine();
            bufferedWriter.write(sStop);
            bufferedWriter.newLine();
            bufferedWriter.write(vStart);
            bufferedWriter.newLine();
            bufferedWriter.write(vStop);
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"+ whiteHSVFileName + "'");
        }	
	}
	
	@FXML
	protected void loadRed(ActionEvent event) {
      loadFromFile(redHSVFileName); 
	}
	
	@FXML
	protected void loadBlue(ActionEvent event) {
		loadFromFile(blueHSVFileName); 
	}
	
	@FXML
	protected void loadGreen(ActionEvent event) {
      loadFromFile(greenHSVFileName); 
	}
	
	@FXML
	protected void loadYellow(ActionEvent event) {
		loadFromFile(yellowHSVFileName); 
	}
	
	@FXML
	protected void loadWhite(ActionEvent event) {
		loadFromFile(whiteHSVFileName); 
	}
	
	@FXML
	protected void loadOrange(ActionEvent event) {
		loadFromFile(orangeHSVFileName); 
	}
	
	private void loadFromFile(String fileName) {
        String line = null;
        try {
           // FileReader reads text files in the default encoding.
           FileReader fileReader = new FileReader(fileName);

           // Always wrap FileReader in BufferedReader.
           BufferedReader bufferedReader = new BufferedReader(fileReader);

           line = bufferedReader.readLine();
           double hStart = Double.parseDouble(line);
           
           line = bufferedReader.readLine();
           double hStop = Double.parseDouble(line);
           
           line = bufferedReader.readLine();
           double sStart = Double.parseDouble(line);
           
           line = bufferedReader.readLine();
           double sStop = Double.parseDouble(line);
           
           line = bufferedReader.readLine();
           double vStart = Double.parseDouble(line);
           
           line = bufferedReader.readLine();
           double vStop = Double.parseDouble(line);

           bufferedReader.close();  
           hStartSlider.setValue(hStart);
           sStartSlider.setValue(sStart);
           vStartSlider.setValue(vStart);
           hStopSlider.setValue(hStop);
           sStopSlider.setValue(sStop);
           vStopSlider.setValue(vStop);
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file'");                     
        }    	
	}
}
