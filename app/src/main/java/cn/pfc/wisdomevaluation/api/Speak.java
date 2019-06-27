package cn.pfc.wisdomevaluation.api;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

import static com.iflytek.cloud.SpeechConstant.TYPE_LOCAL;

/**
 * @author fyh
 * @date 2019.6.25
 * @Description 语音合成
 * */
public class Speak implements SynthesizerListener {
    private Context context;
    private SpeechSynthesizer mTts;
    OnSpeak onSpeak;
    SynthesizerListener synthesizerListener;
    public Speak(Context context) {
        this.context = context;
        init();
    }
    private void init() {
        synthesizerListener = this;
        mTts = SpeechSynthesizer.createSynthesizer(context,null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, TYPE_LOCAL); //设置云端

//        mTts.setParameter(ResourceUtil.GRM_BUILD_PATH,context.getPackageName()+"/" );
        mTts.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC); //设置本地

        //NONE 找不到资源   //在线不需要设置资源目录
        mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());

        Log.e("packge name",context.getPackageName()+"/asset/tts/common.jet");
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "40");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "100");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

    }

    public void setOnSpeak(OnSpeak onSpeak) {
        this.onSpeak = onSpeak;
    }

    public void startSpeaking(String message)
    {
        mTts.startSpeaking(message,synthesizerListener);
    }
    // 获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        // 合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+"common.jet"));
        tempBuffer.append(";");
        // 发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+"xiaoyan.jet"));
        return tempBuffer.toString();
    }
    public SpeechSynthesizer getmTts() {
        return mTts;
    }

    public OnSpeak getOnSpeak() {
        return onSpeak;
    }

    @Override
    public void onSpeakBegin() {

    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {
        if(onSpeak!=null)
        {
            onSpeak.onSpeakCompleted(speechError);
        }
        else
        {
            throw new RuntimeException("Must implements Speak.OnSpeak.onSpeakCompleted");
        }
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }
    public interface OnSpeak{
        void onSpeakCompleted(SpeechError speechError);
    }
}
