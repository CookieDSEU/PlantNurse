package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.ChangeInfoResponse;
import com.plantnurse.plantnurse.Network.GetPlantInfoResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.FileUtil;
import com.plantnurse.plantnurse.utils.SelectPicPopupWindow;
import com.plantnurse.plantnurse.utils.ToastUtil;
import com.plantnurse.plantnurse.utils.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Eason_Tao on 2016/8/27.
 */
public class MyPlantActivity extends KSimpleBaseActivityImpl implements IBaseAction {
    //view
    private Button saveButton;
    private EditText birthText;
    private EditText tagText;
    private TextView nameText;
    private RatingBar ratingBar_sun;
    private RatingBar ratingBar_water;
    private RatingBar ratingBar_cold;
    private ImageView banner_planticon;//放图片的
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout_name;//放名字的
    //data
    private int selectedId;
    private String mName;
    private String mTag;
    private String mBirth;
    private int mSunIndex;
    private int mWaterIndex;
    private int mColdIndex;
    private String birthday;
    private int companyday;
    private String newname;


    //修改图片功能
    /**
     * Created by Cookie_D on 2016/9/12.
     */
    private static final String IMAGE_FILE_NAME = "plantImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private String urlpath;            // 图片本地路径
    private Uri uritempFile;
    private String uuid = "default2";
    private SelectPicPopupWindow menuWindow;
    private static SweetAlertDialog pd;// 等待进度圈
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
        if (DataManager.getMyPlant().response.get(selectedId).pic.contains("default")) {
            uuid = UUID.randomUUID().toString();
            UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
            HashMap<String, String> param = new HashMap<>();
            param.put("nickname", DataManager.getMyPlant().response.get(selectedId).nickname);
            param.put("owner", userInfo.getuserName());
            param.put("pic", uuid);
            SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), MyPlantActivity.this, ChangeInfoResponse.class, param, Constants.CHANGEPLANTPIC_URL, NetworkTask.GET) {
                @Override
                public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                    ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                    if (response.getresponseCode() == 1) {
                        DataManager.isMyPlantChanged = true;
                    }
                }

                @Override
                public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                }
            });
        } else {
            uuid = DataManager.getMyPlant().response.get(selectedId).pic;
        }
        urlpath = FileUtil.saveFile(MyPlantActivity.this, uuid + ".png", photo);
        banner_planticon.setImageDrawable(drawable);

        // 新线程后台上传服务端
        pd = new SweetAlertDialog(MyPlantActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
                String temp = Constants.MYPLANTPIC_URL + DataManager.getMyPlant().response.get(selectedId).pic;
                Picasso.with(MyPlantActivity.this).invalidate(temp);
                DataManager.isMyPlantPicChanged = true;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deleteplant_toolbar_menu, menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_deleteplant) {
                    SweetAlertDialog builder = new SweetAlertDialog(MyPlantActivity.this, SweetAlertDialog.NORMAL_TYPE);
                    builder.setTitleText("提示");
                    builder.setContentText("确定要删除此植物吗？");
                    builder.setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            HashMap<String, String> param = new HashMap<String, String>();
                            UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
                            param.put("userName", userInfo.getuserName());
                            param.put("nickname", DataManager.getMyPlant().response.get(selectedId).nickname);
                            DataManager.isMyPlantChanged = true;
                            SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), MyPlantActivity.this, ChangeInfoResponse.class, param, Constants.DELETEPLANT_URL, NetworkTask.GET) {
                                @Override
                                public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                                    ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                                    if (response.getresponseCode() == 1) {
                                        SweetAlertDialog builder = new SweetAlertDialog(MyPlantActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                        builder.setTitleText("提示");
                                        builder.setContentText("删除成功");
                                        builder.setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                }

                                @Override
                                public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                                }
                            });
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    builder.setCancelText("取消").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    builder.show();
                    return true;
                } else if (menuItemId == R.id.action_editicon) {
                    birthText.setText("");
                    birthText.setHint("请输入新昵称");
                    ratingBar_cold.setIsIndicator(false);
                    ratingBar_sun.setIsIndicator(false);
                    ratingBar_water.setIsIndicator(false);
                    birthText.setEnabled(true);
                    tagText.setEnabled(true);
                    saveButton.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        saveButton = (Button) view.findViewById(R.id.myplant_buttonsave);
        ratingBar_sun = (RatingBar) view.findViewById(R.id.myplant_rating_sun);
        ratingBar_water = (RatingBar) view.findViewById(R.id.myplant_rating_water);
        ratingBar_cold = (RatingBar) view.findViewById(R.id.myplant_rating_cold);
        birthText = (EditText) view.findViewById(R.id.myplant_birthtext);
        tagText = (EditText) view.findViewById(R.id.myplant_tagtext);
        birthText.setEnabled(false);
        tagText.setEnabled(false);
        nameText = (TextView) view.findViewById(R.id.myplant_nametext);
        banner_planticon = (ImageView) view.findViewById(R.id.myplant_bannner);
        toolbar = (Toolbar) view.findViewById(R.id.myplant_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout_name = (CollapsingToolbarLayout) view.findViewById(R.id.myplant_toolbarlayout);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setVisibility(View.GONE);
                birthText.setEnabled(false);
                tagText.setEnabled(false);
                ratingBar_water.setIsIndicator(true);
                ratingBar_sun.setIsIndicator(true);
                ratingBar_cold.setIsIndicator(true);
                //传给服务器的信息，我就写到这里了，其他的移交邓鹏
                newname = birthText.getText().toString();
                int sun = ratingBar_sun.getProgress();
                int water = ratingBar_water.getProgress();
                int cold = ratingBar_cold.getProgress();
                String tag = tagText.getText().toString();

                UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("nickname", newname);
                param.put("sun", sun + "");
                param.put("water", water + "");
                param.put("cold", cold + "");
                param.put("remark", tag);
                param.put("owner", userInfo.getuserName());
                param.put("oldname", DataManager.getMyPlant().response.get(selectedId).nickname);
                SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), MyPlantActivity.this, ChangeInfoResponse.class, param, Constants.CHANGEPLANTINFO_URL, NetworkTask.GET) {
                    @Override
                    public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                        ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                        if (response.getresponseCode() == 1) {
                            birthText.setText(newname + "已经陪伴你" + companyday + "天啦");
                            ToastUtil.showShort("修改成功！");
                        } else if (response.getresponseCode() == 2) {
                            birthText.setText(DataManager.getMyPlant().response.get(selectedId).nickname + "已经陪伴你" + companyday + "天啦");
                            ToastUtil.showShort("和已有昵称重复！");
                        } else {
                            ToastUtil.showShort("修改失败！");
                        }
                    }

                    @Override
                    public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                    }
                });

            }
        });
    }

    @Override
    public void initController() {
        banner_planticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SelectPicPopupWindow(MyPlantActivity.this, itemsOnClick);
                menuWindow.showAtLocation(banner_planticon,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    @Override
    public void onLoadingNetworkData() {
        Intent intent = getIntent();
        selectedId = intent.getIntExtra("id", 0);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("name", mName);
        //判断是否已经收藏
        //是否登录
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), this, GetPlantInfoResponse.class, param, Constants.PLANTINFO_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                GetPlantInfoResponse bookResponse = (GetPlantInfoResponse) result.resultObject;
                mName = "学名: " + DataManager.getMyPlant().response.get(selectedId).name;
                mSunIndex = DataManager.getMyPlant().response.get(selectedId).sun;
                mWaterIndex = DataManager.getMyPlant().response.get(selectedId).water;
                mColdIndex = DataManager.getMyPlant().response.get(selectedId).cold;


                birthday = DataManager.getMyPlant().response.get(selectedId).birthday + "";
                int birth = Integer.parseInt(birthday);
                birth -= 100;
                birthday = birth + "";
                Calendar c = Calendar.getInstance();
                int nowyear = c.get(Calendar.YEAR);
                int nowmonth = c.get(Calendar.MONTH);
                int nowday = c.get(Calendar.DAY_OF_MONTH);
                String now = "" + nowyear;
                if (nowmonth < 10)
                    now += "0" + nowmonth;
                else
                    now += nowmonth;

                if (nowday < 10)
                    now += "0" + nowday;
                else
                    now += nowday;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//输入日期的格式
                Date date1 = null;
                try {
                    date1 = simpleDateFormat.parse(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = null;
                try {
                    date2 = simpleDateFormat.parse(now);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GregorianCalendar cal1 = new GregorianCalendar();
                GregorianCalendar cal2 = new GregorianCalendar();
                cal1.setTime(date1);
                cal2.setTime(date2);
                double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);//从间隔毫秒变成间隔天数
                companyday = (int) dayCount + 1;
                mBirth = DataManager.getMyPlant().response.get(selectedId).nickname + "已经陪伴你" + companyday + "天啦";

                mTag = DataManager.getMyPlant().response.get(selectedId).remark;

                toolbarLayout_name.setTitle(DataManager.getMyPlant().response.get(selectedId).nickname);

                Picasso.with(MyPlantActivity.this).load(Constants.MYPLANTPIC_URL + DataManager.getMyPlant().response.get(selectedId).pic).into(banner_planticon);

                ratingBar_sun.setProgress(mSunIndex);
                ratingBar_water.setProgress(mWaterIndex);
                ratingBar_cold.setProgress(mColdIndex);

                birthText.setText(mBirth);
                nameText.setText(mName);
                tagText.setText(mTag);
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }


    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_myplant;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}