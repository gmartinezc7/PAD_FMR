package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import es.ucm.fdi.v3findmyroommate.R;

public class EditEmailDialog extends DialogFragment {

    private TextView emailTextView;
    private EditText emailEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflates the layout.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_email, null);

        this.emailTextView = view.findViewById(R.id.new_email_label);
        this.emailEditText = view.findViewById(R.id.edit_email_edittext);

        // Builds the dialog.
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                        .setTitle("Editar correo electrónico")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handles the OK button click.
                        String newEmail = EditEmailDialog.this.emailEditText.getText().toString();
                        Log.i("Configuration", "New email address: " + newEmail);;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditEmailDialog.this.dismiss();
                        Log.i("Configuration", "Canceled email update");
                    }
                })
                .create();
    }
}

