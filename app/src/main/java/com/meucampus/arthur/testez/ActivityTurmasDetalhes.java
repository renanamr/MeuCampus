package com.meucampus.arthur.testez;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityTurmasDetalhes extends AppCompatActivity {

    Toolbar mToolbar;
    ListView mList;
    TurmasAdapter adapter;
    ProgressBar progress;
    List<String> objetosJson= new ArrayList<>();

    @Override
    protected void onRestart() {
        finish();
        Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(i);
        super.onRestart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turmas_detalhes);



        mToolbar = (Toolbar) findViewById(R.id.tb_turmas2);
        mToolbar.setTitle("Materiais de aula");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));
        progress = (ProgressBar) findViewById(R.id.progress_detalhes);
        mList = (ListView) findViewById(R.id.listTurmasMateriais);
        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progress.setVisibility(View.VISIBLE);

        Bundle b = getIntent().getExtras();
        Log.e("Bundle material:", b.getString("codigo"));
        new GetTurmas().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/turma-virtual/" + b.getString("codigo"));
    }
    private class GetTurmas extends AsyncTask<String, Void, String> {
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        @Override
        protected String doInBackground(String... strings) {

            try{
                urlSuap = strings[0];
                Log.e("material:::", urlSuap);

                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                Log.e("basicAuth: ", strings[0]);
                HttpURLConnection connection = (HttpURLConnection) new URL(urlSuap).openConnection();
                Log.e("TOKEN", new RepositorioUsuario().getToken());
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
            Log.e("DATA POST material:::", s);
            RepositorioUsuario repo = new RepositorioUsuario();

            JSONObject json;
            JSONArray jsonArray = new JSONArray();
            try{
                json = new JSONObject(s);
                jsonArray = json.getJSONArray("materiais_de_aula");
                Log.e("JSON ARRAY", jsonArray.toString());
                for (int i = 0; i<jsonArray.length(); i++){
                    objetosJson.add(jsonArray.getJSONObject(i).getString("descricao"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            if(objetosJson.size()==0)
                Toast.makeText(getApplicationContext(), "Sem materiais cadastrados!", Toast.LENGTH_SHORT).show();
            final String jsonString = jsonArray.toString();
            adapter = new TurmasAdapter(getApplicationContext(), objetosJson, "2");
            mList.setAdapter(adapter);
            mList.setDividerHeight(4);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try{

                        Uri uri = Uri.parse("https://suap.ifrn.edu.br" + new JSONArray(jsonString).getJSONObject(i).getString("url"));
                        Log.e("URL", "https://suap.ifrn.edu.br" + new JSONArray(jsonString).getJSONObject(i).getString("url"));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            progress.setVisibility(View.GONE);




        }

    }
}
