package rebus.llc.parvoz;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import rebus.llc.parvoz.fragments.InfoFragment;
import rebus.llc.parvoz.fragments.MainFragment;
import rebus.llc.parvoz.fragments.MyTicketsFragment;
import rebus.llc.parvoz.fragments.NotificationFragment;
import rebus.llc.parvoz.others.Settings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    MainFragment mainFragment;
    MyTicketsFragment myTicketsFragment;
    NotificationFragment notificationFragment;
    InfoFragment infoFragment;
    private final static int IMAGE_RESULT = 200;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    FragmentManager fm;
    FragmentTransaction transaction;
    int selected_item;

    private MyServiceNotif myServiceNotif;
    Intent myServiceIntent;
    public static boolean app_is_open = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selected_item = 1;
        myServiceNotif = new MyServiceNotif();
        myServiceIntent = new Intent(getApplicationContext(), myServiceNotif.getClass());
        app_is_open = true;


//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                startService(new Intent(getApplicationContext(), MyServiceNewMeeting.class));
//                startService(new Intent(getApplicationContext(), MyServiceNewPayment.class));
//
//            }
//        };
//        thread.start();
        if (!isMyServiceRunning(myServiceNotif.getClass())) {
            startService(myServiceIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainFragment = new MainFragment();
        myTicketsFragment = new MyTicketsFragment();
        notificationFragment = new NotificationFragment();
        infoFragment = new InfoFragment();

        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.add(R.id.main_frame, mainFragment);
        transaction.add(R.id.main_frame, myTicketsFragment);
        transaction.add(R.id.main_frame, notificationFragment);
        transaction.add(R.id.main_frame, infoFragment);

        transaction.commit();
        transaction = fm.beginTransaction();
        transaction.hide(myTicketsFragment);
        transaction.hide(notificationFragment);
        transaction.hide(infoFragment);

        transaction.commit();
        navigationView.setCheckedItem(R.id.nav_main);
        setTitle("Главное");

        Intent intent = getIntent();

        if(intent.hasExtra("activity")){
            if(intent.getStringExtra("activity").equals("notification")){
                transaction = fm.beginTransaction();
                transaction.hide(mainFragment);
                transaction.hide(myTicketsFragment);
                transaction.hide(infoFragment);

                transaction.show(notificationFragment);
                transaction.commit();
                setTitle("Уведомление");
            }
        }

    }




    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        app_is_open = false;

        super.onDestroy();
    }


    @Override
    protected void onPause() {
        app_is_open = true;
        super.onPause();
    }

    public void goToMyTickets(){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction = fm.beginTransaction();
        transaction.hide(mainFragment);
        transaction.show(myTicketsFragment);

        transaction.commit();
        navigationView.setCheckedItem(R.id.nav_main);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean skip = false;

        if (id == R.id.nav_profile) {
            Intent infoDialog = new Intent(MainActivity.this, ProfileActivity.class);
            infoDialog.putExtra("mode", "new");
            startActivityForResult(infoDialog, 1);
            skip = true;
        } else if (id == R.id.nav_exit) {
            finish();
            skip = true;
        } else if (id == R.id.nav_log_out) {
            new Settings(this).saveLogin("");
            new Settings(this).savePassword("");
            new Settings(this).savePassword_2("");

            Intent iFinanceActivity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(iFinanceActivity);
            finish();
            skip = true;
        }

        if(!skip) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(mainFragment);
            transaction.hide(myTicketsFragment);
            transaction.hide(notificationFragment);
            transaction.hide(infoFragment);

            if (id == R.id.nav_main) {
                // Handle the camera action
                selected_item = 1;
                transaction.show(mainFragment);
                setTitle("Главное");
            } else if (id == R.id.nav_my_ticket) {
                selected_item = 2;
                transaction.show(myTicketsFragment);
                myTicketsFragment.setMyFlightList();
                setTitle("Мои билеты");
            } else if (id == R.id.nav_notifs) {
                selected_item = 3;
                transaction.show(notificationFragment);
                notificationFragment.setNotifList();
                setTitle("Уведомление");
            } else if (id == R.id.nav_info) {
                selected_item = 4;
                transaction.show(infoFragment);
                setTitle("Информация");
            } else if (id == R.id.nav_profile) {
                Intent infoDialog = new Intent(MainActivity.this, ProfileActivity.class);
                infoDialog.putExtra("mode", "new");
                startActivityForResult(infoDialog, 1);
            } else if (id == R.id.nav_exit) {
                finish();
            } else if (id == R.id.nav_log_out) {
                new Settings(this).saveLogin("");
                new Settings(this).savePassword("");
                new Settings(this).savePassword_2("");

                Intent iFinanceActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(iFinanceActivity);
                finish();
            }

            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Connection", "Rezult came right here 1" );

        // если пришло ОК
        if (resultCode == Activity.RESULT_OK) {
                    if (data.hasExtra("activity")) {
                        if (data.getStringExtra("activity").equals("TicketForm")) {
                            mainFragment.setMyFlightList();
                            myTicketsFragment.setMyFlightList();
                        }else if (data.getStringExtra("activity").equals("Profile")) {
                            if (data.getStringExtra("action").equals("deleted")) {
                                Intent iFinanceActivity = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(iFinanceActivity);
                            }
                        }
                    }
                }
    }

}
