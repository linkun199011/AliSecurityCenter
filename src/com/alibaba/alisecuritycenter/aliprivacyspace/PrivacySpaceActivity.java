package com.alibaba.alisecuritycenter.aliprivacyspace;

import java.util.ArrayList;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.memoryspeedup.MemorySpeedUpActivity;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PrivacySpaceActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "PrivacySpaceActivity";
	private static final String INTENT_COMMUNICATION = 
			"com.alibaba.alisecuritycenter.aliprivacyspace.communication.PrivateCommunicationActivity";
	private static final String INTENT_PHOTO = 
			"com.alibaba.alisecuritycenter.aliprivacyspace.photo.PrivatePhotoActivity";
	private static final String INTENT_FILE = 
			"com.alibaba.alisecuritycenter.aliprivacyspace.file.PrivateFileActivity";
	private static final String INTENT_APPLOCK = 
			"com.alibaba.alisecuritycenter.aliprivacyspace.applock.PrivateAppLockActivity";
	private Context mContext;
	private ListView mListView;
	private static final int ITEM_NUMBER = 4;
	private String[] titels; // title
	private String[] subTitles; // subtitle
	private Drawable[]  drawableIcons = new Drawable[ITEM_NUMBER]; // picture
	private ArrayList<PrivacySpaceListItem> mList = new ArrayList<>();
	private PrivacySpaceAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String titleName = getString(R.string.privacy_space);
		new CustomTitleBar().getTitleBarWhole(this,titleName,true,true);
		setContentView(R.layout.privacyspace_main_activity);
		initView();
		initItems();
	}

	private void initView() {
		mContext = this;
		mListView = (ListView) findViewById(R.id.lv_privacyspace);
	}
	private void initItems() {
		Resources res = getResources();
		titels = res.getStringArray(R.array.privacyspace_items_title);
		subTitles = res.getStringArray(R.array.privacyspace_items_subtitle);
		int[] intIcons = {R.drawable.ic_security_pricacy_contact_normal,
				R.drawable.ic_security_pricacy_photo_normal,
				R.drawable.ic_security_pricacy_paper_normal,
				R.drawable.ic_security_pricacy_key_normal};
		// get four icon
		for(int i = 0 ; i < ITEM_NUMBER ; i++ ) {
			drawableIcons[i] = res.getDrawable(intIcons[i]);
			Log.i(TAG, drawableIcons[i]+"");
			Log.i(TAG, titels[i]+"");
			Log.i(TAG, subTitles[i]+"");
		}
		// get mList
		for(int i = 0 ; i < ITEM_NUMBER ; i++ ){
			PrivacySpaceListItem item = new PrivacySpaceListItem();
			item.setDrawable(drawableIcons[i]);
			item.setTitle(titels[i]);
			item.setSubtitle(subTitles[i]);
			
			mList.add(item);
		}
		mAdapter = new PrivacySpaceAdapter(mContext, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	private class PrivacySpaceAdapter extends BaseAdapter{
		private ImageView icon;
		private TextView tvTitle;
		private TextView tvSubTitle;
		private Context adapterContext;
		private ArrayList<PrivacySpaceListItem> adapterList;
		public PrivacySpaceAdapter(Context context , ArrayList<PrivacySpaceListItem> arrayList){
			adapterContext = context;
			adapterList = arrayList;
		}
		@Override
		public int getCount() {
			return ITEM_NUMBER;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PrivacySpaceListItem item = adapterList.get(position);
			View v = View.inflate(adapterContext, R.layout.privacyspace_main_listview_item, null);
			icon = (ImageView) v.findViewById(R.id.iv_privacyspace_item);
			tvTitle = (TextView) v.findViewById(R.id.tv_privacyspace_item_title);
			tvSubTitle = (TextView) v.findViewById(R.id.tv_privacyspace_item_subtitle);
			
			icon.setImageDrawable(item.getDrawable());
			tvTitle.setText(item.getTitle());
			tvSubTitle.setText(item.getSubtitle());
			return v;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		if(position == 0){
			// Ìø×ªÒþË½Í¨Ñ¶
			intent.setClassName(PrivacySpaceActivity.this , INTENT_COMMUNICATION);
			startActivity(intent);
		}
		
	}
}
