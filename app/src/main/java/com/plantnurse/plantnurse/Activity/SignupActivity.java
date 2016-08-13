package com.plantnurse.plantnurse.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.plantnurse.plantnurse.utils.CityCodeDB;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.ToastUtil;


public class SignupActivity extends KSimpleBaseActivityImpl implements IBaseAction {


    private List<String> provinceid, provincename;
    private List<String> cityid, cityname;
    private List<String> areaid, areaname;
    private List<String> list_career = new ArrayList<String>();
    private ArrayAdapter<String> adapter_career;
    private String citycode;
    private String citycode_name;
    private CityCodeDB citycodedb = null;
    private SQLiteDatabase db = null;
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
        provinceid = new ArrayList<String>();
        provincename = new ArrayList<String>();
        cityid = new ArrayList<String>();
        cityname = new ArrayList<String>();
        areaid = new ArrayList<String>();
        areaname = new ArrayList<String>();
        citycodedb = new CityCodeDB(SignupActivity.this);
        db = citycodedb.getDatabase("data.db");
        list_career.add("学生");
        list_career.add("上班族");
        list_career.add("居家人士");
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
        initProvinceSpinner(db);




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

    void initProvinceSpinner(SQLiteDatabase database) {
        Cursor provincecursor = citycodedb.getAllProvince(database);

        if (provincecursor != null) {
            provinceid.clear();
            provincename.clear();
            if (provincecursor.moveToFirst()) {
                do {
                    String province_id = provincecursor
                            .getString(provincecursor.getColumnIndex("id"));
                    String province_name = provincecursor
                            .getString(provincecursor.getColumnIndex("name"));
                    provinceid.add(province_id);
                    provincename.add(province_name);
                } while (provincecursor.moveToNext());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignupActivity.this,
                android.R.layout.simple_spinner_item,
                provincename);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pro.setAdapter(adapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {
                // TODO Auto-generated method stub
                province =arg0.getItemAtPosition(position).toString();
                initCitySpinner(db, provinceid.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };

        spinner_pro.setOnItemSelectedListener(listener);
    }
    void initCitySpinner(SQLiteDatabase database, String provinceid) {
        Cursor citycursor = citycodedb.getCity(database, provinceid);
        if (citycursor != null) {
            cityid.clear();
            cityname.clear();
            if (citycursor.moveToFirst()) {
                do {
                    String city_id = citycursor.getString(citycursor
                            .getColumnIndex("id"));
                    String city_name = citycursor.getString(citycursor
                            .getColumnIndex("name"));
                    String province = citycursor.getString(citycursor
                            .getColumnIndex("p_id"));
                    cityid.add(city_id);
                    cityname.add(city_name);
                } while (citycursor.moveToNext());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignupActivity.this,
                android.R.layout.simple_spinner_item,
                cityname);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {
                // TODO Auto-generated method stub
                city=arg0.getItemAtPosition(position).toString();
                //initAreaSpinner(db, cityid.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        spinner_city.setOnItemSelectedListener(listener);
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