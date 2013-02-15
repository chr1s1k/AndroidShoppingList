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
	private int lockedItems = 0;
	boolean locked = false;
	
	/*
	 * Lock shopping list.
	 */
	public void lock() {
		locked = true;
	}
	
	/*
	 * Unlock shopping list.
	 */
	public void unLock() {
		locked = false;
	}
	
	/*
	 * Return true if shopping list is locked, otherwise false.
	 */
	public boolean isLocked() {
		return locked;
	}
	
	/*
	 * Remove all items from shopping list.
	 */
	public void empty() {
		items.clear();
		lockedItems = 0;
	}
	
	/*
	 * Return all items from shopping list.
	 */
	public ArrayList<ShoppingListItem> getItems() {
		return items;
	}
	
	/*
	 * Return true if shopping list has at least one item, otherwise false.
	 */
	public boolean hasItems() {
		return !items.isEmpty();
	}
	
	/*
	 * Remove a single item from shopping list specified by id.
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
	 * Insert item into shopping list.
	 */
	public void addItem(ShoppingListItem newItem) {
//		if (itemExists(newItem.getId())) {
//			updateItem(newItem);
//		} else {
			items.add(newItem);
//		}
	}
	
	/*
	 * Update existing item in shopping list.
	 */
	public void updateItem(ShoppingListItem updatedItem) {
		if (itemExists(updatedItem.getId())) {
			ShoppingListItem existingItem = getItem(updatedItem.getId());
			existingItem.setQuantity(updatedItem.getQuantity());
			existingItem.setQuantityType(updatedItem.getQuantityType());
		}
	}
	
	/*
	 * Return a single item from shopping list if exists, otherwise null.
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
	 * Return true if item already exists in current shopping list, otherwise false.
	 */
	public boolean itemExists(int id) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public void show() {
		
	}
	
}
