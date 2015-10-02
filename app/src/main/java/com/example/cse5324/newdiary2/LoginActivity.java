package com.example.cse5324.newdiary2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    public static final String PROFILE = "diaryProfile";
    public static final String PROFILE_CREATED = "profileCreated";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText p1;
    private EditText p2;
    private EditText p3;
    private EditText p4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //check for registration
        SharedPreferences settings = getSharedPreferences(PROFILE, 0);
        if (!settings.contains(PROFILE_CREATED)){
            createNewProfile();
        }

        // Set up the login form.

        p1 = (EditText) findViewById(R.id.password1);
        p2 = (EditText) findViewById(R.id.password2);
        p3 = (EditText) findViewById(R.id.password3);
        p4 = (EditText) findViewById(R.id.password4);

        p1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p2.requestFocus();
            }
        });

        p2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p3.requestFocus();
            }
        });

        p3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p4.requestFocus();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(View v) {

        // Reset errors.
        p1.setError(null);
        p2.setError(null);
        p3.setError(null);
        p4.setError(null);

        // Store values at the time of the login attempt.
        String password1 = p1.getText().toString();
        String password2 = p2.getText().toString();
        String password3 = p3.getText().toString();
        String password4 = p4.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        String password = password1 + password2 + password3 + password4;
        if ( !isPasswordValid(password)) {
            p1.setText("");
            p2.setText("");
            p3.setText("");
            p4.setText("");
            p1.setError(getString(R.string.error_invalid_password));
            focusView = p1;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            SharedPreferences settings = getSharedPreferences(PROFILE, 0);
            if (!settings.contains(PROFILE_CREATED)){
                createNewProfile();
            } else {
                String passcode = settings.getString("passcode","");
                if( TextUtils.equals(passcode,password)){
                    //login successful
                    Button loginButton = (Button) findViewById(R.id.sign_in_button);
                    loginButton.setText(R.string.sign_in_successful);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    p1.setText("");
                    p2.setText("");
                    p3.setText("");
                    p4.setText("");
                    p1.setError(getString(R.string.error_incorrect_password));
                    focusView = p1;
                    focusView.requestFocus();
                }
            }

        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() == 4;
    }

    public void createNewProfile(){
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
    }
}