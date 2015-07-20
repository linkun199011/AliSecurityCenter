package com.alibaba.alisecuritycenter.memoryspeedup.whitelist;

import java.util.ArrayList;
import com.alibaba.alisecuritycenter.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class WhiteListAdapter extends BaseAdapter {
	private final String TAG = "WhiteListAdapter";
	private Context mContext;
	private static PackageManager mPackageManager;
	private ApplicationInfo mApplicatinoInfo;
	private SharedPreferences sp ;
	private ArrayList<ApplicationInfo> mApplicationInfoList = new ArrayList<>();
	
	public WhiteListAdapter(Context context ,ArrayList<ApplicationInfo> mApplicationInfoList) {
		super();
		this.mContext = context;
		this.mApplicationInfoList = mApplicationInfoList;
		sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mApplicationInfoList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ApplicationInfo apppplicationInfo = mApplicationInfoList.get(position);
		ViewHolder holder;
		if(convertView == null){
			Log.d(TAG,"new ViewHolder");
			convertView = View.inflate(mContext, R.layout.memoryspeedup_white_list_listview_item, null);
			holder = new ViewHolder();
			holder.iv_appIcon = (ImageView) convertView.findViewById(R.id.iv_white_list_appIcon);
			holder.tv_appName = (TextView) convertView.findViewById(R.id.tv_white_list_app_name);
			holder.cb_select = (CheckBox) convertView.findViewById(R.id.cb_white_list);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			mPackageManager = mContext.getPackageManager();
			mApplicatinoInfo = mPackageManager.getApplicationInfo(apppplicationInfo.packageName, PackageManager.GET_META_DATA);
		} catch (Exception e) {
			mApplicatinoInfo = null;
		}
		String applicationName = (String) mPackageManager
				.getApplicationLabel(mApplicatinoInfo);
		
		final AppInfo appInfo = new AppInfo();
		appInfo.packageName = apppplicationInfo.packageName;
		appInfo.appName = applicationName;
		
		Drawable drawable = mPackageManager.getApplicationIcon(mApplicatinoInfo);
		holder.iv_appIcon.setImageDrawable(drawable);
		holder.tv_appName.setText(appInfo.appName);
		
		boolean isIgnore = false;
		if(sp.getString(appInfo.packageName, "false").equals("true")){
			isIgnore = true;
		}
		
		holder.cb_select.setChecked(isIgnore);
		holder.cb_select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//if the key is already in the "sp.xml" , then ,this Onlick means set checkbox UnChecked
				//otherwise , set checkbox Checked.
				Editor editor = sp.edit();
				if(sp.getString(appInfo.packageName, "false").equals("true")){
					editor.remove(appInfo.packageName);
				}
				else{
					editor.putString(appInfo.packageName, "true");
				}
				editor.commit();
			}
		});
		return convertView;
	}
	static class ViewHolder{
		ImageView iv_appIcon;
		TextView tv_appName;
		CheckBox cb_select;
	}
	private class AppInfo{
		String packageName;
		String appName;
		boolean isIgnore;
	}

}
