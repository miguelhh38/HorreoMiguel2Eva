package com.example.horreomiguel2eva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuAgenda extends AppCompatActivity {

    Button nuevoContacto;
    Button buttonLista;
    Button buttonNota;
    ImageButton buttonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_agenda);
        nuevoContacto = findViewById(R.id.nuevoContactoButton);
        nuevoContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAgenda.this, NuevoContacto.class);
                startActivity(intent);
            }
        });

        buttonLista = findViewById(R.id.button6);
        buttonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAgenda.this, ListadoContactos.class);
                startActivity(intent);
            }
        });

        buttonImage = findViewById(R.id.imageButton1);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAgenda.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonNota = findViewById(R.id.buttonNota);
        buttonNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAgenda.this, NotaActivity.class);
                startActivity(intent);
            }
        });



    }


}