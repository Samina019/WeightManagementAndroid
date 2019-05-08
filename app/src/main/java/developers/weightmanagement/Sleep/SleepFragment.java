package developers.weightmanagement.Sleep;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import developers.weightmanagement.R;

public class SleepFragment extends Fragment {

    private BroadcastReceiver mReceiver = null;
    private static final String TAG = "tag";
    public static String EXTRA_CYCLES = "com.example.gt.smartsleeper.CYCLES";
    public static int bhour=0;
    public static int bminute=0;
    public static String btimepickertime="0";
    public static int whour=0;
    public static int wminute=0;
    public static String wtimepickertime="0";
    public static TextView bdTime;
    public static TextView wkTime;
    public static String alarmTime;
    public static int fallTime = 14; //in minutes
    Context context;
    View rootView;
    public int numHandlers = 0;
    public Handler handler = new Handler();

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
    public void onStop() {
        super.onStop();
        // when the screen is about to turn off or app will be moved to background
        if (ScreenReceiver.isScreenOn) {
            // this is when onStop() is called due to a screen state change
            Log.e(TAG, "onStop: screen was on");
        } else {
            // this is when onStop() is called when the screen state has not changed (still on)
            Log.e(TAG, "onStop: screen was off");
        }

        if (ScreenReceiver.isScreenOn) {
            // saving time of onStop() call
            final Calendar c = Calendar.getInstance();
            final int bh = c.get(Calendar.HOUR_OF_DAY);
            final int bm = c.get(Calendar.MINUTE);
            //defining the Runnable which Handler will call
            Runnable r = new Runnable() {
                @Override
                public void run() { // Set the alarm clock
                    Log.d(TAG, "handler's delayed run called");
                    // Do after time delay if screen has been turned off
                    if (!ScreenReceiver.isScreenOn) {
                        Log.d(TAG, "Set alarm called from onStop()");
                        //Set global bed time variables to time of last onStop()
                        bhour = bh;
                        bminute = bm;
                        //Set alarm
                        setAlarm();
                        numHandlers = 0;
                        Log.e(TAG, "After time delay, alarm is set and numHandlers = "+numHandlers);
                    } else {
                        //do nothing
                        Log.d(TAG,"handler: Screen was on, so I did nothing");
                        numHandlers = 0;
                    }
                    Log.d(TAG,"numHandlers: "+numHandlers);
                }
            };
            Log.d(TAG,"numHandlers (before check): "+numHandlers);
            if (numHandlers>=1){
                //remove all callbacks from queue
                handler.removeCallbacksAndMessages(null);
                numHandlers=0;
                Log.d(TAG,"old handlers removed, numHandlers="+numHandlers);
                //only the newest Runnable callback will be executed
            }
            Log.d(TAG,"new onStop handler added to task queue. H:"+bh+"M:"+bm);
            numHandlers=1;
            Log.d(TAG,"numHandlers set to: "+numHandlers);
            //Time delay: 60 minutes = 3600000ms
            final int timeDelay = 3600000;
            //Post the Runnable callback to the queue
            handler.postDelayed(r, timeDelay);

        } else {
            Log.d(TAG,"no new handler as screen was off");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("alarmTime", alarmTime);
        savedInstanceState.putInt("fallTime", fallTime);
        savedInstanceState.putString("bedTime", btimepickertime);
        savedInstanceState.putString("wakeTime", wtimepickertime);
        savedInstanceState.putInt("bhour", bhour);
        savedInstanceState.putInt("bminute", bminute);
        savedInstanceState.putInt("whour", whour);
        savedInstanceState.putInt("wminute", wminute);
    }

    public void setPreferences(){
        setAlarm();
    }

    public void setAlarm() {
        // Set Preferences: avg bed time, # of sleep cycles before wake up in response to button click
        Log.d(TAG,"Set Alarm button pressed");

        //calculate what time the alarm should go off
        Date alarm = calculateAlarmTime(bhour, bminute, whour, wminute);
        Log.d(TAG,"Alarm time calculated: "+alarm);

        setCalculatedTimeText(alarm);
        Log.d(TAG,"Calculated time updated");

//trying to create a separate class for the alarm clock functionality
        //boolean alarmSuccess = ManageAlarmClock.setAlarm(alarm);

        // Getting time values (hour and minute) from Date variable
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(alarm);   // assigns calendar to given date
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int minute = calendar.get(Calendar.MINUTE); // gets minute

        //sets default Alarm Clock app with time
        setDefaultAlarm(hour, minute);
        // Update on screen text to alarm time
        TextView tv = (TextView) rootView.findViewById(R.id.textView_alarmTime);
        tv.setText("" + String.format("%s\n %tl:%<tM%<tp", "Alarm set for", alarm));
    }

    public void calculateAlarm() {
        Date alarm = calculateAlarmTime(bhour, bminute, whour, wminute);
        Log.d(TAG,"Alarm time calculated: "+alarm);
        setCalculatedTimeText(alarm);
        Log.d(TAG,"Calculated time updated");
    }

    public void setCalculatedTimeText(Date alarm) {
        // Update on screen calculated time to alarm time
        TextView tv = (TextView) rootView.findViewById(R.id.textView_calcTime);
        tv.setText("" + String.format("%tl:%<tM%<tp", alarm));
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

    public void showTimePickerDialog(View v) {
        Log.d(TAG,"show timepickerdialog function called");
        DialogFragment newFragment = new TimePickerFragment();
        FragmentManager fm = getActivity().getFragmentManager();

        newFragment.show(fm, "timePicker");

        //creating a new bundle that includes dialogType
        Bundle args = new Bundle();
        args.putInt("dialogType",0);
        newFragment.setArguments(args);
        Log.d(TAG,"end of showTimePickerDialog");
    }

    public void showBedTimePickerDialog() {
        Log.d(TAG,"show timepickerdialog function called");
        DialogFragment newFragment = new TimePickerFragment();
        FragmentManager fm = getActivity().getFragmentManager();
        newFragment.show(fm, "timePicker");

        //creating a new bundle that includes dialogType
        Bundle args = new Bundle();
        args.putInt("dialogType", 1);
        newFragment.setArguments(args);
        Log.d(TAG, "end of showTimePickerDialog");
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
        final TextView tvSetBedTime=rootView.findViewById(R.id.textView_bedTime);
        final TextView tvSetWakeTime=rootView.findViewById(R.id.textView_wakeTime);
        Button btn1=rootView.findViewById(R.id.button_bedTime);
        Button btn2=rootView.findViewById(R.id.button_latestWakeUp);
        Button btn3=rootView.findViewById(R.id.button_setAlarm);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBedTimePickerDialog();
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
                if(tvSetBedTime.getText().toString().equals("No time set!")){
                    Toast.makeText(context,"Set bed time.",Toast.LENGTH_LONG).show();
                }else if(tvSetWakeTime.getText().toString().equals("No time set!")){
                    Toast.makeText(context,"Set wake time.",Toast.LENGTH_LONG).show();
                }else if(!tvSetBedTime.getText().toString().equals("No time set!") && !tvSetWakeTime.getText().toString().equals("No time set!")){
                    setPreferences();
                }
            }
        });
    }

    public static Date calculateAlarmTime(int bhour, int bmin, int whour, int wmin) {
        // Calculates what time to set the alarm to.
        // Alarm should be set to rounddown of
        // = (wake time - bed time)/90 minutes + time req'd to fall asleep
        Date alarm = new Date();
        System.out.println("date: "+ String.format("%tc", alarm));
        int cycles = 0; // maximum number of sleep cycles
        long diff = 0;
        int alarmTime = 0;

        // Convert bed time and wake time hours and minutes to Date format
        try {
            java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm");
            System.out.println("converting bedtime to date");
            java.util.Date bedTime = df.parse("" + bhour + ":" + bmin);
            java.util.Date wakeTime = df.parse("" + whour + ":" + wmin);

            // Finding the time difference between wake time and bed time
            System.out.println("subtraction: " + wakeTime.getTime() + " - " + bedTime.getTime());
            if (wakeTime.getTime() >= bedTime.getTime()) {
                // wake up time is on the same day as bed time
                // (e.g. nap on Sat aft, wake Sat night)
                diff = wakeTime.getTime() - bedTime.getTime();
            }
            else {
                // wake up time is on the next day
                // (e.g. sleep on Sat night, wake on Sun morning)
                System.out.println("subtraction: " + wakeTime.getTime() + " + (86400000 - "+ bedTime.getTime() +")");
                diff = wakeTime.getTime() + (86400000 - bedTime.getTime());
            }
            System.out.println("diff:" + diff);

            // Calculating maximum # of sleep cycles possible between bed time and wake time.
            if(diff<=fallTime*60000){
                cycles = 0; // when cycles would be otherwise negative
            } else {
                cycles = (int) Math.floor((diff - fallTime * 60000) / (3600000 * 1.5));
            }
            System.out.println("cycles:" + cycles);
            // Calculating the time the alarm should ring
            System.out.println("alarm time calculation: " + bedTime.getTime() +","+ cycles +","+ fallTime);
            if(cycles == 0) {
                // Assumption: Users will be taking a short nap if cycles=0
                // and are not concerned with sleep cycles.
                alarmTime = (int) wakeTime.getTime();
            } else {
                alarmTime = (int) Math.floor(bedTime.getTime() + cycles * 1.5 * 3600000 + (fallTime * 60000));
            }
            System.out.println("alarm time: "+alarmTime);

//            // Error handling in case alarm time is outside of bed to wake time range.
//            if (alarmTime > wakeTime.getTime() || alarmTime < bedTime.getTime()){
//                alarmTime = (int) wakeTime.getTime();
//            }

            // Converting alarm time to Date format
            alarm = new Date(alarmTime);
            System.out.printf("%s %tl:%<tM%<tp\n", "Date:", alarm);
            System.out.println(""+ String.format("%tc", alarm));

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return alarm;
    }

}
