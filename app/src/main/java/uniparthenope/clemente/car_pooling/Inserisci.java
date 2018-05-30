package uniparthenope.clemente.car_pooling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class Inserisci extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton conferma;
        ImageButton back;
        setContentView(R.layout.activity_inserisci);

        CalendarView seleziona = (CalendarView)findViewById(R.id.Calendario);
        seleziona.setVisibility(View.INVISIBLE);

        final Switch switch1 = (Switch)findViewById(R.id.switchData) ;
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CalendarView seleziona = (CalendarView)findViewById(R.id.Calendario);

                if(b){
                    seleziona.setVisibility(View.VISIBLE);
                    seleziona.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                            TextView Data = (TextView) findViewById(R.id.textData);
                            Data.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" +String.valueOf(year));
                            switch1.setText(Data.getText().toString());
                        }});
                }
                else{
                    seleziona.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Button Conferma
        conferma = (ImageButton)findViewById(R.id.buttonConferma);
        conferma.setBackgroundResource( R.drawable.ok );
        conferma.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                ImageView imgToast = new ImageView( getBaseContext() );
                EditText Nome = (EditText)findViewById(R.id.textNome);
                EditText Partenza = (EditText)findViewById(R.id.textPartenza);
                EditText Arrivo = (EditText)findViewById(R.id.textArrivo);
                EditText Costo = (EditText)findViewById(R.id.textCosto);
                EditText Ora = (EditText)findViewById(R.id.textTime);
                EditText Posti = (EditText)findViewById(R.id.textPosti);
                TextView Data = (TextView) findViewById(R.id.textData);
                String nome = Nome.getText().toString();
                String partenza = Partenza.getText().toString();
                String arrivo = Arrivo.getText().toString();

                //Controllo i capi dove non devono essere presenti caratteri
                if(cercaCaratteri(Costo) == true || cercaCaratteri(Posti) == true){
                    Toast.makeText(getApplicationContext(), "Dati inseriti non corretti",Toast.LENGTH_LONG).show();
                }
                //Controllo se è stata inserita la data
                else if(Data.getText().toString()==""){
                    Toast.makeText(getApplicationContext(), "Inserisci una data di partenza",Toast.LENGTH_LONG).show();
                }

                else if((Integer.parseInt( Posti.getText().toString())) > 9){
                    Toast.makeText(getApplicationContext(), "Numero posti troppo alto",Toast.LENGTH_LONG).show();

                }
                //Se arrivo qui sono stati inseriti tutti i dati in maniera corretta
                else {
                    int costo = Integer.parseInt(Costo.getText().toString());
                    String ora = Ora.getText().toString();
                    String posti = Posti.getText().toString();
                    String corsaCompleta = "Nome: " + nome + " Partenza: " + partenza + " Arrivo: " + arrivo + " Data: " + Data.getText().toString() + " Ora: " + ora + " Costo: " + costo + "$ Posti: " + posti + ";";
                    //Salvo
                    onClickSalva(corsaCompleta);

                }
            }
        });

        //Button Back
        back = (ImageButton) findViewById(R.id.buttonBack);
        back.setBackgroundResource( R.drawable.back );
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    //Questa funzione mi permette di verificare la predenza di caratteri
    public static boolean cercaCaratteri(EditText text)
    {
        Scanner scan = new Scanner(System.in);
        int n = -1;
        String stringa = text.getText().toString();

        try
        {
            n=Integer.parseInt(stringa);
        }catch(NumberFormatException e) {
            System.out.print(e);
        }
        if(n == -1) {
            return true;//Ritorna TRUE se contiene almeno un carattere
        }
        else
        {
            return false;//Ritorna FALSE se non sono presenti caratteri
        }
    }

    public void onClickSalva(String corsa) {
        BufferedWriter writer = null;
        try
        {
            FileOutputStream fOut = openFileOutput("mio_file.txt",MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            writer = new BufferedWriter (new OutputStreamWriter( fOut ) );
            writer.write( corsa );
            writer.flush();

            Toast.makeText(getBaseContext(),
                    "Dati salvati correttamente.",
                    Toast.LENGTH_SHORT).show();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}

