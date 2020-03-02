package com.meucampus.arthur.testez.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.meucampus.arthur.testez.ActivityLogin;
import com.meucampus.arthur.testez.BD;
import com.meucampus.arthur.testez.Evento;
import com.meucampus.arthur.testez.MainActivity;
import com.meucampus.arthur.testez.Noticia;
import com.meucampus.arthur.testez.R;
import com.meucampus.arthur.testez.RepositorioUsuario;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meucampus.arthur.testez.TelaDeEspera;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ARTHUR on 02/01/2017.
 */
public class ServicoNotificacao extends Service {
    public static boolean concluidoMaterial = true;
    public static String token="";

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        context1= this;
        new Worker().start();
        super.onTaskRemoved(rootIntent);
    }

    public static boolean readyHorario = false;
    public static boolean isRequesting = false;
    static String[][] horariosM2 = new String[6][6];
    static String[][] horariosT2 = new String[6][6];
    public ServicoNotificacao() {
        //super("Notificacao");
    }
    public static Image image;
    Context context1;
    public int getHorarioPdf(Image image, Context context){
        context1 = context;
        this.image = image;
        if(!readyHorario) {
            Toast.makeText(context, "Aguarde enquanto recuperamos os horários...", Toast.LENGTH_SHORT).show();
            isRequesting = true;
            //getHorarioPdf(image, context);

            //try {
            //    Thread.sleep(5000);
            //    getHorarioPdf(image, context);
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
            createPdf(image);
            return 0;
        }
        createPdf(image);
        return 1;
    }
    public static boolean request = false;
    private void createPdf(Image image){
        //ActivityCompat.requestPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, 1);
        try {

            Log.e("pdf", "create");
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MeuCampus");
            dir.mkdirs();
            Document doc = new Document(PageSize.A4.rotate());
            File file = new File(dir, "meucampus-horario.pdf");
            Log.e("existes", file.exists()+"");

            FileOutputStream fOut = new FileOutputStream(file);

            //open the document
            PdfWriter.getInstance(doc, fOut);
            doc.open();

            doc.add(image);
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 40, Font.BOLD);
            font.setColor(255,255,255);
            doc.add(new Paragraph("asd", font));

            PdfPTable pdfTable = new PdfPTable(7);
            pdfTable.setWidthPercentage(100.0f);

            font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

            Paragraph p = new Paragraph("Horários da manhã", font);
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell header = new PdfPCell(p);
            header.setVerticalAlignment(Element.ALIGN_CENTER);
            header.setColspan(7);
            pdfTable.addCell(header);

            font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Dia da semana", font));
            pdfTable.addCell(new Phrase("07:00 - 07:45", font));
            pdfTable.addCell(new Phrase("07:45 - 08:30", font));
            pdfTable.addCell(new Phrase("08:50 - 09:35", font));
            pdfTable.addCell(new Phrase("09:35 - 10:20", font));
            pdfTable.addCell(new Phrase("10:30 - 11:15", font));
            pdfTable.addCell(new Phrase("11:15 - 12:00", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Segunda", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosM2[0][i]+" ", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Terça", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosM2[1][i]+" ", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Quarta", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosM2[2][i]+" ", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Quinta", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosM2[3][i]+" ", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Sexta", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosM2[4][i]+" ", font));

            doc.add(pdfTable);
            font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
            font.setColor(255,255,255);
            doc.add(new Paragraph("asd", font));

            pdfTable = new PdfPTable(7);
            pdfTable.setWidthPercentage(100.0f);

            font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

            p = new Paragraph("Horários da tarde", font);
            p.setAlignment(Element.ALIGN_CENTER);
            header = new PdfPCell(p);
            header.setVerticalAlignment(Element.ALIGN_CENTER);
            header.setColspan(7);
            pdfTable.addCell(header);

            font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Dia da semana", font));
            pdfTable.addCell(new Phrase("13:00 - 13:45", font));
            pdfTable.addCell(new Phrase("13:45 - 14:30", font));
            pdfTable.addCell(new Phrase("14:50 - 15:35", font));
            pdfTable.addCell(new Phrase("16:20 - 16:30", font));
            pdfTable.addCell(new Phrase("16:30 - 17:15", font));
            pdfTable.addCell(new Phrase("17:15 - 18:00", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Segunda", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosT2[0][i]+" ", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Terça", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosT2[1][i]+" ", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Quarta", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosT2[2][i]+" ", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Quinta", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosT2[3][i]+" ", font));

            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            pdfTable.addCell(new Phrase("Sexta", font));
            font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            for (int i = 0; i<6; i++)
                pdfTable.addCell(new Phrase(horariosT2[4][i]+" ", font));

            doc.add(pdfTable);
            doc.close();
            Log.e("teoricamente", "ok");
            //if(request){
                dir.mkdirs();
                File file1 = new File(dir, "meucampus-horario.pdf");

                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()+".com.meucampus.arthur.testez", file1), "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("pdf",e.getMessage());
                    e.printStackTrace();
                    // Instruct the user to install a PDF reader here, or something
                }

            //}


        }catch (Exception e){
            e.printStackTrace();
        }
    }
