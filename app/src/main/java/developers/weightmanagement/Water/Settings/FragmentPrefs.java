package developers.weightmanagement.Water.Settings;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.TimePicker;
import java.util.Calendar;

import developers.weightmanagement.R;

public class FragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private SettingsActivity mActivity;
    private Context context;
    private Preference glassSizePref, bottleSizePref, soundPref,weightPref,trainingPref,waterNeedPref;
    private Calendar fromC = Calendar.getInstance();
    private Calendar toC = Calendar.getInstance();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =mActivity.getApplicationContext();
        addPreferencesFromResource(R.xml.fragment_prefs);
        soundPref= findPreference(PreferenceKey.PREF_SOUND);
        weightPref =findPreference(PreferenceKey.PREF_WEIGHT_NUMBER);
        trainingPref= findPreference(PreferenceKey.PREF_TRAINING);
        waterNeedPref = findPreference(PreferenceKey.PREF_WATER_NEED);
        glassSizePref =  findPreference(PreferenceKey.PREF_GLASS_SIZE);
        bottleSizePref =  findPreference(PreferenceKey.PREF_BOTTLE_SIZE);


        trainingPref.setOnPreferenceChangeListener(this);
        weightPref.setOnPreferenceChangeListener(this);
        waterNeedPref.setOnPreferenceChangeListener(this);
        soundPref.setOnPreferenceChangeListener(this);
        glassSizePref.setOnPreferenceChangeListener(this);
        bottleSizePref.setOnPreferenceChangeListener(this);
        initSummaries();

    }
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity!=null) {
            mActivity=(SettingsActivity)activity;
        }

    }

    private void initSummaries() {
        boolean isSoundEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_SOUND, false);
        int weight = getPreferenceManager().getSharedPreferences().getInt(PreferenceKey.PREF_WEIGHT_NUMBER, 0);
        boolean training= getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_TRAINING,false);
        int water_reco= getPreferenceManager().getSharedPreferences().getInt(PreferenceKey.PREF_WATER_NEED,0);
        String glassSize = getPreferenceManager().getSharedPreferences().getString(PreferenceKey.PREF_GLASS_SIZE,"");
        String bottleSize = getPreferenceManager().getSharedPreferences().getString(PreferenceKey.PREF_BOTTLE_SIZE,"");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
          if(weight!=0)
             weightPref.setSummary(Integer.toString(weight)+" kg");
        trainingPref.setSummary(getString(training));
        waterNeedPref.setSummary(Integer.toString(water_reco)+" ml");
        glassSizePref.setSummary(glassSize+ " ml");
        bottleSizePref.setSummary(bottleSize+ " ml");
        soundPref.setSummary(getString(isSoundEnabled));

    }




    TimePickerDialog.OnTimeSetListener timeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            fromC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            fromC.set(Calendar.MINUTE,minute);


        }

    };

    TimePickerDialog.OnTimeSetListener timeTo = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            toC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            toC.set(Calendar.MINUTE,minute);

        }

    };

    private String getString(boolean b){
        if (b)
            return "ON";
        return "OFF";

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case PreferenceKey.PREF_WEIGHT_NUMBER:
                   handleWeightPref( newValue.toString(), preference);
                return true;
            case PreferenceKey.PREF_TRAINING:
                  handleTrainingPrefs(newValue.toString(),preference);
                return true;

            case PreferenceKey.PREF_WATER_NEED:
                preference.setSummary(newValue.toString()+ " ml");
                return true;

            case  PreferenceKey.PREF_GLASS_SIZE:
                preference.setSummary(newValue.toString()+ " ml");
                return true;

            case  PreferenceKey.PREF_BOTTLE_SIZE:
                preference.setSummary(newValue.toString()+ " ml");
                return true;

            case PreferenceKey.PREF_SOUND:
                setSwitchPrefSummaries(newValue.toString(), preference);
                return true;

        }

        return true;
    }

    private void handleTrainingPrefs(String s, Preference preference) {
        boolean value = Boolean.valueOf(s);
        setTrainingToPref(value);
        int waterNeedValue =calculateWaterReco();
        setWaterNeedToPref(waterNeedValue);
        preference.setSummary(getString(value));
        waterNeedPref.setSummary(String.valueOf(waterNeedValue) +" ml");
    }

    private void handleWeightPref(String s, Preference preference) {
        setWeightToPref(Integer.valueOf(s));
        int value =calculateWaterReco();
        setWaterNeedToPref(value);
        preference.setSummary(s+" kg");
        waterNeedPref.setSummary(String.valueOf(value) +" ml");
    }

    private void setSwitchPrefSummaries(String newValue, Preference preference) {
        if(newValue.equals("true"))
        {  preference.setSummary("ON ");
        }
        else
            preference.setSummary("OFF ");
    }


    private void setTrainingToPref(Boolean aBoolean) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PreferenceKey.PREF_TRAINING, aBoolean);
        editor.apply();
    }

    private void setWeightToPref(int v) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PreferenceKey.PREF_WEIGHT_NUMBER, v);
        editor.apply();
    }
    private void setWaterNeedToPref(int v) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PreferenceKey.PREF_WATER_NEED, v);
        editor.apply();
    }
    private void setTimePref(String command, String str){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(command, str);
        editor.commit();

    }

    private int calculateWaterReco() {
        int weight = getPreferenceManager().getSharedPreferences().getInt(PreferenceKey.PREF_WEIGHT_NUMBER, 0);
        boolean training= getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_TRAINING,false);
        double waterNeed= weight/ 0.030 ;
        if (training)
            waterNeed= waterNeed+300;

        return (int)waterNeed;

    }



}
