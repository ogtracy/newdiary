package com.example.cse5324.newdiary2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity {

    private Button dateButton;
    private Button timeButton;
    private EditText title;
    private EditText text;
    private Button saveButton;
    private ImageView imageView;
    private final String NOTESFILE ="comexamplecse5324newdiary2.notesFile";
    private final String SEPARATOR = "this is a note separator and should not be used any other way";
    private Calendar cal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        getViews();
    }

    private void getViews(){
        dateButton = (Button) findViewById(R.id.dateButton);
        timeButton = (Button) findViewById(R.id.timeButton);
        title = (EditText) findViewById(R.id.title);
        text = (EditText) findViewById(R.id.text);
        saveButton = (Button) findViewById(R.id.saveButton);
        imageView = (ImageView) findViewById(R.id.imageView);

        cal = Calendar.getInstance();
        Date d = cal.getTime();
        DateFormat df = DateFormat.getDateInstance();
        DateFormat tf = DateFormat.getTimeInstance();
        dateButton.setText(df.format(d));
        timeButton.setText(tf.format(d));

    }

    private boolean validateInput(){

        return false;
    }

    /**
     *
     * @param v
     *
     * This method saves the new note to the top of the file containing all notes
     * TODO make this an AsyncTask so it doesn't stall the rest of the app.
     */
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


        try {
            File file = new File(getFilesDir().getAbsolutePath(), NOTESFILE);
            if(file.exists()) {
                FileInputStream fis = openFileInput(NOTESFILE);
                FileOutputStream fosTemp = openFileOutput("tempFile", Context.MODE_PRIVATE);

                //write new note to temporary file
                fosTemp.write(SEPARATOR.getBytes());
                fosTemp.write(time.getBytes());
                fosTemp.write(title.getBytes());
                fosTemp.write("\n".getBytes());
                fosTemp.write(text.getBytes());
                fosTemp.write(SEPARATOR.getBytes());

                //copy saved notes to temporary file
                byte[] buffer = new byte[1024];
                while (fis.read(buffer) != -1) {
                    fosTemp.write(buffer);
                }
                fis.close();
                fosTemp.close();

                //save all notes in correct file
                FileOutputStream fos = openFileOutput(NOTESFILE, Context.MODE_PRIVATE);
                FileInputStream fis2 = openFileInput("tempFile");
                while (fis2.read(buffer) != -1) {
                    fos.write(buffer);
                }
                fos.close();
                fis2.close();
                finish();
            } else{
                // save directly to save file
                FileOutputStream fos = openFileOutput(NOTESFILE, Context.MODE_PRIVATE);
                fos.write(SEPARATOR.getBytes());
                fos.write(time.getBytes());
                fos.write(title.getBytes());
                fos.write("\n".getBytes());
                fos.write(text.getBytes());
                fos.write(SEPARATOR.getBytes());
                fos.close();
                finish();
            }

        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
