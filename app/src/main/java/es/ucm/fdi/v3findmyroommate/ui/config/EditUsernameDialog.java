package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
    private String currentUsername, newUsername;
    private DialogListener listener;

    private static final String NULL_USERNAME_TOAST_TEXT = "El nombre de usuario no puede estar vacío";

    public EditUsernameDialog(String currentUsername) {
        this.currentUsername = currentUsername;
        this.newUsername = currentUsername;
    }

    public interface DialogListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }

    public static EditUsernameDialog newInstance(String currentUsername) {
        EditUsernameDialog dialog = new EditUsernameDialog(currentUsername);
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment fragment = getParentFragment();

        if (fragment instanceof EditUsernameDialog.DialogListener) {
            this.listener = (EditUsernameDialog.DialogListener) fragment;
        } else if (context instanceof EditUsernameDialog.DialogListener) {
            this.listener = (EditUsernameDialog.DialogListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflates the layout.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_username, null); // Use your XML layout name

        this.usernameTextView = view.findViewById(R.id.new_username_label);
        this.usernameEditText = view.findViewById(R.id.edit_username_edittext);
        this.usernameEditText.setText(currentUsername);

        // Builds the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Editar nombre de usuario")
                .setPositiveButton("Aceptar", null)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditUsernameDialog.this.dismiss();
                        Log.i("EditUsernameDialog", "Username update canceled");
                    }
                });

        AlertDialog editUsernameDialog = builder.create(); // Creates the AlertDialog

        editUsernameDialog.setOnShowListener(dialogInterface -> {
            Button button = editUsernameDialog.getButton(AlertDialog.BUTTON_POSITIVE); // Gets the positive button
            button.setOnClickListener(view1 -> {
                String newUsernameWritten = this.usernameEditText.getText().toString();
                if (newUsernameWritten.isEmpty()) {
                    // Shows void username error toast.
                    Toast nullUsernameToast = Toast.makeText(getActivity(), NULL_USERNAME_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    nullUsernameToast.show();
                    Log.e("EditUsernameDialog", "Username can't be void");
                }
                else {
                    this.newUsername = newUsernameWritten;
                    this.listener.onPositiveButtonClick();
                    Log.i("EditUsernameDialog", "New username: " + newUsernameWritten);
                    editUsernameDialog.dismiss();
                }
            });
        });

        return editUsernameDialog;
    }


    // Obtains the new username.
    public String getNewUsername() {
        return this.newUsername;
    }


}

