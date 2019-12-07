package com.hacksthon.team.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hacksthon.team.R;
import com.hacksthon.team.bean.DeviceInfo;
import java.util.List;

public class DeviceItemBeanAdapter extends BaseQuickAdapter<DeviceInfo, BaseViewHolder> {


    public DeviceItemBeanAdapter(int layoutResId, @Nullable List<DeviceInfo> data) {
        super(layoutResId, data);
    }

    public DeviceItemBeanAdapter(int layoutResId) {
        super(layoutResId);
    }


    public void setOnAdapterClick(OnAdapterClick onAdapterClick) {
        mOnAdapterClick = onAdapterClick;
    }

    OnAdapterClick mOnAdapterClick;

    public interface OnAdapterClick {

        public void onPlaySound(DeviceInfo info);
        public void onSwipeCard(DeviceInfo info);


    }


    @Override
    protected void convert(BaseViewHolder helper, final DeviceInfo item) {

        helper.setText(R.id.tv_address, item.deviceIp);
        helper.setText(R.id.tv_paper_status, item.deviceStatus);
        helper.setText(R.id.tv_device_mac_address, item.deviceMac);

        if (item.deviceStatus.equals("离线")){
            helper.getView(R.id.play_sound).setEnabled(false);
            helper.getView(R.id.swipe_card).setEnabled(false);
        } else {
            helper.getView(R.id.play_sound).setEnabled(true);
            helper.getView(R.id.swipe_card).setEnabled(true);
        }

        helper.setOnClickListener(R.id.play_sound, new OnClickListener() {
            @Override
            public void onClick(View v) {
           mOnAdapterClick.onPlaySound(item);
            }
        });

        helper.setOnClickListener(R.id.swipe_card, new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAdapterClick.onSwipeCard(item);
            }
        });

    }
}


