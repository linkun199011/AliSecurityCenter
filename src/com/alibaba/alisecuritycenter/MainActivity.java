package com.alibaba.alisecuritycenter;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.aliprivacyspace.GestureEditActivity;
import com.alibaba.alisecuritycenter.aliprivacyspace.GestureVerifyActivity;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.PasswordDAO;
import com.alibaba.alisecuritycenter.memoryspeedup.MemorySpeedUpActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private TextView mTVSecurityCenter;
	private Button mBtnMemorySpeedUp;
	private Button mBtnPrivacySpace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.securitycenter_main_activity);
		initView();
		initWordingSize();
	}
	// ���������С������ϵͳ����ı仯���仯
	private void initWordingSize() {
		// TODO Auto-generated method stub
		if(mTVSecurityCenter != null){
			mTVSecurityCenter.setTextSize(50 / getResources().getConfiguration().fontScale);
		}
		if(mBtnMemorySpeedUp != null){
			mBtnMemorySpeedUp.setTextSize(17 / getResources().getConfiguration().fontScale);
		}
		if(mBtnPrivacySpace  != null){
			mBtnPrivacySpace .setTextSize(17 / getResources().getConfiguration().fontScale);
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		mTVSecurityCenter = (TextView) findViewById(R.id.tv_security_center);
		mBtnMemorySpeedUp = (Button) findViewById(R.id.tv_memoryspeedup);
		mBtnMemorySpeedUp.setOnClickListener(this);
		mBtnPrivacySpace = (Button) findViewById(R.id.tv_privacyspace);
		mBtnPrivacySpace.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_memoryspeedup:
			Intent intent = new Intent(MainActivity.this, MemorySpeedUpActivity.class);
			startActivity(intent);
			//finish();
			break;
		case R.id.tv_privacyspace:
			enterPrivacySpace();
			break;

		default:
			break;
		}
	}
	/**
	 * ����PrivacySpace�������Ƿ������������ж���ת��Activity
	 * <p>1.�����Ϊ�գ�������������->�ٽ�����˽�ռ�
	 * <p>2.�����λ�գ�����֤����->�ٽ�����˽�ռ�
	 */
	private void enterPrivacySpace() {
		PasswordDAO passwordDAO = new PasswordDAO(this);
		if(passwordDAO.isEmpty()){
			Intent intent = new Intent(MainActivity.this, GestureEditActivity.class);
			startActivity(intent);
		}
		else{
			Intent intent = new Intent(MainActivity.this, GestureVerifyActivity.class);
			startActivity(intent);
		}
	}

}
