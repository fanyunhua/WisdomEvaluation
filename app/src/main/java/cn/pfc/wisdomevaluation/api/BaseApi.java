package cn.pfc.wisdomevaluation.api;

import android.content.Context;
/**
 * @author fyh
 * @date 2019.6.25
 * */
public abstract class BaseApi {
    public Context context;
    public BaseApi(Context context)
    {
        this.context = context;
    }
    public abstract void init();

    public  Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
