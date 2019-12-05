package com.hacksthon.team;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.hacksthon.team.bean.SocketConfig;
import com.hacksthon.team.manager.SocketServerManager;

/**
 * <pre>
 * com.hacksthon.team
 *
 * *-------------------------------------------------------------------*
 *     scott
 *                                    江城子 . 程序员之歌
 *     /\__/\
 *    /`    '\                     十年生死两茫茫，写程序，到天亮。
 *  ≈≈≈ 0  0 ≈≈≈ Hello world!          千行代码，Bug何处藏。
 *    \  --  /                     纵使上线又怎样，朝令改，夕断肠。
 *   /        \                    领导每天新想法，天天改，日日忙。
 *  /          \                       相顾无言，惟有泪千行。
 * |            |                  每晚灯火阑珊处，夜难寐，加班狂。
 *  \  ||  ||  /
 *   \_oo__oo_/≡≡≡≡≡≡≡≡o
 *
 * Created by scott on 2019/12/5.
 *
 * *-------------------------------------------------------------------*
 *  </pre>
 */
public class SocketServer  extends IntentService{

    public static final int PORT=311904;
    public static String IP_ADDRESS="";


    public SocketServer() {
        super(SocketServer.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SocketServerManager serverManager =SocketServerManager.getInstance();
        serverManager.setSocketConfig(new SocketConfig(PORT));
        serverManager.setEnable(true);
        serverManager.startScoketServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketServerManager.getInstance().stopSocketServer();
    }
}
