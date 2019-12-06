package com.hacksthon.team.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hacksthon.team.R;
import com.hacksthon.team.event.DeviceEvent.DeviceInfo;
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
        public void confirmOrder(Long id, Integer status);
    }


    @Override
    protected void convert(BaseViewHolder helper, final DeviceInfo item) {

        helper.setText(R.id.tv_address, item.deviceIp);
        helper.setText(R.id.tv_paper_status, item.deviceInfo);
        helper.setText(R.id.tv_device_mac_address, item.deviceMac);

    }
}


