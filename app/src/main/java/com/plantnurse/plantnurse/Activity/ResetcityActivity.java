package com.plantnurse.plantnurse.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ResetcityActivity extends KSimpleBaseActivityImpl implements IBaseAction
{
//    private Toolbar toolbar;
    private MaterialSpinner spinner_pro;
    private MaterialSpinner spinner_city;
    private List<String>  provincename,provinceid;
    private List<String>  cityname,cityid;
    private CityCodeDB cityCodeDB=null;
    private SQLiteDatabase db = null;
    private Button button;
    private  String province;
    private  String city;
    private ProgressDialog progressDialog;
    private HashMap<String, String> loginParams;
    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reset_city);
//    }

    public int initLocalData(){
        loginParams = new HashMap<>();
        provincename=new ArrayList<String>();
        provinceid=new ArrayList<String>();
        cityname=new ArrayList<String>();
        cityid = new ArrayList<String>();
        cityCodeDB = new CityCodeDB(ResetcityActivity.this);
        db = cityCodeDB.getDatabase("data.db");

       return 0;
    }

    public void initView(ViewGroup view) {
        button=(Button)findViewById(R.id.reset);
        button.setBackgroundColor(Color.parseColor("#3A5FCD"));
        spinner_pro = (MaterialSpinner) findViewById(R.id.spinner_province);
        spinner_city = (MaterialSpinner) findViewById(R.id.spinner_city);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在更改");
//        toolbar =(Toolbar)findViewById(R.id.signup_toolbar);
//        toolbar.setTitle("更改");
//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void initController() {

        initProvinceSpinner(db);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citychange();
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
        return R.layout.activity_reset_city;
    }



    void initProvinceSpinner(SQLiteDatabase database) {
        Cursor provincecursor = cityCodeDB.getAllProvince(database);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResetcityActivity.this,
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
        Cursor citycursor = cityCodeDB.getCity(database, provinceid);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResetcityActivity.this,
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


    public void citychange() {
        progressDialog.show();

        loginParams.put("province", province);
        loginParams.put("city", city);

        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getApplicationContext(),
                SignupResponse.class, loginParams, Constants.SIGNUP_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                SignupResponse signupResponse = (SignupResponse) result.resultObject;
                if (signupResponse.getresponseCode() == 1) {
                    ToastUtil.showShort("更改成功");
                    UserInfo ui=new UserInfo();
                    ui.setProvince(province);
                    ui.setcity(city);
                    ui.settoken(signupResponse.gettoken());
                    PreferenceManager.setLocalUserModel(ui);
                    getSimpleApplicationContext().setUserModel(ui);
                    // Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    // startActivity(intent);
                    Intent in=getIntent();
                    setResult(RESULT_OK,in);
                    finish();
                    progressDialog.dismiss();
                } else  {
                    ToastUtil.showShort("更改失败，网络好像出了点问题");
                }
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }
    }


