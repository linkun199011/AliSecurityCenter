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
	// ProcessInfo Model�� �����������н�����Ϣ
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
		// ���ActivityManager����Ķ���
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		// ��ǰӦ�õĴ���ִ��Ŀ¼
		upgradeRootPermission(getPackageCodePath());
		// ����������е�Service��Ϣ
		getRunningServiceInfo();
		// �Լ�������
		Collections.sort(serviceInfoList, new comparatorServiceLable());
		System.out.println(serviceInfoList.size() + "-------------");
		// ΪListView��������������
		RunningServiceAdapter mServiceInfoAdapter = new RunningServiceAdapter(
				RunningServiceActivity.this, serviceInfoList);
		listviewService.setAdapter(mServiceInfoAdapter);
		tvTotalServiceNo.setText("��ǰ�������еķ����У�" + serviceInfoList.size());
		
	}

	/**
	 * Ӧ�ó������������ȡ RootȨ�ޣ��豸�������ƽ�(���ROOTȨ��)
	 * 
	 * @return Ӧ�ó�����/���ȡRootȨ��
	 */
	public static boolean upgradeRootPermission(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // �л���root�ʺ�
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

	// ���ϵͳ�������еĽ�����Ϣ
	private void getRunningServiceInfo() {
		// ����һ��Ĭ��Service��������С
		int defaultNum = 100;
		// ͨ������ActivityManager��getRunningAppServicees()�������ϵͳ�������������еĽ���
		List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager
				.getRunningServices(defaultNum);
		System.out.println(runServiceList.size());
		// ServiceInfo Model�� �����������н�����Ϣ
		serviceInfoList = new ArrayList<RunningServiceItem>();
		for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
			// ���Service���ڵĽ��̵���Ϣ
			int pid = runServiceInfo.pid; // service���ڵĽ���ID��
			int uid = runServiceInfo.uid; // �û�ID ������Linux��Ȩ�޲�ͬ��IDҲ�Ͳ�ͬ ���� root��
			// ��������Ĭ���ǰ�������������android��processָ��
			String processName = runServiceInfo.process;
			// ��Service����ʱ��ʱ��ֵ
			long activeSince = runServiceInfo.activeSince;

			// �����Service��ͨ��Bind������ʽ���ӣ���clientCount������service���ӿͻ��˵���Ŀ
			int clientCount = runServiceInfo.clientCount;

			// ��ø�Service�������Ϣ ������pkgname/servicename
			ComponentName serviceCMP = runServiceInfo.service;
			String serviceName = serviceCMP.getShortClassName(); // service ������
			String pkgName = serviceCMP.getPackageName(); // ����

			// ��ӡLog
			Log.i(TAG, "���ڽ���id :" + pid + " ���ڽ�������" + processName + " ���ڽ���uid:"
					+ uid + "\n" + " service������ʱ��ֵ��" + activeSince
					+ " �ͻ��˰���Ŀ:" + clientCount + "\n" + "��service�������Ϣ:"
					+ serviceName + " and " + pkgName);

			// �������ͨ��service�������Ϣ������PackageManager��ȡ��service����Ӧ�ó���İ��� ��ͼ���
			PackageManager mPackageManager = this.getPackageManager(); // ��ȡPackagerManager����;

			try {
				// ��ȡ��pkgName����Ϣ
				ApplicationInfo appInfo = mPackageManager.getApplicationInfo(
						pkgName, 0);

				RunningServiceItem runService = new RunningServiceItem();
				runService.setAppIcon(appInfo.loadIcon(mPackageManager));
				runService.setAppLabel(appInfo.loadLabel(mPackageManager) + "");
				runService.setServiceName(serviceName);
				runService.setPkgName(pkgName);
				// ���ø�service�������Ϣ
				Intent intent = new Intent();
				intent.setComponent(serviceCMP);
				runService.setIntent(intent);

				runService.setPid(pid);
				runService.setProcessName(processName);

				// �����������
				serviceInfoList.add(runService);

			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("--------------------- error -------------");
				e.printStackTrace();
			}

		}
	}

	// ������ֹͣ
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		final Intent stopserviceIntent = serviceInfoList.get(position)
				.getIntent();

		new AlertDialog.Builder(RunningServiceActivity.this)
				.setTitle("�Ƿ�ֹͣ����")
				.setMessage("����ֻ�������������󣬲ſ��Լ������С�������ܻ�������г�Ӧ�ó���������벻���Ľ����")
				.setPositiveButton("ֹͣ", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// ֹͣ��Service
						// ����Ȩ�޲��������⣬Ϊ�˱���Ӧ�ó�������쳣�������SecurityException ���������Ի���
						try {
							stopService(stopserviceIntent);
							//unbindService((ServiceConnection) stopserviceIntent);
						} catch (SecurityException sEx) {
							// �����쳣 ˵��Ȩ�޲���
							System.out.println(" deny the permission");
							new AlertDialog.Builder(
									RunningServiceActivity.this)
									.setTitle("Ȩ�޲���")
									.setMessage("�Բ�������Ȩ�޲������޷�ֹͣ��Service")
									.create().show();
						}
						// ˢ�½���
						// ����������е�Service��Ϣ
						getRunningServiceInfo();
						// �Լ�������
						Collections.sort(serviceInfoList,new comparatorServiceLable());
						// ΪListView��������������
						RunningServiceAdapter mServiceInfoAdapter = new RunningServiceAdapter(
								RunningServiceActivity.this,
								serviceInfoList);
						listviewService.setAdapter(mServiceInfoAdapter);
						tvTotalServiceNo.setText("��ǰ�������еķ����У�"
								+ serviceInfoList.size());
					}

				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss(); // ȡ���Ի���
					}
				}).create().show();
	}

	// �Զ������� ����AppLabel����
	private class comparatorServiceLable implements Comparator<RunningServiceItem> {

		@Override
		public int compare(RunningServiceItem object1, RunningServiceItem object2) {
			// TODO Auto-generated method stub
			return object1.getAppLabel().compareTo(object2.getAppLabel());
		}

	}

}
