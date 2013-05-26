/**
 * 
 */
package com.radeksukup.shoppinglist2;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Radek Sukup
 *
 */
public class ShoppingList extends Application implements Serializable {

	private static final long serialVersionUID = 1L;
	public final String PREF_STORAGE = "preferencesStorage";
	private ArrayList<ShoppingListItem> items = new ArrayList<ShoppingListItem>();
	public int disabledItems = 0;
	private boolean locked = false;
	private boolean imported = false;
	
	/*
	 * Returns true if items was imported from SMS.
	 */
	public boolean wasImported() {
		return imported;
	}
	
	public void setImported(boolean imported) {
		this.imported = imported;
	}

	/*
	 * Locks shopping list.
	 */
	public void lock() {
		locked = true;
	}
	
	/*
	 * Unlocks shopping list.
	 */
	public void unLock() {
		locked = false;
	}
	
	/*
	 * Returns true if shopping list is locked, otherwise false.
	 */
	public boolean isLocked() {
		return locked;
	}
	
	/*
	 * Sets state of shopping list to locked or unlocked.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/*
	 * Returns count of items in list.
	 */
	public int size() {
		return items.size();
	}
	
	/*
	 * Adds items to list
	 */
	public void setItems(ArrayList<ShoppingListItem> items) {
		this.items = items;
	}
	
	/*
	 * Removes all items from shopping list.
	 */
	public void empty() {
		items.clear();
		disabledItems = 0;
	}
	
	/*
	 * Returns all items from shopping list.
	 */
	public ArrayList<ShoppingListItem> getItems() {
		return items;
	}
	
	/*
	 * Returns true if shopping list has at least one item, otherwise false.
	 */
	public boolean hasItems() {
		return !items.isEmpty();
	}
	
	/*
	 * Removes a single item from shopping list specified by id.
	 */
	public void removeItem(int id) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getId() == id) {
				items.remove(i);
				break;
			}
		}
	}
	
	/*
	 * Inserts item into shopping list.
	 */
	public void addItem(ShoppingListItem newItem) {
		items.add(newItem);
	}
	
	/*
	 * Updates existing item in shopping list.
	 */
	public void updateItem(ShoppingListItem updatedItem) {
		if (itemExists(updatedItem.getId())) {
			ShoppingListItem existingItem = getItem(updatedItem.getId());
			existingItem.setQuantity(updatedItem.getQuantity());
			existingItem.setQuantityType(updatedItem.getQuantityType());
		}
	}
	
	/*
	 * Updates existing item in shopping list from SMS.
	 */
	public void updateItemFromSms(ShoppingListItem updatedItem) {
		ShoppingListItem existingItem = getItem(updatedItem.getTitle());
		double quantity = existingItem.getQuantity() + updatedItem.getQuantity(); // old quantity + new quantity
		
		existingItem.setQuantity(quantity); // set new quantity
		existingItem.setQuantityType(updatedItem.getQuantityType());
		
		if (existingItem.isDisabled()) {
			existingItem.setEnabled(); // after merging re-enable the item
			disabledItems--;
		}
	}
	
	/*
	 * @param product id
	 * Returns a single item from shopping list if exists, otherwise null.
	 */
	public ShoppingListItem getItem(int id) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getId() == id) {
				return items.get(i);
			}
		}
		return null;
	}
	
	/*
	 * @param product title
	 * Returns a single item from shopping list if exists, otherwise null.
	 */
	public ShoppingListItem getItem(String title) {
		for (int i = 0; i < items.size(); i++) {
			if (Utils.removeDiacritics(items.get(i).getTitle()).equals(Utils.removeDiacritics(title))) {
				return items.get(i);
			}
		}
		return null;
	}
	
	/*
	 * Returns a single item by index from shopping list if exists, otherwise null.
	 */
	public ShoppingListItem getItemByIndex(int index) {
		return items.get(index);
	}
	
	/*
	 * Returns true if all items in shopping list are disabled, otherwise false.
	 */
	public boolean hasAllItemsDisabled() {
		return disabledItems == items.size();
	}
	
	/*
	 * @param product id
	 * Returns true if item already exists in current shopping list, otherwise false.
	 */
	public boolean itemExists(int id) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * @param product title
	 * Returns true if item already exists in current shopping list, otherwise false.
	 */
	public boolean itemExists(String title) {
		for (int i = 0; i < items.size(); i++) {
			if (Utils.removeDiacritics(items.get(i).getTitle()).equals(Utils.removeDiacritics(title))) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Saves current state of shopping list into sharedPreferences. If operation fails, toast message is displayed.
	 */
	public void save() {
		SharedPreferences pref = getSharedPreferences(PREF_STORAGE, Context.MODE_PRIVATE); // get shared preferences
		SharedPreferences.Editor editor = pref.edit(); 
		try {
			String serializedList = Utils.serializeObjectToString(this); // serialize whole shopping list into string
			editor.putString("shopping-list", serializedList); // save serialized shopping list into persistent storage
			editor.commit();
		} catch (IOException e) {
			String[] toastMessages = getResources().getStringArray(R.array.toast_messages);
			Toast.makeText(getApplicationContext(), toastMessages[11], Toast.LENGTH_SHORT).show(); // display toast message if saving list failed
		}
	}
	
	/*
	 * Restores previously saved shopping list from sharedPreferences.
	 */
	public void restore() {
		SharedPreferences pref = getSharedPreferences(PREF_STORAGE, Context.MODE_PRIVATE); // get shared preferences
		String serializedList = pref.getString("shopping-list", "{}"); // get previously serialized shopping list from shared preferences
		try {
			ShoppingList deserializedList = Utils.deserializeObjectFromString(serializedList); // deserialize shopping list from string
			this.disabledItems = deserializedList.disabledItems; // set disabled items
			this.setLocked(deserializedList.isLocked()); // set status of created shopping list
			this.setImported(deserializedList.wasImported());
			if (deserializedList.getItems() != null) {
				this.setItems(deserializedList.getItems()); // set previously added items
			}
		} catch (Exception e) {
			Log.d("ShoppingList", "Failed when restoring shopping list.");
		}
	}
	
	@Override
	public String toString() {
		return "########## Polozek mame " + items.size() + ".\n############# Seznam je locked: " + locked + ".\n########### Disabled polozek je " + disabledItems;
	}
	
}
