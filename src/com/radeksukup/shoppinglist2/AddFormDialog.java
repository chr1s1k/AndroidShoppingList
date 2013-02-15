package com.radeksukup.shoppinglist2;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddFormDialog extends DialogFragment {
	
	private String title;
	private int productId;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final ShoppingList sl = (ShoppingList) getActivity().getApplication();
		
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		// Inflate and set the layout for the dialog
		final View dialogView = inflater.inflate(R.layout.add_form_dialog, null);
		
		// check if item is already in shopping list
		ShoppingListItem existingItem = sl.getItem(productId);
		if (existingItem != null) { // item exists
			EditText quantityInput = (EditText) dialogView.findViewById(R.id.quantityInput);
			quantityInput.setText(Float.toString((float) existingItem.getQuantity())); // set value entered by user
			
			RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.quantityType);
			int radiosCount = rg.getChildCount(); // get count of radio buttons
			for (int i = 0; i < radiosCount; i++) { // iterate over all radios children
				View radio = rg.getChildAt(i);
				if (radio instanceof RadioButton) {
					if (((RadioButton) radio).getText() == existingItem.getQuantityType()) {
						((RadioButton) radio).setChecked(true); // set checked value entered by user
					}
				}
			}
		}
		
		builder.setView(dialogView);
		builder.setTitle(title)
		.setPositiveButton(R.string.add_product, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// get quantity
				EditText quantityInput = (EditText) dialogView.findViewById(R.id.quantityInput); // find text field by id
				double quantity = Double.parseDouble(quantityInput.getText().toString()); // get value of text field
				
				// get checked quantity type
				RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.quantityType); // find radio group by id
				int checkedRadioId = rg.getCheckedRadioButtonId(); // get id of checked radio
				RadioButton checkedRadio = (RadioButton) dialogView.findViewById(checkedRadioId); // find radio button by id
				String quantityType = checkedRadio.getText().toString(); // get value of checked radio
				
				ShoppingListItem item = new ShoppingListItem(productId, title, quantity, quantityType);
				String[] toastMessages = getResources().getStringArray(R.array.toast_messages);
				String toastMessage;
				if (sl.itemExists(productId)) {
					sl.updateItem(item); // update existing in shopping list
					toastMessage = toastMessages[1]; // set toast message for adding
				} else {
					sl.addItem(item); // add new item to shopping list
					toastMessage = toastMessages[0]; // set toast message for updating
				}
				
				// show toast message
				Toast.makeText(getActivity().getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
				
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// DO NOTHING! Action canceled.
			}
		});
		return builder.create();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

}
