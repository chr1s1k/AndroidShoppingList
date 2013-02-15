/**
 * 
 */
package com.radeksukup.shoppinglist2;

/**
 * @author Radek Sukup
 *
 */
public class ShoppingListItem {
	
	private int id;
	private String title;
	private double quantity;
	private String quantityType;
	
	public ShoppingListItem(int id, String title, double quantity, String quantityType) {
		//super();
		this.id = id;
		this.title = title;
		this.quantity = quantity;
		this.quantityType = quantityType;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	public String getQuantityType() {
		return quantityType;
	}
	
	public void setQuantityType(String quantityType) {
		this.quantityType = quantityType;
	}

}
