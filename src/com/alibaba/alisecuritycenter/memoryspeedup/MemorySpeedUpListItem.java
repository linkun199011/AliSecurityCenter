package com.alibaba.alisecuritycenter.memoryspeedup;

public class MemorySpeedUpListItem {
	private String mTitle;
	private String mDescription;
	private String mNumber;
	
	public MemorySpeedUpListItem() {
		super();
	}
	public MemorySpeedUpListItem(String mTitle, String mDescription , String mNumber) {
		super();
		this.mTitle = mTitle;
		this.mDescription = mDescription;
		this.setmNumber(mNumber);
	}
	
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getmDescription() {
		return mDescription;
	}
	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}
	public String getmNumber() {
		return mNumber;
	}
	public void setmNumber(String mNumber) {
		this.mNumber = mNumber;
	}
	
}
