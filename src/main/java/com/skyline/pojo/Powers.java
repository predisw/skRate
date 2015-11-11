package com.skyline.pojo;

// Generated 2015-11-10 17:13:53 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Powers generated by hbm2java
 */
public class Powers implements java.io.Serializable ,Comparable<Powers>{

	private Integer id;
	private String name;
	private String keyName;
	private String url;
	private Integer parentId;
	private Boolean isMenu;
	private Set roles = new HashSet(0);

	public Powers() {
	}

	public Powers(String name, String keyName, String url, Integer parentId,
			Boolean isMenu, Set roles) {
		this.name = name;
		this.keyName = keyName;
		this.url = url;
		this.parentId = parentId;
		this.isMenu = isMenu;
		this.roles = roles;
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

	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Boolean getIsMenu() {
		return this.isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	public Set getRoles() {
		return this.roles;
	}

	public void setRoles(Set roles) {
		this.roles = roles;
	}

	@Override
	public int compareTo(Powers p) {
		// TODO Auto-generated method stub
		return id.compareTo(p.getId());
	}

}
