package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Dialog;
import android.content.Context;
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
import androidx.fragment.app.Fragment;


import es.ucm.fdi.v3findmyroommate.R;

public class EditEmailDialog extends DialogFragment {

    private TextView emailTextView;
    private EditText emailEditText;
    private String currentEmail, newEmail;
    private DialogListener listener;

    private static final String NULL_EMAIL_TOAST_TEXT = "El email no puede ser nulo";
    private static final String INVALID_EMAIL_TOAST_TEXT = "La dirección de correo ha de ser válida";

    public EditEmailDialog(String currentEmail) {
        this.currentEmail = currentEmail;
        this.newEmail = currentEmail;
    }

    public interface DialogListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }

    public static EditEmailDialog newInstance(String currentEmail) {
        EditEmailDialog dialog = new EditEmailDialog(currentEmail);
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment fragment = getParentFragment();

        if (fragment instanceof DialogListener) {
            this.listener = (DialogListener) fragment;
        } else if (context instanceof DialogListener) {
            this.listener = (DialogListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflates the layout.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_email, null);

        this.emailTextView = view.findViewById(R.id.new_email_label);
        this.emailEditText = view.findViewById(R.id.edit_email_edittext);
        this.emailEditText.setText(this.currentEmail);

        // Builds the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Editar dirección de correo")
                .setPositiveButton("Aceptar", null)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditEmailDialog.this.dismiss();
                        Log.i("EditEmailDialog", "Email update canceled");
                    }
                });

        AlertDialog editEmailDialog = builder.create(); // Creates the AlertDialog

        editEmailDialog.setOnShowListener(dialogInterface -> {
            Button button = editEmailDialog.getButton(AlertDialog.BUTTON_POSITIVE); // Gets the positive button
            button.setOnClickListener(view1 -> {
                String newEmailWritten = this.emailEditText.getText().toString();
                if (newEmailWritten.isEmpty()) {
                    // Shows void email error toast.
                    Toast nullEmailToast = Toast.makeText(getActivity(), NULL_EMAIL_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    nullEmailToast.show();
                    Log.e("EditEmailDialog", "Email can't be void");
                }
                else if (!newEmailWritten.contains("@")) {
                    // Shows invalid email error toast.
                    Toast invalidEmailToast = Toast.makeText(getActivity(), INVALID_EMAIL_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    invalidEmailToast.show();
                    Log.e("EditEmailDialog", "Email address must be a valid address");
                }
                else {
                    this.newEmail = newEmailWritten;
                    this.listener.onPositiveButtonClick();
                    Log.i("EditEmailDialog", "New email: " + newEmailWritten);
                    editEmailDialog.dismiss();
                }
            });
        });

        return editEmailDialog;
    }

    // Obtains the new email.
    public String getNewEmail() {
        return this.newEmail;
    }


}