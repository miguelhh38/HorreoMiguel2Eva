package com.example.horreomiguel2eva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ContactoActual extends AppCompatActivity {

    TextView nombre;
    TextView telefono;
    TextView dir;
    TextView mail;

    Button volver;
    Button llamar;
    Button eliminar;
    Button editar;

    private DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference();

    Contacto contactoActual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_actual);

        telefono = findViewById(R.id.textView20);
        mail = findViewById(R.id.textView22);
        dir = findViewById(R.id.textView21);


        Intent intent = getIntent();
        contactoActual = (Contacto) intent.getSerializableExtra("objeto");

        nombre = findViewById(R.id.textView19);
        nombre.setText(contactoActual.getNombre());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("agenda");
        contactRef = userRef.child("contacts" + userId);


        Query query = contactRef.orderByChild("telefono").equalTo(contactoActual.getTelefono());


        telefono.setText(contactoActual.getTelefono());
        dir.setText(contactoActual.getDireccion());

        mail.setText(contactoActual.getEmail());





        editar = findViewById(R.id.button);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactoActual.this, EditarContacto.class);
                Contacto contacto = contactoActual;
                intent.putExtra("objeto", contacto);
                startActivity(intent);
            }
        });

        volver = findViewById(R.id.button3);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactoActual.this, ListadoContactos.class);
                startActivity(intent);
            }
        });


        llamar = findViewById(R.id.button4);
        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llamar(telefono.getText().toString());
            }
        });

        eliminar = findViewById(R.id.button2);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            String clave = ds.getKey();
                            contactRef.child(clave).removeValue();
                        }
                        Toast.makeText(ContactoActual.this, "Contacto eliminado!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ContactoActual.this, ListadoContactos.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void llamar(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}