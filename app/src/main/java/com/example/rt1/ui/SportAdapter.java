package com.example.rt1.ui;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.rt1.R;
import com.example.rt1.commmon.bean.PathRecord;
import com.example.rt1.sport_motion.MotionUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 描述: 运动日历
 * 作者: james
 * 日期: 2019/2/27 14:02
 * 类名: SportCalendarAdapter
 */
public class SportAdapter extends BaseQuickAdapter<PathRecord, BaseViewHolder> {

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final DecimalFormat intFormat = new DecimalFormat("#");

    public SportAdapter(int layoutResId, @Nullable List<PathRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PathRecord item) {
        helper.setText(R.id.date, item.getDateTag());
        helper.setText(R.id.distance, decimalFormat.format(item.getDistance() / 1000d));
        helper.setText(R.id.duration, MotionUtils.formatseconds(item.getDuration()));

    }
}
