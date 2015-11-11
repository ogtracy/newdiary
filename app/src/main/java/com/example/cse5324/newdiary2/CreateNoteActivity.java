package com.example.cse5324.newdiary2;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText title;
    private EditText text;
    private ImageView imageView;
    private String picturePath="";
    private final int SPEECH_INPUT = 555;
    private final int RESULT_LOAD_IMAGE =745;
    private Calendar cal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        getViews();
    }

    private void getViews(){
        Button dateButton = (Button) findViewById(R.id.dateButton);
        Button timeButton = (Button) findViewById(R.id.timeButton);
        title = (EditText) findViewById(R.id.title);
        text = (EditText) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.imageView);
        ImageButton btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        cal = Calendar.getInstance();
        Date d = cal.getTime();
        DateFormat df = DateFormat.getDateInstance();
        DateFormat tf = DateFormat.getTimeInstance();
        dateButton.setText(df.format(d));
        timeButton.setText(tf.format(d));

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text.setText(text.getText() + " " +result.get(0));
                }
                break;
            }
            case RESULT_LOAD_IMAGE: {
                if (resultCode == RESULT_OK && null!=data){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor!= null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
            }

        }
    }

    public void importImage(View v){
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void save(View v){
        String title = this.title.getText().toString();
        String text = this.text.getText().toString();
        String time = ""+cal.getTimeInMillis();
        View focusView = null;
        boolean cancel = false;

        //validate input
        if (TextUtils.isEmpty(title)){
            title = "Dear Diary,";
            this.title.setText(title);
        }
        if (TextUtils.isEmpty(text)){
            this.text.setError(getString(R.string.error_field_required));
            focusView = this.text;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
            return;
        }

        saveInDB(title, text, time);
    }

    /**
     * TODO make this an AsyncTask so it doesn't stall the rest of the app.
     */
    private void saveInDB(String title, String text, String time) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NAME_IMG, picturePath);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_TEXT, text);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_TIME, time);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_TITLE, title);
        long newRowId = db.insert(NoteContract.NoteEntry.TABLE_NAME, "null", values);
        db.close();
        finish();
    }
}
