package com.alibaba.alisecuritycenter.aliprivacyspace.communication;

import java.util.List;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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
	private RelativeLayout mRelativeLLProgress;
	private Button mButtonAddPrivateContact;
	private List<ContactItem> contactsList ;
	private ProgressBar mProgressBar;
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
		mRelativeLLProgress = (RelativeLayout) findViewById(R.id.rela_progress);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
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
	 * init list .
	 */
	private void initList() {
		// TODO Auto-generated method stub
		// get the number of the privacy contact
	    int totalNumber = 10;
	    if(totalNumber > 0){
	        mRelativeLLList.setVisibility(View.GONE);
	        mRelativeLLProgress.setVisibility(View.VISIBLE);
	        new InitListAsyncTask().execute(totalNumber);
		}
	    //mRelativeLLProgress.setVisibility(View.GONE);
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
    /**
     * 插入、删除、更新时，为了不阻塞UI进程。
     * AsyncTask 有三个参数
     * 根据先后顺序，分别用法为：
     * protected Third doInBackground(First... params);
     * 在DoInBackground中调用publishProgess时，会调用
     * protected void onProgressUpdate(Second... values);
     * 最后，执行完毕，DoInBackground返回第三个参数。在
     * protected void onPostExecute(Third result);
     * ----------------------------------------------------
     * 参数1:向后台任务执行过程方法传递参数的类型
                  参数2:在后台任务执行过程中，要求主UI线程处理中间状态，通常是一些UI处理中传递的参数类型
                  参数3:后台任务执行完返回时的参数类型
     * @author linkun.lk
     *
     */
    private class InitListAsyncTask extends AsyncTask<Integer, Integer, Void> {
        int duration = 0;
        int current = 0;

        @Override
        protected Void doInBackground(Integer... params) {
            do {
                Log.d("handleMessage", "id:" + Thread.currentThread().getId()
                        + " name:" + Thread.currentThread().getName());
                current += 10;
                try {
                 // 这里的参数类型是 AsyncTask<Void,Integer,Void>中的Integer决定的，在onProgressUpdate中可以得到这个值去更新UI主线程，这里是异步线程
                    publishProgress(current); 
                    Thread.sleep(1000);
                    if (mProgressBar.getProgress() >= 100) {
                        break;
                    }
                } catch (Exception e) {
                }
            } while (mProgressBar.getProgress() <= 100);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println(values[0]);
            mProgressBar.setProgress(values[0]);
            if(values[0] > 90){
                mProgressBar.setVisibility(View.GONE);
            }
            Log.d("updateThread", "id:" + Thread.currentThread().getId()
                    + " name:" + Thread.currentThread().getName());// 这里是UI主线程
        }
    }
}
