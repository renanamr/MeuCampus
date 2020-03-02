package com.meucampus.arthur.testez;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * Created by renan on 01/06/2017.
 */
public class ActivityChatAluno extends AppCompatActivity {

    public static List<Usuario> listUsuarios = new ArrayList<>();
    ListView listView;
    Toolbar toolbar;
    Button limpar;
    String url1;
    FloatingActionButton fab;
    String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_aluno);
        url1 = getResources().getString(R.string.url);
        tipo = getIntent().getStringExtra("tipo");
        tipo = tipo.substring(0, tipo.length()-2);

        new GetUsuarios().execute(url1+"mensagem/dg");
        fab = (FloatingActionButton) findViewById(R.id.novamensagem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityNovaConversaAluno.class);
                startActivity(i);

            }
        });
        limpar = (Button) findViewById(R.id.btnLimpar);
        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BD b = new BD(getApplicationContext());
                b.delete();
                Log.e("LER ARQUIVO", "NULL PLS " );
            }
        });
        toolbar = (Toolbar) findViewById(R.id.tb_chatinicio);
        toolbar.setTitle("Caixa de entrada");
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
//pecisamudar
    @Override
    protected void onRestart() {
        if (tipo.equals("COADES/NC"))
            new GetUsuarios().execute(url1+"mensagem/listaUsuarios");
        else
        if(tipo.equals("DG/NC"))
            new GetUsuarios().execute(url1+"mensagem/dg");
        super.onRestart();
    }
//ate aqui
    public void aviso(int i){
        TextView edt = (TextView) findViewById(R.id.semmsg);
        if (i ==0)
            edt.setVisibility(View.VISIBLE);
        else
            edt.setVisibility(View.INVISIBLE);
    }
    private class GetUsuarios extends AsyncTask<String , Void, List<Usuario>> {
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        int a =0;
        @Override
        protected List<Usuario> doInBackground(String... url) {
            Log.e("BACK", "ASPDF");
            try {
                HttpURLConnection get = (HttpURLConnection) new URL(url[0]).openConnection();
                get.setRequestMethod("GET");

                get.connect();
                Log.e("Log", "1");
                br = new BufferedReader(new InputStreamReader(get.getInputStream()));
                Log.e("Log", "2");
                data.append(br.readLine());


                Log.e("LogData", data.toString());

                get.disconnect();

            }catch(Exception e) {
                Log.e("Erro", e.getMessage());

            }
            String s = data.toString();

            if (s.equals("[]")){
                a=0;
                return null;
            }else{
                a=  1;
            }
            List<Usuario> usuarios;

            StringBuffer data = new StringBuffer();
            BufferedReader br = null;
            BufferedWriter bw = null;
            try {
                Log.e("dentro", s);
                JSONArray jsonArray = new JSONArray(s);

                int jsonLength = jsonArray.length();
                Log.d("JSONARRAY", jsonArray.getJSONObject(0).length() + "");
                usuarios = new ArrayList<>();

                //Log.d("JSON2", jsonArray.get(1).toString());
                for (int i = 0;i < jsonLength; i++){
                    Log.e("JSONARRAYLENGTH", jsonArray.length()+"");
                    Log.d("INT I", i +"");
                    /*Type type = new TypeToken<ArrayList<Mensagem>>(){}.getType();
                    ArrayList<Mensagem> arrayMensagem = new Gson().fromJson(json.toString(), type);
                    usuario.setMensagens(arrayMensagem);*/

                    /*try {
                        usuario.setMensagens((List<Mensagem>) json.get("mensagens"));
                    }catch (Exception e){
                        Log.e("LIST EXCEPTION", e.getMessage());
                    }*/
                    //usuario.setMatricula(json.getString("matricula"));


                    JSONObject json = jsonArray.getJSONObject(i);
                    Log.e("JSON", json.toString()+"");
                    Usuario usuario = new Gson().fromJson(json.toString(), Usuario.class);

                    usuario.setId(json.getInt("id"));

                    Log.e("CONEXÃO", "OK");
                    String url2="";
                    if (tipo.equals("COADES/NC"))
                        url2 = url1+"mensagem/getUltima";
                    else
                    if(tipo.equals("DG/NC"))
                        url2 = url1+"mensagem/dg/getUltima";
                    HttpURLConnection connection = (HttpURLConnection) new URL (url2).openConnection();


                    connection.setRequestMethod("POST");

                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.connect();
                    Log.e("CONEXÃO", "OK");


                    bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

                    bw.write(String.valueOf(new Gson().toJson(usuario)));
                    //jsonLengthU++;
                    Log.e("asdf", String.valueOf(new Gson().toJson(usuario)));
                    bw.flush();

                    data= new StringBuffer();
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    data.append(br.readLine());
                    Log.e("CONEXÃO", "OK");


                    if (connection.getResponseCode() == 200)
                        Log.e("CONNECTION", "200");
                    else
                        Log.e("PROB", connection.getResponseMessage());
                    connection.disconnect();
                    Log.d ("DATA", data.toString());
                    Mensagem mensagem = new Gson().fromJson(data.toString(), Mensagem.class);
                    List<Mensagem> mensagensUsuario = new ArrayList<>();
                    mensagensUsuario.add(mensagem);
                    usuario.setMensagens(mensagensUsuario);
                    usuarios.add(usuario);
                    Log.e("MATRICULA", usuario.getMatricula() + "null");
                    Log.e("MENSAGEM", usuario.getMensagens().get(0) + "null");

                }


                listView = (ListView) findViewById(R.id.list_usuarios_mensagem);

                if (usuarios==null){
                    Log.e("USUARIOS", "NULL");
                }else Log.e("USUARIOS", "NOT NULL");
                return (usuarios);

            }catch (Exception e){
                Log.e("EXCPTION: ",e.getMessage() + "");
                return null;
            }

        }

        private void set(List<Usuario> usuarios){
            listView = (ListView) findViewById(R.id.list_usuarios_mensagem);
            ChatUsersAdapter chat = new ChatUsersAdapter(getApplicationContext(), usuarios);
            listView.setAdapter(chat);
        }

        @Override
        protected void onPostExecute(final List<Usuario> usuarios) {
            aviso(a);
            if (usuarios == null) {
                return;
            }
            listView = (ListView) findViewById(R.id.list_usuarios_mensagem);
            ChatUsersAdapter chat = new ChatUsersAdapter(getApplicationContext(), usuarios);
            listUsuarios = usuarios;
            listView.setAdapter(chat);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle a = new Bundle();
                    a.putInt("idusuario", usuarios.get(i).getId());
                    a.putString("tipo", tipo);
                    Log.e("Intent", usuarios.get(i).getId()+"");
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtras(a);
                    startActivity(intent);
                }
            });


        }
    }

}

