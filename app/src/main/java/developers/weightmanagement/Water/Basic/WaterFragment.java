package developers.weightmanagement.Water.Basic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Objects;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.lzyzsd.circleprogress.DonutProgress;
import developers.weightmanagement.R;
import developers.weightmanagement.Water.Chart.ChartActivity;
import developers.weightmanagement.Water.Database.DrinkDataSource;
import developers.weightmanagement.Water.Dialogs.AddDialog;
import developers.weightmanagement.Water.MainWindow.AlarmHelper;
import developers.weightmanagement.Water.MainWindow.DateHandler;
import developers.weightmanagement.Water.OutlinesFragments.OutlineActivity;
import developers.weightmanagement.Water.Settings.PrefsHelper;
import developers.weightmanagement.Water.Settings.SettingsActivity;
import developers.weightmanagement.Water.WaterDrankHistory.DateLogActivity;


public class WaterFragment extends Fragment implements View.OnClickListener{

    Context context;
    View rootView;

    private ImageButton logButton, chartButton, settingButton,outlinesButton;
    private Button addDrinkButton;
    private LinearLayout mainLayout;
    public static  DonutProgress circleProgress;
    public static TextView choosenAmountTv;
    private DrinkDataSource db;
    private BroadcastReceiver updateUIReciver;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_water, container, false);

        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        db= new DrinkDataSource(context);
        db.open();
        checkAppFirstTimeRun();
        AlarmHelper.setDBAlarm(context);
        mainLayout= (LinearLayout)rootView. findViewById(R.id.main_view);
        circleProgress = (DonutProgress) rootView.findViewById(R.id.donut_progress);
        initializeViews();
        updateView();
        registerUIBroadcastReceiver();
        return rootView;
    }

    private void checkAppFirstTimeRun() {

        if (PrefsHelper.getFirstTimeRunPrefs(context)) {
            db.createDateLog(0,PrefsHelper.getWaterNeedPrefs(context),
                DateHandler.getCurrentDate());
            //   AlarmHelper.setDBAlarm(context);
            startActivityForResult(new Intent(context, SettingsActivity.class),0);
            PrefsHelper.setFirstTimeRunPrefs(context,false);
        }
    }

    @Override
    public void onDestroy(){
        context.unregisterReceiver(updateUIReciver);
        super.onDestroy();

    }

    private void  updateView(){
        int perValue= db.getConsumedPercentage();
        circleProgress.setProgress(perValue);
        choosenAmountTv.setText(String.valueOf(db.geConsumedWaterForToadyDateLog()+" out of "+
            PrefsHelper.getWaterNeedPrefs(context)+" ml"));
    }

    private void showAddDialog() {

        AddDialog a = new AddDialog(getActivity(), db);
        a.show();

    }


    private void goToSettingActivity() {
        startActivityForResult(new Intent(context, SettingsActivity.class),0);
    }

    private void goToOutlineActivity() {
        Intent intent2 = new Intent(context, OutlineActivity.class);
        intent2.putExtra("name","water");
        startActivity(intent2);
    }

    private void goTolChartActivity() {
        Intent intent3 = new Intent(context, ChartActivity.class);
        startActivity(intent3);
    }

    private void goToLogActivity() {
        Intent intent4 = new Intent(context, DateLogActivity.class);
        startActivity(intent4);
    }

    public void  initializeViews(){

        logButton =(ImageButton)rootView.findViewById(R.id.log_button);
        chartButton =(ImageButton)rootView.findViewById(R.id.chart_button);
        settingButton =(ImageButton)rootView.findViewById(R.id.setting_button);
        outlinesButton =(ImageButton)rootView.findViewById(R.id.outlines_button);

        choosenAmountTv=(TextView) rootView.findViewById(R.id.choosen_drink_text);
        addDrinkButton = (Button) rootView.findViewById(R.id.add_drink_button);
        logButton.setOnClickListener(this);
        chartButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        outlinesButton.setOnClickListener(this);

        addDrinkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.log_button:
                goToLogActivity();
                break;
            case R.id.chart_button:
                goTolChartActivity();
                break;
            case R.id.outlines_button:
                goToOutlineActivity();
                break;
            case R.id.setting_button:
                goToSettingActivity();
                break;
            case R.id.add_drink_button:
                showAddDialog();
                break;


        }
    }

    private void registerUIBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.update.view.action");
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateView();
            }
        };
        context.registerReceiver(updateUIReciver,filter);
    }


}


