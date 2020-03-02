package com.meucampus.arthur.testez;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meucampus.arthur.testez.Services.AcessoTurmas;
import com.meucampus.arthur.testez.Services.ServicoAtualizacao;
import com.meucampus.arthur.testez.Services.ServicoNotificacao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class TelaDeEspera extends AppCompatActivity {
    String url1;
    String token;
    ProgressBar mProgressBar;
    boolean tokenRefreshLogado = false;
    Context context;
    SharedPreferences sharedPreferences;
    private void mostrarLogin() {
        Intent i = new Intent(getApplicationContext(), ServicoNotificacao.class);
        stopService(i);
        i = new Intent(getApplicationContext(), ServicoAtualizacao.class);
        stopService(i);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean logado = sharedPreferences.getBoolean("logado", false);
        mProgressBar.incrementProgressBy(10);
        Log.e("logado: mostrarLogin", sharedPreferences.getString("token","."));
        if (logado){
            Log.d("Teste1", "teste2");

            RepositorioUsuario repo = new RepositorioUsuario();
            Log.d("Teste1", sharedPreferences.getString("matricula", ""));
            repo.setMatricula(sharedPreferences.getString("matricula", ""));
            new GetPermissoes().execute(url1+"usuario/GetUsuario/"+repo.getMatricula());
            //editor.putString("matricula", usuarioGlobal.getMatricula());
            //repo.setSenha(sharedPreferences.getString("senha", ""));
            repo.setToken(sharedPreferences.getString("token",""));
            //editor.putString("senha", usuarioGlobal.getSenha());
            repo.setId(sharedPreferences.getInt("id", 0));
            //editor.putInt("id", usuarioGlobal.getId());
            //editor.putString("tipo", usuarioGlobal.getTipo());
            String teste = sharedPreferences.getString("permissoes", "0");
            repo.setPermissoes(sharedPreferences.getString("permissoes", "0"));
            repo.setNome(sharedPreferences.getString("nome", ""));

            //editor.putBoolean("logado", true);
            repo.setTipo(sharedPreferences.getString("tipo", ""));
            repo.setCurso(sharedPreferences.getString("curso", ""));
            repo.setFoto(sharedPreferences.getString("url", ""));

            if (repo.getTipo().equals("ALUNO")) {
                String[] turma  = new String[1];
                turma[0] = sharedPreferences.getString("turma", "");
                repo.setTurma(turma);
            }else{
                try {
                    Usuario a = new Usuario();
                    a.setMatricula(repo.getMatricula());
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(a));
                    

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Log.e("TIPO", repo.getTipo());
            Log.i("TOKEN", repo.getToken());
            new DownloadImageTask().execute("https://suap.ifrn.edu.br/"+repo.getFoto());


            String token = "{\"token\":\""+repo.getToken()+"\"}";
            tokenRefreshLogado = true;
            new TaskRefreshToken().execute("https://suap.ifrn.edu.br/api/v2/autenticacao/token/refresh/",token);


        }else{
            mProgressBar.incrementProgressBy(90);
            Intent intent = new Intent(context, ActivityLogin.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_espera);
        context = getApplicationContext();
        url1 = getString(R.string.url);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Log.e("TELADEESPERA", "ONCREATE");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        mostrarLogin();
        else{
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean logado = sharedPreferences.getBoolean("logado", false);
            Log.e("Logado:::", logado+".");
            if(logado){
                new RepositorioUsuario().setInternet(true);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Não há conexão com a internet. Utilizando os serviços em modo offline.", Toast.LENGTH_LONG).show();
            }else
                mostrarLogin();

        }

    }
    private class GetPermissoes extends AsyncTask<String, Void, String> {
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        @Override
        protected String doInBackground(String... strings) {

            try{
                Log.e("DOINB", "OK");
                urlSuap = strings[0];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário

                HttpURLConnection get = (HttpURLConnection) new URL(strings[0]).openConnection();
                get.setRequestMethod("GET");

                get.connect();
                Log.e("Log", "1");
                br = new BufferedReader(new InputStreamReader(get.getInputStream()));
                Log.e("Log", "2");
                data.append(br.readLine());


                Log.e("LogData", data.toString());

                get.disconnect();
            }catch(Exception e){

                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.incrementProgressBy(20);
            Log.e("DATA POST us", s);
            RepositorioUsuario repo = new RepositorioUsuario();

            JSONObject json;
            try{
                json = new JSONObject(s);
                repo.setPermissoes(json.getString("permissoes"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private class DownloadImageTask extends AsyncTask<String , Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... url) {
            Log.e("BACKGROUND", "entrei");
            try{
                HttpURLConnection get = (HttpURLConnection) new URL(url[0]).openConnection();
                get.setRequestMethod("GET");

                get.connect();

                // decoding stream data back into image Bitmap that android understands
                final Bitmap bitmap = BitmapFactory.decodeStream(get.getInputStream());

                get.disconnect();

                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
//               Toast.makeText(mContext, "Erro de imagem", Toast.LENGTH_SHORT).show();


                Log.e("BITMAP","Erro de imagem",e);
            }
            Log.e("BACKGROUND", "Retornei null");
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try{
                mProgressBar.incrementProgressBy(30);
                new RepositorioUsuario().setBitmap(bitmap);

                
                Log.e("ONPOSTEXECUTE", "alterei no bitmap");
                //image.setImageBitmap(bitmap);
            }catch (Exception e){
                Log.e("ERRO", e.getMessage());
                //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    private class GetPeriodos extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        @Override
        protected String doInBackground(String... strings) {
            Log.e("Periodos", "tela de espera");
            try{

                urlSuap = strings[0];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                //Log.e("basicAuth: ", strings[0]);
                HttpURLConnection connection = (HttpURLConnection) new URL(urlSuap).openConnection();
                new RepositorioUsuario().setToken(token);
                connection.setRequestProperty("Authorization", "JWT "+token);
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
            Log.e("DATA POST periodo", s);
            RepositorioUsuario repo = new RepositorioUsuario();
            String caminhoPeriodo="";
            JSONArray json;
            try{

                json = new JSONArray(s);
                caminhoPeriodo += json.getJSONObject(json.length()-1).getString("ano_letivo") + "/";
                caminhoPeriodo += json.getJSONObject(json.length()-1).getString("periodo_letivo");


            }catch(Exception e){
                e.printStackTrace();
            }
            if(!sharedPreferences.getString("periodo", "nada").equals(caminhoPeriodo)){
                new BD(context).deletarTurmas();
            }

            SharedPreferences.Editor editor =  sharedPreferences.edit();
            editor.putString("periodo", caminhoPeriodo);
            editor.apply();
            Log.e("Perido: ", caminhoPeriodo);
            mProgressBar.incrementProgressBy(10);
            repo.setCaminhoPeriodo(caminhoPeriodo);
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);

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
                mProgressBar.incrementProgressBy(10);
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                json = new JSONObject(strings[1]);
                bw.write(String.valueOf(json));
                Log.d("JSON", String.valueOf(json));

                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                token = new JSONObject(data.toString()).getString("token");

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", token);
                editor.apply();

                new RepositorioUsuario().setToken(token);
                Log.e("DATA2", data.toString()+"");
                connection.disconnect();

                return "OK";
            }catch(Exception e){
                e.printStackTrace();
            }
            return "FAIL";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("POST", s+".");
            if(s.equals("OK")) {
                mProgressBar.incrementProgressBy(20);

                //new GetPeriodos().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-periodos-letivos/");

                /*Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

                progressBar.setVisibility(View.GONE);*/
                Usuario usuario = new Usuario();
                usuario.setMatricula(new RepositorioUsuario().getMatricula());
                Log.e("Matrícula: ", usuario.getMatricula());
                //usuario.setSenha(senha);
                Toast.makeText(context, "Autenticação pelo SUAP feita com sucesso", Toast.LENGTH_SHORT).show();
                //try{
                //new GetInformacoes().execute(new JSONObject(data.toString()).getString("token"), json.toString());}catch(Exception e){e.printStackTrace();}
                Log.e("TokenRefresh", tokenRefreshLogado+".");
                new GetPeriodos().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-periodos-letivos/");
            }else{
                Intent intent = new Intent(context, ActivityLogin.class);
                startActivity(intent);
            }

        }
    }
}
