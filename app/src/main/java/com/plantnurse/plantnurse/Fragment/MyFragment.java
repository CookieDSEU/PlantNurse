package com.plantnurse.plantnurse.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.i.ITabPageAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.plantnurse.plantnurse.Activity.AboutActivity;
import com.plantnurse.plantnurse.Activity.CollectActivity;
import com.plantnurse.plantnurse.utils.DeleteData;
import com.plantnurse.plantnurse.Activity.MainActivity;
import com.plantnurse.plantnurse.Activity.ResetcityActivity;
import com.plantnurse.plantnurse.Activity.ResetpsdActivity;
import com.plantnurse.plantnurse.Activity.SigninActivity;
import com.plantnurse.plantnurse.MainApplication;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.CircleImg;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.FileUtil;
import com.plantnurse.plantnurse.utils.SelectPicPopupWindow;
import com.plantnurse.plantnurse.utils.Util;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Cookie_D on 2016/8/26.
 */
public class MyFragment extends KSimpleBaseFragmentImpl implements IBaseAction, ITabPageAction {
    private String urlpath;            // 图片本地路径
    private static SweetAlertDialog pd;// 等待进度圈
    private CircleImg avatarview;
    private TextView usnview;
    private MainApplication mApp;
    private TableRow mycity;
    private TableRow mypsd;
    private TableRow myhobby;
    private TableRow sysset;
    private TableRow sysreflct;
    private TableRow sysupdate;
    private TableRow sysabout;


    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private SelectPicPopupWindow menuWindow;
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
    private DeleteData deleteData;

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
                if (data != null) {
                    setPicToView(data);
                }
                break;
            case MainActivity.REQUEST_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else if (resultCode == getActivity().RESULT_CANCELED) {

                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            UserInfo userInfo = (UserInfo) mApp.getUserModel();
            urlpath = FileUtil.saveFile(getActivity(), userInfo.getuserName() + ".png", photo);
            avatarview.setImageDrawable(drawable);

            // 新线程后台上传服务端
            pd = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pd.setTitleText("正在上传，请稍候").show();
            SimpleTaskManager.startNewTask(new SimpleTask(getTaskTag()) {

                @Override
                protected Object doInBackground(Object[] params) {
                    UserInfo userInfo = (UserInfo) mApp.getUserModel();
                    String info = Util.uploadAvatar(userInfo.getuserName(), Util.TYPE_AVATAR);
                    File file = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    file.delete();
                    file = new File(Environment.getExternalStorageDirectory() + "/avatar/" + userInfo.getuserName() + ".png");
                    file.delete();
                    pd.dismiss();
                    DataManager.isAvatarChanged_drawer = true;
                    DataManager.isIsAvatarChanged_myfragment = true;
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {

                }
            });
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/png");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    @Override
    public int initLocalData() {
        mApp = (MainApplication) getActivity().getApplication();

        return 0;
    }

    @Override
    public void initView(ViewGroup view) {

        avatarview = (CircleImg) view.findViewById(R.id.ava_img);
        usnview = (TextView) view.findViewById(R.id.usn_txv);
        mycity = (TableRow) view.findViewById(R.id.city);
        mypsd = (TableRow) view.findViewById(R.id.resetpsd);
        myhobby = (TableRow) view.findViewById(R.id.hobby);
        sysset = (TableRow) view.findViewById(R.id.sysset);
        sysreflct = (TableRow) view.findViewById(R.id.reflect);
        sysupdate = (TableRow) view.findViewById(R.id.update);
        sysabout = (TableRow) view.findViewById(R.id.about);

    }

    @Override
    public void initController() {

        if (mApp.isLogined()) {
            UserInfo userInfo = (UserInfo) mApp.getUserModel();
            usnview.setText("Hi," + userInfo.getuserName());
        }
        avatarview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mApp.isLogined()) {
                    Intent intent = new Intent(getActivity(), SigninActivity.class);
                    startActivityForResult(intent, MainActivity.REQUEST_CODE);
                } else {
                    menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
                    menuWindow.showAtLocation(avatarview,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });
        /**
         * Created by went on 2016/8/26.
         */
        mycity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApp.isLogined()) {
                    Intent intent = new Intent(getActivity(), ResetcityActivity.class);
                    startActivity(intent);
                } else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请先登录")
                            .show();
                }

            }
        });
        mypsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetpsdActivity.class);
                if (mApp.isLogined()) {
                    startActivity(intent);
                } else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请先登录")
                            .show();
                }

            }
        });
        myhobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mApp.isLogined()) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请先登录!")
                            .show();
                } else {
                    Intent intent = new Intent(getActivity(), CollectActivity.class);
                    startActivity(intent);
                }
            }
        });
        sysset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData = new DeleteData();
                File file = mApp.getExternalFilesDir("SDCard/Android/data/com.plantnurse.plantnurse/files/");
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("是否清除所有闹钟？")
                        .showCancelButton(true)
                        .setCancelText("否")
                        .setConfirmText("是")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                DeleteData.cleanApplicationData(mApp);
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("已完成")
                                        .show();
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                DeleteData.cleanInternalCache(mApp);
                                DeleteData.cleanExternalCache(mApp);
                                DeleteData.cleanSharedPreference(mApp);
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("已完成")
                                        .show();
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
        sysreflct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Constants.REPORT_URL);   //指定网址
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);           //指定Action
                intent.setData(uri);                            //设置Uri
                startActivity(intent);        //启动Activity

            }
        });
        sysupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.checkVersion(getActivity());
            }
        });
        sysabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);

            }
        });
        //created by went
    }

    @Override
    public void onLoadingNetworkData() {


    }

    @Override
    public void onLoadedNetworkData(View contentView) {
        if (mApp.isLogined()) {
            UserInfo userInfo = (UserInfo) mApp.getUserModel();
            String temp = Constants.AVATAR_URL + "?id=" + userInfo.getuserName();
            Picasso.with(getActivity()).load(temp).into(avatarview);
        }
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void onPageSelected() {
        if (DataManager.isIsAvatarChanged_myfragment) {
            UserInfo userInfo = (UserInfo) mApp.getUserModel();
            String temp = Constants.AVATAR_URL + "?id=" + userInfo.getuserName();
            Picasso.with(getActivity()).invalidate(temp);
            DataManager.isIsAvatarChanged_myfragment = false;
        }
    }
}
