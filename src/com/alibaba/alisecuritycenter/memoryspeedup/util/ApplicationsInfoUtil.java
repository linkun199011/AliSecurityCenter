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
	 * ���ϵͳ�����ڴ���Ϣ
	 * @param type Ϊ<b>int</b>���ͣ�<b>AVAILABLE_MEMORY</b>�����ѯ�����ڴ棬<b>TOTAL_MEMORY</b>�����ѯ���ڴ�
	 * @param activityManager
	 * @param mContext
	 * @return ���ظ�ʽ��֮���ϵͳ�ڴ��С��<b>string</b>����
	 */
	@SuppressLint("NewApi")
	public String getSystemMemorySize(int type , ActivityManager activityManager , Context mContext) {
		// ���MemoryInfo����
		MemoryInfo memoryInfo = new MemoryInfo();
		// ���ϵͳ�����ڴ棬������MemoryInfo������
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
	 * ��ȡ�������е�Ӧ�ó�������(Ŀǰֻ��ͳ���û�����)
	 * 
	 * @param mActivityManager
	 * @param packageManager
	 * @return ����int��Ӧ�ó�������
	 */
	public int getRunningProcessNumber(ActivityManager mActivityManager ,PackageManager packageManager){
		int runningProcessNumber = 0;
		List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appSingleProcessInfo : appProcessInfoList){
			String processName = appSingleProcessInfo.processName;
			// ���ÿ�����������е�Ӧ�ó���(��),��ÿ��Ӧ�ó���İ���
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
					// ϵͳ���� �Լ� �Լ�����
				}
				else{
					runningProcessNumber++;
				}
			}
		}
		return runningProcessNumber;
	}
	/**
	 * �����������е�Ӧ�ó����б�(ֻ���û�����Ӧ�ó���)
	 * @param mActivityManager
	 * @param packageManager
	 * @return ����ArrayList �� Ӧ�ó����б�
	 */
	public ArrayList<RunningProgressListItem> getRunningProgressList(ActivityManager mActivityManager ,PackageManager packageManager) {
		//mRunningProgressList.removeAll(mRunningProgressList);
		ArrayList<RunningProgressListItem> tempRunningProgressList = new ArrayList<>();
		List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appSingleProcessInfo : appProcessInfoList) {
			int pid = appSingleProcessInfo.pid;
			int uid = appSingleProcessInfo.uid;
			// ��������Ĭ���ǰ�������������android��process=""ָ��
			String processName = appSingleProcessInfo.processName;
			// ���ÿ�����������е�Ӧ�ó���(��),��ÿ��Ӧ�ó���İ���
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
					// ��ϵͳ���� 
					// ��øý���ռ�õ��ڴ�
					int[] pids = new int[] { pid };
					// ��MemoryInfoλ��android.os.Debug.MemoryInfo���У�����ͳ�ƽ��̵��ڴ���Ϣ
					Debug.MemoryInfo[] memoryInfo = mActivityManager
							.getProcessMemoryInfo(pids);
					// ��ȡ����ռ�ڴ�����Ϣ kb��λ
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
	 * ��ѯ�����������е�Ӧ�ó��򣨰���ϵͳ��Ӧ�ã�
	 * @param mActivityManager
	 * @param packageManager
	 * @return ����ArrayList , �����������е�Ӧ�ó���
	 */
	public ArrayList<RunningProgressListItem> getRunningProgressAllList(ActivityManager mActivityManager ,PackageManager packageManager) {
		//mRunningProgressList.removeAll(mRunningProgressList);
		ArrayList<RunningProgressListItem> tempRunningProgressList = new ArrayList<>();
		List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appSingleProcessInfo : appProcessInfoList) {
			int pid = appSingleProcessInfo.pid;
			int uid = appSingleProcessInfo.uid;
			// ��������Ĭ���ǰ�������������android��process=""ָ��
			String processName = appSingleProcessInfo.processName;
			// ���ÿ�����������е�Ӧ�ó���(��),��ÿ��Ӧ�ó���İ���
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
					// ���˱�������������������
					// ��øý���ռ�õ��ڴ�
					int[] pids = new int[] { pid };
					// ��MemoryInfoλ��android.os.Debug.MemoryInfo���У�����ͳ�ƽ��̵��ڴ���Ϣ
					Debug.MemoryInfo[] memoryInfo = mActivityManager
							.getProcessMemoryInfo(pids);
					// ��ȡ����ռ�ڴ�����Ϣ kb��λ
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
	 * ��ȡӦ�ó���������ĸ���
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
	 * ��ȡ������ArrayList
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
