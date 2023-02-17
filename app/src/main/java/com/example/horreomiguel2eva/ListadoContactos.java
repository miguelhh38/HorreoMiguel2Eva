package com.example.horreomiguel2eva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListadoContactos extends AppCompatActivity {

    Button buttonVolver;

    ListView contactos;



    private DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("agenda");
        contactRef = userRef.child("contacts" + userId);
        contactos = findViewById(R.id.ListVIewContacts);
        contactRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Contacto contacto;
                ArrayAdapter<Contacto> adapter;
                ArrayList<Contacto> listadoContactos = new ArrayList<Contacto>();
                for (DataSnapshot dp : snapshot.getChildren()) {
                    contacto = dp.getValue(Contacto.class);
                    listadoContactos.add(contacto);
                }
                adapter = new ArrayAdapter<>(ListadoContactos.this,
                        android.R.layout.simple_list_item_1, listadoContactos);
                contactos.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        contactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListadoContactos.this, ContactoActual.class);
                Contacto contacto = (Contacto) contactos.getItemAtPosition(position);
                intent.putExtra("objeto", contacto);
                startActivity(intent);
                startActivity(intent);
            }
        });

        buttonVolver = findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListadoContactos.this, MenuAgenda.class);
                startActivity(intent);
            }
        });






    }

}