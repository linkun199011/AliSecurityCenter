package com.alibaba.alisecuritycenter.aliprivacyspace.communication;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.alisecuritycenter.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PrivacyContactListAdapter extends BaseAdapter{
    private Context mContext;
    private List<ContactItem> mList = new ArrayList<>();
    public static boolean isShown = false;
    private RelativeLayout mRelaSmsCall;
    private RelativeLayout mFooterButtons;
    private Button mAddMore;
    // 这个list用来记录那些item是被点击选中的。长度和上面的mList一样。
    public static List<Boolean> isChecked = new ArrayList<Boolean>();
    // 该list是用来保存被选中的，将要被删除的联系人合集
    public static List<ContactItem> delList = new ArrayList<>();
    
    public void setAddMoreButton(Button addMore){
        mAddMore = addMore;
    }
    public void setFooterButtons(RelativeLayout footerButtons){
        mFooterButtons = footerButtons;
    }
    public PrivacyContactListAdapter(){}
    public PrivacyContactListAdapter(Context context , List<ContactItem> list){
        mContext = context;
        mList = list;
        isShown = false;
        // init checkBox's state
        isChecked.clear();
        initCheckBoxList();
    }
    public void initCheckBoxList(){
        for(int i = 0 , count = getCount(); i < count ; i++){
            isChecked.add(false);
        }
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
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
        final ContactItem item = mList.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.privacyspace_communication_list_item, null); 
            ImageView headImg = (ImageView) convertView.findViewById(R.id.headImg);
            headImg.setVisibility(View.GONE);
            
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.contact_name);
            holder.number = (TextView) convertView.findViewById(R.id.contact_number);
            holder.sms = (ImageView) convertView.findViewById(R.id.security_message);
            holder.call = (ImageView) convertView.findViewById(R.id.security_call);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_contact);
            
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.name.setText(item.getName());
        holder.number.setText(item.getPhone());
        holder.sms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, item.getName() + " sms is pressed!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.call.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, item.getName() + " call is pressed!", Toast.LENGTH_SHORT).show();
            }
        });
        
        mRelaSmsCall = (RelativeLayout) convertView.findViewById(R.id.relative_message_call);
        
        //convertView.setOnLongClickListener(new Onlongclick());
        if(isShown){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(isChecked.get(position));
            holder.checkBox.setClickable(false);
            mRelaSmsCall.setVisibility(View.GONE);
            mAddMore.setVisibility(View.GONE);
            mFooterButtons.setVisibility(View.VISIBLE);
        }
        
        return convertView;
    }
    
    public static class ViewHolder{
        TextView name;
        TextView number;
        ImageView sms;
        ImageView call;
        CheckBox checkBox;
    }
    
    /*class Onlongclick implements OnLongClickListener{

        public boolean onLongClick(View v) {
            // TODO Auto-generated method stub
            notifyDataSetInvalidated(); // ????
            isShown = true;
            return true;
        }
    }*/

}
