package com.example.rt1.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.example.rt1.R;
import com.example.rt1.commmon.bean.UserAccount;
import com.example.rt1.commmon.utils.Conn;
import com.example.rt1.commmon.utils.LogUtils;
import com.example.rt1.commmon.utils.UIHelper;
import com.example.rt1.commmon.utils.Utils;
import com.example.rt1.db.DataManager;
import com.example.rt1.db.RealmHelper;
import com.example.rt1.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SigninActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.et_psd)
    EditText etPsd;
    @BindView(R.id.et_checkPsd)
    EditText etCheckPsd;
    @BindView(R.id.bt_regist)
    Button btRegist;

    private String code = "-1";

    private DataManager dataManager = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_signin;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        dataManager = new DataManager(new RealmHelper());

        LogUtils.d("已注册的账号", new Gson().toJson(dataManager.queryAccountList()) + "");

        chronometer.setText("获取验证码");

    }

    public void yzmStart() {
        chronometer.setTag(SystemClock.elapsedRealtime() / 1000 + 60);
        chronometer.setText("(60)重新获取");
        chronometer.start();
        chronometer.setEnabled(false);

        this.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void initListener() {
        chronometer.setOnChronometerTickListener(chronometer -> {
            long time = (Long) chronometer.getTag() - SystemClock.elapsedRealtime() / 1000;
            if (time > 0) {
                chronometer.setText(UIHelper.getString(R.string.chronometer_time, time));
            } else {
                chronometer.setText("重新获取");
                chronometer.stop();
                chronometer.setEnabled(true);
            }
        });
    }

    @OnClick({R.id.container, R.id.rlBadk, R.id.chronometer, R.id.bt_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.container:
                // 设置点击影藏输入法
                hideSoftKeyBoard();
                break;
            case R.id.rlBadk:
                finish();
                break;
            case R.id.chronometer:
                String phone = etAccount.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(SigninActivity.this,"请输入11位手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Utils.isMobile(phone)) {
                    Toast.makeText(SigninActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();

                    return;
                }

                // 先影藏输入法
                hideSoftKeyBoard();

                yanZhengMa();
                break;
            case R.id.bt_regist:
                hideSoftKeyBoard();
                if (TextUtils.isEmpty(etAccount.getText())) {
                    Toast.makeText(SigninActivity.this,"请输入11位手机号码",Toast.LENGTH_SHORT).show();
                } else if (!Utils.isMobile(etAccount.getText().toString())) {
                    Toast.makeText(SigninActivity.this,"请输入正确的手机号码!",Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etCode.getText().toString())) {
                    Toast.makeText(SigninActivity.this,"验证码不可以为空!",Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(etCode.getText(), code)) {
                    Toast.makeText(SigninActivity.this,"请输入正确的验证码!",Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etPsd.getText().toString())) {
                    Toast.makeText(SigninActivity.this,"密码不可以为空!",Toast.LENGTH_SHORT).show();
                } else if (etPsd.getText().length() < 6) {
                    Toast.makeText(SigninActivity.this,"请输入大于六位数的密码!",Toast.LENGTH_SHORT).show();;
                } else if (TextUtils.isEmpty(etCheckPsd.getText().toString())) {
                    Toast.makeText(SigninActivity.this,"校验密码不可以为空!",Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(etPsd.getText(), etCheckPsd.getText())) {
                    Toast.makeText(SigninActivity.this,"两次密码输入不一致，请检验!",Toast.LENGTH_SHORT).show();
                } else {
                    btRegist.setEnabled(false);
                    regist();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void yanZhengMa() {
        showLoadingView();
        new Handler().postDelayed(() -> {
            dismissLoadingView();
            int numcode = (int) ((Math.random() * 9 + 1) * 100000);
            code = numcode + "";
            yzmStart();
            Toast.makeText(SigninActivity.this,"验证获取成功！",Toast.LENGTH_SHORT).show();
            etCode.setText(code);
        }, Conn.Delayed);
    }

    /**
     * 注册
     */
    public void regist() {
        showLoadingView();
        new Handler().postDelayed(() -> {
            dismissLoadingView();
            btRegist.setEnabled(true);
            if (dataManager.checkAccount(etAccount.getText().toString())) {
                Toast.makeText(SigninActivity.this,"账号已存在！",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SigninActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                UserAccount userAccount = new UserAccount();
                userAccount.setAccount(etAccount.getText().toString());
                userAccount.setPsd(etPsd.getText().toString());
                dataManager.insertAccount(userAccount);
                finish();
            }
        }, Conn.Delayed);
    }

    @Override
    protected void onDestroy() {
        if (null != dataManager)
            dataManager.closeRealm();
        super.onDestroy();
    }
}
