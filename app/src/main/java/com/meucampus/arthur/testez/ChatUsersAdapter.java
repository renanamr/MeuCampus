package com.meucampus.arthur.testez;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ARTHUR on 18/11/2016.
 */
public class ChatUsersAdapter extends BaseAdapter{
    List<Usuario> list;
    Context context;
    View v;
    TextView matricula;
    TextView mensagem;



    public ChatUsersAdapter(Context context, List<Usuario> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        v = View.inflate(context, R.layout.list_chat_users, null);
        Log.e("Bem,", "Chegamos aqui");
        matricula = (TextView) v.findViewById(R.id.matricula_messagem);
        mensagem = (TextView) v.findViewById(R.id.ultima_mensagem);
        matricula.setText(list.get(i).getMatricula());

        mensagem.setText(list.get(i).getLastM());

        return v;
    }
}
