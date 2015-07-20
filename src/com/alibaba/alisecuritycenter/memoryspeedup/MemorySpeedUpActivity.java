package com.alibaba.alisecuritycenter.memoryspeedup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;
import com.alibaba.alisecuritycenter.memoryspeedup.runnningprocess.RunningProgressListItem;
import com.alibaba.alisecuritycenter.memoryspeedup.util.ApplicationsInfoUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MemorySpeedUpActivity extends Activity implements OnItemClickListener{

	private static final String TAG = "MemorySpeedUp_MainActivity";
	public static final int AVAILABLE_MEMORY = 0;
	public static final int TOTAL_MEMORY = 1;
	public static final int RUNGNING_PROCESS = 1;
	public static final int WHITE_LIST = 2;
	public static final int AUTO_RUN = 3;
	public static final int ONE_KEY_BTN_ENABLE = 4;
	public static final int ONE_KEY_BTN_DISABLE = 5;
	
	private static Context mContext;
	private ListView mListview;
	private MainActivityListener mainActivityListener;
	private static ActivityManager mActivityManager = null;
	private static PackageManager packageManager = null;
	private ApplicationInfo applicationInfo = null;
	private ApplicationsInfoUtil applicationsInfo ;
	private ArrayList<String> whiteList ;
	private TextView mTotleMemory;
	private TextView mAvailMemory;
	private Button mBtnOneKeyAccelerate;
	private long exitTime = 0;
	private MyHandler mHandler;
	private int runningProcessNumber = 0;
	private int whiteListNumber = 0;
	private int autoRunNumber = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new CustomTitleBar().getTitleBarWhole(this,getResources().getString(R.string.memory_accelerate),true,false);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setContentView(R.layout.memoryspeedup_main_land_activity);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setContentView(R.layout.memoryspeedup_main_activity);
		}
		
		Log.i(TAG, "mainActivity");
		System.out.println(">>>>mainActivity");
		// ��ʼ��UI
		init();
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "----onResume");
		mainActivityListener = new MainActivityListener();
		mainActivityListener.run();
		whiteList = applicationsInfo.getWhiteList(mContext);
		for(String str : whiteList){
			System.out.println("whiteList item :" + str);
		}
		getNumbers();
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "----onPause");
		mainActivityListener.callWait();
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "----onStop");
		mainActivityListener.cancel();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "----onRestart");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "----onDestroy");
	}
	//
	public class MyHandler extends Handler {
		public MyHandler() {
		}

		public MyHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AVAILABLE_MEMORY:
				Object object0 = msg.obj;
				String availMemory = (String) object0;
				mAvailMemory.setText(availMemory);
				break;
			case RUNGNING_PROCESS:
				Object object1 = msg.obj;
				int numbers[] = (int[]) object1;
				MemorySpeedUpAdapter msua = new MemorySpeedUpAdapter(mContext, numbers);
				mListview.setAdapter(msua);
				mListview.setOnItemClickListener(MemorySpeedUpActivity.this);
				break;
			case WHITE_LIST:
				
				break;
			case AUTO_RUN:
				
				break;
			case ONE_KEY_BTN_ENABLE:
				mBtnOneKeyAccelerate.setEnabled(true);
				mBtnOneKeyAccelerate.setAlpha(1.0f);
				mBtnOneKeyAccelerate.setText(getResources().getString(R.string.StaticOneKeyAccelerate));
				Toast.makeText(mContext,getResources().getString(R.string.one_key_accelerate_done), Toast.LENGTH_SHORT).show();
				break;
			case ONE_KEY_BTN_DISABLE:
				mBtnOneKeyAccelerate.setEnabled(false);
				mBtnOneKeyAccelerate.setText(getResources().getString(R.string.one_key_accelerating));
				break;
			default:
				break;
			}
		}

	}
	public void init() {
		
		mContext = this;
		mHandler = new MyHandler();
		// ���ActivityManager����Ķ���
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		packageManager = getApplicationContext().getPackageManager();
		
		mTotleMemory = (TextView) findViewById(R.id.tv_totalMemory);
		mAvailMemory = (TextView) findViewById(R.id.tv_availMemory);
		mBtnOneKeyAccelerate = (Button) findViewById(R.id.bt_oneKeyAccelerate);
		
		//ArrayList<String> test = applicationsInfo.getWhiteList(this);
		// ��ÿ����ڴ���Ϣ
		applicationsInfo = new ApplicationsInfoUtil();
		String availMemStr = applicationsInfo.getSystemMemorySize(ApplicationsInfoUtil.AVAILABLE_MEMORY,mActivityManager ,mContext);
		mAvailMemory.setText(availMemStr);
		// ������ڴ���Ϣ
		String totalMemStr = applicationsInfo.getSystemMemorySize(ApplicationsInfoUtil.TOTAL_MEMORY,mActivityManager ,mContext);
		mTotleMemory.setText(totalMemStr);
		
		int number[] = this.getNumbers();
		mListview = (ListView) findViewById(R.id.lv_total);
		MemorySpeedUpAdapter msua = new MemorySpeedUpAdapter(this, number);
		mListview.setAdapter(msua);
		mListview.setOnItemClickListener(MemorySpeedUpActivity.this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("mAvailMemory", mAvailMemory.getText().toString());
		if(position == 0){
			//��ת�����������е�Ӧ�ó����б�
			intent.setClassName(MemorySpeedUpActivity.this,
					"com.alibaba.alisecuritycenter.memoryspeedup.runnningprocess.RunningProgressListViewActivity");
			startActivity(intent);
		}
		else if(position == 1){
			//��ת����Ӧ�ð������б�
			intent.setClassName(MemorySpeedUpActivity.this,
					"com.alibaba.alisecuritycenter.memoryspeedup.whitelist.WhiteListActivity");
			startActivity(intent);
		}
		else if(position == 2){
			//��ת���������������б�
		}
		else{
			//��ת�����������еķ���
			intent.setClassName(MemorySpeedUpActivity.this,
					"com.alibaba.alisecuritycenter.memoryspeedup.runningservice.RunningServiceActivity");
			startActivity(intent);
		}
	}

	/**
	 * There are three numbers that need to be transfer to the adapter , which are : 
	 * <br>1. the number of running applications
	 * <br>2. the number of whiteList
	 * <br>3. the number of autoRun
	 */
	public int[] getNumbers() {
		int number[] = {0,0,0,0}; 
		// running application number
		number[0] = getRunningProcessNumber();
		// whiteList application number
		number[1] = getWhiteListAppNumber();
		// autoRun application number
		number[2] = 0;
		// service Number
		number[3] = 0;
		
		Message msg = Message.obtain();
		msg.what = RUNGNING_PROCESS;
		msg.obj = number;
		mHandler.sendMessage(msg);
		
		return number;
	}
	private int getWhiteListAppNumber() {
		int whiteListAppNumber = 0;
		whiteListAppNumber = applicationsInfo.getWhiteListAppNumber(mContext);
		return whiteListAppNumber;
	}
	private int getRunningProcessNumber(){
		int runningProcessNumber = 0;
		runningProcessNumber = applicationsInfo.getRunningProcessNumber(MemorySpeedUpActivity.mActivityManager,
				MemorySpeedUpActivity.packageManager);
		return runningProcessNumber;
	}

	
	@SuppressLint("NewApi")
	public void oneKeyAccelerate(View view){
		mBtnOneKeyAccelerate.setAlpha(0.4f);
		mBtnOneKeyAccelerate.setEnabled(false);
		mBtnOneKeyAccelerate.setText(this.getResources().getString(R.string.one_key_accelerating));
		new TaskLoopKillApp().start();
	}

	// �ڲ���
	/**
	 * ��ʱˢ��
	 * @author linkun.lk
	 *
	 */
	public class MainActivityListener {
		private boolean isWait = false;
		Timer mTimer = new Timer();
		TimerTask mTimerTask = new TimerTask() {
			@Override
			public synchronized void run() {
				while (true) {
					if (isWait) {
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Log.d(TAG, ">>-------mTimerTask");
					int numbers[] = getNumbers();
					Message msg = Message.obtain();
					msg.obj = numbers;
					msg.what = RUNGNING_PROCESS;
					mHandler.sendMessage(msg);
					
					Message msg1 = Message.obtain();
					applicationsInfo = new ApplicationsInfoUtil();
					String availMemStr = applicationsInfo.getSystemMemorySize(ApplicationsInfoUtil.AVAILABLE_MEMORY
							,MemorySpeedUpActivity.mActivityManager ,MemorySpeedUpActivity.mContext);
					msg1.obj = availMemStr;
					msg1.what = AVAILABLE_MEMORY;
					mHandler.sendMessage(msg1);
					
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		};

		public void run() {
			mTimer.schedule(mTimerTask, 5000);// �ӳ�5���ִ�� ��ִ��һ��
		}

		public void cancel() {
			mTimer.cancel();
		}

		public synchronized void callWait() {
			isWait = true;
		}

		public synchronized void call() {
			isWait = false;
			notifyAll();
		}
	}
	
	public class TaskLoopKillApp extends Thread {
        public void run() {
            synchronized (this) {
                killApp();
            }
        }
    }
	public void killApp() {
		// TODO Auto-generated method stub
		ArrayList<RunningProgressListItem> mList = new ArrayList<>();
		mList = applicationsInfo.getRunningProgressAllList(mActivityManager, packageManager);
		for(RunningProgressListItem tmp : mList){
			System.out.println("++++kill :  "+tmp.getProcessName());
			// �Ƿ������whiteList��
			if(whiteList.contains(tmp.getPackageName())){
				System.out.println("whiteList contains "+ tmp.getPackageName());
				continue;
			}
			// �÷�������������⿴�ļ�ĩβ������
			mActivityManager.killBackgroundProcesses(tmp.getPackageName());
		}
		
		Message msg = Message.obtain();
		msg.what = ONE_KEY_BTN_ENABLE;
		mHandler.sendMessage(msg);
		
	}
	

}



/**
 * ��һ��Ӧ�ã���Ҫǿ�ƹرս��̡�
����ʹ��ActivityManager��killBackgroundProcesses������
��ҪȨ��android.permission.KILL_BACKGROUND_PROCESSES��
��ʹ�ô˷���ɱ�����̺󣬽��̻�������Դ���н������£�
Have the system immediately kill all background processes associated with the given package.  
This is the same as the kernel killing those processes to reclaim memory; 
the system will take care of restarting these processes in the future as needed.

Ϊ��ǿ�ƹرս��̣�ϣ��ʹ��ActivityManager������һ��������forceStopPackage��
Դ���н������£�
Have the system perform a force stop of everything associated with the given application package.  
All processes that share its uid will be killed, 
all services it has running stopped, all activities removed, etc.  
In addition, a {@link Intent#ACTION_PACKAGE_RESTARTED} broadcast will be sent, 
so that any of its registered alarms can be stopped, notifications removed, etc.

ʹ�����������������Ҫע�⣺
- �˷�����@hide�ķ�����
���������ʹ��java�ķ��������ɵ��ã��������£�
ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
method.invoke(mActivityManager, packageName);  //packageName����Ҫǿ��ֹͣ��Ӧ�ó������
- �˷�����ҪȨ�ޣ�android.permission.FORCE_STOP_PACKAGES
�������ַ������Ȩ�ޡ�
���Ȩ����frameworks/base/core/res/AndroidManifest.xml�ļ�������������:
<permission android:name="android.permission.FORCE_STOP_PACKAGES"
 android:permissionGroup="android.permission-group.SYSTEM_TOOLS"
 android:protectionLevel="signature"
 android:label="@string/permlab_forceStopPackages"
 android:description="@string/permdesc_forceStopPackages"/>
 ע��protectionLevel����ֵδsignature����sdk�ĵ�
 http://developer.android.com/guide/topics/manifest/permission-element.html#plevel�ж���һ���ԵĽ������£�
A permission that the system grants only if the requesting application is signed with the same certificate 
as the application that declared the permission. If the certificates match, 
the system automatically grants the permission without notifying the user or asking for the user's explicit approval.
��˼�ǣ�appʹ��FORCE_STOP_PACKAGESȨ�ޣ�app��������Ȩ�޵������ߵ�ǩ������һ�£�
FORCE_STOP_PACKAGES����������frameworks/base/core/res/,������frameworks/base/core/res/Android.mk�п�������ǩ����Ϣ��

LOCAL_NO_STANDARD_LIBRARIES := true
LOCAL_PACKAGE_NAME := framework-res
LOCAL_CERTIFICATE := platform


����ǩ��Ϊplatform. 
���յõ����ۣ�app��Ҫ��platformǩ�����ſ���ʹ��forceStopPackage������
�����кܶ������ἰ����Ҫ��app��AndroidManifest.xml�����android:sharedUserId="android.uid.system"һ�仰��
��sdk��http://developer.android.com/guide/topics/manifest/manifest-element.html���Դ˵Ľ��ͣ�
android:sharedUserId
The name of a Linux user ID that will be shared with other applications. 
By default, Android assigns each application its own unique user ID. 
However, if this attribute is set to the same value for two or more applications, 
they will all share the same ID �� provided that they are also signed by the same certificate. 
Application with the same user ID can access each other's data and, if desired, run in the same process.
��˼�ǣ�����appʹ������ͬ��user id���Ϳ��Ի�����ʶԷ������ݡ�
��ˣ�appʹ��android.uid.system��user id���Ϳ��Է���ϵͳ���ݡ�
ע�ⱳ��Ϊ��ɫ��һ�䣬������Ȼ��Ҫ����app����ͬ��ǩ�����С�

��������������ǣ�
��λ�ȡӦ��ǩ����
 */
