package com.example.cse5324.newdiary2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewNoteActivity extends AppCompatActivity {

    private long timeValue;
    private MyListItem thisItem;
    private static final String LOG_TAG = "E-Motions";

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        TextView time = (TextView) findViewById(R.id.time);
        TextView title = (TextView) findViewById(R.id.title);
        TextView text = (TextView) findViewById(R.id.text);
        ImageView image = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        text.setText(intent.getStringExtra("text"));
        String picPath = intent.getStringExtra("picPath");
        if (!picPath.equals("")) {
            image.setImageBitmap(BitmapFactory.decodeFile(picPath));
        }
        timeValue = intent.getLongExtra("time",0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeValue);
        DateFormat df = DateFormat.getDateTimeInstance();
        time.setText(df.format(cal.getTime()));
        thisItem = new DiaryListItem(picPath, title.getText().toString(), text.getText().toString(),
                cal);
    }

    public void delete(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deleting Note")
                .setTitle("Confirmation Message");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String itemId = "" + timeValue;
                String selection = NoteContract.NoteEntry.COLUMN_NAME_TIME + "=?";
                String[] selectionArgs = {itemId};
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
                db.close();

                Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*public void saveToPDF(View v){
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = "Note " + thisItem.getName();
        printManager.print(jobName, new MyPrintDocumentAdapter(this, thisItem, null), null);
    }*/

    public void editNote(View v){
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra(DiaryListItem.TITLE, thisItem.getName());
        intent.putExtra(DiaryListItem.TEXT, thisItem.getDescription());
        intent.putExtra(DiaryListItem.TIME, thisItem.getID());
        intent.putExtra(DiaryListItem.PIC_PATH, thisItem.getPicPath());
        startActivity(intent);
        finish();
    }


    public void saveToPDF(View v)  {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Emotions");
        if (!pdfFolder.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
            //Toast.makeText(getApplicationContext(), "Directory could not be created", Toast.LENGTH_LONG).show();
        }
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);
        String filename = thisItem.getName() + "_" +timeStamp + ".pdf";
        File myFile = new File(pdfFolder, filename);

        try {
            OutputStream output = new FileOutputStream(myFile);
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();
            document.add(new Paragraph(thisItem.getName(), catFont));

            String imgPath = thisItem.getPicPath();
            Image img;
            if (!imgPath.equals("")) {
                img = Image.getInstance(thisItem.getPicPath());
                if (img.getScaledWidth() > 300 || img.getScaledHeight() > 300) {
                    img.scaleToFit(300, 300);
                }
                document.add(img);
            }

            document.add(new Paragraph(thisItem.getFormatted()));
            document.close();
            Toast.makeText(getApplicationContext(), "File Successfully Created", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "PDF File could not be created", Toast.LENGTH_LONG).show();
        }
    }

}
