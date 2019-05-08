package developers.weightmanagement.Water.OutlinesFragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Objects;

import developers.weightmanagement.R;

public class OutlineActivity extends AppCompatActivity {
    private TextView pageMun;
    private PagerAdapter pagerAdapter;
    Intent intent;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_outline);
        setTitle("Outlines");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        intent = getIntent();
        name = intent.getStringExtra("name");

        pageMun = (TextView) findViewById(R.id.pageNum);
        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        pagerAdapter = new MyPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pageMun.setText(((position + 1) + "/" + pagerAdapter.getCount()) );

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        Fragment[] fragments = new Fragment[6];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);

            switch (name){
                case "exercise":
                    fragments[0] = adviceFragment.create(R.string.advice1, R.drawable.one);
                    fragments[1] = adviceFragment.create( R.string.advice2, R.drawable.two);
                    fragments[2] = adviceFragment.create( R.string.advice3, R.drawable.three);
                    fragments[3] = adviceFragment.create( R.string.advice4, R.drawable.four);
                    fragments[4] = adviceFragment.create( R.string.advice5, R.drawable.five);
                    fragments[5] = adviceFragment.create( R.string.advice6, R.drawable.bicycle);

                    break;

                case "water":
                    fragments[0] = adviceFragment.create(R.string.wake_up_review, R.drawable.wakeup);
                    fragments[1] = adviceFragment.create( R.string.sleep_review, R.drawable.sleep);
                    fragments[2] = adviceFragment.create( R.string.shower_review, R.drawable.shower);
                    fragments[3] = adviceFragment.create( R.string.meal_review, R.drawable.eat);
                    fragments[4] = adviceFragment.create( R.string.lose_weight, R.drawable.lose_weight);
                    fragments[5] = adviceFragment.create( R.string.lack_water, R.drawable.pheart);
                    break;
            }

        }


        @Override
        public Fragment getItem(int position) {

            return fragments[position];

        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

