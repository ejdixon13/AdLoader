package com.broulimApp.adUploader;

import java.io.File;

/**
 * 
 * @author ericjdixon
 * The WeeklyAdItem will hold our Item that has a picture associated with it as well as a storeID
 *
 */

public class WeeklyAdItem {
	
	// either price per unit or price per lb with produce
	protected String price;
	protected String category;
	protected File pictureUrl;
	protected String adDescript;
	protected String storeName;
	protected String itemId;
	protected String adStartDate;
	protected String adEndDate;
	protected String adName;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getAdDescript() {
		return adDescript;
	}
	public void setAdDescript(String adDescript) {
		this.adDescript = adDescript;
	}
	public String getAdStartDate() {
		return adStartDate;
	}
	public void setAdStartDate(String adStartDate) {
		this.adStartDate = adStartDate;
	}
	public String getAdEndDate() {
		return adEndDate;
	}
	public void setAdEndDate(String adEndDate) {
		this.adEndDate = adEndDate;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}

	public File getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(File pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	
}
