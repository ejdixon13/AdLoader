package com.broulimApp.adUploader;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileChooserApp extends Application {
	private Desktop desktop = Desktop.getDesktop();
	private File picture;
	TextField adPicturePath = new TextField();
	TextField productName = new TextField();
	TextField productPrice = new TextField();
	TextField productDesc = new TextField();
	TextField adExp = new TextField();

	@Override
	public void start(final Stage stage) {
		stage.setTitle("File Chooser Sample");

		final FileChooser fileChooser = new FileChooser();

		final Button openButton = new Button("Open an Ad Picture...");
		final Button uploadButton = new Button("Upload to Firebase");
		
		adPicturePath.setPrefColumnCount(10);
		
		// product Name
		Label productLabel = new Label("Enter Product Name:");
		productName.setPrefColumnCount(10);

		// product Price
		Label productPriceLabel = new Label("Enter Product Price:");
		productPrice.setPrefColumnCount(10);

		// product Description
		Label productDescLabel = new Label("Enter Product Description:");
		productDesc.setPrefColumnCount(10);
		
		// Ad Expiration
		Label adExpLabel = new Label("Enter Ad expiration:");
		adExp.setPrefColumnCount(10);		

		openButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					picture = file;
					adPicturePath.setText(file.getAbsolutePath());
				}
			}
		});


		uploadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				String adName = productName.getText();
				String adDesc = productDesc.getText();
				String adPrice = productPrice.getText();
				String adExpiration = adExp.getText();
				FirebaseData database = new FirebaseData();
				if (picture != null) {
					database.saveAd(picture, adName, adDesc, adPrice, adExpiration);
				}
			}
		});
		

//		FirebaseData database = new FirebaseData();
//		database.saveImage(file);
		final GridPane inputGridPane = new GridPane();
		

		
		GridPane.setConstraints(productLabel, 0, 1);
		GridPane.setConstraints(productName, 1, 1);
		GridPane.setConstraints(productPriceLabel, 0, 2);
		GridPane.setConstraints(productPrice, 1, 2);
		GridPane.setConstraints(productDescLabel, 0, 3);
		GridPane.setConstraints(productDesc, 1, 3);
		GridPane.setConstraints(adExpLabel, 0, 4);
		GridPane.setConstraints(adExp, 1, 4);
		GridPane.setConstraints(openButton, 0, 5);
		GridPane.setConstraints(adPicturePath, 1, 5);
		GridPane.setConstraints(uploadButton, 0, 6);
		
		inputGridPane.setHgap(6);
		inputGridPane.setVgap(6);
		inputGridPane.getChildren().addAll(openButton, adPicturePath, uploadButton,
				productLabel, productName, productPriceLabel, productPrice, productDescLabel, productDesc,
				adExpLabel, adExp );

		final Pane rootGroup = new VBox(12);
		rootGroup.getChildren().addAll(inputGridPane);
		rootGroup.setPadding(new Insets(12, 12, 12, 12));

		stage.setScene(new Scene(rootGroup));
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			Logger.getLogger(FileChooserApp.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
