package com.radeksukup.shoppinglist2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	private final String FILENAME = "filename";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ShoppingList sl = (ShoppingList) getApplication();

//		int disabledItems = 0;
//		boolean locked = false;
//		ArrayList<ShoppingListItem> items = null;
//		
//		if (savedInstanceState != null) {
//			disabledItems = savedInstanceState.getInt("disabled-items"); // get disabled items
//			locked = savedInstanceState.getBoolean("locked"); // get status of created shopping list
//			items = savedInstanceState.getParcelableArrayList("shopping-list-items"); // get previously added items
//			
//			sl.disabledItems = disabledItems;
//			sl.setLocked(locked);
//		}
//		
//		if (items != null) {
//			sl.setItems(items);
//		}
		
		try {
			FileInputStream fis = openFileInput(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			sl = (ShoppingList) (getApplication()); ois.readObject();
			ois.close();
			fis.close();
			System.out.println("Polozek nacteno: " + String.valueOf(sl.getItems().size()));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (StreamCorruptedException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		renderMainScreen();

	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		if (intent.getStringExtra(ReadSmsActivity.IMPORT_TOAST_MESSAGE) != null) {
			Toast.makeText(getApplicationContext(), intent.getStringExtra(ReadSmsActivity.IMPORT_TOAST_MESSAGE), Toast.LENGTH_SHORT).show();
			intent.removeExtra(ReadSmsActivity.IMPORT_TOAST_MESSAGE); // clear intent
			intent.setAction("");
		}
		renderMainScreen();
	}
	
	protected void onPause() {
		super.onPause();
		
		EditText hiddenText = (EditText) findViewById(R.id.hiddenText);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromInputMethod(hiddenText.getWindowToken(), 0); // hide soft keyboard when activity is paused
		
		ShoppingList sl = (ShoppingList) getApplication();
		
		// save current items into file
		if (sl.getItems().size() != 0) {
			if(!saveItemsToFile(sl, FILENAME)) {
				String[] toastMessages = getResources().getStringArray(R.array.toast_messages);
				Toast.makeText(getApplicationContext(), toastMessages[12], Toast.LENGTH_SHORT).show();
			} else {
				System.out.println("Polozek ulozeno: " + String.valueOf(sl.getItems().size()));
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		ShoppingList sl = (ShoppingList) getApplication();
//		
//		outState.putInt("disabled-items", sl.disabledItems);
//		outState.putBoolean("locked", sl.isLocked());
//		outState.putParcelableArrayList("shopping-list-items", sl.getItems());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
			case R.id.about:
				intent = new Intent(this, AboutApplicationActivity.class);
				startActivity(intent);
				return true;
				
			case R.id.read_from_sms:
				intent = new Intent(this, ReadSmsActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
				return true;
				
			case R.id.send_as_sms:
				ShoppingList sl = (ShoppingList) getApplication();
				if (sl.hasItems()) { // list has at least one item
					sendSms(null);
				} else { // no items in shopping list
					String[] toastMessages = getResources().getStringArray(R.array.toast_messages);
					Toast.makeText(this, toastMessages[10], Toast.LENGTH_SHORT).show();
				}
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
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
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	/*
	 * Locks current shopping list a modifies layout of main activity.
	 */
	public void lockCurrentList (View view) {
		view.setVisibility(View.GONE);
		findViewById(R.id.sendSmsButton).setVisibility(View.GONE);
		findViewById(R.id.readSmsButton).setVisibility(View.GONE);
		findViewById(R.id.addNextButton).setVisibility(View.GONE);
		findViewById(R.id.addCustomButton).setVisibility(View.GONE);
		findViewById(R.id.clearCurrentListButton).setVisibility(View.VISIBLE);
		ShoppingList sl = (ShoppingList) getApplication();
		sl.lock();
	}
	
	public void clearCurrentList (View view) {
		DialogFragment confirmDialog = new ConfirmDialog();
		confirmDialog.show(getSupportFragmentManager(), "confirmDialog");
	}
	
	/*
	 * Opens built-in SMS application with pre-filled SMS body.
	 */
	public void sendSms(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		ShoppingList sl = (ShoppingList) getApplication();
		ArrayList<ShoppingListItem> items = sl.getItems();
		String smsBody = "";
		double quantity;
		String castedQuantity;
	
		for (int i = 0; i < items.size(); i++) {
			quantity = items.get(i).getQuantity();
			castedQuantity = Double.toString(quantity);
			
			if (quantity % 1.0 == 0) { // if quantity has no decimal part => casts it to integer
				castedQuantity = String.valueOf((int) items.get(i).getQuantity());
			}
			
			smsBody += items.get(i).getTitle() + " " + castedQuantity + " " + items.get(i).getQuantityType();
			if (i + 1 != items.size()) {
				smsBody += ",\n";
			} else {
				smsBody += ".";
			}
		}
		
		// replace diacritics characters
		smsBody = Utils.removeDiacritics(smsBody);
		
		intent.setData(Uri.parse("sms:"));
		intent.putExtra("sms_body", smsBody);
		startActivity(intent);
	}
	
	public void readSms(View view) {
		Intent intent = new Intent(this, ReadSmsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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
		shoppingList.setOnItemClickListener(shoppingListItemClickListener(sl, adapter)); // set on item click listener - for editing and disabling/enabling items
		shoppingList.setOnItemLongClickListener(shoppingListLongClickListener(sl, adapter)); // set on item long click listener - for deleting items
	}

	private OnItemClickListener shoppingListItemClickListener(final ShoppingList sl, final ArrayAdapter<ShoppingListItem> adapter) {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ShoppingListItem item = sl.getItemByIndex(position); // get selected item

				if (sl.isLocked()) { // shopping list is locked
					
					TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
					TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
					ImageView icon = (ImageView) view.findViewById(android.R.id.icon);

					if (!item.isDisabled()) { // item is enabled => disables it
						tv1.setPaintFlags(tv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // strike through text of title
						tv2.setPaintFlags(tv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // strike through quantity and quantity type
						tv1.setTextColor(getResources().getColor(R.color.light_gray));
						tv2.setTextColor(getResources().getColor(R.color.light_gray));
						icon.setImageResource(R.drawable.check_mark_black); // display black check mark

						item.setDisabled(); // disable selected item in model
						sl.disabledItems++; // increase count of disabled items
					} else { // item is disabled => enables it
						tv1.setPaintFlags(tv1.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG)); // remove strike through text
						tv2.setPaintFlags(tv2.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
						tv1.setTextColor(getResources().getColor(R.color.black)); // set origin color
						tv2.setTextColor(getResources().getColor(R.color.black));
						icon.setImageResource(R.drawable.check_mark_gray); // display gray check mark

						item.setEnabled(); // enable item
						sl.disabledItems--;
					}
					
					if (sl.hasAllItemsDisabled()) { // if all items are disabled show confirm dialog
						DialogFragment confirmDialog = new ConfirmDialog();
						confirmDialog.show(getSupportFragmentManager(), "confirmDialog");
					}

				} else { // shopping list is unlocked => show update form dialog
					DialogFragment formDialog = new FormDialog();
					int productId = item.getId();
					String dialogTitle = item.getTitle();
					
					((FormDialog) formDialog).setTitle(dialogTitle); // set dialog title
					((FormDialog) formDialog).setProductId(productId); // set selected product id
					formDialog.show(getSupportFragmentManager(), "updateFormDialog");
				}
			}
		};
	}
	
	private OnItemLongClickListener shoppingListLongClickListener(final ShoppingList sl, final ArrayAdapter<ShoppingListItem> adapter) {
		return new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (!sl.isLocked()) {
					ShoppingListItem item = sl.getItemByIndex(position); // get selected item
					int productId = item.getId();
					String dialogTitle = item.getTitle();
					
					DialogFragment deleteDialog = new DeleteDialog();
					((DeleteDialog) deleteDialog).setTitle(dialogTitle); // set dialog title
					((DeleteDialog) deleteDialog).setProductId(productId); // set selected product id
					deleteDialog.show(getSupportFragmentManager(), "deleteDialog"); // show delete dialog
				}
				return false;
			}
		};
	}
	
	public void showAddCustomForm(View view) {
		DialogFragment addCustomFormDialog = new FormDialog();
		int generatedId = (int) (Math.random()*100000);
		String dialogTitle = getResources().getString(R.string.add_custom_form_header);
		
		((FormDialog) addCustomFormDialog).setTitle(dialogTitle); // set dialog title
		((FormDialog) addCustomFormDialog).setProductId(generatedId); // set newly generated product id
		((FormDialog) addCustomFormDialog).setCustomProduct(true); // tell the dialog that we are adding custom product
		addCustomFormDialog.show(getSupportFragmentManager(), "addCustomFormDialog");
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
			findViewById(R.id.addCustomButton).setVisibility(View.VISIBLE);
			findViewById(R.id.lockCurrentListButton).setVisibility(View.VISIBLE);
			if (sl.isLocked()) {
				findViewById(R.id.clearCurrentListButton).setVisibility(View.VISIBLE);
				findViewById(R.id.sendSmsButton).setVisibility(View.GONE);
				findViewById(R.id.readSmsButton).setVisibility(View.GONE);
				findViewById(R.id.addNextButton).setVisibility(View.GONE);
				findViewById(R.id.addCustomButton).setVisibility(View.GONE);
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
			findViewById(R.id.addCustomButton).setVisibility(View.GONE);
		}
	}
	
	/*
	 * Saves all items in current list to file.
	 */
	public boolean saveItemsToFile(ShoppingList sl, String fileName) {
		try {
			FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(sl);
			oos.close();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}
