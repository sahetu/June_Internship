package june.internship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class DashboardActivity extends AppCompatActivity {

    MeowBottomNavigation meowBottomNavigation;

    int HOME_MENU = 1;
    int DASHBOARD_MENU = 2;
    int NOTIFICATION_MENU = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        meowBottomNavigation = findViewById(R.id.dashboard_bottom);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(HOME_MENU, R.drawable.ic_home_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(DASHBOARD_MENU, R.drawable.ic_dashboard_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(NOTIFICATION_MENU, R.drawable.ic_notifications_black_24dp));

        //meowBottomNavigation.show(HOME_MENU,true);

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if (item.getId() == HOME_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_layout, new HomeFragment()).commit();
                }
                if (item.getId() == DASHBOARD_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_layout, new HomeFragment()).commit();
                }
                if (item.getId() == NOTIFICATION_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_layout, new HomeFragment()).commit();
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
}