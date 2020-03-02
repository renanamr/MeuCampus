package com.meucampus.arthur.testez.Fragments;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.meucampus.arthur.testez.R;
import com.meucampus.arthur.testez.RepositorioUsuario;

import java.io.File;

public class PdfOpen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_open);

        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MeuCampus");
        dir.mkdirs();
        File file = new File(dir, "meucampus-horario.pdf");
        pdfView.fromFile(file).load();

    }
}
