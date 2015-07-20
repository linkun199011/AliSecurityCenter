package com.alibaba.alisecuritycenter.memoryspeedup.whitelist;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.ListView;

public class WhiteListActivity extends Activity {
	private final static String TAG = "WhiteListActivity";

	private static Context mContext;
	private static ActivityManager mActivityManager = null;
	private static PackageManager mPackageManager = null;
	private static ApplicationInfo mApplicatinoInfo = null;
	private ArrayList<ApplicationInfo> mApplicationInfoList = new ArrayList<>();
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new CustomTitleBar().getTitleBarWhole(this, getResources().getString(R.string.whiteList), true, false);
		setContentView(R.layout.memoryspeedup_white_list_listview);
		init();
	}

	private void init() {
		mContext = this;
		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		mPackageManager = getApplicationContext().getPackageManager();
		mListView = (ListView) findViewById(R.id.white_list_lv);
		
		getInstalledApplications();
		WhiteListAdapter whiteListAdapter = new WhiteListAdapter(mContext, mApplicationInfoList);
		mListView.setAdapter(whiteListAdapter);
	}

	private void getInstalledApplications() {
		ArrayList<ApplicationInfo> userAppList = new ArrayList<>();
		ArrayList<ApplicationInfo> sysAppList = new ArrayList<>();
		List<PackageInfo> installedAppList = mPackageManager.getInstalledPackages(0);
		SharedPreferences sp = this.getSharedPreferences("SP", Context.MODE_PRIVATE);
		for(PackageInfo packageInfo : installedAppList ){
			System.out.println("packageName:"+packageInfo.packageName);
			
			try {
				mApplicatinoInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA);
				// 如果在sp文件中找到该应用，则插入表头（保证在选中的应用都是在前面）
				if(sp.getString(mApplicatinoInfo.packageName, "false").equals("true")){
					mApplicationInfoList.add(0, mApplicatinoInfo);
					continue;
				}
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
	                // 系统级应用
					sysAppList.add(mApplicatinoInfo);
					continue;
	            }
				else{
					// 用户级应用
					userAppList.add(mApplicatinoInfo);
					continue;
				}
				
				
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		//添加用户级应用(排在前面)
		for(ApplicationInfo userApp : userAppList){
			mApplicationInfoList.add(userApp);
		}
		//添加系统级应用(排在后面)
		for(ApplicationInfo sysApp : sysAppList){
			mApplicationInfoList.add(sysApp);
		}
	}
}
