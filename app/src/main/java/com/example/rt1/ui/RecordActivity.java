package com.example.rt1.ui;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.example.rt1.R;
import com.example.rt1.commmon.bean.PathRecord;
import com.example.rt1.commmon.bean.SportMotionRecord;
import com.example.rt1.commmon.utils.LogUtils;
import com.example.rt1.commmon.utils.MySp;
import com.example.rt1.db.DataManager;
import com.example.rt1.db.RealmHelper;
import com.example.rt1.sport_motion.MotionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecordActivity extends AppCompatActivity {


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sport_achievement)
    LinearLayout sport_achievement;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private DataManager dataManager = null;
    private final List<PathRecord> sportList = new ArrayList<>(0);
    private SportAdapter adapter;


    public int getLayoutId() {
        return R.layout.activity_record;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
       // upDateUI();
    }

    public void initData(Bundle savedInstanceState) {

        dataManager = new DataManager(new RealmHelper());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.line)));
        adapter = new SportAdapter(R.layout.adapter_sport, sportList);
        mRecyclerView.setAdapter(adapter);

       getSports();

    }
    @SuppressLint("NotifyDataSetChanged")
    private void getSports() {
        try {
            List<SportMotionRecord> records = dataManager.queryRecordList(Integer.parseInt(SPUtils.getInstance().getString(MySp.USERID)));
            if (null != records) {

                sportList.clear();
                adapter.notifyDataSetChanged();

                for (SportMotionRecord record : records) {
                    PathRecord pathRecord = new PathRecord();
                    pathRecord.setId(record.getId());
                    pathRecord.setDistance(record.getDistance());
                    pathRecord.setDuration(record.getDuration());
                    pathRecord.setPathline(MotionUtils.parseLatLngLocations(record.getPathLine()));
                    pathRecord.setStartpoint(MotionUtils.parseLatLngLocation(record.getStratPoint()));
                    pathRecord.setEndpoint(MotionUtils.parseLatLngLocation(record.getEndPoint()));
                    pathRecord.setStartTime(record.getmStartTime());
                    pathRecord.setEndTime(record.getmEndTime());
                    pathRecord.setSpeed(record.getSpeed());
                    pathRecord.setDistribution(record.getDistribution());
                    pathRecord.setDateTag(record.getDateTag());
                    sportList.add(pathRecord);
                }
                if (sportList.isEmpty())
                    sport_achievement.setVisibility(View.GONE);
                else
                    sport_achievement.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            } else {
                sport_achievement.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.e("获取运动数据失败", e);
            sport_achievement.setVisibility(View.GONE);
        }
    }

    public void initListener() {

        adapter.setOnItemClickListener((adapter, view, position) -> {

            SportRecordDetailsActivity.StartActivity(this, sportList.get(position));
        });
    }


    //recyclerView设置间距
    protected static class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mSpace;

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = mSpace;
            outRect.left = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            } else {
                outRect.top = 0;
            }

        }

        SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

//    private void loadSportData() {
//        try {
//            List<SportMotionRecord> records = dataManager.queryRecordList(Integer.parseInt(SPUtils.getInstance().getString(MySp.USERID, "0")));
//            if (null != records) {
//                Map<String, Calendar> map = new HashMap<>();
//                for (SportMotionRecord record : records) {
//                    String dateTag = record.getDateTag();
//                    String[] strings = dateTag.split("-");
//                    int year = Integer.parseInt(strings[0]);
//                    int month = Integer.parseInt(strings[1]);
//                    int day = Integer.parseInt(strings[2]);
//                    map.put(getSchemeCalendar(year, month, day, 0xFFCC0000, "记").toString(),
//                            getSchemeCalendar(year, month, day, 0xFFCC0000, "记"));
//                }
//                //此方法在巨大的数据量上不影响遍历性能，推荐使用
//                mCalendarView.setSchemeDate(map);
//            }
//        } catch (Exception e) {
//            LogUtils.e("获取运动数据失败", e);
//        }
//    }
    @Override
    protected void onDestroy() {
        if (null != dataManager)
            dataManager.closeRealm();
        super.onDestroy();
    }


}