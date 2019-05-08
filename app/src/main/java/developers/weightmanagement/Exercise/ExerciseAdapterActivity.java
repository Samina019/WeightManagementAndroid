package developers.weightmanagement.Exercise;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import developers.weightmanagement.R;
import developers.weightmanagement.Room.DatabaseClient;
import developers.weightmanagement.Room.Exercise;
import developers.weightmanagement.Water.Database.DrinkDataSource;

public class ExerciseAdapterActivity extends AppCompatActivity {

    static ListView listView;
    static List<Exercise> values ;
    static ArrayAdapter<Exercise> adapter;


    private static DrinkDataSource db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_exercise_adapter);
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
                    .exerciseDao()
                    .allExercisesRecord(email);

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

    private class myListAdapter extends ArrayAdapter<Exercise> {

        List<Exercise> Values ;
        myListAdapter(List<Exercise> values) {
            super(ExerciseAdapterActivity.this, R.layout.cell_exercise_adapter, values);
            this.Values=values;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                    R.layout.cell_exercise_adapter, parent, false);
            }

            TextView date=itemView.findViewById(R.id.tvDate);
            TextView name=itemView.findViewById(R.id.tvExerciseName);
            TextView cals=itemView.findViewById(R.id.tvCals);
            ImageView image=itemView.findViewById(R.id.ivImage);
            ImageButton shareButton=itemView.findViewById(R.id.forward);

            name.setText(Values.get(position).getExercise_name());
            date.setText(Values.get(position).getDate());
            cals.setText(Values.get(position).getCalories() + " calories burned");

            switch (Values.get(position).getExercise_name()){

                case "Walking":
                    image.setImageResource(R.drawable.walkk);
                    break;

                case "Running":
                    image.setImageResource(R.drawable.run);
                    break;

                case "Cycling":
                    image.setImageResource(R.drawable.bicycle);
                    break;

                default:
                    break;

            }

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text =  "I've just been reminded to burned " +Values.get(position).getCalories() +
                        " calories through " + Values.get(position).getExercise_name()+" by Weight Management App!";
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

