package com.radeksukup.shoppinglist2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormDialog extends DialogFragment {
	
	private String title;
	private int productId;
	private boolean customProduct = false;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final ShoppingList sl = (ShoppingList) getActivity().getApplication();

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		// Inflate and set the layout for the dialog
		final View dialogView = inflater.inflate(R.layout.add_form_dialog, null);

		// assign "add string" to positive button text
		String positiveButtonText = getResources().getString(R.string.add_product);

		// check if item is already in shopping list
		ShoppingListItem existingItem = sl.getItem(productId);
		EditText quantityInput = (EditText) dialogView.findViewById(R.id.quantityInput);

		if (existingItem != null) { // item exists
			String castedQuantity = String.valueOf((Double) existingItem.getQuantity());
			
			if ((Double) existingItem.getQuantity() % 1.0 == 0) { // if quantity has no decimal part => casts it to integer
				castedQuantity = String.valueOf((int) existingItem.getQuantity());
			}
			
			quantityInput.setText(castedQuantity); // set value entered by user

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
			
			// assign "update string" to positive button text
			positiveButtonText = getResources().getString(R.string.update_product);
		}
		
		// prepare form dialog view for adding custom product
		if (customProduct) {
			EditText productTitleInput = (EditText) dialogView.findViewById(R.id.productTitleInput);
			
			productTitleInput.setVisibility(View.VISIBLE);
			productTitleInput.setFocusableInTouchMode(true);
			productTitleInput.requestFocus();
		}

		builder.setView(dialogView);
		builder.setTitle(title)
		.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// get quantity
				EditText quantityInput = (EditText) dialogView.findViewById(R.id.quantityInput); // find text field by id
				
				// get product title
				EditText productTitleInput = (EditText) dialogView.findViewById(R.id.productTitleInput);
				
				String[] toastMessages = getResources().getStringArray(R.array.toast_messages);
				String toastMessage;
				
				// form validation
				if ((customProduct && !productTitleInput.getText().toString().equals("") && !quantityInput.getText().toString().equals("") && Double.parseDouble(quantityInput.getText().toString()) > 0) || (!customProduct && !quantityInput.getText().toString().equals("") && Double.parseDouble(quantityInput.getText().toString()) > 0)) {
					double quantity = Double.parseDouble(quantityInput.getText().toString()); // get value of text field

					// get & set product title entered by user
					if (customProduct) {
						title = productTitleInput.getText().toString();
					}
					
					// get checked quantity type
					RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.quantityType); // find radio group by id
					int checkedRadioId = rg.getCheckedRadioButtonId(); // get id of checked radio
					RadioButton checkedRadio = (RadioButton) dialogView.findViewById(checkedRadioId); // find radio button by id
					String quantityType = checkedRadio.getText().toString(); // get value of checked radio
					
					ShoppingListItem item = new ShoppingListItem(productId, title, quantity, quantityType);
					if (sl.itemExists(productId)) {
						sl.updateItem(item); // update existing in shopping list
						toastMessage = toastMessages[1]; // set toast message for adding
					} else {
						sl.addItem(item); // add new item to shopping list
						toastMessage = toastMessages[0]; // set toast message for updating
					}
					
					
					ListView shoppingList = (ListView) getActivity().findViewById(R.id.shoppingList); // try to find shoppingList view
					if (shoppingList != null) {
						shoppingList.invalidateViews(); // invalidate current shoppingList view => make a refresh of list
					}
					
					EditText searchInput = (EditText) getActivity().findViewById(R.id.search_input);
					if (searchInput != null) {
						searchInput.setText(""); // clear text of search input
					}
					
				} else {
					toastMessage = toastMessages[4]; // set toast message for invalid input (zero or nothing entered)
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

	public void onStart() {
		super.onStart();
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
	
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
	
	public void setCustomProduct(boolean customProduct) {
		this.customProduct = customProduct;
	}

}
