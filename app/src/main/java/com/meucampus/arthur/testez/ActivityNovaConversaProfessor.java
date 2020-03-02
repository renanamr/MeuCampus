package com.meucampus.arthur.testez;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.meucampus.arthur.testez.Services.ConversasRepositorio;
import com.meucampus.arthur.testez.Services.ServicoGetProfessores;
import com.meucampus.arthur.testez.Services.Turmas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityNovaConversaProfessor extends AppCompatActivity {


    Toolbar toolbar;
    static ListView list;

    List<String> matriculas = new ArrayList<>();
    List<String> turmas = new ArrayList<>();
    String[] nomesTurma = new String[100];
    String[] codigosTurma = new String[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_nova_conversa_professor);
        toolbar = (Toolbar) findViewById(R.id.tb_professores);
        list = (ListView) findViewById(R.id.list_professores);

        toolbar.setTitle("Selecione a turma");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //while (true) {
            if (new ConversasRepositorio().getFlag() == 1) {
                SetorAdapter setorAdapter = new SetorAdapter(getApplicationContext(), new ConversasRepositorio().getMatriculas());
                list.setAdapter(setorAdapter);
            } else {
                if(new BD(getApplicationContext()).listarTurmas().size()==0){
                    Toast.makeText(getApplicationContext(), "Carregando lista de professores...", Toast.LENGTH_SHORT).show();
                    new GetTurmas().execute();
                }else{
                    List<Turmas> lst = new BD(getApplicationContext()).listarTurmas();
                    for(int i = 0; i<lst.size(); i++){
                        matriculas.add(lst.get(i).getMatriculaProfessor());
                        turmas.add(lst.get(i).getDescricao());
                        codigosTurma[i] = lst.get(i).getCodigo();
                        new GetServidor().execute();
                    }
                }

            }



    }

    private class GetTurmasProfessor extends AsyncTask<String, Void, String> {
        BufferedReader br;
        StringBuffer data = new StringBuffer();

        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-diarios/2017/1/").openConnection();

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


            try {
                Log.e("Data", s);
            }catch (Exception e){
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray();
            final List<String> turmasDiario = new ArrayList<>();
            List<String> turmasDiarioN = new ArrayList<>();
            try {
                jsonArray= new JSONArray(s);
                for(int i = 0; i <jsonArray.length(); i++){
                    turmasDiario.add(jsonArray.getJSONObject(i).getInt("id")+"");
                    turmasDiarioN.add(jsonArray.getJSONObject(i).getString("nome"));
                    Log.e("Turmas diário: ", turmasDiario.get(i));
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            SetorAdapter setorAdapter = new SetorAdapter(getApplicationContext(), turmasDiarioN);
            list.setAdapter(setorAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("tipo", Integer.parseInt(turmasDiario.get(i)));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    private class GetTurmas extends AsyncTask<String, Void, String> {
        BufferedReader br;
        StringBuffer data = new StringBuffer();

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

            new GetDados().execute(codigosTurma);
        }
    }

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
            Log.e("lista: ", s.toString());
            final List<String> listCodigos = new ArrayList<>();
            for(int i = 0 ;i<matriculas.size(); i++){
                if(s.contains(matriculas.get(i))) {
                    Log.e("Ok", codigosTurma[i]);
                    Log.e("Ok", turmas.get(i));
                    listCodigos.add(codigosTurma[i]);

                    matriculasExibidas.add(turmas.get(i));
                }else{}
            }
            SetorAdapter setorAdapter = new SetorAdapter(getApplicationContext(), matriculasExibidas);
            list.setAdapter(setorAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("tipo", Integer.parseInt(listCodigos.get(i)));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }

    }
    /*

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
            Log.e("lista: ", s.toString());
            for(String professores : matriculas){
                if(s.contains(professores)) {
                    Log.e("Ok", professores);
                    matriculasExibidas.add(professores);
                }else
                    Log.e("P",professores);
            }
            for(int i = 0 ;i<matriculas.size(); i++){
                if(s.contains(matriculas.get(i))) {
                    Log.e("Ok", matriculas.get(i));
                    Log.e("Ok", nomesTurma[i]);
                    matriculasExibidas.add(nomesTurma[i]);
                }else{}
            }
            SetorAdapter setorAdapter = new SetorAdapter(getApplicationContext(), new ConversasRepositorio().getMatriculas());
            list.setAdapter(setorAdapter);
            list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("tipo", Integer.parseInt(matriculas.get(i)));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }*/
}
