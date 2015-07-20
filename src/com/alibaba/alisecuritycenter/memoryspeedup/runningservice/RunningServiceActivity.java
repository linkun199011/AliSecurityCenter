package com.alibaba.alisecuritycenter.memoryspeedup.runningservice;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RunningServiceActivity extends Activity implements
		OnItemClickListener {
	private static String TAG = "RunServiceInfo";
	private ActivityManager mActivityManager = null;
	// ProcessInfo Model类 用来保存所有进程信息
	private List<RunningServiceItem> serviceInfoList = null;
	private ListView listviewService;
	private TextView tvTotalServiceNo;

	public void onCreate(Bundle savedInstanceState) {
		new CustomTitleBar().getTitleBarWhole(this, getResources().getString(R.string.runningService),true,false);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memoryspeedup_service_listview);
		listviewService = (ListView) findViewById(R.id.listviewService);
		listviewService.setOnItemClickListener(this);
		tvTotalServiceNo = (TextView) findViewById(R.id.tvTotalServiceNo);
		// 获得ActivityManager服务的对象
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		// 当前应用的代码执行目录
		upgradeRootPermission(getPackageCodePath());
		// 获得正在运行的Service信息
		getRunningServiceInfo();
		// 对集合排序
		Collections.sort(serviceInfoList, new comparatorServiceLable());
		System.out.println(serviceInfoList.size() + "-------------");
		// 为ListView构建适配器对象
		RunningServiceAdapter mServiceInfoAdapter = new RunningServiceAdapter(
				RunningServiceActivity.this, serviceInfoList);
		listviewService.setAdapter(mServiceInfoAdapter);
		tvTotalServiceNo.setText("当前正在运行的服务共有：" + serviceInfoList.size());
		
	}

	/**
	 * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
	 * 
	 * @return 应用程序是/否获取Root权限
	 */
	public static boolean upgradeRootPermission(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // 切换到root帐号
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	// 获得系统正在运行的进程信息
	private void getRunningServiceInfo() {
		// 设置一个默认Service的数量大小
		int defaultNum = 100;
		// 通过调用ActivityManager的getRunningAppServicees()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager
				.getRunningServices(defaultNum);
		System.out.println(runServiceList.size());
		// ServiceInfo Model类 用来保存所有进程信息
		serviceInfoList = new ArrayList<RunningServiceItem>();
		for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
			// 获得Service所在的进程的信息
			int pid = runServiceInfo.pid; // service所在的进程ID号
			int uid = runServiceInfo.uid; // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
			// 进程名，默认是包名或者由属性android：process指定
			String processName = runServiceInfo.process;
			// 该Service启动时的时间值
			long activeSince = runServiceInfo.activeSince;

			// 如果该Service是通过Bind方法方式连接，则clientCount代表了service连接客户端的数目
			int clientCount = runServiceInfo.clientCount;

			// 获得该Service的组件信息 可能是pkgname/servicename
			ComponentName serviceCMP = runServiceInfo.service;
			String serviceName = serviceCMP.getShortClassName(); // service 的类名
			String pkgName = serviceCMP.getPackageName(); // 包名

			// 打印Log
			Log.i(TAG, "所在进程id :" + pid + " 所在进程名：" + processName + " 所在进程uid:"
					+ uid + "\n" + " service启动的时间值：" + activeSince
					+ " 客户端绑定数目:" + clientCount + "\n" + "该service的组件信息:"
					+ serviceName + " and " + pkgName);

			// 这儿我们通过service的组件信息，利用PackageManager获取该service所在应用程序的包名 ，图标等
			PackageManager mPackageManager = this.getPackageManager(); // 获取PackagerManager对象;

			try {
				// 获取该pkgName的信息
				ApplicationInfo appInfo = mPackageManager.getApplicationInfo(
						pkgName, 0);

				RunningServiceItem runService = new RunningServiceItem();
				runService.setAppIcon(appInfo.loadIcon(mPackageManager));
				runService.setAppLabel(appInfo.loadLabel(mPackageManager) + "");
				runService.setServiceName(serviceName);
				runService.setPkgName(pkgName);
				// 设置该service的组件信息
				Intent intent = new Intent();
				intent.setComponent(serviceCMP);
				runService.setIntent(intent);

				runService.setPid(pid);
				runService.setProcessName(processName);

				// 添加至集合中
				serviceInfoList.add(runService);

			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("--------------------- error -------------");
				e.printStackTrace();
			}

		}
	}

	// 触摸可停止
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		final Intent stopserviceIntent = serviceInfoList.get(position)
				.getIntent();

		new AlertDialog.Builder(RunningServiceActivity.this)
				.setTitle("是否停止服务")
				.setMessage("服务只有在重新启动后，才可以继续运行。但这可能会给电子市场应用程序带来意想不到的结果。")
				.setPositiveButton("停止", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// 停止该Service
						// 由于权限不够的问题，为了避免应用程序出现异常，捕获该SecurityException ，并弹出对话框
						try {
							stopService(stopserviceIntent);
							//unbindService((ServiceConnection) stopserviceIntent);
						} catch (SecurityException sEx) {
							// 发生异常 说明权限不够
							System.out.println(" deny the permission");
							new AlertDialog.Builder(
									RunningServiceActivity.this)
									.setTitle("权限不够")
									.setMessage("对不起，您的权限不够，无法停止该Service")
									.create().show();
						}
						// 刷新界面
						// 获得正在运行的Service信息
						getRunningServiceInfo();
						// 对集合排序
						Collections.sort(serviceInfoList,new comparatorServiceLable());
						// 为ListView构建适配器对象
						RunningServiceAdapter mServiceInfoAdapter = new RunningServiceAdapter(
								RunningServiceActivity.this,
								serviceInfoList);
						listviewService.setAdapter(mServiceInfoAdapter);
						tvTotalServiceNo.setText("当前正在运行的服务共有："
								+ serviceInfoList.size());
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss(); // 取消对话框
					}
				}).create().show();
	}

	// 自定义排序 根据AppLabel排序
	private class comparatorServiceLable implements Comparator<RunningServiceItem> {

		@Override
		public int compare(RunningServiceItem object1, RunningServiceItem object2) {
			// TODO Auto-generated method stub
			return object1.getAppLabel().compareTo(object2.getAppLabel());
		}

	}

}
