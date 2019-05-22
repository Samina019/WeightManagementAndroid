package developers.weightmanagement.BMI;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import developers.weightmanagement.R;
import developers.weightmanagement.Program.WeightManagementActivity;
import developers.weightmanagement.Startup.LoginActivity;

public class BMIActivity extends AppCompatActivity {

    int age2 = 0, age3 = 0, height2 = 0, height3 = 0, weight2 = 0, weight3 = 0, gender = -1, t1 = 0, t2 = 0, t3 = 0, t4 = 0;
    float temp1 = 0, temp2 = 0, temp3 = 0;
    String age1 = "", height1 = "", weight1 = "";

    // For the bmi window
    EditText age, height, weight;

    // For the Unit Converter Window
    Spinner conversion_type_spinner, from_spinner, to_spinner;
    String from_spinner_string, to_spinner_string, choice_spinner;
    int from_spinner_position, to_spinner_position, choice_spinner_pos;
    ArrayAdapter<CharSequence> adapter_area, adapter_volume, adapter_digital_storage, adapter_energy,           // Create All the array adapter for different type of conversions
        adapter_frequency, adapter_mass, adapter_length, adapter_plane_angle, adapter_pressure, adapter_speed, adapter_temperature, adapter_time;
    EditText editText_from;
    TextView textView_to;
    BigDecimal[][] array = {{new BigDecimal("1"), new BigDecimal("1000000.0"), new BigDecimal("1195990.05"), new BigDecimal("10763910.4"), new BigDecimal("1550000000"), new BigDecimal("0.386102"), new BigDecimal("100"), new BigDecimal("247.105")},
        {new BigDecimal("1"), new BigDecimal("0.125"), new BigDecimal("0.000122073125"), new BigDecimal("0.00000011920992"), new BigDecimal("0.0000000001164153"), new BigDecimal("0.0000000000001136868377"), new BigDecimal("0.000000000000001110223024")},
        {new BigDecimal("1"), new BigDecimal("0.001"), new BigDecimal("0.000239006"), new BigDecimal("0.00000027778"), new BigDecimal("0.737562")},
        {new BigDecimal("1"), new BigDecimal("0.001"), new BigDecimal("0.000001"), new BigDecimal("0.000000001")},
        {new BigDecimal("1"), new BigDecimal("1000"), new BigDecimal("1000000"), new BigDecimal("1000000000"), new BigDecimal("1000000000000"), new BigDecimal("157.473"), new BigDecimal("2204.62"), new BigDecimal("35274")},
        {new BigDecimal("1"), new BigDecimal("1000"), new BigDecimal("100000"), new BigDecimal("10000000000"), new BigDecimal("10000000000000"), new BigDecimal("0.621371"), new BigDecimal("1093.61"), new BigDecimal("3280.84"), new BigDecimal("39370.1"), new BigDecimal("0.539953")},
        {new BigDecimal("1"), new BigDecimal("1.1111111300619"), new BigDecimal("0.0174533"), new BigDecimal("17.453300")},
        {new BigDecimal("1"), new BigDecimal("1.01325"), new BigDecimal("101325"), new BigDecimal("760")},
        {new BigDecimal("1"), new BigDecimal("1.46667"), new BigDecimal("0.44704"), new BigDecimal("1.60934"), new BigDecimal("0.868976")},
        {},  // keep blank for temperature
        {new BigDecimal("1"), new BigDecimal("0.001"), new BigDecimal("0.000016667"), new BigDecimal("0.00000027778"), new BigDecimal("0.000000011574"), new BigDecimal("0.0000000016534"), new BigDecimal("0.00000000038052"), new BigDecimal("0.00000000003171"), new BigDecimal("0.000000000003171"), new BigDecimal("0.0000000000003171")},
        {new BigDecimal("1"), new BigDecimal("1000"), new BigDecimal("0.219969"), new BigDecimal("0.879877"), new BigDecimal("3.51951"), new BigDecimal("0.0353147"), new BigDecimal("61.0237"), new BigDecimal("61023.7")}};

    //   For the unit converter
    String val = "0";
    //Used to add some time so that user cannot directly press and exity out of the activity
    boolean doubleBackToExitPressedOnce = false;

    //For the BMI Window
    private RadioGroup rg;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_bmi:
                    setTitle(" BMI Calculator");    // To change the title of the action bar
                    change_layout(0);
                    //bmi_calculator();
                    return true;

                case R.id.navigation_unit:
                    setTitle(" Unit Converter");    // To change the title of the action bar
                    // To change the color of the action Bar
                    change_layout(1);
                    unit_converter();

