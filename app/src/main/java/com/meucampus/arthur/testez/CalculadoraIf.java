package com.meucampus.arthur.testez;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.meucampus.arthur.testez.Services.AcessoNotas;
import com.meucampus.arthur.testez.Services.ServicoTeste;

import java.util.ArrayList;

/* Created by renan on 13/10/2016.
 */
public class CalculadoraIf extends AppCompatActivity {
    static EditText n1, n2, n3, n4,n5;
    TextView m, m1,m2,m3;
    Button resultado;
    int branco;
    int troca;
    double[] nCalc =new double[5];
    double result,falta;
    Toolbar mToolbar;
    double menor=100;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Spinner spinner;
    private String turma;
    ArrayList<String> spinnerArray;

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
        setContentView(R.layout.activity_calc);

//        Log.e("TesteCalc", new AcessoNotas(getApplicationContext()).listar().size()+"");
        mTabLayout =(TabLayout) findViewById(R.id.tab_layout);
        mViewPager=(ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new PagerAdp(getSupportFragmentManager(),getResources().getStringArray(R.array.titles_tab)));
        mTabLayout.setupWithViewPager(mViewPager);
        Toast.makeText(getApplicationContext(), "Carregando disciplinas...", Toast.LENGTH_LONG).show();
        mToolbar = (Toolbar) findViewById(R.id.tb_calc);
        resultado = (Button) findViewById(R.id.buttoncalc);
        mToolbar.setTitle("Cálculo de médias");
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

        /*
        spinnerArray= new ArrayList<String>();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);//
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                turma = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }





}
