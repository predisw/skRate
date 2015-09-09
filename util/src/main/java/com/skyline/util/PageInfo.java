package com.skyline.util;

import java.util.List;

 //分页类
public class PageInfo {
	

	private int curPage=1;
	private int totalPage;
	private int pageSize=8;
	private int totalCount;
	@SuppressWarnings("unchecked")
	private List data;
	public PageInfo() {
	}
    
	public PageInfo(int curPage, int totalCount, int pageSize, List data) {
		this.curPage = curPage;
		this.totalPage = computePageCount(totalCount, pageSize);
		this.pageSize = pageSize;
		this.data = data;
	}

	public static int computePageCount(int totalCount ,int pageSize){
		if(pageSize==0||totalCount==0){
			return 0;
		}
		return (totalCount+pageSize-1)/pageSize;
	}
	
	
	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getTotalPage() {
		if(totalCount==0)
			return 1;
		if(pageSize==0)
			return 0;
		else
			return (totalCount+pageSize-1)/pageSize;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}
	


	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
