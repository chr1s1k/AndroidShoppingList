package com.radeksukup.shoppinglist2;

import java.text.Normalizer;
import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ShoppingList sl = (ShoppingList) getApplication();
		int disabledItems = 0;
		boolean locked = false;
		ArrayList<ShoppingListItem> items = null;
		
		if (savedInstanceState != null) {
			disabledItems = savedInstanceState.getInt("disabled-items"); // get disabled items
			locked = savedInstanceState.getBoolean("locked"); // get status of created shopping list
			items = savedInstanceState.getParcelableArrayList("shopping-list-items"); // get previously added items
			
			sl.disabledItems = disabledItems;
			sl.setLocked(locked);
		}
		
		if (items != null) {
			sl.setItems(items);
		}

		renderMainScreen();
	}

	@Override
	protected void onResume() {
		super.onResume();
		renderMainScreen();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		ShoppingList sl = (ShoppingList) getApplication();
		
		outState.putInt("disabled-items", sl.disabledItems);
		outState.putBoolean("locked", sl.isLocked());
		outState.putParcelableArrayList("shopping-list-items", sl.getItems());
		
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
		 super.onConfigurationChanged(newConfig);
	 }
	
	/*
	 * Starts an activity which contains list of categories.
	 */
	public void showCategories (View view) {
		Intent intent = new Intent(this, ShowCategoriesActivity.class);
		startActivity(intent);
	}
	
	/*
	 * Locks current shopping list a modifies layout of main activity.
	 */
	public void lockCurrentList (View view) {
		view.setVisibility(View.GONE);
		findViewById(R.id.addNextButton).setVisibility(View.GONE);
		findViewById(R.id.clearCurrentListButton).setVisibility(View.VISIBLE);
		ShoppingList sl = (ShoppingList) getApplication();
		sl.lock();
	}
	
	public void clearCurrentList (View view) {
		DialogFragment confirmDialog = new ConfirmDialog();
		confirmDialog.show(getFragmentManager(), "confirmDialog");
	}
	
	/*
	 * Opens built-in SMS application with pre-filled SMS body.
	 */
	public void sendSms(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		ShoppingList sl = (ShoppingList) getApplication();
		ArrayList<ShoppingListItem> items = sl.getItems();
		String smsBody = "";
	
		for (int i = 0; i < items.size(); i++) {
			smsBody += items.get(i).getTitle() + " " + Double.toString(items.get(i).getQuantity()) + " " + items.get(i).getQuantityType();
			if (i + 1 != items.size()) {
				smsBody += ",\n";
			} else {
				smsBody += ".";
			}
		}
		
		// replace diacritics characters
		smsBody = Normalizer.normalize(smsBody, Normalizer.Form.NFD);
		smsBody = smsBody.replaceAll("[^\\p{ASCII}]", "");
		
		intent.setData(Uri.parse("sms:"));
		intent.putExtra("sms_body", smsBody);
		startActivity(intent);
	}
	
	/*
	 * Populates shopping list view.
	 */
	public void renderShoppingList(ShoppingList sl) {
		ArrayList<ShoppingListItem> items = sl.getItems();
		ListView shoppingList = (ListView) findViewById(R.id.shoppingList);
		
		// set custom adapter for shopping list view
		ArrayAdapter<ShoppingListItem> adapter = new ShoppingListItemAdapter(this, R.layout.shopping_list_item, items);
		
		shoppingList.setAdapter(adapter);
		shoppingList.setOnItemClickListener(shoppingListItemClickListener(sl, adapter)); // set on item click listener
	}

	private OnItemClickListener shoppingListItemClickListener(final ShoppingList sl, final ArrayAdapter<ShoppingListItem> adapter) {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ShoppingListItem item = sl.getItemByIndex(position); // get selected item

				if (sl.isLocked()) { // shopping list is locked
					
					if (!item.isDisabled()) {
						view.setAlpha((float) 0.3);
						item.setDisabled(); // disable selected item in model
						sl.disabledItems++; // increase count of disabled items
					}
					
					if (sl.hasAllItemsDisabled()) { // if all items are disabled show confirm dialog
						DialogFragment confirmDialog = new ConfirmDialog();
						confirmDialog.show(getFragmentManager(), "confirmDialog");
					}
				} else { // shopping list is unlocked => show update form dialog
					DialogFragment formDialog = new FormDialog();
					int productId = item.getId();
					String dialogTitle = item.getTitle();
					
					((FormDialog) formDialog).setTitle(dialogTitle); // set dialog title
					((FormDialog) formDialog).setProductId(productId); // set selected product id
					formDialog.show(getFragmentManager(), "updateFormDialog");
				}
			}
		};
	}
	
	/*
	 * Renders layout of main activity according to current state of shopping list.
	 */
	public void renderMainScreen() {
		ShoppingList sl = (ShoppingList) getApplication();
		
		if (sl.hasItems()) {
			findViewById(R.id.sendSmsButton).setVisibility(View.VISIBLE);
			findViewById(R.id.showCategoriesButton).setVisibility(View.GONE);
			findViewById(R.id.addNextButton).setVisibility(View.VISIBLE);
			findViewById(R.id.lockCurrentListButton).setVisibility(View.VISIBLE);
			if (sl.isLocked()) {
				findViewById(R.id.clearCurrentListButton).setVisibility(View.VISIBLE);
				findViewById(R.id.addNextButton).setVisibility(View.GONE);
				findViewById(R.id.lockCurrentListButton).setVisibility(View.GONE);
			} else {
				findViewById(R.id.clearCurrentListButton).setVisibility(View.GONE);
			}
			renderShoppingList(sl);
		} else {
			findViewById(R.id.sendSmsButton).setVisibility(View.GONE);
			findViewById(R.id.clearCurrentListButton).setVisibility(View.GONE);
			findViewById(R.id.lockCurrentListButton).setVisibility(View.GONE);
			findViewById(R.id.addNextButton).setVisibility(View.GONE);
		}
	}
}
