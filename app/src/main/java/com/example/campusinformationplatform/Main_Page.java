package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Main_Page extends AppCompatActivity {

    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment

    //退出时的时间
    private long mExitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__page);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_information,
                R.id.navigation_personal)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navView.setSelectedItemId(R.id.navigation_information);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


//        Button To_Release_Page=(Button) findViewById(R.id.To_Release_Page_Bt);
//
//        To_Release_Page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Main_Page.this , Release_Page.class);
//                startActivity(i);
//
//            }
//        });



    }

    @Override

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //
        try {
            String Inf = getIntent().getStringExtra("Inf").toString();
            //Log.d("TAG", "onNewIntent: " + Inf);

            if (Inf.equals("SignIn")||Inf.equals("Refersh")) {

                BottomNavigationView navView = findViewById(R.id.nav_view);

                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_information,
                        R.id.navigation_personal)
                        .build();
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

                navView.setSelectedItemId(R.id.navigation_information);
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
                NavigationUI.setupWithNavController(navView, navController);


//                Button To_Release_Page = (Button) findViewById(R.id.To_Release_Page_Bt);
//
//                To_Release_Page.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i = new Intent(Main_Page.this, Release_Page.class);
//                        startActivity(i);
//
//                    }
//                });
            }
        }catch (Exception e){
            //e.printStackTrace();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(Main_Page.this, "再按一次退出当前应用程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            moveTaskToBack(true);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_release){
            //todo
            Log.d("111", "onOptionsItemSelected: ");

            Intent i = new Intent(Main_Page.this , Release_Page.class);
            startActivity(i);

            return false;
        }
        return super.onOptionsItemSelected(item);
    }


}
