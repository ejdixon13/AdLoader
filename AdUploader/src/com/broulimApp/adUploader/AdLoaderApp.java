package com.broulimApp.adUploader;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;















import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdLoaderApp extends Application {
	private Desktop desktop = Desktop.getDesktop();
	private File picture;
	TextField adPicturePath = new TextField();
	TextField productName = new TextField();
	TextField productPrice = new TextField();
	TextField productDesc = new TextField();
	TextField adStart = new TextField();
	TextField adEnd = new TextField();
	ImageView firebaseImageView;
	Image firebaseImage;
    ComboBox<String> adPicker;
    ComboBox<String> categoryPicker;
    ComboBox<String> storePicker;
	ObservableList<String> adOptions;
	ObservableList<String> categoryOptions;
	ObservableList<String> storeOptions;
	private static AdLoaderApp guiInterface = new AdLoaderApp();

    public static AdLoaderApp getInstance() {
    	return guiInterface;
    }
	
	@Override
	public void start(final Stage stage) {
		guiInterface = this;
		stage.setTitle("Ad Loader");

		// create default choices for ads
		adOptions = 
			    FXCollections.observableArrayList(
			        "Create New Ad"
			    );
		
		categoryOptions = 
			    FXCollections.observableArrayList(
			    		"Dairy", "Frozen Foods",
			            "Bread/Bakery", "Meat", 
			            "Produce", "Canned Goods", 
			            "Dry/Baking Goods",
			            "Paper_Goods", "Cleaners", 
			            "Personal_Care", "Other" 
				    );				
		storeOptions = 
			    FXCollections.observableArrayList(
			        "Afton",
			        "Alpine_Market",
			        "Driggs",
			        "Montpelier",
			        "Rexburg",
			        "Rigby",
			        "Shelly",
			        "Soda_Springs",
			        "St_Anthony"
			    );
		// create new combobox with observable adOptions array for choosing which ad to work on
		adPicker = new ComboBox<String>(adOptions);
		adPicker.setValue(adOptions.get(0));
		
		// create category drop down list
		categoryPicker = new ComboBox<String>(categoryOptions);
		categoryPicker.setValue(categoryOptions.get(0));
		
		//create store drop-down list
		storePicker = new ComboBox<String>(storeOptions);
		storePicker.setValue(storeOptions.get(0));
		
		// change displayed ad when ad changes
		storePicker.valueProperty().addListener(new ChangeListener<String>() { 
			@Override 
			public void changed(ObservableValue ov, String t, String t1) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						FirebaseTask.triggerChange(storePicker.getSelectionModel().getSelectedItem());
					}
				});
					
		        } 
		});
		
		// change displayed ad when ad changes
		adPicker.valueProperty().addListener(new ChangeListener<String>() { 
			@Override 
			public void changed(ObservableValue ov, String t, String t1) {
		            FirebaseTask.triggerChange(storePicker.getSelectionModel().getSelectedItem());
		            //System.out.println("store: " + storePicker.getSelectionModel().getSelectedItem());
		        } 
		});
		
		final FileChooser fileChooser = new FileChooser();

		// buttons
		final Button openButton = new Button("Open an Ad Picture...");
		final Button uploadButton = new Button("Upload Ad");
		final Button stageButton = new Button("Stage Ad");
		final Button removeAdButton = new Button("Remove Ad");
		
		// initiate firebaseImage view
		firebaseImage = null;
		firebaseImageView = new ImageView(firebaseImage);
		firebaseImageView.setFitWidth(100);
		firebaseImageView.setFitHeight(150);
		
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
		
		// Ad Start
		Label adStartLabel = new Label("Enter Ad Start: (Format: 040414)");
		adStart.setPrefColumnCount(10);
		
		// Ad End
		Label adEndLabel = new Label("Enter Ad End: (Format: 040414)");
		adEnd.setPrefColumnCount(10);
		
		// Product Category
		Label categoryLabel = new Label("Choose a category:");
		
		// Store Category
		Label storeLabel = new Label("Choose a store:");
		
		// Ad Picker
		Label adPickerLabel = new Label("Choose an ad:");

		
		/**
		 * Button actions
		 */
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
				WeeklyAdItem ad = createAd();
				FirebaseData database = new FirebaseData();
				if (ad.getPictureUrl() != null) {
					database.saveAd(ad);
				}
			}
		});
	
		


		// layout of all of our Views
		final GridPane inputGridPane = new GridPane();
		
		GridPane.setConstraints(storeLabel, 0, 0);
		GridPane.setConstraints(storePicker, 1, 0);
		GridPane.setConstraints(adPickerLabel, 0, 1);
		GridPane.setConstraints(adPicker, 1, 1);
		GridPane.setConstraints(productLabel, 0, 2);
		GridPane.setConstraints(productName, 1, 2);
		GridPane.setConstraints(productPriceLabel, 0, 3);
		GridPane.setConstraints(productPrice, 1, 3);
		GridPane.setConstraints(productDescLabel, 0, 4);
		GridPane.setConstraints(productDesc, 1, 4);
		GridPane.setConstraints(categoryLabel, 0, 5);
		GridPane.setConstraints(categoryPicker, 1, 5);
		GridPane.setConstraints(adStartLabel, 0, 6);
		GridPane.setConstraints(adStart, 1, 6);
		GridPane.setConstraints(adEndLabel, 0, 7);
		GridPane.setConstraints(adEnd, 1, 7);
		GridPane.setConstraints(openButton, 0, 8);
		GridPane.setConstraints(adPicturePath, 1, 8);
		GridPane.setConstraints(stageButton, 0, 11);
		GridPane.setConstraints(uploadButton, 1, 11);
		GridPane.setConstraints(removeAdButton, 0, 14);
		
		GridPane.setConstraints(firebaseImageView, 2, 0);
		

		
		inputGridPane.setHgap(6);
		inputGridPane.setVgap(6);
		inputGridPane.getChildren().addAll(openButton, adPicturePath, uploadButton, firebaseImageView,
				productLabel, productName, productPriceLabel, productPrice, productDescLabel, productDesc,
				adStartLabel, adStart, adEndLabel, adEnd, adPicker, categoryLabel, categoryPicker, storeLabel, 
				storePicker,adPickerLabel, stageButton, removeAdButton);

		final Pane rootGroup = new VBox(12);
		rootGroup.getChildren().addAll(inputGridPane);
		rootGroup.setPadding(new Insets(12, 12, 12, 12));
		
		stage.setHeight(700);
		stage.setWidth(700);
	

		stage.setScene(new Scene(rootGroup));
		stage.show();
		
		Runnable runningObject = (Runnable) new FirebaseTask();
		Thread newThread = new Thread(runningObject);
		newThread.start();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			Logger.getLogger(AdLoaderApp.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	
	// creates ad from component fields
	private WeeklyAdItem createAd() {
		WeeklyAdItem newAd = new WeeklyAdItem();
		newAd.setAdName(productName.getText());
		newAd.setAdDescript(productDesc.getText());
		newAd.setPrice(productPrice.getText());
		newAd.setAdStartDate(adStart.getText());
		newAd.setAdEndDate(adEnd.getText());
		newAd.setCategory(categoryPicker.getSelectionModel().getSelectedItem());
		newAd.setStoreName(storePicker.getSelectionModel().getSelectedItem());
		newAd.setPictureUrl(picture);
		return newAd;
	}
}
