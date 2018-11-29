package com.neo.yande.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.beanutils.BeanMap;

	
@Entity
public class Yande implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true)	
	public String imageId;
	
	@Column(nullable = false)
	public String imageName;
	
	@Column(nullable = false)
	public String largeSizeImage;
	
	@Column(nullable = false)
	public String previewImage;
	
	@Column(nullable = false)
	public boolean hadDownload;
	
	@Column(nullable = false)
	public String createDate;
	
	@Column(nullable = true)
	public String imageResolution;//分辨率
	
	@Column(name = "filesize",nullable = true)
	public long imageFileSize;
	
	@Column(nullable = true,columnDefinition="int default 0")
	public int overFlag ;
	
	@Override
	public String toString() {
		return "yande [imageId=" + imageId + ", largeSizeImage=" + largeSizeImage + ", previewImage=" + previewImage
				+ ", hadDownload=" + hadDownload + ", createDate=" + createDate + ", imageResolution=" + imageResolution
				+ ", imageFileSize=" + imageFileSize + "]";
	}
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
	
	public HashMap<String , Object> yandeToMap(){
		org.apache.commons.beanutils.BeanMap beanMap = new  org.apache.commons.beanutils.BeanMap(this);
		return beanToHashMap(beanMap);
	}
	public int isOverFlag() {
		return overFlag;
	}
	public void setOverFlag(int overFlag) {
		this.overFlag = overFlag;
	}
	/**
	 * beanMap to HashMap conversion
	 * @param beanMap
	 * @return
	 */
	private HashMap<String, Object> beanToHashMap(BeanMap beanMap) {
		HashMap<String, Object> yandeMap = new HashMap<String, Object>();
		Iterator<Entry<Object, Object>> beanMapIterator = beanMap.entryIterator();
		if(beanMapIterator.hasNext()) {
			Map.Entry<Object, Object> entry = beanMapIterator.next();
			String key = entry.getKey().toString();
			Object val = entry.getValue();
			//
			if(val != null )  yandeMap.put(key,val);
		}
		return null;
	}

}
