package com.football.predictions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(com.football.predictions.R.anim.slide_in, R.anim.slide_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Previous Matches");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        navigationView.setCheckedItem(R.id.previousmatches);

        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {
            previous previous=new previous();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, previous, "previous").commit();
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInterstitialAd();
                }
            },18000);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            //Toast.makeText(getBaseContext(), "not ready",Toast.LENGTH_LONG).show();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "This App Has Helped me win thousands on SportPesa.\n" +
                    "It gives daily SURE BETS FOR FREE.\n" +
                    "Download it here; \n" +
                    "https://play.google.com/store/apps/details?id=com.football.predictions\n";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Football Predictions");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Share Football Predictions app With Friends"));

            return true;
        }else if (id == R.id.action_about) {

            if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
            else {   startActivity(new Intent(MainActivity.this,about.class));   }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            Fragment fragment=getSupportFragmentManager().findFragmentByTag("home");
            if(fragment==null){
                if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
                else {
                    setTitle("Best Predictions");
                    home home = new home();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main, home, "home").commit();
                }
            }
        } else if (id == R.id.todaysmatches) {
            Fragment fragment=getSupportFragmentManager().findFragmentByTag("todays");
            if(fragment==null){
                if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
                else {
                    setTitle("Today's Matches");
                    todays Todays = new todays();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main, Todays, "todays").commit();
                }
            }
        } else if (id == R.id.previousmatches) {
            Fragment fragment=getSupportFragmentManager().findFragmentByTag("previous");
            if(fragment==null){
                if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
                else {
                    setTitle("Previous Matches");
                    previous Previous = new previous();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main, Previous, "previous").commit();
                }
            }
        } else if (id == R.id.nav_rateus) {


            if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
            else {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }


        }  else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "This App Has Helped me win thousands on SportPesa.\n" +
                    "It gives daily SURE BETS FOR FREE.\n" +
                    "Download it here; \n" +
                    "https://play.google.com/store/apps/details?id=com.football.predictions\n";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Football Predictions");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Share Football Predictions app With Friends"));


        } else if (id == R.id.nav_about) {
            if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
            else {
                startActivity(new Intent(MainActivity.this, about.class));
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showInterstitialAd() {
        interstitialAd = new InterstitialAd(this, "342304149587187_342506506233618");
        interstitialAd.setAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                super.onError(ad, adError);
                if(adError.getErrorCode()==AdError.NO_FILL_ERROR_CODE){
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showInterstitialAd();
                        }
                    },30000);
                }
                Log.d("awesome","Main activity interestitial ad error: "+adError.getErrorCode()+":-"+adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                try{
                    interstitialAd.show();
                }catch (Exception ignored){}

                Log.d("awesome","Main activity interestitial ad loaded: "+ad);
            }

            @Override
            public void onAdClicked(Ad ad) {
                super.onAdClicked(ad);
                Log.d("awesome","Main activity interestitial ad clicked: "+ad);
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
                Log.d("awesome","Main activity interestitial ad displayed: "+ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
                Log.d("awesome","Main activity interestitial ad dismissed: "+ad);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                super.onLoggingImpression(ad);
                Log.d("awesome","Main activity interestitial ad impression: "+ad);
            }
        });
        interstitialAd.loadAd();
    }


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }


    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setIcon(R.drawable.ic_error);
        builder.setTitle("Internet Connection Lost");
        builder.setMessage("You need to have Mobile Data or wifi to access this.");

        builder.setPositiveButton("REMAIN", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                System.exit(0);
            }
        });
        return builder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(interstitialAd!=null){
            interstitialAd.destroy();
        }

    }
}