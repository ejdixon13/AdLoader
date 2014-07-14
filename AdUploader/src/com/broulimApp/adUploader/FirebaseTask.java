package com.broulimApp.adUploader;

import java.io.ByteArrayInputStream;

import javafx.application.Platform;
import javafx.scene.image.Image;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class FirebaseTask implements Runnable {

	AdLoaderApp instance = AdLoaderApp.getInstance();
	static String selectValue = "robot";
	String productSelected = "stuff";


	/**
	 * triggers a basic change in firebase to cause updates to occur
	 */
	public static void triggerChange(String path) {
		String fireBaseUrl = "https://incandescent-fire-4835.firebaseio.com/TestAd/" + path + "/S";
		Firebase f = new Firebase(fireBaseUrl);
		if (selectValue.equals("change")) {
			f.setValue("changed");
		}
		else {
			f.setValue("change");
		}
	}
	
	/**
	 * When the dropdown ad is changed the ad preview will change
	 */
	@Override
	public void run() {

		for (String store : instance.storeOptions) {
			String fireBaseUrl = "https://incandescent-fire-4835.firebaseio.com/TestAd/"
					+ store;
			System.out.println("Store in FirebaseTask: " + fireBaseUrl);
			Firebase f = new Firebase(fireBaseUrl);

			f.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					 //re-initate the list for each store
//					Platform.runLater(new Runnable() {
//						@Override
//						public void run() {
//							instance.adOptions.clear();
//							instance.adOptions.add("Create New Ad");
//						}
//					});

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							productSelected = instance.adPicker
									.getSelectionModel()
									.getSelectedItem();
						}
					});

					for (DataSnapshot obj : snapshot.getChildren()) {
						if (obj.child("Ad_Picture").getValue() != null) {
							String image = obj.child("Ad_Picture").getValue()
									.toString();
							String product = obj.child("Name").getValue()
									.toString();
							FirebaseData database = new FirebaseData();
							byte[] imageByteArray = database.decodeImage(image);
							ByteArrayInputStream inputStream = new ByteArrayInputStream(
									imageByteArray);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									instance.firebaseImage = new Image(
											inputStream);
									instance.firebaseImageView
											.setImage(instance.firebaseImage);
									instance.adOptions.add(product);
								}
							});
							System.out.println("prod retrieved: " + product);
							System.out.println("prod selected: "
									+ productSelected);
							if (product.equals(productSelected)) {
								break;
							}
						}
					}
					// create S child to keep track of changes
					if (snapshot.child("S").getValue() == null) {
						f.child("S").setValue("change");
					}
					selectValue = snapshot.child("S").getValue().toString();

				}

				@Override
				public void onCancelled(FirebaseError error) {
					System.err.println("Listener was cancelled");
				}
			});

		}
	}

}
