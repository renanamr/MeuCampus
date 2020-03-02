package com.meucampus.arthur.testez;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.meucampus.arthur.testez.Services.AcessoTurmas;
import com.meucampus.arthur.testez.Services.AcessoUsuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class ActivityLogin extends AppCompatActivity {
    EditText matriculaTxt;
    EditText senhaTxt;
    Button btnEntrar;
    TextView aviso;
    Button btn;
    String url1 = new String();
     String token = "";
    String matri ="";
    String sen = "";
    boolean tokenRefreshLogado = false;
    static ProgressBar progressBar;

    static Usuario usuarioGlobal;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public boolean testeLogado(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getBoolean("logado", false);
    }

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Log.e("TESTEEEEEEE", "1010101010");
        context = this;
        url1 = getString(R.string.url);
        usuarioGlobal = null;
        matriculaTxt = (EditText) findViewById(R.id.matriculaEdt);
        senhaTxt = (EditText) findViewById(R.id.senhaEdt);
        matriculaTxt.setText("");
        senhaTxt.setText("");
        btnEntrar = (Button) findViewById(R.id.entrar);
        aviso = (TextView) findViewById(R.id.aviso);
        progressBar = (ProgressBar) findViewById(R.id.progressLogin);

       // new GetServidor().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-dados/");
//alteraropa

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean testarProfessor = false;

        if(testarProfessor){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("logado",true);
            editor.putString("matricula","2276153");
            editor.putString("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IjIyNzYxNTMiLCJvcmlnX2lhdCI6MTUxODk3NDkzNCwidXNlcl9pZCI6OTgzMTgsImVtYWlsIjoibWFyaW8ubWVsb0BpZnJuLmVkdS5iciIsImV4cCI6MTUxOTA2MTMzNH0.6T3z8-zeQ5-QnhRMgesRLTLbjCgT-oFDRPqE_ekZvBM");
            editor.apply();
        }

        boolean logado = sharedPreferences.getBoolean("logado", false);

        Log.e("logado", logado+"");
        //logado
        /*if (logado){
                //finish();
                //Intent intent1 = new Intent(getApplicationContext(), TelaDeEspera.class);
                //startActivity(intent1);
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
                        //a.setSenha(repo.getSenha());
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(a));
                    /*String json = jsonObject.toString();
                    GetTurmas get = new GetTurmas();
                    get.execute(url1 + "usuario/login", json);
                    String[] turmas;
                    turmas = get.getTurmas();

                    repo.setTurma(turmas);*/
/*
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                Log.e("TIPO", repo.getTipo());
                Log.i("TOKEN", repo.getToken());
                //editor.putString("tipo", usuarioGlobal.getTipo());
                new DownloadImageTask().execute("https://suap.ifrn.edu.br/"+repo.getFoto());
                //String json = "{\"username\":\""+repo.getMatricula()+"\",\"password\":\""+repo.getSenha()+"\"}";
                //Log.e("JSONNNN", json);
            //if (!matricula.equals("") && !senha.equals("")) {
            //AutenticaAsynk autenticaAsynk = new AutenticaAsynk();
            progressBar.setVisibility(View.VISIBLE);
            //new AutenticaP().execute("https://suap.ifrn.edu.br/api/v2/autenticacao/token/", json);

            String token = "{\"token\":\""+repo.getToken()+"\"}";
            tokenRefreshLogado = true;
            new TaskRefreshToken().execute("https://suap.ifrn.edu.br/api/v2/autenticacao/token/refresh/",token);

            //startActivity(intent);
            matriculaTxt.setText("");
            senhaTxt.setText("");
        }*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void clickEntrar(View view) {
        //ALTERE AQUI
        /*RepositorioUsuario repo = new RepositorioUsuario();
        repo.setMatricula("1234");
        repo.setNome("Márcio");
        repo.setCurso("COADES/NC");
        repo.setTipo("COADES/NC");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);*/
        //ATÉ AQUI
        Log.e("CLICK","clicou");
        String matricula = matriculaTxt.getText() + "";
        matri = matricula;

        String senha = senhaTxt.getText() + "";
        sen = senha;

            String json = "{\"username\":\""+matricula+"\",\"password\":\""+senha+"\"}";
            Log.e("JSONNNN", json);
            progressBar.setVisibility(View.VISIBLE);
        boolean testarProfessor = false;

        if(testarProfessor){
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("logado",true);
            editor.putString("matricula","2276153");
            editor.putString("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IjIyNzYxNTMiLCJvcmlnX2lhdCI6MTUxODk3NDkzNCwidXNlcl9pZCI6OTgzMTgsImVtYWlsIjoibWFyaW8ubWVsb0BpZnJuLmVkdS5iciIsImV4cCI6MTUxOTA2MTMzNH0.6T3z8-zeQ5-QnhRMgesRLTLbjCgT-oFDRPqE_ekZvBM");
            editor.apply();
            RepositorioUsuario usuario = new RepositorioUsuario();
            usuario.setMatricula("2276153");
            usuario.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IjIyNzYxNTMiLCJvcmlnX2lhdCI6MTUxODk3NDkzNCwidXNlcl9pZCI6OTgzMTgsImVtYWlsIjoibWFyaW8ubWVsb0BpZnJuLmVkdS5iciIsImV4cCI6MTUxOTA2MTMzNH0.6T3z8-zeQ5-QnhRMgesRLTLbjCgT-oFDRPqE_ekZvBM");
            Usuario u = new Usuario();
            u.setMatricula("2276153");

            new IsCadastrado().execute(url1+"usuario", new Gson().toJson(u));
        }else
            new Autentica2().execute("https://suap.ifrn.edu.br/api/v2/autenticacao/token/", json);


    }

    public boolean verificacao(String matricula, String senha) {
        try {
            JSONArray jsonArray = new JSONArray(readRawTextFile());
            for (int i =0;jsonArray.getJSONObject(i)!=null;i++) {
                if (matricula.equals(jsonArray.getJSONObject(i).getString("matricula")) && senha.equals(jsonArray.getJSONObject(i).getString("senha"))) {
                    return true;
                }

            }
         return false;
        }catch(Exception e){
            return false;
        }


    }

    public String readRawTextFile() {

        try {

            InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.usuarios);

            InputStreamReader inputreader = new InputStreamReader(inputStream);

            BufferedReader buffreader = new BufferedReader(inputreader);

            return buffreader.readLine();

        } catch (Exception e) {

            return null;

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityLogin Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.meucampus.arthur.testez/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityLogin Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.meucampus.arthur.testez/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    private class AutenticaAsynk extends AsyncTask<String, Void, String>{
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        BufferedWriter bw = null;
        public AutenticaAsynk(){
            Log.e("Eee", "aaa");
        }
        @Override
        protected String doInBackground(String... strings) {
            Log.e("doIN", "OK");

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                JSONObject json = new JSONObject(strings[1]);


                bw.write(String.valueOf(json));
                Log.e("JSON", String.valueOf(json));
                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());



                connection.disconnect();
            }catch (Exception e){
                Log.e("ERRO", e.getMessage());
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("kk eae men", s+"a");
            matriculaTxt = (EditText) findViewById(R.id.matriculaEdt);
            senhaTxt= (EditText) findViewById(R.id.senhaEdt);
            Gson gson = new Gson();
            JSONObject json;
            Log.e("OAPSDFOPAS", s+"A");
            if(s.contains("toke")){
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("CONSEGUIMOS");
                alert.setTitle("LOGOU");
            }else{
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("n logou");
                alert.setTitle("LOGOU");
            }

/*
            try {
                json = new JSONObject(s);
                usuarioGlobal = gson.fromJson(json.toString(), Usuario.class);

                Log.e("USUA TURMA: ",usuarioGlobal.getTurma()[0]+ "sdf");
                usuarioGlobal.setPermissoes(json.getString("permissoes"));

                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (data !=null) {
                    RepositorioUsuario repo = new RepositorioUsuario();
                    repo.setMatricula(usuarioGlobal.getMatricula());
                    editor.putString("matricula", usuarioGlobal.getMatricula());
                    repo.setSenha(usuarioGlobal.getSenha());
                    editor.putString("senha", usuarioGlobal.getSenha());
                    repo.setId(usuarioGlobal.getId());
                    editor.putInt("id", usuarioGlobal.getId());
                    editor.putString("tipo", usuarioGlobal.getTipo());

                    editor.putBoolean("logado", true);
                    repo.setTipo(usuarioGlobal.getTipo());
                    Log.e("TIPO", repo.getTipo());
                    editor.putString("tipo", usuarioGlobal.getTipo());
                    repo.setPermissoes(usuarioGlobal.getPermissoes());
                    Log.e("PERMISSOES", usuarioGlobal.getPermissoes() + "");
                    repo.setTurma(usuarioGlobal.getTurma());
                    if (repo.getTipo().equals("ALUNO")) {
                        String[] turma = usuarioGlobal.getTurma();
                        editor.putString("turma", turma[0]);
                    }repo.setHorario(usuarioGlobal.getHorario());


                    editor.putString("permissoes", usuarioGlobal.getPermissoes());

                }else{
                    editor.putBoolean("logado", false);
                }
                editor.apply();
                sharedPref = getPreferences(Context.MODE_PRIVATE);
                Log.e("PREFERENCES", sharedPref.getString("matricula", ""));
            }catch(Exception e){
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("logado", false);
                editor.commit();
                Log.e("JSONPOST", e.getMessage());
            }

            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            boolean logado = sharedPreferences.getBoolean("logado", false);
            Log.e("LOGADO", logado+"");

            if (logado){

                Log.e("IF", logado+"");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                matriculaTxt.setText("");
                senhaTxt.setText("");
                aviso.setVisibility(View.INVISIBLE);
            }else{
                aviso.setVisibility(View.VISIBLE);
            }
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

            Log.e("LOGADO",sharedPref.getBoolean("logado", false)+"");

            Log.e("DATA", s+"");*/
        }
    }/*
    private class GetTurmas extends AsyncTask<String, Void, String>{
        private String[] turmas;
        private void setTurmas(String[] turmas){
            this.turmas = turmas;
        }
        public String[] getTurmas(){
            return turmas;
        }
        private StringBuffer data = new StringBuffer();
        private BufferedReader br = null;
        BufferedWriter bw = null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                JSONObject json = new JSONObject(strings[1]);
                bw.write(String.valueOf(json));
                Log.e("JSON", String.valueOf(json));
                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Log.e("DATA2", data.toString());
                connection.disconnect();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                String d2 =s ;
                JSONObject jsonObject = new JSONObject(d2);
                Gson gson = new Gson();
                Usuario usuario = gson.fromJson(jsonObject.toString(), Usuario.class);
                RepositorioUsuario repo = new RepositorioUsuario();
                repo.setTurma(usuario.getTurma());
                Log.e("TURMAS NO JSON", usuario.getTurma()[0]);
                this.setTurmas(usuario.getTurma());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/

    private class Autentica2 extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String matricula = "";
        String senha = "";
        JSONObject json;
        @Override
        protected String doInBackground(String... strings) {
            try{

                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                json = new JSONObject(strings[1]);
                matricula = json.getString("username");
                senha =  json.getString("password");
                bw.write(String.valueOf(json));
                Log.e("JSON", String.valueOf(json));

                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                token = new JSONObject(data.toString()).getString("token");
                new RepositorioUsuario().setToken(token);
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", token);
                editor.apply();
                Log.e("DATA2", data.toString()+"");
                connection.disconnect();

                return "OK";
            }catch(Exception e){
                if(e.getMessage().contains("o address associated with hostname"))
                    return "CONEXAO";
            }
            return "FAIL";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("POST", s+".");
            if(s.equals("OK")){
                Usuario usuario = new Usuario();
                usuario.setMatricula(matricula);
                //usuario.setSenha(senha);
                Toast.makeText(context, "Autenticação pelo SUAP feita com sucesso", Toast.LENGTH_SHORT).show();
                //try{
                //new GetInformacoes().execute(new JSONObject(data.toString()).getString("token"), json.toString());}catch(Exception e){e.printStackTrace();}
                new IsCadastrado().execute(url1+"usuario", new Gson().toJson(usuario));
            }
            else {
                if(s.equals("CONEXAO")){
                    /*android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Sair");
                    builder.setMessage("Deseja realmente sair?");
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();*/
                    Toast.makeText(getApplicationContext(), "Sem conexão com a internet.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else {

                    progressBar.setVisibility(View.INVISIBLE);
                    aviso.setVisibility(View.VISIBLE);
                }
            }

        }
    }
    private class AutenticaP extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String matricula = "";
        String senha = "";
        JSONObject json;
        @Override
        protected String doInBackground(String... strings) {
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                json = new JSONObject(strings[1]);
                matricula = json.getString("username");
                senha =  json.getString("password");
                bw.write(String.valueOf(json));
                Log.d("JSON", String.valueOf(json));

                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                token = new JSONObject(data.toString()).getString("token");
                new RepositorioUsuario().setToken(token);
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", token);
                editor.apply();
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

                new GetPeriodos().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-periodos-letivos/");

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

                progressBar.setVisibility(View.GONE);
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
                //new AcessoUsuario(getApplicationContext()).inserir(new RepositorioUsuario().getMatricula(), new RepositorioUsuario().getToken());
                if(!tokenRefreshLogado)
                    new IsCadastrado().execute(url1+"usuario", new Gson().toJson(usuario));
                else{
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);

                    progressBar.setVisibility(View.GONE);
                }
            }

        }
    }

    private class IsCadastrado extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String json;
        String url;
        @Override
        protected String doInBackground(String... strings) {
            url = strings[0];
            Log.e("CADASTRO", "ENTROU");
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]+"/isCadastrado").openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                json = strings[1];
                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                JSONObject json = new JSONObject(strings[1]);
                bw.write(String.valueOf(json));
                Log.e("JSON", String.valueOf(json));
                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());

                Log.e("DATA2", connection.getResponseCode()+"");
                connection.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("false")){
                new GetInformacoes().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-dados/", url);
            }else{
                Log.e("json: ", json+".");
                new GetUsuario().execute(url+"/GetUsuario", json);
            }
        }
    }

    private class GetInformacoes extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        @Override
        protected String doInBackground(String... strings) {

            try{
                url = strings[1];
                urlSuap = strings[0];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                Log.e("basicAuth: ", strings[0]);
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
            Log.e("DATA POST", s);
            RepositorioUsuario repo = new RepositorioUsuario();
            Usuario usuario = new Usuario();
            JSONObject json;
            try{
                json = new JSONObject(s);
                usuario.setMatricula(json.getString("matricula"));
                usuario.setNome(json.getString("nome_usual"));
                usuario.setTipo(json.getJSONObject("vinculo").getString("curso")+"");
                usuario.setCurso(json.getJSONObject("vinculo").getString("curso")+"");
                usuario.setUrl(json.getString("url_foto_75x100"));
                repo.setNome(usuario.getNome());
                repo.setMatricula(matri);
                //repo.setSenha(sen);
                repo.setTipo(usuario.getTipo());
                repo.setCurso(usuario.getCurso());
                repo.setFoto(json.getString("url_foto_75x100"));
                Log.e("FOTOOOO" , "a " +repo.getFoto());
            }catch(Exception e){

            }
            Log.e("Usuario: ", usuario.getTipo()+".");
            //verificar
            //if (!usuario.getTipo().equals("Aluno"))
            //    new GetServidor().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-dados/", new Gson().toJson(usuario),url);
            //else
                new AddUsuario().execute(url+"/adicionar", new Gson().toJson(usuario));

        }

    }
    private class GetServidor extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String url;
        String jsonUsuario;
        @Override
        protected String doInBackground(String... strings) {
            try{
                url = strings[2];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                //aletaropa
                jsonUsuario = strings[1];
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                //ALTERAOPA
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
            }Log.e("DADOS", "dados: " + data.toString());
            return data.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            Usuario usuario = new Usuario();
            JSONObject json;
            try{

                json = new JSONObject(s);
                usuario = new Gson().fromJson(jsonUsuario, Usuario.class);
                //usuario.setTipo(json.getString("setor_suap"));
            }catch(Exception e){
                e.printStackTrace();
            }
            new AddUsuario().execute(url+"/adicionar", new Gson().toJson(usuario));
        }
    }

    private class GetUsuario extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String url;
        @Override
        protected String doInBackground(String... strings) {
            try{
                url = strings[1];
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]+"/"+matri).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Log.e("DATA2", connection.getResponseCode()+"");
                connection.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("Usuarios: ", s+".");
            RepositorioUsuario repo = new RepositorioUsuario();
            Usuario usuario = new Gson().fromJson(s, Usuario.class);
            repo.setNome(usuario.getNome());
            repo.setMatricula(usuario.getMatricula());
            //repo.setSenha(senhaTxt.getText()+"");
            repo.setPermissoes(usuario.getPermissoes());
            repo.setHorario(usuario.getHorario());
            repo.setId(usuario.getId());
            repo.setTipo(usuario.getTipo());
            repo.setCurso(usuario.getCurso());
            repo.setFoto(usuario.getUrl());

            sharedPreferences(repo);

            progressBar.setVisibility(View.INVISIBLE);
            new DownloadImageTask().execute("https://suap.ifrn.edu.br/"+repo.getFoto());
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
            Log.e("POST", s+".");
        }

        private void sharedPreferences(RepositorioUsuario repo){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("matricula", repo.getMatricula());
            //editor.putString("senha", repo.getSenha());
            editor.putString("token",repo.getToken());
            editor.putInt("id", repo.getId());
            editor.putString("nome", repo.getNome());
            editor.putString("tipo", repo.getTipo());
            editor.putBoolean("logado", true);
            editor.putString("tipo", repo.getTipo());
            editor.putString("curso", repo.getCurso());
            editor.putString("url", repo.getFoto());
            Log.e("PERMISSOES", repo.getPermissoes() + "");
/*            if (repo.getTipo().equals("ALUNO")) {
                String[] turma = repo.getTurma();
                editor.putString("turma", turma[0]);
            }ALTERAR*/
            editor.putBoolean("logado", true);
            editor.putString("permissoes", repo.getPermissoes());


            editor.apply();
        }
    }

    private class AddUsuario extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        @Override
        protected String doInBackground(String... strings) {
            try{

                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                JSONObject json = new JSONObject(strings[1]);
                bw.write(String.valueOf(json));
                Log.e("JSON add", String.valueOf(json));
                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                Log.e("DATA2", connection.getResponseCode()+"");
                connection.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("POST REPO::::", s+"d");
            RepositorioUsuario repo = new RepositorioUsuario();
            Usuario usuario = new Gson().fromJson(s, Usuario.class);
            repo.setNome(usuario.getNome());
            repo.setMatricula(usuario.getMatricula());
            ///repo.setSenha(senhaTxt.getText()+"");
            repo.setPermissoes(usuario.getPermissoes());
            repo.setHorario(usuario.getHorario(

            ));
            repo.setId(usuario.getId());
            repo.setTipo(usuario.getTipo());
            repo.setCurso(usuario.getCurso());
            repo.setFoto(usuario.getUrl());
            sharedPreferences(repo);

            progressBar.setVisibility(View.INVISIBLE);
            new DownloadImageTask().execute("https://suap.ifrn.edu.br/"+repo.getFoto());
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
            Log.e("POST", s+".");
        }

        private void sharedPreferences(RepositorioUsuario repo){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("matricula", repo.getMatricula());
            //editor.putString("senha", repo.getSenha());
            editor.putInt("id", repo.getId());
            editor.putString("nome", repo.getNome());
            editor.putString("tipo", repo.getTipo());
            editor.putBoolean("logado", true);
            editor.putString("tipo", repo.getTipo());
            editor.putString("curso", repo.getCurso());
            editor.putString("url", repo.getFoto());
            editor.putString("token",repo.getToken());

            Log.e("PERMISSOES", repo.getPermissoes() + "");
            try{if (repo.getTipo().equals("Al")) {
                String[] turma = repo.getTurma();
                editor.putString("turma", turma[0]);
            }}catch(Exception e){}
            editor.putBoolean("logado", true);
            editor.putString("permissoes", repo.getPermissoes());


            editor.apply();
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
                new RepositorioUsuario().setBitmap(bitmap);
                new GetPeriodos().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-periodos-letivos/");
                //List<IProfile> list = new ArrayList<IProfile>();
                //list.add(new ProfileDrawerItem().withName("").withEmail(matricula).withIcon(image));
                //mHeaderNavigation.setProfiles(list);



                //notifyDataSetChanged();
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

            try{

             urlSuap = strings[0];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                Log.e("basicAuth: ", strings[0]);
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
            Log.e("DATA POST", s);
            RepositorioUsuario repo = new RepositorioUsuario();
            String caminhoPeriodo = "";
            JSONArray json;
            try {

                json = new JSONArray(s);
                caminhoPeriodo += json.getJSONObject(json.length() - 1).getString("ano_letivo") + "/";
                caminhoPeriodo += json.getJSONObject(json.length() - 1).getString("periodo_letivo");


            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (!sharedPreferences1.getString("periodo", "nada").equals(caminhoPeriodo)) {
                new BD(context).deletarTurmas();
            }

            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("periodo", caminhoPeriodo);
            editor.apply();
            Log.e("Perido: ", caminhoPeriodo);
            //mProgressBar.incrementProgressBy(20);
            repo.setCaminhoPeriodo(caminhoPeriodo);

        }
    }
    private class GetPermissoes extends AsyncTask<String, Void, String>{
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
            Log.e("DATA POST us", s);
            RepositorioUsuario repo = new RepositorioUsuario();

            JSONObject json;
            try{
                json = new JSONObject(s);
                repo.setPermissoes(json.getString("permissoes"));
            }catch(Exception e){

            }



        }

    }


}

