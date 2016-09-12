package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.LoginResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.Constants;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Cookie_D on 2016/9/11.
 */
public class ForgetPwdActivity extends KSimpleBaseActivityImpl implements IBaseAction {
    private String phone;
    private EditText npwd_text;
    private EditText rnpwd_text;
    private Button confirm_button;
    private Toolbar toolbar;

    @Override
    public int initLocalData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        toolbar = (Toolbar) findViewById(R.id.forgetpwd_toolbar);
        toolbar.setTitle("重置密码");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        npwd_text = (EditText) findViewById(R.id.fg_newpwd);
        rnpwd_text = (EditText) findViewById(R.id.fg_rnewpwd);
        confirm_button = (Button) findViewById(R.id.fg_button_rpwd);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String npwd = npwd_text.getText().toString();
                String rnpwd = rnpwd_text.getText().toString();
                if (npwd.matches("[a-zA-Z0-9]{8,16}")) {
                    if (!npwd.equals(rnpwd)) {
                        new SweetAlertDialog(ForgetPwdActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setContentText("两次输入的密码不一样！请重新输入");
                    } else {
                        HashMap<String, String> param = new HashMap<String, String>();
                        param.put("phone", phone);
                        param.put("npwd", npwd);
                        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), ForgetPwdActivity.this, LoginResponse.class,
                                param, Constants.FORGETPWD_URL, NetworkTask.GET) {
                            @Override
                            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                                LoginResponse response = (LoginResponse) result.resultObject;
                                if (response.getresponseCode() == 1) {
                                    new SweetAlertDialog(ForgetPwdActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("修改密码成功")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                }
                                            })
                                            .show();
                                } else if (response.getresponseCode() == 2) {
                                    new SweetAlertDialog(ForgetPwdActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("修改失败,用户冻结")
                                            .show();
                                } else if (response.getresponseCode() == 4) {
                                    new SweetAlertDialog(ForgetPwdActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("修改失败,该用户尚未注册")
                                            .show();
                                } else {
                                    new SweetAlertDialog(ForgetPwdActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("修改失败,未知错误")
                                            .show();
                                }
                            }

                            @Override
                            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                            }
                        });
                    }
                } else {
                    new SweetAlertDialog(ForgetPwdActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("密码输入必须为大小写或数字且在8-16位")
                            .show();
                }
            }
        });
    }

    @Override
    public void initController() {

    }

    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_forgetpwd;
    }
}
