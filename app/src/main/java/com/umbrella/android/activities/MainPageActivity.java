package com.umbrella.android.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.umbrella.android.R;

public class MainPageActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rlContainer, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_activity);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
      // bottomNav.setOnNavigationItemSelectedListener(navListener);

    }
//    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            // By using switch we can easily get
//            // the selected fragment
//            // by using there id.
//            Fragment selectedFragment = null;
//            switch (item.getItemId()) {
//                case R.id.save:
//                    selectedFragment = new SaveFragment();
//                    break;
//                case R.id.upload:
//                    selectedFragment = new UploadFragment();
//                    break;
//                case R.id.delete:
//                    selectedFragment = new DeleteFragment();
//                    break;
//            }
//            // It will help to replace the
//            // one fragment to other.
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, selectedFragment)
//                    .commit();
//            return true;
//        }
//    };
}
