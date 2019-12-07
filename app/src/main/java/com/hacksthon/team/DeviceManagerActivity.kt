package com.hacksthon.team

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.hacksthon.team.adapter.DeviceItemBeanAdapter
import com.hacksthon.team.bean.CmdConstantType
import com.hacksthon.team.bean.DeviceInfo
import com.hacksthon.team.bean.ServerRep
import com.hacksthon.team.event.DeviceDisConnectEvent
import com.hacksthon.team.event.DeviceEvent
import com.hacksthon.team.event.DevicePayEvent
import com.hacksthon.team.event.DevicePlaySoundEvent
import com.hacksthon.team.manager.MediaPlayerManager
import com.hacksthon.team.manager.SocketServerManager
import com.hacksthon.team.utils.RecyclerViewUtil
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.InputDialog
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import kotlinx.android.synthetic.main.activity_device.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal


/**
 * 设备管理
 */
class DeviceManagerActivity : AppCompatActivity(), DeviceItemBeanAdapter.OnAdapterClick {

    var waitDialog : TipDialog? = null

    //刷卡
    override fun onSwipeCard(info: DeviceInfo) {


        InputDialog.show(this, "刷卡金额", "请输入金额", "确定")
                .setOnOkButtonClickListener(object : OnInputDialogButtonClickListener{
                    override fun onClick(baseDialog: BaseDialog?, v: View, inputStr: String): Boolean {
                        if (TextUtils.isEmpty(inputStr)) {
                               return false
                        }
                        val dc = BigDecimal.valueOf(inputStr.toDouble())

                        Thread(Runnable {
                            try {

                                if (SocketServerManager.cacheSocketMap.containsKey(info.deviceMac)) {
                                    val socketTemp = SocketServerManager.cacheSocketMap.get(info.deviceMac)
                                    if (socketTemp != null) {
                                        val out = socketTemp.getOutputStream()
                                        val input = socketTemp.getInputStream()
                                        val buffer = ByteArray(1024 * 4)
                                        var temp = 0


                                        val resp = ServerRep()
                                        resp.cmdType = CmdConstantType.CMD_PAY
                                        resp.info = "小龙坎火锅"
                                        resp.amount = dc
                                        resp.orderNo = System.currentTimeMillis().toString() + ""

                                        out.write(Gson().toJson(resp).toByteArray(charset("utf-8")))
                                        out.flush()

                                        runOnUiThread(Runnable {
                                           // WaitDialog.show(DeviceManagerActivity, "请稍候...");
                                           // ToastUtils.showShort("ddd")
                                            waitDialog = WaitDialog.show(this@DeviceManagerActivity, "请稍后...")

                                        })

                                        var mac = ""

//                        var read: Int = -1
//                        while (input.read().also { read = it } != -1) {
//
//                            val content = String(buffer, 0, read, charset("utf-8"))
//                            Log.i("qinglin.fan", "DeviceManagerActyivity >>> " + content)
//                            val deviceInfo = Gson().fromJson(content, DeviceInfo::class.java)
//
//                            Log.i("qinglin.fan", "card >>>" + deviceInfo)
//                            ToastUtils.showShort("收到刷卡绘制")
//
//                        }
                                    }
                                }
                            } catch (e :Exception) {
                                e.printStackTrace()

                            }
                        }).start()

                        return false

                    }

                } )









    }

    //播放声音
    override fun onPlaySound(info: DeviceInfo) {

        Thread(Runnable {
            try {

                if (SocketServerManager.cacheSocketMap.containsKey(info.deviceMac)) {
                    val socketTemp = SocketServerManager.cacheSocketMap.get(info.deviceMac)
                    if (socketTemp != null) {
                        val out = socketTemp.getOutputStream()
                        val input = socketTemp.getInputStream()
                        val buffer = ByteArray(1024 * 4)
                        var temp = 0


                        val resp = ServerRep()
                        resp.cmdType = CmdConstantType.CMD_PLAY_SOUND
                        resp.info = "播放声音"

                        out.write(Gson().toJson(resp).toByteArray(charset("utf-8")))
                        out.flush()

                        runOnUiThread(Runnable {
                            // WaitDialog.show(DeviceManagerActivity, "请稍候...");
                            // ToastUtils.showShort("ddd")
                           // waitDialog = WaitDialog.show(this@DeviceManagerActivity, "请稍后...")

                            ToastUtils.showShort("呼叫成功")
                        })

                        var mac = ""

//                        var read: Int = -1
//                        while (input.read().also { read = it } != -1) {
//
//                            val content = String(buffer, 0, read, charset("utf-8"))
//                            Log.i("qinglin.fan", "DeviceManagerActyivity >>> " + content)
//                            val deviceInfo = Gson().fromJson(content, DeviceInfo::class.java)
//
//                            Log.i("qinglin.fan", "card >>>" + deviceInfo)
//                            ToastUtils.showShort("收到刷卡绘制")
//
//                        }
                    }
                }
            } catch (e :Exception) {
                e.printStackTrace()

            }
        }).start()

    }


