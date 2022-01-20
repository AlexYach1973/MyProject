package com.example.android.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.navView);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph())
//                R.id.scheduleFragment, R.id.dimensionFragment, R.id.settingFragment)
                .setOpenableLayout(drawerLayout)
                .build();


        // Связать Controller Navigation с ActionBar
        NavigationUI.setupActionBarWithNavController(this,
                navController, mAppBarConfiguration);

        // Боковое меню
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    // Ящик слева вверху
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}