package com.tylerspaeth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//TODO javadocs
public class FXUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() {
		System.err.println("shutting down");
		// TODO setup this so that it closes the necessary connections
	}

	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
			Scene scene = new Scene(root);

			stage.setTitle("FishingApp");

			stage.setScene(scene);
			stage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}



	
