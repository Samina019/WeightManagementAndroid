package developers.weightmanagement.Startup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Objects;

import developers.weightmanagement.BMI.BMIActivity;
import developers.weightmanagement.R;
import developers.weightmanagement.Room.DatabaseClient;

public class LoginActivity extends AppCompatActivity {

    LinearLayout redirectToRegister;
    Button btnLogin;
    private TextInputLayout layoutPassword,layoutEmailOrPhoneNumber;
    EditText etEmailOrPhoneNumber,etPassword;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerViews();
        registerListeners();
    }

    private void registerViews(){
        redirectToRegister = findViewById(R.id.RedirectToRegister);
        btnLogin = findViewById(R.id.btnRedirectToHome);
        etEmailOrPhoneNumber = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        layoutPassword=findViewById(R.id.layoutPassword);
        layoutEmailOrPhoneNumber=findViewById(R.id.layoutEmail);

    }

    private void registerListeners(){

        redirectToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataEnteredForLogin()){
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        Objects.requireNonNull(imm).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    isValidUser();
                }
            }
        });

    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean checkDataEnteredForLogin() {

        if (isEmpty(etEmailOrPhoneNumber)) {
            layoutEmailOrPhoneNumber.setError("Email or phone number is required");
            layoutEmailOrPhoneNumber.setFocusable(true);
            layoutEmailOrPhoneNumber.requestFocus();
            layoutEmailOrPhoneNumber.setFocusableInTouchMode(true);
            return false;
        }
        else {
            layoutEmailOrPhoneNumber.setError(null);
            layoutEmailOrPhoneNumber.setFocusable(false);
        }

        if (!isEmail(etEmailOrPhoneNumber)) {
            layoutEmailOrPhoneNumber.setError("Enter valid format");
            layoutEmailOrPhoneNumber.setFocusable(true);
            layoutEmailOrPhoneNumber.requestFocus();
            layoutEmailOrPhoneNumber.setFocusableInTouchMode(true);
            return false;
        }
        else {
            layoutEmailOrPhoneNumber.setError(null);
            layoutEmailOrPhoneNumber.setFocusable(false);
        }

        if (isEmpty(etPassword)) {
            layoutPassword.setError("Password is required");
            layoutPassword.setFocusable(true);
            layoutPassword.requestFocus();
            layoutPassword.setFocusableInTouchMode(true);
            return false;
        }
        else {
            layoutPassword.setError(null);
            layoutPassword.setFocusable(false);
        }

        return true;
    }

    private void isValidUser(){
        @SuppressLint("StaticFieldLeak")
        class IsValidUser extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                @SuppressLint("WrongThread") String email=etEmailOrPhoneNumber.getText().toString().trim();
                @SuppressLint("WrongThread") String password=etPassword.getText().toString().trim();
                //adding to database

                    // Get Data
                   count= DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .registerDao()
                            .isValidUser(email,password);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(count==1){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("weightManagement", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("userEmail");
                    editor.commit();
                    editor.putString("userEmail",etEmailOrPhoneNumber.getText().toString().trim() );
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), BMIActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Wrong credentials", Snackbar.LENGTH_LONG).show();
                    etPassword.setText("");
                }

            }
        }

        IsValidUser st = new IsValidUser();
        st.execute();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        etEmailOrPhoneNumber.setText("");
        etPassword.setText("");
    }
}
