package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.v3findmyroommate.PreferencesFragment.PreferenceUser;


public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }
    public void createAccount(View view){
        try{
            Intent intent = new Intent(SignUp.this, PreferenceUser.class);
            startActivity(intent);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }
}
