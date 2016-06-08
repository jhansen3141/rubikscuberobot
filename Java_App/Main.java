//Josh Hansen
//Rubik's Cube Robot
//April 2016

package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;



public class Main extends Application {
	
	Stage theStage;
	@Override
	public void start(Stage primaryStage) {
		theStage = primaryStage;
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("RubikFX.fxml"));
			Scene scene = new Scene(root,1024,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			theStage.setTitle("Rubik's Solve");
			theStage.setScene(scene);
			theStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
}
