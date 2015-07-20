package com.alibaba.alisecuritycenter.memoryspeedup;

import java.util.ArrayList;
import com.alibaba.alisecuritycenter.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemorySpeedUpAdapter extends BaseAdapter {
	private final String TAG = "MemorySpeedUpAdapter";
	private Context context;
	private int[] number = {0,0,0,0};
	private final int LIST_ITEM_NUMBER = 4;
	public static ArrayList<MemorySpeedUpListItem> msuliList = new ArrayList<MemorySpeedUpListItem>();
	public static ArrayList<MemorySpeedUpListItem> msuliList1 = new ArrayList<MemorySpeedUpListItem>();

	public MemorySpeedUpAdapter(Context context , int[] number) {
		super();
		msuliList.clear();
		this.context = context;
		this.number = number;
		MemorySpeedUpListItem tmp0 = new MemorySpeedUpListItem();
		String strTitle0 = context.getString(R.string.runningProgress);
		String strDesc0 = context.getString(R.string.tv_gray01);
		int strNumber0 = number[0];
		tmp0.setmTitle(strTitle0);
		tmp0.setmDescription(strDesc0);
		tmp0.setmNumber(strNumber0+"");
		msuliList.add(tmp0);
		//Log.d(TAG, "获得第"+position+"个位置");
		//Log.d(TAG, ">>>>>> number[0] = "+strNumber0+"");
		
		MemorySpeedUpListItem tmp1 = new MemorySpeedUpListItem();
		String strTitle1 = context.getString(R.string.whiteList);
		String strDesc1 = context.getString(R.string.tv_gray02);
		int strNumber1 = number[1];
		tmp1.setmTitle(strTitle1);
		tmp1.setmDescription(strDesc1);
		tmp1.setmNumber(strNumber1+"");
		msuliList.add(tmp1);
		
		MemorySpeedUpListItem tmp2 = new MemorySpeedUpListItem();
		String strTitle2 = context.getString(R.string.autoRun);
		String strDesc2 = context.getString(R.string.tv_gray03);
		int strNumber2 = number[2];
		tmp2.setmTitle(strTitle2);
		tmp2.setmDescription(strDesc2);
		tmp2.setmNumber(strNumber2+"");
		msuliList.add(tmp2);
		
		MemorySpeedUpListItem tmp3 = new MemorySpeedUpListItem();
		String strTitle3 = context.getString(R.string.runningService);
		String strDesc3 = context.getString(R.string.tv_gray04);
		int strNumber3 = number[3];
		tmp3.setmTitle(strTitle3);
		tmp3.setmDescription(strDesc3);
		tmp3.setmNumber(strNumber3+"");
		msuliList.add(tmp3);
		
		msuliList1.removeAll(msuliList1);
		msuliList1.addAll(msuliList);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return LIST_ITEM_NUMBER;
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
		
		//System.out.println("msuliList1 = >>>>>>>>>" + msuliList1.get(0).getmNumber());
		MemorySpeedUpListItem msuli = msuliList1.get(position);
		ViewHolder holder;
		if (convertView == null) {
			//Log.i(TAG,"new ViewHolder()"); 
			convertView =  View.inflate(context, R.layout.memoryspeedup_main_listview_item, null);
			holder = new ViewHolder();
			holder.tv_black = (TextView) convertView.findViewById(R.id.tv_black01);
			holder.tv_gray = (TextView) convertView.findViewById(R.id.tv_gray01);
			holder.tv_number = (TextView)convertView.findViewById(R.id.tv_number01);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_black.setText(msuli.getmTitle());
		holder.tv_gray.setText(msuli.getmDescription());
		holder.tv_number.setText(msuli.getmNumber());
		notifyDataSetChanged();
		return convertView;
	}
	static class ViewHolder {
		TextView tv_black;
		TextView tv_gray;
		TextView tv_number;
		}

}
