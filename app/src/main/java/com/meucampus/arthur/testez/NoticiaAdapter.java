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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by ARTHUR on 15/09/2016.
 */
public class NoticiaAdapter extends BaseAdapter{
    private Context mContext;
    public static List<Noticia> mList;

    public NoticiaAdapter(Context mContext, List<Noticia> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

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
    int index = 0;
    ImageView img;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        index = i;
        View v = View.inflate(mContext, R.layout.list_layout, null);
        TextView tit = (TextView) v.findViewById(R.id.txtTituloN);
        TextView desc = (TextView) v.findViewById(R.id.textView11);
        TextView dat = (TextView) v.findViewById(R.id.txtDataN);
        img = (ImageView) v.findViewById(R.id.imageNoticia);
        dat.setText(mList.get(i).getData());
        tit.setText(mList.get(i).getTitulo());
        if (mList.get(i).getDescricao().length()>=90) {
            desc.setText("    "+mList.get(i).getDescricao().substring(0, 90) + "...");
        }else{
            desc.setText("    "+mList.get(i).getDescricao());

        }
            if(mList.get(i).getImagemLink()!=null){
               // Picasso.with(mContext).load(mList.get(i).getImagemLink()).into(img);
            download d = new download();
            d.setImageView(img);
            d.execute(mList.get(i).getImagemLink());
        }
        else
            img.setVisibility(View.GONE);
        //desc.setText(mList.get(i).getDescricao());
        Log.e("ALTERA NO LISTVIEW", "ok");
        return v;


    }
    public List<Noticia> getList(){
        return mList;
    }

    private class download extends AsyncTask<String, Void, Bitmap>{

        private ImageView imageView;

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.e("IMAGE LINK", strings[0]+".");
            String add ="";
            if(!strings[0].contains("http")){
                add="http://";
            }
            try {

                java.net.URL url = new java.net.URL(add + strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap aVoid) {
            imageView.setImageBitmap(aVoid);
            //Log.e("iMG", aVoid.toString());

        }

    }

}
