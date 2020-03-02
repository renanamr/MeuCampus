package com.meucampus.arthur.testez;

import android.content.Context;
import android.util.Log;

import java.io.*;
/**
 * Created by ARTHUR on 25/11/2016.
 */
public class CriarArquivo {

    public String lerArquivo(Context context){

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("mensagem.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());

        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        Log.e("RET", ret);

        return ret;
    }
    public void editarArquivo(Context context, String atualizacao){

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("mensagem.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }


                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        try {
            ret = atualizacao;
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("mensagem.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(ret);
            outputStreamWriter.close();
        }catch (Exception e){
            Log.d("ERRRO EDITA", e.getMessage());
        }
        Log.e("RET ATUALIZACAO", ret);
        //return ret;
    }
    public void editarArquivoPost(Context context, String atualizacao){

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("mensagem.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }


                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        try {
            ret = ret.substring(0, ret.length() - 1) +atualizacao + "]";
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("mensagem.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(ret);
            outputStreamWriter.close();
            Log.e("EDITANDO: NO POST", ret);
        }catch (Exception e){
            Log.d("ERRRO EDITA", e.getMessage());
        }
        //return ret;
    }
    public void excluirArquivo(Context context){
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("mensagem.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }


                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        try {
            ret = "";
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("mensagem.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(ret);
            outputStreamWriter.close();
        }catch (Exception e){
            Log.d("ERRRO EDITA", e.getMessage());
        }
    }
    public void criarArquivo(String data, Context context) {


        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("mensagem.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);

            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }



    }
}
