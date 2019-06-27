package cn.pfc.wisdomevaluation.api;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;
import static com.iflytek.cloud.SpeechConstant.TYPE_LOCAL;

/**
 * @author fyh
 * @date 2019.6.25
 * @Description 语音识别
 * */
public class Listen implements RecognizerListener{
    private Context context;
    private SpeechRecognizer mIat;

    public Listen(){};
    RecognizerListener recognizerListener;
    OnListen onListen;
    public Listen(Context context) {
        this.context = context;
        init();
    }
    private void init() {
        mIat = SpeechRecognizer.createRecognizer(context,null);
        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIat.setParameter( SpeechConstant.CLOUD_GRAMMAR, null );
        mIat.setParameter( SpeechConstant.SUBJECT, null );
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "plain");
        //此处engineType为“local” //离线听写
        mIat.setParameter( SpeechConstant.ENGINE_TYPE, TYPE_LOCAL);
        //在线不需要设置资源目录
        mIat.setParameter(ResourceUtil.ASR_RES_PATH,getResourPath());
        //设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        //取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "4000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,"0");
        recognizerListener = this;
//        mIat.startListening(recognizerListener);

    }

    //获取离线资源包
    private String getResourPath() {
        StringBuffer tempBuffer = new StringBuffer();
        tempBuffer.append(
                ResourceUtil.generateResourcePath(context,
                        ResourceUtil.RESOURCE_TYPE.assets,"iat/common.jet"));
        tempBuffer.append(";");

        tempBuffer.append(
                ResourceUtil.generateResourcePath(context,
                        ResourceUtil.RESOURCE_TYPE.assets,"iat/sms_16k.jet"));
        return tempBuffer.toString();
    }

    public void setOnListen(OnListen onListen) {
        this.onListen = onListen;
    }

    public RecognizerListener getRecognizerListener() {
        return recognizerListener;
    }

    public SpeechRecognizer getmIat() {
        return mIat;
    }
    @Override
    public void onVolumeChanged(int i, byte[] bytes) {
        Log.e("listen onResult","onVolumeChanged");
    }

    @Override
    public void onBeginOfSpeech() {
        Log.e("listen onResult","onBeginOfSpeech");
    }

    @Override
    public void onEndOfSpeech() {
        Log.e("listen onResult","onEndOfSpeech");
        if(onListen!=null)
        {
            onListen.onEndOfSpeech();
        }
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        Log.e("listen onResult",recognizerResult.getResultString());
        if(onListen!=null)
        {
            onListen.onResult(recognizerResult,b);
        }
    }

    @Override
    public void onError(SpeechError speechError) {
        Log.e("listen onResult","onError");
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        Log.e("listen onResult","onEvent");
    }
    public interface OnListen
    {
        void onResult(RecognizerResult recognizerResult,boolean b);
        void onEndOfSpeech();
    }
}
