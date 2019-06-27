package cn.pfc.wisdomevaluation.ui.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.pfc.wisdomevaluation.R;
import cn.pfc.wisdomevaluation.service.WakeUpService;
import de.hdodenhof.circleimageview.CircleImageView;
/**
 * @author fyh
 * @date 2019.6.24
 * */
public class LoginActivity extends AppCompatActivity{
    private LinearLayout mLogInImagelayout;
    private CircleImageView mLogInImage;
    private LinearLayout loginLl1;
    private TextInputLayout mLogInPhoneNumberLayout;
    private EditText mLogInPhoneNumber;
    private TextInputLayout mLogInPhonePasswordLayout;
    private EditText mLogInPhonePassword;
    private Button mLogInBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setLayoutHight();
        setListaner();
    }

    private void setListaner() {
        mLogInBtn.setOnClickListener((view)->
        {
//            if(mLogInPhoneNumber.getText().toString().length()<11)
//            {
//                mLogInPhoneNumberLayout.setError("请输入正确的手机号");
//            }
//            else
//            {
//                mLogInPhoneNumberLayout.setError("");
//                //TODO login user
//            }
            //test
            startService(new Intent(LoginActivity.this, WakeUpService.class));
        });
    }

    private void setLayoutHight() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,getScreenHeoght(),0,0);
        mLogInImagelayout.setLayoutParams(layoutParams);
    }

    /**
     * @Description 获取并计算出MarginTop高度
     * @return int
     * */
    private int  getScreenHeoght() {
        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        return (int) (height*0.2);
    }

    private void initView() {
        mLogInImagelayout = (LinearLayout) findViewById(R.id.mLogInImagelayout);
        mLogInImage = (CircleImageView) findViewById(R.id.mLogInImage);
        loginLl1 = (LinearLayout) findViewById(R.id.login_ll1);
        mLogInPhoneNumberLayout = (TextInputLayout) findViewById(R.id.mLogInPhoneNumberLayout);
        mLogInPhoneNumber = (EditText) findViewById(R.id.mLogInPhoneNumber);
        mLogInPhonePasswordLayout = (TextInputLayout) findViewById(R.id.mLogInPhonePasswordLayout);
        mLogInPhonePassword = (EditText) findViewById(R.id.mLogInPhonePassword);
        mLogInBtn = (Button) findViewById(R.id.mLogInBtn);
    }
}
