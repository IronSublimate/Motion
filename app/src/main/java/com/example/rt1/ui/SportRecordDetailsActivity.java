package com.example.rt1.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.example.rt1.R;
import com.example.rt1.commmon.bean.PathRecord;
import com.example.rt1.commmon.bean.TabEntity;
import com.example.rt1.BaseActivity;
import com.example.rt1.ui.fragment.SportRecordDetailsFragment;
import com.example.rt1.ui.fragment.SportRecordDetailsMapFragment;
import com.example.rt1.ui.fragment.SportRecordDetailsSpeedFragment;
import com.example.rt1.ui.weight.AMapScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 描述: 运动记录详情
 * 作者: james
 * 日期: 2019/2/27 15:10
 * 类名: SportRecordDetailsActivity
 */
public class SportRecordDetailsActivity extends BaseActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp)
    AMapScrollViewPager mViewPager;

    private final ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private final String[] mTitles = {"轨迹", "详情", "配速"};

    private final ArrayList<Fragment> mFragments = new ArrayList<Fragment>() {{
        add(new SportRecordDetailsMapFragment());
        add(new SportRecordDetailsFragment());
        add(new SportRecordDetailsSpeedFragment());
    }};

    private PathRecord pathRecord = null;

    public static String SPORT_DATA = "SPORT_DATA";

    public static void StartActivity(Activity activity, PathRecord pathRecord) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SPORT_DATA, pathRecord);
        intent.putExtras(bundle);
        intent.setClass(activity, SportRecordDetailsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sport_record_details;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        tvTitle.setText("运动记录");

        if (getIntent().hasExtra(SPORT_DATA)) {
            pathRecord = getIntent().getParcelableExtra(SPORT_DATA);
        } else {
            ToastUtils.showShort("参数错误!");
            finish();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(SPORT_DATA, pathRecord);
        for (int i = 0, size = mTitles.length; i < size; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
            mFragments.get(i).setArguments(bundle);
        }

        commonTabLayout.setTabData(mTabEntities);

        //设置缓存页面数量，默认为2，防止地图显示重载丢失已设置的数据
        mViewPager.setOffscreenPageLimit(4);

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mViewPager.setCurrentItem(0);
    }

    @Override
    public void initListener() {
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
