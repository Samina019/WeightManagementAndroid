package developers.weightmanagement.Food;

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
import developers.weightmanagement.Room.Food;
import developers.weightmanagement.Water.Database.DrinkDataSource;

public class FoodAdapterActivity extends AppCompatActivity {

    static ListView listView;
    static List<Food> values ;
    static ArrayAdapter<Food> adapter;

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
                    .foodDao()
                    .allFoodRecord(email);

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

    private class myListAdapter extends ArrayAdapter<Food> {

        List<Food> Values ;
        myListAdapter(List<Food> values) {
            super(FoodAdapterActivity.this, R.layout.cell_food_adapter, values);
            this.Values=values;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                    R.layout.cell_food_adapter, parent, false);
            }

            TextView tvFoodName=itemView.findViewById(R.id.tvFoodName);
            TextView tvDate=itemView.findViewById(R.id.tvDate);
            TextView tvMealType=itemView.findViewById(R.id.tvMealType);
            TextView tvCalories=itemView.findViewById(R.id.tvCalories);
            TextView tvCarbs=itemView.findViewById(R.id.tvCarbs);
            TextView tvProteins=itemView.findViewById(R.id.tvFats);
            TextView tvFats=itemView.findViewById(R.id.tvProtiens);
            ImageButton shareButton=itemView.findViewById(R.id.forward);

            tvFoodName.setText(Values.get(position).getFoodName());
            tvDate.setText(Values.get(position).getDate());
            tvMealType.setText(Values.get(position).getMealType());
            tvCalories.setText(Values.get(position).getCalories());
            tvCarbs.setText(Values.get(position).getCarbohydrates());
            tvProteins.setText( Values.get(position).getProteins());
            tvFats.setText(Values.get(position).getFats());

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text =  "I've just been reminded that I eat " + Values.get(position).getCalories()
                    + " calories by Weight Management App!";
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