                    return true;

            }
            return false;
        }

    };

    //This method takes a big decimal number and convert that to the exponent from and scale is mantissa
    private static String format(BigDecimal x, int scale) {
        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initially set the BMI the main homepage
        setTitle("BMI Calculator");
        // To change the color of the action Bar

        setContentView(R.layout.activity_bmi);
        rg = findViewById(R.id.rg);

        //To remove the focus from the activity when the activity starts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void change_layout(int selectedItem) {
        // get your outer Frame layout
        FrameLayout frameLayout = findViewById(R.id.content);
        View view = null;
        if (selectedItem == 0) {
            //Add the content bmi layout to Inflator
            view = getLayoutInflater().inflate(R.layout.content_bmi, frameLayout, false);
        }
        if (selectedItem == 1) {
            //Add the content bmi layout to Inflator
            view = getLayoutInflater().inflate(R.layout.content_unit, frameLayout, false);
        }
        frameLayout.removeAllViews();
        frameLayout.addView(view);
    }

    private void initialise_adapters() {
        // Create All the array adapter for different type of conversions
        adapter_area = ArrayAdapter.createFromResource(getApplication(), R.array.area, android.R.layout.simple_spinner_item);
        adapter_digital_storage = ArrayAdapter.createFromResource(getApplication(), R.array.digital_storage, android.R.layout.simple_spinner_item);
        adapter_energy = ArrayAdapter.createFromResource(getApplication(), R.array.energy, android.R.layout.simple_spinner_item);
        adapter_frequency = ArrayAdapter.createFromResource(getApplication(), R.array.frequency, android.R.layout.simple_spinner_item);
        adapter_mass = ArrayAdapter.createFromResource(getApplication(), R.array.mass, android.R.layout.simple_spinner_item);
        adapter_length = ArrayAdapter.createFromResource(getApplication(), R.array.length, android.R.layout.simple_spinner_item);
        adapter_plane_angle = ArrayAdapter.createFromResource(getApplication(), R.array.phase_angle, android.R.layout.simple_spinner_item);
        adapter_pressure = ArrayAdapter.createFromResource(getApplication(), R.array.pressure, android.R.layout.simple_spinner_item);
        adapter_speed = ArrayAdapter.createFromResource(getApplication(), R.array.speed, android.R.layout.simple_spinner_item);
        adapter_temperature = ArrayAdapter.createFromResource(getApplication(), R.array.temperature, android.R.layout.simple_spinner_item);
        adapter_time = ArrayAdapter.createFromResource(getApplication(), R.array.time, android.R.layout.simple_spinner_item);
        adapter_volume = ArrayAdapter.createFromResource(getApplication(), R.array.volume, android.R.layout.simple_spinner_item);

    }

    // Main method of the unit converter
    private void unit_converter() {
        // Initialise the main spinner
        conversion_type_spinner = findViewById(R.id.conversion_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplication(), R.array.conversion_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        conversion_type_spinner.setAdapter(adapter);
        conversion_type_spinner.setOnItemSelectedListener(new spinner());

        from_spinner = findViewById(R.id.from_spinner);
        to_spinner = findViewById(R.id.to_spinner);

        initialise_adapters();

        //initally set default to  area adapter
        adapter_area.setDropDownViewResource(R.layout.spinner_dropdown_item);
        from_spinner.setAdapter(adapter_area);
        to_spinner.setAdapter(adapter_area);
        from_spinner_string = "Square kilometer";
        to_spinner_string = "Square kilometer";
        choice_spinner = "Area";

        //Default values
        from_spinner_position = 0;
        to_spinner_position = 0;
        choice_spinner_pos = 0;

        from_spinner.setOnItemSelectedListener(new spinner());
        to_spinner.setOnItemSelectedListener(new spinner());


        editText_from = findViewById(R.id.from_edit);
        textView_to = findViewById(R.id.to_text);
        editText_from.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                val = s.toString();
                int flag = 0;
                if (val.equals("")) {
                    flag = 1;
                    val = "0";
                }
                if (val.equals("-") || val.equals(".")) flag = 1;
                if (choice_spinner.equals("Temperature")) {
                    if (flag == 0) {
                        double celcius = Double.parseDouble(val);
                        if (from_spinner_position == 2) {
                            celcius = celcius - Double.parseDouble("32");
                            celcius = celcius * Double.parseDouble("0.55555");
                        } else if (from_spinner_position == 1)
                            celcius = celcius - Double.parseDouble("273.15");

                        if (to_spinner_position == 2) {
                            celcius = celcius * Double.parseDouble("1.8");
                            celcius = celcius + Double.parseDouble("32");
                        } else if (to_spinner_position == 1)
                            celcius = celcius + Double.parseDouble("273.15");
                        print_exponent(textView_to, celcius);
                    } else {
                        textView_to.setText("");
                    }

                } else {
                    if (flag == 0) {
                        double temp = Double.parseDouble(val);
                        temp = temp / Double.parseDouble(array[choice_spinner_pos][from_spinner_position].toString());
                        temp = temp * Double.parseDouble(array[choice_spinner_pos][to_spinner_position].toString());
                        temp = temp / Double.parseDouble(array[choice_spinner_pos][0].toString());
                        print_exponent(textView_to, temp);
                    } else {
                        textView_to.setText("");
                    }
                }
            }
        });

    }

    //Method to print the numbers in exponent form
    public void print_exponent(TextView view, double temp) {
        String temp1 = "" + temp;
        String temp2 = "";
        int bds = 0;
        for (char c : temp1.toCharArray()) {
            if (c == 'E') {
                temp2 += " e ";
                bds = 1;
            } else {
                if (bds == 1) {
                    if (c == '-')
                        temp2 += c;
                    else
                        temp2 = temp2 + "+" + c;
                } else
                    temp2 += c;
                bds = 0;
            }
        }
        view.setText(temp2);
    }

    public void clear_unit(View view) {
        editText_from.setText("");
        textView_to.setText("");
        val = "";
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);
    }

    public void check(View view) {
        int checkedId = rg.getCheckedRadioButtonId();
        if (R.id.rb1 == checkedId) {
            gender = 0;
        } else if (R.id.rb2 == checkedId) {
            gender = 1;
        }
    }


    //Interest calculator

    private float bmiCalculator(int a, int b) {
        if (b == 0 || a == 0) {
            Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double temp = (b * 10000.0) / (a * a * 1.0);
        float f = (float) temp;
        return f;
    }

    //take age from user
    private int ageit() {
        t1 = 1;
        age = findViewById(R.id.age);
        age1 = age.getText().toString();
        try {
            age2 = Integer.parseInt(age1);
        } catch (NumberFormatException e) {
            // Toast.makeText(this,"Only Digits Allowed",Toast.LENGTH_SHORT).show();
        }
        return age2;
    }

    //Take height from user
    private int heightit() {
        t2 = 1;
        height = findViewById(R.id.height);
        height1 = height.getText().toString();
        try {
            height2 = Integer.parseInt(height1);
        } catch (NumberFormatException e) {
            // Toast.makeText(this,"Only Digits Allowed",Toast.LENGTH_SHORT).show();
        }
        return height2;
    }

    //take weight from user
    private int weightit() {
        t3 = 1;
        weight = findViewById(R.id.weight);
        weight1 = weight.getText().toString();
        try {
            weight2 = Integer.parseInt(weight1);
        } catch (NumberFormatException e) {
            //Toast.makeText(this,"Only Digits Allowed",Toast.LENGTH_SHORT).show();
        }
        return weight2;
    }

    @SuppressLint("SetTextI18n")
    private void showdatamen(float f, int a) {
        TextView under = findViewById(R.id.underweight);
        TextView normal = findViewById(R.id.normal);
        TextView over = findViewById(R.id.overweight);
        TextView obese = findViewById(R.id.obese);
        TextView morobese = findViewById(R.id.morobese);
        TextView tv = findViewById(R.id.result);
        if (a < 10) {
            under.setText("< 14.6");
            normal.setText("14.6 - 21.3");
            over.setText("21.3 - 25.0");
            obese.setText("> 25.0");
            morobese.setText("-");
            if (f == 0) {
                tv.setText("--");
                tv.setBackgroundColor(Color.parseColor("#000000"));

            } else if (f < 14.6) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#48acda"));
            } else if (f >= 14.6 && f < 21.3) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#42cd3a"));
            } else if (f >= 21.3 && f < 25.0) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#F30BDE"));
            } else if (f >= 25.0) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff9900"));
            }
        } else if (a >= 10 && a < 15) {
            under.setText("< 16.7");
            normal.setText("16.7 - 22.5");
            over.setText("22.5 - 25.6");
            obese.setText("> 25.6");
            morobese.setText("-");
            if (f == 0) {
                tv.setText("--");
                tv.setBackgroundColor(Color.parseColor("#000000"));

            } else if (f < 16.7) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#48acda"));
            } else if (f >= 16.7 && f < 22.5) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#42cd3a"));
            } else if (f >= 22.5 && f < 25.6) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#F30BDE"));
            } else if (f >= 25.6) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff9900"));
            }
        } else if (a >= 15 && a < 20) {
            under.setText("< 18.6");
            normal.setText("18.6 - 23.9");
            over.setText("23.9 -26.7");
            obese.setText("> 26.7");
            morobese.setText("-");
            if (f == 0) {
                tv.setText("--");
                tv.setBackgroundColor(Color.parseColor("#000000"));

            } else if (f < 18.6) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#48acda"));
            } else if (f >= 18.6 && f < 23.9) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#42cd3a"));
            } else if (f >= 23.9 && f < 26.7) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#F30BDE"));
            } else if (f >= 26.7) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff9900"));
            }
        } else if (a >= 20) {
            under.setText("< 20");
            normal.setText("20 - 25");
            over.setText("25 - 30");
            obese.setText("30 - 40");
            morobese.setText("> 40");
            if (f == 0) {
                tv.setText("--");
                tv.setBackgroundColor(Color.parseColor("#000000"));

            } else if (f < 20) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#48acda"));
            } else if (f >= 20 && f < 25) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#42cd3a"));
            } else if (f >= 25 && f < 30) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#F30BDE"));
            } else if (f >= 30 && f < 40) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff9900"));
            } else {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff0400"));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showdatawomen(float f, int a) {
        TextView under = findViewById(R.id.underweight);
        TextView normal = findViewById(R.id.normal);
        TextView over = findViewById(R.id.overweight);
        TextView obese = findViewById(R.id.obese);
        TextView morobese = findViewById(R.id.morobese);
        TextView tv = findViewById(R.id.result);
        if (a < 10) {
            under.setText("< 14.2");
            normal.setText("14.2 - 20.6");
            over.setText("20.6 - 23.3");
            obese.setText("> 23.3");
            morobese.setText("-");
            if (f == 0) {
                tv.setText("--");
                tv.setBackgroundColor(Color.parseColor("#000000"));

            } else if (f < 14.2) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#48acda"));
            } else if (f >= 14.2 && f < 20.6) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#42cd3a"));
            } else if (f >= 20.6 && f < 23.3) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#F30BDE"));
            } else if (f >= 23.3) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff9900"));
            }
        } else if (a >= 10 && a < 15) {
            under.setText("< 15");
            normal.setText("15 - 21.4");
            over.setText("21.4 - 23.3");
            obese.setText("> 23.3");
            morobese.setText("-");
            if (f == 0) {
                tv.setText("--");
                tv.setBackgroundColor(Color.parseColor("#000000"));

            } else if (f < 15) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#48acda"));
            } else if (f >= 15 && f < 21.4) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#42cd3a"));
            } else if (f >= 21.4 && f < 23.3) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#F30BDE"));
            } else if (f >= 23.3) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff9900"));
            }
        } else if (a >= 15 && a < 20) {
            under.setText("< 17.8");
            normal.setText("17.8 - 23.3");
            over.setText("23.3 - 25.6");
            obese.setText("> 25.6");
            morobese.setText("-");
            if (f == 0) {
                tv.setText("--");
                tv.setBackgroundColor(Color.parseColor("#000000"));

            } else if (f < 17.8) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#48acda"));
            } else if (f >= 17.8 && f < 23.3) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#42cd3a"));
            } else if (f >= 23.3 && f < 25.6) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#F30BDE"));
            } else if (f >= 25.6) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff9900"));
            }
        } else if (a >= 20) {
            under.setText("< 19");
            normal.setText("19 - 24");
            over.setText("24 - 30");
            obese.setText("30 - 40");
            morobese.setText("> 40");
            if (f == 0) {
                tv.setText("--");
                tv.setBackgroundColor(Color.parseColor("#000000"));

            } else if (f < 19) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#48acda"));
            } else if (f >= 19 && f < 24) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#42cd3a"));
            } else if (f >= 24 && f < 30) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#F30BDE"));
            } else if (f >= 30 && f < 40) {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff9900"));
            } else {
                tv.setText("" + f);
                tv.setBackgroundColor(Color.parseColor("#ff0400"));
            }
        }
    }

    public void result(View view) {
        Button button = findViewById(R.id.res);
        age3 = ageit();
        height3 = heightit();
        weight3 = weightit();
        if ((gender == 0 || gender == 1) && t1 == 1 && t2 == 1 && t3 == 1) {
            temp1 = bmiCalculator(height3, weight3);
            t4 = 1;
            if (gender == 0)
                showdatamen(temp1, age3);
            else
                showdatawomen(temp1, age3);
        } else if (t1 == 0 || t2 == 0 || t3 == 0) {
            Toast.makeText(this, "Choose the Required Fields", Toast.LENGTH_SHORT).show();
            age3 = ageit();
            height3 = heightit();
            weight3 = weightit();
        } else {
            Toast.makeText(this, "Choose Gender", Toast.LENGTH_SHORT).show();
        }
    }

    public void reset(View view) {
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(600);
        Button button = findViewById(R.id.reset);
        button.startAnimation(animation);
        if (t4 != 0) {
            TextView under = findViewById(R.id.underweight);
            TextView normal = findViewById(R.id.normal);
            TextView over = findViewById(R.id.overweight);
            TextView obese = findViewById(R.id.obese);
            TextView morobese = findViewById(R.id.morobese);
            age2 = 0;
            age3 = 0;
            height2 = 0;
            height3 = 0;
            weight2 = 0;
            weight3 = 0;
            gender = -1;
            t1 = 0;
            t4 = 0;
            t2 = 0;
            t3 = 0;
            temp1 = 0;
            temp2 = 0;
            temp3 = 0;
            age1 = "";
            height1 = "";
            weight1 = "";
            TextView t = findViewById(R.id.result);
            t.setText("0.0");
            t.setBackgroundColor(Color.parseColor("#FFCDCDC6"));
            rg.clearCheck();
            under.setText("-");
            normal.setText("-");
            over.setText("-");
            obese.setText("-");
            morobese.setText("-");
            height.setText("");
            weight.setText("");
            age.setText("");
        }
    }

    //Spinner class to select spinner and also add gender
    class spinner implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            if (parent.getId() == R.id.conversion_type) {

                editText_from.getText().clear();
                textView_to.setText("");
                choice_spinner = parent.getItemAtPosition(position).toString();
                choice_spinner_pos = position;
                from_spinner.setEnabled(true);
                to_spinner.setEnabled(true);
                if (choice_spinner.equals("Area") == true) {
                    adapter_area.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_area);

                    to_spinner.setAdapter(adapter_area);
                } else if (choice_spinner.equals("Digital Storage") == true) {
                    adapter_digital_storage.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_digital_storage);
                    to_spinner.setAdapter(adapter_digital_storage);
                } else if (choice_spinner.equals("Energy") == true) {
                    adapter_energy.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_energy);
                    to_spinner.setAdapter(adapter_energy);
                } else if (choice_spinner.equals("Frequency") == true) {
                    adapter_frequency.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_frequency);
                    to_spinner.setAdapter(adapter_frequency);
                } else if (choice_spinner.equals("Mass") == true) {
                    adapter_mass.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_mass);
                    to_spinner.setAdapter(adapter_mass);
                } else if (choice_spinner.equals("Length") == true) {
                    adapter_length.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_length);
                    to_spinner.setAdapter(adapter_length);
                } else if (choice_spinner.equals("Plane Angle") == true) {
                    adapter_plane_angle.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_plane_angle);
                    to_spinner.setAdapter(adapter_plane_angle);
                } else if (choice_spinner.equals("Pressure") == true) {
                    adapter_pressure.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_pressure);
                    to_spinner.setAdapter(adapter_pressure);
                } else if (choice_spinner.equals("Speed") == true) {
                    adapter_speed.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_speed);
                    to_spinner.setAdapter(adapter_speed);
                } else if (choice_spinner.equals("Temperature") == true) {
                    adapter_temperature.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_temperature);
                    to_spinner.setAdapter(adapter_temperature);
                } else if (choice_spinner.equals("Time") == true) {
                    adapter_time.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_time);
                    to_spinner.setAdapter(adapter_time);
                } else if (choice_spinner.equals("Volume") == true) {
                    adapter_volume.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    from_spinner.setAdapter(adapter_volume);
                    to_spinner.setAdapter(adapter_volume);

                }
            } else if (parent.getId() == R.id.from_spinner) {
                editText_from.getText().clear();
                textView_to.setText("");
                from_spinner_string = parent.getItemAtPosition(position).toString();
                from_spinner_position = position;

            } else if (parent.getId() == R.id.to_spinner) {
                editText_from.getText().clear();
                textView_to.setText("");
                to_spinner_string = parent.getItemAtPosition(position).toString();
                to_spinner_position = position;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //When nothing is selected
            Toast.makeText(getApplicationContext(), "Please Select a category", Toast.LENGTH_SHORT).show();
            from_spinner.setEnabled(false);
            to_spinner.setEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        inflater.inflate(R.menu.menu_logout, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.next:

                startActivity(new Intent(BMIActivity.this, WeightManagementActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.logout:

                AlertDialog();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    void AlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BMIActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are you sure you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("weightManagement", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userEmail","none");
                    editor.commit();

                    startActivity(new Intent(BMIActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    BMIActivity.this.finish();
                }


            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
