package com.meucampus.arthur.testez;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ARTHUR on 05/05/2017.
 */
public class TurmasAdapter extends BaseAdapter {
    Context context;
    List<String> jsonElements;
    String tipo;
    TextView textView;
    TextView txtNome;
    String nome;
    ImageView img;
    int ordem=1;

    public TurmasAdapter(Context context, List<String> jsonElements, String tipo) {
        this.context = context;
        this.jsonElements = jsonElements;
        this.tipo = tipo;
    }

    @Override
    public int getCount() {
        return jsonElements.size();
    }

    @Override
    public Object getItem(int i) {
        return jsonElements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int numeroOrdem = jsonElements.size() - i;
        if(tipo.equals("1")){
            view = View.inflate(context, R.layout.list_turmas, null);
            textView = (TextView) view.findViewById(R.id.txtTurma);
            txtNome = (TextView) view.findViewById(R.id.txtNomeP);

            img=(ImageView) view.findViewById(R.id.imgTurma);
            if (jsonElements != null)
                try{
                    JSONObject jsonObject = new JSONObject(jsonElements.get(i));
                    Log.e("JSON" + i, jsonObject.getString("disciplina"));

                    nome=(jsonObject.getString("disciplina"));
                    if (nome.substring(nome.length()-1,nome.length()).equals(")")){

                        for (int u=0;u < nome.length();u++){
                                textView.setText(nome.substring(11,nome.length()));

                        }

                    }else {
                    textView.setText(nome.substring(11,nome.length()));
                    }
                    txtNome.setText(jsonObject.getString("nomeProfessor"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            else
                textView.setText("Nada castrado");
        }else{
            ActivityTurmas activityTurmas= new ActivityTurmas();
            view = View.inflate(context, R.layout.list_turma_2, null);
            TextView nome;
            TextView posicao;
            TextView turma;
            posicao = (TextView) view.findViewById(R.id.TxtOrdem);
            nome = (TextView) view.findViewById(R.id.TxtMaterial);
            turma = (TextView) view.findViewById(R.id.TxtNomeMateria);
            try{
                //JSONObject jsonObject = new JSONObject(jsonElements.get(i));
                nome.setText(jsonElements.get(i));
                turma.setText(activityTurmas.materia);
                if (numeroOrdem>=10)
                    posicao.setText(""+(numeroOrdem));
                else
                    posicao.setText("0" + (numeroOrdem ));

                Log.e("JSON TEXT DES", jsonElements.get(i));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return view;
    }
}
