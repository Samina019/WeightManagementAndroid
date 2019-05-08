package developers.weightmanagement.Startup;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import developers.weightmanagement.BMI.BMIActivity;
import developers.weightmanagement.R;
import developers.weightmanagement.Room.DatabaseClient;
import developers.weightmanagement.Room.Register;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout redirectToLogin;
    EditText etName,etEmailOrPhoneNumber,etPassword,etConfirmPassword;
    private TextInputLayout layoutName,layoutEmail,layoutPassword,layoutConfirmPassword;
    Button btnRegister;
    Boolean mainFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViews();
        registerListeners();
    }

    private void registerViews() {
        redirectToLogin = findViewById(R.id.RedirectToLogin);
        etName= findViewById(R.id.etName);
        etEmailOrPhoneNumber= findViewById(R.id.etEmail);
        etPassword= findViewById(R.id.etPassword);
        etConfirmPassword= findViewById(R.id.etConfirmPassword);
        layoutName=findViewById(R.id.layoutName);
        layoutEmail=findViewById(R.id.layoutEmail);
        layoutPassword=findViewById(R.id.layoutPassword);
        layoutConfirmPassword=findViewById(R.id.layoutConfirmPassword);
        btnRegister= findViewById(R.id.btnRegister);

    }

    private void registerListeners(){

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataEntered()) {
                    registerUser();
                }
            }
        });

        redirectToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    public boolean validPhoneNumber(EditText number) {
        CharSequence phoneNumber = number.getText().toString();
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }

    boolean checkDataEntered() {

        if (isEmpty(etName)) {
            layoutName.setError("Name is required");
            layoutName.setFocusable(true);
            layoutName.requestFocus();
            layoutName.setFocusableInTouchMode(true);
            return false;
        }
        else {
            layoutName.setError(null);
        }

        if (isEmpty(etEmailOrPhoneNumber)) {
            layoutEmail.setError("Email or phone number is required");
            layoutEmail.setFocusable(true);
            layoutEmail.requestFocus();
            layoutEmail.setFocusableInTouchMode(true);

            return false;
        }
        else {
            layoutEmail.setError(null);
        }

        if (!isEmail(etEmailOrPhoneNumber) ) {
            layoutEmail.setError("Enter valid format");
            layoutEmail.setFocusable(true);
            layoutEmail.requestFocus();
            layoutEmail.setFocusableInTouchMode(true);
            return false;
        }
        else {
            layoutEmail.setError(null);
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
        }

        if ((etPassword.getText().toString()).length() < 8){
            layoutPassword.setError("Password must contain atleast 8 characters");
            layoutPassword.setFocusable(true);
            layoutPassword.requestFocus();
            layoutPassword.setFocusableInTouchMode(true);
            return false;
        }
        else {
            layoutPassword.setError(null);
        }

        if (isEmpty(etConfirmPassword)) {
            layoutConfirmPassword.setError("Confirm password is required");
            layoutConfirmPassword.setFocusable(true);
            layoutConfirmPassword.requestFocus();
            layoutConfirmPassword.setFocusableInTouchMode(true);
            return false;
        }
        else {
            layoutConfirmPassword.setError(null);
        }

        if(!(etPassword.getText().toString()).equals(etConfirmPassword.getText().toString())) {
            layoutConfirmPassword.setError("Password and confirm password does not matches");
            layoutConfirmPassword.setFocusable(true);
            layoutConfirmPassword.requestFocus();
            layoutConfirmPassword.setFocusableInTouchMode(true);
            return false;
        }
        else {
            layoutConfirmPassword.setError(null);
        }

        return true;
    }

    private void registerUser(){

        @SuppressLint("StaticFieldLeak")
        class RegisterUser extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                        Register register = new Register();
                        register.setName(etName.getText().toString().trim());
                        register.setEmail(etEmailOrPhoneNumber.getText().toString().trim());
                        register.setPassword(etPassword.getText().toString().trim());
                        //adding to database

                        try {
                            // Get Data
                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                    .registerDao()
                                    .registerUser(register);
                        }
                        catch (Exception sqlite){

                            mainFlag=true;
                        }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(mainFlag) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "User with this email already registered", Snackbar.LENGTH_LONG).show();
                    etName.setText("");
                    etEmailOrPhoneNumber.setText("");
                    etPassword.setText("");
                    etConfirmPassword.setText("");
                }
                else {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("weightManagement", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("userEmail");
                    editor.commit();
                    editor.putString("userEmail",etEmailOrPhoneNumber.getText().toString().trim() );
                    editor.commit();

                    startActivity(new Intent(getApplicationContext(), BMIActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    RegisterActivity.this.finish();
                }

            }
        }

        RegisterUser st = new RegisterUser();
        st.execute();
    }
}

