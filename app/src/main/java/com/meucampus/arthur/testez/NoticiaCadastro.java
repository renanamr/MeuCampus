package com.meucampus.arthur.testez;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class NoticiaCadastro extends AppCompatActivity {

    static Bitmap bitmap;
    Toolbar mToolbar;
    EditText edtTitulo;
    EditText edtDescricao;
    EditText edtImagem;
    Context mContext;
    Button btnCadastro;
    public static EditText edtData;
    static Noticia n;
    int status;
    public static ProgressBar progress;
    private static final int REQUEST_CODE = 5678;
    ImageView img;
    String url="";
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;

    String url1;
    boolean sendImage = false;
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
        requestStoragePermission();
        mContext = getApplicationContext();
        img = (ImageView) findViewById(R.id.imgCadastro);
        setContentView(R.layout.activity_noticia_cadastro);
        mToolbar = (Toolbar) findViewById(R.id.tb_notica_add);
        edtTitulo = (EditText) findViewById(R.id.edtTitulo);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtImagem = (EditText) findViewById(R.id.edtImagem);
        edtImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        progress= (ProgressBar) findViewById(R.id.progressCadastroN);
        btnCadastro = (Button) findViewById(R.id.btnCadastro3);

        mToolbar.setTitle("Cadastro de notícias");
        mToolbar.setTitle("Notícia");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimarytext));

        url1 = getResources().getString(R.string.url);
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
                Log.e("ERRORC", "ONCLICK");
                if(!edtTitulo.getText().toString().isEmpty() && !edtDescricao.getText().toString().isEmpty()) {

                    try {

                        if(edtImagem.getText().toString().contains("http://")){
                            url = edtImagem.getText().toString().substring(7, edtImagem.getText().toString().length());
                        }else{
                            if(edtImagem.getText().toString().contains("https://")){
                                url = edtImagem.getText().toString().substring(8, edtImagem.getText().toString().length());
                            }else{
                                url = edtImagem.getText().toString();
                            }
                        }
                        /*url = edtImagem.getText().toString();
                        url = url.replace("/","??");*/
                        n = new Noticia(edtTitulo.getText() + "", edtDescricao.getText() + "", getData(), url);




                        /*try {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            n.setImagem(Base64.encodeToString(byteArray, 0));
                        }catch(Exception e) {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(n));
                        }*/


                       // new PostAsynk().execute(url1+"noticia/criar");
                        progress.setVisibility(View.VISIBLE);
                        Log.e("SendImage", sendImage+".");
                        if(sendImage)
                            uploadMultipart();
                        else
                            new PostAsynk().execute(url1+"noticia/criar");

                    }catch (Exception e){
                        Log.e("ERROLOG", e.getMessage());
                    }


                    edtTitulo.setText("");
                    edtDescricao.setText("");
                    edtImagem.setText("");
                }else
                    Toast.makeText(getApplication(), "Os campos de título e descrição são obrigatórios.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private class PostAsynk extends AsyncTask<String, Void, String>{
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        BufferedWriter bw = null;

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.e("BACKGROUND", "gggg");
                HttpURLConnection connection = (HttpURLConnection) new URL (strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));


                String json = "{\"titulo\":\""+n.getTitulo()+"\",\"data\":\""+n.getData()+"\",\"descricao\":\""+n.getDescricao()+"\",\"imagemLink\":\""+url.substring(7, url.length())+"\"}";
                Log.e("JSON STRING: ", json);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("data", n.getData());
                jsonObject.put("titulo", n.getTitulo());
                jsonObject.put("descricao", n.getDescricao());
                jsonObject.put("imagemLink", url);
                Log.e("linkkkkk", url);
                Log.e("JSON STRING2: ", String.valueOf(jsonObject));
                Log.e("teste", "aaaaaa " + n.getImagemLink());
                Log.e("asdf", String.valueOf(jsonObject));
                Log.e("Http: ", connection.getURL()+".");
                bw.write(String.valueOf(jsonObject));

                bw.flush();

               // br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //data.append(br.readLine());

                Log.d("RESPONDE", connection.getResponseMessage()+".");
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
            Log.e("EXECUTE", s.toString());
            if(false)
                new PostImagem().execute(n.getImagemLink(), n.getTitulo());
            else {
                progress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Notícia enviada com sucesso!", Toast.LENGTH_LONG).show();
            }
        }

    }

    private class GetUrlImage extends AsyncTask<String, Void, String>{
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        @Override
        protected String doInBackground(String... url) {
            Log.e("BACK", "ASPDF");
            try {
                HttpURLConnection get = (HttpURLConnection) new URL("http://200.137.2.185/testequercus/get_url_image.php").openConnection();
                get.setRequestMethod("GET");

                get.connect();
                Log.e("Log", "1");
                br = new BufferedReader(new InputStreamReader(get.getInputStream()));
                Log.e("Log", "2");
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
                url = new JSONObject(s).getString("url");
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.e("EXECUTE", s.toString());
            n.setImagem(s.toString());
            n.setImagemLink(s.toString());
            new PostAsynk().execute(url1+"noticia/criar");
        }

    }

    private class PostImagem extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            try{
                Log.e("Link:", strings[0]);
                Log.e("Titulo:", strings[1]);
                HttpURLConnection connection = (HttpURLConnection) new URL ("http://200.137.2.185/meucampus/service/file1/upload/").openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                Log.e("response!!", connection.getResponseCode()+".");
                connection.disconnect();

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Notícia enviada com sucesso!", Toast.LENGTH_LONG).show();
        }
    }
    public static String getData(){
        GregorianCalendar gc = new GregorianCalendar();
        Date date = gc.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

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
            if (month <10){
                data = day+"/"+"0"+month+"/"+year;
            }
            edtData.setText(data);
        }
    }

    //código para upload php
    public void uploadMultipart() {
        //getting name for the image
        String name = "";

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            UploadNotificationConfig unc = new UploadNotificationConfig();
            unc.setTitleForAllStatuses("Envio de imagem.");

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId,"http://200.137.2.185/testequercus/upload_image.php")
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(unc)
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("Exception: ", exception.getMessage());
                            Log.e("ServerResponse: ", serverResponse.getBodyAsString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.e("Response: ", serverResponse.getBodyAsString());
                            new GetUrlImage().execute();
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .startUpload(); //Starting the upload


        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            sendImage = true;
            Log.e("SendImage", sendImage+".");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                edtImagem.setText(filePath.toString());
                Log.e("File: ", filePath.toString());
                //imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        sendImage = true;
        Log.e("SendImage", sendImage+".");
        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