    var list = ArrayList<DeviceInfo>()
    var adapter = DeviceItemBeanAdapter(R.layout.layout_device_item)
    var serverManager: SocketServerManager? = null
    var runnable : Runnable? = null

    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.hide()
        }
        registerEventBus()
        serverManager = SocketServerManager.getInstance()
        serverManager!!.setEnable(true)
        serverManager!!.startScoketServer()
        adapter.setOnAdapterClick(this)

        // 设置IOS风格
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS
        // 设置亮色
        DialogSettings.theme = DialogSettings.THEME.LIGHT
        DialogSettings.tipTheme = DialogSettings.THEME.LIGHT
//
//        handler = Handler()
//        runnable = Runnable {
//            val iter = list.iterator()
//            while (iter.hasNext()) {
//                val device = iter.next()
//                val mac = device.deviceMac
//                if (isSocketClosed(mac)) {
//                    device.deviceStatus = "离线"
//                } else {
//                    if (SocketServerManager.cacheTimes.containsKey(mac)) {
//                        val cacheTime = SocketServerManager.cacheTimes.get(mac)
//                        if (System.currentTimeMillis() - cacheTime!! > (1000*60) ) {
//                            Log.i("qinglin.fan", "该设备已经离线" + device.deviceInfo)
//                            device.deviceStatus = "离线"
//                        } else {
//                            device.deviceStatus = "在线"
//                        }
//                    }
//                }
//            }
//            adapter.notifyDataSetChanged()
//            handler!!.postDelayed(runnable, 60*1000)
//        }
//        handler!!.postDelayed(runnable, 60*1000)
    }

    private fun isSocketClosed(key: String) : Boolean{
        if (SocketServerManager.cacheSocketMap.containsKey(key)) {
            val socketTemp = SocketServerManager.cacheSocketMap.get(key)
            return socketTemp == null || socketTemp.isClosed
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterEventBus()
        handler!!.removeCallbacks(runnable)
        serverManager!!.stopSocketServer()
    }

    override fun onResume() {
        super.onResume()
        RecyclerViewUtil.initRecyclerViewV(this, rc_list, adapter)
        adapter.setNewData(list)
        adapter.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: DeviceEvent) {
        Log.i("qinglin.fan", "onRefreshEvent >>" + event.mDeviceInfo)

        val deviceInfo = event.mDeviceInfo
        var position = -1

        if (deviceInfo != null) {

            var find = false
            if (list.size > 0) {
                for (i in 0..list.size-1) {
                    var item = list[i]
                    if (item.deviceMac.equals(deviceInfo.deviceMac)) {
                      //  item = deviceInfo
                        position = i
                        find = true
                        break
                    }
                }
                if (!find) {
                    list.add(deviceInfo)
                } else {
                    list[position] = deviceInfo
                }
            } else {
                list.add(deviceInfo)
            }
            adapter.notifyDataSetChanged()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDisconnectEvent(event: DeviceDisConnectEvent) {

        Log.i("qinglin.fan", "onRefreshDisconnectEvent >>")
        val mac = event.mac
        var position = -1

        if (mac != null) {

            var find = false
            if (list.size > 0) {
                for (i in 0..list.size) {
                    var item = list[i]
                    if (item.deviceMac.equals(mac)) {
                        position = i
                        find = true
                        item.deviceStatus = "离线"
                        break
                    }
                }
               // if (find) list.removeAt(position)

            }
            adapter.notifyDataSetChanged()
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayEvent(event: DevicePlaySoundEvent) {
        MediaPlayerManager.playerSound(this@DeviceManagerActivity, R.raw.pos_sound)

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPayEvent(event: DevicePayEvent) {
        if (waitDialog != null) {
            waitDialog!!.doDismiss()
        }
        if (event != null && event.mDeviceInfo != null) {

            if (event.mDeviceInfo.cmdType == CmdConstantType.CMD_PAY_SUCCESS) {
                MessageDialog.show(this@DeviceManagerActivity ,"支付结果", "支付成功", "确定")
            } else {
                MessageDialog.show(this@DeviceManagerActivity ,"支付结果", "支付失败", "确定")
            }
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