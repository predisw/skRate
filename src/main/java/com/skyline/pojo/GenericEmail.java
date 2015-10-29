package com.skyline.pojo;

public class GenericEmail {

	private Integer id;
	private String name;
	private String subject;
	private String content;
	private String addrList;
	private String ccAdddrList;
	private String bccAddrList;
	private String attacheName;


	public GenericEmail() {
	}

	public GenericEmail(String name, String subject, String content, String addrList,
			String ccAdddrList, String bccAddrList, String attacheName	) {
		this.name = name;
		this.subject = subject;
		this.content = content;
		this.addrList = addrList;
		this.ccAdddrList = ccAdddrList;
		this.bccAddrList = bccAddrList;
		this.attacheName = attacheName;
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

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddrList() {
		return this.addrList;
	}

	public void setAddrList(String addrList) {
		this.addrList = addrList;
	}

	public String getCcAdddrList() {
		return this.ccAdddrList;
	}

	public void setCcAdddrList(String ccAdddrList) {
		this.ccAdddrList = ccAdddrList;
	}

	public String getBccAddrList() {
		return this.bccAddrList;
	}

	public void setBccAddrList(String bccAddrList) {
		this.bccAddrList = bccAddrList;
	}

	public String getAttacheName() {
		return this.attacheName;
	}

	public void setAttacheName(String attacheName) {
		this.attacheName = attacheName;
	}



}
