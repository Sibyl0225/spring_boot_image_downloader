package com.neo.entity;

import java.io.Serializable;

	

public class YandeEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	public String imageId;
	

	public String imageName;
	

	public String largeSizeImage;
	

	public String previewImage;
	

	public boolean hadDownload;
	

	public String createDate;
	

	public String imageResolution;//分辨率
	

	public long imageFileSize;
	

	public boolean overFlag;
	
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getLargeSizeImage() {
		return largeSizeImage;
	}
	public void setLargeSizeImage(String largeSizeImage) {
		this.largeSizeImage = largeSizeImage;
	}
	public String getPreviewImage() {
		return previewImage;
	}
	public void setPreviewImage(String previewImage) {
		this.previewImage = previewImage;
	}
	public boolean isHadDownload() {
		return hadDownload;
	}
	public void setHadDownload(boolean hadDownload) {
		this.hadDownload = hadDownload;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getImageResolution() {
		return imageResolution;
	}
	public void setImageResolution(String imageResolution) {
		this.imageResolution = imageResolution;
	}
	public long getImageFileSize() {
		return imageFileSize;
	}
	public void setImageFileSize(long imageFileSize) {
		this.imageFileSize = imageFileSize;
	}

	public boolean isOverFlag() {
		return overFlag;
	}
	public void setOverFlag(boolean overFlag) {
		this.overFlag = overFlag;
	}


}
