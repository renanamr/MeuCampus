package com.meucampus.arthur.testez;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meucampus.arthur.testez.Fragments.PdfOpen;
import com.meucampus.arthur.testez.Services.ConfiguracoesIniciais;
import com.meucampus.arthur.testez.Services.ServicoAtualizacao;
import com.meucampus.arthur.testez.Services.ServicoNotificacao;
import com.itextpdf.text.Image;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Toolbar mToolbarBottom;
    private Drawer mLeftDrawer;
    private AccountHeader mHeaderNavigation;


    private  FloatingActionButton fab;
    private static List<Noticia> mList;
    private static List<Evento> mListEvento;
    private static NoticiaAdapter mAdapter;
    private static EventoAdapter mAdapterEvento;
    private static ListView mListView;
    private static ListView mListViewEventos;
    private String matricula;
    private String url1;
    private static ProgressBar progress;
    static Bitmap image = new RepositorioUsuario().getBitmap();
    @Override
    protected void onStart() {
        //new ServicoNotificacao(this);
        super.onStart();
    }

    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                Toast.makeText(MainActivity.this, "tá como true (ativado) ", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity.this, "tá como false (desativado) ", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onResume() {
        new GetNoticias().execute(url1+ "noticia/lista");
        new GetEventos().execute(url1+"evento/lista");
        super.onResume();
    }
    public void abc(String titulo, String tipo){
        NotificationManager nM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), ServicoNotificacao.class), 0);
        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this);
        builder1.setTicker("MeuCampus");
        builder1.setContentTitle(tipo);
        builder1.setContentText(titulo);
        builder1.setSmallIcon(R.mipmap.ic_launcher);
        builder1.setContentIntent(p);

        Notification n = builder1.build();
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nM.notify(R.mipmap.ic_launcher, n);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(getApplicationContext(), som);
            toque.play();
        } catch (Exception e) {

        }
    }

    private void startService(Context context){
        new RepositorioUsuario().setUrl(url1);
        Log.e("URL1", new RepositorioUsuario().getUrl());

        //Intent intent = new Intent(context, ServicoNotificacao.class);
        //context.startService(intent);
    }

    public void stopService(){
        Intent intent = new Intent(getApplicationContext(), ServicoAtualizacao.class);
        stopService(intent);
    }
    Bundle bundle;
    @Override
    protected void onRestart() {
        /*finish();
        Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(i);*/
        if(new RepositorioUsuario().getToken().isEmpty() && new RepositorioUsuario().getInternet() == false) {
            finish();
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }
        super.onRestart();
    }
    public void pdfCriado(){
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MeuCampus");
        dir.mkdirs();
        File file = new File(dir, "meucampus-horario.pdf");

        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.e("pdf",e.getMessage());
            e.printStackTrace();
            // Instruct the user to install a PDF reader here, or something
        }
        new RepositorioUsuario().pdf = file;
        Intent open = new Intent(getApplicationContext(), PdfOpen.class);
        startActivity(open);
        Toast.makeText(getApplicationContext(), "Criado!!!", Toast.LENGTH_LONG).show();
    }
    private void requestPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aviso");
                builder.setMessage("É necessário dar permissão ao aplicativo.");
                builder.create();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
            }
        }else{
            criar();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aviso");
        builder.setMessage("É necessário dar permissão ao aplicativo.");
        builder.create();
    }
    private void criar(){
        try {
            Drawable myImage = getApplicationContext().getResources().getDrawable(R.drawable.icon2);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            Image image = Image.getInstance(bitmapdata);
            image.setAbsolutePosition(375, 500);

            if(new ServicoNotificacao().getHorarioPdf(image, this) ==0){
                //Toast.makeText(getApplicationContext(), "Aguarde enquanto recuperamos os horários e tente novamente...", Toast.LENGTH_SHORT).show();
                Log.e("não", "criado");
                //Thread.sleep(Toast.LENGTH_SHORT);
                //criar();

            }else {
                pdfCriado();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 300:
                for(int i =0; i<permissions.length; i++){
                    if(permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        criar();
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.e("notnotaaa", new BD(getApplicationContext()).getNoticias());

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        try{
            Log.e("Periodo", new RepositorioUsuario().getCaminhoPeriodo());
        }catch (Exception e){
            Log.e("periodo", "bugs");
        }
        //requestPermissions();
        new DownloadHorario();
        //new teste3().execute();
        Intent service = new Intent(getApplicationContext(), ServicoNotificacao.class);
        //Intent service2 = new Intent(getApplicationContext(), ServicoAtualizacao.class);
        startService(service);
        //startService(service2);

        Log.e("TESTE31", "TESTE");
        RepositorioUsuario repo = new RepositorioUsuario();
        bundle = savedInstanceState;
        String matricula = repo.getMatricula();
        String permissoes = repo.getPermissoes();
        url1 = getResources().getString(R.string.url);
        //startService(new Intent(getApplicationContext(), ServicoGetProfessores.class));

        //startService(new Intent(getApplicationContext(), ServicoMaterial.class));
        String url = repo.getFoto();
        new DownloadImageTask().execute("https://suap.ifrn.edu.br/"+repo.getUrl());

        //boolean cadastraNoticia = repo.getPermissoes().contains("noticia");


        //Log.e("MATRICULA", repo.getMatricula());
        setContentView(R.layout.activity_main);
        Log.e("BD_MAIN",new BD(getApplicationContext()).listarTurmas().size()+".");
        if(new BD(getApplicationContext()).listarTurmas().size()==0) {
            Intent in = new Intent(getApplicationContext(), ConfiguracoesIniciais.class);
            startService(in);
        }
        Log.e("opa", "9999");
        fab = (FloatingActionButton) findViewById(R.id.add);
        Toast.makeText(this, repo.getCurso(), Toast.LENGTH_SHORT).show();
        try{if (!repo.getPermissoes().contains("noticia")){
            fab.setVisibility(View.INVISIBLE);
        }}catch(Exception e){}

        /*if (!cadastraNoticia) {
            fab.setVisibility(View.INVISIBLE);
        }*/


        progress = (ProgressBar) findViewById(R.id.progressMain);
        mListView = (ListView) findViewById(R.id.listViewNoticias);
        mListViewEventos = (ListView) findViewById(R.id.listViewEventos);
        //txt.setText("Carregando...");
        //ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //if (netInfo.isConnectedOrConnecting() && netInfo != null) {
            Log.e("CONEXÃO", "OK");

            new GetNoticias().execute(url1+"noticia/lista");
            //new GetEventos().execute("http://192.168.43.127:8080/meucampus/service/evento/lista");
        //}else{

          //  txt.setText("Erro na conexão!");
        //}



        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("MeuCampus");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));
        //mToolbar.setSubtitle("Subtitlle 123");
        setSupportActionBar(mToolbar);
        notticiaSelecionada();



  /*      mToolbarBottom = (Toolbar) findViewById(R.id.inc_tb_bottom);
        mToolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent it = null;
                switch (item.getItemId()){
                    case R.id.action_settings:
                        it = new Intent(Intent.ACTION_VIEW);
                        it.setData(Uri.parse("http://www.google.com"));
                        Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_settings1:
                        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_settings2:
                        Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_settings3:
                        Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_settings4:
                        Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_LONG).show();
                        break;
                }
                startActivity(it);
                return true;
            }
        });
        mToolbarBottom.inflateMenu(R.menu.menu_bottom); */
        //mToolbarBottom = (Toolbar) findViewById(R.id.tb_bottom1);
        String nome = new RepositorioUsuario().getNome();
        mHeaderNavigation = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(bundle)
                .withThreeSmallProfileImages(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(nome).withEmail(matricula).withIcon(image))
                .withTextColor(getResources().getColor(R.color.accent))
                .build();
        mLeftDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar (mToolbar)
                .withDisplayBelowStatusBar(true)
                .withAccountHeader(mHeaderNavigation)
                .withSelectedItem(0)
                //.withDrawerLayout(R.layout.list_layout)
                .withActionBarDrawerToggleAnimated(true)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.e("Position", position+" ;;");
                        //Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
                        switch (position){
                            case 2:

                                notticiaSelecionada();
                                break;
                            case 3:
                                eventoSelecionado();
                                //stopService();
                                break;
                            case 4:
                                if(new RepositorioUsuario().getMatricula().length() >9){
                                    Intent i = new Intent(getApplicationContext(), ActivityIniciar.class);
                                    startActivity(i);
                                }

                                //String tipo = sharedPreferences.getString("tipo");
                                /*String tipo = new RepositorioUsuario().getTipo();
                                Bundle tipob = new Bundle();
                                tipob.putString("tipo", tipo);
                                if (!tipo.equals("Aluno")) {
                                    Intent intent = new Intent(getApplicationContext(), ActivityChatCoades.class);
                                    intent.putExtras(tipob);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                    intent.putExtras(tipob);
                                    startActivity(intent);
                                }*/
                                /*Bundle tipob2 = new Bundle();
                                //tipob2.putString("tipo", "COADES/NC");
                                RepositorioUsuario repo1 = new RepositorioUsuario();
//                                repo1.setMatricula("123");
                                Intent intent4 = new Intent(getApplicationContext(), ActivityChatCoades.class);
                                intent4.putExtras(tipob2);
                                startActivity(intent4);

                                try{


                                Log.e("REPOOO",repo1.getTipo() );
                                String repo3 = repo1.getTipo();
                                String repo4 = repo1.getCurso();
                                if (new RepositorioUsuario().getCurso().contains("Técnico")){
                                    Log.e("IF1", "TESTE");
                                    Intent intent3 = new Intent(getApplicationContext(), ActivityIniciar.class);
                                    intent3.putExtras(tipob2);
                                    startActivity(intent3);
                                }else{

                                    Log.e("IF2", new RepositorioUsuario().getTipo());
                                    Intent intent3 = new Intent(getApplicationContext(), ActivityChatCoades.class);
                                    tipob2.putString("tipo", new RepositorioUsuario().getTipo());
                                    intent3.putExtras(tipob2);
                                    startActivity(intent3);
                                }
                                }catch(Exception e){}*/
                                /*if (new RepositorioUsuario().getCurso().contains("Médio")){
                                    Log.e("IF1", "TESTE");
                                    Intent intent3 = new Intent(getApplicationContext(), ActivityNovaConversaAluno.class);
                                    intent3.putExtras(tipob2);
                                    startActivity(intent3);
                                }else{
                                    Log.e("OPAAA", "ÉOQ");
                                    Log.e("IF2", new RepositorioUsuario().getTipo());
                                    Intent intent3 = new Intent(getApplicationContext(), ActivityChatCoades.class);
                                    tipob2.putString("tipo", new RepositorioUsuario().getTipo());
                                    intent3.putExtras(tipob2);
                                    startActivity(intent3);
                                }*/
                                break;
                            case 5:
                                Intent intent1 = new Intent(getApplicationContext(), CalculadoraIf.class);
                                startActivity(intent1);
                                break;
                            case 19:
                                Intent intent2;
                                RepositorioUsuario repo = new RepositorioUsuario();
                                if (repo.getTipo().equals("PROFESSOR")){
                                    intent2 = new Intent(getApplicationContext(), ActivityHorario.class);
                                }else{
                                    Bundle b = new Bundle();
                                    b.putString("turma", repo.getTurma()[0]);

                                    intent2 = new Intent(getApplicationContext(), ActivityHorarioVisualizar.class);
                                    intent2.putExtras(b);
                                }

                                Log.e("case 6", "e" +
                                        "ntrei");
                                startActivity(intent2);
                                break;
                            case 6:
                                Intent intent = new Intent(getApplicationContext(), ActivityTurmas.class);
                                startActivity(intent);
                                break;
                            case 7:
                                /*tring tipo2 = new RepositorioUsuario().getTipo();
                                Bundle tipob2 = new Bundle();
                                tipob2.putString("tipo", tipo2);
                                if(tipo2.equals("Aluno")) {
                                    Intent intent3 = new Intent(getApplicationContext(), ChatActivity.class);
                                    intent3.putExtras(tipob2);
                                    startActivity(intent3);
                                }else{
                                    Intent intent3 = new Intent(getApplicationContext(), ActivityChatCoades.class);
                                    intent3.putExtras(tipob2);
                                    startActivity(intent3);
                                }*/
                                /*Bundle tipob2 = new Bundle();
                                tipob2.putString("tipo", "COADES/NC");
                                Intent intent3 = new Intent(getApplicationContext(), ChatActivity.class);
                                intent3.putExtras(tipob2);
                                startActivity(intent3);*/
                                requestPermissions();

                                break;
                            case 8:
                                Log.e("case 7", "e" +
                                        "ntrei");
                                /*SharedPreferences pref = getPreferences(MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("logado", false);
                                editor.putString("matricula", "sdsfdasd");
                                editor.remove("senha");
                                editor.commit();
                                Log.e("Teste", "teste");
                                Log.e("teste", "Opa");
                                new RepositorioUsuario().setMatricula("");
                                new RepositorioUsuario().setSenha("");
                                new RepositorioUsuario().setToken("");
                                editor.apply();*/
                                sair();
                                break;
                            case 9:
                                sair();
                                break;

                        }
                        return false;
                    }
                })
                .build();
        mLeftDrawer.addItem(new SectionDrawerItem().withName("Serviços"));
        mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Notícias").withIcon(R.drawable.ic_noticias));
        mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Eventos").withIcon(R.drawable.ic_eventos));
        //mLeftDrawer.addItem(new DividerDrawerItem());
        //mLeftDrawer.addItem(new SectionDrawerItem().withName("Opções"));
        //mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Persolalizar").withIcon(R.drawable.ic_config));
        mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Iniciar Chat").withIcon(R.drawable.ic_chatcoades));
        mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Calcular nota").withIcon(R.drawable.ic_calc));
        //mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Horario"));
        mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Turmas virtuais").withIcon(R.drawable.ic_turmas));
        mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Horário").withIcon(R.drawable.ic_horario));
        mLeftDrawer.addItem(new DividerDrawerItem());
        mLeftDrawer.addItem(new PrimaryDrawerItem().withName("Sair").withIcon(R.drawable.sair));

        //mLeftDrawer.addItem(new SwitchDrawerItem().withName("Notificações").withOnCheckedChangeListener(mOnCheckedChangeListener));

        Log.e("Repo teste: ", new RepositorioUsuario().getTipo()+".");
        Log.e("Repo teste: ", new RepositorioUsuario().getCurso()+".");
