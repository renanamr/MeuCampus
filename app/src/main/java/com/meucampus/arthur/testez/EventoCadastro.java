package com.meucampus.arthur.testez;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class EventoCadastro extends AppCompatActivity {

    Toolbar mToolbar;
    EditText edtTitulo;
    EditText edtDescricao;
    EditText edtImagem;
    static EditText edtInicio;
    static EditText edtFim;
    static EditText edtPub;
    Button btnCadastro;
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
        url1 = getResources().getString(R.string.url);
        setContentView(R.layout.activity_evento_cadastro);
        mToolbar = (Toolbar) findViewById(R.id.tb_notica_add1);
        edtTitulo = (EditText) findViewById(R.id.edtTitulo1);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao1);
        edtImagem = (EditText) findViewById(R.id.edtImagem1);
        edtInicio = (EditText) findViewById(R.id.edtInicio);
        edtPub = (EditText) findViewById(R.id.dataPub);
        edtPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment2("pub").show(getSupportFragmentManager(), "Data");
            }
        });
        edtInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment2("inicio").show(getSupportFragmentManager(), "Data");
            }
        });
        edtFim = (EditText) findViewById(R.id.edtTermino);
        edtFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment2("else").show(getSupportFragmentManager(), "Data");
            }
        });
        btnCadastro = (Button) findViewById(R.id.btnCadastro1);
        mToolbar.setTitle("Cadastro de eventos");
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
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtTitulo.getText().toString().isEmpty() && !edtDescricao.getText().toString().isEmpty()) {
                    Evento e = new Evento();
                    e.setTitulo(edtTitulo.getText()+"");
                    e.setDescricao(edtDescricao.getText()+"");
                    e.setDataInicio(edtInicio.getText()+"");
                    e.setDataFim(edtFim.getText()+"");
                    e.setDataPublicacao(edtPub.getText()+"");
                    e.setImageLink(edtImagem.getText()+"");
                    try {
                        JSONObject json = new JSONObject(new Gson().toJson(e));
                        String jsons = json.toString();
                        Log.e("JSONST", json.toString());
                        new PostAsynk().execute(url1+"evento/criar", jsons);
                    }catch(Exception exception){
                        Toast.makeText(getApplicationContext(), "Erro inesperado", Toast.LENGTH_LONG).show();
                    }

                    Toast.makeText(getApplicationContext(), "Evento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    edtTitulo.setText("");
                    edtDescricao.setText("");
                    edtImagem.setText("");
                    edtPub.setText("");
                    edtInicio.setText("");
                    edtFim.setText("");
                }else
                    Toast.makeText(getApplication(), "Os campos de título e descrição são obrigatórios.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class PostAsynk extends AsyncTask<String, Void, String> {
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        BufferedWriter bw = null;

        @Override
        protected String doInBackground(String... strings) {
            Log.e("DOINBA", "FASDFSADFASFAFSDFF");
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
                Log.e("asdf", String.valueOf(json));
                bw.flush();

                Log.e("RESPONSE", connection.getResponseMessage() );
                // br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //data.append(br.readLine());
                connection.disconnect();


            }catch (Exception e){
                //Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Log.e("EXCP", e.getMessage());
            }



            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("EXECUTE", s.toString()+"TETE");
        }
    }
    public static class DatePickerFragment2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public DatePickerFragment2(){

        }
        String edt;
        @SuppressLint("ValidFragment")
        public DatePickerFragment2(String s){
            edt = s;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month++;

            String data = day+"/"+month+"/"+year;
            Log.e("month:", month+".");
            Log.e("day: ", day+".");
            if (month <10 && day>10){
                data = day+"/"+"0"+month+"/"+year;
                Log.e("month", data);
            }else {
                if (month > 10 && day <10) {
                    data = "0" + day + "/" + month + "/" + year;
                }else{
                    if(month<10 && day<10){
                        data="0" + day + "/0" + month + "/" + year;
                    }
                }
            }

            if(edt.equals("inicio")){
                edtInicio.setText(data);
            }else{
                if(edt.equals("pub"))
                    edtPub.setText(data);
                else
                    edtFim.setText(data);
            }
        }
    }
}
