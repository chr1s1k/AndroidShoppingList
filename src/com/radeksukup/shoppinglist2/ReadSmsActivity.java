package com.radeksukup.shoppinglist2;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReadSmsActivity extends Activity {
	
	private boolean slWasPopulated;
	public final static String IMPORT_TOAST_MESSAGE = "com.radeksukup.shoppinglist2.IMPORT_TOAST_MESSAGE";
	private final int smsLimit = 15;
	private int smsCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_sms);
		// Show the Up button in the action bar.
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	// Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final ShoppingList sl = (ShoppingList) getApplication();
        final ArrayList<Sms> smsList = getAllIncomingSms();
        ListView smsListView = (ListView) findViewById(android.R.id.list);
        TextView smsLimitView = (TextView) findViewById(R.id.sms_limit);
        
        if (smsList.size() != 0) { // we have some SMS in inbox, so display them
        	if (smsCount > smsLimit) { // if there more than "smsLimit" messages, display info message
        		smsLimitView.setText(String.format(this.getResources().getString(R.string.sms_limit), smsLimit));
        	} else {
        		smsLimitView.setVisibility(View.GONE); // otherwise hide the info message
        	}
        	
        	ArrayAdapter<Sms> adapter = new SmsListItemAdapter(this, android.R.layout.simple_list_item_1, smsList);
        	smsListView.setAdapter(adapter); // set adapter
        	smsListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final String smsBody = smsList.get(position).getBody();
					
					// shopping list already contains some items
					if (sl.hasItems()) {
						new AlertDialog.Builder(parent.getContext())
							.setTitle(R.string.import_dialog_title)
							.setMessage(R.string.import_dialog_message)
							.setPositiveButton(R.string.import_dialog_merge_button, new DialogInterface.OnClickListener() { // merge
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									importListFromSms(smsBody, sl);
								}
								
							})
							.setNegativeButton(R.string.import_dialog_replace_button, new DialogInterface.OnClickListener() { // replace
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									slWasPopulated = sl.hasItems();
									sl.unLock();
									sl.empty();
									importListFromSms(smsBody, sl);
								}
							})
							.show();
					} else {
						importListFromSms(smsBody, sl);
					}
				}
			});
        } else { // no incoming SMS in inbox
        	TextView noSmsView = (TextView) findViewById(R.id.no_sms);
        	
        	smsLimitView.setVisibility(View.GONE);
        	smsListView.setVisibility(View.GONE);
        	noSmsView.setVisibility(View.VISIBLE);
        }
	}
	
	/*
	 * Get all SMS received SMS messages from inbox.
	 */
	public ArrayList<Sms> getAllIncomingSms() {
		ArrayList<Sms> smsList = new ArrayList<Sms>();
		Sms sms;
		int limit = smsLimit;
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null); // get SMS cursor
		if (cursor.moveToFirst()) {
			smsCount = cursor.getCount();

			if (smsCount < limit) {
				limit = smsCount; // display only last "smsLimit" SMS messages
			}
			
			for (int i = 0; i < limit; i++) { // iterate through all sms in inbox
				sms = new Sms();
				sms.setId(cursor.getColumnIndexOrThrow("_id"));
				sms.setThreadId(cursor.getColumnIndexOrThrow("thread_id"));
				String sender = geDisplayNameByMsisdn(cursor.getString(cursor.getColumnIndexOrThrow("address")));
				sms.setSender(sender);
				sms.setDate(cursor.getLong(cursor.getColumnIndexOrThrow("date")));
				sms.setBody(cursor.getString(cursor.getColumnIndexOrThrow("body")));
				
				smsList.add(sms);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return smsList;
	}
	
	public void backToMainActivity(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}
	
	/*
	 * Get contact display name by his MSISDN.
	 */
	public String geDisplayNameByMsisdn(String msisdn) {
		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(msisdn));
		String[] columns = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }; // set column(s) to query 
		Cursor cursor = getContentResolver().query(contactUri, columns, null, null, null);
		
		if (cursor.moveToFirst()) { // contact found
			return cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME)); // return display name
		} else {
			return msisdn; // contact not found
		}
	}
	
	/*
	 * Import products from SMS body.
	 */
	public void importListFromSms(String smsBody, ShoppingList sl) {
		if (smsBody != null && smsBody.length() != 0) { // check if sms content is not empty
			smsBody = smsBody.substring(0, smsBody.length() - 1);
			
			String products[] = smsBody.split(",\n"); // split sms content
			ShoppingListItem item;
			int added = 0;
			int merged = 0;

			for (int i = 0; i < products.length; i++) {
				String productDetails[] = products[i].split(" ");
				
				try {
					if (productDetails.length >= 3) {
						int generatedId = (int) (Math.random()*100000);
						double quantity = Double.parseDouble(productDetails[productDetails.length - 2]); // can throw NumberFormatException
						String quantityType = productDetails[productDetails.length - 1];
						String title = productDetails[0]; // get first index of product title
						for (int j = 1; j < productDetails.length - 2; j++) {
							title += " " + productDetails[j]; // append rest of product title
						}
						
						item = new ShoppingListItem(generatedId, title, quantity, quantityType);

						if (sl.itemExists(title)) {
							sl.updateItemFromSms(item); // update existing in shopping list
							merged++;
						} else {
							sl.addItem(item); // add new item to shopping list
							added++;
						}
					}
				} catch (NumberFormatException e) {
					continue; // if exception is thrown, skip current loop iteration
				}
				
			}
			
			String[] toastMessages = getResources().getStringArray(R.array.toast_messages);
			String toastMessage = "";
			
			if (added != 0 && merged != 0) { // at least one item was imported and merged from SMS
				toastMessage = String.format(toastMessages[8], added, merged);
			} else if (added != 0 && slWasPopulated) { // at least one item was imported and previously created list was removed
				toastMessage = String.format(toastMessages[9], added);
			} else if (added != 0) { // only added
				toastMessage = String.format(toastMessages[6], added);
			} else if (merged != 0) { // only merged
				toastMessage = String.format(toastMessages[7], merged);
			} else { // nothing was imported or merged
				toastMessage = toastMessages[5];
			}
			
			slWasPopulated = false;
			
			if (added != 0 || merged != 0) { // if there was some action, "redirect" to main activity and afterwards show toast message
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra(IMPORT_TOAST_MESSAGE, toastMessage);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			} else { // if nothing was imported, show toast message immediately
				Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
			}
		}
	} 

	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_sms, menu);
		return true;
	}

}
