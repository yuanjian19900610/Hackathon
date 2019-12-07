package com.hacksthon.team.manager;

import android.content.Context;
import android.media.MediaPlayer;

import com.hacksthon.team.HackathonClientMainActivity;
import com.hacksthon.team.R;

/**
 * <pre>
 * com.hacksthon.team.manager
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
 * Created by scott on 2019-12-07.
 *
 * *-------------------------------------------------------------------*
 *  </pre>
 */
public class MediaPlayerManager {

    //    private void play(Context context){
//        MediaPlayer player = new MediaPlayer.create(context,);
//    }
    public static void playerSound(Context context,int soundId) {
        final MediaPlayer player = new MediaPlayer().create(context, soundId);
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });
    }


}
