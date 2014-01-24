package com.radeksukup.shoppinglist2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
//import android.app.DialogFragment;

public class ConfirmDialog extends DialogFragment {
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ConfirmDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    ConfirmDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the ConfirmDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ConfirmDialogListener so we can send events to the host
            mListener = (ConfirmDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement ConfirmDialogListener");
        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle(R.string.confirm_dialog_title)
			.setMessage(R.string.confirm_dialog_message)
			.setPositiveButton(R.string.confirm_dialog_yes_button, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					mListener.onDialogPositiveClick(ConfirmDialog.this);
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
