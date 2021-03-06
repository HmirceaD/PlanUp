package com.example.mircea.moneymanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mircea.moneymanager.Adapters.MainActivityPagerAdapter;
import com.example.mircea.moneymanager.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Ui
    private ViewPager viewPager;
    private TabLayout tabLayout;

    //Logic
    private int startingPosition = 0;

    //Adapters
    private MainActivityPagerAdapter mainActivityPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerInit();
        uiAndAdaptersInit();
        getPosition();

        viewPager.setAdapter(mainActivityPagerAdapter);
        viewPager.setCurrentItem(startingPosition);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void getPosition() {

        Intent intent = getIntent();

        if (intent != null) {

            try{
                startingPosition = intent.getIntExtra("CURRENT_PAGE",0);

            }catch (Exception ex){
                ex.printStackTrace();
                startingPosition = 0;
            }
        }
    }

    private void uiAndAdaptersInit() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.pagerTabs);

        mainActivityPagerAdapter = new MainActivityPagerAdapter(this, getSupportFragmentManager());
    }

    /**Drawer UI**/
    private void drawerInit() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**Back Button**/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**Inflate Hamburger**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**Hamburger**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
        }else if(id == R.id.action_wallet){

            startActivity(new Intent(this, CreatePlanActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**Drawer**/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_create_plan) {

            startActivity(new Intent(this, CreatePlanActivity.class));
        } else if (id == R.id.nav_achievements) {

            startActivity(new Intent(this, AchievementsActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
