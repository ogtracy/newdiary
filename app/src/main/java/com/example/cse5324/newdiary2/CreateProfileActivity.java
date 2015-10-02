package com.example.cse5324.newdiary2;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class CreateProfileActivity extends AppCompatActivity {

    private String PREFS_NAME = "diaryProfile";

    private EditText fName;
    private EditText lName;
    private EditText email;
    private EditText passcode;
    private EditText confirmPasscode;
    private EditText secQ1;
    private EditText secQ2;
    private EditText secA1a;
    private EditText secA1b;
    private EditText secA2a;
    private EditText secA2b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getViews();
    }

    private void getViews(){
        fName = (EditText) findViewById(R.id.firstName);
        lName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        passcode = (EditText) findViewById(R.id.passcode);
        confirmPasscode = (EditText) findViewById(R.id.confirmPasscode);
        secQ1 = (EditText) findViewById(R.id.secQuestion1);
        secA1a = (EditText) findViewById(R.id.secAnswer1A);
        secA1b = (EditText) findViewById(R.id.secAnswer1B);
        secQ2 = (EditText) findViewById(R.id.secQuestion2);
        secA2a = (EditText) findViewById(R.id.secAnswer2A);
        secA2b = (EditText) findViewById(R.id.secAnswer2B);

    }

    public void attemptSave(View v){

        boolean cancel = false;
        View focusView = null;

        String fName = this.fName.getText().toString();
        String lName = this.lName.getText().toString();
        String email = this.email.getText().toString();
        String passcode = this.passcode.getText().toString();
        String confirmPasscode = this.confirmPasscode.getText().toString();
        String ques1 = this.secQ1.getText().toString();
        String ques2 = this.secQ2.getText().toString();
        String ans1a = this.secA1a.getText().toString();
        String ans1b = this.secA1b.getText().toString();
        String ans2a = this.secA2a.getText().toString();
        String ans2b = this.secA2b.getText().toString();


        if (TextUtils.isEmpty(fName)) {
            this.fName.setError(getString(R.string.error_field_required));
            focusView = this.fName;
            cancel = true;
        }
        if (TextUtils.isEmpty(lName)) {
            this.lName.setError(getString(R.string.error_field_required));
            focusView = this.lName;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)){
            this.email.setError(getString(R.string.error_field_required));
            focusView = this.email;
            cancel = true;
        }
        if (TextUtils.isEmpty(passcode)){
            this.passcode.setError(getString(R.string.error_field_required));
            focusView = this.passcode;
            cancel = true;
        }
        if (TextUtils.isEmpty(confirmPasscode)) {
            this.confirmPasscode.setError(getString(R.string.error_field_required));
            focusView = this.confirmPasscode;
            cancel = true;
        }

        if (!TextUtils.equals(passcode, confirmPasscode)){
            this.passcode.setError(getString(R.string.error_passwords_not_matching));
            focusView = this.passcode;
            this.passcode.setText("");
            this.confirmPasscode.setText("");
            cancel = true;
        }

        if (TextUtils.isEmpty(ques1)) {
            this.secQ1.setError(getString(R.string.error_field_required));
            focusView = this.secQ1;
            cancel = true;
        }
        if (TextUtils.isEmpty(ans1a)){
            this.secA1a.setError(getString(R.string.error_field_required));
            focusView = this.secA1a;
            cancel = true;
        }
        if (TextUtils.isEmpty(ans1b)){
            this.secA1b.setError(getString(R.string.error_field_required));
            focusView = this.secA1b;
            cancel = true;
        }

        if (!TextUtils.equals(ans1a, ans1b)){
            this.secA1a.setError(getString(R.string.error_answers_not_matching));
            focusView = this.secA1a;
            secA1a.setText("");
            secA1b.setText("");
            cancel = true;
        }

        if (TextUtils.isEmpty(ques2)) {
            this.secQ2.setError(getString(R.string.error_field_required));
            focusView = this.secQ2;
            cancel = true;
        }
        if (TextUtils.isEmpty(ans2a)){
            this.secA2a.setError(getString(R.string.error_field_required));
            focusView = this.secA2a;
            cancel = true;
        }
        if (TextUtils.isEmpty(ans2b)){
            this.secA2b.setError(getString(R.string.error_field_required));
            focusView = this.secA2b;
            cancel = true;
        }

        if (!TextUtils.equals(ans2a, ans2b)){
            this.secA2a.setError(getString(R.string.error_answers_not_matching));
            secA2a.setText("");
            secA2b.setText("");
            focusView = this.secA2a;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        } else {
            SharedPreferences settings = getSharedPreferences(LoginActivity.PROFILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("firstName", fName);
            editor.putString("lastName", lName);
            editor.putString("email", email);
            editor.putString("passcode", passcode);
            editor.putString("ques1", ques1);
            editor.putString("ans1", ans1a);
            editor.putString("ques2", ques2);
            editor.putString("ans2", ans2a);
            editor.putBoolean(LoginActivity.PROFILE_CREATED, true);

            editor.commit();
            finish();
        }
    }
}
