package com.meucampus.arthur.testez.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ARTHUR on 26/01/2018.
 */

public class ServicoTeste extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
      //  new Worker().run();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //new Worker().run();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //new Worker().run();
        super.onDestroy();

    }
    private class Worker extends Thread{

        @Override
        public void run() {
            while (true){
                Log.e("Servico", "ok");
                try{
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
