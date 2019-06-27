package cn.pfc.wisdomevaluation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cn.pfc.wisdomevaluation.ui.activity.LoginActivity;
/**
 * @author fyh
 * @date 2019.6.24
 *
 * */
public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();

    }
    /***
     *
     * @Description 权限申请
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }
            }
            //打开悬浮窗权限申请界面
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())), 0);
            } else {
                //已有权限
                Toast.makeText(this, "已有权限", Toast.LENGTH_SHORT);
//                //启动语音唤醒服务
//                Intent intent = new Intent(MainActivity.this, WakeUpService.class);
//                startService(intent);
                //转到登录界面
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Toast.makeText(this, "授权失败,需要重新授权", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())), 0);
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                //启动语音唤醒服务
//                startService(new Intent(MainActivity.this, WakeUpService.class));
                //转到登录界面
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
    }
}
