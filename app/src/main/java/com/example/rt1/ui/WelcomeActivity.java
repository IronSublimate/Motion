package com.example.rt1.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.rt1.BaseActivity;
import com.example.rt1.MyApplication;
import com.example.rt1.R;
import com.example.rt1.commmon.utils.MySp;
import com.example.rt1.commmon.utils.UIHelper;
import com.example.rt1.ui.permission.PermissionHelper;
import com.example.rt1.ui.permission.PermissionListener;
import com.example.rt1.ui.weight.CountDownProgressView;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;

/**
 * 描述: 闪屏界面
 * 作者: james
 * 日期: 2019/2/25 16:20
 * 类名: SplashActivity
 */
public class WelcomeActivity extends BaseActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.im_url)
    ImageView imUrl;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.countdownProgressView)
    CountDownProgressView countDownProgressView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.versions)
    TextView versions;

    /**
     * 上次点击返回键的时间
     */
    private long lastBackPressed;
    /**
     * 上次点击返回键的时间
     */
    private static final int QUIT_INTERVAL = 3000;

    // 要申请的权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (ImmersionBar.hasNavigationBar(this)) {
            ImmersionBar.with(this).transparentNavigationBar().init();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        imUrl.setImageResource(R.drawable.start);

        versions.setText(UIHelper.getString(R.string.splash_appversionname, MyApplication.getAppVersionName()));

        countDownProgressView.setTimeMillis(2000);
        countDownProgressView.setProgressType(CountDownProgressView.ProgressType.COUNT_BACK);
        countDownProgressView.start();
    }

    @Override
    public void initListener() {
       // countDownProgressView.setOnClickListener(this::onClick);

        countDownProgressView.setProgressListener(progress -> {
            if (progress == 0) {
                // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 获取权限
                    PermissionHelper.requestPermissions(this, PERMISSIONS_STORAGE,
                            getResources().getString(R.string.app_name) + "需要获取存储、位置权限", new PermissionListener() {
                                @Override
                                public void onPassed() {
                                    startActivity();
                                }
                            });
                } else {
                    startActivity();
                }
            }
        });
    }

    public void startActivity() {
        if (SPUtils.getInstance().getBoolean(MySp.ISLOGIN)) {
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }
        countDownProgressView.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { // 表示按返回键 时的操作
                long backPressed = System.currentTimeMillis();
                if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                    lastBackPressed = backPressed;
                    ToastUtils.showShort("再按一次退出");
                } else {
                    if (countDownProgressView != null) {
                        countDownProgressView.stop();
                        countDownProgressView.clearAnimation();
                    }
                    moveTaskToBack(false);
                    MyApplication.closeApp(this);
                    finish();
                }
            }
        }
        return false;
    }

    private void onClick(View v) {

        countDownProgressView.stop();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 获取权限
            PermissionHelper.requestPermissions(this, PERMISSIONS_STORAGE, new PermissionListener() {
                @Override
                public void onPassed() {
                    startActivity();

                }
            });
        } else {
            startActivity();
        }
    }
}
