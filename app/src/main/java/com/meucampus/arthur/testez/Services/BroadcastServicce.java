package com.meucampus.arthur.testez.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ARTHUR on 28/01/2018.
 */

public class BroadcastServicce extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServicoNotificacao.class));
        context.startService(new Intent(context, ServicoGetProfessores.class));
        context.startService(new Intent(context, ServicoAtualizacao.class));
        Log.e("Broadcast-meucampus", "Ok");
    }
}
