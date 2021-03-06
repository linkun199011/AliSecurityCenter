package com.alibaba.alisecuritycenter.aliprivacyspace;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.aliprivacyspace.common.Constants;
import com.alibaba.alisecuritycenter.aliprivacyspace.common.MD5;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.DBHelper;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.MimetypesDAO;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.PasswordDAO;
import com.alibaba.alisecuritycenter.aliprivacyspace.entity.PasswordInfo;
import com.alibaba.alisecuritycenter.aliprivacyspace.widget.GestureContentView;
import com.alibaba.alisecuritycenter.aliprivacyspace.widget.LockIndicator;
import com.alibaba.alisecuritycenter.aliprivacyspace.widget.GestureDrawline.GestureCallBack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 手势密码设置界面
 *
 */
public class GestureEditActivity extends Activity implements OnClickListener {
	/** 手机号码*/
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	/** 首次提示绘制手势密码，可以选择跳过 */
	public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
	private TextView mTextTitle;
	private TextView mTextCancel;
	private LockIndicator mLockIndicator;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private String mParamSetUpcode = null;
	private String mParamPhoneNumber;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;
	private String mConfirmPassword = null;
	private int mParamIntentCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacyspace_gesture_edit);
		setUpViews();
		setUpListeners();
	}
	
	private void setUpViews() {
		mTextTitle = (TextView) findViewById(R.id.text_title);
		mTextCancel = (TextView) findViewById(R.id.text_cancel);
		mTextReset = (TextView) findViewById(R.id.text_reset);
		mTextReset.setClickable(false);
		mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, false, "", new GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
				if (!isInputPassValidate(inputCode)) {
					mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>"));
					mGestureContentView.clearDrawlineState(0L);
					return;
				}
				if (mIsFirstInput) {
					mFirstPassword = inputCode;
					updateCodeList(inputCode);
					mGestureContentView.clearDrawlineState(0L);
					mTextReset.setClickable(true);
					mTextReset.setText(getString(R.string.reset_gesture_code));
				} else {
					if (inputCode.equals(mFirstPassword)) {
						Toast.makeText(GestureEditActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
						//you can save THE password into the database here.
						updatePassword();
						mGestureContentView.clearDrawlineState(0L);
						GestureEditActivity.this.finish();
					}
					else {
						mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						// 保持绘制的线，1.5秒后清除
						mGestureContentView.clearDrawlineState(1300L);
					}
				}
				mIsFirstInput = false;
			}

			@Override
			public void checkedSuccess() {
				
			}

			@Override
			public void checkedFail() {
				
			}
		});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");
	}
	
	private void setUpListeners() {
		mTextCancel.setOnClickListener(this);
		mTextReset.setOnClickListener(this);
	}
	
	private void updateCodeList(String inputCode) {
		// 更新选择的图案
		mLockIndicator.setPath(inputCode);
	}
	/**
	 * 更新数据库password表
	 * <p>（1）如果该表存在旧的记录，则update
	 * <p>（2）如果该表没有任何记录，则insert
	 */
	private void updatePassword(){
		Toast.makeText(GestureEditActivity.this, "The MD5 is : "
			+MD5.getMD5(mFirstPassword), Toast.LENGTH_SHORT).show();
		PasswordInfo passwordInfo = new PasswordInfo();
		passwordInfo.setPassword(MD5.getMD5(mFirstPassword));
		passwordInfo.setQuestion("Hellllo");
		passwordInfo.setAnswer("Hi");
		passwordInfo.setFlag(0);
		PasswordDAO passwordDAO = new PasswordDAO(this);
		DBHelper dbhelper = DBHelper.getInstance(this);
		synchronized (dbhelper) {
			boolean isEmpty = passwordDAO.isEmpty();
			if(isEmpty){
				passwordDAO.insert(passwordInfo);
				MimetypesDAO mimetypesDAO = new MimetypesDAO(this);
				if(mimetypesDAO.isEmpty()){
					mimetypesDAO.initTable(); // 初始化Mimetypes表.想不到更好的地方初始化该表，如果放在DBHelper中，会造成对DBHelper的递归调用
				}
			}
			else{
				passwordDAO.update(passwordInfo);
			}
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_cancel:
			this.finish();
			break;
		case R.id.text_reset:
			mIsFirstInput = true;
			updateCodeList("");
			mTextTip.setText(getString(R.string.set_gesture_pattern));
			break;
		default:
			break;
		}
	}
	
	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}
	
}
