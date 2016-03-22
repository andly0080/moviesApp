package com.example.android.mymoiveapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.mymoiveapp.utilities.Utility;


public class MainActivity extends ActionBarActivity {

    private String mSort;
    private static final String movieFragTag ="movieFragTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSort = Utility.getPreferredSort(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieFragment(),movieFragTag)
                    .commit();
        }




    }


    @Override
    protected void onResume() {
        super.onResume();


       String sortType = Utility.getPreferredSort(this);

       if(sortType !=null && !sortType.equals(mSort))
       {

           //sortTye change! let fragment to update the weather info

           MovieFragment movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentByTag(movieFragTag);

           if(null !=movieFragment)
           {

               movieFragment.refreshMovieList();

           }


       }


      mSort = sortType;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */





}
