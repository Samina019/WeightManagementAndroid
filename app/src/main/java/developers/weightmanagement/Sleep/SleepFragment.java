package developers.weightmanagement.Sleep;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import developers.weightmanagement.R;
import developers.weightmanagement.Room.DatabaseClient;
import developers.weightmanagement.Room.Sleep;
import developers.weightmanagement.SleepAdapterActivity;

public class SleepFragment extends Fragment {

    private BroadcastReceiver mReceiver = null;
    private static final String TAG = "tag";
    public static int whour=0;
    public static int wminute=0;
    public static String wtimepickertime="0";
    public static String alarmTime;
    public static int fallTime = 14; //in minutes
    Context context;
    View rootView;
    Boolean mainFlag=false;
    TextView tvSetWakeTime;
    ImageButton history;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sleep, container, false);

        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        clickListener();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("alarmTime", alarmTime);
        savedInstanceState.putInt("fallTime", fallTime);
        savedInstanceState.putString("wakeTime", wtimepickertime);
        savedInstanceState.putInt("whour", whour);
        savedInstanceState.putInt("wminute", wminute);
    }

    public void setDefaultAlarm(int hour, int minute){
        // Setting an alarm on the Alarm Clock app with time
        // Note: can only set times in next 24 hours
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "Smart Alarm");
        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        //i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(i);
        Log.d(TAG, "AlarmClock set for (h:m): " + hour + ":" + minute);
    }

    public void showWakeTimePickerDialog() {
        Log.d(TAG,"show timepickerdialog function called");
        DialogFragment newFragment = new TimePickerFragment();
        FragmentManager fm = getActivity().getFragmentManager();
        newFragment.show(fm, "timePicker");

        //creating a new bundle that includes dialogType
        Bundle args = new Bundle();
        args.putInt("dialogType", 2);
        newFragment.setArguments(args);
        Log.d(TAG, "end of showTimePickerDialog");
    }

    public void clickListener(){
        tvSetWakeTime=rootView.findViewById(R.id.textView_wakeTime);
        Button btn2=rootView.findViewById(R.id.button_latestWakeUp);
        Button btn3=rootView.findViewById(R.id.button_setAlarm);
        Button save=rootView.findViewById(R.id.button_save);
        history=rootView.findViewById(R.id.log_button);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogActivity();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWakeTimePickerDialog();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvSetWakeTime.getText().toString().equals("No time set!")){
                    Toast.makeText(context,"Set wake time.",Toast.LENGTH_LONG).show();
                }else{
                    setDefaultAlarm(whour, wminute);

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvSetWakeTime.getText().toString().equals("No time set!")){
                    Toast.makeText(context,"Set wake time.",Toast.LENGTH_LONG).show();
                }else{

                    SharedPreferences pref = Objects.requireNonNull(getActivity()).getSharedPreferences("weightManagement", 0); // 0 - for private mode
                    String userEmail=pref.getString("userEmail", "");


                    @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("h:mma");
                    String bedTime = dateFormat.format(new Date()).toString().toLowerCase();

                    String wakeTime=wtimepickertime;

                    saveData(userEmail,date,bedTime,wakeTime);

                }
            }
        });
    }

    private void goToLogActivity() {
        Intent intent = new Intent(context, SleepAdapterActivity.class);
        startActivity(intent);
    }

    private void saveData(final String email, final String date, final String bedTime, final String wakeTime){

        @SuppressLint("StaticFieldLeak")
        class RegisterSleep extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Sleep sleep = new Sleep();
                sleep.setEmail(email);
                sleep.setDate(date);
                sleep.setBedTime(bedTime);
                sleep.setWakeTime(wakeTime);
                //adding to database

                try {
                    // Get Data
                    DatabaseClient.getInstance(context).getAppDatabase()
                        .sleepDao()
                        .registerSleep(sleep);
                }
                catch (Exception sqlite){

                    mainFlag=true;
                }

                return null;
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(mainFlag) {
                    Toast.makeText(context,"There is some problem",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();
                    tvSetWakeTime.setText("No time set!");

                }

            }
        }

        RegisterSleep st = new RegisterSleep();
        st.execute();

    }

}
