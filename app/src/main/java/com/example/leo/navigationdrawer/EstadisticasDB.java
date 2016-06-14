package com.example.leo.navigationdrawer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EstadisticasDB extends SQLiteOpenHelper {
    private Context ctx;

    public EstadisticasDB(Context context) {
        super(context, "estadisticas.db", null, 1);
        ctx = context;
    }

    //método que se ejecuta cuando la base de datos no existe.
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Jugador(id_jugador INTEGER PRIMARY KEY AUTOINCREMENT, id_equipo INTEGER, nombre_jugador TEXT, dorsal INTEGER)"); // Sí no existe la base de datos la crea y ejecuta la consulta
        db.execSQL("CREATE TABLE Equipo(id_equipo INTEGER PRIMARY KEY AUTOINCREMENT, nombre_equipo TEXT)");
        db.execSQL("CREATE TABLE Partido(id_partido INTEGER PRIMARY KEY AUTOINCREMENT, fecha_partido TEXT, id_equipo_local INTEGER, id_equipo_visitante INTEGER)");
        db.execSQL("CREATE TABLE JuegaPartido (id_jugador INTEGER NOT NULL,id_partido INTEGER NOT NULL,amarillas INTEGER,rojas INTEGER, goles INTEGER, faltas INTEGER, PRIMARY KEY(id_jugador,id_partido))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Jugador"); // Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Equipo");
        db.execSQL("DROP TABLE IF EXISTS Partido");
        db.execSQL("DROP TABLE IF EXISTS JuegaPartido");
        onCreate(db);
    }
    EstadisticasDB ayuda;
    SQLiteDatabase db;

    // Métodos para manejar la base de datos
    public void abrir(){
        ayuda = new EstadisticasDB(ctx);
        db = ayuda.getWritableDatabase();
    }
    public void cerrar(){
        db.close();
    }

    // Métodos para manipular datos

    public long registrarJugador (String nombre_equipo,String nombre, int dorsal){
        String[] columnas = new String[]{nombre_equipo};
        Cursor c = db.rawQuery("select id_equipo from Equipo where nombre_equipo=?",columnas);
        if (c != null){
            c.moveToFirst();
        }

        ContentValues valores = new ContentValues();
        valores.put("id_equipo", c.getInt(0));
        valores.put("nombre_jugador", nombre);
        valores.put("dorsal", dorsal);
        return db.insert("Jugador",null, valores);
    }
    public long registrarEquipo (String nombre){
        ContentValues valores = new ContentValues();
        valores.put("nombre_equipo", nombre);
        return db.insert("Equipo",null, valores);
    }
    public long registrarPartido (String fecha, int id_equipo_local, int id_equipo_visitante){
        ContentValues valores = new ContentValues();
        valores.put("fecha_partido", fecha);
        valores.put("id_equipo_local", id_equipo_local);
        valores.put("id_equipo_visitante", id_equipo_visitante);
        return db.insert("Partido",null, valores);
    }

    public long registrarJuegaPartido(int id_jugador, int id_partido, int amarillas, int rojas, int goles, int faltas) {
        ContentValues valores = new ContentValues();
        valores.put("id_jugador", id_jugador);
        valores.put("id_partido", id_partido);
        valores.put("amarillas", amarillas);
        valores.put("faltas", faltas);
        valores.put("rojas", rojas);
        valores.put("goles", goles);

        return db.insert("JuegaPartido",null, valores);
    }


    // local: true->equipo local false-> equipo visitante
    // accion: 0->gol 1->falta 2->amarilla 3->roja
    public int getEstadisticaJugador (int dorsal, int id_partido, boolean local,int accion) {
        String[] columnas = new String[] {String.valueOf(dorsal), String.valueOf(id_partido), String.valueOf(id_partido)};

        Cursor cursor = null;
        switch (accion){
            case 0:
                if (local)
                    cursor  = db.rawQuery("SELECT goles from JuegaPartido,Jugador, Partido WHERE Jugador.dorsal = ? and JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_local",columnas);
                else
                    cursor  = db.rawQuery("SELECT goles from JuegaPartido,Jugador, Partido WHERE Jugador.dorsal = ? and JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_visitante",columnas);
                break;
            case 1:
                if (local)
                    cursor  = db.rawQuery("SELECT faltas from JuegaPartido,Jugador, Partido WHERE Jugador.dorsal = ? and JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_local",columnas);
                else
                    cursor  = db.rawQuery("SELECT faltas from JuegaPartido,Jugador, Partido WHERE Jugador.dorsal = ? and JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_visitante",columnas);
                break;
            case 2:
                if (local)
                    cursor  = db.rawQuery("SELECT amarillas from JuegaPartido,Jugador, Partido WHERE Jugador.dorsal = ? and JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_local",columnas);
                else
                    cursor  = db.rawQuery("SELECT amarillas from JuegaPartido,Jugador, Partido WHERE Jugador.dorsal = ? and JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_visitante",columnas);
                break;
            case 3:
                if (local)
                    cursor  = db.rawQuery("SELECT rojas from JuegaPartido,Jugador, Partido WHERE Jugador.dorsal = ? and JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_local",columnas);
                else
                    cursor  = db.rawQuery("SELECT rojas from JuegaPartido,Jugador, Partido WHERE Jugador.dorsal = ? and JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_visitante",columnas);
                break;
        }

        if (cursor !=null) { cursor.moveToFirst(); }

        int output = cursor.getInt(0);

        return output;
    }
    // Incrementar goles, tarjetas ...
    public void anotarAccionJugador (int dorsal, int id_partido, boolean local, int accion) {

        int playerScore = getEstadisticaJugador(dorsal, id_partido, local, accion);
        int playerScoreInc = playerScore + 1;
        String[] columnas = new String[] { String.valueOf(playerScoreInc), String.valueOf(dorsal), String.valueOf(id_partido)};

        switch (accion) {
            case 0:
                if (local)
                    db.execSQL("UPDATE JuegaPartido SET goles = ? WHERE id_jugador IN (SELECT id_jugador FROM JUGADOR WHERE dorsal = ? and  id_equipo IN (SELECT id_equipo_local FROM Partido WHERE id_partido = ?))", columnas);
                else
                    db.execSQL("UPDATE JuegaPartido SET goles = ? WHERE id_jugador IN (SELECT id_jugador FROM JUGADOR WHERE dorsal = ? and  id_equipo IN (SELECT id_equipo_visitante FROM Partido WHERE id_partido = ?))", columnas);
                break;
            case 1:
                if (local)
                    db.execSQL("UPDATE JuegaPartido SET faltas = ? WHERE id_jugador IN (SELECT id_jugador FROM JUGADOR WHERE dorsal = ? and  id_equipo IN (SELECT id_equipo_local FROM Partido WHERE id_partido = ?))", columnas);
                else
                    db.execSQL("UPDATE JuegaPartido SET faltas = ? WHERE id_jugador IN (SELECT id_jugador FROM JUGADOR WHERE dorsal = ? and  id_equipo IN (SELECT id_equipo_visitante FROM Partido WHERE id_partido = ?))", columnas);
                break;
            case 2:
                if (local)
                    db.execSQL("UPDATE JuegaPartido SET amarillas = ? WHERE id_jugador IN (SELECT id_jugador FROM JUGADOR WHERE dorsal = ? and  id_equipo IN (SELECT id_equipo_local FROM Partido WHERE id_partido = ?))", columnas);
                else
                    db.execSQL("UPDATE JuegaPartido SET amarillas = ? WHERE id_jugador IN (SELECT id_jugador FROM JUGADOR WHERE dorsal = ? and  id_equipo IN (SELECT id_equipo_visitante FROM Partido WHERE id_partido = ?))", columnas);
                break;
            case 3:
                if (local)
                    db.execSQL("UPDATE JuegaPartido SET rojas = ? WHERE id_jugador IN (SELECT id_jugador FROM JUGADOR WHERE dorsal = ? and  id_equipo IN (SELECT id_equipo_local FROM Partido WHERE id_partido = ?))", columnas);
                else
                    db.execSQL("UPDATE JuegaPartido SET rojas = ? WHERE id_jugador IN (SELECT id_jugador FROM JUGADOR WHERE dorsal = ? and  id_equipo IN (SELECT id_equipo_visitante FROM Partido WHERE id_partido = ?))", columnas);
                break;

        }

    }



    public int getSumaEstadisticas (int id_partido, boolean local,int accion) {

        String[] columnas = new String[] {String.valueOf(id_partido), String.valueOf(id_partido)};
        Cursor cursor = null;
        switch (accion){
            case 0:
                if (local)
                    cursor  = db.rawQuery("SELECT sum(goles) from JuegaPartido, Jugador, Partido WHERE JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_local",columnas);
                else cursor  = db.rawQuery("SELECT sum(goles) from JuegaPartido, Jugador, Partido WHERE JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_visitante",columnas);
                break;
            case 1:
                if (local)
                    cursor  = db.rawQuery("SELECT sum(faltas) from JuegaPartido, Jugador, Partido WHERE JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_local",columnas);
                else cursor  = db.rawQuery("SELECT sum(faltas) from JuegaPartido, Jugador, Partido WHERE JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_visitante",columnas);
                break;
            case 2:
                if (local)
                    cursor  = db.rawQuery("SELECT sum(amarillas) from JuegaPartido, Jugador, Partido WHERE JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_local",columnas);
                else cursor  = db.rawQuery("SELECT sum(amarillas) from JuegaPartido, Jugador, Partido WHERE JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_visitante",columnas);
                break;
            case 3:
                if (local)
                    cursor  = db.rawQuery("SELECT sum(rojas) from JuegaPartido,Jugador, Partido WHERE JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_local",columnas);
                else cursor  = db.rawQuery("SELECT sum(rojas) from JuegaPartido,Jugador, Partido WHERE JuegaPartido.id_partido = ? and Partido.id_partido = ? and JuegaPartido.id_jugador = Jugador.id_jugador and Jugador.id_equipo = Partido.id_equipo_visitante",columnas);
                break;

        }
        if (cursor !=null) { cursor.moveToFirst(); }

        return cursor.getInt(0);
    }






}
