package com.alibaba.alisecuritycenter.memoryspeedup.common;

import com.alibaba.alisecuritycenter.R;

import android.app.Activity;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class CustomTitleBar{

	private Activity mActivity;
	private Button mBtnBack;
	private Button mBtnSetting;
	private ImageView mImageSeperator;

	/**
	 * @param activity 
	 * @param title 标题名称
	 */
	public void getTitleBar(Activity activity, String title) {
		mActivity = activity;
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.custom_title);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		// 标题名称
		TextView textView = (TextView) mActivity.findViewById(R.id.head_center_text);
		textView.setText(title);
		// 分割线
		mImageSeperator = (ImageView) mActivity.findViewById(R.id.cumstom_title_seperator);
		
	}
	/**
	 * 
	 * @param activity
	 * @param title 标题名称
	 * @param hasBack 是否有返回键（左上角）
	 * @param hasSetting 是否有设置键（右上角）
	 */
	public void getTitleBarWhole(Activity activity, String title, boolean hasBack , boolean hasSetting) {
		mActivity = activity;
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.custom_title);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		// 标题名称
		TextView textView = (TextView) mActivity.findViewById(R.id.head_center_text);
		textView.setText(title);
		// 按钮初始化
		mBtnBack = (Button) mActivity.findViewById(R.id.head_left_image);
		mBtnSetting = (Button) mActivity.findViewById(R.id.head_right_image);
		// 分割线
		mImageSeperator = (ImageView) mActivity.findViewById(R.id.cumstom_title_seperator);
		// 后退键
		if(hasBack){
			mBtnBack.setVisibility(View.VISIBLE);
			mBtnBack.setBackgroundResource(R.drawable.hw_actionbar_back_normal);
			mBtnBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.finish();
				}
			});
		}
		else{
			mBtnBack.setVisibility(View.GONE);
		}
		// 设置键
		if(hasSetting){
			mBtnSetting.setVisibility(View.VISIBLE);
			mBtnSetting.setBackgroundResource(R.drawable.btn_setting_normal);
		}
		else{
			mBtnSetting.setVisibility(View.GONE);
		}
	}
	
}
