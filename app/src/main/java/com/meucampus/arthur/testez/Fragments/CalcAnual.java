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
public class CalcAnual extends Fragment {

    String periodo;
    EditText n1, n2, n3, n4,n5;
    TextView m2;
    Button resultado;
    String ProvaTal;
    int branco,faltaInt,resultInt;
    int troca,ProvaF,ProbabilidadePInt;
    double[] nCalc =new double[5];
    int[] CalcMInt= new int[3];
    double[] CalcM= new double[3];
    double[] result= new double[3];
    double teste,ProbabilidadeP;
    double menor=100;
    public void recriar(){
        n1.setText("");
        n2.setText("");
        n4.setText("");
        n3.setText("");
        n5.setText("");
        branco=0;
        ProvaF=0;
        troca=0;
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

        final View rootView = inflater.inflate(R.layout.calc_anual, container, false);
        context = getContext();
        n1 = (EditText) rootView.findViewById(R.id.EdtCalc);
        n2 = (EditText) rootView.findViewById(R.id.Edtcalc1);
        n3 = (EditText) rootView.findViewById(R.id.Edtcalc2);
        n4 = (EditText) rootView.findViewById(R.id.EdtCalc3);
        n5 = (EditText) rootView.findViewById(R.id.Edtcalc4);
        m2 = (TextView) rootView.findViewById(R.id.TxtProvaFinal);

        spinner = (Spinner) rootView.findViewById(R.id.spinner2);
        resultado = (Button) rootView.findViewById(R.id.buttoncalc);
        spinnerArray= new ArrayList<String>();
        spinnerArray.add("Obtendo notas...");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
        //new GetPeriodoLetivo().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-periodos-letivos/");
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
                            n1.setText("");
                            n2.setText("");
                            n3.setText("");
                            n4.setText("");
                            Log.e("position", position+"");
                            Log.e("turma::", lista2.get(position-1).getNota2()+"l");
                            int n11 = lista2.get(position -1).getNota1();
                            n1.setText(n11+"");
                            int n22 = lista2.get(position -1).getNota2();
                            n2.setText(n22+"");
                            int n33= lista2.get(position -1).getNota3();
                            n3.setText(n33+"");
                            int n44= lista2.get(position -1).getNota4();
                            n4.setText(n44+"");
                        }catch(Exception e){e.printStackTrace();}

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }



        resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branco = 0;
                Log.e("Click", "Teste");

                Log.e("Branco: ", branco+"");
                if (!n1.getText().toString().equals("")) {
                    nCalc[0] = Double.parseDouble(n1.getText().toString());
                }else{
                    nCalc[0] = 0;
                    branco=branco+1;
                }
                if (!n2.getText().toString().equals("")) {
                    nCalc[1] = Double.parseDouble(n2.getText().toString());
                }else{
                    nCalc[1] = 0;
                    branco=branco+3;
                }
                if (!n3.getText().toString().equals("")) {
                    nCalc[2] = Double.parseDouble(n3.getText().toString());
                }else{
                    nCalc[2] = 0;
                    branco=branco+10;
                }

                if (!n4.getText().toString().equals("")) {
                    nCalc[3] = Double.parseDouble(n4.getText().toString());
                }else{
                    nCalc[3] = 0;
                    branco=branco+20;
                }
                if (!n5.getText().toString().equals("")) {
                    nCalc[4] = Double.parseDouble(n5.getText().toString());
                }
                Log.e("Branco", branco+"");
                Log.e("n1", n1.getText().toString());
                if(ProvaF==1) {
                    for (int i = 4; i >-1; i--) {
                        if (nCalc[i] < menor) {
                            menor = nCalc[i];
                        }
                    }
                    for (int i = 4; i >-1; i--) {
                        if ((menor < nCalc[4]) && (menor == nCalc[i]) && (troca == 0)) {
                            nCalc[i] = nCalc[4];

                            troca = troca + 1;
                        }
                    }
                }
                result[2]=(result[0] + nCalc[4])/2;
                result[0] = ((nCalc[0] * 2) + (nCalc[1] * 2) + (nCalc[2] * 3) + (nCalc[3] * 3)) / 10;
                for (int i=0;i<3;i++){
                    if (resultInt<result[i]) {
                        resultInt = (int) result[i];
                    }
                }

                if(branco==30&&result[0]<60){
                    CalcM[0]=((60-result[0])/6)*10;
                    faltaInt=(int) CalcM[0];
                    teste=((nCalc[0] * 2) + (nCalc[1] * 2) + (faltaInt * 3) + (faltaInt * 3)) / 10;
                    if(teste!=60){
                        faltaInt=faltaInt+1;
                    }
                    ProbabilidadeP= Math.pow(101-faltaInt,2)/Math.pow(100,1);
                    ProbabilidadePInt=(int) ProbabilidadeP;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Cursando");
                    builder.setMessage("Média anual: " + resultInt+"\nVocê precisa de: " + faltaInt+" no 3° e 4º Bimestre"+"\nProbabilidade de passar: "+ ProbabilidadePInt+"%");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            branco=0;
                        }
                    });
                    builder.show();
                }
                if (branco==33&&result[0]<60){
                    CalcM[0]=((60-result[0])/8)*10;
                    faltaInt=(int) CalcM[0];
                    teste=((nCalc[0] * 2) + (nCalc[1] * 2) + (nCalc[2] * 3) + (faltaInt * 3)) / 10;
                    if(teste!=60){
                        faltaInt=faltaInt+1;
                    }
                    ProbabilidadeP= Math.pow(101-faltaInt,3)/Math.pow(100,2);
                    ProbabilidadePInt=(int) ProbabilidadeP;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Cursando");
                    builder.setMessage("Média anual: " + resultInt+"\nVocê precisa de:" + faltaInt+" no 2°,3° e 4º "+"\nProbabilidade de passar: "+ ProbabilidadePInt+"%");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            branco=0;
                        }
                    });
                    builder.show();
                }

                Log.e("result:", result[0]+"" );
                if(branco==20&&result[0]<60){
                    CalcM[0]=((60-result[0])/3)*10;
                    faltaInt=(int) CalcM[0];
                    teste=((nCalc[0] * 2) + (nCalc[1] * 2) + (nCalc[2] * 3) + (faltaInt * 3)) / 10;
                    if(teste!=60){
                        faltaInt=faltaInt+1;
                    }
                    ProbabilidadeP=101-faltaInt;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Cursando");
                    builder.setMessage("Média anual: " + resultInt+"\nVocê precisa de: " + faltaInt+" no 4º Bimestre\nProbabilidade de passar: "+ (int)ProbabilidadeP+"%");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            branco=0;
                        }
                    });
                    builder.show();
                }

                if ((result[0]<20)&&(branco==0)||((result[2] < 60)&&(result[0] < 60)&&(branco==0)&&(ProvaF==1))) {
                    m2.setVisibility(View.INVISIBLE);
                    n5.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Reprovado");
                    builder.setMessage("Média anual: " + resultInt+"");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            recriar();
                        }
                    });
                    builder.show();
                }

                if ((result[0]>=20)&&(result[0] < 60)&&(branco==0)&&(ProvaF==0)) {
                    m2.setVisibility(View.VISIBLE);
                    n5.setVisibility(View.VISIBLE);

                    result[2]=(result[0] + CalcMInt[1])/2;
                    CalcM[1]=(60-result[2])*2;
                    CalcMInt[1]=(int) CalcM[1];
                    teste=(result[0] + CalcMInt[1])/2;
                    if (teste != 60) {
                        CalcMInt[1] = CalcMInt[1] + 1;
                    }

                    if ((nCalc[3] <= nCalc[1]) && (nCalc[3] <= nCalc[2]) && (nCalc[3] <= nCalc[0])) {
                        result[1] = ((nCalc[0] * 2) + (nCalc[1] * 2) + (nCalc[2] * 3) + (CalcMInt[0] * 3)) / 10;
                        CalcM[0] = ((60 - result[1]) / 3) * 10;
                        CalcMInt[0] = (int) CalcM[0];
                        teste = ((nCalc[0] * 2) + (nCalc[1] * 2) + (nCalc[2] * 3) + (faltaInt * 3)) / 10;
                        if (teste != 60) {
                            CalcMInt[0] = CalcMInt[0] + 1;
                        }
                    } else {
                        if ((nCalc[2] <= nCalc[0]) && (nCalc[2] <= nCalc[1]) && (nCalc[2] <= nCalc[3])) {
                            result[1] = ((nCalc[0] * 2) + (nCalc[1] * 2) + (CalcMInt[0] * 3) + (nCalc[3] * 3)) / 10;
                            CalcM[0] = ((60 - result[1]) / 3) * 10;
                            CalcMInt[0] = (int) CalcM[0];
                            teste = ((nCalc[0] * 2) + (nCalc[1] * 2) + (faltaInt * 3) + (nCalc[3] * 3)) / 10;
                            if (teste != 60) {
                                CalcMInt[0] = CalcMInt[0] + 1;
                            }


                        } else {
                            if ((nCalc[1] <= nCalc[0]) && (nCalc[1] <= nCalc[2]) && (nCalc[1] <= nCalc[3])) {
                                result[1] = ((nCalc[0] * 2) + (CalcMInt[0] * 2) + (nCalc[2] * 3) + (nCalc[3] * 3)) / 10;
                                CalcM[0] = ((60 - result[1]) / 2) * 10;
                                CalcMInt[0] = (int) CalcM[0];
                                teste = ((nCalc[0] * 2) + (faltaInt * 2) + (nCalc[2] * 3) + (nCalc[3] * 3)) / 10;
                                if (teste != 60) {
                                    CalcMInt[0] = CalcMInt[0] + 1;
                                }
                            } else {
                                result[1] = ((CalcMInt[0] * 2) + (nCalc[1] * 2) + (nCalc[2] * 3) + (nCalc[3] * 3)) / 10;
                                CalcM[0] = ((60 - result[1]) / 2) * 10;
                                CalcMInt[0] = (int) CalcM[0];
                                teste = ((faltaInt * 2) + (nCalc[1] * 2) + (nCalc[2] * 3) + (nCalc[3] * 3)) / 10;
                                if (teste != 60) {
                                    CalcMInt[0] = CalcMInt[0] + 1;
                                }

                            }
                        }
                    }
                    for (int i=0;i<2;i++){
                        if (menor >= CalcMInt[i]) {
                            faltaInt= CalcMInt[i];
                        }
                    }

                    ProbabilidadeP=101-faltaInt;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Prova Final");
                    builder.setMessage("Você precisa de: " + faltaInt + " na Prova Final\nProbabilidade de passar: "+ (int)ProbabilidadeP+"%");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ProvaF = 1;
                        }
                    });

                    builder.show();
                }
                if (result[0]>=60 && branco==20){
                    m2.setVisibility(View.INVISIBLE);
                    n5.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Aprovado");
                    builder.setMessage("Média anual: " + resultInt+"\nCursando...");
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            recriar();
                        }
                    });
                    builder.show();
                }
                if ((result[0]>=60&&branco==0)||(result[2]>=60&&branco==0)){
                    if (result[0]>=80)
                        ProvaTal="Habilitado";
                    else
                        ProvaTal="Desabilitado";
                    m2.setVisibility(View.INVISIBLE);
                    n5.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Aprovado");
                    builder.setMessage("Média anual: " + resultInt+"\nProva de Tal: " + ProvaTal);
                    builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           recriar();
                        }
                    });
                    builder.show();

                }
                if ((branco==23)||(branco==21)||(branco==11)||(branco==13)||(branco==1)||(branco==4)||(branco==14)||(branco==24)||(branco==31)||(branco==34)){
                    Toast.makeText(getActivity(),"Operação invalida",Toast.LENGTH_SHORT).show();
                    branco=0;
                }
            }
        });
        return rootView;

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
                    Log.e("POSITION", position+".");
                    turma = parent.getSelectedItem().toString();
                    if (position!=0)
                    try {
                        n1.setText("");
                        n2.setText("");
                        n3.setText("");
                        n4.setText("");
                        JSONArray j = new JSONArray(dados);
                        Log.e("position", position+"");
                        int n11 = Integer.parseInt(j.getJSONObject(position-1).getJSONObject("nota_etapa_1").getString("nota"));
                        Log.e("nota1",j.getJSONObject(position-1).getString("nota_etapa_1"));
                        n1.setText(n11+"");
                        int n22 = Integer.parseInt(j.getJSONObject(position-1).getJSONObject("nota_etapa_2").getString("nota"));
                        n2.setText(n22+"");
                        int n33= Integer.parseInt(j.getJSONObject(position-1).getJSONObject("nota_etapa_3").getString("nota"));
                        n3.setText(n33+"");
                        int n44= Integer.parseInt(j.getJSONObject(position-1).getJSONObject("nota_etapa_4").getString("nota"));
                        n4.setText(n44+"");
                    }catch(Exception e){e.printStackTrace();}

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        }

    }


}