package com.example.leo.navigationdrawer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Estadisticas2Fragment extends Fragment {
    EstadisticasDB dbAyuda;
    Spinner spinner;
    TextView textView;
    List<String> lista;
    List<Integer> lista2;

    private OnFragmentInteractionListener mListener;


    public Estadisticas2Fragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estadisticas2, container, false);
        dbAyuda = new EstadisticasDB(this.getActivity().getBaseContext());
        textView = (TextView) view.findViewById(R.id.goles);
        spinner = (Spinner)  view.findViewById(R.id.spinner4);
        lista = new ArrayList<String>();
        lista2 = new ArrayList<Integer>();


        try {
            dbAyuda.abrir();
            SQLiteDatabase db = dbAyuda.getReadableDatabase();
            int partido = PartidosFragment.partido;

            Cursor cur = db.rawQuery("SELECT Jugador.id_jugador,nombre_jugador FROM Jugador, JuegaPartido WHERE JuegaPartido.id_partido=? and Jugador.id_jugador=JuegaPartido.id_jugador",new String[]{String.valueOf(partido)});
            while(cur.moveToNext()){

                lista2.add(Integer.parseInt(cur.getString(0)));
                lista.add(cur.getString(1));
            }
            cur.close();
            db.close();
        }catch (Exception e){

        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.spinner_item,lista);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);// spinner se carga con adaptador
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_SHORT).show();

                dbAyuda.abrir();
                int indice = (int) spinner.getSelectedItemId();
                int idjugador = lista2.get(indice);
                int golesjugador = 0;
                try {
                    dbAyuda.abrir();
                    SQLiteDatabase db = dbAyuda.getReadableDatabase();
                    int partido = PartidosFragment.partido;
                    String[] columnas = new String[]{String.valueOf(partido), String.valueOf(idjugador)};
                    Cursor cur = db.rawQuery("SELECT goles FROM Jugador, JuegaPartido WHERE JuegaPartido.id_partido=? and Jugador.id_jugador=JuegaPartido.id_jugador and Jugador.id_jugador=?", columnas);
                    while(cur.moveToNext()){
                        golesjugador = cur.getInt(0);
                    }
                    cur.close();
                    db.close();
                }catch (Exception e){

                }
                textView.setText( String.valueOf(golesjugador) );

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}