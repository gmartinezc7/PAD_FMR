package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import es.ucm.fdi.v3findmyroommate.R;

public class EditEmailDialog extends DialogFragment {

    private TextView emailTextView;
    private EditText emailEditText;


    private static final String NULL_EMAIL_TOAST_TEXT = "El email no puede ser nulo";
    private static final String INVALID_EMAIL_TOAST_TEXT = "La dirección de correo ha de ser válida";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflates the layout.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_email, null);

        this.emailTextView = view.findViewById(R.id.new_email_label);
        this.emailEditText = view.findViewById(R.id.edit_email_edittext);

        // Builds the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Editar dirección de correo")
                .setPositiveButton("Aceptar", null)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditEmailDialog.this.dismiss();
                        Log.i("Configuration", "Canceled email update");
                    }
                });

        AlertDialog editEmailDialog = builder.create(); // Creates the AlertDialog

        editEmailDialog.setOnShowListener(dialogInterface -> {
            Button button = editEmailDialog.getButton(AlertDialog.BUTTON_POSITIVE); // Gets the positive button
            button.setOnClickListener(view1 -> {
                String newEmail = this.emailEditText.getText().toString();
                if (newEmail.isEmpty()) {
                    // Shows void email error toast.
                    Toast nullEmailToast = Toast.makeText(getActivity(), NULL_EMAIL_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    nullEmailToast.show();
                    Log.e("Configuration", "Email can't be void");
                }
                else if (!newEmail.contains("@")) {
                    // Shows invalid email error toast.
                    Toast invalidEmailToast = Toast.makeText(getActivity(), INVALID_EMAIL_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    invalidEmailToast.show();
                    Log.e("Configuration", "Email address must be a valid address");
                }
                else {
                    Log.i("Configuration", "New email: " + newEmail);
                    editEmailDialog.dismiss();
                }
            });
        });

        return editEmailDialog;
    }


}