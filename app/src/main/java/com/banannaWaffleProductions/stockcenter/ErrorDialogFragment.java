package com.banannaWaffleProductions.stockcenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {
    /*Class that is responsible for a dialogFragment that alerts the user of an error and gracefully
      handles the error.*/

    public ErrorDialogFragment(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Sorry your request cannot be processed at this time.\n"
                + "Please try again later.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing.
                    }
                });
        return builder.create();
    }
}
