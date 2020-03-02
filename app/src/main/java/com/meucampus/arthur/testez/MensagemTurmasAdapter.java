package com.meucampus.arthur.testez;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ARTHUR on 18/02/2018.
 */

public class MensagemTurmasAdapter extends BaseAdapter {
    private List<Mensagem> mensagens;
    private Context context;

    public MensagemTurmasAdapter(List<Mensagem> mensagens, Context context){
        this.mensagens = mensagens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mensagens.size();
    }

    @Override
    public Object getItem(int i) {
        return mensagens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v =view.inflate(context, R.layout.mensagem_enviada_professor, null);;
        if (new RepositorioUsuario().getMatricula().equals(mensagens.get(i).getMatriculaRemetente())) {
            //mensagem enviada por mim

            TextView txtMensagem = (TextView) v.findViewById(R.id.mensagem_enviada_turmas);
            TextView txtHora = (TextView) v.findViewById(R.id.hora_enviada_turmas);
            txtMensagem.setText(mensagens.get(i).getTexto());
            txtHora.setText(mensagens.get(i).getHora());
            return v;

        }else{
            View v1 = view.inflate(context, R.layout.mensagem_recebida_professor, null);
            TextView txtMensagem = (TextView) v1.findViewById(R.id.mensagem_recebida);
            TextView txtHora = (TextView) v1.findViewById(R.id.nome_mensagem_recebida);
            Log.e("MAtricula remetente: ", mensagens.get(i).getMatriculaRemetente());
            try {
                Log.e("Dados:::", new RepositorioUsuario().getDataUsuarios());
                JSONArray jsonArray = new JSONArray(new RepositorioUsuario().getDataUsuarios());
                Log.e("JSONARRAY: ", jsonArray.toString());
                for (int j = 0; j<jsonArray.length(); j++){
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    Log.e("JSONOBJECT: ", jsonObject.toString());

                    Log.e("Matrícula: ",jsonObject.getString("matricula"));
                    Log.e("Mensagem: ",mensagens.get(i).getMatriculaRemetente());
                    if(jsonObject.getString("matricula").equals(mensagens.get(i).getMatriculaRemetente())){
                        Log.e("Nome: ",jsonObject.getString("nome"));
                        txtHora.setText(jsonObject.getString("nome"));
                    }

                }
            }catch(Exception e){
                Log.e("msg", "error");
                e.printStackTrace();
            }
            txtMensagem.setText(mensagens.get(i).getTexto());

            return v1;
            //{"id":102,"texto":"teste...","data":"25/11/2017","hora":"14:14","recebida":false,"idSetor":26329,"matriculaRemetente":"2010","matriculaDestinatario":"0"}]
        }
    }


}
