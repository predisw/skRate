package com.skyline.pojo;

// Generated 2015-8-31 9:24:24 by Hibernate Tools 4.3.1

import java.util.Date;

/**
 * Log generated by hbm2java
 */
public class Log implements java.io.Serializable {

	private Integer id;
	private String who;
	private String how;  //操作
	private String what; //对象
	private Date time;
	private String content; //修改的内容


	public Log() {
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * @return the who
	 */
	public String getWho() {
		return who;
	}


	/**
	 * @param who the who to set
	 */
	public void setWho(String who) {
		this.who = who;
	}





	/**
	 * @return the what
	 */
	public String getWhat() {
		return what;
	}


	/**
	 * @param what the what to set
	 */
	public void setWhat(String what) {
		this.what = what;
	}




	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}


	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}


	/**
	 * @return the how
	 */
	public String getHow() {
		return how;
	}


	/**
	 * @param how the how to set
	 */
	public void setHow(String how) {
		this.how = how;
	}


	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}


	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	
}