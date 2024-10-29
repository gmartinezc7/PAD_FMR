package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import es.ucm.fdi.v3findmyroommate.R;

public class EditUsernameDialog extends DialogFragment {

    private TextView usernameTextView;
    private EditText usernameEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflates the layout.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_username, null); // Use your XML layout name

        this.usernameTextView = view.findViewById(R.id.new_username_label);
        this.usernameEditText = view.findViewById(R.id.edit_username_edittext);

        // Builds the dialog.
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Editar nombre de usuario")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handles the OK button click.
                        String newUsername = EditUsernameDialog.this.usernameEditText.getText().toString();
                        Log.i("Configuration", "New username: " + newUsername);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditUsernameDialog.this.dismiss();
                        Log.i("Configuration", "Canceled username update");
                    }
                })
                .create();
    }
}

