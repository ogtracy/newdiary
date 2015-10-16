package com.example.cse5324.newdiary2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewNoteActivity extends AppCompatActivity {

    private long timeValue;
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
        if (!intent.getStringExtra("picPath").equals("")) {
            image.setImageBitmap(BitmapFactory.decodeFile(intent.getStringExtra("picPath")));
        }
        timeValue = intent.getLongExtra("time",0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeValue);
        DateFormat df = DateFormat.getDateTimeInstance();
        time.setText(df.format(cal.getTime()));
    }

    public void delete(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deleting Note")
                .setTitle("Confirmation Message");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String itemId = ""+timeValue;
                String selection = NoteContract.NoteEntry.COLUMN_NAME_TIME + "=?";
                String[] selectionArgs = { itemId };
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
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
}
