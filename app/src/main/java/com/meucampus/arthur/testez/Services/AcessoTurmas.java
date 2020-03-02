package com.meucampus.arthur.testez.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meucampus.arthur.testez.BDCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 05/03/2018.
 */

public class AcessoTurmas {
    private SQLiteDatabase bd;
    public AcessoTurmas(Context context){
        BDCore auxbd = new BDCore(context);
        bd = auxbd.getWritableDatabase();
    }

}
