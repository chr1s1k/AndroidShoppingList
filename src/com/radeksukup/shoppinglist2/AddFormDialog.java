package com.radeksukup.shoppinglist2;

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

public class AddFormDialog extends DialogFragment {
	
	private String title;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		// Inflate and set the layout for the dialog
		final View dialogView = inflater.inflate(R.layout.add_form_dialog, null);
		builder.setView(dialogView);
		builder.setTitle(title)
		.setPositiveButton(R.string.add_product, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// get quantity
				EditText quantityInput = (EditText) dialogView.findViewById(R.id.quantityInput); // find text field by id
				String quantity = quantityInput.getText().toString(); // get value of text field
				
				// get checked quantity type
				RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.quantityType); // find radio group by id
				int checkedRadioId = rg.getCheckedRadioButtonId(); // get id of checked radio
				RadioButton checkedRadio = (RadioButton) dialogView.findViewById(checkedRadioId); // find radio button by id
				String quantityType = checkedRadio.getText().toString(); // get value of checked radio
				// TODO - insert data into model
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

}
