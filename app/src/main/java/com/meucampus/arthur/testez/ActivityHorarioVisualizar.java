package com.meucampus.arthur.testez;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityHorarioVisualizar extends AppCompatActivity {

    String materia,materiaA;
    Button buttons[][]=new Button[6][5];
    int l;

    public static String[][] horarioManha = new String[6][5];
    public static List<Disciplina> disciplinas = new ArrayList<>();
    public static String url;
    public  static Horario horario = new Horario();

    @Override
    protected void onRestart() {
        finish();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Cria um metodo la no horarario service
        //ele vai receber um professor pelo json
        //e pelo path param, vc passa a turma (String)
        // &&
        //lembra de receber os dados do possivel professor pelo json
        //la no activity login e add no repositorio
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_visualizar);
        url = getResources().getString(R.string.url);
        Button btnTrocar = (Button) findViewById(R.id.buttonTrocar);
        RepositorioUsuario repo = new RepositorioUsuario();
        if (!repo.getTipo().equals("PROFESSOR")) {
            btnTrocar.setVisibility(View.INVISIBLE);
            Intent intent = getIntent();

            Log.e("A", " PROFESSOR 1");
            String turma = intent.getStringExtra("turma");
            horario.setTurma(turma);
            Log.e("TURMA", turma);


            new getHorario().execute(url + "horario/horario/" + turma + "");

        }else{
            Intent intent = getIntent();
            String turmaSelecionada = intent.getStringExtra("turma");
            Log.e("TURMA2", turmaSelecionada);
            RepositorioUsuario repo1 = new RepositorioUsuario();
            String matricula = repo1.getMatricula();
            Log.e("MATRICULA!!", matricula +"p");
            horario.setTurma(turmaSelecionada);
            new getHorario().execute(url+"horario/horario/"+turmaSelecionada+"");
            new GetTurmas().execute(url+ "horario/disciplina/" + turmaSelecionada +"/"+ matricula);
        }
        buttons[0][0] = (Button) findViewById(R.id.button);
        buttons[0][1] = (Button) findViewById(R.id.button2);
        buttons[0][2] = (Button) findViewById(R.id.button3);
        buttons[0][3] = (Button) findViewById(R.id.button4);
        buttons[0][4] = (Button) findViewById(R.id.button5);
        buttons[1][0] = (Button) findViewById(R.id.button6);
        buttons[1][1] = (Button) findViewById(R.id.button7);
        buttons[1][2] = (Button) findViewById(R.id.button8);
        buttons[1][3] = (Button) findViewById(R.id.button9);
        buttons[1][4] = (Button) findViewById(R.id.button10);
        buttons[2][0] = (Button) findViewById(R.id.button11);
        buttons[2][1] = (Button) findViewById(R.id.button12);
        buttons[2][2] = (Button) findViewById(R.id.button13);
        buttons[2][3] = (Button) findViewById(R.id.button14);
        buttons[2][4] = (Button) findViewById(R.id.button15);
        buttons[3][0] = (Button) findViewById(R.id.button16);
        buttons[3][1] = (Button) findViewById(R.id.button17);
        buttons[3][2] = (Button) findViewById(R.id.button18);
        buttons[3][3] = (Button) findViewById(R.id.button19);
        buttons[3][4] = (Button) findViewById(R.id.button20);
        buttons[4][0] = (Button) findViewById(R.id.button21);
        buttons[4][1] = (Button) findViewById(R.id.button22);
        buttons[4][2] = (Button) findViewById(R.id.button23);
        buttons[4][3] = (Button) findViewById(R.id.button24);
        buttons[4][4] = (Button) findViewById(R.id.button25);
        buttons[5][0] = (Button) findViewById(R.id.button26);
        buttons[5][1] = (Button) findViewById(R.id.button27);
        buttons[5][2] = (Button) findViewById(R.id.button28);
        buttons[5][3] = (Button) findViewById(R.id.button29);
        buttons[5][4] = (Button) findViewById(R.id.button30);
        materia = "PEOO";
        for (int i=0;i<6;i++){
            for (int t=0;t<5;t++){
                try {

                    buttons[i][t].setText(horarioManha[i][t]);
                }catch(Exception e){

                }
            }
        }
    }

    int posXA = -1; // o x representa o horario do professor
    int posYA = -1;
    int posXB = -1; // o y representa o horario a ser trocado
    int posYB = -1;
    public void clickMudar(int x, int y){
        contClicks++; // ímpar o horário é dele
        if (contClicks%2==0) {
            if (buttons[x][y].getText().toString().equals(materia)) {
                Toast.makeText(ActivityHorarioVisualizar.this, "Este horario já é seu", Toast.LENGTH_LONG).show();
                contClicks--;
            } else {
                posXB = x;
                posYB = y;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buttons[x][y].setBackgroundTintList(getResources().getColorStateList(R.color.horario1));
                }

            }
        }else{
            if (buttons[x][y].getText().toString().equals(materia)) {
                posXA = x;
                posYA = y;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    buttons[x][y].setBackgroundTintList(getResources().getColorStateList(R.color.horario2));
                //Coloca o aviso pra trocar
            }else{
                contClicks--;
                Toast.makeText(ActivityHorarioVisualizar.this, "Selecione um horário seu.", Toast.LENGTH_LONG).show();
            }
        }
    }
    int contClicks = 0;
    public void Mudar (View view) {
        clickMudar(0,0);
    }
    public void Mudar1 (View view) {
        clickMudar(0,1);
    }
    public void Mudar2 (View view) {
        clickMudar(0,2);
    }
    public void Mudar3 (View view) {
        clickMudar(0,3);

    }
    public void Mudar4 (View view) {
        clickMudar(0,4);
    }
    public void Mudar5 (View view) {
        clickMudar(1,0);
    }
    public void Mudar6 (View view) {
        clickMudar(1,1);
    }
    public void Mudar7 (View view) {
        clickMudar(1,2);
    }
    public void Mudar8 (View view) {
        clickMudar(1,3);
    }
    public void Mudar9 (View view) {
        clickMudar(1,4);
    }
    public void Mudar10 (View view) {
        clickMudar(2,0);
    }
    public void Mudar11 (View view) {
        clickMudar(2,1);
    }
    public void Mudar12 (View view) {
        clickMudar(2,2);
    }
    public void Mudar13 (View view) {
        clickMudar(2,3);
    }
    public void Mudar14 (View view) {
        clickMudar(2,4);
    }
    public void Mudar15 (View view) {
        clickMudar(3,0);
    }
    public void Mudar16 (View view) {
        clickMudar(3,1);
    }
    public void Mudar17 (View view) {
        clickMudar(3,2);
    }
    public void Mudar18 (View view) {
        clickMudar(3,3);
    }
    public void Mudar19 (View view) {
        clickMudar(3,4);
    }
    public void Mudar20 (View view) {
        clickMudar(4,0);
    }
    public void Mudar21 (View view) {
        clickMudar(4,1);
    }
    public void Mudar22 (View view) {
        clickMudar(4,2);
    }
    public void Mudar23 (View view) {
        clickMudar(4,3);
    }
    public void Mudar24 (View view) {

        clickMudar(4,4);
    }
    public void Mudar25 (View view) {
        clickMudar(5,0);
    }
    public void Mudar26 (View view) {
        clickMudar(5,1);
    }
    public void Mudar27 (View view) {
        clickMudar(5,2);
    }
    public void Mudar28 (View view) {
        clickMudar(5,3);
    }
    public void Mudar29 (View view) {
        clickMudar(5,4);
    }
    public void TrocarDeHorario(View view) {
        if(posXA != -1 && posXB != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Troca");
            builder.setMessage("Deseja realmente efetuar a troca de seu horario com " + horarioManha[posXB][posYB] + "?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String a = horarioManha[posXA][posYA];
                    String b = horarioManha[posXB][posYB];
                    horarioManha[posXB][posYB] = a;
                    horarioManha[posXA][posYA] = b;


                    NotificationManager nM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    PendingIntent p = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), ActivityHorarioVisualizar.class), 0);
                    Grade g = new Grade();
                    g.setHorarioManha(horarioManha);
                    horario.setGrade(g);
                    new AlterarHorario().execute(horario);
                    NotificationCompat.Builder builder1 = new NotificationCompat.Builder(getApplicationContext());
                    builder1.setTicker("Meu campus");
                    builder1.setContentTitle("Meu campus");
                    builder1.setContentText("Troca do horario de " + materiaA + " por " + materia);
                    builder1.setSmallIcon(R.mipmap.ic_launcher);
                    builder1.setContentIntent(p);

                    Notification n = builder1.build();
                    n.flags = Notification.FLAG_AUTO_CANCEL;
                    nM.notify(R.mipmap.ic_launcher, n);

                    try {
                        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone toque = RingtoneManager.getRingtone(getApplicationContext(), som);
                        toque.play();
                    } catch (Exception e) {

                    }
                    finish();
                    Toast.makeText(ActivityHorarioVisualizar.this ,"Operação concluída", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recreate();

                }
            });
            builder.show();
        }else{
            Toast.makeText(ActivityHorarioVisualizar.this ,"Horario vazio", Toast.LENGTH_LONG).show();
        }


    }
    private class getHorario extends AsyncTask<String, Void, String[][]>{
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        BufferedWriter bw = null;
        @Override
        protected String[][] doInBackground(String... strings) {

                Log.e("BACKGROUND", "INCICIO");

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Gson gson = new Gson();


                Log.e("JSON", data.toString() + "");
                horarioManha = gson.fromJson(data.toString(), Grade.class).getHorarioManha();


                connection.disconnect();
            }catch(Exception e){

            }
            return horarioManha;
        }

        @Override
        protected void onPostExecute(String[][] strings) {
            Log.e("NA POS 0 0 ", strings[0][0]+"");
        }
    }
    private class GetTurmas extends  AsyncTask<String, Void, String>{
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Gson gson = new Gson();
                JSONArray jsonArray = new JSONArray(data.toString());
                disciplinas = new ArrayList<>();
                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Disciplina disciplina = gson.fromJson(jsonObject.toString(), Disciplina.class);
                    disciplinas.add(disciplina);
                }

                connection.disconnect();
            }catch(Exception e){

            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("DATA TURMAS", s);
        }
    }
    private class AlterarHorario extends AsyncTask<Horario, Void, String>{
        BufferedWriter bw = null;
        @Override
        protected String doInBackground(Horario... horarios) {
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL(url+"horario/alterar").openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(new Gson().toJson(horarios));
                Log.e("JSONNN", jsonObject.toString());
                bw.write(String.valueOf(jsonObject));
                bw.flush();
                connection.disconnect();
            }catch (Exception e){

            }
            return null;
        }
    }
}

