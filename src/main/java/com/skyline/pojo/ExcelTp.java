package com.skyline.pojo;

// Generated 2015-8-31 9:24:24 by Hibernate Tools 4.3.1

/**
 * ExcelTp generated by hbm2java
 */
public class ExcelTp implements java.io.Serializable {

	private Integer id;
	private String name;
	private String filePathName;
	private String downloadPath;

	public ExcelTp() {
	}

	public ExcelTp(String name, String filePathName, String downloadPath) {
		this.name = name;
		this.filePathName = filePathName;
		this.downloadPath = downloadPath;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePathName() {
		return this.filePathName;
	}

	public void setFilePathName(String filePathName) {
		this.filePathName = filePathName;
	}

	public String getDownloadPath() {
		return this.downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

}
