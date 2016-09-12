package com.plantnurse.plantnurse.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.ChangeInfoResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.AddplantAdapter;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.utils.CircleImg;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.FileUtil;
import com.plantnurse.plantnurse.utils.SelectPicPopupWindow;
import com.plantnurse.plantnurse.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import android.view.View.OnClickListener;

import com.plantnurse.plantnurse.utils.Util;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Eason_Tao on 2016/8/27.
 */
public class AddplantActivity extends KSimpleBaseActivityImpl implements IBaseAction, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Toolbar toolbar;
    private List<Integer> mData;//添加植物的图片列表
    private AddplantAdapter mAdapter;//添加植物的图片适配器
    private RecyclerView mRecyclerView;
    private CircleImg mImg;
    private Button overButton;
    private ImageButton addimageButton;
    private Calendar calendar;
    public static final String DATEPICKER_TAG = "datepicker";
    private DatePickerDialog datePickerDialog;
    private EditText _year;
    private EditText _month;
    private EditText _day;
    private EditText nameText;
    private EditText nicnameText;
    private EditText otherText;
    private RatingBar sunRatingBar;
    private RatingBar waterRatingBar;
    private RatingBar snowRatingBar;
    private SelectPicPopupWindow menuWindow;
    private static SweetAlertDialog pd;// 等待进度圈
    //data
    private String birth;
    private int birthday;
    private int birthmonth;
    private String name;
    private String nicname;
    private String other;
    private int sun;
    private int water;
    private int snow;
    private String mName;
    private int mSun;
    private int mWater;
    private int mSnow;
    private String uuid = "default2";
    private static final String IMAGE_FILE_NAME = "plantImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private String urlpath;            // 图片本地路径
    private Uri uritempFile;
    Calendar c = Calendar.getInstance();
    int myear = c.get(Calendar.YEAR);
    int mmonth = c.get(Calendar.MONTH);
    int mday = c.get(Calendar.DAY_OF_MONTH);
    /**
     * Created by Cookie_D on 2016/9/12.
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Created by Cookie_D on 2016/9/12.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                setPicToView();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Created by Cookie_D on 2016/9/12.
     */
    private void setPicToView() {
        // 取得SDCard图片路径做显示
        Bitmap photo = null;
        try {
            photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Drawable drawable = new BitmapDrawable(null, photo);
        uuid = UUID.randomUUID().toString();
        urlpath = FileUtil.saveFile(AddplantActivity.this, uuid + ".png", photo);
        mImg.setImageDrawable(drawable);

        // 新线程后台上传服务端
        pd = new SweetAlertDialog(AddplantActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.setTitleText("正在上传，请稍候").show();
        SimpleTaskManager.startNewTask(new SimpleTask(getTaskTag()) {

            @Override
            protected Object doInBackground(Object[] params) {
                String info = Util.uploadAvatar(uuid, Util.TYPE_PLANT);
                //删除上传暂存文件。
                File file = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                file.delete();
                file = new File(Environment.getExternalStorageDirectory() + "/avatar/" + uuid + ".png");
                file.delete();
                file = new File(Environment.getExternalStorageDirectory() + "/" + "temp.jpg");
                file.delete();
                pd.dismiss();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

            }
        });
    }

    /**
     * Created by Cookie_D on 2016/9/12.
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/png");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);
        //MIUI无法直接返回DATA。故将图片保存在Uri中，
        // 调用时将Uri转换为Bitmap，
        //intent.putExtra("return-data", true);
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    //返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //当点击edittext以外的地方，取消焦点，隐藏输入键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        initDatas();
        //初始化数据
        toolbar = (Toolbar) view.findViewById(R.id.addplant_toolbar);
        mImg = (CircleImg) findViewById(R.id.id_content);
        overButton = (Button) findViewById(R.id.addplant_addoverbutton);
        addimageButton = (ImageButton) findViewById(R.id.addimagebutton);
        nameText = (EditText) findViewById(R.id.addplant_name);
        nicnameText = (EditText) findViewById(R.id.addplant_nicname);
        otherText = (EditText) findViewById(R.id.addplant_other);
        _year = (EditText) findViewById(R.id.text_birthyear);
        _month = (EditText) findViewById(R.id.text_birthmonth);
        _day = (EditText) findViewById(R.id.text_birthday);
        sunRatingBar = (RatingBar) findViewById(R.id.addplant_sun);
        waterRatingBar = (RatingBar) findViewById(R.id.addplant_water);
        snowRatingBar = (RatingBar) findViewById(R.id.addplant_snow);
        mImg.setImageResource(R.drawable.flower2);
        //关联图片，设置默认图片

        //ratingbar 初始化
        nameText.setText(mName);
        sunRatingBar.setProgress(mSun);
        waterRatingBar.setProgress(mWater);
        snowRatingBar.setProgress(mSnow);
        //y-m-d初始化
        mmonth++;
        _year.setText(myear + "");
        _month.setText(mmonth + "");
        _day.setText(mday + "");

        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("添加植物");

        //完成按钮
        overButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameText.getText().equals("") || nicnameText.getText().equals("") || nameText.getText().equals("") || _year.getText().equals("") || _month.getText().equals("") || _day.getText().equals("")) {
                    ToastUtil.showLong("请多留下一些它的信息吧...");
                } else {
                    //get birthday
                    birth = "" + _year.getText();
                    birthmonth = Integer.parseInt(_month.getText().toString());
                    birthday = Integer.parseInt(_day.getText().toString());
                    if (birthmonth < 10)
                        birth = birth + "0" + birthmonth;
                    else
                        birth = birth + birthmonth;
                    if (birthday < 10)
                        birth = birth + "0" + birthday;
                    else
                        birth = birth + birthday;
                    //get name ...
                    name = nameText.getText().toString();
                    nicname = nicnameText.getText().toString();
                    other = otherText.getText().toString();
                    //get setting
                    sun = sunRatingBar.getProgress();
                    water = waterRatingBar.getProgress();
                    snow = waterRatingBar.getProgress();
                    HashMap<String, String> param = new HashMap<String, String>();
                    param.put("nickname", nicname);
                    param.put("name", name);
                    param.put("birthday", birth);
                    param.put("sun", sun + "");
                    param.put("water", water + "");
                    param.put("cold", snow + "");
                    param.put("remark", other);
                    UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
                    param.put("owner", userInfo.getuserName());
                    param.put("pic", uuid);
                    SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getSimpleApplicationContext(), ChangeInfoResponse.class, param, Constants.ADDPLANT_URL, NetworkTask.GET) {
                        @Override
                        public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                            ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                            if (response.getresponseCode() == 1) {
                                DataManager.isMyPlantChanged = true;
                                new SweetAlertDialog(AddplantActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("添加成功！")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .show();
                            } else if (response.getresponseCode() == 2) {
                                new SweetAlertDialog(AddplantActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("昵称和已有植物重复")
                                        .show();
                            } else {
                                new SweetAlertDialog(AddplantActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("连接超时，添加失败！")
                                        .show();
                            }
                        }

                        @Override
                        public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                        }
                    });
                }
            }
        });

        addimageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SelectPicPopupWindow(AddplantActivity.this, itemsOnClick);
                menuWindow.showAtLocation(mImg,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        findViewById(R.id.text_birthyear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        findViewById(R.id.text_birthmonth).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        findViewById(R.id.text_birthday).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        //选择图片recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new AddplantAdapter(this, mData);
        mAdapter.setOnItemClickLitener(new AddplantAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mImg.setImageResource(mData.get(position));
                uuid = "default" + (position + 1);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDatas() {
        mData = new ArrayList<Integer>(Arrays.asList(R.drawable.flower1_s, R.drawable.flower2_s,
                R.drawable.flower3_s, R.drawable.flower4_s, R.drawable.flower5_s));
        Intent mIntent = getIntent();
        int type = mIntent.getIntExtra("addplant", 0);
        if (type == 1) {
            mName = mIntent.getStringExtra("name");
            mSun = mIntent.getIntExtra("sun", 1);
            mWater = mIntent.getIntExtra("water", 1);
            mSnow = mIntent.getIntExtra("snow", 1);
        }

        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);

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
        return R.layout.activity_addplant;
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        month++;
        _year.setText("" + year);
        _month.setText("" + month);
        _day.setText("" + day);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(AddplantActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }
}
