package com.riosgavagnin.emiliano.a2048;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.riosgavagnin.emiliano.a2048.data.UsuariosContractSQL;

/*
 * Actividad que se usará como menu principal una vez iniciada la sesion o registrado el usuario
 */

public class MainActivity extends AppCompatActivity {

    protected int FILASCOLUMNAS = 4;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
       ////
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       ////

       //OBTENER ALTURA Y ANCHO DE PANTALLA
       //DisplayMetrics displayMetrics = new DisplayMetrics();
       //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
       //int height = displayMetrics.heightPixels;
       //int width = displayMetrics.widthPixels;

       ImageView lArrow = findViewById(R.id.larrow_iv); // Creo dos ImageView que utilizare como botones para elegir niveles
       ImageView rArrow = findViewById(R.id.rarrow_iv);
       TextView tvNivel = findViewById(R.id.nivel_tv);
       tvNivel.setText(R.string.nivel4x4);

       lArrow.setOnClickListener(new View.OnClickListener() { // Establezco la acción del boton seleccionar de nivel
           @Override
           public void onClick(View v) {
               switch (FILASCOLUMNAS) {
                   case 3:
                       FILASCOLUMNAS = 8;
                       tvNivel.setText(R.string.nivel8x8);
                       break;
                   case 4:
                       FILASCOLUMNAS = 3;
                       tvNivel.setText(R.string.nivel3x3);
                       break;
                   case 8:
                       FILASCOLUMNAS = 4;
                       tvNivel.setText(R.string.nivel4x4);
                       break;
               }
           }
       });

       rArrow.setOnClickListener(v -> { // Establezco la acción del boton seleccionar de nivel
           switch (FILASCOLUMNAS){
               case 3:
                   FILASCOLUMNAS = 4;
                   tvNivel.setText(R.string.nivel4x4);
                   break;
               case 4:
                   FILASCOLUMNAS = 8;
                   tvNivel.setText(R.string.nivel8x8);
                   break;
               case 8:
                   FILASCOLUMNAS = 3;
                   tvNivel.setText(R.string.nivel3x3);
                   break;
           }
       });




       Intent previous = getIntent(); // Actividad anterior (LoginActivity)
       String usuarioActual = previous.getStringExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_USER);

       Button botonJugar = findViewById(R.id.jugar_boton);
       Button botonRanking = findViewById(R.id.ranking_boton);
       Button botonSalir = findViewById(R.id.salir_boton);

       botonJugar.setOnClickListener(view -> { // Defino la accion del boton Jugar donde se determina el nivel del juego y se crea la actividad JugarActivity
           Context context = MainActivity.this;

           Class destinationActivity = JugarActivity.class;

           Intent startJugar= new Intent(context,destinationActivity);

           int recordActual = previous.getIntExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD,0);
           startJugar.putExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD,recordActual);

           startJugar.putExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_USER,usuarioActual);

           startJugar.putExtra("NIVEL",FILASCOLUMNAS);

           startActivity(startJugar);

       });

       botonRanking.setOnClickListener(view ->{ // Defino la accion del boton Ranking donde se crea la actividad RankingActivity
           Context context = MainActivity.this;

           Class destinationActivity = RankingActivity.class;

           Intent startRank= new Intent(context,destinationActivity);

           //startMain.putExtra(Intent.EXTRA_TEXT, username);

           startActivity(startRank);

       });

       botonSalir.setOnClickListener(view ->  { // Defino la accion del boton Salir donde finalizo la sesión

           finish();

       });





    }




}












