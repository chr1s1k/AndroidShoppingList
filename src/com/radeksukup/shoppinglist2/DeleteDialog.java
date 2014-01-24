/**
 * 
 */
package com.radeksukup.shoppinglist2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class DeleteDialog extends DialogFragment  {
	
	private int productId;
	private String title;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final ShoppingList sl = (ShoppingList) getActivity().getApplication();
		
		builder.setTitle(title)
			.setMessage(R.string.delete_dialog_message)
			.setPositiveButton(R.string.delete_dialog_yes_button, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					ListView shoppingList = (ListView) getActivity().findViewById(R.id.shoppingList);
					sl.removeItem(productId);
					shoppingList.invalidateViews();
					
					if (sl.size() == 0) { // all items has been deleted => rerender main screen
//						getActivity().findViewById(R.id.sendSmsButton).setVisibility(View.GONE);
						getActivity().findViewById(R.id.readSmsButton).setVisibility(View.VISIBLE);
//						getActivity().findViewById(R.id.addNextButton).setVisibility(View.GONE);
//						getActivity().findViewById(R.id.addCustomButton).setVisibility(View.GONE);
						getActivity().findViewById(R.id.lockCurrentListButton).setVisibility(View.GONE);
//						getActivity().findViewById(R.id.showCategoriesButton).setVisibility(View.VISIBLE);
						sl.setImported(false);
					}
					
					String[] toastMessages = getResources().getStringArray(R.array.toast_messages);
					Toast.makeText(getActivity().getApplicationContext(), toastMessages[2], Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton(R.string.delete_dialog_no_button, new DialogInterface.OnClickListener() {
				
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

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

}