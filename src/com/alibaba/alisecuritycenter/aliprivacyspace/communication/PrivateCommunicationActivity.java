package com.alibaba.alisecuritycenter.aliprivacyspace.communication;

import java.util.List;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 
 * @author linkun.lk
 *
 */
public class PrivateCommunicationActivity extends Activity{
    private static final String TAG = "PrivateCommunicationActivity";
	private ListView mListView;
	private RelativeLayout mRelativeLLEmpty;
	private RelativeLayout mRelativeLLList;
	private Button mButtonAddPrivateContact;
	private List<ContactItem> contactsList ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String titleName = getString(R.string.private_communicate);
		new CustomTitleBar().getTitleBarWhole(this,titleName,true,false);
		setContentView(R.layout.privacyspace_communication_activity);
		Log.i(TAG, "onCreate");
		//
		initView();
	}

	private void initView() {
		mButtonAddPrivateContact = (Button) findViewById(R.id.btn_add_private_contact);
		mListView = (ListView) findViewById(R.id.contact_list);
		mRelativeLLEmpty = (RelativeLayout) findViewById(R.id.empty_page);
		mRelativeLLList = (RelativeLayout) findViewById(R.id.list_page);
		// look up db.
		contactsList = null;
		if (contactsList == null){
			mRelativeLLEmpty.setVisibility(View.VISIBLE);
			mRelativeLLList.setVisibility(View.GONE);
		}
		mButtonAddPrivateContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PrivateCommunicationActivity.this , AddPrivateContactsActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * init list 
	 */
	private void initList() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * update PrivateContacts list here.
	 * <p>Two ways to show this activity:
	 * <p>1. onCreate() -> onStart() -> onResume
	 * <p>2. onPause() -> onStop() -> onRestart() -> onStart() -> onResume()
	 * <p>The first one is to create instance of this Activity ,
	 * The second one is to bring the instance to the top of the task.
	 * Both of them should execute onStart().The Updating thing can put it in here.
	 * <p>should we get list in this function?
	 * what about after account sync ,and we could not get list in this situation.
	 */
	@Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // should i use AsyncTask to solve the ANR problem because of the 
        // long-time execution of adding privacy contact? 
        initList();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
	
}
