package developers.weightmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import developers.weightmanagement.BMI.BMIActivity;
import developers.weightmanagement.Startup.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    LinearLayout splashScreen;
    String userEmail;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        registerSplash();

    }

    private void registerSplash() {

        pref = getApplicationContext().getSharedPreferences("weightManagement", 0); // 0 - for private mode
        userEmail=pref.getString("userEmail", "");

        splashScreen = findViewById(R.id.linearLayout);

        Thread td = new Thread() {
            public void run() {

                try {

                    splashScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            moveToActivity();
                        }
                    });

                    sleep(2000);
                    moveToActivity();

                } catch (Exception ex) {

                    ex.printStackTrace();

                }

            }

        };
        td.start();

    }

    private void moveToActivity(){

        if(!userEmail.equals("none")){
            startActivity(new Intent(SplashActivity.this, BMIActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            SplashActivity.this.finish();
        }
        else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            SplashActivity.this.finish();

        }

    }

}
