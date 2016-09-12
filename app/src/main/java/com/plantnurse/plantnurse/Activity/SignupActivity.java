package com.plantnurse.plantnurse.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.preference.PreferenceManager;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.SignupResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.CityCodeDB;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.ToastUtil;

import cn.smssdk.SMSSDK;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Cookie_D on 2016/8/12.
 */
public class SignupActivity extends KSimpleBaseActivityImpl implements IBaseAction {

    private Toolbar toolbar;
    private List<String> provinceid, provincename;
    private List<String> cityid, cityname;
    private List<String> areaid, areaname;
    private List<String> list_career = new ArrayList<String>();
    private ArrayAdapter<String> adapter_career;
    private CityCodeDB citycodedb = null;
    private SQLiteDatabase db = null;
    private Button button;
    private EditText text_id;
    private EditText text_pwd;
    private MaterialSpinner spinner_pro;
    private MaterialSpinner spinner_city;
    private MaterialSpinner spinner_career;

    private String id;
    private String pwd;
    private String province;
    private String city;
    private String career;
    private String phone;

    private HashMap<String, String> loginParams;

    /**
     * Created by Eason_Tao on 2016/8/12.
     */
    @Override
    public int initLocalData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
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

    /**
     * Created by Eason_Tao on 2016/8/12.
     */
    @Override
    public void initView(ViewGroup view) {
        button = (Button) findViewById(R.id.button_over);
        text_id = (EditText) findViewById(R.id.editText_id);
        text_pwd = (EditText) findViewById(R.id.editText_pwd);
        spinner_pro = (MaterialSpinner) findViewById(R.id.spinner_province);
        spinner_city = (MaterialSpinner) findViewById(R.id.spinner_city);
        spinner_career = (MaterialSpinner) findViewById(R.id.spinner_career);
        toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

    }


    /**
     * Created by Eason_Tao on 2016/8/12.
     */
    @Override
    public void initController() {
        id = text_id.getText().toString();//获取id
        pwd = text_pwd.getText().toString();//获取pwd
        initProvinceSpinner(db);


        adapter_career = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_career);
        adapter_career.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_career.setAdapter(adapter_career);
        spinner_career.setSelection(1);
        spinner_career.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == -1) {
                    spinner_career.setSelection(0);
                } else {
                    career = adapter_career.getItem(arg2);//获得职业
                    arg0.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = text_id.getText().toString();//获取id
                pwd = text_pwd.getText().toString();//获取pwd
                if (!checkID(id)) {
                    new AlertDialog.Builder(SignupActivity.this)
                            .setTitle("我是一个小提示")
                            .setMessage("用户名必须是4~10位英语和数字的组合")
                            .setPositiveButton("确定", null)
                            .show();
                } else {
                    if (!checkPwd(pwd)) {
                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("我是一个小提示")
                                .setMessage("密码必须是8~16位英语和数字的组合")
                                .setPositiveButton("确定", null)
                                .show();
                    } else {
                        signup();
                    }
                }
            }
        });
    }

    private boolean checkID(String s) {
        return s.matches("[a-zA-Z0-9]{4,10}");
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

    /**
     * Created by Cookie_D on 2016/8/12.
     */
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
        spinner_pro.setSelection(1);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == -1) {
                    spinner_pro.setSelection(0);
                } else {
                    province = arg0.getItemAtPosition(position).toString();
                    initCitySpinner(db, provinceid.get(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };

        spinner_pro.setOnItemSelectedListener(listener);
    }

    /**
     * Created by Cookie_D on 2016/8/12.
     */
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
        spinner_city.setSelection(1);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == -1) {
                    spinner_city.setSelection(0);
                } else {
                    city = arg0.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        spinner_city.setOnItemSelectedListener(listener);
    }

    /**
     * Created by Cookie_D on 2016/8/12.
     */
    public void signup() {
        loginParams.put("userName", id);
        loginParams.put("password", pwd);
        loginParams.put("province", province);
        loginParams.put("city", city);
        loginParams.put("career", career);
        loginParams.put("phone", phone);
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getApplicationContext(),
                SignupResponse.class, loginParams, Constants.SIGNUP_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                SignupResponse signupResponse = (SignupResponse) result.resultObject;
                if (signupResponse.getresponseCode() == 1) {
                    ToastUtil.showShort("注册成功");
                    UserInfo ui = new UserInfo();
                    ui.setuserName(id);
                    ui.setProvince(province);
                    ui.setcareer(career);
                    ui.setcity(city);
                    ui.settoken(signupResponse.gettoken());
                    PreferenceManager.setLocalUserModel(ui);
                    getSimpleApplicationContext().setUserModel(ui);
                    // Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    // startActivity(intent);
                    Intent in = getIntent();
                    setResult(RESULT_OK, in);
                    finish();
                } else if (signupResponse.getresponseCode() == 2) {
                    ToastUtil.showShort("注册失败：该用户名已被注册");
                } else if (signupResponse.getresponseCode() == 3) {
                    ToastUtil.showShort("注册失败，该电话已被注册");
                }
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }

    /**
     * Created by Cookie_D on 2016/8/12.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//按返回键，跳转回MainActivity界面

    @Override
    public void onBackPressed() {
        Intent in = getIntent();
        setResult(RESULT_CANCELED, in);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}