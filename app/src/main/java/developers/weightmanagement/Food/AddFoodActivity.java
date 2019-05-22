package developers.weightmanagement.Food;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import developers.weightmanagement.R;
import developers.weightmanagement.Room.DatabaseClient;
import developers.weightmanagement.Room.Food;

public class AddFoodActivity extends AppCompatActivity {

    String mealType;
    Bundle extras;
    EditText etFoodName,etCalories,etCarbs,etFats,etProteins;
    Boolean mainFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        initViews();
        loadData();

    }

    public void initViews(){
        extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        // get data via the key
        mealType= extras.getString("mealType","");

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        setTitle(mealType);

        etFoodName=findViewById(R.id.etFoodName);
        etCalories=findViewById(R.id.etCalories);
        etCarbs=findViewById(R.id.etCarbs);
        etFats=findViewById(R.id.etFats);
        etProteins=findViewById(R.id.etProteins);
    }

    private void loadData(){
        etCalories.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!etCalories.getText().toString().equals("")){
                    double totalCal= Double.parseDouble(etCalories.getText().toString());
                    double fat=totalCal/9;
                    double carb=totalCal/4;
                    double proteins=totalCal/4;

                    etCarbs.setText(String.valueOf(carb));
                    etFats.setText(String.valueOf(fat));
                    etProteins.setText(String.valueOf(proteins));
                }else {
                    etCarbs.setText("0");
                    etProteins.setText("0");
                    etFats.setText("0");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save:

                if(isValid()){

                    String userEmail,date,foodName,calories,carb,fats,proteins;

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("weightManagement", 0); // 0 - for private mode
                    userEmail=pref.getString("userEmail", "");

                    @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                     date = df.format(Calendar.getInstance().getTime());

                    pref = getApplicationContext().getSharedPreferences("weightManagement", 0); // 0 - for private mode
                    userEmail=pref.getString("userEmail", "");
                      saveData(date,userEmail,mealType,etFoodName.getText().toString(),
                          etCalories.getText().toString(),etCarbs.getText().toString(),
                          etFats.getText().toString(),etProteins.getText().toString());


                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private boolean isValid(){

        if (isEmpty(etFoodName)) {
            etFoodName.setError("Enter Food Name");
            etFoodName.setFocusable(true);
            etFoodName.setFocusableInTouchMode(true);
            etFoodName.requestFocus();
            return false;
        }
        else {
            etFoodName.setError(null);
        }

        if (isEmpty(etCalories)) {
            etCalories.setError("Enter total calories");
            etCalories.setFocusable(true);
            etCalories.setFocusableInTouchMode(true);
            etCalories.requestFocus();
            return false;
        }
        else {
            etCalories.setError(null);
        }


        return true;
    }

    private void saveData(final String date, final String userEmail, final String mealType, final String foodName, final String cals, final String carbs, final String fats, final String proteins){

        @SuppressLint("StaticFieldLeak")
        class RegisterFood extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Food food = new Food();
                food.setEmail(userEmail);
                food.setDate(date);
                food.setMealType(mealType);
                food.setFoodName(foodName);
                food.setCalories(cals);
                food.setCarbohydrates(carbs);
                food.setFats(fats);
                food.setProteins(proteins);

                //adding to database

                try {
                    // Get Data
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .foodDao()
                        .registerFood(food);
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
                    Snackbar.make(getWindow().getDecorView().getRootView(), "There is some problem", Snackbar.LENGTH_LONG).show();

                }
                else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Saved", Snackbar.LENGTH_LONG).show();
                    etFoodName.setText("");
                    etCalories.setText("");
                    etCarbs.setText("");
                    etFats.setText("");
                    etProteins.setText("");
                    etFoodName.requestFocus();
                }

            }
        }

        RegisterFood st = new RegisterFood();
        st.execute();

    }

}
