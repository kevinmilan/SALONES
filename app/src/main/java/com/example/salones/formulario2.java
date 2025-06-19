package com.example.salones;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class formulario2 extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lv=findViewById(R.id.lista);

        // recibimos el arraylist que enviamos desde el formulario2
        ArrayList<String> datos = getIntent().getStringArrayListExtra("datosUsuario");


        // verificamos que los datos no sean nulos
        if (datos != null) {
            // creamos un arrayadapter para mostrar la lista en el listview
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1, // layout para mostrar un item simple de texto
                    datos // la lista de datos a mostrar
            );
            // asignamos el adaptador al listview para que muestre los datos
            lv.setAdapter(adapter);
        } else {
            // si no hay datos, se muestra este mensaje de error
            Toast.makeText(this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
        }

    }
}