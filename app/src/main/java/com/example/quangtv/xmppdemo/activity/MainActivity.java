package com.example.quangtv.xmppdemo.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.example.quangtv.xmppdemo.fragment.FragmentContact;
import com.example.quangtv.xmppdemo.fragment.FragmentText;
import com.example.quangtv.xmppdemo.R;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by QuangTV on 11/5/15.
 */
public class MainActivity extends ActionBarActivity implements MaterialTabListener {
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    MaterialTabHost tabHost;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = this.getResources();
        // init toolbar (old action bar)

//        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(Color.WHITE);
//        this.setSupportActionBar(toolbar);


        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);
        // init view pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
        }
    }
    @Override
    public void onTabSelected(MaterialTab tab) {
// when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public Fragment getItem(int num) {
            switch (num) {
                case 0: return new FragmentText();
                case 1: return new FragmentContact();

                default: return null;
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "tab 1";
                case 1: return "tab 2";

                default: return null;
            }
        }
    }
    /*
    * It doesn't matter the color of the icons, but they must have solid colors
    */
    private Drawable getIcon(int position) {
        switch(position) {
            case 0:
                return res.getDrawable(R.mipmap.ic_person);
            case 1:
                return res.getDrawable(R.mipmap.ic_group);

        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        String abc = intent.getExtras().getString("to");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 65537) {
            if(resultCode == RESULT_OK){
                String to = data.getStringExtra("to");
                String body = data.getStringExtra("message");

                FragmentText fragmentText = (FragmentText) getSupportFragmentManager().getFragments().get(0);

                fragmentText.addData(to, body);
                fragmentText.setListAdapter();
            }
        }
    }
}