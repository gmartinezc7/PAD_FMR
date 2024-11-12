package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        FirebaseApp.initializeApp(this);

        // Prueba GitHub
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openLoginView();
            }
        });
        TextView signUP = findViewById(R.id.signUP);
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openSignUPView();}
        });
    }

    public void openLoginView(){
        Intent intent =  new Intent(MainActivity.this, Lobby.class);
        startActivity(intent);

    }
    public void openSignUPView(){
        Intent intent =  new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);

    }
}