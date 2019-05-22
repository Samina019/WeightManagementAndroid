package developers.weightmanagement.Sleep;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import developers.weightmanagement.R;
import developers.weightmanagement.Room.DatabaseClient;
import developers.weightmanagement.Room.Sleep;

public class SleepAdapterActivity extends AppCompatActivity {

    static ListView listView;
    static List<Sleep> values ;
    static ArrayAdapter<Sleep> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_adapter);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        setTitle("History");

        getData();
    }

    private void getData(){
        @SuppressLint("StaticFieldLeak")
        class getAllData extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                String email;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("weightManagement", 0); // 0 - for private mode
                email=pref.getString("userEmail", "");

                //adding to database

                // Get Data
                values= DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .sleepDao()
                    .allSleepRecord(email);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                listView = (ListView) findViewById(R.id.log_list);

                adapter = new myListAdapter(values);
                listView.setAdapter(adapter);

            }
        }

        getAllData st = new getAllData();
        st.execute();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class myListAdapter extends ArrayAdapter<Sleep> {

        List<Sleep> Values ;
        myListAdapter(List<Sleep> values) {
            super(SleepAdapterActivity.this, R.layout.cell_sleep_adapter, values);
            this.Values=values;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                    R.layout.cell_sleep_adapter, parent, false);
            }

            TextView date=itemView.findViewById(R.id.tvDate);
            TextView bedTime=itemView.findViewById(R.id.tvBedTime);
            TextView wakeTime=itemView.findViewById(R.id.tvWakeTime);
            ImageButton shareButton=itemView.findViewById(R.id.forward);

            date.setText(Values.get(position).getDate());
            bedTime.setText(Values.get(position).getBedTime());
            wakeTime.setText(Values.get(position).getWakeTime());


            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text =  "I've just been reminded that I slept at " +Values.get(position).getBedTime()+
                        " and wake up at " + Values.get(position).getWakeTime()+ " by Weight Management App!";
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,text);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });

            return itemView;
        }
    }
    public static void reloadAdapter() {
        adapter.clear();
        listView.setAdapter(adapter);
    }

}
