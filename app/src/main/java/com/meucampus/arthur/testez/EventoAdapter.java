package com.meucampus.arthur.testez;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by ARTHUR on 15/09/2016.
 */
public class EventoAdapter extends BaseAdapter {
    private Context mContext;
    private List<Evento> mList;
    private ImageView image;
    int i = 0;
    private View v;
    public EventoAdapter(Context mContext, List<Evento> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    private Bitmap bitmap23;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public TextView data1;

    public TextView dataMes;
    TextView periodo;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        v = View.inflate(mContext, R.layout.list_evento_layout, null);
        TextView tit = (TextView) v.findViewById(R.id.txtTituloN);

        data1 = (TextView) v.findViewById(R.id.TxtDataImg3);
        dataMes = (TextView) v.findViewById(R.id.TxtDataImg4);
        data1.setText("" +mList.get(i).getDataInicio().substring(0,2));
        dataMes.setText(""+getMes(mList.get(i).getDataInicio()));

        TextView datI = (TextView) v.findViewById(R.id.TxtData);
        try{
            TextView a  = (TextView) v.findViewById(R.id.TxtLocal);
            //a.setText(mList.get(i).getLocal()+"");
            a.setText("");
        }catch(Exception e){

        }


        Log.e("DatI", mList.get(i).getDataPublicacao());
        datI.setText(mList.get(i).getDataPublicacao());
        tit.setText(mList.get(i).getTitulo());
        image = (ImageView) v.findViewById(R.id.imageEvento1);
        image.setVisibility(View.VISIBLE);
        //image.setImageResource(R.drawable.calendar);

        try {
            boolean imageNull = mList.get(i).getImageLink().equals("");
            if (imageNull == false && !mList.get(i).getImageLink().equals("")) {

                new DownloadImageTask().execute(mList.get(i).getImageLink());

                image.setVisibility(View.VISIBLE);
                //image.setImageResource(R.drawable.calendar);
                data1.setVisibility(View.VISIBLE);
                dataMes.setVisibility(View.VISIBLE);

            } else {
                image.setVisibility(View.VISIBLE);

                image.setVisibility(View.VISIBLE);
                //image.setImageResource(R.drawable.calendar);
                data1.setVisibility(View.VISIBLE);
                dataMes.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex ){
            //Log.e("ERRO", ex.getMessage());
            image.setVisibility(View.VISIBLE);
            data1.setVisibility(View.VISIBLE);
            dataMes.setVisibility(View.VISIBLE);
            //image.setVisibility(View.INVISIBLE);

        }
        datI.setText(mList.get(i).getDataInicio());
        Log.e("TESTE", dataMes.getText().toString());
        return v;


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
                image = (ImageView) v.findViewById(R.id.imageEvento1);
                image.setImageBitmap(bitmap);
                data1.setVisibility(View.INVISIBLE);
                dataMes.setVisibility(View.INVISIBLE);
                //notifyDataSetChanged();
                Log.e("ONPOSTEXECUTE33", "alterei no bitmap");
                //image.setImageBitmap(bitmap);
            }catch (Exception e){
                Log.e("ERRO", e.getMessage());
                //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}




