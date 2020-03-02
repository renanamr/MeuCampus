package com.meucampus.arthur.testez.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.meucampus.arthur.testez.RepositorioUsuario;
import com.meucampus.arthur.testez.SetorAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 31/12/2017.
 */
public class ServicoGetProfessores extends Service {
    @Override
    public void onCreate() {
        Log.e("Service", "Servico iniciado");
        //new GetTurmas().execute();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //new GetTurmas().execute();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class GetTurmas extends AsyncTask<String, Void, String> {
        BufferedReader br;
        StringBuffer data = new StringBuffer();
        String[] codigosTurma = new String[100];
        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/2017/1/").openConnection();

                connection.setRequestProperty("Authorization", "JWT " + new RepositorioUsuario().getToken());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(false);
                connection.setDoInput(true);


                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Log.e("DATA", data.toString() + "");
                Log.e("CODE", connection.getResponseCode() + "");
                connection.disconnect();
                return data.toString();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {

            List<String> turmas = new ArrayList<>();
            try {
                Log.e("Data", s);
            }catch (Exception e){
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray= new JSONArray(s);
                for(int i = 0; i <jsonArray.length(); i++){
                    codigosTurma[i] = jsonArray.getJSONObject(i).getString("codigo_diario");
                    turmas.add(jsonArray.getJSONObject(i).getString("disciplina"));
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            SetorAdapter setorAdapter = new SetorAdapter(getApplicationContext(), turmas);
            new GetDados().execute(codigosTurma);
        }
    }
    List<String> matriculas = new ArrayList<>();
    private class GetDados extends AsyncTask<String, Void, String>{
        BufferedReader br;
        StringBuffer data = new StringBuffer();

        @Override
        protected String doInBackground(String... strings) {

            int i =0;
            while (true) {
                try {
                    Log.e("i", strings[i]);
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/turma-virtual/" + strings[i]).openConnection();

                    connection.setRequestProperty("Authorization", "JWT " + new RepositorioUsuario().getToken());
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-type", "application/json");
                    connection.setUseCaches(false);
                    connection.setDoOutput(false);
                    connection.setDoInput(true);


                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    data.append(br.readLine());
                    Log.e("DATA", data.toString() + "");
                    Log.e("CODE", connection.getResponseCode() + "");
                    connection.disconnect();
                    String matricula = new JSONObject(data.toString()).getJSONArray("professores").get(0).toString();
                    matricula = new JSONObject(matricula).getString("matricula");
                    matriculas.add(matricula);
                    Log.e("matricula", matricula + ".");
                    br = null;
                    connection = null;
                    data = new StringBuffer();

                } catch (Exception e) {
                    e.printStackTrace();
                    break;


                }
                i++;

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            for (String m : matriculas){
                Log.e("M", m);
            }
            new GetServidor().execute();
        }
    }

    private class GetServidor extends AsyncTask<String, Void, String>{
        BufferedReader br;
        StringBuffer data = new StringBuffer();
        List<String> matriculasExibidas = new ArrayList<>();
        @Override
        protected String doInBackground(String... strings) {

            try {
                HttpURLConnection get = (HttpURLConnection) new URL("http://200.137.2.185/meucampus/service/usuario/lista").openConnection();
                get.setRequestMethod("GET");

                get.connect();

                br = new BufferedReader(new InputStreamReader(get.getInputStream()));

                data.append(br.readLine());


                Log.e("LogData", data.toString());

                get.disconnect();
            }catch(Exception e) {
                Log.e("Erro", e.getMessage());

            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            for(String professores : matriculas){
                if(s.contains(professores))
                    matriculasExibidas.add(professores);
                else
                    Log.e("P",professores);
            }
            new ConversasRepositorio(1, matriculasExibidas);

        }

    }
}
