package com.alibaba.alisecuritycenter.aliprivacyspace.communication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.ContactsDAO;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.DataDAO;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.DataTable;
import com.alibaba.alisecuritycenter.aliprivacyspace.db.RawContactsDAO;
import com.alibaba.alisecuritycenter.memoryspeedup.common.CustomTitleBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class AddPrivateContactsActivity extends Activity implements
        OnItemClickListener, OnClickListener {
    private static final String TAG = "AddPrivateContactsActivity";
    private Context mContext;
    private ListView mListView;
    private Button mButtonCancleAll;
    private Button mButtonAdd;
    private TextView mTextViewCount;
    private String contactsCount;
    private List<ContactItem> list = new ArrayList<>();
    private ContactAdapter mAdapter;
    private ContactListGenerator listGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        mContext = this;
        String titleName = getString(R.string.add_private_contacts);
        new CustomTitleBar().getTitleBarWhole(this, titleName, true, false);
        setContentView(R.layout.privacyspace_add_private_contacts);
        initView();
        initValue();
        setButtonDisable();

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.add_private_contacts_list);
        mButtonCancleAll = (Button) findViewById(R.id.cancle_all);
        mButtonAdd = (Button) findViewById(R.id.add_contacts);
        mTextViewCount = (TextView) findViewById(R.id.txtcount);
        contactsCount = getResources().getString(R.string.contacts_count);
    }

    private void initValue() {
        listGenerator = new ContactListGenerator(mContext);
        list = listGenerator.generateContactList();
        mAdapter = new ContactAdapter(mContext, list);
        mListView.setAdapter(mAdapter);
        setContactsCount();
        mListView.setOnItemClickListener(this);
        mButtonAdd.setOnClickListener(this);
        mButtonCancleAll.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Log.d("AddPrivateContacts->onItemClick :", position + "");
        // 第二个参数可以取到Item的子view
        view.findViewById(R.id.cb_contact).performClick();
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_contact);
        // checkBox没有设置onClickListener，每个状态都在list中保存着
        ContactAdapter.isChecked.set(position, checkBox.isChecked());
        // add or remove ContactItem from the addList
        if (checkBox.isChecked()) {
            ContactAdapter.addList.add(list.get(position));
        } else {
            ContactAdapter.addList.remove((ContactItem) list.get(position));
        }
        // 计算选中个数
        setContactsCount();
        Log.d("isChecked :", checkBox.isChecked() + "");
        if (ContactAdapter.isChecked.contains(true)) {
            setButtonEnable();
        } else {
            setButtonDisable();
        }
    }

    /**
     * 计算共选中多少个联系人
     */
    private void setContactsCount() {
        // TODO Auto-generated method stub
        int contactCount = 0;
        for (int i = 0, count = ContactAdapter.isChecked.size(); i < count; i++) {
            if (ContactAdapter.isChecked.get(i) == true) {
                contactCount++;
            }
        }
        String strContactsCount = String.format(contactsCount, contactCount);
        mTextViewCount.setText(strContactsCount);
    }

    private void setButtonEnable() {
        mButtonCancleAll.setEnabled(true);
        mButtonAdd.setEnabled(true);
    }

    private void setButtonDisable() {
        mButtonCancleAll.setEnabled(false);
        mButtonAdd.setEnabled(false);
    }

    /**
     * 当点击撤销按钮时，要清空addList列表，以防在添加时出现错误
     */
    private void clearAddList() {
        // TODO Auto-generated method stub
        ContactAdapter.addList.clear();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.cancle_all:
            ContactAdapter.isChecked.clear();
            mAdapter.initCheckBoxList();
            // set All CheckBox not checked.
            mAdapter.notifyDataSetChanged();
            setButtonDisable();
            setContactsCount();
            // 情况addList内容
            clearAddList();
            break;
        case R.id.add_contacts:
            AddContacts();
            break;

        default:
            break;
        }

    }

    
    /**
     * there are many circumstances that we should think about.
     * <p>Please look up the flow chart about this Logic , which is named "添加联系人流程图"
     * <p>>>>>>>>>>>>>>>>>>>>>>>>>>>>USE AsyncTask TO DO IT.
     * <br>
     * 1. the specific contact has more than one phone number.
     * <p>
     * (1)we just add the name and phone number and other stuff to the
     * PrivacySpace DB.
     * <p>
     * (2)and then delete the corresponding raw in the contacts' data table .
     * 
     * <p>
     * <br>
     * 2. the specific contact has only one phone number.
     * <p>
     * (1)we add the name and phone number and stuff to the PS DB.
     * <p>
     * (2)and then delete all related information about this contacts , which
     * include data table , raw_contact table and contacts table.
     * 
     */
    private void AddContacts() {
        // TODO Auto-generated method stub
        Iterator<ContactItem> it = ContactAdapter.addList.iterator();
        Log.d("---------------", "-----------------");
        while (it.hasNext()) {
            int contactId = -1;
            int rawContactId = -1;
            ContactItem contactItem = it.next();
            ContactsDAO contactsDAO = new ContactsDAO(mContext);
            RawContactsDAO rawContactsDAO = new RawContactsDAO(mContext);
            // check if the contact exists.
            DataDAO dataDAO = new DataDAO(mContext);
            DataTable dt = dataDAO.findPSContactByName(contactItem.getName());
            if(dt == null ){    // PrivacySpace's DB has NOT stored this contact 
                // insert into PS' Contact table
                contactId = contactsDAO.insertContact(contactItem.getContactId());
                Log.i(TAG, ">>>>>>> insertContactId :" + contactId);
                // insert into PS' RawContact table
                rawContactId = rawContactsDAO.insertRawContact(contactItem.getRawContactId(),contactId);
                Log.i(TAG, ">>>>>>> insertRawContactId :" + rawContactId);
                // insert into PS' Data table (mimetype = name)
                int insertDataId = dataDAO.insertData(contactItem.getName(), contactItem.getRawContactId(), rawContactId);
                Log.i(TAG, ">>>>>>> insertDataId :" + insertDataId);
                
            } else{
                // find PS's rawContactId through Data table
                rawContactId = dataDAO.getRawContactIdByName(contactItem.getName());
                Log.i(TAG, ">>>>>>> PS has stored rawContactId : " + rawContactId);
                // find PS's contactId through RawContact table
                contactId = rawContactsDAO.getContactIdByRawContactId(rawContactId);
                Log.i(TAG, ">>>>>>> PS has stored contactId : " + contactId);
            }
            
            boolean hasMorePhoneNumber = dataDAO.hasMorePhoneNumber(contactItem.getRawContactId());
            if (hasMorePhoneNumber) {
                // JUST add phoneNumber into the PS' data table.
                dataDAO.insertPhoneIntoPS(contactItem.getPhone(), contactItem.getRawContactId(), rawContactId);
                // JUST delete phoneNumber from system's data table.
                dataDAO.deletePhoneFromSys(contactItem.getPhone(), contactItem.getRawContactId());
                
            }
            else{
                // add THIS specific contact's other info into the Data table
                dataDAO.insertOtherInfoIntoPS(contactItem.getRawContactId() , rawContactId);
                // delete specific contact's all info from System's table.
                rawContactsDAO.deleteAllInfo(contactItem.getRawContactId());
            }
        }
        finish();

    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        clearAddList();
    }

}
