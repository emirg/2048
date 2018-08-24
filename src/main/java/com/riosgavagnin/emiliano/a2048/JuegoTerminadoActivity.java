package com.riosgavagnin.emiliano.a2048;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.riosgavagnin.emiliano.a2048.data.UsuariosContractSQL;
import com.riosgavagnin.emiliano.a2048.data.UsuariosDbHelper;

/*
* Activity lanzada una vez que se detecta que el juego termino
* Muestra por pantalla el record/puntaje y lo actualiza en la BD de ser necesario
* Permite al jugador iniciar una partida nueva o salir
*/
public class JuegoTerminadoActivity extends AppCompatActivity {

    private String usuarioActual;
    private int recordNuevo;
    private SQLiteDatabase usuariosBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego_terminado);


        UsuariosDbHelper dbHelper = new UsuariosDbHelper(this);
        usuariosBD = dbHelper.getWritableDatabase();


        Intent previous = getIntent(); // Obtengo la actividad anterior (JugarActivity)
        usuarioActual = previous.getStringExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_USER); // Obtengo el nombre de usuario enviado por JugarActivity
        recordNuevo = previous.getIntExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD,0); // Obtengo el record del usuario enviado por JugarActivity


        TextView recordNuevoTextView = findViewById(R.id.puntajeNuevo_tv);
        recordNuevoTextView.setText(String.valueOf(recordNuevo));


        Button botonJugarDeNuevo = findViewById(R.id.boton_juegarDeNuevo_bv);

        Button botonSalir = findViewById(R.id.boton_salirJuegoTerminado_bv);

        botonJugarDeNuevo.setOnClickListener(view -> { // Establezco la acción del boton Jugar de nuevo (finaliza la actividad actual e informa a JugarActivity que debe reiniciarse)
            setResult(RESULT_CANCELED,null);
            finish();

        });

        botonSalir.setOnClickListener(view -> { // Establezco la acción del boton Salir (finaliza la actividad actual e informa a la actividad JugarActivity que debe finalizar)
           setResult(RESULT_OK,null);
           finish();
        });

        if(recordNuevo != 0){ // Si el record mejoro, lo actualizo
            actualizarPuntaje();
        }

    }

    // Establezco la acción a realizar cuando se presiona el boton de ir atras (Actualizar el puntaje y volver al menu principal)
    @Override
    public void onBackPressed(){
        setResult(RESULT_OK,null);
        super.onBackPressed();
        finish();
        actualizarPuntaje();
    }

    // Actualizo el record
    public void actualizarPuntaje(){
        String[] columnas = {"_ID",
                UsuariosContractSQL.UsuariosEntry.COLUMN_USER,
                UsuariosContractSQL.UsuariosEntry.COLUMN_PASSWORD,
                UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD};

        ContentValues cv  = new ContentValues();

        cv.put(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD, recordNuevo);

        usuariosBD.update(UsuariosContractSQL.UsuariosEntry.TABLE_NAME,cv,UsuariosContractSQL.UsuariosEntry.COLUMN_USER+"= '"+usuarioActual+"'",null);
    }
}
