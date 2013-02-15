package com.radeksukup.shoppinglist2;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
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
		
		if (sl.hasItems()) {
			findViewById(R.id.showCategoriesButton).setVisibility(View.GONE);
			findViewById(R.id.addNextButton).setVisibility(View.VISIBLE);
			findViewById(R.id.lockCurrentListButton).setVisibility(View.VISIBLE);
			if (sl.isLocked()) {
				findViewById(R.id.clearCurrentListButton).setVisibility(View.VISIBLE);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void showCategories (View view) {
		Intent intent = new Intent(this, ShowCategoriesActivity.class);
		startActivity(intent);
	}
	
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
	
	public void renderShoppingList(ShoppingList sl) {
		List<ShoppingListItem> items = sl.getItems();
		ListView shoppingList = (ListView) findViewById(R.id.shoppingList);
		ArrayAdapter<ShoppingListItem> adapter = new ArrayAdapter<ShoppingListItem>(this, android.R.layout.simple_list_item_1, items);
		
		shoppingList.setAdapter(adapter);
		shoppingList.setOnItemClickListener(shoppingListItemClickListener(sl)); // set on item click listener
	}

	private OnItemClickListener shoppingListItemClickListener(final ShoppingList sl) {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ShoppingListItem item = sl.getItemByIndex(position); // get selected item
				
				if (sl.isLocked()) { // shopping list is locked
					view.setEnabled(false); // disable selected item in view
					
					if (!item.isDisabled()) {
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

}
