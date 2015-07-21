package com.alibaba.alisecuritycenter.memoryspeedup.runnningprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;
import com.alibaba.alisecuritycenter.memoryspeedup.util.ApplicationsInfoUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RunningProgressListViewActivity extends Activity implements
		OnItemClickListener {
	private static final String TAG = "MemorySpeedUp_RunningProgressListViewActivity";
	public final static String PAR_KEY = "com.alibaba.memoryspeedup.par";
	private static final int PRE_LOADING = 0;
	private static final int LISTVIEW_LOADING = 1;
	private static final int LISTVIEW_LOADING_DONE = 2;
	private static final int LISTVIEW_CHANGE = 3;
	private static final String BAR1 = "appNumber";
	private static final String BAR2 = "availMemory";
	//private String mIntentValue;
	private Context mContext;
	// 统计栏
	private TextView mTextViewAppNumber;
	private TextView mTextViewMemSize;
	private ListView mListView;
	private static ActivityManager mActivityManager = null;
	private ArrayList<RunningProgressListItem> mRunningProgressList = null;
	private MyHandler mHandler;
	private static PackageManager packageManager = null;
	private ApplicationsInfoUtil mApplicationsInfo = null;
	private ProgressDialog dialog;
	private Map<String,String> mMap = new HashMap<String,String>();
	private RunningProgressListener runningProgressListener;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "----onCreate");
		new CustomTitleBar().getTitleBarWhole(this, getResources().getString(R.string.runningProgress),true,false);
		setContentView(R.layout.memoryspeedup_running_progress_listview);
		
		initUi();
		// 启动线程
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					//
					Message msg0 = new Message();
					msg0.what = LISTVIEW_LOADING;
					mHandler.sendMessage(msg0);
					// 获取后台程序进程信息
					mApplicationsInfo = new ApplicationsInfoUtil();
					getRunningProgressInfo();
					int appNumber = mRunningProgressList.size();
					mMap.put(BAR1, appNumber+"");
					Message msg1 = Message.obtain();
					msg1.what = PRE_LOADING;
					msg1.obj = mMap;
					mHandler.sendMessage(msg1);
					Message msg2 = Message.obtain();
					msg2.what = LISTVIEW_LOADING_DONE;
					mHandler.sendMessage(msg2);
				} catch (Exception e) {
					System.out.println("The getRunningProgressInfo Runnable thread is down>>>>>>>>>>>>");
					e.printStackTrace();
				}

			}
		};
		Thread t = new Thread(r);
		t.start();
		
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
			case PRE_LOADING:
				//activateProgressDialog();
				setBar();
				break;
			case LISTVIEW_LOADING:
				activateProgressDialog();
				break;
			case LISTVIEW_LOADING_DONE:
				deactivateProgressDialog();
				RunningProgressListViewAdapter rpla = new RunningProgressListViewAdapter(mContext, mRunningProgressList,mActivityManager);
				mListView.setAdapter(rpla);
				mListView.requestLayout();
				rpla.notifyDataSetChanged();
				mListView.setOnItemClickListener(RunningProgressListViewActivity.this);
				break;
			case LISTVIEW_CHANGE:
				
				break;
			default:
				break;
			}
		}

	}
	
	
	// 初始化Activity界面
	public void initUi() {
		mContext = this;
		mHandler = new MyHandler();
		// 获得ActivityManager服务的对象
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		packageManager = getApplicationContext().getPackageManager();
		
		mTextViewAppNumber = (TextView) findViewById(R.id.running_progress_tv_number);
		mTextViewMemSize = (TextView) findViewById(R.id.running_progress_tv_memsize);
		mListView = (ListView) findViewById(R.id.running_progress_lv);
		
		mRunningProgressList = new ArrayList<RunningProgressListItem>();
		
		System.out.println("mIntentValue is null and invoke mainActivity");
		// 获取可用内存
		String availMemStr = getAvailableMemory();
		mMap.put(BAR2, availMemStr);
		dialog = new ProgressDialog(mContext);
	}
	private String getAvailableMemory() {
		ApplicationsInfoUtil applicationsInfo = new ApplicationsInfoUtil();
		String availMemStr = applicationsInfo.getSystemMemorySize(ApplicationsInfoUtil.AVAILABLE_MEMORY,mActivityManager ,this.mContext);
		return availMemStr;
	}
	public void getRunningProgressInfo(){
		mRunningProgressList = mApplicationsInfo.getRunningProgressList(mActivityManager ,packageManager);
	}
	
	/**
	 * 激活加载程序的进度框
	 */
	public void activateProgressDialog(){
		//设置进度条风格，风格为圆形，旋转的 
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//设置ProgressDialog 标题 
		dialog.setTitle("加载程序中..."); 
		//设置ProgressDialog 提示信息 
		dialog.setMessage("请稍等..."); 
		//设置ProgressDialog 标题图标 
		//dialog.setIcon(android.R.drawable.ic_dialog_map); 
		//设置ProgressDialog 的进度条是否不明确 
		dialog.setIndeterminate(false); 
		//设置ProgressDialog 是否可以按退回按键取消
		dialog.setCancelable(false); 
		//显示 
		dialog.show();
	}
	/**
	 * 停止进度框的显示
	 */
	public void deactivateProgressDialog(){
		dialog.dismiss();
	}
	/**
	 * 设置bar，“正在运行应用xx个，可用内存xxMB”
	 */
	public void setBar(){
		String appNum = mMap.get(BAR1);
		String availMem = mMap.get(BAR2);
		mTextViewAppNumber.setText(appNum);
		mTextViewMemSize.setText(availMem);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG, "onItemClick");
		RunningProgressListItem rpli = mRunningProgressList.get(position);
		Intent mIntent = new Intent(RunningProgressListViewActivity.this,
				ProcessDetailActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putParcelable(PAR_KEY, rpli);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "----onStart");
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "----onResume");
		runningProgressListener = new RunningProgressListener();
		runningProgressListener.run();
	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "----onPause");
		runningProgressListener.callWait();
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "----onStop");
		runningProgressListener.cancel();
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
	
	
	// 内部类
	public class RunningProgressListener {
		private boolean isWait = false;
		Timer mTimer=new Timer();
		Timer mTimer1=new Timer();
		TimerTask mTimerTask = new TimerTask() {
			@Override
			public synchronized void run() {
				while (true) {
					if (isWait) {
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.d(TAG, ">>-------mTimerTask");
					Message msg1 = Message.obtain();
					System.out.println("The timer is running");
					int i = mRunningProgressList.size();
					mMap.put(BAR1, i + "");
					String availMemory = getAvailableMemory();
					mMap.put(BAR2, availMemory);

					msg1.what = PRE_LOADING;
					msg1.obj = mMap;
					mHandler.sendMessage(msg1);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		};
		TimerTask mTimerTask1 = new TimerTask() {
			@Override
			public synchronized void run() {
				while (true) {
					if (isWait) {
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.d(TAG, ">>-------mTimerTask1");
					// 获取后台程序进程信息
					mApplicationsInfo = new ApplicationsInfoUtil();
					mRunningProgressList = mApplicationsInfo.getRunningProgressList(mActivityManager, packageManager);
					Message msg2 = Message.obtain();
					msg2.what = LISTVIEW_LOADING_DONE;
					mHandler.sendMessage(msg2);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	    public void run(){
	        mTimer.schedule(mTimerTask, 5000);// 延迟5秒后执行 ，执行一次
	        mTimer1.schedule(mTimerTask1, 5000);
	    }
	    public void cancel(){
	    	mTimer.cancel();
	    	mTimer1.cancel();
	    }
	    public synchronized void callWait() {
	        isWait = true;
	    }
	 
	    public synchronized void call() {
	        isWait = false;
	        notifyAll();
	    }
	}
	

}
