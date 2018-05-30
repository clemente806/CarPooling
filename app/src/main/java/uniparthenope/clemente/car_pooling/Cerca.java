package uniparthenope.clemente.car_pooling;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class Cerca extends AppCompatActivity {
    RelativeLayout.LayoutParams parms;

    ListView lista;
    private int[] listValue;
    ImageButton conferma;
    String selectedFromList;
    ArrayAdapter arryAdapter;
    private float corY, corX;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca);

        lista=(ListView)findViewById( R.id.Listlista );

        img = (ImageView) findViewById( R.id.img1 );

        lista.setCacheColorHint( 0 );
        lista.setBackgroundResource( R.drawable.car_pooling );
        lista.setTextFilterEnabled( true );

        conferma = (ImageButton) findViewById(R.id.buttonConferma);
        conferma.setBackgroundResource( R.drawable.ok );
        conferma.setVisibility( View.INVISIBLE );
        final ArrayList<String> arrayList = new ArrayList<>( );
        //Carico i dati
        load (arrayList);
        //Inserisco nella lista tutti i dati caricati
        arryAdapter = new ArrayAdapter( this, android.R.layout.simple_list_item_1,arrayList );
        lista.setAdapter( arryAdapter );
        lista.setCacheColorHint( 000000 );
        //Seleziona una riga della ListView
        lista.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                conferma.setVisibility( View.VISIBLE );
                selectedFromList =(String) (lista.getItemAtPosition(position));
                Toast imageToast = new Toast(getBaseContext());
                LinearLayout toastLayout = new LinearLayout( getBaseContext() );
               // toastLayout.setOrientation( LinearLayout.HORIZONTAL );
                ImageView imgToast = new ImageView( getBaseContext() );
                TextView text = new TextView( getBaseContext() );
                text.setText( "Per confermare premere" );
                imgToast.setImageResource( R.drawable.ok);
                toastLayout.addView( text );
                toastLayout.addView( imgToast );
                imageToast.setView( toastLayout );
                imageToast.setGravity( 0,0,700 );
                imageToast.setDuration( Toast.LENGTH_SHORT );
                imageToast.show();
                conferma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Prendo il valore dei posti
                        char c = selectedFromList.charAt(selectedFromList.length()-1);
                        //Considero la tabella ASCII per prendere il valore in maniere corretta(da qui il -48)
                        int postiLiberi = c - 48;
                        postiLiberi--;
                        //Rimuovo l'ultimo carattere dalla stringa ed aggiorno
                        selectedFromList = changeCh( selectedFromList, selectedFromList.length() - 1, postiLiberi );
                        if(postiLiberi==0){
                            arrayList.remove( position );
                        }
                        else {
                            arrayList.set( position, selectedFromList );
                        }
                        updateData(arrayList, position);
                        arryAdapter.notifyDataSetChanged();
                        conferma.setVisibility( View.INVISIBLE );
                        Toast.makeText(getApplicationContext(), "Prenotazione effettuata", Toast.LENGTH_SHORT).show();
                        ClickMoveDown();
                    }
                });
            }
        } );

        final ImageButton back = (ImageButton) findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    public void load(ArrayList arrayList) {
        BufferedReader read = null;
        ArrayList<String> corse = new ArrayList<String>(  );
        int index = 0;
        String[] parts = new String[0];
        try
        {
            FileInputStream fileInputStream = openFileInput( "mio_file.txt" );
            read = new BufferedReader( new InputStreamReader( fileInputStream ) );
            String temp = read.readLine();
            while (temp != null){
                parts = (temp.toString()).split(";");
                corse.add( temp );
                temp = read.readLine();
            }
            index = parts.length;

            for(int i = 0; i<index; i++){
                arrayList.add(parts[i]);
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void updateData(ArrayList arrayList, int posizione) {
        BufferedWriter writer = null;
        int size = arrayList.size();
        try {
        // Ora riscrivo tutto, tranne l'ultima riga
            FileOutputStream fOut = openFileOutput("mio_file.txt",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            writer = new BufferedWriter (new OutputStreamWriter( fOut ) );
            for(int i = 0; i<size; i++) {

                writer.write( arrayList.get( i ).toString() + ";" );
            }
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String changeCh (String s , int index, int posti) {
        if ((index > s.length()-1) || (index < 0)) return null;
        String c = s.substring(0,index) + posti;
        return c;
    }

    private void Move (float x, float y){
        img.animate().translationX(img.getTranslationX()+x).setDuration(1500);
    }

    public void ClickMoveDown(){
        float distance = 900;
        Move (distance,0);
    }
}
