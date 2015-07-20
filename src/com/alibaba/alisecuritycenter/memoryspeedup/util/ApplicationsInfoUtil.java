package com.alibaba.alisecuritycenter.memoryspeedup.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.alisecuritycenter.memoryspeedup.runnningprocess.RunningProgressListItem;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.text.format.Formatter;
/**
 * This Class is an Util class.
 * @author linkun.lk
 *
 */
public class ApplicationsInfoUtil {
	
	public static final int AVAILABLE_MEMORY = 0;
	public static final int TOTAL_MEMORY = 1;
	
	private ApplicationInfo applicationInfo = null;
	private SharedPreferences sp ;
	/**
	 * 获得系统可用内存信息
	 * @param type 为<b>int</b>类型，<b>AVAILABLE_MEMORY</b>代表查询可用内存，<b>TOTAL_MEMORY</b>代表查询总内存
	 * @param activityManager
	 * @param mContext
	 * @return 返回格式化之后的系统内存大小，<b>string</b>类型
	 */
	@SuppressLint("NewApi")
	public String getSystemMemorySize(int type , ActivityManager activityManager , Context mContext) {
		// 获得MemoryInfo对象
		MemoryInfo memoryInfo = new MemoryInfo();
		// 获得系统可用内存，保存在MemoryInfo对象上
		activityManager.getMemoryInfo(memoryInfo);
		long memSize = 0;
		if(type == AVAILABLE_MEMORY){
			memSize = memoryInfo.availMem;
		}
		else{
			memSize = memoryInfo.totalMem;
		}
		String strMemSize = Formatter.formatFileSize(mContext,memSize);
		return strMemSize;
	}
	/**
	 * 获取正在运行的应用程序数量(目前只是统计用户级的)
	 * 
	 * @param mActivityManager
	 * @param packageManager
	 * @return 返回int，应用程序数量
	 */
	public int getRunningProcessNumber(ActivityManager mActivityManager ,PackageManager packageManager){
		int runningProcessNumber = 0;
		List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appSingleProcessInfo : appProcessInfoList){
			String processName = appSingleProcessInfo.processName;
			// 获得每个进程里运行的应用程序(包),即每个应用程序的包名
			String[] packageList = appSingleProcessInfo.pkgList;
			for (int count = 0 ; count < packageList.length; count ++) {
				try {
					applicationInfo = packageManager.getApplicationInfo(
							packageList[count], PackageManager.GET_META_DATA);
				} catch (PackageManager.NameNotFoundException e) {
					applicationInfo = null;
					e.printStackTrace();
				}
				if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0 || processName.equals("com.alibaba.memoryspeedup") ){
					// 系统程序 以及 自己进程
				}
				else{
					runningProcessNumber++;
				}
			}
		}
		return runningProcessNumber;
	}
	/**
	 * 返回正在运行的应用程序列表(只是用户级的应用程序)
	 * @param mActivityManager
	 * @param packageManager
	 * @return 返回ArrayList ， 应用程序列表
	 */
	public ArrayList<RunningProgressListItem> getRunningProgressList(ActivityManager mActivityManager ,PackageManager packageManager) {
		//mRunningProgressList.removeAll(mRunningProgressList);
		ArrayList<RunningProgressListItem> tempRunningProgressList = new ArrayList<>();
		List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appSingleProcessInfo : appProcessInfoList) {
			int pid = appSingleProcessInfo.pid;
			int uid = appSingleProcessInfo.uid;
			// 进程名，默认是包名或者由属性android：process=""指定
			String processName = appSingleProcessInfo.processName;
			// 获得每个进程里运行的应用程序(包),即每个应用程序的包名
			String[] packageList = appSingleProcessInfo.pkgList;
			for (int count = 0 ; count < packageList.length; count ++) {
				try {
					applicationInfo = packageManager.getApplicationInfo(
							packageList[count], PackageManager.GET_META_DATA);
				} catch (PackageManager.NameNotFoundException e) {
					applicationInfo = null;
					e.printStackTrace();
				}
				String applicationName = (String) packageManager
						.getApplicationLabel(applicationInfo);
				if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !(processName.equals("com.alibaba.memoryspeedup")) ){
					// 非系统程序 
					// 获得该进程占用的内存
					int[] pids = new int[] { pid };
					// 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
					Debug.MemoryInfo[] memoryInfo = mActivityManager
							.getProcessMemoryInfo(pids);
					// 获取进程占内存用信息 kb单位
					int memSize = memoryInfo[0].dalvikPrivateDirty;

					RunningProgressListItem rpli = new RunningProgressListItem();
					rpli.setPid(pid);
					rpli.setUid(uid);
					rpli.setPackageName(packageList[0]);
					rpli.setProcessName(processName);
					rpli.setAppName(applicationName);
					rpli.setMemSize(memSize + "");
					rpli.setProtected(false);
					tempRunningProgressList.add(rpli);
				}
			}
		}
		return tempRunningProgressList;
	}
	/**
	 * 查询所有正在运行的应用程序（包括系统级应用）
	 * @param mActivityManager
	 * @param packageManager
	 * @return 返回ArrayList , 所有正在运行的应用程序
	 */
	public ArrayList<RunningProgressListItem> getRunningProgressAllList(ActivityManager mActivityManager ,PackageManager packageManager) {
		//mRunningProgressList.removeAll(mRunningProgressList);
		ArrayList<RunningProgressListItem> tempRunningProgressList = new ArrayList<>();
		List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appSingleProcessInfo : appProcessInfoList) {
			int pid = appSingleProcessInfo.pid;
			int uid = appSingleProcessInfo.uid;
			// 进程名，默认是包名或者由属性android：process=""指定
			String processName = appSingleProcessInfo.processName;
			// 获得每个进程里运行的应用程序(包),即每个应用程序的包名
			String[] packageList = appSingleProcessInfo.pkgList;
			for (int count = 0 ; count < packageList.length; count ++) {
				try {
					applicationInfo = packageManager.getApplicationInfo(
							packageList[count], PackageManager.GET_META_DATA);
				} catch (PackageManager.NameNotFoundException e) {
					applicationInfo = null;
					e.printStackTrace();
				}
				String applicationName = (String) packageManager
						.getApplicationLabel(applicationInfo);
				if(!(processName.equals("com.alibaba.memoryspeedup")) ){
					// 除了本程序外所有其他程序
					// 获得该进程占用的内存
					int[] pids = new int[] { pid };
					// 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
					Debug.MemoryInfo[] memoryInfo = mActivityManager
							.getProcessMemoryInfo(pids);
					// 获取进程占内存用信息 kb单位
					int memSize = memoryInfo[0].dalvikPrivateDirty;
					/*Log.i(TAG, "[[[[[[[processName: " + processName + "  pid: " + pid
							+ " uid:" + uid + " memorySize is -->" + memSize + "kb");*/
					//
					RunningProgressListItem rpli = new RunningProgressListItem();
					rpli.setPid(pid);
					rpli.setUid(uid);
					rpli.setPackageName(packageList[0]);
					rpli.setProcessName(processName);
					rpli.setAppName(applicationName);
					rpli.setMemSize(memSize + "");
					rpli.setProtected(false);
					tempRunningProgressList.add(rpli);
				}
				//Log.i(TAG, "packageName " + packageList[count] + " in process id is -->" + pid);
			}
		}
		return tempRunningProgressList;
	}
	/**
	 * 获取应用程序白名单的个数
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getWhiteListAppNumber(Context context){
		int whiteListAppNumber = 0;
		sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE);
		Map<String ,String> maps = (Map<String, String>) sp.getAll(); 
		whiteListAppNumber = maps.size();
		//System.out.println("whiteListAppNumber = "+whiteListAppNumber );
		return whiteListAppNumber;
	}
	/**
	 * 获取白名单ArrayList
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getWhiteList(Context context){
		ArrayList<String> whiteList = new ArrayList<>();
		sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE);
		Map<String ,String> maps = (Map<String, String>) sp.getAll();
		Set<String> set = maps.keySet();
		Iterator<String> i = set.iterator();
		while(i.hasNext()){
			String packageName = i.next();
			whiteList.add(packageName);
		}
		return whiteList;
	}
}
