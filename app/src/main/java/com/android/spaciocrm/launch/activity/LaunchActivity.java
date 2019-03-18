package com.android.spaciocrm.launch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.spaciocrm.R;
import com.android.spaciocrm.launch.fragments.fragLaunchFive;
import com.android.spaciocrm.launch.fragments.fragLaunchFour;
import com.android.spaciocrm.launch.fragments.fragLaunchOne;
import com.android.spaciocrm.launch.fragments.fragLaunchSix;
import com.android.spaciocrm.launch.fragments.fragLaunchThree;
import com.android.spaciocrm.launch.fragments.fragLaunchTwo;
import com.android.spaciocrm.login.activity.LoginActivity;
import com.viewpagerindicator.CirclePageIndicator;

public class LaunchActivity extends AppCompatActivity {

    static SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    Button getStarted;
    private LinearLayout circleBack;
    Context ctx;
    ImageView imgStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        ctx = LaunchActivity.this;

        circleBack = findViewById(R.id.start_image_layout);
        imgStart = findViewById(R.id.frag_one_image);

        getStarted = findViewById(R.id.get_started);

        mSectionsPagerAdapter = new SectionsPagerAdapter(this.getSupportFragmentManager());

        mViewPager = findViewById(R.id.start_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        CirclePageIndicator cp = findViewById(R.id.indicator);

        cp.setFillColor(ContextCompat.getColor(this, R.color.colorPrimary));
        cp.setStrokeColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (cp != null) {
            cp.setViewPager(mViewPager);
        }

        mViewPager.setCurrentItem(0, true);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intent);
                LaunchActivity.this.finish();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.blue_theme_oval_background, null));
                    }
                    else {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.blue_theme_oval_background));
                    }

                    imgStart.setImageResource(R.drawable.ic_launch_spacio_tick);
                }

                if(position == 1) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.green_oval_background, null));
                    }
                    else {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.green_oval_background));
                    }

                    imgStart.setImageResource(R.drawable.ic_launch_phone_book);
                }

                if(position == 2) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.orange_oval_background, null));
                    }
                    else {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.orange_oval_background));
                    }

                    imgStart.setImageResource(R.drawable.ic_launch_event);
                }

                if(position == 3) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.blue_oval_background, null));
                    }
                    else {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.blue_oval_background));
                    }

                    imgStart.setImageResource(R.drawable.ic_launch_cart_product);
                }

                if(position == 4) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.red_oval_background, null));
                    }
                    else {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.red_oval_background));
                    }

                    imgStart.setImageResource(R.drawable.ic_launch_referral);
                }

                if(position == 5) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.purple_oval_background, null));
                    }
                    else {
                        circleBack.setBackground(ctx.getResources().getDrawable(R.drawable.purple_oval_background));
                    }

                    imgStart.setImageResource(R.drawable.ic_launch_line_chart);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fragmentManager) {

            super(fragmentManager);
        }

        public int getCount() {
            return 6;
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new fragLaunchOne();

                case 1:
                    return new fragLaunchTwo();

                case 2:
                    return new fragLaunchThree();

                case 3:
                    return new fragLaunchFour();

                case 4:
                    return new fragLaunchFive();

                case 5:
                    return new fragLaunchSix();

                default:
                    return null;
            }
        }
    }
}
