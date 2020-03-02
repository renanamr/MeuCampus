package com.meucampus.arthur.testez;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityChatCoades extends AppCompatActivity {

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
        setContentView(R.layout.activity_chat_coades);
        url1 = getResources().getString(R.string.url);
        tipo = getIntent().getStringExtra("tipo");
        tipo = tipo.substring(0, tipo.length()-2);

        new GetUsuarios().execute(url1+"mensagem/getUsuarios/" + tipo+ new RepositorioUsuario().getMatricula());
        fab = (FloatingActionButton) findViewById(R.id.novamensagem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityNovaConversaCoades.class);
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

    @Override
    protected void onRestart() {
        //if (tipo.equals("COADES/NC"))
        new GetUsuarios().execute(url1+"mensagem/getUsuarios" + tipo+"/"+ new RepositorioUsuario().getMatricula());
        //else
        //if(tipo.equals("DG/NC"))
        //    new GetUsuarios().execute(url1+"mensagem/dg");
        super.onRestart();
    }

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
                Log.e("URL", url[0]+" .");
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


            try {
                Log.e("dentrossssss", s);
                JSONArray jsonArray = new JSONArray(s);

                int jsonLength = jsonArray.length();
                Log.d("JSONARRAY", jsonArray.getJSONObject(0).length() + "");
                usuarios = new ArrayList<>();

                for(int i =0; i<jsonLength; i++){
                    usuarios.add(new Gson().fromJson(jsonArray.get(i).toString(), Usuario.class));
                }


                //listView = (ListView) findViewById(R.id.list_usuarios_mensagem);

                if (usuarios==null){
                    Log.e("USUARIOS", "NULL");
                }else Log.e("USUARIOS", "NOT NULL");
                return (usuarios);

            }catch (Exception e){
                Log.e("EXCPTION: ",e.getMessage() + "");
                return null;
            }


        }

        /*private void set(List<Usuario> usuarios){
            listView = (ListView) findViewById(R.id.list_usuarios_mensagem);
            ChatUsersAdapter chat = new ChatUsersAdapter(getApplicationContext(), usuarios);
            listView.setAdapter(chat);
        }*/

        @Override
        protected void onPostExecute(final List<Usuario> usuarios) {

            aviso(a);//verificar depois

            try {
                List<String> parametro = new ArrayList<>();
                for (int i = 0; i < usuarios.size(); i++) {
                    Log.e("parametro", usuarios.get(i).getMatricula());
                    parametro.add(usuarios.get(i).getMatricula());
                }
                Log.e("RETORNO DO PARAMETRO", parametro.get(0).toString());
                new GetUltima().execute(parametro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public class GetUltima extends AsyncTask<List<String>,Void, List<Usuario>>{
            StringBuffer data = new StringBuffer();
            BufferedReader br = null;
            BufferedWriter bw = null;



            @Override
            protected List<Usuario> doInBackground(List<String>... strings) {
                List<Usuario> retorno = new ArrayList<>();
                Log.e("String show", strings[0].get(0)+" a");
                for (int i = 0;i < strings.length; i++){


                    /*Type type = new TypeToken<ArrayList<Mensagem>>(){}.getType();
                    ArrayList<Mensagem> arrayMensagem = new Gson().fromJson(json.toString(), type);
                    usuario.setMensagens(arrayMensagem);*/

                    /*try {
                        usuario.setMensagens((List<Mensagem>) json.get("mensagens"));
                    }catch (Exception e){
                        Log.e("LIST EXCEPTION", e.getMessage());
                    }*/
                    //usuario.setMatricula(json.getString("matricula"));

                    try {
                        Log.e("String atual", ":" +strings[i].toString());
                        HttpURLConnection connection = (HttpURLConnection) new URL(url1 + "mensagem/getUltima/" + tipo + strings[0].get(i).toString()).openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();
                        Log.e("CONEXÃO", "OK");

                        data = new StringBuffer();
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        data.append(br.readLine());
                        Log.e("CONEXÃO", "OK");


                        if (connection.getResponseCode() == 200)
                            Log.e("CONNECTION", "200");
                        else
                            Log.e("PROB", connection.getResponseMessage());
                        connection.disconnect();
                        Log.d("retornou isso aqui boy", data.toString());
                        Usuario usuario= new Usuario();
                        List<Mensagem> ut = new ArrayList<>();
                        Mensagem m = new Mensagem();


                        usuario.setMatricula(new JSONObject(data.toString()).getString("matriculaRemetente"));
                        m.setTexto(new JSONObject(data.toString()).getString("texto"));
                        Log.e("M1111", m.getTexto().toString()+".");
                        ut.add(m);
                        Log.e("GEt do maluco", ut.get(0).getTexto()+".");
                        usuario.setMensagens(ut);
                        //usuario.setMatricula();
                        retorno.add(usuario);
                        usuario.setLastM(m.getTexto());
                        Log.e("MATRICULA", usuario.getMatricula() + "null");
                        Log.e("MENSAGEM", usuario.getLastM() + "null");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return retorno;
            }


            @Override
            protected void onPostExecute(final List<Usuario> usuarios) {
                aviso(a);
                if (usuarios == null) {
                    return;
                }
                Log.e("ON POSTTT", "OK");
                listView = (ListView) findViewById(R.id.list_usuarios_mensagem);
                ChatUsersAdapter chat = new ChatUsersAdapter(getApplicationContext(), usuarios);
                listUsuarios = usuarios;
                listView.setAdapter(chat);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("Onclick", "entrei po");
                        Bundle a = new Bundle();
                        a.putString("idusuario", usuarios.get(i).getMatricula());
                        a.putString("tipo", tipo);
                        Log.e("matriculaaaa", usuarios.get(i).getMatricula()+" a");
                        Log.e("Intent", usuarios.get(i).getId()+"");
                        new RepositorioUsuario().setMat(usuarios.get(i).getMatricula());
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtras(a);
                        startActivity(intent);
                    }
                });


            }

        }
    }

}
