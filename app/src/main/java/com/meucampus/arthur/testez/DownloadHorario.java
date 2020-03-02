package com.meucampus.arthur.testez;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 04/01/2018.
 */

public class DownloadHorario {
    public DownloadHorario(){}
    List<String> horariosM[] = new ArrayList[5];
    List<String> horariosT[] = new ArrayList[5];
    String periodo;
    private class GetPeriodoLetivo extends AsyncTask<String, Void, String> {
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
                int periodoAno = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length()-1).getString("ano_letivo")) - 1;

                periodo = periodoAno+"/1";
                Log.e("PERIDO: ", periodo+".");
                new GetHorarios().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/"+periodo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class GetHorarios extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        @Override
        protected String doInBackground(String... strings) {
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();

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
            }catch (Exception e){
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i<jsonArray.length(); i++){
                    String h = jsonArray.getJSONObject(i).getString("horarios_de_aula");
                    String turma =jsonArray.getJSONObject(i).getString("descricao");
                    int diaSemana;
                    String horaM ="";

                    if(h.contains("/")){
                        String horaT = "";
                        diaSemana = Integer.parseInt(h.charAt(0)+"");
                        horaM = h.substring(2, h.indexOf(" /"));
                        horaT = h.substring(h.indexOf("/ "), h.length());
                        if (horariosM[diaSemana] == null)
                            horariosM[diaSemana] = new ArrayList<>();
                        for (int j = 0; j<horaM.length(); j++){
                            horariosM[diaSemana].add(Integer.parseInt(horaM.charAt(j)+"") - 1, turma);
                        }
                        for (int j = 0; j<horaT.length(); j++){
                            if (horariosT[diaSemana] == null)
                                horariosT[diaSemana] = new ArrayList<>();
                            horariosT[diaSemana].add(Integer.parseInt(horaT.charAt(j)+"") - 1, turma);
                        }

                    }else{
                        String horaT = "";
                        diaSemana = Integer.parseInt(h.charAt(0)+"");
                        if(h.contains("M")){
                            horaM = h.substring(2, h.length());
                            if (horariosT[diaSemana] == null)
                                horariosT[diaSemana] = new ArrayList<>();
                            for (int j = 0; j<horaM.length(); j++){
                                horariosM[diaSemana].add(Integer.parseInt(horaM.charAt(j)+"") - 1, turma);
                            }
                        }else{
                            horaT = h.substring(2, h.length());
                            horariosT[diaSemana] = new ArrayList<>();
                            for (int j = 0; j<horaM.length(); j++){
                                horariosM[diaSemana].add(Integer.parseInt(horaM.charAt(j)+"") - 1, turma);
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if(horariosM != null){
                for (int i = 0; i<5; i++){
                    if(horariosM[i] != null ){
                        for (int j = 0; j<horariosM[i].size(); j++){
                            Log.e("HORARIO", horariosM[i].get(j));
                        }
                    }
                    if(horariosT[i] != null ){
                        for (int j = 0; j<horariosT[i].size(); j++){
                            Log.e("HORARIO", horariosT[i].get(j));
                        }
                    }

                }
            }
        }
    }
}
