package com.broulimApp.adUploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
//import java.util.Base64.*;


import org.shaded.apache.commons.codec.binary.Base64;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class FirebaseData {

	
	public boolean saveAd(WeeklyAdItem ad) {
		try {

			// Reading a Image file from file system
			FileInputStream imageInFile = new FileInputStream(ad.getPictureUrl());
			byte imageData[] = new byte[(int) ad.getPictureUrl().length()];
			imageInFile.read(imageData);

			// Converting Image byte array into Base64 String
			String imageDataString = encodeImage(imageData);
			if (imageDataString == null) {
				imageDataString = "Did not work!";
			}
			
			String fireBaseUrl = "https://incandescent-fire-4835.firebaseio.com/TestAd/" + ad.getStoreName();
			Firebase f = new Firebase(fireBaseUrl);

			// add all items to object and push to Firebase
			Map<String, Object> toSet = new HashMap<String, Object>();
			
			toSet.put("Name", ad.getAdName());
			toSet.put("Category", ad.getCategory());
			toSet.put("Description", ad.getAdDescript());
			toSet.put("Price", ad.getPrice());
			toSet.put("Ad_Start", ad.getAdStartDate());
			toSet.put("Ad_End", ad.getAdEndDate());
			toSet.put("Ad_Picture", imageDataString);
			f.push().setValue(toSet);

			imageInFile.close();
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return false;		
	}

	public boolean saveImage(File file) {
		try {

			// Reading a Image file from file system
			FileInputStream imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);

			// Converting Image byte array into Base64 String
			String imageDataString = encodeImage(imageData);
			if (imageDataString == null) {
				imageDataString = "Did not work!";
			}

			Firebase f = new Firebase(
					"https://incandescent-fire-4835.firebaseio.com/TestImage");

			// add first and last name inputted
			Map<String, Object> toSet = new HashMap<String, Object>();
			toSet.put("image01", imageDataString);
			f.push().setValue(toSet);

			imageInFile.close();
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return false;
	}

	/**
	 * Encodes the byte array into base64 string
	 *
	 * @param imageByteArray
	 *            - byte array
	 * @return String a {@link java.lang.String}
	 */
	public String encodeImage(byte[] imageByteArray) {

		return Base64.encodeBase64String(imageByteArray);
	}

	/**
	 * Decodes the base64 string into byte array
	 *
	 * @param imageDataString
	 *            - a {@link java.lang.String}
	 * @return byte array
	 */
	public byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString);
	}

}
