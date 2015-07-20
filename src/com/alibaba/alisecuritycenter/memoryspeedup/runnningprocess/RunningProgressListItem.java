package com.alibaba.alisecuritycenter.memoryspeedup.runnningprocess;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class RunningProgressListItem implements Parcelable{
	private String appName;    //应用程序名
	private String packageName; //包名
	private String processName; //进程名
	private int pid;	// pid
	private int uid;	// uid
	private String memSize;    //应用程序占用内存
	private boolean isProtected = false;    //是否在受保护（在白名单中）
	
	public RunningProgressListItem(String appName, String packageName, String processName,Drawable appIcon, int pid, int uid, String memSize,
			boolean isProtected) {
		super();
		this.appName = appName;
		this.packageName = packageName;
		this.setProcessName(processName);
		this.pid = pid;
		this.uid = uid;
		this.memSize = memSize;
		this.isProtected = isProtected;
	}
	public RunningProgressListItem() {
		super();
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getMemSize() {
		return memSize;
	}
	public void setMemSize(String memSize) {
		this.memSize = memSize;
	}
	public boolean isProtected() {
		return isProtected;
	}
	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(appName);
		dest.writeString(packageName);
		dest.writeString(processName);
		dest.writeInt(pid);
		dest.writeInt(uid);
		dest.writeString(memSize);
		dest.writeByte((byte)(isProtected ? 1 : 0));
	}
	
	// 用来创建自定义的Parcelable的对象
    public static final Parcelable.Creator<RunningProgressListItem> CREATOR
            = new Parcelable.Creator<RunningProgressListItem>() {
        public RunningProgressListItem createFromParcel(Parcel in) {
        	RunningProgressListItem rpli = new RunningProgressListItem();
        	rpli.appName = in.readString();
        	rpli.packageName = in.readString();
        	rpli.processName = in.readString();
        	rpli.pid = in.readInt();
        	rpli.uid = in.readInt();
        	rpli.memSize = in.readString();
        	rpli.isProtected = in.readByte() != 0;//?
            return rpli;
        }

        public RunningProgressListItem[] newArray(int size) {
            return new RunningProgressListItem[size];
        }
    };
	
}
