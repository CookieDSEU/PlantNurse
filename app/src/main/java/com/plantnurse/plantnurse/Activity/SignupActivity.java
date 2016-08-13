package com.plantnurse.plantnurse.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.cache.ACache;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.SignupResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.ToastUtil;


public class SignupActivity extends KSimpleBaseActivityImpl implements IBaseAction {

    private List<String> list_pro = new ArrayList<String>();
    private List<String> list_sh = new ArrayList<String>();
    private List<String> list_hb = new ArrayList<String>();
    private List<String> list_gd = new ArrayList<String>();
    private List<String> list_sx = new ArrayList<String>();
    private List<String> list_js = new ArrayList<String>();
    private List<String> list_career = new ArrayList<String>();

    private ArrayAdapter<String> adapter_pro;
    private ArrayAdapter<String> adapter_city;
    private ArrayAdapter<String> adapter_career;

    private Button button;
    private TextView text_id;
    private TextView text_pwd;
    private Spinner spinner_pro;
    private Spinner spinner_city;
    private Spinner spinner_career;

    private String id;
    private String pwd;
    private String province;
    private String city;
    private String career;

    private ProgressDialog progressDialog;
    private HashMap<String, String> loginParams;

    @Override
    public int initLocalData() {
        loginParams = new HashMap<>();
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        button = (Button) findViewById(R.id.button_over);
        text_id = (TextView) findViewById(R.id.editText_id);
        text_pwd = (TextView) findViewById(R.id.editText_pwd);

        spinner_pro = (Spinner) findViewById(R.id.spinner_province);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_career = (Spinner) findViewById(R.id.spinner_career);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在注册...");

        //省份
        list_pro.add("上海市");
        list_pro.add("湖北省");
        list_pro.add("广东省");
        list_pro.add("山西省");
        list_pro.add("江苏省");
        //城市
        list_sh.add("上海市");

        list_hb.add("武汉市");
        list_hb.add("黄石市");
        list_hb.add("荆州市");

        list_gd.add("汕头市");
        list_gd.add("广州市");
        list_gd.add("东莞市");

        list_sx.add("阳泉市");
        list_sx.add("太原市");
        list_sx.add("晋城市");

        list_js.add("南京市");
        list_js.add("无锡市");
        list_js.add("苏州市");

        list_career.add("学生");
        list_career.add("上班族");
        list_career.add("居家人士");
    }


    //软键盘返回键实现页面跳转
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.setClass(SignupActivity.this, SigninActivity.class);
            SignupActivity.this.startActivity(intent);
            SignupActivity.this.finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


    @Override
    public void initController() {
        id = text_id.getText().toString();//获取id
        pwd = text_pwd.getText().toString();//获取pwd

        //将可选内容与ArrayAdapter连接起来
        adapter_pro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_pro);
        //为适配器设置下拉列表下拉时的菜单样式。
        adapter_pro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到下拉列表上
        spinner_pro.setAdapter(adapter_pro);

        //为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner_pro.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                province = adapter_pro.getItem(arg2);//获得省份
                selectCity(arg2);
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });

        adapter_career = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_career);
        adapter_career.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_career.setAdapter(adapter_career);
        spinner_career.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                career = adapter_career.getItem(arg2);//获得职业
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=text_id.getText().toString();
                pwd=text_id.getText().toString();
                if (!checkID(id) ){
                    new AlertDialog.Builder(SignupActivity.this)
                            .setTitle("我是一个小提示")
                            .setMessage("用户名必须是8~16位英语和数字的组合")
                            .setPositiveButton("确定", null)
                            .show();
                }
                else{
                    if(!checkPwd(pwd)){
                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("我是一个小提示")
                                .setMessage("密码必须是8~16位英语和数字的组合")
                                .setPositiveButton("确定", null)
                                .show();
                    }
                    else{
                        signup();
                    }
                }
            }
        });
    }

    private boolean checkID(String s) {
        return s.matches("[a-zA-Z0-9]{8,16}");
    }

    private boolean checkPwd(String s) {
        return s.matches("[a-zA-Z0-9]{8,16}");
    }


    public void selectCity(int loc_id) {
        switch (loc_id) {
            case 0:
                adapter_city = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_sh);
                break;
            case 1:
                adapter_city = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_hb);
                break;
            case 2:
                adapter_city = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_gd);
                break;
            case 3:
                adapter_city = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_sx);
                break;
            case 4:
                adapter_city = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_js);
                break;
        }
        adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapter_city);
        spinner_city.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                city = adapter_city.getItem(arg2);//获得城市
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
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
        return R.layout.activity_register;
    }


    public void signup() {
        progressDialog.show();
        loginParams.put("userName", id);
        loginParams.put("password", pwd);
        loginParams.put("province", province);
        loginParams.put("city", city);
        loginParams.put("career", career);
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getApplicationContext(),
                SignupResponse.class, loginParams, Constants.SIGNUP_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                SignupResponse signupResponse = (SignupResponse) result.resultObject;
                if (signupResponse.getresponseCode() == 1) {
                    ToastUtil.showShort("注册成功");
                    ACache mCache = ACache.get(SignupActivity.this);
                    mCache.put("userName", signupResponse.getuserName(), 7 * ACache.TIME_DAY);
                    mCache.put("token", signupResponse.gettoken(), 7 * ACache.TIME_DAY);
                    finish();
                    progressDialog.dismiss();
                } else if (signupResponse.getresponseCode() == 2) {
                    ToastUtil.showShort("注册失败：该用户名已被注册");
                }
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }
}