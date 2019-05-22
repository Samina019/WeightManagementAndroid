package developers.weightmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.Objects;

public class FoodFragment extends Fragment {

    Context context;
    View rootView;
    RelativeLayout breakfast,lunch,dinner,snack;
    ImageButton history;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_food_fragment, container, false);
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        initViews();
        initListeners();
        return rootView;
    }

    private void initViews(){
        breakfast=rootView.findViewById(R.id.breakFast);
        lunch=rootView.findViewById(R.id.lunch);
        dinner=rootView.findViewById(R.id.dinner);
        snack=rootView.findViewById(R.id.snack);
        history=rootView.findViewById(R.id.log_button);

    }

    private void initListeners(){
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddFoodActivity.class);
                intent.putExtra("mealType","Breakfast");
                startActivity(intent);
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddFoodActivity.class);
                intent.putExtra("mealType", "Lunch");
                startActivity(intent);
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddFoodActivity.class);
                intent.putExtra("mealType", "Dinner");
                startActivity(intent);
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddFoodActivity.class);
                intent.putExtra("mealType", "Snack");
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogActivity();
            }
        });

    }

    private void goToLogActivity() {
        Intent intent4 = new Intent(context, FoodAdapterActivity.class);
        startActivity(intent4);
    }

}
