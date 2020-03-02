package com.meucampus.arthur.testez.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.meucampus.arthur.testez.ActivityLogin;
import com.meucampus.arthur.testez.R;
import com.meucampus.arthur.testez.RepositorioUsuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 01/02/2018.
 */

public class ServicoMaterial extends Service {

    SharedPreferences sharedPref;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("Onstratate", "Teste");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e("Oncreate", "Teste");
        new GetTurmas().execute();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public List<String> codigosTurmas = new ArrayList<>();
    private class GetTurmas extends AsyncTask<String, Void, String> {
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        List<String> objetosJson = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {

            try{

                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/2017/1/").openConnection();

                connection.setRequestProperty("Authorization", "JWT "+new RepositorioUsuario().getToken());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(false);
                connection.setDoInput(true);


                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Log.e("DATA", data.toString()+"");
                Log.e("CODE", connection.getResponseCode()+"");
                connection.disconnect();
            }catch(Exception e){

                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("DATAPOST", s);
            RepositorioUsuario repo = new RepositorioUsuario();

            JSONArray json;
            try{
                json = new JSONArray(s);
                for (int i = 0; i<(json.length()); i++){
                    Log.e("JSON: ", json.getJSONObject(i).toString());
                    objetosJson.add(json.getJSONObject(i).toString());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            String codigoDiario ="";
            try {
                for(int i = 0; i<objetosJson.size(); i++) {
                    JSONObject jsonObject = new JSONObject(objetosJson.get(i));
                    codigoDiario = jsonObject.getString("codigo_diario");
                    codigosTurmas.add(codigoDiario);


                    Log.e("Service: CODIGO: ", codigoDiario);
                }
                //1 2 3 2
                for(int i = 0; i<objetosJson.size();i++){
                    Log.e("ObjetosJson", i+".");
                    for(int j = 0; j<i; j++){
                        if(objetosJson.get(i).equals(objetosJson.get(j))){
                            Log.e("IFFOR", objetosJson.get(i));
                            Log.e("IFFOR", objetosJson.get(j));
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            new GetTurmasDetalhes().execute();
        }

    }
    public void notificacao(String titulo, String tipo){
        final android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker("tipo")
                .setSmallIcon(R.drawable.ic_noticias)
                .setContentTitle(tipo)
                .setContentText(titulo)
                .setAutoCancel(true);
//        Log.e("TITULO", titulo);
        Intent intent = new Intent(this, ActivityLogin.class);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

        builder.setPriority(android.support.v4.app.NotificationCompat.PRIORITY_HIGH);
        builder.setVisibility(android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC);


        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(som);
        } catch (Exception e) {

        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());


    }
    int count = 0;

    private class GetTurmasDetalhes extends AsyncTask<String, Void, String> {
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;


        @Override
        protected String doInBackground(String... strings) {

            try{
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/turma-virtual/"+codigosTurmas.get(count)).openConnection();
                connection.setRequestProperty("Authorization", "JWT "+new RepositorioUsuario().getToken());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(false);
                connection.setDoInput(true);

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                connection.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            RepositorioUsuario repo = new RepositorioUsuario();

            JSONObject json;
            JSONArray jsonArray = new JSONArray();
            try{
                json = new JSONObject(s);
                jsonArray = json.getJSONArray("materiais_de_aula");
                String length =jsonArray.length()+"";
                Log.e("3030: ", sharedPref.getString(codigosTurmas.get(count), "nada")+".");
                Log.e("3031: ", length+".");
                if (!length.equals(sharedPref.getString(codigosTurmas.get(count), "nada")) && !length.equals("0") && !sharedPref.getString(codigosTurmas.get(count), "nada").equals("nada")){
                    Log.e("Notificação: ", sharedPref.getString(codigosTurmas.get(count), "nada") + " e " + length);
                    notificacao("Novo material: " + jsonArray.getJSONObject(jsonArray.length()-1).getString("descricao"), "Materiais de aula");
                }
                Log.e("Antes: ", sharedPref.getString(codigosTurmas.get(count), "nada"));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(codigosTurmas.get(count), length);
                editor.apply();
                Log.e("Depois: ", sharedPref.getString(codigosTurmas.get(count), "nada"));
                Log.e("JSON LENGTH" + count, jsonArray.length()+".");
                count++;
                new GetTurmasDetalhes().execute();
            }catch(Exception e){

                e.printStackTrace();
                finalizar();
            }
            startService(new Intent(getApplicationContext(), ConfiguracoesIniciais.class));

        }
        public void finalizar(){
            for (int i = 0; i<codigosTurmas.size(); i++){

                Log.e("shared pref: ", sharedPref.getString(codigosTurmas.get(i), "nada"));
            }
        }

    }
}
