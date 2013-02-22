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
 * @author sukuprad
 *
 */
public class ProductsListItemAdapter extends ArrayAdapter<Product> {
	
	private List<Product> products;
	private Context context;
	
	public ProductsListItemAdapter(Context context, int textViewResourceId, List<Product> products) {
		super(context, textViewResourceId, products);
		this.context = context;
		this.products = products;
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
			item = mInflater.inflate(R.layout.products_list_item, null);
		} else {
			item = convertView;
		}

		TextView tv1 = (TextView) item.findViewById(android.R.id.text1);
		TextView tv2 = (TextView) item.findViewById(android.R.id.text2);

		tv1.setText(getItem(position).toString()); // set product title
		tv2.setText(String.valueOf(getItem(position).getId())); // set product id
		
		return item;
	}

}