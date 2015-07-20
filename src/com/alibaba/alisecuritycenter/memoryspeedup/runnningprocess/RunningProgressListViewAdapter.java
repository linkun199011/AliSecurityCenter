package com.alibaba.alisecuritycenter.memoryspeedup.runnningprocess;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.alisecuritycenter.R;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RunningProgressListViewAdapter extends BaseAdapter {
	private String TAG = "RunningProgressListViewAdapter";
	private Context mContext;
	private PackageManager packageManager = null;
	private ApplicationInfo applicationInfo = null;
	private ActivityManager mActivityManager = null;
	private List<RunningProgressListItem> mRunningProgressList = null;
	public RunningProgressListViewAdapter(Context mContext,
			List<RunningProgressListItem> mRunningProgressList , ActivityManager mActivityManager) {
		super();
		this.mContext = mContext;
		this.mRunningProgressList = mRunningProgressList;
		this.mActivityManager = mActivityManager;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRunningProgressList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		final RunningProgressListItem rpli = mRunningProgressList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			//Log.i(TAG,"new ViewHolder()"); 
			convertView = View.inflate(mContext, R.layout.memoryspeedup_running_progress_listview_item, null);
			holder = new ViewHolder();
			holder.iv_appIcon = (ImageView) convertView.findViewById(R.id.iv_appIcon);
			holder.tv_appName = (TextView) convertView.findViewById(R.id.running_progress_tv_app_name);
			holder.tv_memSize = (TextView) convertView.findViewById(R.id.running_progress_tv_single_memsize);
			holder.tv_whiteList= (TextView) convertView.findViewById(R.id.running_progress_tv_protected_by_whitelist); 
			holder.bt_stop = (Button) convertView.findViewById(R.id.running_progress_bt_stop);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
			packageManager = mContext.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(rpli.getPackageName(), PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
			e.printStackTrace();
		}
		Drawable appIcon = packageManager.getApplicationIcon(applicationInfo);
		
		holder.iv_appIcon.setImageDrawable(appIcon);
		holder.tv_appName.setText(rpli.getAppName());
		holder.tv_memSize.setText(rpli.getMemSize()+" Kb");
		if(rpli.isProtected() == false ){
			holder.tv_whiteList.setText("");
		}
		holder.bt_stop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext,"Process Killed : " + rpli.getAppName()  ,Toast.LENGTH_SHORT).show();
				ArrayList<RunningProgressListItem> sameUIDProgressList = new ArrayList<RunningProgressListItem>() ;
				
				int uid = rpli.getUid();
				// 获取同一个UID的程序
				for(RunningProgressListItem tmp : mRunningProgressList){
					if(uid == tmp.getUid() ){
						//System.out.println("++++++getUID + ");
						sameUIDProgressList.add(tmp);
					}
				}
				// 删除同一个UID的所有程序
				for(RunningProgressListItem tmp : sameUIDProgressList){
					//System.out.println("++++kill :  "+tmp.getProcessName());
					mActivityManager.killBackgroundProcesses(tmp.getPackageName());
					Toast.makeText(mContext,"Process Killed : " + tmp.getAppName()  ,Toast.LENGTH_SHORT).show();
				}
				mRunningProgressList.removeAll(sameUIDProgressList);
				RunningProgressListViewAdapter.this.notifyDataSetChanged();
			}
		});
		
		return convertView;
	}
	static class ViewHolder {
		ImageView iv_appIcon;
		TextView tv_appName;
		TextView tv_memSize;
		TextView tv_whiteList;
		Button bt_stop;
		
		}

}
