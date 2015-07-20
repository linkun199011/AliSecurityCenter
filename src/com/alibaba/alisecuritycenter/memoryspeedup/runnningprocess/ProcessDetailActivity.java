package com.alibaba.alisecuritycenter.memoryspeedup.runnningprocess;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProcessDetailActivity extends Activity {
	private static final String TAG= "ProcessDetailActivity";
	private Intent mItent ;
	private RunningProgressListItem mRPLI = null;
	private ActivityManager mActivityManager = null;
	private PackageManager packageManager = null;
	private ApplicationInfo applicationInfo = null;
	
	private ImageView mImageViewAppIcon;
	private TextView mTextViewPackageName;
	private TextView mTextViewProcessName;
	private TextView mTextViewAppName;
	private TextView mTextViewPid;
	private TextView mTextViewUid;
	private TextView mTextViewMemSize;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		new CustomTitleBar().getTitleBarWhole(this, getResources().getString(R.string.detail_process),true,false);
		setContentView(R.layout.memoryspeedup_progress_detail);
		mActivityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE); 
		mItent = this.getIntent();
		mRPLI = (RunningProgressListItem)mItent.getParcelableExtra(RunningProgressListViewActivity.PAR_KEY);  
		//mRPLI = (RunningProgressListItem)mItent.getSerializableExtra("ListItem");
		System.out.println(">>>>>>>>>>ProcessDetailActivity : " + mRPLI.getAppName()+mRPLI.getPid());
		initUi();
	};
	public void initUi(){
		Log.i(TAG, "initUi");
		mImageViewAppIcon = (ImageView) findViewById(R.id.iv_detail_appIcon);
		mTextViewPackageName = (TextView) findViewById(R.id.tv_detail_packageName);
		mTextViewProcessName = (TextView) findViewById(R.id.tv_detail_processName);
		mTextViewAppName = (TextView) findViewById(R.id.tv_detail_appName);
		mTextViewPid = (TextView) findViewById(R.id.tv_detail_pid);
		mTextViewUid = (TextView) findViewById(R.id.tv_detail_uid);
		mTextViewMemSize = (TextView) findViewById(R.id.tv_detail_memSize);
		
		try {
			packageManager = this.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(mRPLI.getPackageName(), PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
			e.printStackTrace();
		}
		Drawable appIcon = packageManager.getApplicationIcon(applicationInfo);
		
		mImageViewAppIcon.setImageDrawable(appIcon);
		mTextViewPackageName.setText(mRPLI.getPackageName());
		mTextViewProcessName.setText(mRPLI.getProcessName());
		mTextViewAppName.setText(mRPLI.getAppName());
		mTextViewPid.setText(mRPLI.getPid()+"");
		mTextViewUid.setText(mRPLI.getUid()+"");
		mTextViewMemSize.setText(mRPLI.getMemSize());
		
	}

/*	public void killApp(View view) {
		new AlertDialog.Builder(this)
				.setMessage("是否杀死该进程")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//if (which == 0) {
							// 杀死该进程，释放进程占用的空间
							System.out.println("which == 0");
						    
							mActivityManager.killBackgroundProcesses(mRPLI.getPackageName());
							Toast.makeText(getBaseContext(),"Process Killed : " + mRPLI.getAppName()  ,Toast.LENGTH_LONG).show();
							// 返回原来的界面
							Intent intent = new Intent();
							intent.setClassName(ProcessDetailActivity.this,
									"com.alibaba.memoryspeedup.RunningProgressListViewActivity");
							finish();
							startActivity(intent);
						//}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//if(which == 1) {
							System.out.println("which == 1");
							dialog.cancel();
						//}
					}
				}).create().show();
	}
*/	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
