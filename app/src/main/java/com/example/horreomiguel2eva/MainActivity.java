package com.example.horreomiguel2eva;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText userTEXT;
    EditText passTEXT;
    Button iniciarSesionButton;
    Button registroButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        userTEXT = findViewById(R.id.userTEXT);
        passTEXT = findViewById(R.id.passTEXT);
        iniciarSesionButton = findViewById(R.id.iniciarSesion);
        registroButton = findViewById(R.id.registrarse);

        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(userTEXT.getText().toString(), passTEXT.getText().toString());
            }
        });

        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro(userTEXT.getText().toString(), passTEXT.getText().toString());
            }
        });

    }


    public void registro(String name, String pass) {
        if (name.isEmpty() || pass.isEmpty()) {
            Toast.makeText(MainActivity.this, "Debe de completar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(userTEXT.getText().toString(), passTEXT.getText().toString())
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "El usuario ya existe!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    public void login(String name, String pass) {
        if (name.isEmpty() || pass.isEmpty()) {
            Toast.makeText(MainActivity.this, "Debe de completar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(name, pass)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(MainActivity.this, MenuAgenda.class);
                                startActivity(intent);
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}