//        if(!repo.getTipo().equals("PROFESSOR"))
//            startService(new Intent(getApplicationContext(), ServicoNotificacao.class));

        startService(getApplicationContext());
    }





    private void sair() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair");
        builder.setMessage("Deseja realmente sair?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                new RepositorioUsuario().setMatricula("");
                //new RepositorioUsuario().setSenha("");
                new RepositorioUsuario().setToken("");
                /*SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("logado", false);
                editor.putString("matricula", "opa");
                editor.remove("senha");
                Log.e("Teste", "teste");
                //pref.edit().remove("logado").commit();
                //editor.commit();
                editor.apply();*/
                new BD(getApplicationContext()).deletarTurmas();
                new BD(getApplicationContext()).apagarTudo();
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("matricula", "opaaaa");
                editor.apply();

                //Toast.makeText(getApplicationContext(), "Dados:" + sharedPref.getString("matricula","opa"), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
                finish();




            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();


    }
/*
    public String readRawTextFile() {
        try {
            InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.entradas);
            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            return buffreader.readLine();
        } catch (Exception e) {
            return null;
        }
    }

*/
    public void notticiaSelecionada(){

        //mListViewEventos = (ListView) findViewById(R.id.listViewEventos);
        mListViewEventos.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.VISIBLE);
        fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticiaCadastro.class);
                startActivity(intent);
            }
        });

        //mList = new ArrayList<>();
        //new GetNoticias().execute("http://192.168.43.127:8080/meucampus/service/noticia/lista");




        /*try {
            JSONArray jsonArray = new JSONArray(readRawTextFile());
            for (int i =0;jsonArray.getJSONObject(i)!=null;i++) {
                if(jsonArray.getJSONObject(i).getString("tipo").equals("noticia")) {
                    mList.add(new Noticia(jsonArray.getJSONObject(i).getString("titulo" ),jsonArray.getJSONObject(i).getString("descricao" ),jsonArray.getJSONObject(i).getString("data"),jsonArray.getJSONObject(i).getString("imagem")));
                }
            }
        }catch(Exception e){
        }*/


    }

    @Override
    public void onBackPressed() {

    }

    public void eventoSelecionado(){

        new GetEventos().execute(url1+ "evento/lista");
        mListView.setVisibility(View.INVISIBLE);
        mListViewEventos.setVisibility(View.VISIBLE);
        fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventoCadastro.class);
                startActivity(intent);
            }
        });

        mListEvento = new ArrayList<>();
        //new GetEventos().execute("http://192.168.43.127:8080/meucampus/service/evento/lista");
        /*try {
            JSONArray jsonArray = new JSONArray(readRawTextFile());
            for (int i =0;jsonArray.getJSONObject(i)!=null;i++) {
                if(jsonArray.getJSONObject(i).getString("tipo").equals("evento")) {
                    mListEvento.add(new Evento(jsonArray.getJSONObject(i).getString("titulo"),jsonArray.getJSONObject(i).getString("descricao" ),jsonArray.getJSONObject(i).getString("data"),jsonArray.getJSONObject(i).getString("dataInicio"),jsonArray.getJSONObject(i).getString("dataFim"),jsonArray.getJSONObject(i).getString("imagem")));
                }
            }
        }catch(Exception e){
        }

        mListViewEventos = (ListView) findViewById(R.id.listViewPrincipal);
        mAdapterEvento = new EventoAdapter (getApplicationContext(), mListEvento);
        mListViewEventos.setAdapter(mAdapterEvento);
        mListViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle a = new Bundle();
                a.putString("titulo", mListEvento.get(i).getTitulo() + "");
                a.putString("descricao", mListEvento.get(i).getDescricao() + "");
                a.putString("data", mListEvento.get(i).getData()+"");
                a.putString("dataInicio", mListEvento.get(i).getDataInicio() + "");
                a.putString("dataFim", mListEvento.get(i).getDataFim() + "");
                a.putString("link", mListEvento.get(i).getLink() + "" );
                Intent intent = new Intent(getApplicationContext(), EventExpand.class);
                intent.putExtras(a);
                startActivity(intent);
            }
        });
*/

    }
    private class teste3 extends AsyncTask<String, Void, String>{
        BufferedReader br = null;
        StringBuffer data = new StringBuffer();
        BufferedWriter bw = null;
        JSONObject json;
        @Override
        protected String doInBackground(String... strings) {
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-diarios/2017/1/").openConnection();
                connection.setRequestProperty("Authorization", "JWT "+new RepositorioUsuario().getToken());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(false);
                connection.setDoInput(true);

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                data.append(br.readLine());
                connection.disconnect();

                return data.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            return "FAIL";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("POST", s+".");


        }
    }
    String noticias = "";
    String eventos = "";
    private class GetNoticias extends AsyncTask<String , Void, String> {
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        @Override
        protected String doInBackground(String... url) {
            Log.e("notnot", new BD(getApplicationContext()).getNoticias());
            if(new RepositorioUsuario().getInternet()){
                Log.e("notnot", "retornei");

                return new BD(getApplicationContext()).getNoticias();
            }

            Log.e("BACK", "ASPDF");
            try {
                HttpURLConnection get = (HttpURLConnection) new URL(url[0]).openConnection();
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
            noticias = data.toString();

            return data.toString();
        }


        @Override
        protected void onPostExecute(String s) {
            mList = new ArrayList<>();
            try {
                Log.e("dentro", s);
                JSONArray jsonArray = new JSONArray(s);

                int jsonLength = jsonArray.length();
                for (int i = 0;i < jsonLength; i++){

                    Noticia a = new Noticia(jsonArray.getJSONObject(i).getString("titulo" ),jsonArray.getJSONObject(i).getString("descricao" ),jsonArray.getJSONObject(i).getString("data"),"");
                    a.setImagemLink(jsonArray.getJSONObject(i).getString("imagemLink"));
                    mList.add(a);


                    Log.e("NOTIDICA", "ADD");

                }
                Log.e("exeeee",mList.get(0).getTitulo()+"");
                mAdapter = new NoticiaAdapter(getApplicationContext(), mList);
                //mListView = (ListView) findViewById(R.id.listViewNoticias);

                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle a = new Bundle();

                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,findViewById(R.id.imageNoticia),"imageTrasintion");

                        a.putString("titulo", mList.get(i).getTitulo() + "");
                        a.putString("descricao", mList.get(i).getDescricao() + "");
                        a.putString("data", mList.get(i).getData() + "");
                        a.putString("foto", mList.get(i).getImagemLink() + "");
                        Intent intent = new Intent(getApplicationContext(), NoticiaExpand.class);
                        intent.putExtras(a);
                        startActivity(intent, optionsCompat.toBundle());

                    }
                });



            }catch (Exception e){
                Log.e("EXCPTION: ",e.getMessage());
            }
        }
    }
    private class GetEventos extends AsyncTask<String , Void, String> {
        StringBuffer data = new StringBuffer();
        BufferedReader br = null;
        @Override
        protected String doInBackground(String... url) {
            Log.e("BACK", "ASPDF");
            if(new RepositorioUsuario().getInternet())
                return new BD(getApplicationContext()).getEventos();
            try {
                HttpURLConnection get = (HttpURLConnection) new URL(url[0]).openConnection();
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
            eventos = data.toString();
            return data.toString();
        }


        @Override
        protected void onPostExecute(String s) {
            if(!noticias.isEmpty() || noticias.length() !=0)
            new BD(getApplicationContext()).inserirJson(noticias, eventos);
            mListEvento = new ArrayList<>();
            try {
                Log.e("dentro", s);
                JSONArray jsonArray = new JSONArray(s);

                int jsonLength = jsonArray.length();
                for (int i = 0;i < jsonLength; i++){

                    JSONObject json = jsonArray.getJSONObject(i);
                    Evento e = new Evento();
                    e.setTitulo(json.getString("titulo"));
                    e.setDataFim(json.getString("dataFim"));
                    e.setDescricao(json.getString("descricao"));
                    e.setDataInicio(json.getString("dataInicio"));
                    e.setDataPublicacao(json.getString("dataPublicacao"));
                    e.setLocal(json.getString("local"));
                    //e.setTipo((Tipo) json.get("tipo"));
                    if (!json.getString("imageLink").isEmpty())
                    e.setImageLink(json.getString("imageLink"));
                    mListEvento.add(e);

                }


                mAdapterEvento = new EventoAdapter(getApplicationContext(), mListEvento);
                //mListViewEventos = (ListView) findViewById(R.id.listViewEventos);


                mListViewEventos.setAdapter(mAdapterEvento);
                mListViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle a = new Bundle();
                        try {
                            a.putString("titulo", mListEvento.get(i).getTitulo() + "");
                            a.putString("descricao", mListEvento.get(i).getDescricao() + "");
                            a.putString("data", mListEvento.get(i).getDataPublicacao() + "");
                            a.putString("dataInicio", mListEvento.get(i).getDataInicio() + "");
                            a.putString("dataFim", mListEvento.get(i).getDataFim() + "");
                            a.putString("link", mListEvento.get(i).getImageLink() + "");
                            Intent intent = new Intent(getApplicationContext(), EventExpand.class);
                            intent.putExtras(a);
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Aguarde...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                Log.e("LIST", mListEvento.get(0).getTitulo()+"");
            }catch (Exception e){
                Log.e("EXCPTION: ",e.getMessage());
            }
            progress.setVisibility(View.GONE);
        }
    }
    private class DownloadImageTask extends AsyncTask<String , Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... url) {
            Log.e("BACKGROUND", "entrei");
            try{
                HttpURLConnection get = (HttpURLConnection) new URL(url[0]).openConnection();
                get.setRequestMethod("GET");

                get.connect();

                // decoding stream data back into image Bitmap that android understands
                final Bitmap bitmap = BitmapFactory.decodeStream(get.getInputStream());

                get.disconnect();

                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
//               Toast.makeText(mContext, "Erro de imagem", Toast.LENGTH_SHORT).show();


                Log.e("BITMAP","Erro de imagem",e);
            }
            Log.e("BACKGROUND", "Retornei null");
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try{
                image = (bitmap);
                //List<IProfile> list = new ArrayList<IProfile>();
                //list.add(new ProfileDrawerItem().withName("").withEmail(matricula).withIcon(image));
                //mHeaderNavigation.setProfiles(list);



                //notifyDataSetChanged();
                Log.e("ONPOSTEXECUTE", "alterei no bitmap");
                //image.setImageBitmap(bitmap);
            }catch (Exception e){
                Log.e("ERRO", e.getMessage());
                //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
