package june.internship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class DashboardActivity extends AppCompatActivity {

    MeowBottomNavigation meowBottomNavigation;

    int HOME_MENU = 1;
    int CATEGORY_MENU = 2;
    int PROFILE_MENU = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        meowBottomNavigation = findViewById(R.id.dashboard_bottom);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(HOME_MENU, R.drawable.ic_home_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(CATEGORY_MENU, R.drawable.ic_category));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(PROFILE_MENU, R.drawable.ic_user));

        //meowBottomNavigation.show(HOME_MENU,true);

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if (item.getId() == HOME_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_layout, new HomeFragment()).commit();
                }
                if (item.getId() == CATEGORY_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_layout, new CategoryFragment()).commit();
                }
                if (item.getId() == PROFILE_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_layout, new ProfileFragment()).commit();
                }
                //Toast.makeText(DashboardActivity.this, "clicked item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(HomeActivity.this, "showing item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        meowBottomNavigation.show(HOME_MENU, true);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.dashboard_layout, new HomeFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
    }

}