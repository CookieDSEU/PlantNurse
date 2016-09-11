package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.preference.PreferenceManager;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.LoginResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.Constants;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by Cookie_D on 2016/8/12.
 */
public class SigninActivity extends KSimpleBaseActivityImpl implements IBaseAction{
    private Toolbar toolbar;
    private TextView button_signup;
    private TextView button_forget;
    private Button button_login;
    private TextView text;
    private TextView text2;
    private HashMap<String, String> loginParams;
    @Override
    public int initLocalData() {
        loginParams = new HashMap<>();
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        button_signup = (TextView) findViewById(R.id.register_button);
        button_forget=(TextView)findViewById(R.id.forget_button);
        button_login = (Button) findViewById(R.id.login_button);
        text= (TextView) view.findViewById(R.id.userID);
        text2=  (TextView) view.findViewById(R.id.pwd);
        toolbar =(Toolbar)findViewById(R.id.signin_toolbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initController() {
        button_signup.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v)
            {
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            String phone = (String) phoneMap.get("phone");
                            // 提交用户信息
                            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                            intent.putExtra("phone",phone);
                            startActivity(intent);
                        }
                    }
                });
                registerPage.show(SigninActivity.this);
            }
        });
        button_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            String phone = (String) phoneMap.get("phone");
                            // 提交用户信息
                            Intent intent = new Intent(SigninActivity.this, ForgetPwdActivity.class);
                            intent.putExtra("phone",phone);
                            startActivity(intent);
                        }
                    }
                });
                registerPage.show(SigninActivity.this);
            }
        });

        button_login.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String username = text.getText().toString();
                String password = text2.getText().toString();
                loginParams.put("userName", username);
                loginParams.put("password", password);
                if(check(username)&&check(password)){
                    SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getApplicationContext(),
                            LoginResponse.class, loginParams, Constants.SIGNIN_URL, NetworkTask.GET) {
                        @Override
                        public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                            LoginResponse loginResponse = (LoginResponse) result.resultObject;
                            if (loginResponse.getresponseCode() == 1) {
//                            ToastUtil.showLong("登录成功");
                                UserInfo ui = new UserInfo();
                                ui.setuserName(loginResponse.getuserName());
                                ui.setProvince(loginResponse.getprovince());
                                ui.setcareer(loginResponse.getcareer());
                                ui.setcity(loginResponse.getcity());
                                ui.settoken(loginResponse.gettoken());
                                PreferenceManager.setLocalUserModel(ui);
                                getSimpleApplicationContext().setUserModel(ui);
                                new SweetAlertDialog(SigninActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("登陆成功")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                Intent in = getIntent();
                                                setResult(RESULT_OK, in);
                                                sweetAlertDialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .show();
                            } else if (loginResponse.getresponseCode() == 0) {
                                new SweetAlertDialog(SigninActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("账户未注册")
                                        .show();

                            } else if (loginResponse.getresponseCode() == 2) {
                                new SweetAlertDialog(SigninActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("登录失败:账户已冻结")
                                        .show();

                            } else if (loginResponse.getresponseCode() == 3) {
                                new SweetAlertDialog(SigninActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("登录失败：密码错误")
                                        .show();

                            }

                        }


                        @Override
                        public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                        }

                    });
                }
                else  {
                    new SweetAlertDialog(SigninActivity.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("账号密码不能为空")
                            .show();
                }


            }
        });

    }


    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_login;
    }

    //按返回键，返回MainActivity界面,关闭当前页面

    @Override
    public void onBackPressed() {
        Intent in=getIntent();
        setResult(RESULT_CANCELED,in);
        finish();
        super.onBackPressed();
    }
    private boolean check(String s) {return s.matches("[a-zA-Z0-9]{1,16}");}

}
