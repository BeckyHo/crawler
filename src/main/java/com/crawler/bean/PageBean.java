package com.crawler.bean;

import java.util.List;

public class PageBean<E> {

	private List<E> list;
	private int allRow;
	private int totalPage;
	private int currentPage;
	private int pageSize;
	private boolean isFirstPage;
	private boolean isLastPage;
	private boolean hasPreviousPage;
	private boolean hasNextPage;

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public int getAllRow() {
		return allRow;
	}

	public void setAllRow(int allRow) {
		this.allRow = allRow;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isFirstPage() {
		return isFirstPage;
	}

	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public void init() {
		this.isFirstPage = (currentPage == 1);
		this.isLastPage = (currentPage == totalPage);
		this.hasPreviousPage = (currentPage != 1);
		this.hasNextPage = (currentPage != totalPage);
	}

	public static int countOffset(final int pageSize, final int currentPage) {
		return pageSize * (currentPage - 1);
	}

	public static int countTotalPage(final int pageSize, final int allRow) {
		return (allRow + pageSize - 1) / pageSize;
	}

	public static int countCurrentPage(int page) {
		return page == 0 ? 1 : page;
	}
}
