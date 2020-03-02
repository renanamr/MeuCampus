package com.meucampus.arthur.testez;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by renan on 06/10/2016.
 */
public class MensagemAdapter extends BaseAdapter{
    private Context context;
    private List<Mensagem> mensagem;

    private String tipo;
    private boolean flag = true;
    public MensagemAdapter(Context context, List<Mensagem> mensagem,String tipo) {
        this.context = context;
        this.mensagem = mensagem;

        this.tipo = tipo;
    }


    @Override
    public int getCount() {
        return mensagem.size();
    }

    @Override
    public Object getItem(int i) {
        return mensagem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.chat_layout, null);
        Log.e("LOGaaaa", mensagem.size()+"");
        TextView message = (TextView) v.findViewById(R.id.mensagemTxt);
        TextView hora = (TextView) v.findViewById(R.id.horaR);
        if (i == 0)
            flag = true;


//        Log.e("ADAPTER: ", mensagem.get(i).getTexto() + mensagem.get(i).isTipoMensagem(tipo));
        /*if (mensagem.get(i).isTipoMensagem(tipo)) {
            hora.setText(mensagem.get(i).getHora());
            message.setText(mensagem.get(i).getTexto());
            //message.setTextColor(v.getResources().getColor(R.color.colorAccent));
            //message.setBackgroundColor(v.getResources().getColor(R.color.colorSend));

            message.setGravity(Gravity.LEFT);


        }else{
            v = View.inflate(context, R.layout.mensagem_enviada, null);
            TextView msm = (TextView) v.findViewById(R.id.mensagemTxt2);
            TextView hor = (TextView) v.findViewById(R.id.horaE);
            hor.setText(mensagem.get(i).getHora());
            msm.setText(mensagem.get(i).getTexto());

        }*/

        //if(flag){
            Log.e("ULTIMO TESTE FIIII", "matricula remetente: " + mensagem.get(i).getMatriculaRemetente() + "   matricula destinatario: " + mensagem.get(i).getMatriculaDestinatario());
            if(mensagem.get(i).getMatriculaRemetente().equals(new RepositorioUsuario().getMatricula())){
                v = View.inflate(context, R.layout.mensagem_enviada, null);
                TextView msm = (TextView) v.findViewById(R.id.mensagemTxt2);
                TextView hor = (TextView) v.findViewById(R.id.horaE);
                hor.setText(mensagem.get(i).getHora());
                msm.setText(mensagem.get(i).getTexto());
            }else{
                hora.setText(mensagem.get(i).getHora());
                message.setText(mensagem.get(i).getTexto());
            }
        //}else
          //  Log.e("ELSE<><>", "ENTREIWW");

        if (i == mensagem.size()-1)
            flag = false;
        return v;


    }
}
