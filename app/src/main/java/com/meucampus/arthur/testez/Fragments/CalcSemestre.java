package com.meucampus.arthur.testez.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.meucampus.arthur.testez.BD;
import com.meucampus.arthur.testez.R;
import com.meucampus.arthur.testez.RepositorioUsuario;
import com.meucampus.arthur.testez.Services.Turmas;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by renan on 16/11/2016.
 */

public class CalcSemestre extends Fragment {
    String periodo;
    EditText j1, j2, j3;
    TextView k2;
    Button resultadoS;
    double[] nCalc = new double[3];
    double menor = 100,teste;
    int[] CalcMInt= new int[3];
    double[] CalcM= new double[3];
    double[] result= new double[3];
    int branco,resultInt,faltaInt,Pv;
    int troca,probabiladadeP;
    public void recriar(){
        j1.setText("");
        j2.setText("");
        j3.setText("");
        branco=0;
        troca=0;
        Pv=0;
        menor=101;
        for (int i=0;i<3;i++){
            CalcMInt[i]=0;
            CalcM[i]=0;
            result[i]=0;
        }
    }
    Spinner spinner;
    String turma;
    ArrayList<String> spinnerArray;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.calc_semestre, container, false);
        context = getContext();
        spinner = (Spinner) rootView.findViewById(R.id.spinner3);
        j1 = (EditText) rootView.findViewById(R.id.editText);
        j2 = (EditText) rootView.findViewById(R.id.editText2);
        j3 = (EditText) rootView.findViewById(R.id.editText3);
        k2 = (TextView) rootView.findViewById(R.id.TxtSProva);
        resultadoS = (Button) rootView.findViewById(R.id.BtSResultado);
        //new GetTurmas().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/2017/1/");

        Log.e("calc: ", new BD(context).listarTurmas().size()+".");
        if(new BD(context).listarTurmas().size() == 0)
            new GetTurmas().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/"+new RepositorioUsuario().getCaminhoPeriodo());
        else{

            List<Turmas> lista = new BD(context).listarTurmas();
            spinnerArray= new ArrayList<String>();
            spinnerArray.add("Obter notas...");
            try {
                for (Turmas t : lista){
                    spinnerArray.add(t.getDescricao());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerArrayAdapter1);//

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("POSITION", position+".");
                    turma = parent.getSelectedItem().toString();
                    if (position!=0)
                        try {
                            List<Turmas> lista2 = new BD(context).listarTurmas();
                            j1.setText("");
                            j2.setText("");
                            Log.e("position", position+"");
                            Log.e("turma::", lista2.get(position-1).getNota2()+"l");
                            int n11 = lista2.get(position -1).getNota1();
                            j1.setText(n11+"");
                            int n22 = lista2.get(position -1).getNota2();
                            j2.setText(n22+"");

                        }catch(Exception e){e.printStackTrace();}

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        resultadoS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!j1.getText().toString().isEmpty()) {
                    nCalc[0] = Double.parseDouble(j1.getText().toString());
                }else{
                    nCalc[0] = 0;
                    branco=branco+1;
                }
                if (!j2.getText().toString().isEmpty()) {
                    nCalc[1] = Double.parseDouble(j2.getText().toString());
                }else{
                    nCalc[1] = 0;
                    branco=branco+1;
                }
                if (!j3.getText().toString().isEmpty()) {
                    nCalc[2] = Double.parseDouble(j3.getText().toString());

                }
                if (Pv==1) {
                    for (int i = 1; i >-1 ; i--) {
                        if (nCalc[i] < menor) {
                            menor = nCalc[i];
                        }
                    }
                    for (int i = 1; i > -1; i--) {
                        if ((menor < nCalc[2]) && (menor == nCalc[i]) && (troca == 0)) {
                            nCalc[i] = nCalc[2];
                            troca = troca + 1;
                        }
                    }
                }
                result[2]=(result[0] + nCalc[2])/2;
                result[0] = ((nCalc[0] * 2) + (nCalc[1] * 3)) / 5;
                for (int i=0;i<3;i++){
                    if (resultInt<result[i]) {
                        resultInt = (int) result[i];
                    }
                }
                if(branco==1&&result[0]<60){
                    CalcM[0]=((60-result[0])/3)*5;
                    faltaInt=(int) CalcM[0];
                    teste=((nCalc[0] * 2) + (faltaInt * 3)) / 5;
                    if(teste!=60){
                        faltaInt=faltaInt+1;
                    }
                    probabiladadeP=101-faltaInt;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Cursando");
                    builder.setMessage("Media anual: " + resultInt+"\nVocê precisa de: " + faltaInt+" no 2º Bimestre\nProbalidade de passar: "+ probabiladadeP);
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            branco=0;
                        }
                    });
                    builder.show();
                }
                if (((result[0] < 60)&&(branco==0)&&(Pv==1)&&(result[2] < 60))||(result[0]<20)&&(branco==0)) {
                    k2.setVisibility(View.INVISIBLE);
                    j3.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Reprovado");
                    builder.setMessage("Media anual: " + resultInt+"");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            recriar();
                        }
                    });
                    builder.show();
                }

                if ((result[0] < 60)&&(branco==0)&&(Pv==0)&&(result[0]>=20)) {
                    k2.setVisibility(View.VISIBLE);
                    j3.setVisibility(View.VISIBLE);
                    result[2]=(result[0] + CalcMInt[1])/2;
                    CalcM[1]=(60-result[2])*2;
                    CalcMInt[1]=(int) CalcM[1];
                    teste=(result[0] + CalcMInt[1])/2;
                    if (teste != 60) {
                        CalcMInt[1] = CalcMInt[1] + 1;
                    }

                    if(nCalc[1]<=nCalc[0]){
                        nCalc[1]=0;
                        result[0] = ((nCalc[0] * 2) + (nCalc[1] * 3)) / 5;
                        CalcM[0]=((60-result[0])/3)*5;
                        CalcMInt[0] = (int) CalcM[0];
                        teste=((nCalc[0] * 2) + (faltaInt * 3)) / 5;
                        if(teste!=60){
                            CalcMInt[0] = CalcMInt[0] + 1;
                        }
                    }else{
                        nCalc[0]=0;
                        result[0] = ((nCalc[0] * 2) + (nCalc[1] * 3)) / 5;
                        CalcM[0]=((60-result[0])/2)*5;
                        CalcMInt[0] = (int) CalcM[0];
                        teste=((faltaInt * 2) + (nCalc[1] * 3)) / 5;
                        if(teste!=60){
                            CalcMInt[0] = CalcMInt[0] + 1;
                        }

                    }
                    for (int i=0;i<2;i++){
                        if (menor >= CalcMInt[i]) {
                            faltaInt= CalcMInt[i];
                        }
                    }
                    probabiladadeP=101-faltaInt;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Prova Final");
                    builder.setMessage("Você precisa de: " + faltaInt+" na Prova Final\nProbabiladade de passar: "+ probabiladadeP+"%");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Pv=1;
                        }
                    });
                    builder.show();
                }

                if (result[0]>=60&&branco==0) {
                    k2.setVisibility(View.INVISIBLE);
                    j3.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Aprovado");
                    builder.setMessage("Media anual: " + resultInt+" ");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            recriar();
                        }
                    });
                    builder.show();
                }
                if (branco==1){
                    Toast.makeText(getActivity(),"Operação invalida",Toast.LENGTH_SHORT).show();
                    branco=0;

                }
            }
        }); return rootView;
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
                periodo = jsonArray.getJSONObject(jsonArray.length()-1).getString("ano_letivo")+"/"+jsonArray.getJSONObject(jsonArray.length()-1).getString("periodo_letivo");
                Log.e("PERIDO: ", periodo+".");
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
        String dados;
        @Override
        protected void onPostExecute(String s) {
            Log.e("DATA POST", s);
            dados = s;
            spinnerArray= new ArrayList<String>();
            spinnerArray.add("Obter notas...");
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray= new JSONArray(s);
                for(int i = 0; i <jsonArray.length(); i++){
                    spinnerArray.add(jsonArray.getJSONObject(i).getString("disciplina"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerArrayAdapter);//

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    turma = parent.getSelectedItem().toString();
                    if (position !=0)
                        try {
                            j1.setText("");
                            j2.setText("");
                            JSONArray j = new JSONArray(dados);
                            Log.e("position", position+"");
                            int n11 = Integer.parseInt(j.getJSONObject(position-1).getJSONObject("nota_etapa_1").getString("nota"));
                            Log.e("nota1",j.getJSONObject(position-1).getString("nota_etapa_1"));
                            j1.setText(n11+"");
                            int n22 = Integer.parseInt(j.getJSONObject(position-1).getJSONObject("nota_etapa_2").getString("nota"));
                            j2.setText(n22+"");

                        }catch(Exception e){e.printStackTrace();}

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        }

    }
}