package developers.weightmanagement.Water.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import developers.weightmanagement.Water.Database.DrinkDataSource;
import developers.weightmanagement.Water.MainWindow.AlarmHelper;
import developers.weightmanagement.Water.Settings.PrefsHelper;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context pContext, Intent intent) {
        DrinkDataSource db= new DrinkDataSource(pContext);
        db.open();
       int waterNeed= PrefsHelper.getWaterNeedPrefs(pContext);
       db.createMissingDateLog(0,waterNeed);
       AlarmHelper.setDBAlarm(pContext);
    }
}


