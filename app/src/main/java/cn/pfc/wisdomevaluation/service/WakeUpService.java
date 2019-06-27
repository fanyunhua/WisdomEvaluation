package cn.pfc.wisdomevaluation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import cn.pfc.wisdomevaluation.R;
import cn.pfc.wisdomevaluation.api.Listen;
import cn.pfc.wisdomevaluation.api.Speak;
import cn.pfc.wisdomevaluation.api.WakeUp;
import cn.pfc.wisdomevaluation.ui.window.FloatingWindow;
import cn.pfc.wisdomevaluation.util.PlayerWakeUp;

/**
 * @author fyh
 * @date 2019.6.24
 *
 * */
public class WakeUpService extends Service  implements
        WakeUp.OnWakeUp,Listen.OnListen,FloatingWindow.OnActionListener,Speak.OnSpeak{
    WakeUp wakeUp;
    PlayerWakeUp playerWakeUp;
    FloatingWindow floatingWindow;
    Speak speak;
    Context context;
    Listen listen;
    //语音识别对象
    private SpeechRecognizer mIat;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        context = WakeUpService.this;
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        StringBuffer bf = new StringBuffer();
        //初始化key
        bf.append("appid=" + getResources().getString(R.string.app_id));
        bf.append(",");
        //初始化
        SpeechUtility.createUtility(WakeUpService.this, bf.toString());
        Log.d(getPackageName(), "初始化成功");

        //加载语音唤醒
        wakeUp = new WakeUp(context);
        wakeUp.setOnWakeUp(this);
        //初始化声音提示对象
        playerWakeUp = new PlayerWakeUp(context);
        //悬浮窗
        floatingWindow = new FloatingWindow(context);
        floatingWindow.setOnActionListener(this);
        //语音合成
        speak = new Speak(context);
        speak.setOnSpeak(this);
        //语音听写
        listen = new Listen(context);
//
//        listen.getmIat().startListening(listen.getRecognizerListener());
        listen.setOnListen(this);

    }
    /**
     * @Description 语音唤醒成功的操作
     * @see WakeUp
     * */
    @Override
    public void onWakeOK() {
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0)
                {
                    //1.播放提示音表示已唤醒
                    playerWakeUp.listenStar();
                    //2.显示悬浮窗
//              floatingWindow.addView();
                    floatingWindow.show();
                    //3.合成语音提示
                    speak.startSpeaking("我在");
                }
                else
                {
                    //4.开启听写功能，等待用户说话
                    listen.getmIat().startListening(listen.getRecognizerListener());
                }

            }
        };
        new Thread(()->
        {
            try {
                handler .sendEmptyMessage(0);
                Thread.sleep(800);
                handler.sendEmptyMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        Log.e("speak result",recognizerResult.getResultString());
        //识别结果
        speak.startSpeaking("好的呢");
        floatingWindow.getmFloatingWindowTextView().setText(recognizerResult.getResultString());
    }

    @Override
    public void onEndOfSpeech() {
        //隐藏掉悬浮窗
        floatingWindow.removeView();
        playerWakeUp.listenOver();
    }

    @Override
    public void onButtonClick() {
        //按钮点击
        playerWakeUp.listenStar();
        listen.getmIat().startListening(listen.getRecognizerListener());
    }

    @Override
    public void onTextViewChanged(TextView textView) {
        //设置文字
    }

    //语音合成完成
    @Override
    public void onSpeakCompleted(SpeechError speechError) {
        Toast.makeText(context,"speak completed",Toast.LENGTH_SHORT).show();
    }
}
