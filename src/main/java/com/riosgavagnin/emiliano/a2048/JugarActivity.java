package com.riosgavagnin.emiliano.a2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.riosgavagnin.emiliano.a2048.data.UsuariosContractSQL;
import com.riosgavagnin.emiliano.a2048.data.UsuariosDbHelper;

import static com.riosgavagnin.emiliano.a2048.ArrayUtils.arrayToList;
import static com.riosgavagnin.emiliano.a2048.ArrayUtils.inicializarArray;
import static com.riosgavagnin.emiliano.a2048.ArrayUtils.juegoTermino;
import static com.riosgavagnin.emiliano.a2048.ArrayUtils.shiftArrayDown;
import static com.riosgavagnin.emiliano.a2048.ArrayUtils.shiftArrayLeft;
import static com.riosgavagnin.emiliano.a2048.ArrayUtils.shiftArrayRight;
import static com.riosgavagnin.emiliano.a2048.ArrayUtils.shiftArrayUp;
import static com.riosgavagnin.emiliano.a2048.ArrayUtils.actualizarFuente;

/* Actividad encargada de manejar el juego en si
 *
 */
public class JugarActivity extends AppCompatActivity {
    private float x1,x2,y1,y2; // Coordenadas utilizadas para detectar el swipe
    static final int MIN_DISTANCE = 150; // Distancia minima que debe tener el swipe para ser evaluado
    private int puntajeActual; // Puntaje actual del jugador
    private int puntajeRecord; // Puntaje record del jugador
    private String usuarioActual; // Nombre del usuario que esta jugando
    private MediaPlayer mp; // MediaPlayer utilizado para reproducir musica en el juego
    private boolean reproduciendo = true; // Variable que indica si es esta reproduciendo musica o no (true-false respectivamente)
    private SQLiteDatabase usuariosBD;


    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);
        ////
        Intent intentMain = getIntent();
        ////

        UsuariosDbHelper dbHelper = new UsuariosDbHelper(this);
        usuariosBD = dbHelper.getWritableDatabase();

        usuarioActual = intentMain.getStringExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_USER);

        // Busco la información del usuario para revisar su ultimo record
        String[] columnas = {"_ID",UsuariosContractSQL.UsuariosEntry.COLUMN_USER, UsuariosContractSQL.UsuariosEntry.COLUMN_PASSWORD, UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD};
        Cursor result = usuariosBD.query(
                UsuariosContractSQL.UsuariosEntry.TABLE_NAME,
                columnas,
                UsuariosContractSQL.UsuariosEntry.COLUMN_USER + " = '" + usuarioActual + "'",
                null,
                null,
                null,
                null
        );


        int FILASCOLUMNAS = intentMain.getIntExtra("NIVEL",8); // Verifica el nivel elegido y arma la grilla acorde al nivel traido de la Activity anterior

        int[][] array = new int[FILASCOLUMNAS][FILASCOLUMNAS];
        inicializarArray(array); // Inicializo la grilla colocando dos numeros (2 ó 4) en posiciones aleatorias
        //int [][] array = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}}; //CASO PARA PROVOCAR FIN DE JUEGO 4x4
        //int [][] array = {{1,2,3,4,5,128,7,8},{1,2,3,4,5,6,7,8},{1,2,2048,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8}}; //CASO PARA PROVOCAR FIN DE JUEGO 8x8

        puntajeActual = 0;
        result.moveToFirst();
        puntajeRecord = result.getInt(result.getColumnIndex(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD));// Verifico si el usuario tenia un puntaje record anterior

        // BOTON RESTART
        ImageView restart = findViewById(R.id.boton_restart);
        restart.setOnClickListener(v -> {
            mp.stop();
            recreate();
        });

        // MUSICA
        mp = MediaPlayer.create(JugarActivity.this, R.raw.musica);
        mp.setLooping(true);
        mp.start();

        // BOTON MUSICA ON/OFF
        ImageView volumen = findViewById(R.id.boton_volumen);
        volumen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                    reproduciendo = false;
                    volumen.setImageResource(R.drawable.mute);
                }else{
                    mp.start();
                    reproduciendo = true;
                    volumen.setImageResource(R.drawable.volume);
                }
            }
        });

        // PUNTAJE ACTUAL
        TextView puntajeTextView = findViewById(R.id.puntaje_tv);
        puntajeTextView.setText(String.valueOf(puntajeActual));

        // PUNTAJE RECORD
        TextView puntajeRecordTextView = findViewById(R.id.puntajeRecord_tv);
        puntajeRecordTextView.setText(String.valueOf(puntajeRecord));

        // GRILLA
        GridView gridView = findViewById(R.id.gridview);
        gridView.setNumColumns(array[0].length);
        gridView.setAdapter(new GridAdapter(this,arrayToList(array))); // Creo el GridView y le establezco el GridAdapter con la lista de datos (numeros de la grilla)

        gridView.setOnTouchListener((v, event) -> { // Defino las acciones de swipe
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    x2 = event.getX();
                    y2 = event.getY();
                    float deltaX = x2 - x1;
                    float deltaY = y2 - y1;

                    if (Math.abs(deltaX) > MIN_DISTANCE) // Según la diferencia que se detecta entre el punto de inicio y de fin del swipe, se fija si fue un swipe lateral o vertical
                    {
                        // Swipe Izquierda a Derecha
                        if (x2 > x1) // Si el x2 (punto donde libero el swipe) es mayor que el x1 (punto donde inicio) entonces es un swipe de izq. a der.
                        {
                            puntajeActual = puntajeActual + shiftArrayRight(array); // Aplico el shift correspondiente y actualizo el puntaje

                                gridView.setAdapter(new GridAdapter(JugarActivity.this,arrayToList(array))); // Actualizo el GridView
                                if(puntajeActual != Integer.valueOf(puntajeTextView.getText().toString())){ // Si el puntaje cambio...
                                    puntajeTextView.setText(String.valueOf(puntajeActual));
                                    //actualizarFuente(puntajeActual,puntajeTextView);
                                    if(puntajeActual > puntajeRecord){ // Si el nuevo puntaje actual es mejor que el record, comienzo a mostrar el record nuevo junto al actual
                                        puntajeRecord = puntajeActual;
                                        puntajeRecordTextView.setText(String.valueOf(puntajeRecord));
                                    }
                                }
                                if(juegoTermino(array)){ // Verifico si termino el juego
                                    //Log.v("TERMINO JUEGO","OK");
                                    terminarJuego();
                                }
                        }

                        // Swipe Derecha a Izquierda
                        else
                        {

                            puntajeActual = puntajeActual +shiftArrayLeft(array);

                                gridView.setAdapter(new GridAdapter(JugarActivity.this,arrayToList(array)));
                                if(puntajeActual != Integer.valueOf(puntajeTextView.getText().toString())){
                                    puntajeTextView.setText(String.valueOf(puntajeActual));
                                    if(puntajeActual > puntajeRecord){
                                        puntajeRecord = puntajeActual;
                                        puntajeRecordTextView.setText(String.valueOf(puntajeRecord));
                                    }
                                }
                                if(juegoTermino(array)){
                                    //Log.v("TERMINO JUEGO","OK");
                                    terminarJuego();
                                }
                        }

                    }
                    else
                    {
                        if (Math.abs(deltaY) > MIN_DISTANCE)
                        {
                            // Swipe de Abajo a Arriba
                            if (y1 > y2)
                            {
                                puntajeActual = puntajeActual +shiftArrayUp(array);

                                    gridView.setAdapter(new GridAdapter(JugarActivity.this,arrayToList(array)));
                                    if(puntajeActual != Integer.valueOf(puntajeTextView.getText().toString())){
                                        puntajeTextView.setText(String.valueOf(puntajeActual));
                                        if(puntajeActual > puntajeRecord){
                                            puntajeRecord = puntajeActual;
                                            puntajeRecordTextView.setText(String.valueOf(puntajeRecord));
                                        }
                                    }
                                if(juegoTermino(array)){
                                    //Log.v("TERMINO JUEGO","OK");
                                    terminarJuego();
                                }
                            }

                            // Swipe de Arriba a Abajo
                            else
                            {
                                puntajeActual = puntajeActual +shiftArrayDown(array);

                                gridView.setAdapter(new GridAdapter(JugarActivity.this,arrayToList(array)));
                                if(puntajeActual != Integer.valueOf(puntajeTextView.getText().toString())){
                                    puntajeTextView.setText(String.valueOf(puntajeActual));
                                    if(puntajeActual > puntajeRecord){
                                        puntajeRecord = puntajeActual;
                                        puntajeRecordTextView.setText(String.valueOf(puntajeRecord));
                                    }
                                }
                                if(juegoTermino(array)){
                                   //Log.v("TERMINO JUEGO","OK");
                                    terminarJuego();
                                }
                            }

                        }

                    }
                    break;
            }
            return true;
        });


    }

    // Metodo encargado de recrear la actividad o finalizarla acorde a la accion elegida en JuegoTerminadoActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                finish();
            }else{
                if (resultCode == RESULT_CANCELED){
                    this.recreate();
                }
            }
        }
    }

   /* @Override
    protected void onPause(){
        super.onPause();
    }*/

    @Override
    public void onBackPressed() { // Establezco la accion a realizar cuando se presiona el boton de ir atras (Salir de la sesion)
        super.onBackPressed();
        mp.stop();
        finish();

    }

    protected void onPause(){ // Establezco el comportamiento de la aplicacion cuando la actividad cambia a estado de pausa
        super.onPause();
        mp.pause();
    }

    protected void onResume(){ // Establezco el comportamiento de la aplicacion cuando la actividad cambia a estado de reanudar
        super.onResume();
        if(reproduciendo){
            mp.start();
        }
    }



    // Metodo encargado de finalizar el juego cuando se cumplen las condiciones
    private void terminarJuego(){
        mp.stop();

        Context context = JugarActivity.this;

        Class destinationActivity = JuegoTerminadoActivity.class;

        Intent startTerminado= new Intent(context,destinationActivity);

        if(puntajeRecord <= puntajeActual) {
            startTerminado.putExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD, puntajeActual);
        }else{
            startTerminado.putExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD, 0);
        }
        startTerminado.putExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_USER,usuarioActual);

        startActivityForResult(startTerminado,1);


    }












}
