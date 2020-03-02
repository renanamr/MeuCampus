package com.meucampus.arthur.testez.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.meucampus.arthur.testez.ActivityNovaConversaProfessor;
import com.meucampus.arthur.testez.ActivityTurmasDetalhes;
import com.meucampus.arthur.testez.BD;
import com.meucampus.arthur.testez.RepositorioUsuario;
import com.meucampus.arthur.testez.TurmasAdapter;

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

/**
 * Created by ARTHUR on 05/03/2018.
 */

public class ServicoAtualizacao extends Service {
    @Override
    public void onCreate() {
        new Worker().start();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Worker().start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //new Worker().start();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //new Worker().start();
        super.onTaskRemoved(rootIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class Worker extends Thread{
        @Override
        public void run() {
            while(true){
                new TaskRefreshToken().execute();
                try{
                    Thread.sleep(30000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private class TaskRefreshToken extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        JSONObject json;
        @Override
        protected String doInBackground(String... strings) {
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/autenticacao/token/refresh/").openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String token = sharedPreferences1.getString("token", "");

                json = new JSONObject("{\"token\":\""+ token+"\"}");
                bw.write(String.valueOf(json));
                Log.d("ServicoAtualizacao:", String.valueOf(json));

                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                token = new JSONObject(data.toString()).getString("token");
                new RepositorioUsuario().setToken(token);

                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("token",token);
                editor.apply();

                Log.e("DATA2", data.toString()+"");
                connection.disconnect();

                return "OK";
            }catch(Exception e){
                Log.e("ServicoAtualizacao", e.getMessage());
                e.printStackTrace();
            }
            return "FAIL";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("POST", s+".");
            if(s.equals("OK")) {
                new AtualizarTurmas().execute();
            }

        }
    }

    static Turmas[] turmas = new Turmas[100];
    List<Turmas> turmasL = new ArrayList<>();
    private class AtualizarTurmas extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        int count = 0;

        @Override
        protected String doInBackground(String... strings) {
            Log.e("Atualizar Turmas", "Do inb");
            try{
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/2017/1").openConnection();
                connection.setRequestProperty("Authorization", "JWT "+new RepositorioUsuario().getToken());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(false);
                connection.setDoInput(true);
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Log.e("DATA123", data.toString()+"");
                Log.e("CODE", connection.getResponseCode()+"");
                connection.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("Atualizar: DATA POST", s);
            RepositorioUsuario repo = new RepositorioUsuario();

            JSONArray json;
            String[] turmasCod = new String[100];
            try{
                json = new JSONArray(s);
                for (int i = 0; i<json.length(); i++){
                    JSONObject jsonObject = new JSONObject(json.getJSONObject(i).toString());
                    turmasCod[i] = jsonObject.getString("codigo_diario");

                    Turmas turma = new Turmas();
                    turma.setDescricao(jsonObject.getString("disciplina"));
                    turma.setCodigo(jsonObject.getString("codigo_diario"));
                    turmasL.add(turma);

                    turmas[i] = new Turmas();
                    turmas[i].setDescricao(jsonObject.getString("disciplina"));
                    turmas[i].setCodigo(jsonObject.getString("codigo_diario"));

                }
                for (int i = 0; i<json.length(); i++){
                    Log.e("AtualizarTurmas: ", turmas[i].getDescricao());
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            new GetDados().execute(turmasCod);
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
                    Log.e("Atualizar: i", strings[i]);
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/turma-virtual/" + strings[i]).openConnection();

                    connection.setRequestProperty("Authorization", "JWT " + new RepositorioUsuario().getToken());
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-type", "application/json");
                    connection.setUseCaches(false);
                    connection.setDoOutput(false);
                    connection.setDoInput(true);


                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    data.append(br.readLine());
                    Log.e("Atualizar: DATA", data.toString() + "");
                    Log.e("CODE", connection.getResponseCode() + "");
                    connection.disconnect();
                    String matricula = new JSONObject(data.toString()).getJSONArray("professores").get(0).toString();
                    matricula = new JSONObject(matricula).getString("matricula");
                    matriculas.add(matricula);
                    Log.e("Atualizar: matricula", matricula + ".");
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
            Log.e("Atualizar: ", "finalizar");
            for(int i =0; i<matriculas.size();i++){
                Log.e("Turmas::::", turmasL.get(i).getDescricao()+".");
                Turmas turma = turmasL.get(i);
                turma.setMatriculaProfessor(matriculas.get(i));
                turmasL.set(i, turma);

                Log.e("codigo: ", turmasL.get(i).getCodigo());
                Log.e("descricao: ", turmasL.get(i).getDescricao());
                Log.e("Matricula: ", turmasL.get(i).getMatriculaProfessor());

            }
            try {
                new BD(getApplicationContext()).inserirTurmas(turmasL);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
