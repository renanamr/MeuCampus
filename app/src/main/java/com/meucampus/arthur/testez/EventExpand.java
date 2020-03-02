package com.meucampus.arthur.testez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EventExpand extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView txtTitulo;
    private TextView txtDescricao;
    private TextView txtData;
    private TextView txtInicio;
    private TextView txtFim;
    private ImageView imagem;

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
        setContentView(R.layout.activity_evento_expand);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String titulo = bundle.getString("titulo");
        String descricao = bundle.getString("descricao");
        String dataI = bundle.getString("dataInicio");
        String dataF = bundle.getString("dataFim");
        String link;
        try{
            link = bundle.getString("link");
        }catch(Exception e){
            e.printStackTrace();
        }

        mToolbar = (Toolbar) findViewById(R.id.tb_Evento);

        mToolbar.setTitle("Evento");
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
        TextView txtData1 = (TextView) findViewById(R.id.TxtDataImg);
        txtData1.setText(dataI.substring(0,2));
        TextView txtData2 = (TextView) findViewById(R.id.TxtDataImg2);
        txtData2.setText(getMes(dataI));

        txtTitulo = (TextView) findViewById(R.id.TituloEventoE);
        txtTitulo.setText(titulo);
        txtDescricao = (TextView) findViewById(R.id.DescricaoEventoE);
        txtDescricao.setText(descricao);
        txtInicio = (TextView) findViewById(R.id.TxtData7);
        txtInicio.setText(dataI);
        TextView periodo = (TextView) findViewById(R.id.txtPeriodoE);
        periodo.setText("Início: "+dataI.substring(0,2)+", "+getMes(dataI)+" - Fim: "+dataF.substring(0,2)+", "+getMes(dataF));


    }
    public String getMes(String data){
        String mes = data.substring(3,5);
        int mes1 = Integer.parseInt(mes);
        Log.e("mes1", mes1+"");
        switch (mes1){
            case 1:
                return "Jan";

            case 2:
                return "Fev";

            case 3:
                return  "Mar";

            case 4:
                return "Abr";

            case 5:
                return "Mai";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Ago";

            case 9:
                return "Set";

            case 10:
                return "Out";

            case 11:
                return "Nov";

            case 12:
                return "Dez";


        }
        return null;
    }
}
