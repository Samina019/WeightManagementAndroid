package developers.weightmanagement.Program;

import android.os.Bundle;
import developers.weightmanagement.R;

public class BalancedLifeActivity extends BaseActivity {

    @Override
    int getContentViewId() {
        return R.layout.layout_balanced_life;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_balanced_life;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Balanced Life");

    }
}
