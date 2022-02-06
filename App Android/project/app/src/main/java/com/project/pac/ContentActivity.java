package com.project.pac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class ContentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new ListaTurniFragment());
        fragmentTransaction.commit();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.close();
        switch (item.getItemId()){
            case R.id.nav_turni:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new ListaTurniFragment());
                fragmentTransaction.commit();
                break;
            case R.id.nav_malattia:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new MalattiaFragment());
                fragmentTransaction.commit();
                break;
            case R.id.nav_ferie:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new FerieFragment());
                fragmentTransaction.commit();
                break;
            case R.id.nav_account:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                // fragmentTransaction.replace(R.id.container_fragment, new FerieFragment());
                fragmentTransaction.commit();
                break;
            case R.id.nav_logout:

                sharedpreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String BASE_IP = sharedpreferences.getString(String.valueOf(R.string.SESSION_IP), String.valueOf(Context.MODE_PRIVATE));
                String token = sharedpreferences.getString(String.valueOf(R.string.SESSION_TOKEN), String.valueOf(Context.MODE_PRIVATE));
                String email = sharedpreferences.getString(String.valueOf(R.string.session_emailUtente), String.valueOf(Context.MODE_PRIVATE));

                Map<String, String> map = new HashMap<String, String>();
                map.put("email", email);
                map.put("token", token);

                ApiRequest apiRequest = new ApiRequest(map, "http://" + BASE_IP + "/api/logout", this);

                if(apiRequest.logoutReq()){
                    Log.d("logout", "loggin out");
                    editor.remove(String.valueOf(R.string.session_emailUtente));
                    editor.remove(String.valueOf(R.string.SESSION_TOKEN));
                    editor.remove(String.valueOf(R.string.SESSION_IP));
                    editor.commit();

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    editor.remove(String.valueOf(R.string.session_emailUtente));
                    editor.remove(String.valueOf(R.string.SESSION_TOKEN));
                    editor.remove(String.valueOf(R.string.SESSION_IP));
                    editor.commit();
                }

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        return true;
    }
}