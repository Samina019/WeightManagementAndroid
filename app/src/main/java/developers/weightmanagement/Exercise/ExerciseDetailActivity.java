package developers.weightmanagement.Exercise;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import developers.weightmanagement.R;
import developers.weightmanagement.Room.DatabaseClient;
import developers.weightmanagement.Room.Exercise;

public class ExerciseDetailActivity extends AppCompatActivity {

    TextView exerciseName,calories;
    EditText amount,weight;
    ImageView image;
    Spinner spinner;
    String type;
    Boolean mainFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        registerViews();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        // get data via the key
        String exerciseType = extras.getString("exerciseType","");
        spreadSninner(exerciseType);

    }

    private void registerViews(){
        exerciseName=findViewById(R.id.tvExerciseName);
        image=findViewById(R.id.ivImage);
        spinner=findViewById(R.id.spinner);
        calories=findViewById(R.id.tvCals);
        amount=findViewById(R.id.etAmount);
        weight=findViewById(R.id.etWeight);
    }

    private void spreadSninner(String exerciseType){
        switch (exerciseType){
            case "Walking":
                exerciseName.setText(exerciseType);
                image.setImageResource(R.drawable.walkk);

                List<String> spinnerArray = new ArrayList<>();
                spinnerArray.add("2 mph, level slow pace, firm surface");
                spinnerArray.add("2.5 mph, firm surface");
                spinnerArray.add("3 mph, level, moderate pace, firm surface");
                spinnerArray.add("3.5 – 4 mph, level, brisk, firm surface");
                spinnerArray.add("4.5 mph, level, firm surface, very very brisk");
                spinnerArray.add("racewalking");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    ExerciseDetailActivity.this, android.R.layout.simple_spinner_item, spinnerArray
                );
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        Object item = parent.getItemAtPosition(position);
                        String type=item.toString();
                        final double METS=findMETS(type);
                        if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                            double w = Double.parseDouble(weight.getText().toString());
                            double energy=0.0175 * METS * w;
                            double totalAmount = Double.parseDouble(amount.getText().toString());
                            Double totalCalories = (Double) (totalAmount * energy);
                            calories.setText(String.valueOf(Math.ceil(totalCalories)));
                        }
                        else {
                            calories.setText("0");
                        }

                        amount.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                                    double w = Double.parseDouble(weight.getText().toString());
                                    double energy=0.0175 * METS * w;
                                    double totalAmount = Double.parseDouble(amount.getText().toString());
                                    Double totalCalories = (Double) (totalAmount * energy);
                                    calories.setText(String.valueOf(Math.ceil(totalCalories)));
                                }
                                else {
                                    calories.setText("0");
                                }
                            }
                        });

                        weight.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                                    double w = Double.parseDouble(weight.getText().toString());
                                    double energy=0.0175 * METS * w;
                                    double totalAmount = Double.parseDouble(amount.getText().toString());
                                    Double totalCalories = (Double) (totalAmount * energy);
                                    calories.setText(String.valueOf(Math.ceil(totalCalories)));
                                }
                                else {
                                    calories.setText("0");
                                }
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });

                break;

            case "Running":
                exerciseName.setText(exerciseType);
                image.setImageResource(R.drawable.run);

                List<String> spinnerArray2 = new ArrayList<>();
                spinnerArray2.add("5 mph (12 min mile)");
                spinnerArray2.add("5.2 mph (11.5 min mile)");
                spinnerArray2.add("6 mph (10 min mile)");
                spinnerArray2.add("6.7 mph (9 min mile)");
                spinnerArray2.add("7 mph (8.5 min mile)");
                spinnerArray2.add("7.5 mph (8 min mile)");
                spinnerArray2.add("8 mph (7.5 min mile)");
                spinnerArray2.add("8.6 mph (7 min mile)");
                spinnerArray2.add("9 mph (6.5 min mile)");
                spinnerArray2.add("10 mph (6 min mile)");
                spinnerArray2.add("10.9 mph (5.5 min mile)");
                spinnerArray2.add("Running stairs");
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                    ExerciseDetailActivity.this, android.R.layout.simple_spinner_item, spinnerArray2
                );
                spinner.setAdapter(adapter2);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        Object item = parent.getItemAtPosition(position);
                        String type=item.toString();
                        final double METS=findMETS(type);
                        if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                            double w = Double.parseDouble(weight.getText().toString());
                            double energy=0.0175 * METS * w;
                            double totalAmount = Double.parseDouble(amount.getText().toString());
                            Double totalCalories = (Double) (totalAmount * energy);
                            calories.setText(String.valueOf(Math.ceil(totalCalories)));
                        }
                        else {
                            calories.setText("0");
                        }

                        amount.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                                    double w = Double.parseDouble(weight.getText().toString());
                                    double energy=0.0175 * METS * w;
                                    double totalAmount = Double.parseDouble(amount.getText().toString());
                                    Double totalCalories = (Double) (totalAmount * energy);
                                    calories.setText(String.valueOf(Math.ceil(totalCalories)));
                                }
                                else {
                                    calories.setText("0");
                                }
                            }
                        });

                        weight.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                                    double w = Double.parseDouble(weight.getText().toString());
                                    double energy=0.0175 * METS * w;
                                    double totalAmount = Double.parseDouble(amount.getText().toString());
                                    Double totalCalories = (Double) (totalAmount * energy);
                                    calories.setText(String.valueOf(Math.ceil(totalCalories)));
                                }
                                else {
                                    calories.setText("0");
                                }
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });

                break;

            case "Cycling":
                exerciseName.setText(exerciseType);
                image.setImageResource(R.drawable.bicycle);

                List<String> spinnerArray3 = new ArrayList<>();
                spinnerArray3.add("50 watts, very light effort");
                spinnerArray3.add("100 watts, light effort");
                spinnerArray3.add("150 watts, moderate effort");
                spinnerArray3.add("200 watts, vigorous effort");
                spinnerArray3.add("250 watts, very vigorous effort");
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                    ExerciseDetailActivity.this, android.R.layout.simple_spinner_item, spinnerArray3
                );
                spinner.setAdapter(adapter3);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        Object item = parent.getItemAtPosition(position);
                        String type=item.toString();
                        final double METS=findMETS(type);
                        if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                            double w = Double.parseDouble(weight.getText().toString());
                            double energy=0.0175 * METS * w;
                            double totalAmount = Double.parseDouble(amount.getText().toString());
                            Double totalCalories = (Double) (totalAmount * energy);
                            calories.setText(String.valueOf(Math.ceil(totalCalories)));
                        }
                        else {
                            calories.setText("0");
                        }

                        amount.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                                    double w = Double.parseDouble(weight.getText().toString());
                                    double energy=0.0175 * METS * w;
                                    double totalAmount = Double.parseDouble(amount.getText().toString());
                                    Double totalCalories = (Double) (totalAmount * energy);
                                    calories.setText(String.valueOf(Math.ceil(totalCalories)));
                                }
                                else {
                                    calories.setText("0");
                                }
                            }
                        });

                        weight.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                if(!amount.getText().toString().equals("") && !weight.getText().toString().equals("")) {
                                    double w = Double.parseDouble(weight.getText().toString());
                                    double energy=0.0175 * METS * w;
                                    double totalAmount = Double.parseDouble(amount.getText().toString());
                                    Double totalCalories = (Double) (totalAmount * energy);
                                    calories.setText(String.valueOf(Math.ceil(totalCalories)));
                                }
                                else {
                                    calories.setText("0");
                                }
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });

                break;

            default:
                break;
        }
    }

    private double findMETS(String exerciseType){
        double mets=0;
        switch (exerciseType){
            case "2 mph, level slow pace, firm surface":
                mets=2.5;
                break;
            case "2.5 mph, firm surface":
                mets=3.0;
                break;
            case "3 mph, level, moderate pace, firm surface":
                mets=3.5;
                break;
            case "3.5 – 4 mph, level, brisk, firm surface":
                mets=4.0;
                break;
            case "4.5 mph, level, firm surface, very very brisk":
                mets=4.5;
                break;
            case "racewalking":
                mets=6.5;
                break;
            case "5 mph (12 min mile)":
                mets=8.0;
                break;
            case "5.2 mph (11.5 min mile)":
                mets=9.0;
                break;
            case "6 mph (10 min mile)":
                mets=10.0;
                break;
            case "6.7 mph (9 min mile)":
                mets=11.0;
                break;
            case "7 mph (8.5 min mile)":
                mets=11.5;
                break;
            case "7.5 mph (8 min mile)":
                mets=12.5;
                break;
            case "8 mph (7.5 min mile)":
                mets=13.5;
                break;
            case "8.6 mph (7 min mile)":
                mets=14.0;
                break;
            case "9 mph (6.5 min mile)":
                mets=15.0;
                break;
            case "10 mph (6 min mile)":
                mets=16.0;
                break;
            case "10.9 mph (5.5 min mile)":
                mets=18.0;
                break;
            case "Running stairs":
                mets=15.0;
                break;
            case "50 watts, very light effort":
                mets=3.0;
                break;
            case "100 watts, light effort":
                mets=5.5;
                break;
            case "150 watts, moderate effort":
                mets=7.0;
                break;
            case "200 watts, vigorous effort":
                mets=10.5;
                break;
            case "250 watts, very vigorous effort":
                mets=12.5;
                break;
            default:
                break;

        }
        type=exerciseType;
        return mets;
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
                    String userEmail,name,exerciseType,userWeight,time,cals;

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("weightManagement", 0); // 0 - for private mode
                    userEmail=pref.getString("userEmail", "");
                    name=exerciseName.getText().toString();
                    exerciseType=type;
                    userWeight=weight.getText().toString();
                    time=amount.getText().toString() + " min";
                    cals=calories.getText().toString();

                    @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());

                    saveData(date,userEmail,name,exerciseType,userWeight,time,cals);

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

        if (isEmpty(weight)) {
            weight.setError("Enter Weight");
            weight.setFocusable(true);
            weight.setFocusableInTouchMode(true);
            weight.requestFocus();
            return false;
        }
        else {
            weight.setError(null);
        }

        if (isEmpty(amount)) {
            amount.setError("Enter exercise time");
            amount.setFocusable(true);
            amount.setFocusableInTouchMode(true);
            amount.requestFocus();
            return false;
        }
        else {
            amount.setError(null);
        }


        return true;
    }

    private void saveData(final String date,final String userEmail, final String name, final String exerciseType, final String userWeight, final String time, final String userCaloriies){

        @SuppressLint("StaticFieldLeak")
        class RegisterExercise extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Exercise exercise = new Exercise();
                exercise.setEmail(userEmail);
                exercise.setExercise_name(name);
                exercise.setExercise_type(exerciseType);
                exercise.setWeight(userWeight);
                exercise.setTime(time);
                exercise.setCalories(userCaloriies);
                exercise.setDate(date);

                //adding to database

                try {
                    // Get Data
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .exerciseDao()
                        .registerExercise(exercise);
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
                    weight.setText("");
                    amount.setText("");
                    calories.setText("0");
                    weight.requestFocus();

                }

            }
        }

        RegisterExercise st = new RegisterExercise();
        st.execute();

    }
}
