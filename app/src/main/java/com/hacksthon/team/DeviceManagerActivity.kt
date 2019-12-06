package com.hacksthon.team

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hacksthon.team.adapter.DeviceItemBeanAdapter
import com.hacksthon.team.bean.DeviceInfo
import com.hacksthon.team.event.DeviceEvent
import com.hacksthon.team.utils.RecyclerViewUtil
import kotlinx.android.synthetic.main.activity_device.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 设备管理
 */
class DeviceManagerActivity : AppCompatActivity() {

    var list = ArrayList<DeviceInfo>()
    var adapter = DeviceItemBeanAdapter(R.layout.layout_device_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        registerEventBus()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterEventBus()
    }

    override fun onResume() {
        super.onResume()


        RecyclerViewUtil.initRecyclerViewV(this, rc_list, adapter)
        adapter.setNewData(list)
        adapter.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: DeviceEvent) {
        Log.i("qinglin", "event >>>>> ")
        val deviceInfo = event.mDeviceInfo
        Log.i("qinglin", "event >>>>> " + deviceInfo)

        if (deviceInfo != null) {

//            var findDeviceInfo:DeviceEvent.DeviceInfo? = null
//
//            for (i in 0..list.size) {
//                var item = list[i]

//                if (item.deviceMac.equals(deviceInfo.deviceMac)) {
//                    findDeviceInfo = item
//                    item = deviceInfo
//                }
//            }
//
//            if (findDeviceInfo == null) {
//                list.add(deviceInfo)
//            }
            list.add(deviceInfo)
            adapter.notifyDataSetChanged()
        }
    }

    protected fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    protected fun unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

}