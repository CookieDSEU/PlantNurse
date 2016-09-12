package com.plantnurse.plantnurse.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.MainApplication;
import com.plantnurse.plantnurse.Network.LoginResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.Constants;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by went on 2016/9/6.
 */
public class ResetpsdActivity extends KSimpleBaseActivityImpl implements IBaseAction {
    private Toolbar toolbar;
    private String user;
    private String oldpsd;
    private String newpsd;
    private String rnewpsd;
    private Button button;
    private TextView text_oldpsd;
    private TextView text_newpsd;
    private TextView text_rnewpsd;
    private MainApplication mApp;
    private HashMap<String, String> param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);

    }

    @Override
    public int initLocalData() {


        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        text_oldpsd = (TextView) findViewById(R.id.oldpsd);
        text_newpsd = (TextView) findViewById(R.id.newpsd);
        text_rnewpsd = (TextView) findViewById(R.id.rnewpsd);
        button = (Button) findViewById(R.id.button_rpsd);
        oldpsd = text_oldpsd.getText().toString();
        newpsd = text_newpsd.getText().toString();
        rnewpsd = text_rnewpsd.getText().toString();
        mApp = (MainApplication) getApplication();
        param = new HashMap<>();
    }

    @Override
    public void initController() {
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          oldpsd = text_oldpsd.getText().toString();
                                          newpsd = text_newpsd.getText().toString();
                                          rnewpsd = text_rnewpsd.getText().toString();
                                          if (check(newpsd)) {
                                              if (newpsd.equals(rnewpsd)) {

                                                  resetpsd();

                                              } else {
                                                  new SweetAlertDialog(ResetpsdActivity.this, SweetAlertDialog.WARNING_TYPE)

                                                          .setTitleText("两次密码输入不一致")
                                                          .show();
                                              }
                                          } else {
                                              new SweetAlertDialog(ResetpsdActivity.this, SweetAlertDialog.WARNING_TYPE)

                                                      .setTitleText("密码输入必须为大小写或数字且在8-16位")
                                                      .show();

                                          }

                                      }


                                  }
        );

    }

    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_resetpwd;
    }

    public void resetpsd() {
        UserInfo userInfo = (UserInfo) mApp.getUserModel();
        user = userInfo.getuserName();
        param.put("Userid", user);
        param.put("Userpsd", oldpsd);
        param.put("Rpsd", newpsd);
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getApplicationContext(), LoginResponse.class,
                param, Constants.RESETPSD_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                LoginResponse response = (LoginResponse) result.resultObject;
                if (response.getresponseCode() == 1) {
                    new SweetAlertDialog(ResetpsdActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("修改密码成功")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    finish();
                                }
                            })
                            .show();


                } else {
                    if (response.getresponseCode() == 3)
                        new SweetAlertDialog(ResetpsdActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("原密码错误")
                                .show();

                    else {
                        new SweetAlertDialog(ResetpsdActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("修改失败,用户冻结")

                                .show();

                    }
                }

            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });

    }

    private boolean check(String s) {
        return s.matches("[a-zA-Z0-9]{8,16}");
    }
}
