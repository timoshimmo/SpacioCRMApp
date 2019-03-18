package com.android.spaciocrm.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.help.HelpActivity;
import com.android.spaciocrm.home.fragments.FragAppointments;
import com.android.spaciocrm.home.fragments.FragContacts;
import com.android.spaciocrm.home.fragments.FragProducts;
import com.android.spaciocrm.home.fragments.FragReferrals;
import com.android.spaciocrm.home.fragments.FragmentPreferences;
import com.android.spaciocrm.home.fragments.FragmentProfile;
import com.android.spaciocrm.home.fragments.appointments.FragmentNewAppointment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String TAG;
    String activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            activeUser = bundle.getString("USERNAME");
        }

        SharedPreferences prefActiveUSer = getSharedPreferences("PREFACTIVEUSER", Context.MODE_PRIVATE);
        SharedPreferences.Editor listEditor = prefActiveUSer.edit();
        listEditor.putString("activeUser", activeUser);
        listEditor.apply();

        TAG = "FRAG_CONTACTS";
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_home, FragContacts.newInstance("Contacts"), TAG);
        transaction.commit();

        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch(item.getItemId()) {

                    case R.id.nav_contacts:
                        selectedFragment = FragContacts.newInstance("Contacts");
                        TAG = "FRAG_CONTACTS";
                        break;
                    case R.id.nav_appointments:
                        selectedFragment = FragAppointments.newInstance("Appointments");
                        TAG = "FRAG_APPOINTMENTS";
                        break;
                    case R.id.nav_products:
                        selectedFragment = FragProducts.newInstance("Products");
                        TAG = "FRAG_PRODUCTS";
                        break;
                    case R.id.nav_referrals:
                        selectedFragment = FragReferrals.newInstance("Referrals");
                        TAG = "FRAG_REFERRALS";
                        break;
                    case R.id.nav_menu:
                        selectedFragment = getSupportFragmentManager().findFragmentByTag(TAG);
                        if (drawer.isDrawerOpen(GravityCompat.END)) {
                            drawer.closeDrawer(GravityCompat.END);
                        } else {
                            drawer.openDrawer(GravityCompat.END);
                        }
                        break;

                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_home, selectedFragment, TAG);
                transaction.commit();

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            FragmentProfile fragmentProfile = new FragmentProfile();
            fragmentProfile.show(getSupportFragmentManager(), "ProfileFragment");
        } else if (id == R.id.nav_performance_graph) {

        } else if (id == R.id.nav_preferences) {
            FragmentPreferences fragmentPreferences = new FragmentPreferences();
            fragmentPreferences.show(getSupportFragmentManager(), "PreferenceFragment");

        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_logout) {
            System.exit(0);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


}
