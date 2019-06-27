package cn.pfc.wisdomevaluation.api;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import cn.pfc.wisdomevaluation.R;
/**
 * @author fyh
 * @date 2019.6.25
 * */
public class WakeUp extends BaseApi implements WakeuperListener{
    private VoiceWakeuper mIvw;
    private String PATH = "";
    private String resultString;
    private OnWakeUp onWakeUp;
    public WakeUp(Context context) {
        super(context);
        init();
        requestData();
        aiWkeUp();
    }
    @Override
    public void init() {
        //初始化语音唤醒
        mIvw = VoiceWakeuper.createWakeuper(context, null);
        mIvw = VoiceWakeuper.getWakeuper();
    }
    /**
     * @Description 加载唤醒词文件
     * */
    private void requestData() {
        StringBuffer param = new StringBuffer();
        String resPath = ResourceUtil.generateResourcePath(
                context, ResourceUtil.RESOURCE_TYPE.assets,
                "ivw/" +context.getResources().getString(R.string.app_id)+".jet");
        param.append(ResourceUtil.IVW_RES_PATH + "=" + resPath);
        param.append("," + ResourceUtil.ENGINE_START + "=" + SpeechConstant.ENG_IVW);
        PATH = param.toString();
        boolean ret = SpeechUtility.getUtility().setParameter(
                ResourceUtil.ENGINE_START, param.toString());

        if (!ret) {
            Log.d("ERROR", "启动本地引擎失败！");
        }
    }
    private void aiWkeUp() {
        if(mIvw !=null)
        {
            resultString = "";
            mIvw.setParameter(SpeechConstant.PARAMS, PATH);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "65");  //20为门阀值
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置唤醒一直保持，直到调用stopListening，传入0则完成一次唤醒后，会话立即结束（默认0）
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1");
            mIvw.startListening(this); //onresume中操作
        }
        else
        {
            Log.d("ERROR", "引擎初始化失败！");
        }
    }


    @Override
    public void onBeginOfSpeech() {

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onResult(WakeuperResult wakeuperResult) {
        //成功唤醒的操作
        if(onWakeUp!=null)
        {
            onWakeUp.onWakeOK();
        }
//        resultString = wakeuperResult.getResultString();
//        new PlayerWakeUp(context).listenStar();
//        //唤醒成功
//        new Speak(context).startSpeaking("我在");
    }

    @Override
    public void onError(SpeechError speechError) {
        Log.d("ERROR",speechError.getErrorCode()+"");
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }
    @Override
    public void onVolumeChanged(int i) {

    }
    public interface OnWakeUp
    {
        void onWakeOK();
    }

    public void setOnWakeUp(OnWakeUp onWakeUp) {
        this.onWakeUp = onWakeUp;
    }
}
