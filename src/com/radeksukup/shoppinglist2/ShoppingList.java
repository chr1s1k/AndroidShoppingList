/**
 * 
 */
package com.radeksukup.shoppinglist2;

import java.util.ArrayList;

import android.app.Application;

/**
 * @author Radek Sukup
 *
 */
public class ShoppingList extends Application {

	private ArrayList<ShoppingListItem> items = new ArrayList<ShoppingListItem>();
	public int disabledItems = 0;
	private boolean locked = false;

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
	
	@Override
	public String toString() {
		return "########## Polozek mame " + items.size() + ".\n############# Seznam je locked: " + locked + ".\n########### Disabled polozek je " + disabledItems;
	}
	
}
