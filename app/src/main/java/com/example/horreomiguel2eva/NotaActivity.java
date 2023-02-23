package com.example.horreomiguel2eva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class NotaActivity extends AppCompatActivity {

    EditText titulo;
    EditText contenido;
    Button volver;
    Button guardar;
    private DatabaseReference notaRef;
    Boolean existe = false;

    private static Nota nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        titulo = findViewById(R.id.editTextTitulo);
        contenido = findViewById(R.id.editTextContenido);

        volver = findViewById(R.id.button7);
        guardar = findViewById(R.id.button5);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("agenda");
        notaRef = userRef.child("nota" + userId);

        notaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nota nota;
                ArrayAdapter<Nota> adapter;
                ArrayList<Nota> listadoNota = new ArrayList<Nota>();
                for (DataSnapshot dp : snapshot.getChildren()) {
                    nota = dp.getValue(Nota.class);
                    listadoNota.add(nota);
                }
                if (!listadoNota.isEmpty()) {
                    existe = true;
                    nota = new Nota(listadoNota.get(0).getTitulo(), listadoNota.get(0).getContenido());
                    titulo.setText(listadoNota.get(0).getTitulo());
                    contenido.setText(listadoNota.get(0).getContenido());
                } else {
                    existe = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotaActivity.this, MenuAgenda.class);
                startActivity(intent);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errores = "";
                if (titulo.getText().length()<1) {
                    errores += "El titulo introducido es demasiado corto";
                }
                if (contenido.getText().length()<2) {
                    errores += "El contenido introducido es demasiado corto";
                }
                if (errores.isEmpty()) {

                    if (existe) {
                        Query query = notaRef.orderByChild("titulo").equalTo(nota.getTitulo());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()) {
                                    String clave = ds.getKey();

                                    notaRef.child(clave).child("contenido").setValue(contenido.getText().toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(NotaActivity.this, "Nota actualizada!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NotaActivity.this, MenuAgenda.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Nota nota = new Nota(titulo.getText().toString(), contenido.getText().toString());
                        notaRef.push().setValue(nota);

                        Toast.makeText(NotaActivity.this, "Nota creada!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NotaActivity.this, MenuAgenda.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NotaActivity.this);
                    builder.setTitle("ERROR AL GUARDAR NOTA");
                    builder.setMessage(errores);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}