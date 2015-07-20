package com.alibaba.alisecuritycenter.aliprivacyspace.communication;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.alisecuritycenter.R;
import com.alibaba.alisecuritycenter.aliprivacyspace.PrivacySpaceListItem;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter{
	private Context mContext;
	// adpter 使用的list
	private List<ContactItem> list = new ArrayList<>();
	// 这个list用来记录那些item是被点击选中的。长度和上面的list一样。
	public static List<Boolean> isChecked = new ArrayList<Boolean>();
	// 该list是用来保存被选中的，将要被添加为私密联系人的联系人合集
	public static List<ContactItem> addList = new ArrayList<>();
	
	private RelativeLayout mRelativeLayout;
	
	public interface CheckBoxCallback{
		public void click(View v);
	}
	
	public ContactAdapter(Context context , List<ContactItem> list ){
		this.mContext = context;
		this.list = list;
		//init checkbox's state
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
		return list.size();
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
		ContactItem item = list.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.privacyspace_communication_list_item, null); 
			mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_message_call);
			mRelativeLayout.setVisibility(View.GONE);
			
			holder = new ViewHolder();
			holder.imageHead = (ImageView) convertView.findViewById(R.id.headImg);
			holder.name = (TextView) convertView.findViewById(R.id.contact_name);
			holder.number = (TextView) convertView.findViewById(R.id.contact_number);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_contact);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(item.getHead() == null){
			item.setHead(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.head));
		}
		holder.imageHead.setImageBitmap(item.getHead());
		holder.name.setText(item.getName());
		holder.number.setText(item.getPhone());
		holder.checkBox.setVisibility(View.VISIBLE);
		
		holder.checkBox.setChecked(isChecked.get(position));
		holder.checkBox.setClickable(false);
		//holder.checkBox.setOnClickListener(this);

		return convertView;
	}
	public static class ViewHolder{
		ImageView imageHead;
		TextView name;
		TextView number;
		CheckBox checkBox;
	}


}
