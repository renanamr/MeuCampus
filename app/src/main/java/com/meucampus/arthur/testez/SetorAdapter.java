package com.meucampus.arthur.testez;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 17/10/2017.
 */
public class SetorAdapter extends BaseAdapter{
    List<String> setores = new ArrayList<>();
    Context context;
    public SetorAdapter(Context context, List<String> setores){
        this.setores = setores;
        this.context = context;
    }
    @Override
    public int getCount() {
        return setores.size();
    }

    @Override
    public Object getItem(int i) {
        return setores.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.layout_setor, null);
        TextView s = (TextView) v.findViewById(R.id.setor_txt);
        s.setText(setores.get(i));
        return v;

    }
}
