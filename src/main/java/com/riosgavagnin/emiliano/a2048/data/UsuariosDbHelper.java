package com.riosgavagnin.emiliano.a2048.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsuariosDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "usuarios.db";

        private static  final int DATABASE_VERSION = 1;

        public UsuariosDbHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USUARIOS_TABLE = "CREATE TABLE " + UsuariosContractSQL.UsuariosEntry.TABLE_NAME +
                " (" + UsuariosContractSQL.UsuariosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UsuariosContractSQL.UsuariosEntry.COLUMN_USER + " TEXT NOT NULL, " +
                UsuariosContractSQL.UsuariosEntry.COLUMN_PASSWORD + " TEXT NOT NULL," +
                UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD + " INTEGER" +
                ");";

        db.execSQL(SQL_CREATE_USUARIOS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsuariosContractSQL.UsuariosEntry.TABLE_NAME);
        onCreate(db);
    }
}
