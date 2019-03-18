package com.android.spaciocrm.util.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.spaciocrm.home.fragments.referrals.FragIncoming;
import com.android.spaciocrm.home.fragments.referrals.FragOutgoing;

/**
 * Created by freshfuturesmy on 12/01/18.
 */

public class ReferralFragmentPageAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = new String[]{"Incoming", "Outgoing"};

    public ReferralFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {

            case 0:
                return new FragIncoming();

            case 1:
                return new FragOutgoing();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
