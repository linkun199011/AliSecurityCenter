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
		// 初始化UI
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
		// 获得ActivityManager服务的对象
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		packageManager = getApplicationContext().getPackageManager();
		
		mTotleMemory = (TextView) findViewById(R.id.tv_totalMemory);
		mAvailMemory = (TextView) findViewById(R.id.tv_availMemory);
		mBtnOneKeyAccelerate = (Button) findViewById(R.id.bt_oneKeyAccelerate);
		
		//ArrayList<String> test = applicationsInfo.getWhiteList(this);
		// 获得可用内存信息
		applicationsInfo = new ApplicationsInfoUtil();
		String availMemStr = applicationsInfo.getSystemMemorySize(ApplicationsInfoUtil.AVAILABLE_MEMORY,mActivityManager ,mContext);
		mAvailMemory.setText(availMemStr);
		// 获得总内存信息
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
			//跳转至：正在运行的应用程序列表
			intent.setClassName(MemorySpeedUpActivity.this,
					"com.alibaba.alisecuritycenter.memoryspeedup.runnningprocess.RunningProgressListViewActivity");
			startActivity(intent);
		}
		else if(position == 1){
			//跳转至：应用白名单列表
			intent.setClassName(MemorySpeedUpActivity.this,
					"com.alibaba.alisecuritycenter.memoryspeedup.whitelist.WhiteListActivity");
			startActivity(intent);
		}
		else if(position == 2){
			//跳转至：程序自启动列表
		}
		else{
			//跳转至：正在运行的服务
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

	// 内部类
	/**
	 * 定时刷新
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
			mTimer.schedule(mTimerTask, 5000);// 延迟5秒后执行 ，执行一次
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
			// 是否包含在whiteList中
			if(whiteList.contains(tmp.getPackageName())){
				System.out.println("whiteList contains "+ tmp.getPackageName());
				continue;
			}
			// 该方法：具体的问题看文件末尾！！！
			mActivityManager.killBackgroundProcesses(tmp.getPackageName());
		}
		
		Message msg = Message.obtain();
		msg.what = ONE_KEY_BTN_ENABLE;
		mHandler.sendMessage(msg);
		
	}
	

}



/**
 * 做一个应用，需要强制关闭进程。
可以使用ActivityManager的killBackgroundProcesses方法，
需要权限android.permission.KILL_BACKGROUND_PROCESSES。
但使用此方法杀死进程后，进程会重启。源码中解释如下：
Have the system immediately kill all background processes associated with the given package.  
This is the same as the kernel killing those processes to reclaim memory; 
the system will take care of restarting these processes in the future as needed.

为了强制关闭进程，希望使用ActivityManager的另外一个方法，forceStopPackage。
源码中解释如下：
Have the system perform a force stop of everything associated with the given application package.  
All processes that share its uid will be killed, 
all services it has running stopped, all activities removed, etc.  
In addition, a {@link Intent#ACTION_PACKAGE_RESTARTED} broadcast will be sent, 
so that any of its registered alarms can be stopped, notifications removed, etc.

使用这个方法有两点需要注意：
- 此方法是@hide的方法：
解决方案是使用java的反射机制完成调用，代码如下：
ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
method.invoke(mActivityManager, packageName);  //packageName是需要强制停止的应用程序包名
- 此方法需要权限：android.permission.FORCE_STOP_PACKAGES
下面着手分析这个权限。
这个权限在frameworks/base/core/res/AndroidManifest.xml文件中声明，如下:
<permission android:name="android.permission.FORCE_STOP_PACKAGES"
 android:permissionGroup="android.permission-group.SYSTEM_TOOLS"
 android:protectionLevel="signature"
 android:label="@string/permlab_forceStopPackages"
 android:description="@string/permdesc_forceStopPackages"/>
 注意protectionLevel属性值未signature。看sdk文档
 http://developer.android.com/guide/topics/manifest/permission-element.html#plevel中对这一属性的解释如下：
A permission that the system grants only if the requesting application is signed with the same certificate 
as the application that declared the permission. If the certificates match, 
the system automatically grants the permission without notifying the user or asking for the user's explicit approval.
意思是：app使用FORCE_STOP_PACKAGES权限，app必须和这个权限的声明者的签名保持一致！
FORCE_STOP_PACKAGES的声明者是frameworks/base/core/res/,可以在frameworks/base/core/res/Android.mk中看到它的签名信息：

LOCAL_NO_STANDARD_LIBRARIES := true
LOCAL_PACKAGE_NAME := framework-res
LOCAL_CERTIFICATE := platform


即，签名为platform. 
最终得到结论，app需要是platform签名，才可以使用forceStopPackage方法！
网上有很多文章提及，需要在app的AndroidManifest.xml中添加android:sharedUserId="android.uid.system"一句话。
看sdk（http://developer.android.com/guide/topics/manifest/manifest-element.html）对此的解释：
android:sharedUserId
The name of a Linux user ID that will be shared with other applications. 
By default, Android assigns each application its own unique user ID. 
However, if this attribute is set to the same value for two or more applications, 
they will all share the same ID — provided that they are also signed by the same certificate. 
Application with the same user ID can access each other's data and, if desired, run in the same process.
意思是，两个app使用了相同的user id，就可以互相访问对方的数据。
因此，app使用android.uid.system的user id，就可以访问系统数据。
注意背景为黄色的一句，这里依然需要两个app有相同的签名才行。

接下来的问题就是：
如何获取应用签名。
 */
