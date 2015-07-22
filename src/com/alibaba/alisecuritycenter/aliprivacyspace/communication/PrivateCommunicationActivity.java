package com.alibaba.alisecuritycenter.aliprivacyspace.communication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.DataDAO;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.DataTable;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.RawContactsDAO;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.RawContactsTable;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;

import android.app.Activity;
import android.content.Context;
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
 */
public class PrivateCommunicationActivity extends Activity {
    private static final String TAG = "PrivateCommunicationActivity";
    private Context mContext;
    private ListView mListView;
    private RelativeLayout mRelativeLLEmpty;
    private RelativeLayout mRelativeLLList;
    private RelativeLayout mRelativeLLProgress;
    private Button mButtonAddPrivateContact;
    private List<ContactItem> mContactList = new ArrayList<>();
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String titleName = getString(R.string.private_communicate);
        new CustomTitleBar().getTitleBarWhole(this, titleName, true, false);
        setContentView(R.layout.privacyspace_communication_activity);
        Log.i(TAG, "onCreate");
        //
        initView();
    }

    private void initView() {
        mContext = this;
        mButtonAddPrivateContact = (Button) findViewById(R.id.btn_add_private_contact);
        mListView = (ListView) findViewById(R.id.contact_list);
        mRelativeLLEmpty = (RelativeLayout) findViewById(R.id.empty_page);
        mRelativeLLList = (RelativeLayout) findViewById(R.id.list_page);
        mRelativeLLProgress = (RelativeLayout) findViewById(R.id.rela_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        // look up db.
        //mContactList = null;
        if (mContactList.size() == 0) {
            mRelativeLLEmpty.setVisibility(View.VISIBLE);
            mRelativeLLList.setVisibility(View.GONE);
        }
        mButtonAddPrivateContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PrivateCommunicationActivity.this,
                        AddPrivateContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * init list .
     */
    private void initList() {
        // get the number of the privacy contact
        int totalNumber = 0;
        RawContactsDAO rcd = new RawContactsDAO(mContext);
        totalNumber = rcd.getAllContactsCount();
        Log.i(TAG, "totalNumber of PS RawContacts :" + totalNumber);
        if (totalNumber > 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setMax(totalNumber);
            mRelativeLLList.setVisibility(View.GONE);
            mRelativeLLProgress.setVisibility(View.VISIBLE);
            // mRelativeLLEmpty.setVisibility(View.GONE);
            new InitListAsyncTask().execute(totalNumber);
        }
        // mListView.setAdapter(adapter);
        // mRelativeLLProgress.setVisibility(View.GONE);
    }

    /**
     * update PrivateContacts list here.
     * <p>
     * Two ways to show this activity:
     * <p>
     * 1. onCreate() -> onStart() -> onResume
     * <p>
     * 2. onPause() -> onStop() -> onRestart() -> onStart() -> onResume()
     * <p>
     * The first one is to create instance of this Activity , The second one is
     * to bring the instance to the top of the task. Both of them should execute
     * onStart().The Updating thing can put it in here.
     * <p>
     * should we get list in this function? what about after account sync ,and
     * we could not get list in this situation.
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
     * 查询数据库。 插入、删除、更新时，为了不阻塞UI进程。 AsyncTask 有三个参数 根据先后顺序，分别用法为： protected Third
     * doInBackground(First... params); 在DoInBackground中调用publishProgess时，会调用
     * protected void onProgressUpdate(Second... values);
     * 最后，执行完毕，DoInBackground返回第三个参数。在 protected void onPostExecute(Third
     * result);
     * <p>
     * ----------------------------------------------------
     * <p>
     * 参数1:向后台任务执行过程方法传递参数的类型
     * <p>
     * 参数2:在后台任务执行过程中，要求主UI线程处理中间状态，通常是一些UI处理中传递的参数类型
     * <p>
     * 参数3:后台任务执行完返回时的参数类型
     * 
     * @author linkun.lk
     * 
     */
    private class InitListAsyncTask extends AsyncTask<Integer, Integer, Void> {
        // int duration = 0;
        int current = 1;
        int totalNumber = 0;
        ContactItem contactItem = null;
        @Override
        protected Void doInBackground(Integer... params) {
            totalNumber = params[0];
            Log.d("handleMessage", "id:" + Thread.currentThread().getId()
                    + " name:" + Thread.currentThread().getName());
            try {
                RawContactsDAO rcd = new RawContactsDAO(mContext);
                List<RawContactsTable> list = rcd.getAllContacts();
                Iterator<RawContactsTable> it = list.iterator();
                RawContactsTable rct = null;
                DataDAO dataDAO = new DataDAO(mContext);
                DataTable dt = null;
                while (it.hasNext()) {
                    rct = it.next();
                    // get phone list
                    List<DataTable> phoneList = dataDAO.getPhoneList(rct.get_id());
                    //System.out.println("phoneList : "+phoneList.size() + " Raw_contact_id: " +rct.get_id() );
                    Iterator<DataTable> dataIterator = phoneList.iterator();
                    while(dataIterator.hasNext()){
                        dt = dataIterator.next();
                        contactItem = new ContactItem();
                        contactItem.setContactId(rct.getContact_id());
                        contactItem.setRawContactId(rct.get_id());
                        contactItem.setName(rct.getDisplay_name());
                        contactItem.setPhone(dt.getData1());
                        Log.i(TAG, "dataTable phone :" + dt.getData1());
                        // add into Adapter's list
                        mContactList.add(contactItem);
                    }
                    publishProgress(current++);
                    Log.i(TAG,
                            "RawContactTable :" + rct.get_id()
                                    + rct.getDisplay_name()
                                    + rct.getContact_id());
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Iterator<ContactItem> it = mContactList.iterator();
            while(it.hasNext()){
                ContactItem ci = it.next();
                System.out.println("ContactItem : name = " + ci.getName() + " phone = "+ci.getPhone());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println(values[0]);
            mProgressBar.setProgress(values[0]);
            if (values[0] >= totalNumber) {
                mProgressBar.setVisibility(View.GONE);
            }
            Log.d("updateThread", "id:" + Thread.currentThread().getId()
                    + " name:" + Thread.currentThread().getName());// 这里是UI主线程
        }
    }
}
