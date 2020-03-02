package com.meucampus.arthur.testez;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NoticiaExpand extends AppCompatActivity {

    Toolbar mToolbar;
    TextView mTextTitulo;
    TextView mTextDescricao;
    TextView mTextData;
    ImageView mImgNoticia;

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
        setContentView(R.layout.activity_noticia_expand);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String titulo = bundle.getString("titulo");
        String descricao = bundle.getString("descricao");
        String data = bundle.getString("data");
        String linkImg = bundle.getString("foto");

        mToolbar = (Toolbar) findViewById(R.id.tb_noticia);
        mToolbar.setTitle("Notícia");
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
        mImgNoticia = (ImageView) findViewById(R.id.imageView3);

        Picasso.with(getApplicationContext()).load("http://"+linkImg).into(mImgNoticia);
        mTextTitulo = (TextView) findViewById(R.id.tituloNoticiaE);
        mTextTitulo.setText(titulo);
        mTextDescricao = (TextView) findViewById(R.id.descricaoNoticiaE);
        mTextDescricao.setText(descricao);
        mTextData = (TextView) findViewById(R.id.dataNoticaE);
        mTextData.setText("Dia "+ data );


    }
}
