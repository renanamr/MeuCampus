package com.meucampus.arthur.testez;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button btn;
    private static List<Mensagem> m = new ArrayList<>();
    private static ListView mList;
    private Toolbar mToolbarBottom;

    static int jsonLengthU = 0;
    String url1;
    private static Usuario user;
    boolean coadesRequest;
    static String idUsuario;
    int tipo=0;
    /*@Override
    protected void onRestart() {
        request();
        super.onRestart();
    }*/

    private void recuperarMensagens(int tipoBd){
        BD bd = new BD(getApplicationContext());
        Log.e("bd e isso", "Opaaaaa");
        if (bd.buscar(idUsuario, tipo+"") == null) {
            Log.e("BD", "NULL");
            m = new ArrayList<>();
        }else{
            Log.e("BD", "not null");
            List<Mensagem> listas = bd.buscar(idUsuario, tipoBd+"");
            Log.e("bd size::::::", listas.size()+".");
            int vez = new RepositorioUsuario().getVez();
            Log.e("VEZZZZZZZZZ", vez+"");
            m = new ArrayList<>();
            for (int i = 0; i<listas.size(); i++){
                Log.e("Lista bd", listas.size()+"");

                Mensagem mensagem = listas.get(i);
                Log.e("BD bbb", mensagem.getTipo());
                //if(mensagem.getTipo().contains(tipoBd))
                    m.add(i, mensagem);
            }
            Log.e("BD aa", m.size()+".");
            mList = findViewById(R.id.listView50);
            if (m != null) {
                RepositorioUsuario repositorioUsuario = new RepositorioUsuario();
                mList.setAdapter(new MensagemAdapter(getApplicationContext(), m, repositorioUsuario.getTipo()));
                // user.setMensagens(m);
            }
            new RepositorioUsuario().setVez(vez);


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        url1 = getResources().getString(R.string.url);
        new GetUsuarios().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        tipo = b.getInt("tipo");
        Log.e("tipo", tipo+"");
        idUsuario = b.getString("idusuario");
        Log.e("Create", "on");
        String matricula = new RepositorioUsuario().getMat();
        Log.e("Matriculaaa223232", matricula+" a" );
        String tipoS = b.getString("tipo");
        if(tipoS.contains("COADES"))
            tipo = 0;
        else
            tipo = 1;
        recuperarMensagens(tipo);


//        Log.e("IDUSU", idUsuario);
        if(tipo<10)
            new GetMensagens().execute(url1+"mensagem/listaNaoLidas/"+tipo+"/"+new RepositorioUsuario().getMatricula());
        else
            new GetMensagensTurmas().execute();
        //new GetMensagens().execute(url1+"mensagem/lista");


        /*m = new ArrayList<>();

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            int id = bundle.getInt("idusuario");
            idUsuario = id;
            Log.e("IDUSUARIO", idUsuario+"");
            coadesRequest = true;
        }catch(Exception e){
            coadesRequest = false;
        }


        request();
        */
        mToolbar = (Toolbar) findViewById(R.id.tb_chat);
        mToolbar.setTitle("Chat");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimarytext));
        mList = (ListView) findViewById(R.id.listView50);


        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        //mToolbarBottom = (Toolbar) findViewById(R.id.chat_tb_bottom);

        btn = (Button) findViewById(R.id.btn_Send);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();



                TextView txt = (TextView) findViewById(R.id.txt_inputText);
                String texto = txt.getText() + "";
                RepositorioUsuario usuario = new RepositorioUsuario();
                if (texto.isEmpty())
                    Toast.makeText(getApplicationContext(), "Insira algo na mensagem", Toast.LENGTH_SHORT).show();
                else{

                    Mensagem mensagem = (new Mensagem(txt.getText() + "", c.get(Calendar.HOUR_OF_DAY)+ ":" + c.get(Calendar.MINUTE), c.get(Calendar.DAY_OF_MONTH)+"/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR)));
                    mensagem.setTipo(tipo+"");
                    mensagem.setMatricula(usuario.getMatricula());
/*                    if(idUsuario.equals(new RepositorioUsuario().getMatricula()))
                        mensagem.setMatriculaDestinatario("0");*/

                    mensagem.setMatriculaRemetente(new RepositorioUsuario().getMatricula());
                    txt.setText("");
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", 0);
                        jsonObject.put("texto", mensagem.getTexto());
                        jsonObject.put("hora", c.get(Calendar.HOUR_OF_DAY)+ ":" + c.get(Calendar.MINUTE));
                        jsonObject.put("data", mensagem.getData());
                        jsonObject.put("recebida", false);
                        jsonObject.put("idSetor", 0);
                        jsonObject.put("matriculaRemetente", mensagem.getMatriculaRemetente());
                        jsonObject.put("matriculaDestinatario", mensagem.getMatriculaDestinatario());

                        String json = jsonObject.toString();
                        Log.e("JSONST", json);
                        BD bd = new BD(getApplicationContext());
                        //if (coadesRequest){
                        mensagem.setMatriculaDestinatario("0");
                            bd.inserir(mensagem, tipo+"", mensagem.getMatriculaRemetente(), mensagem.getMatriculaDestinatario() );
                            new PostMensagem().execute(url1+"mensagem/"+"enviar/"+tipo, json);

                        /*}else{
                            RepositorioUsuario repo = new RepositorioUsuario();
                            mensagem.setIdUsuario(repo.getId());
                            jsonObject = new JSONObject(new Gson().toJson(mensagem));
                            json = jsonObject.toString();
                            Log.e("JSONALUNO", json);
                            bd.inserir(mensagem, tipo);
                            if(tipo.equals("COADES/NC"))
                                new PostMensagem().execute(url1+"mensagem/criar", json);
                            else
                            if (tipo.equals("DG/NC"))
                                new PostMensagem().execute(url1+"mensagem/dg/criar", json);

                        }*/
                        //new PostMensagem().execute("http://192.168.43.127:8080/meucampus/service/mensagem/criar", json);
                    }catch(Exception e){
                        Log.e("GSON", e.getMessage());
                    }

                    mList = (ListView) findViewById(R.id.listView50);
                    m.add(mensagem);
                    RepositorioUsuario repo = new RepositorioUsuario();
                    mList.setAdapter(new MensagemAdapter(getApplication(), m, repo.getTipo()));
                    //user.setMensagens(m);
                }
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Restart", "On");
        mList = (ListView) findViewById(R.id.listView50);
        mList.setAdapter(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Stop", "on");
        mList = (ListView) findViewById(R.id.listView50);
        mList.setAdapter(null);
    }

    private class GetMensagens extends AsyncTask<String , Void, String> {
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        int usuarioId;

        @Override
        protected String doInBackground(String... url) {
            Log.e("BACK", "ASPDF");
            try {

                //CHAMA O ARQUIVO DE TEXTO
                //SE TIVER EM BRANCO, CONTINUA ESSA EXECUCAO
                //SENAO
                //A GENTE PEGA AS MSGS TROCADAS COM BASE NO ID (url[1])
                //ARMAZENA NO LIST<>
                //E ALTERA NO LISTVIEW !
                //SAFEEEEEEE
                Log.e("URL MEU TRUTA", url[0]);
                HttpURLConnection get = (HttpURLConnection) new URL(url[0]).openConnection();
                get.setRequestMethod("GET");

                get.connect();
                Log.e("Log", "1");
                br = new BufferedReader(new InputStreamReader(get.getInputStream()));
                Log.e("Log", "2");
                data.append(br.readLine());

//erro ta no servidor!!!!!!!!!!!
                Log.e("LogData", data.toString());

                get.disconnect();
            }catch(Exception e) {
                Log.e("Erro", e.getMessage());

            }

            return data.toString();

        }


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), "Carregando mensagens...", Toast.LENGTH_SHORT).show();
            //Acessa o BD e verifica se há mensagens a partir do IdUsuario
            BD bd = new BD(getApplicationContext());
            Log.e("POSTEXECUTE", "Opaaaaa");
            if (bd.buscar(idUsuario, tipo+"") == null) {
                Log.e("BD", "NULL");
                m = new ArrayList<>();
            }else{
                Log.e("BD", "not null");
                List<Mensagem> listas = bd.buscar(idUsuario, tipo+"");
                Log.e("teste", listas.size()+".");
                int vez = new RepositorioUsuario().getVez();
                Log.e("VEZZZZZZZZZ", vez+"");
                m = new ArrayList<>();
                for (int i = 0; i<listas.size(); i++){
                    Log.e("Lista bd", listas.size()+"");

                    Mensagem mensagem = listas.get(i);

                    m.add(i, mensagem);
                }

                vez++;
                new RepositorioUsuario().setVez(vez);
                Log.e("mensagensss", m.size()+"");

            }

            try {
                Log.e("dentro", s);
                JSONArray jsonArray = new JSONArray(s);
                int jsonLength = jsonArray.length();
                Log.e("JSONLENGTH", jsonArray.length() + "");


                Log.e("mensagensss", m.size()+"");
                //Log.e("BUSCAR", "b" + m.get(0).getTexto());
                for (int i = 0;i < jsonArray.length(); i++){


                    JSONObject json = jsonArray.getJSONObject(i);
                    Log.e("JSONOBJECT:::: ", json.toString());
                    //Log.e("JSON ID: ", json.getInt("idUsuario") + "");
                    Log.e("ID USUARIO: ", idUsuario+"");
                    //if (json.getInt("idUsuario") == Integer.parseInt(idUsuario)) {
                        Log.e("IF", "if");
                        Mensagem mensagem = new Mensagem();
                        mensagem.setData(json.getString("data"));
                        mensagem.setHora(json.getString("hora"));
                        mensagem.setId(json.getInt("id"));
                        mensagem.setMatriculaRemetente(json.getString("matriculaRemetente"));
                        mensagem.setMatriculaDestinatario(json.getString("matriculaDestinatario"));
                        RepositorioUsuario user = new RepositorioUsuario();
                        mensagem.setTexto(json.getString("texto"));
                        //e.setTipo((Tipo) json.get("tipo"));

                        //mensagem.setMatricula(json.getString("matricula"));
                        mensagem.setTipo(tipo+"");
                        bd.inserir(mensagem, tipo+"", json.getString("matriculaRemetente"), json.getString("matriculaDestinatario"));
                        m.add(mensagem);
                    //}

                }
                //Log.e("M", "SIZE: " + m.size());
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.e("mensagensss", m.size()+"");
            mList = (ListView) findViewById(R.id.listView50);
            if (m != null) {
                RepositorioUsuario repositorioUsuario = new RepositorioUsuario();
                mList.setAdapter(new MensagemAdapter(getApplicationContext(), m, repositorioUsuario.getTipo()));
                // user.setMensagens(m);
            }
        }
    }
    private class GetUsuarios extends AsyncTask<String , Void, String> {
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;

        @Override
        protected String doInBackground(String... url) {
            Log.e("BACK", "ASPDF");
            try {
                HttpURLConnection get = (HttpURLConnection) new URL("http://200.137.2.185/meucampus/service/usuario/lista").openConnection();
                get.setRequestMethod("GET");
                get.connect();
                br = new BufferedReader(new InputStreamReader(get.getInputStream()));
                data.append(br.readLine());
                Log.e("Mensagens turma:", data.toString());

                get.disconnect();
            }catch(Exception e) {
                Log.e("Erro", e.getMessage());
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            new RepositorioUsuario().setDataUsuarios(s);
        }
    }

    private class GetMensagensTurmas extends AsyncTask<String , Void, String> {
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        int usuarioId;

        @Override
        protected String doInBackground(String... url) {
            Log.e("BACK", "ASPDF");
            try {
                HttpURLConnection get = (HttpURLConnection) new URL("http://200.137.2.185/meucampus/service/mensagem/listaTurmas/"+ tipo + "/0").openConnection();
                get.setRequestMethod("GET");
                get.connect();
                br = new BufferedReader(new InputStreamReader(get.getInputStream()));
                data.append(br.readLine());
                Log.e("Mensagens turma:", data.toString());

                get.disconnect();
            }catch(Exception e) {
                Log.e("Erro", e.getMessage());
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            List<Mensagem> mensagens = new ArrayList<>();
            try{
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i<jsonArray.length(); i++){
                    Mensagem m1 = new Gson().fromJson(jsonArray.get(i).toString(), Mensagem.class);
                    m.add(m1);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            mList.setAdapter(new MensagemTurmasAdapter(m, getApplicationContext()));
        }
    }

    private class PostMensagem extends AsyncTask<String, Void, String>{
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        BufferedWriter bw = null;
        @Override
        protected String doInBackground(String... strings) {
            JSONObject json = new JSONObject();
            try {
                Log.e("url", strings[0]);
                HttpURLConnection connection = (HttpURLConnection) new URL (strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                json = new JSONObject(strings[1]);
                bw.write(String.valueOf(json));
                //jsonLengthU++;
                Log.e("asdf", String.valueOf(json));
                bw.flush();

                // br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //data.append(br.readLine());

                Log.e("Coonnection", connection.getResponseMessage() +" ......");
                if (connection.getResponseCode() == 200)
                    Log.e("CONNECTION", "DE BOA!");
                else
                    Log.e("PROB", connection.getResponseMessage());
                connection.disconnect();


            }catch (Exception e){
                //Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Log.e("EXCP", e.getMessage());
            }


            String datafim = data.toString();
            /*CriarArquivo criarArquivo = new CriarArquivo();
            if (criarArquivo.lerArquivo(getApplicationContext()).isEmpty()){
                criarArquivo.criarArquivo(json.toString(), getApplicationContext());
            }else{
                criarArquivo.editarArquivoPost(getApplicationContext(), "," + json.toString());
            }*/

            return datafim;
        }

        @Override
        protected void onPostExecute(String s) {


            Log.e("EXE", "" + jsonLengthU);
            Log.e("Execute", s +"");
        }
    }
}
