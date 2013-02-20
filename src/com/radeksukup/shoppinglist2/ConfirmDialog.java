package com.radeksukup.shoppinglist2;

import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ConfirmDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final ShoppingList sl = (ShoppingList) getActivity().getApplication();
		
		builder.setTitle(R.string.confirm_dialog_title)
			.setMessage(R.string.confirm_dialog_message)
			.setPositiveButton(R.string.confirm_dialog_yes_button, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					sl.empty();
					sl.unLock();
					ListView shoppingList = (ListView) getActivity().findViewById(R.id.shoppingList);
					shoppingList.invalidateViews(); // clear data from shopping list view
					getActivity().findViewById(R.id.sendSmsButton).setVisibility(View.GONE);
					getActivity().findViewById(R.id.clearCurrentListButton).setVisibility(View.GONE);
					getActivity().findViewById(R.id.showCategoriesButton).setVisibility(View.VISIBLE);
					
					// show toast message
					String[] toastMessages = getResources().getStringArray(R.array.toast_messages);
					Toast.makeText(getActivity().getApplicationContext(), toastMessages[3], Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton(R.string.confirm_dialog_no_button, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// DO NOTHING! Action canceled.
				}
			});
		return builder.create();
	}
	
}
