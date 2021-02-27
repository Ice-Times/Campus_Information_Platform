package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main_Page extends AppCompatActivity {

    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment

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


        Button To_Release_Page=(Button) findViewById(R.id.To_Release_Page_Bt);

        To_Release_Page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main_Page.this , Release_Page.class);
                startActivity(i);

            }
        });



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


                Button To_Release_Page = (Button) findViewById(R.id.To_Release_Page_Bt);

                To_Release_Page.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Main_Page.this, Release_Page.class);
                        startActivity(i);

                    }
                });
            }
        }catch (Exception e){
            //e.printStackTrace();
        }


    }



}
