package com.meucampus.arthur.testez;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityNovaConversaCoades extends AppCompatActivity {

    Button btn;
    EditText edt;
    String url1;
    @Override
    protected void onRestart() {
        /*finish();
        Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(i);*/
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
        setContentView(R.layout.activity_nova_conversa_coades);
        btn = (Button) findViewById(R.id.btn_conversar);
        edt = (EditText) findViewById(R.id.matricula_m);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetId().execute(url1+"mensagem/buscarId/" + edt.getText());
            }
        });
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
                    Bundle a = new Bundle();
                    a.putInt("idusuario", id);

                    Log.e("Intent", id + "");
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtras(a);
                    startActivity(intent);
                }
            }catch (Exception e){
                Log.e("EXCPTION: ",e.getMessage());
            }
        }
    }
}
