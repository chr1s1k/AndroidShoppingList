/**
 * 
 */
package com.radeksukup.shoppinglist2;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

	    return 1000; // bad workaround
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
		
		ShoppingList sl = (ShoppingList) context.getApplicationContext();

		TextView tv1 = (TextView) item.findViewById(android.R.id.text1);
		TextView tv2 = (TextView) item.findViewById(android.R.id.text2);
		ImageView icon = (ImageView) item.findViewById(android.R.id.icon);

		tv1.setText(getItem(position).toString());
		tv2.setText(getItem(position).getQuantityAndType());

		if (sl.isLocked()) {
			icon.setImageResource(R.drawable.check_mark_gray); // display gray check mark
		}
		
		ShoppingListItem currentItem = items.get(position);
		if (currentItem.isDisabled()) {
			tv1.setPaintFlags(tv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // strike through text of title
			tv2.setPaintFlags(tv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // strike through quantity and quantity type
			tv1.setTextColor(context.getResources().getColor(R.color.light_gray));
			tv2.setTextColor(context.getResources().getColor(R.color.light_gray));
			icon.setImageResource(R.drawable.check_mark_black); // display black check mark
		}
		
		return item;
	}
	

}
