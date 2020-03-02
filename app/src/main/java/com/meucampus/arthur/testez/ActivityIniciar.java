package com.meucampus.arthur.testez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ActivityIniciar extends AppCompatActivity {

    Toolbar toolbar;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar);
        toolbar = (Toolbar) findViewById(R.id.tb_iniciar);
        list = (ListView) findViewById(R.id.list_iniciar);

        toolbar.setTitle("Iniciar chat");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        List<String> setor = new ArrayList<>();
        setor.add("Setores");
        setor.add("Professores");
        SetorAdapter s = new SetorAdapter(getApplicationContext(), setor);
        list.setAdapter(s);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    Intent intent = new Intent(getApplicationContext(), ActivityNovaConversaProfessor.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), ActivityNovaConversaAluno.class);
                    startActivity(intent);
                }
            }
        });

    }
}
