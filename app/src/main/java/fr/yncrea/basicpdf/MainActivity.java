package fr.yncrea.basicpdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;

    //Views
    EditText mTextEt;
    Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views
        mSaveBtn = findViewById(R.id.saveBtn);

        //handle button click
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // version
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    // check storage permission
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    }

                    else {
                        savePdf();

                    }
                }
                else {
                    savePdf();
                }

            }
        });
    }

    /* //v5
    private void savePdf() {
        // initialize doc
        Document mDoc = new Document();
        String mFileName = "Doki Dok";
        String mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mFileName + ".pdf";

        // build doc
        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();
            mDoc.addTitle("Titre");
            String mText = mTextEt.getText().toString();
            mDoc.add(new Paragraph(mText));
            mDoc.add(new Paragraph("texte 1"));
            mDoc.add(new Paragraph("texte 2"));
            mDoc.close();
            Toast.makeText(this, mFileName + ".pdf \n is saved to \n" + mFilePath, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/

    private void savePdf(){
        String mFileName = "Document";
        String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mFileName + ".pdf";
        PdfWriter writer;

            try {
                writer = new PdfWriter(new FileOutputStream(mPath));
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                Text text = new Text("Hello World PDF created using iText")
                        .setFontSize(15);

                //Add paragraph to the document
                document.add(new Paragraph(text));
                document.add(new Paragraph("je sais ecrire!"));
                document.close();
                Toast.makeText(this, mFileName + ".pdf \n is saved to \n" + mPath, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

    }

    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}