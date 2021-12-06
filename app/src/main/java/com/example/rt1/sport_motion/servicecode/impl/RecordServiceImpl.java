package com.example.rt1.sport_motion.servicecode.impl;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.example.rt1.commmon.utils.LogUtils;
import com.example.rt1.sport_motion.servicecode.RecordService;

/**
 * 记录运动信息的实现类
 */
public class RecordServiceImpl implements RecordService {

    private Context mContext;

    public RecordServiceImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void recordSport(LatLng latLng, String location) {
        LogUtils.d("保存定位数据 = " + latLng.latitude + ":" + latLng.longitude + "   " + location);
    }

}
