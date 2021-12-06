package com.example.rt1.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.rt1.R;
import com.example.rt1.commmon.bean.PathRecord;
import com.example.rt1.sport_motion.MotionUtils;
import com.example.rt1.BaseFragment;
import com.example.rt1.ui.SportRecordDetailsActivity;

import java.text.DecimalFormat;

import butterknife.BindView;

/**
 * 描述: 运动记录详情-详情
 * 作者: james
 * 日期: 2019/2/27 15:25
 * 类名: SportRecordDetailsFragment
 */
public class SportRecordDetailsFragment extends BaseFragment {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSpeed)
    TextView tvSpeed;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDistribution)
    TextView tvDistribution;

    private PathRecord pathRecord = null;

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final DecimalFormat intFormat = new DecimalFormat("#");

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sportrecorddetails;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pathRecord = bundle.getParcelable(SportRecordDetailsActivity.SPORT_DATA);
        }

        if (null != pathRecord) {
            tvDistance.setText(decimalFormat.format(pathRecord.getDistance() / 1000d));
            tvDuration.setText(MotionUtils.formatseconds(pathRecord.getDuration()));
            tvSpeed.setText(decimalFormat.format(pathRecord.getSpeed()));
            tvDistribution.setText(decimalFormat.format(pathRecord.getDistribution()));
        }

    }

    @Override
    public void initListener() {

    }
}
