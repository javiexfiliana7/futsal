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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JugadoresFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class JugadoresFragment extends Fragment implements View.OnClickListener {
    Button boton,boton2;
    EditText jugadorl,dorsall;
    EstadisticasDB dbAyuda;
    Spinner spinner;
    ArrayList<String> m = new ArrayList<String>();
    TextView j1,j2,j3,j4,j5;
    long result;
    int n_jug=-1;
    int id_equipo;

    private OnFragmentInteractionListener mListener;

    public JugadoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jugadores, container, false);


        dbAyuda = new EstadisticasDB(this.getActivity().getBaseContext());
        boton = (Button) view.findViewById(R.id.button3);
        assert boton != null;
        boton.setOnClickListener(this);

        boton2 = (Button) view.findViewById(R.id.button2);
        assert boton2 != null;
        boton2.setOnClickListener(this);

        jugadorl = (EditText) view.findViewById(R.id.editText);
        dorsall = (EditText) view.findViewById(R.id.editText2);

        j1=(TextView) view.findViewById(R.id.textView17);
        j2=(TextView) view.findViewById(R.id.textView18);
        j3=(TextView) view.findViewById(R.id.textView19);
        j4=(TextView) view.findViewById(R.id.textView20);
        j5=(TextView) view.findViewById(R.id.textView21);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        spinner = (Spinner) view.findViewById(R.id.spinner);
        List<String> lista = new ArrayList<String>();
        try {
            dbAyuda.abrir();
            SQLiteDatabase db = dbAyuda.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT nombre_equipo FROM Equipo",null);
            cur.moveToFirst();
            do {
                lista.add(cur.getString(0));
            }while(cur.moveToNext());
            cur.close();
            db.close();
        }catch (Exception e){

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item,lista);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);// spinner se carga con adaptador
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarJugadores();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //obtenemos el id del equipo en el spinner
        dbAyuda.abrir();
        SQLiteDatabase db = dbAyuda.getReadableDatabase();
        String[] x = new String[]{String.valueOf(spinner.getSelectedItem())};
        Cursor d = db.rawQuery("SELECT id_equipo FROM Equipo where nombre_equipo=?", x);
        d.moveToFirst();
        id_equipo=d.getInt(0);
        d.close();
        db.close();
        return view;
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
        dbAyuda.abrir();
        SQLiteDatabase db = dbAyuda.getReadableDatabase();
        String[] x;
        switch (v.getId()) {
            case R.id.button3:
                try {
                    if (n_jug == 5) {
                        Toast.makeText(getActivity(), "El equipo seleccionado ya tiene el máximo de jugadores", Toast.LENGTH_SHORT).show();
                    } else {
                        result = dbAyuda.registrarJugador(String.valueOf(spinner.getSelectedItem()), jugadorl.getText().toString(), Integer.parseInt(dorsall.getText().toString()));
                        jugadorl.setText("");
                        dorsall.setText("");
                        if (result > 0) {
                            Toast t = Toast.makeText(getActivity(), "Valores insertadoos con éxito", Toast.LENGTH_LONG);
                            t.show();
                        }
                    }
                } catch (Exception e) {
                    Toast t = Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
                    t.show();
                    e.printStackTrace();
                }
                actualizarJugadores();
                break;
            case R.id.button2:
                //Intent intent = new Intent(this, Main6Activity.class);
                //startActivity(intent);
                Fragment fragment = new Jugadores2Fragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment)
                        .commit();
                break;
        }

        dbAyuda.cerrar();
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

    public void actualizarJugadores(){
        dbAyuda.abrir();
        SQLiteDatabase db = dbAyuda.getReadableDatabase();
        String[] x = new String[]{String.valueOf(spinner.getSelectedItem())};

        Cursor c = db.rawQuery("SELECT nombre_jugador from Jugador, Equipo WHERE  Jugador.id_equipo = Equipo.id_equipo and Equipo.nombre_equipo=?", x);
        c.moveToFirst();

        if (c != null) {
            do {
                if (c.getCount()!=0)
                    m.add(c.getString(c.getColumnIndex("nombre_jugador"))); //add the item
            } while (c.moveToNext());
            Toast.makeText(getActivity(), String.valueOf(m.size()), Toast.LENGTH_SHORT).show();
            n_jug=m.size();
            if (m.size()==0){
                j1.setText("");
                j2.setText("");
                j3.setText("");
                j4.setText("");
                j5.setText("");
            } else if (m.size() == 1){
                j1.setText(m.get(0));
                j2.setText("");
                j3.setText("");
                j4.setText("");
                j5.setText("");
            } else if (m.size() == 2) {
                j1.setText(m.get(0));
                j2.setText(m.get(1));
                j3.setText("");
                j4.setText("");
                j5.setText("");
            } else if (m.size() == 3) {
                j1.setText(m.get(0));
                j2.setText(m.get(1));
                j3.setText(m.get(2));
                j4.setText("");
                j5.setText("");
            } else if (m.size() == 4) {
                j1.setText(m.get(0));
                j2.setText(m.get(1));
                j3.setText(m.get(2));
                j4.setText(m.get(3));
                j5.setText("");
            } else if (m.size() == 5) {
                j1.setText(m.get(0));
                j2.setText(m.get(1));
                j3.setText(m.get(2));
                j4.setText(m.get(3));
                j5.setText(m.get(4));
            }
            m.clear();
        }
        dbAyuda.cerrar();

    }

}
