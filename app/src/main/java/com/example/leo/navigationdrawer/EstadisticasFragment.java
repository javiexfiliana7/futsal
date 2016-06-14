package com.example.leo.navigationdrawer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class EstadisticasFragment extends Fragment implements View.OnClickListener {
    TextView tvGolesLocal, tvGolesVisitante, tvAmarillasLocal, tvAmarillasVisitante, tvRojasLocal, tvRojasVisitante, tvFaltasLocal, tvFaltasVisitante, local, visitante;
    RadioButton r1, r2, r3, r4;
    Button iniciar, pausa, reiniciar;
    Button btLocal1,btLocal2,btLocal3,btLocal4,btLocal5;
    Button btVisitante1,btVisitante2,btVisitante3,btVisitante4,btVisitante5;
    Button estadisticasIndividuales;
    Chronometer crono;
    int partido;


    EstadisticasDB dbAyuda;
    ArrayList<String> text;
    ArrayList<String> mArrayList = new ArrayList<String>();
    ArrayList<String> nArrayList = new ArrayList<String>();
    String delimitadores = "[ .,;?!¡¿\'\"\\[\\]]+";
    boolean l_v = false;
    int accion = -1;
    int dorsalaint = -1;
    Cursor c_lo = null;
    Cursor c_vi = null;
    //private GoogleApiClient client;


    private OnFragmentInteractionListener mListener;


    public EstadisticasFragment() {
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
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        partido = PartidosFragment.partido;
        dbAyuda = new EstadisticasDB(this.getActivity().getBaseContext());
        local = (TextView) view.findViewById(R.id.textView12);
        visitante = (TextView) view.findViewById(R.id.textView14);
        estadisticasIndividuales=(Button) view.findViewById(R.id.button);
        estadisticasIndividuales.setOnClickListener(this);


        ImageButton reconoce = (ImageButton) view.findViewById(R.id.imageButton3);
        assert reconoce != null;

        reconoce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Este es el intent del reconocimiento de voz
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //Especificamos el idioma, en esta ocasión probé con el de Estados Unidos
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //Iniciamos la actividad dentro de un Try en caso surja un error.
                try {
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText( getContext().getApplicationContext(), "Tu dispositivo no soporta el reconocimiento de voz", Toast.LENGTH_LONG).show();
                }

            }


        });
        btLocal1=(Button) view.findViewById(R.id.btLocal1);
        assert btLocal1 != null;
        btLocal1.setOnClickListener(this);
        btLocal2=(Button) view.findViewById(R.id.btLocal2);
        assert btLocal2 != null;
        btLocal2.setOnClickListener(this);
        btLocal3=(Button) view.findViewById(R.id.btLocal3);
        assert btLocal3 != null;
        btLocal3.setOnClickListener(this);
        btLocal4=(Button) view.findViewById(R.id.btLocal4);
        assert btLocal4 != null;
        btLocal4.setOnClickListener(this);
        btLocal5=(Button) view.findViewById(R.id.btLocal5);
        assert btLocal5 != null;
        btLocal5.setOnClickListener(this);
        btVisitante1=(Button) view.findViewById(R.id.btVisitante1);
        assert btVisitante1 != null;
        btVisitante1.setOnClickListener(this);
        btVisitante2=(Button) view.findViewById(R.id.btVisitante2);
        assert btVisitante2 != null;
        btVisitante2.setOnClickListener(this);
        btVisitante3=(Button) view.findViewById(R.id.btVisitante3);
        assert btVisitante3 != null;
        btVisitante3.setOnClickListener(this);
        btVisitante4=(Button) view.findViewById(R.id.btVisitante4);
        assert btVisitante4 != null;
        btVisitante4.setOnClickListener(this);
        btVisitante5=(Button) view.findViewById(R.id.btVisitante5);
        assert btVisitante5 != null;
        btVisitante5.setOnClickListener(this);

        dbAyuda.abrir();
        SQLiteDatabase db = dbAyuda.getReadableDatabase();
        String[] x = new String[]{String.valueOf(partido), String.valueOf(partido)};



        c_lo = db.rawQuery(" SELECT dorsal from JuegaPartido, Jugador, Partido WHERE  Jugador.id_jugador = JuegaPartido.id_jugador and Partido.id_partido = ? and JuegaPartido.id_partido = ? and Partido.id_equipo_local = Jugador.id_equipo", x);
        c_lo.moveToFirst();

        if (c_lo != null && c_lo.getCount()!=0) {
            do {
                mArrayList.add(c_lo.getString(c_lo.getColumnIndex("dorsal"))); //add the item
            }while(c_lo.moveToNext());
            btLocal1.setText(mArrayList.get(0));
            btLocal2.setText(mArrayList.get(1));
            btLocal3.setText(mArrayList.get(2));
            btLocal4.setText(mArrayList.get(3));
            btLocal5.setText(mArrayList.get(4));
        }


        c_vi = db.rawQuery(" SELECT dorsal from JuegaPartido, Jugador, Partido WHERE  Jugador.id_jugador = JuegaPartido.id_jugador and Partido.id_partido = ? and JuegaPartido.id_partido = ? and Partido.id_equipo_visitante = Jugador.id_equipo", x);
        c_vi.moveToFirst();

        if (c_vi != null && c_vi.getCount()!=0) {
            do {
                nArrayList.add(c_vi.getString(c_vi.getColumnIndex("dorsal"))); //add the item
            }while(c_vi.moveToNext());
            btVisitante1.setText(nArrayList.get(0));
            btVisitante2.setText(nArrayList.get(1));
            btVisitante3.setText(nArrayList.get(2));
            btVisitante4.setText(nArrayList.get(3));
            btVisitante5.setText(nArrayList.get(4));
        }




        tvGolesLocal = (TextView) view.findViewById(R.id.tvGolesLocal);
        tvGolesVisitante = (TextView) view.findViewById(R.id.tvGolesVisitante);
        tvAmarillasLocal = (TextView) view.findViewById(R.id.tvAmarillasLocal);
        tvAmarillasVisitante = (TextView) view.findViewById(R.id.tvAmarillasVisitante);
        tvRojasLocal = (TextView) view.findViewById(R.id.tvRojasLocal);
        tvRojasVisitante = (TextView) view.findViewById(R.id.tvRojasVisitante);
        tvFaltasLocal = (TextView) view.findViewById(R.id.tvFaltasLocal);
        tvFaltasVisitante = (TextView) view.findViewById(R.id.tvFaltasVisitante);

        r1 = (RadioButton) view.findViewById(R.id.r1);
        r2 = (RadioButton) view.findViewById(R.id.r2);
        r3 = (RadioButton) view.findViewById(R.id.r3);
        r4 = (RadioButton) view.findViewById(R.id.r4);

        actualizarGoles();
        actualizarFaltas();
        actualizarAmarillas();
        actualizarRojas();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    public void onActivityResult(int requestcode, int resultcode, Intent datos) {
        dbAyuda.abrir();

// Si el reconocimiento de voz es correcto almacenamos dentro de un array los datos obtenidos.
//Mostramos en pantalla el texto obtenido de la posición 0.
        if (resultcode == Activity.RESULT_OK && datos != null) {
            text = datos.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String cadena = text.get(0);
            String[] palabrasSeparadas = cadena.split(delimitadores);

            Toast.makeText(this.getActivity(), text.get(0), Toast.LENGTH_LONG).show();
            if (palabrasSeparadas.length != 3) {
                Toast.makeText(this.getActivity(), "DIGA: ACCION - LOCAL o VISITANTE - DORSAL", Toast.LENGTH_LONG).show();
            } else {
/*
                Toast.makeText(this, palabrasSeparadas[0], Toast.LENGTH_LONG).show();
                Toast.makeText(this, palabrasSeparadas[1], Toast.LENGTH_LONG).show();
                Toast.makeText(this, palabrasSeparadas[2], Toast.LENGTH_LONG).show();
*/
                //ACCION
                if (palabrasSeparadas[0].equals("gol") || palabrasSeparadas[0].equals("Gol"))
                    accion = 0;
                else if (palabrasSeparadas[0].equals("falta") || palabrasSeparadas[0].equals("Falta")) {
                    accion = 1;
                } else if (palabrasSeparadas[0].equals("amarilla") || palabrasSeparadas[0].equals("Amarilla")) {
                    accion = 2;
                } else if (palabrasSeparadas[0].equals("roja") || palabrasSeparadas[0].equals("Roja")) {
                    accion = 3;
                } else Toast.makeText( this.getActivity(), "La accion no es correcta", Toast.LENGTH_LONG).show();


                SQLiteDatabase db = dbAyuda.getReadableDatabase();
                String[] x = new String[]{palabrasSeparadas[2], String.valueOf(partido), String.valueOf(partido)};
                Cursor idp = null;

                //LOCAL O VISITANTE
                if (palabrasSeparadas[1].equals("local")) {
                    l_v = true;
                    idp = db.rawQuery("SELECT * from JuegaPartido, Jugador, Partido WHERE Jugador.dorsal = ? and Jugador.id_jugador = JuegaPartido.id_jugador and Partido.id_partido = ? and JuegaPartido.id_partido = ? and Partido.id_equipo_local = Jugador.id_equipo", x);
                } else if (palabrasSeparadas[1].equals("visitante")) {
                    l_v = false;
                    idp = db.rawQuery("SELECT * from JuegaPartido, Jugador, Partido WHERE Jugador.dorsal = ? and Jugador.id_jugador = JuegaPartido.id_jugador and Partido.id_partido = ? and JuegaPartido.id_partido = ? and Partido.id_equipo_visitante = Jugador.id_equipo", x);
                } else Toast.makeText( this.getActivity(), "Elija local o visitante", Toast.LENGTH_LONG).show();

                //DORSAL
                dorsalaint = Integer.parseInt(palabrasSeparadas[2]);

                assert idp != null;
                if (accion != -1 && idp.getCount() == 1) {
                    dbAyuda.anotarAccionJugador(dorsalaint, partido, l_v, accion);//dorsal 1, partido 1, local, apunta gol
                    actualizarGoles();
                    actualizarAmarillas();
                    actualizarFaltas();
                    actualizarRojas();
                    accion = -1;
                } else Toast.makeText(this.getActivity(), "La accion no es correcta", Toast.LENGTH_LONG).show();
                db.close();

                idp.close();
            }

        }
        dbAyuda.cerrar();

    }

    @Override
    public void onClick(View v) {
        dbAyuda.abrir();

        switch (v.getId()) {
            case R.id.btLocal1:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(0)), partido, true, 0);//dorsal 1, partido 1, local, apunta gol
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(0)), partido, true, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(0)), partido, true, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(0)), partido, true, 3);
                    actualizarRojas();
                }


                break;
            case R.id.btLocal2:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(1)), partido, true, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(1)), partido, true, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(1)), partido, true, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(1)), partido, true, 3);
                    actualizarRojas();
                }

                break;
            case R.id.btLocal3:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(2)), partido, true, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(2)), partido, true, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(2)), partido, true, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(2)), partido, true, 3);
                    actualizarRojas();
                }

                break;
            case R.id.btLocal4:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(3)), partido, true, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(3)), partido, true, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(3)), partido, true, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(3)), partido, true, 3);
                    actualizarRojas();
                }
                break;
            case R.id.btLocal5:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(4)), partido, true, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(4)), partido, true, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(4)), partido, true, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(mArrayList.get(4)), partido, true, 3);
                    actualizarRojas();
                }
                break;
            case R.id.btVisitante1:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(0)), partido, false, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(0)), partido, false, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(0)), partido, false, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(0)), partido, false, 3);
                    actualizarRojas();
                }
                break;
            case R.id.btVisitante2:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(1)), partido, false, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(1)), partido, false, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(1)), partido, false, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(1)), partido, false, 3);
                    actualizarRojas();
                }
                break;
            case R.id.btVisitante3:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(2)), partido, false, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(1)), partido, false, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(1)), partido, false, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(1)), partido, false, 3);
                    actualizarRojas();
                }
                break;
            case R.id.btVisitante4:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(3)), partido, false, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(3)), partido, false, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(3)), partido, false, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(3)), partido, false, 3);
                    actualizarRojas();
                }
                break;
            case R.id.btVisitante5:
                if (r1.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(4)), partido, false, 0);
                    actualizarGoles();
                } else if (r2.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(4)), partido, false, 1);
                    actualizarFaltas();
                } else if (r3.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(4)), partido, false, 2);
                    actualizarAmarillas();
                } else if (r4.isChecked()) {
                    dbAyuda.anotarAccionJugador(Integer.parseInt(nArrayList.get(4)), partido, false, 3);
                    actualizarRojas();
                }
                break;
            case R.id.button:
                //Intent intent = new Intent(this, Main6Activity.class);
                //startActivity(intent);
                Fragment fragment = new Estadisticas2Fragment();
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

    public void actualizarGoles() {
        dbAyuda.abrir();
        int local = dbAyuda.getSumaEstadisticas(partido, true, 0);
        int visitante = dbAyuda.getSumaEstadisticas(partido, false, 0);
        tvGolesLocal.setText(String.valueOf(local));
        tvGolesVisitante.setText(String.valueOf(visitante));
        dbAyuda.cerrar();

    }

    public void actualizarFaltas() {
        dbAyuda.abrir();
        int local = dbAyuda.getSumaEstadisticas(partido, true, 1);
        int visitante = dbAyuda.getSumaEstadisticas(partido, false, 1);
        tvFaltasLocal.setText(String.valueOf(local));
        tvFaltasVisitante.setText(String.valueOf(visitante));
        dbAyuda.cerrar();

    }

    public void actualizarAmarillas() {
        dbAyuda.abrir();
        int local = dbAyuda.getSumaEstadisticas(partido, true, 2);
        int visitante = dbAyuda.getSumaEstadisticas(partido, false, 2);
        tvAmarillasLocal.setText(String.valueOf(local));
        tvAmarillasVisitante.setText(String.valueOf(visitante));
        dbAyuda.cerrar();

    }

    public void actualizarRojas() {
        dbAyuda.abrir();
        int local = dbAyuda.getSumaEstadisticas(partido, true, 3);
        int visitante = dbAyuda.getSumaEstadisticas(partido, false, 3);
        tvRojasLocal.setText(String.valueOf(local));
        tvRojasVisitante.setText(String.valueOf(visitante));
        dbAyuda.cerrar();

    }


}