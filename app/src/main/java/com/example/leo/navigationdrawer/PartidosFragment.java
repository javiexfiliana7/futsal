package com.example.leo.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PartidosFragment extends Fragment implements View.OnClickListener{
    Button registrar;
    EditText fecha;
    EstadisticasDB dbAyuda;
    Spinner local, visitante;
    protected static String equipo_visitante, equipo_local;
    int id_equipolocal, id_equipovisitante;
    protected static int partido=-1;
    long result;

    private OnFragmentInteractionListener mListener;


    public PartidosFragment() {
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
        View view = inflater.inflate(R.layout.fragment_partidos, container, false);
        dbAyuda = new EstadisticasDB(this.getActivity().getBaseContext());

        registrar = (Button) view.findViewById(R.id.button7);
        registrar.setOnClickListener(this);

        fecha = (EditText) view.findViewById(R.id.editText4);


        local = (Spinner) view.findViewById(R.id.spinner2);
        visitante = (Spinner) view.findViewById(R.id.spinner3);

        List<String> equipos = new ArrayList<String>();
        try {
            dbAyuda.abrir();
            SQLiteDatabase db = dbAyuda.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT nombre_equipo FROM Equipo", null);
            while (cur.moveToNext()) {
                equipos.add(cur.getString(0));
            }
            cur.close();
            db.close();
        } catch (Exception e) {

        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, equipos);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        local.setAdapter(arrayAdapter);// spinner se carga con adaptador
        local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                equipo_local = String.valueOf(local.getSelectedItem()); //Tenemos el nombre del equipo seleccionado
                dbAyuda.abrir();
                SQLiteDatabase db = dbAyuda.getReadableDatabase();
                String[] columnas = new String[]{equipo_local};
                Cursor c = db.rawQuery("select id_equipo from Equipo where nombre_equipo=?", columnas);
                if (c != null) {
                    c.moveToFirst();
                    id_equipolocal = c.getInt(0);
                }
                dbAyuda.cerrar();
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        visitante.setAdapter(arrayAdapter);// spinner se carga con adaptador
        visitante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                equipo_visitante = String.valueOf(visitante.getSelectedItem());
                dbAyuda.abrir();
                SQLiteDatabase db = dbAyuda.getReadableDatabase();
                String[] columnas = new String[]{equipo_visitante};
                Cursor c = db.rawQuery("select id_equipo from Equipo where nombre_equipo=?", columnas);
                if (c != null) {
                    c.moveToFirst();
                    id_equipovisitante = c.getInt(0);
                }
                dbAyuda.cerrar();
                db.close();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button7:
                try {
                    dbAyuda.abrir();
                    SQLiteDatabase db = dbAyuda.getReadableDatabase();
                    if (validaFechaMascara(fecha.getText().toString(),"dd/MM/yyyy")) {
                        result = dbAyuda.registrarPartido(fecha.getText().toString(), id_equipolocal, id_equipovisitante);// id_equipolocal y id_equipovisitante
                        String[] x = new String[]{String.valueOf(fecha.getText().toString()), String.valueOf(id_equipolocal), String.valueOf(id_equipovisitante)};
                        Cursor idp = db.rawQuery("SELECT id_partido FROM PARTIDO where fecha_partido=? and id_equipo_local=? and id_equipo_visitante=?", x);//obtener id partido

                        idp.moveToFirst();
                        partido = idp.getInt(0);


                        //registra juegapartido
                        String[] columnas = new String[]{String.valueOf(id_equipolocal), String.valueOf(id_equipovisitante)};
                        Cursor cur = db.rawQuery("SELECT id_jugador FROM JUGADOR where id_equipo=? or id_equipo=?", columnas);
                        if (cur.moveToFirst()) {
                            do {
                                dbAyuda.registrarJuegaPartido(cur.getInt(0), partido, 0, 0, 0, 0);
                            } while (cur.moveToNext());
                        }
                        cur.close();
                        db.close();
                        fecha.setText("");
                        if (result > 0) {
                            Toast t = Toast.makeText(getActivity(), "Partido insertado con éxito", Toast.LENGTH_LONG);
                            t.show();
                            Fragment fragment = new EstadisticasFragment();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.content_main, fragment)
                                    .commit();
                        }
                    }else{
                        Toast t = Toast.makeText(getActivity(), "La fecha insertadano no es correcta.", Toast.LENGTH_LONG);
                        t.show();
                    }
                    dbAyuda.cerrar();


                } catch (Exception e) {
                    break;
                }

        }
    }

    public static boolean validaFechaMascara(String sFecha, String sMascara)
    {
        boolean retorno = false;
        try {
            //Convertir la fecha al Calendar
            java.util.Locale locInstancia = new java.util.Locale("es","CL");
            java.text.DateFormat dfInstancia;
            java.util.Date dInstancia;
            dfInstancia = new java.text.SimpleDateFormat(sMascara,locInstancia);
            dInstancia = dfInstancia.parse(sFecha);
            java.util.Calendar cal = java.util.Calendar.getInstance(locInstancia);
            cal.setTime(dInstancia); //setear la fecha en cuestion al calendario
            retorno = true;
        } catch (java.text.ParseException excep) {
            retorno = false; //unparseable date
        } finally {
            return retorno;
        }
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
