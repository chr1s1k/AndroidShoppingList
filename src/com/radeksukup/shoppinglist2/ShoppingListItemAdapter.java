/**
 * 
 */
package com.radeksukup.shoppinglist2;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author sukuprad
 *
 */
public class ShoppingListItemAdapter extends ArrayAdapter<ShoppingListItem> {
	
	private ArrayList<ShoppingListItem> items;
	private Context context;

	public ShoppingListItemAdapter(Context context, int textViewResourceId, ArrayList<ShoppingListItem> items) {
		super(context, textViewResourceId, items);
		this.items = items;
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
			item = mInflater.inflate(R.layout.shopping_list_item, null);
		} else {
			item = convertView;
		}
		
		TextView tv = (TextView) item.findViewById(android.R.id.text1);
		tv.setText(getItem(position).toString());
		
		ShoppingListItem currentItem = items.get(position);
		if (currentItem.isDisabled()) {
			item.setAlpha((float) 0.3);
		}
		
		return item;
	}
	

}
