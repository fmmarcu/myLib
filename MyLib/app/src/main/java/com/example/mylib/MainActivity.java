package com.example.mylib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    protected BottomNavigationView mBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        //Create the NavController
        final NavController navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        NavigationUI.setupWithNavController(mBottomNavigation, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.loginFragment:
                    case R.id.registerFragment:
                        mBottomNavigation.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        mBottomNavigation.setVisibility(View.VISIBLE);
                }
            }
        });

        getSupportActionBar().hide();
    }

    public void hideBottomNavigation() {
        mBottomNavigation.setVisibility(View.GONE);
    }

    public void displayBottomNavigation() {
        mBottomNavigation.setVisibility(View.VISIBLE);
    }
}
