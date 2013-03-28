/**
 * 
 */
package com.radeksukup.shoppinglist2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Radek Sukup
 *
 */
public class ShoppingListItem implements Parcelable {
	
	private int id;
	private String title;
	private double quantity;
	private String quantityType;
	private boolean disabled;
	
	public ShoppingListItem(int id, String title, double quantity, String quantityType) {
		this.id = id;
		this.title = title;
		this.quantity = quantity;
		this.quantityType = quantityType;
		disabled = false;
	}
	
	private ShoppingListItem(Parcel in) {
		id = in.readInt();
		title = in.readString();
		quantity = in.readDouble();
		quantityType = in.readString();
		disabled = in.readByte() == 1 ? true : false;
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
	
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled() {
		disabled = true;
	}
	
	public void setEnabled() {
		disabled = false;
	}

	@Override
	public String toString() {
		return title;
	}
	
	public String getQuantityAndType() {
		String castedQuantity = String.valueOf(quantity);
		
		if (quantity % 1.0 == 0) {
			castedQuantity = String.valueOf((int) quantity);
		}
		
		return castedQuantity + " " + quantityType;
	}
	
	public static final Parcelable.Creator<ShoppingListItem> CREATOR = new Creator<ShoppingListItem>() {

		@Override
		public ShoppingListItem createFromParcel(Parcel in) {
			return new ShoppingListItem(in);
		}

		@Override
		public ShoppingListItem[] newArray(int size) {
			return new ShoppingListItem[size];
		}
		
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(title);
		out.writeDouble(quantity);
		out.writeString(quantityType);
		out.writeByte((byte) (disabled ? 1 : 0));
	}

}
