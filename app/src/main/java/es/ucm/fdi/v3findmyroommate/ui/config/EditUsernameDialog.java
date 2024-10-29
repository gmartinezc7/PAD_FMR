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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.ucm.fdi.v3findmyroommate.R;

public class EditUsernameDialog extends DialogFragment {

    private TextView usernameTextView;
    private EditText usernameEditText;

    private static final String NULL_USERNAME_TOAST_TEXT = "El nombre de usuario no puede estar vacío";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflates the layout.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_username, null); // Use your XML layout name

        this.usernameTextView = view.findViewById(R.id.new_username_label);
        this.usernameEditText = view.findViewById(R.id.edit_username_edittext);

        // Builds the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Editar nombre de usuario")
                .setPositiveButton("Aceptar", null)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditUsernameDialog.this.dismiss();
                        Log.i("Configuration", "Canceled username update");
                    }
                });

        AlertDialog editUsernameDialog = builder.create(); // Creates the AlertDialog

        editUsernameDialog.setOnShowListener(dialogInterface -> {
            Button button = editUsernameDialog.getButton(AlertDialog.BUTTON_POSITIVE); // Gets the positive button
            button.setOnClickListener(view1 -> {
                String newUsername = this.usernameEditText.getText().toString();
                if (newUsername.isEmpty()) {
                    // Shows void username error toast.
                    Toast nullUsernameToast = Toast.makeText(getActivity(), NULL_USERNAME_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    nullUsernameToast.show();
                    Log.e("Configuration", "Username can't be void");
                }
                else {
                    Log.i("Configuration", "New username: " + newUsername);
                    editUsernameDialog.dismiss();
                }
            });
        });

        return editUsernameDialog;
    }


}

