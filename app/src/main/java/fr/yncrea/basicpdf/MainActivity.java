package fr.yncrea.basicpdf;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;

    //Views
    Button mSaveBtn;

    // generated data
    Random random = new Random();
    int mFactureRef = random.nextInt(999) + 1;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate date =  LocalDate.now();
    String today = dtf.format(date);
    String nextMonth = dtf.format(date.plusMonths(1));



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


    private void savePdf(){
        String mFileName = "Document-" + mFactureRef;
        String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mFileName + ".pdf";
        PdfWriter writer;

        // generating text
            try {
                writer = new PdfWriter(new FileOutputStream(mPath));
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // header
                Text mTitre = new Text("Malterie Brasserie des Carnutes")
                        .setFontSize(30).setBold();
                Text mSousTitre = new Text("37 rue des Montfort \n 45170 Neuville-aux-Bois")
                        .setFontSize(20);
                Text mFacture = new Text("Facture : " + mFactureRef)
                        .setFontSize(30);
                Text mDate = new Text("Date : " + today)
                        .setFontSize(20).setWidth(100);
                // LINE SEPARATOR
                LineSeparator lineSeparator = new LineSeparator(new DottedLine());
                lineSeparator.setStrokeColor(new DeviceRgb(0, 0, 68));

                // creating table
                float [] pointColumnWidths = {50F, 150F, 50F, 100F, 100F, 100F};
                Table table = new Table(pointColumnWidths);

                // creating cells
                table.addCell(new Cell().add("Réf").setBackgroundColor(Color.RED).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold());
                table.addCell(new Cell().add("Désignation").setBackgroundColor(Color.RED).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold());
                table.addCell(new Cell().add("Unité").setBackgroundColor(Color.RED).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold());
                table.addCell(new Cell().add("Quantité").setBackgroundColor(Color.RED).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold());
                table.addCell(new Cell().add("PU HT").setBackgroundColor(Color.RED).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold());
                table.addCell(new Cell().add("Total HT").setBackgroundColor(Color.RED).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold());
                table.setFontSize(15);
                //FONCTION REMPLISSAGE


                // creating bottom table
                float [] arrangedPointColumnWidths = {700F,300F};
                Table bottomTable = new Table(arrangedPointColumnWidths);
                // creating cells
                bottomTable.addCell(new Cell().add("Mode de régelment : Virement \n Echeance de paiement : " + nextMonth +"  \n Réglements :").setBorder(Border.NO_BORDER));
                bottomTable.addCell(new Cell().add("Total HT \nRéglements :\nNet à payer : ").setBorder(Border.NO_BORDER).setBackgroundColor(Color.RED).setFontColor(Color.WHITE));
                bottomTable.addCell(new Cell().add("\n\n\nTVA non applicable, article 293B du CGI").setBorder(Border.NO_BORDER));
                bottomTable.setFixedPosition(40,40,500).setFontSize(15);




                // adding content to document
                document.add(new Paragraph(mTitre));
                document.add(new Paragraph(mSousTitre));
                document.add(new Paragraph(mDate));

                // space with line
                document.add(new Paragraph(""));
                document.add(lineSeparator);
                document.add(new Paragraph(""));

                document.add(new Paragraph(mFacture));
                document.add(table);
                document.add(bottomTable);
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