/*
    @Override
    protected void onHandleIntent(Intent intent) {

    }*/

    List<String> horariosM[] = new ArrayList[5];
    List<String> horariosT[] = new ArrayList[5];


    private Context context = this;
    static String url;

    private MainActivity act;

    private static String noticiaJson = "[]";
    private static String eventoJson;


    public static String getEventoJson() {
        return eventoJson;
    }

    public static void setEventoJson(String eventoJson) {
        ServicoNotificacao.eventoJson = eventoJson;
    }

    public static String getNoticiaJson() {

        return noticiaJson;
    }

    public static void setNoticiaJson(String noticiaJson) {
        ServicoNotificacao.noticiaJson = noticiaJson;
    }

    SharedPreferences sharedPref;
    @Override
    public void onCreate() {
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        context1= this;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        BD bd = new BD(this);
        try {
            bd.inserir2(new RepositorioUsuario().getMatricula(), new RepositorioUsuario().getToken());
        }catch (Exception e){e.printStackTrace();}
//        String idN = sharedPref.getString("noticia", "0");
        Log.e("Serviço criado", "notificaçãp");
        Worker run = new Worker();
        run.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //vice(new Intent(this, ServicoNotificacao.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notificacao(String titulo, String tipo){
        startService(new Intent(getApplicationContext(), ConfiguracoesIniciais.class));
        Log.e("worker", "noti");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MC";
            String description = "MeuCampus";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MC", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(this, TelaDeEspera.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "MC")
                .setSmallIcon(R.drawable.ic_noticias)
                .setContentTitle(tipo)
                .setContentText(titulo)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());
        /*
        final android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker("tipo")
                .setSmallIcon(R.drawable.ic_noticias)
                .setContentTitle(tipo)
                .setContentText(titulo)
                .setAutoCancel(true);
//        Log.e("TITULO", titulo);
        Intent intent = new Intent(this, ActivityLogin.class);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

        builder.setPriority(android.support.v4.app.NotificationCompat.PRIORITY_HIGH);
        builder.setVisibility(android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC);


        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(som);
        } catch (Exception e) {
            Log.e("worker", e.getMessage());
            e.printStackTrace();
        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
*/

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context1= this;
        Worker w = new Worker();
        w.start();
        return START_STICKY;
    }

    public void teste(){
        Log.e("Chamei teste", "Ok");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean logado = sharedPreferences.getBoolean("logado", false);

        Log.e("logado", logado+"");

            Log.d("Teste1", "teste2");



    }

    private class Worker extends Thread{
        String url = RepositorioUsuario.getUrl();

        public Worker(){
            Log.e("Worker", "ok");
            teste();
        }

        @Override
        public void run() {
            Log.e("WORKER", "ta funcionando");
            //notificacao("Teste", "teste");
            while (true) {
                String token =new RepositorioUsuario().getToken()+"";
                if(!token.isEmpty()) {


                    SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String token1 = sharedPreferences1.getString("token", "");
                    String json = "{\"token\":\""+token1+"\"}";
                    Log.e("ServicoNotificacao: ", json+",");

                    new TaskRefreshToken().execute("https://suap.ifrn.edu.br/api/v2/autenticacao/token/refresh/",token);


                    //startService(new Intent(context1, ServicoMaterial.class));
                    //if(concluidoMaterial)
                     //   new GetTurmas().execute();
                }else{
                    teste();
                }
                try {
                    Thread.sleep(900000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

    }
    private class AutenticaP extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String matricula = "";
        String senha = "";
        JSONObject json;
        @Override
        protected String doInBackground(String... strings) {
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                json = new JSONObject(strings[1]);
                matricula = json.getString("username");
                senha =  json.getString("password");
                bw.write(String.valueOf(json));
                Log.d("JSON", String.valueOf(json));

                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                token = new JSONObject(data.toString()).getString("token");
                new RepositorioUsuario().setToken(token);
                Log.e("DATA2", data.toString()+"");
                connection.disconnect();

                return "OK";
            }catch(Exception e){
                e.printStackTrace();
            }
            return "FAIL";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("POST", s+".");
            if(s.equals("OK")) {

            }

        }
    }

    private class TaskRefreshToken extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        JSONObject json;
        @Override
        protected String doInBackground(String... strings) {
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String token = sharedPreferences1.getString("token", "");

                json = new JSONObject("{\"token\":\""+ token+"\"}");
                bw.write(String.valueOf(json));
                Log.d("ServicoNotificacao:", String.valueOf(json));

                bw.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                token = new JSONObject(data.toString()).getString("token");
                new RepositorioUsuario().setToken(token);

                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("token",token);
                editor.apply();

                Log.e("DATA2", data.toString()+"");
                connection.disconnect();

                return "OK";
            }catch(Exception e){
                e.printStackTrace();
            }
            return "FAIL";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("POST", s+".");
            if(s.equals("OK")) {
                new Get().execute();
            }

        }
    }

    private class Get extends AsyncTask<String, Void, String>{
        private String noticiaJson;
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        @Override
        protected String doInBackground(String... strings) {
            noticiaJson = ServicoNotificacao.getNoticiaJson();
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("http://200.137.2.185/meucampus/service/noticia/notificacao").openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                data.append(br.readLine());
                Log.e("codigoteste:", conn.getResponseCode()+"..");
                conn.disconnect();

                return data.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("DATATESTE:", s+".");

                if (s != null && (noticiaJson!= null) && !s.equals("null")) {

                    if (!s.equals(noticiaJson)) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject = new JSONObject(s);
                        }catch(Exception e){}
                            Noticia n = new Gson().fromJson(jsonObject.toString(), Noticia.class);
                            Log.e("SERVICE", "CHAMEI NOTIFICACAO");
                            Log.e("SERVICE NOTIFICACAO", sharedPref.getString("noticia", "0"));
                        if(!sharedPref.getString("noticia", "0").equals(n.getTitulo())){
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("noticia", n.getTitulo());
                            editor.apply();
                            Log.e("shared",sharedPref.getString("noticia", "nada"));
                            notificacao(n.getTitulo(), "notícia");
                        }

                        }
                    }
            new GetEvento().execute();

        }
    }
    private class GetEvento extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        private String eventoJson;
        @Override
        protected String doInBackground(String... strings) {
            eventoJson = ServicoNotificacao.getEventoJson();
            try{
                HttpURLConnection conn = (HttpURLConnection) new URL("http://200.137.2.185/meucampus/service/evento/notificacao").openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                data.append(br.readLine());
                return data.toString();
            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s != null && (eventoJson!= null)) {
                    if (!s.equals(noticiaJson)) {
                        JSONObject jsonObject = new JSONObject(s);
                        Evento e = new Gson().fromJson(jsonObject.toString(), Evento.class);
                        //new ServicoNotificacao().notificacao(e.getTitulo(), "evento");
                    }
                }
            }catch (Exception e){
                Log.e("ERRO GETE", e.getMessage());
            }
            new GetPeriodoLetivo().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-periodos-letivos/");

        }
    }
    String periodo;
    private class GetPeriodoLetivo extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        @Override
        protected String doInBackground(String... strings) {
            /*try{
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
            }*/
            return ".";
        }

        @Override
        protected void onPostExecute(String s) {
            /*JSONArray jsonArray = new JSONArray();
            try{
                int periodoAno = 0;
                if("".equals(s) || s == null) {
                    periodoAno = Calendar.getInstance().get(Calendar.YEAR);
                }
                else{
                    jsonArray = new JSONArray(s);
                    periodoAno = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length()-1).getString("ano_letivo")) - 1;
                }

                periodo = periodoAno+"/1";

                if(!new RepositorioUsuario().getTipo().equals("PROFESSOR"))
                    new GetHorarios().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/turmas-virtuais/"+new RepositorioUsuario().getCaminhoPeriodo());

            }catch (Exception e){
                e.printStackTrace();
            }*/
            new GetHorarios().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/turmas-virtuais/"+new RepositorioUsuario().getCaminhoPeriodo());

        }
    }
    public List<String> codigosTurmas = new ArrayList<>();
    private class GetTurmas extends AsyncTask<String, Void, String> {
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        List<String> objetosJson = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            concluidoMaterial = false;
            Log.e("Turmas::::", "entrei");
            try{
//                Log.e("url", strings[0]);
                Log.e("GetTurmas", "opa");
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/"+new RepositorioUsuario().getCaminhoPeriodo()).openConnection();

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
            Log.e("DATAPOST", s);
            RepositorioUsuario repo = new RepositorioUsuario();

            JSONArray json;
            try{
                json = new JSONArray(s);
                if(objetosJson.size()==0) {
                    Log.e("objetosJson, ", "if");
                    for (int i = 0; i < (json.length()); i++) {
                        Log.e("JSON: ", json.getJSONObject(i).toString());
                        objetosJson.add(json.getJSONObject(i).toString());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            String codigoDiario ="";
            try {
                for(int i = 0; i<objetosJson.size(); i++) {
                    JSONObject jsonObject = new JSONObject(objetosJson.get(i));
                    codigoDiario = jsonObject.getString("codigo_diario");
                    codigosTurmas.add(codigoDiario);


                    Log.e("Service: CODIGO: ", codigoDiario);
                }
                //1 2 3 2
                for(int i = 0; i<objetosJson.size();i++){
                    Log.e("ObjetosJson", i+".");
                    for(int j = 0; j<i; j++){
                        if(objetosJson.get(i).equals(objetosJson.get(j))){
                            Log.e("IFFOR", objetosJson.get(i));
                            Log.e("IFFOR", objetosJson.get(j));
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            new GetTurmasDetalhes().execute();
        }

    }
    int count = 0;

    private class GetTurmasDetalhes extends AsyncTask<String, Void, String> {
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;

        @Override
        protected String doInBackground(String... strings) {

            try{
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/turma-virtual/"+codigosTurmas.get(count)).openConnection();
                connection.setRequestProperty("Authorization", "JWT "+new RepositorioUsuario().getToken());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(false);
                connection.setDoInput(true);

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                connection.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            RepositorioUsuario repo = new RepositorioUsuario();

            JSONObject json;
            JSONArray jsonArray = new JSONArray();
            try{
                json = new JSONObject(s);
                jsonArray = json.getJSONArray("materiais_de_aula");
                String length =jsonArray.length()+"";
                Log.e("3030: ", sharedPref.getString(codigosTurmas.get(count), "nada")+".");
                Log.e("3031: ", length+".");
                if (!length.equals(sharedPref.getString(codigosTurmas.get(count), "nada")) && !length.equals("0") && !sharedPref.getString(codigosTurmas.get(count), "nada").equals("nada")){
                    Log.e("Notificação: ", sharedPref.getString(codigosTurmas.get(count), "nada") + " e " + length);
                    //notificacao("Novo material: " + jsonArray.getJSONObject(jsonArray.length()-1).getString("descricao"), "Materiais de aula");
                }
                Log.e("Antes: ", sharedPref.getString(codigosTurmas.get(count), "nada"));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(codigosTurmas.get(count), length);
                editor.apply();
                Log.e("Depois: ", sharedPref.getString(codigosTurmas.get(count), "nada"));
                Log.e("JSON LENGTH" + count, jsonArray.length()+".");
                count++;
                new GetTurmasDetalhes().execute();
            }catch(Exception e){

                e.printStackTrace();
                finalizar();
            }
            new GetNotas().execute();


        }
        public void finalizar(){
            concluidoMaterial = true;
            for (int i = 0; i<codigosTurmas.size(); i++){

                Log.e("shared pref: ", sharedPref.getString(codigosTurmas.get(i), "nada"));
            }
        }

    }


    private class GetHorarios extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        @Override
        protected String doInBackground(String... strings) {
            try{
                Log.e("TURMAS HORARIOS", "ENTREI");
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
                Log.e("HORARIO:::", s.toString());
                JSONArray jsonArray = new JSONArray(s);
                for(int i =0; i<6; i++){
                    for (int j = 0; j<6; j++){
                        horariosM2[i][j] = "VAGO";
                        horariosT2[i][j] = "VAGO";
                    }
                }
                for (int i = 0; i<jsonArray.length(); i++){
                    String h = jsonArray.getJSONObject(i).getString("horarios_de_aula");
                    String turma =jsonArray.getJSONObject(i).getString("descricao");
                    if(jsonArray.getJSONObject(i).getString("observacao").contains("apenas"))
                        continue;
                    int diaSemana;
                    String horaM ="";
                    Log.e("turma", turma);
                    Log.e("h", h);
                    /*if(h.contains("/")){
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
                            if (horariosM[diaSemana] == null)
                                horariosM[diaSemana] = new ArrayList<>();
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
                    }*/
                    if(h.contains("/")){
                        String hora2 = "";
                        diaSemana = Integer.parseInt(h.charAt(0)+"");
                        horaM = h.substring(2, h.indexOf(" /"));
                        Log.e("h: ", h);
                        Log.e("horaM", horaM);

                        hora2 = h.substring(horaM.length() +5, h.length());
                        Log.e("HORA 2:" , hora2);
                        for (int j = 0; j<horaM.length(); j++){
                            Log.e("dia semana : ", diaSemana-1+"");
                            horariosM2[diaSemana-2][Integer.parseInt(horaM.charAt(j)+"") - 1] = turma;
                        }
                        diaSemana = Integer.parseInt(hora2.charAt(0)+"");
                        for (int j = 2; j<hora2.length(); j++){

                            Log.e("dia semana : ", diaSemana-1+"");
                            //Log.e("horaT: ", hora2.charAt(j)+"");
                            Log.e("horaT: ", hora2.charAt(j)+"  : " +j);
                            if(hora2.charAt(1) == 'M')
                                horariosM2[diaSemana-2][Integer.parseInt(hora2.charAt(j)+"") - 1] =  turma;
                            else
                                horariosT2[diaSemana-2][Integer.parseInt(hora2.charAt(j)+"") - 1] =  turma;
                        }

                    }else{
                        String horaT = "";
                        diaSemana = Integer.parseInt(h.charAt(0)+"");
                        if(h.contains("M")){
                            horaM = h.substring(2, h.length());
                            for (int j = 0; j<horaM.length(); j++){
                                Log.e("dia semana : ", diaSemana-1+"");
                                horariosM2[diaSemana-2][Integer.parseInt(horaM.charAt(j)+"") - 1] = turma;
                            }
                        }else{
                            horaT = h.substring(2, h.length());
                            for (int j = 0; j<horaT.length(); j++){
                                Log.e("dia semana : ", diaSemana-1+"");
                                //Log.e("outro", )
                                horariosT2[diaSemana-2][Integer.parseInt(horaT.charAt(j)+"") - 1] = turma;
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("HORARIO ERRO", e.getMessage());
            }

            for (int i = 0; i<5; i++) {
                Log.e("dia da semana: ", i+2+":");
                for (int j = 0; j < 6; j++)
                    Log.e("HORÁRIO: ", horariosM2[i][j] + ".");
            }
            readyHorario = true;
            if(isRequesting){
                isRequesting = false;
                request = true;
                createPdf(image);
            }
            new GetTurmas().execute("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/" + new RepositorioUsuario().getCaminhoPeriodo());

        }

    }

    private class GetNotas extends AsyncTask<String, Void, String> {

        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        String urlSuap;
        String url;
        @Override
        protected String doInBackground(String... strings) {

            try{
//                url = strings[1];
//                urlSuap = strings[0];
                //String basicAuth = new String(Base64.encode(strings[0].getBytes(), Base64.DEFAULT));//encoding token do usuário
                Log.e("basicAuth: ", new RepositorioUsuario().getToken());
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/boletim/2018/1/").openConnection();

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
            JSONArray jsonArray = new JSONArray();

            try {
                jsonArray= new JSONArray(s);
            }catch(Exception e){
                e.printStackTrace();
            }

                for (int i = 0; i < jsonArray.length(); i++) {
                    int notasCadastradas= 0;
                    String codigoDiario = "";
                    String disciplina = "";
                    try {
                        codigoDiario = jsonArray.getJSONObject(i).getString("codigo_diario");
                        disciplina = jsonArray.getJSONObject(i).getString("disciplina").substring(11);
                    Log.e("Diciplina: ", jsonArray.getJSONObject(i).getString("disciplina"));
                    String nota1 = "";
                    nota1 += jsonArray.getJSONObject(i).getJSONObject("nota_etapa_1").getInt("nota");
                    notasCadastradas++;
                    new AcessoNotas(getApplicationContext()).inserirNotas(new Notas(codigoDiario, Integer.parseInt("1"), Integer.parseInt(nota1)));
                    String nota2 = "";
                    nota2 += jsonArray.getJSONObject(i).getJSONObject("nota_etapa_2").getInt("nota");
                    notasCadastradas++;

                        new AcessoNotas(getApplicationContext()).inserirNotas(new Notas(codigoDiario, Integer.parseInt("2"), Integer.parseInt(nota2)));
                    String nota3 = "";
                    nota3 += jsonArray.getJSONObject(i).getJSONObject("nota_etapa_3").getInt("nota");
                    notasCadastradas++;

                        new AcessoNotas(getApplicationContext()).inserirNotas(new Notas(codigoDiario, Integer.parseInt("3"), Integer.parseInt(nota3)));

                    String nota4 = "";
                    nota4 += jsonArray.getJSONObject(i).getJSONObject("nota_etapa_4").getInt("nota");
                    notasCadastradas++;

                        new AcessoNotas(getApplicationContext()).inserirNotas(new Notas(codigoDiario, Integer.parseInt("4"), Integer.parseInt(nota4)));
                    Log.e("Nota 1: ", nota1 + ".");
                    Log.e("Nota 1: ", nota2 + ".");
                    Log.e("Nota 1: ", nota3 + ".");
                    Log.e("Nota 1: ", nota4 + ".");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(sharedPref.getString("notas_"+ codigoDiario, "nada").equals(notasCadastradas+"")){
                        Log.e("Nota: ", "iguais");
                    }else{
                        if(!sharedPref.getString("notas_"+ codigoDiario, "nada").equals("nada")) {
                            Log.e("nota - shared: ", sharedPref.getString("notas_" + codigoDiario, "nada"));
                            Log.e("nota - cadastradas: ", notasCadastradas + ".");
                            notificacao("Atualização de nota", disciplina);
                        }
                    }
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("notas_"+ codigoDiario, notasCadastradas+"");
                    editor.apply();

                    Log.e("notas cadastradas: ", notasCadastradas+".");

                }
                for(int i = 0;i<jsonArray.length(); i++){
                    try{
                        Log.e("notas "+i,sharedPref.getString("notas_"+jsonArray.getJSONObject(i).getString("codigo_diario"), "nada"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                //Intent i = new Intent(getApplicationContext(), ServicoAtualizacao.class);
                //startService(i);
        }

    }


}
