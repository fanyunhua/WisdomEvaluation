package cn.pfc.wisdomevaluation.util;

import android.content.Context;
import android.media.MediaPlayer;

import cn.pfc.wisdomevaluation.R;
import cn.pfc.wisdomevaluation.api.BaseApi;

public class PlayerWakeUp extends BaseApi {
    MediaPlayer mMediaPlayerStart,mMediaPlayerOver;
    public PlayerWakeUp(Context context) {
        super(context);

        mMediaPlayerStart=MediaPlayer.create(context, R.raw.record_start);
        mMediaPlayerOver=MediaPlayer.create(context, R.raw.record_over);

    }
    public void listenStar()
    {
        mMediaPlayerStart.start();
    }
    public void listenOver()
    {
        mMediaPlayerOver.start();
    }

    @Override
    public void init() {

    }
}
