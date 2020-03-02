package com.meucampus.arthur.testez;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by renan on 01/06/2017.
 */
public class ActivityNovaConversaAluno extends AppCompatActivity {

    Button btn;
    EditText edt;
    String url1;
    Spinner spinner;
    ListView list;
    Toolbar mToolbar;
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
        url1  = getResources().getString(R.string.url);
        setContentView(R.layout.activity_nova_conversa_aluno);
        mToolbar = (Toolbar) findViewById(R.id.tb_chatn2);
        mToolbar.setTitle("Setores");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimarytext));
        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        //btn = (Button) findViewById(R.id.bttInicioC);
        //spinner = (Spinner) findViewById(R.id.spinnerEscSetor);
        list = (ListView) findViewById(R.id.list_setor);
        List<String> setores = new ArrayList<>();
        setores.add("COADES");
        setores.add("Direção geral");
        SetorAdapter adapter = new SetorAdapter(getApplicationContext(), setores);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<String> setores = new ArrayList<>();
                setores.add("COADES");
                setores.add("Direção geral");
                Bundle a = new Bundle();
                String tipo = setores.get(i).toString()+"/";
                if(tipo.contains("geral")){
                    tipo = "DG/";
                }
                a.putString("tipo", tipo);
                a.putString("idusuario", new RepositorioUsuario().getMatricula());
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtras(a);
                startActivity(intent);
            }
        });
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Spinner", spinner.getSelectedItem().toString()+".");
                Bundle a = new Bundle();
                a.putString("tipo", spinner.getSelectedItem().toString()+"/");
                a.putString("idusuario", new RepositorioUsuario().getMatricula());
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtras(a);
                startActivity(intent);
            }
        });*/
        /*Bundle a = new Bundle();
        //a.putInt("idusuario", id);

//        Log.e("Intent", id + "");
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtras(a);
        startActivity(intent);*/
    }
    public void toastErro(){
        Toast.makeText(getApplicationContext(), "Usuário não encontrado.", Toast.LENGTH_LONG).show();
    }
    private class GetId extends AsyncTask<String , Void, String> {
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        @Override
        protected String doInBackground(String... url) {

            try {
                HttpURLConnection get = (HttpURLConnection) new URL(url[0]).openConnection();
                get.setRequestMethod("POST");
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
            try{
                int id = Integer.parseInt(s);
                if (id == -1){
                    toastErro();
                }else {

                }
            }catch (Exception e){
                Log.e("EXCPTION: ",e.getMessage());
            }
        }
    }
}

