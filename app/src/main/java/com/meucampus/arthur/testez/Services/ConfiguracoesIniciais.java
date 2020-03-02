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
import android.widget.Toast;

import com.meucampus.arthur.testez.ActivityNovaConversaProfessor;
import com.meucampus.arthur.testez.ActivityTurmas;
import com.meucampus.arthur.testez.ActivityTurmasDetalhes;
import com.meucampus.arthur.testez.BD;
import com.meucampus.arthur.testez.RepositorioUsuario;
import com.meucampus.arthur.testez.TurmasAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracoesIniciais extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("create", "config");
       // new Worker().start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.e("create", "config");
        new Worker().start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            try{
                Log.e("config periodo", new RepositorioUsuario().getCaminhoPeriodo());
                new GetTurmas().execute();
            }catch (Exception e){
                new GetPeriodoLetivo().execute();
            }
        }
    }

    private class GetPeriodoLetivo extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        @Override
        protected String doInBackground(String... strings) {
            try{
//                url = strings[1];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                Log.e("basicAuth: ", new RepositorioUsuario().getToken());
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-periodos-letivos/").openConnection();

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
            JSONArray jsonArray = new JSONArray();
            try{
                jsonArray = new JSONArray(s);
                //periodo = jsonArray.getJSONObject(jsonArray.length()-1).getString("ano_letivo")+"/"+jsonArray.getJSONObject(jsonArray.length()-1).getString("periodo_letivo");
                String periodo = jsonArray.getJSONObject(jsonArray.length()-1).getString("ano_letivo")+"/"+1;
                Log.e("config: ", periodo+".");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("periodo", periodo);
                editor.apply();
                new GetTurmas().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/"+periodo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    List<Turmas> list = new ArrayList<>();
    List<String> turmas = new ArrayList<>();
    String dadosJson="";
    private class GetTurmas extends AsyncTask<String, Void, String> {
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;



        String[] codigosTurma = new String[100];

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Toast.makeText(getApplicationContext(), new BD(getApplicationContext()).listarTurmas().size()+".", Toast.LENGTH_SHORT).show();
                Log.e("config size:", new BD(getApplicationContext()).listarTurmas().size()+".");
                if(new BD(getApplicationContext()).listarTurmas().size() != 0){
                    Log.e("config", "if");
                    return "oka" +
                            "";
                }
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/"+ new RepositorioUsuario().getCaminhoPeriodo()).openConnection();

                connection.setRequestProperty("Authorization", "JWT " + new RepositorioUsuario().getToken());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(false);
                connection.setDoInput(true);

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Log.e("DATA123", data.toString() + "");
                Log.e("CODE", connection.getResponseCode() + "");
                connection.disconnect();
            } catch (Exception e) {

                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("ok"))
                return;
            dadosJson = s;
            Log.e("config", s);
            //[{"codigo_diario":"40267","disciplina":"TIN.0166 - Biologia II(120H)","segundo_semestre":false,"carga_horaria":160,"carga_horaria_cumprida":18,"numero_faltas":0,"percentual_carga_horaria_frequentada":100,"situacao":"Cursando","quantidade_avaliacoes":4,"nota_etapa_1":{"nota":null,"faltas":0},"nota_etapa_2":{"nota":null,"faltas":0},"nota_etapa_3":{"nota":null,"faltas":0},"nota_etapa_4":{"nota":null,"faltas":0},"media_disciplina":null,"nota_avaliacao_final":{"nota":null,"faltas":0},"media_final_disciplina":null}
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

            new GetDados().execute(codigosTurma);
        }

    }


    //List<String> turmas = new ArrayList<>();

    List<String> matriculas = new ArrayList<>();
    List<String> nomes = new ArrayList<>();
    private class GetDados extends AsyncTask<String, Void, String>{
        BufferedReader br;
        StringBuffer data = new StringBuffer();

        @Override
        protected String doInBackground(String... strings) {

            int i =0;
            while (true) {
                try {
                    Log.e("config i", strings[i]);
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/turma-virtual/" + strings[i]).openConnection();

                    connection.setRequestProperty("Authorization", "JWT " + new RepositorioUsuario().getToken());
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-type", "application/json");
                    connection.setUseCaches(false);
                    connection.setDoOutput(false);
                    connection.setDoInput(true);


                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    data.append(br.readLine());
                    Log.e("DATA config", data.toString() + "");
                    Log.e("CODE", connection.getResponseCode() + "");
                    connection.disconnect();

                    try{


                        String matricula = new JSONObject(data.toString()).getJSONArray("professores").get(0).toString();
                        String nome = new JSONObject(matricula).getString("nome");
                        Log.e("config nome: ", ""+ "..." + nome);
                        nomes.add(nome);

                        matricula = new JSONObject(matricula).getString("matricula");
                        matriculas.add(matricula);



                        Log.e("matricula", matricula + ".");
                    }catch(Exception e){
                        String matricula = "não definido";
                        String nome = "não definido";
                        nomes.add(nome);
                        matriculas.add(matricula);
                        Log.e("matricula", matricula + ".");
                    }
                    Log.e("config turmas", strings[i]);
                    int n1=0;
                    int n2=0;
                    int n3=0;
                    int n4=0;
                    try{
                        JSONArray jsonArray = new JSONArray(dadosJson);
                        Log.e("dados config222", jsonArray.toString());
                        n1 = Integer.parseInt(jsonArray.getJSONObject(i).getJSONObject("nota_etapa_1").getString("nota"));
                        n2 = Integer.parseInt(jsonArray.getJSONObject(i).getJSONObject("nota_etapa_2").getString("nota"));
                        n3 = Integer.parseInt(jsonArray.getJSONObject(i).getJSONObject("nota_etapa_3").getString("nota"));
                        n4 = Integer.parseInt(jsonArray.getJSONObject(i).getJSONObject("nota_etapa_4").getString("nota"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    list.add(new Turmas(strings[i],turmas.get(i), matriculas.get(i), nomes.get(i), n1,n2,n3,n4));
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
                Log.e("config", m);
            }
            for (Turmas t : list){
                Log.e("config list:", t.toString());
            }
            new BD(getApplicationContext()).inserirTurmas(list);
        }
    }


}