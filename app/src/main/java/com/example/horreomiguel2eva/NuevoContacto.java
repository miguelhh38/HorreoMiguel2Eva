package com.example.horreomiguel2eva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NuevoContacto extends AppCompatActivity {

    Button volverButton;
    Button guardarButton;
    EditText nombreEditText;
    EditText emailEditText;
    EditText dirEditText;
    EditText telfEditText;
    private DatabaseReference contactRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("agenda");
        contactRef = userRef.child("contacts" + userId);

        volverButton = findViewById(R.id.volverButton);
        nombreEditText = findViewById(R.id.nombreEditText);
        emailEditText = findViewById(R.id.emailEditText);
        dirEditText = findViewById(R.id.dirEditText);
        telfEditText = findViewById(R.id.telfEditText);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoContacto.this, MenuAgenda.class);
                startActivity(intent);
            }
        });

        guardarButton = findViewById(R.id.buttonGuardar);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean numero1 = false;
                        String errores = "";
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Contacto contacto = ds.getValue(Contacto.class);
                            if (contacto.getTelefono().equals(telfEditText.getText().toString())) {
                                numero1 = true;
                                break;
                            }
                        }
                        if (numero1) {
                            errores += "El número ya está guardado";
                        } else {
                            if (nombreEditText.getText().length()<2) {
                                errores += "El nombre es demasiado corto.\n";
                            }
                            if (!correoValido(emailEditText.getText().toString())) {
                                errores += "El email no es válido.\n";
                            }
                            if (dirEditText.getText().length()<2) {
                                errores += "La dirección no es válida.\n";
                            }
                            if (!numeroValido(telfEditText.getText().toString())) {
                                errores += "El teléfono no es válido.\n";
                            }
                        }

                        if (errores.isEmpty()) {
                            Contacto contacto = new Contacto(nombreEditText.getText().toString(),
                                    emailEditText.getText().toString(),
                                    telfEditText.getText().toString(), dirEditText.getText().toString());

                            contactRef.push().setValue(contacto);
                            Toast.makeText(NuevoContacto.this, "Contacto guardado!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NuevoContacto.this, MenuAgenda.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NuevoContacto.this);
                            builder.setTitle("ERROR AL GUARDAR CONTACTO");
                            builder.setMessage(errores);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



    }

    public static boolean correoValido (String correo){
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher marcher = pattern.matcher(correo);
        return marcher.matches();
    }

    public static boolean numeroValido(String numero) {
        return (numero.length() == 9 && numero.matches("[0-9]+"));
    }




}