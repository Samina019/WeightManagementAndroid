package developers.weightmanagement.Exercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Objects;

import developers.weightmanagement.Exercise.ExerciseAdapterActivity;
import developers.weightmanagement.Exercise.ExerciseDetailActivity;
import developers.weightmanagement.R;
import developers.weightmanagement.Water.OutlinesFragments.OutlineActivity;

public class exerciseFragment extends Fragment{


    Context context;
    View rootView;
    LinearLayout walking,running,cycling;
    ImageButton history,outlines;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        context = Objects.requireNonNull(getActivity()).getApplicationContext();

        registerViews();
        registerListener();
        return rootView;
    }

    private void registerViews(){

        walking=rootView.findViewById(R.id.layoutWalking);
        running=rootView.findViewById(R.id.layoutRunning);
        cycling=rootView.findViewById(R.id.layoutBicycling);
        history=rootView.findViewById(R.id.log_button);
        outlines=rootView.findViewById(R.id.outlines_button);

    }

    private void registerListener(){
        walking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ExerciseDetailActivity.class);
                intent.putExtra("exerciseType","Walking");
                startActivity(intent);

            }
        });
        running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ExerciseDetailActivity.class);
                intent.putExtra("exerciseType","Running");
                startActivity(intent);
            }
        });
        cycling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ExerciseDetailActivity.class);
                intent.putExtra("exerciseType","Cycling");
                startActivity(intent);
            }
        });

        outlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOutlineActivity();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogActivity();
            }
        });

    }

    private void goToOutlineActivity() {
        Intent intent2 = new Intent(context, OutlineActivity.class);
        intent2.putExtra("name","exercise");
        startActivity(intent2);
    }


    private void goToLogActivity() {
        Intent intent4 = new Intent(context, ExerciseAdapterActivity.class);
        startActivity(intent4);
    }

}
