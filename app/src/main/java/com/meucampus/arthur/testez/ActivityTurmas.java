package com.meucampus.arthur.testez;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.meucampus.arthur.testez.Services.AcessoTurmas;
import com.meucampus.arthur.testez.Services.ServicoNotificacao;
import com.meucampus.arthur.testez.Services.Turmas;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityTurmas extends AppCompatActivity {
    static String materia;
    String periodo;
    Toolbar mToolbar;
    List<String> objetosJson = new ArrayList<>();
    TurmasAdapter adapter;
    ListView mList;
    ProgressBar progress;
    MaterialSearchView searchView;
    List<String> descricao = new ArrayList<>();
    @Override
    protected void onRestart() {
        if(new RepositorioUsuario().getToken().isEmpty()) {
            finish();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        super.onRestart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turmas);
        Intent intent = new Intent(getApplicationContext(), ServicoNotificacao.class);
        stopService(intent);
        mToolbar = (Toolbar) findViewById(R.id.tb_turmas1);
        mToolbar.setTitle("Turmas virtuais");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));

        mList = (ListView) findViewById(R.id.listTurmas);
        progress = (ProgressBar) findViewById(R.id.progress);
        setSupportActionBar(mToolbar);
        //new GetPeriodoLetivo().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-periodos-letivos/");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.seachView);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                adapter = new TurmasAdapter(getApplicationContext(), descricao, "1");
                mList.setAdapter(adapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!= null && !newText.isEmpty()){
                    List<String> lstFound = new ArrayList<String>();
                    for (String item: descricao){
                        if (item.toUpperCase().contains(newText.toUpperCase()))
                         lstFound.add(item);
                    }
                    adapter = new TurmasAdapter(getApplicationContext(), lstFound, "1");
                    mList.setAdapter(adapter);

                }else {
                    adapter = new TurmasAdapter(getApplicationContext(), descricao, "1");
                    mList.setAdapter(adapter);

                }
                return true;
            }
        });


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.e("BD_MAIN: TURMAS", new BD(getApplicationContext()).listarTurmas().size()+".");
        if(new BD(getApplicationContext()).listarTurmas().size() == 0)
            new GetTurmas().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/"+new RepositorioUsuario().getCaminhoPeriodo());
        else{
            Log.e("else", "else");
            final List<Turmas> turmas = new BD(getApplicationContext()).listarTurmas();

            for (Turmas t: turmas){
                descricao.add("{\"disciplina\":\""+t.getDescricao()+"\", \"nomeProfessor\":\""+t.getNomeProfessor()+"\"}");
            }
            adapter = new TurmasAdapter(getApplicationContext(), descricao, "1");
            mList.setAdapter(adapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(new RepositorioUsuario().getInternet())
                        Toast.makeText(getApplicationContext(), "Verifique sua conexão com a internet e reinicie o app.", Toast.LENGTH_LONG).show();
                    else{
                        String codigoDiario ="";
                        try {
                            Log.e("CODIGO material", new BD(getApplicationContext()).listarTurmas().get(i).getCodigo());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Bundle a = new Bundle();
                        a.putString("codigo", new BD(getApplicationContext()).listarTurmas().get(i).getCodigo());
                        Intent intent = new Intent(getApplicationContext(), ActivityTurmasDetalhes.class);
                        intent.putExtras(a);
                        startActivity(intent);
                    }
                }
            });
            progress.setVisibility(View.GONE);
        }


        //TESTAR URL PERIODO
        //new GetTurmas().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/2016/1");
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
                urlSuap = strings[0];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                Log.e("basicAuth: ", new RepositorioUsuario().getToken());
                HttpURLConnection connection = (HttpURLConnection) new URL(urlSuap).openConnection();

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
                periodo = jsonArray.getJSONObject(jsonArray.length()-1).getString("ano_letivo")+"/"+1;
                Log.e("PERIDO: ", periodo+".");
                progress.setVisibility(View.VISIBLE);
                new GetTurmas().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/"+periodo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class GetTurmas extends AsyncTask<String, Void, String> {
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        @Override
        protected String doInBackground(String... strings) {
            progress.setVisibility(View.VISIBLE);
            Intent i = new Intent(getApplicationContext(), ServicoNotificacao.class);
            stopService(i);
            try{
                progress.setVisibility(View.VISIBLE);
                urlSuap = strings[0];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                Log.e("basicAuth: ", strings[0]);
                HttpURLConnection connection = (HttpURLConnection) new URL(urlSuap).openConnection();

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
            Log.e("DATA POST", s);
            RepositorioUsuario repo = new RepositorioUsuario();

            JSONArray json;
            try{
                json = new JSONArray(s);
                for (int i = 0; i<json.length(); i++){
                    objetosJson.add(json.getJSONObject(i).toString());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            adapter = new TurmasAdapter(getApplicationContext(), objetosJson, "1");
            mList.setAdapter(adapter);
            mList.setDividerHeight(10);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String codigoDiario ="";
                    try {
                        JSONObject jsonObject = new JSONObject(objetosJson.get(i));
                        materia=(jsonObject.getString("disciplina"));
                        materia=materia.substring(11,materia.length());
                        codigoDiario = jsonObject.getString("codigo_diario");
                        Log.e("CODIGO", codigoDiario);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Bundle a = new Bundle();
                    a.putString("codigo", codigoDiario);
                    Intent intent = new Intent(getApplicationContext(), ActivityTurmasDetalhes.class);
                    intent.putExtras(a);
                    startActivity(intent);
                }
            });

            progress.setVisibility(View.GONE);
            Intent i = new Intent(getApplicationContext(), ServicoNotificacao.class);
            startService(i);



        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
}
