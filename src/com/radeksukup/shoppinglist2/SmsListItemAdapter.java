/**
 * 
 */
package com.radeksukup.shoppinglist2;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Radek Sukup
 *
 */
public class SmsListItemAdapter extends ArrayAdapter<Sms> {
	
	private List<Sms> smsList;
	private Context context;
	
	public SmsListItemAdapter(Context context, int textViewResourceId, List<Sms> smsList) {
		super(context, textViewResourceId, smsList);
		this.smsList = smsList;
		this.context = context;
	}
	
	@Override
	public int getViewTypeCount() {
	    return getCount();
	}

	@Override
	public int getItemViewType(int position) {
	    return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View item;

		if (null == convertView) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			item = mInflater.inflate(R.layout.sms_list_item, null);
		} else {
			item = convertView;
		}

		TextView sender = (TextView) item.findViewById(android.R.id.title);
		TextView smsBody = (TextView) item.findViewById(android.R.id.text1);
		TextView date = (TextView) item.findViewById(android.R.id.text2);

		sender.setText(getItem(position).getSender()); // set sender
		smsBody.setText(getItem(position).toString()); // set sms content
		date.setText(String.valueOf(getItem(position).getDate())); // set date
		
		return item;
	}

}
