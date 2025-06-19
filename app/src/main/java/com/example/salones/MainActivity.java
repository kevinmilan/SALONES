package com.example.salones;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText c1,c2,c3,c4,c5,c6;

    Button b1,b2,b3,b4;

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
        c1 = findViewById(R.id.caja1);
        c2 = findViewById(R.id.caja2);
        c3 = findViewById(R.id.caja3);
        c4 = findViewById(R.id.caja4);
        c5 = findViewById(R.id.caja5);
        c6 = findViewById(R.id.caja6);

        b1 = findViewById(R.id.boton1); // INGRESAR
        b2 = findViewById(R.id.boton2); // BUSCAR
        b3 = findViewById(R.id.boton3); // ELIMINAR
        b4 = findViewById(R.id.boton4); // MODIFICAR

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);



    }

    @Override // este metodo se ejecuta cuando se hace click en el boton
    public void onClick(View v) {
        if (v.getId() == R.id.boton1){
            ingresar(); // si el boton que se presiona es el 1, el de ingresar, se llama al metodo ingresar
        }else if (v.getId() == R.id.boton2){
            buscarCodigo(); // si se presiona el boton 2, el de buscar, se llama el metodo buscarCodigo
        } else if (v.getId() == R.id.boton3) {
            eliminar(); // si se presiona el boton 3, el de eliminar, se llama al metodo eliminar
        } else if (v.getId() == R.id.boton4) {
            modificar(); // si se presiona el boton 4, el de modificar, se llama al metodo modificar
        }

    }



    public void ingresar(){ // metodo para insertar datos en la base de datos
       // creamos una instancia de la clase conexion para abrir la base de datos salo
        Conexion admin = new Conexion(this, "salo",null, 1);
        SQLiteDatabase db = admin.getWritableDatabase(); // hacemos la conexion del boton con la base de datos para que se registre

        // obtenemos los valores y los convertimos a los tipos necesarios
        int cod = Integer.parseInt(c1.getText().toString());
        String man = c2.getText().toString();
        int salon = Integer.parseInt(c3.getText().toString());
        int edifi = Integer.parseInt(c4.getText().toString());
        String facu = c5.getText().toString();

        // creamos un objeto contentvalues para guardar los datos en pares clave - valor
        ContentValues rg = new ContentValues();
        rg.put("Codigo", cod);
        rg.put("Mantenimiento", man);
        rg.put("Salón", salon);
        rg.put("Edificio", edifi);
        rg.put("Facultad", facu);

        try {
            // insertamos los datos en la tabla form
            db.insert("salo", null, rg);
            Toast.makeText(this, "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // si ocurre un error aparece este mensaje
            Toast.makeText(this, "Error al insertar los datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
            c1.setText("");
            c2.setText("");
            c3.setText("");
            c4.setText("");
            c5.setText("");
        }

    }

    public void buscarCodigo(){ // metodo para buscar un registro por su codigo
        // creamos una nueva conexion a la base de datos
        Conexion admin = new Conexion(this, "salo", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();

        String bux = c6.getText().toString().trim(); // este es el codigo que se quiere buscar, el texto del campo el c7, caja7


        // hacemos una consulta SQL para ver y buscar el registro del codigo
        Cursor fila = db.rawQuery("SELECT Mantenimiento, Salón, Edificio, Facultad FROM salo WHERE Codigo = ?", new String[]{bux});



        // si se encuentra un resulta  do se crea una lista para guardar los datos encontrados
        if (fila.moveToFirst()) {
            ArrayList<String> datos = new ArrayList<>(); // usamos arraylist que es una herramienta que viene incluida en Java para guardar listas
            datos.add("Código: " + bux);
            datos.add("Mantenimiento: " + fila.getString(0));
            datos.add("Salón: " + fila.getString(1));
            datos.add("Edificio: " + fila.getString(2));
            datos.add("Facultad: " + fila.getString(3));

            // ahora limpiamos los datos del formulario
            c1.setText("");
            c2.setText("");
            c3.setText("");
            c4.setText("");
            c5.setText("");

            // creamos un intent para mandar al formulario 2 y mandamos la lista de datos
            Intent x = new Intent(this, formulario2.class); // enviamos la lista al siguiente formulario, el 3
            x.putStringArrayListExtra("datosUsuario", datos);
            startActivity(x); // iniciamos el formulario 3 con esto.
        } else {
            // si no se encuentra un registro con el codigo aparece este mensaje
            Toast.makeText(this, "Error, no se encontró ningun registro con ese código", Toast.LENGTH_SHORT).show();
            c1.setText("");
            c2.setText("");
            c3.setText("");
            c4.setText("");
            c5.setText("");

        }

}


      public void eliminar(){
          // Se crea una conexión a la base de datos llamada "salo"
          Conexion admin = new Conexion(this, "salo", null, 1);
          SQLiteDatabase db = admin.getWritableDatabase(); // Se obtiene acceso de escritura a la base de datos

          // Se obtiene el código a eliminar desde el campo de texto c6 (el campo de búsqueda)
          String cod = c6.getText().toString().trim();

          // Se verifica que el campo no esté vacío
          if (!cod.isEmpty()) {
              // Se intenta eliminar el registro que tenga ese código
              int cantidad = db.delete("salo", "Codigo = ?", new String[]{cod});

              // Se cierra la base de datos
              db.close();

              // Si se eliminó al menos un registro, se muestra mensaje de éxito
              if (cantidad > 0) {
                  Toast.makeText(this, "Registro eliminado exitosamente", Toast.LENGTH_SHORT).show();
              } else {
                  // Si no se eliminó nada, significa que no existía un registro con ese código
                  Toast.makeText(this, "No se encontró un registro con ese código", Toast.LENGTH_SHORT).show();
              }
          } else {
              // Si el campo está vacío, se solicita que se ingrese un código
              Toast.makeText(this, "Por favor, ingresa un código para eliminar", Toast.LENGTH_SHORT).show();
          }

          // Se limpian todos los campos de texto
          c1.setText("");
          c2.setText("");
          c3.setText("");
          c4.setText("");
          c5.setText("");

      }
      public void modificar(){
          // Se crea una conexión a la base de datos llamada "salo"
          Conexion admin = new Conexion(this, "salo", null, 1);
          SQLiteDatabase db = admin.getWritableDatabase(); // Se obtiene acceso de escritura a la base de datos

          // Se obtiene el código que se va a buscar/modificar desde el campo c6
          String codBuscar = c6.getText().toString().trim();

          // Se verifica que el campo no esté vacío
          if (!codBuscar.isEmpty()) {

              // Se obtienen los nuevos datos desde los campos c1 a c5
              int nuevoCod = Integer.parseInt(c1.getText().toString());       // Nuevo código
              String man = c2.getText().toString();                            // Nuevo mantenimiento
              int salon = Integer.parseInt(c3.getText().toString());          // Nuevo salón
              int edifi = Integer.parseInt(c4.getText().toString());          // Nuevo edificio
              String facu = c5.getText().toString();                           // Nueva facultad

              // Se almacenan los nuevos valores en un objeto ContentValues (clave-valor)
              ContentValues datos = new ContentValues();
              datos.put("Codigo", nuevoCod);
              datos.put("Mantenimiento", man);
              datos.put("Salón", salon);
              datos.put("Edificio", edifi);
              datos.put("Facultad", facu);

              // Se intenta actualizar el registro que tenga el código indicado en codBuscar
              int cantidad = db.update("salo", datos, "Codigo = ?", new String[]{codBuscar});

              // Se cierra la base de datos
              db.close();

              // Si se actualizó al menos un registro, se muestra mensaje de éxito
              if (cantidad > 0) {
                  Toast.makeText(this, "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
              } else {
                  // Si no se actualizó nada, significa que no existía un registro con ese código
                  Toast.makeText(this, "No se encontró un registro con ese código", Toast.LENGTH_SHORT).show();
              }
          } else {
              // Si no se ingresó código en c6, se muestra mensaje
              Toast.makeText(this, "Debes ingresar el código a modificar en la caja de búsqueda (caja6)", Toast.LENGTH_SHORT).show();
          }

          // Se limpian todos los campos de texto
          c1.setText("");
          c2.setText("");
          c3.setText("");
          c4.setText("");
          c5.setText("");
      }

}
