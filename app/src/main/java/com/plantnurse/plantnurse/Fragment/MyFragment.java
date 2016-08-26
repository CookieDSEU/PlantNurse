package com.plantnurse.plantnurse.Fragment;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.Activity.SigninActivity;
import com.plantnurse.plantnurse.MainApplication;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.FileUtil;
import com.plantnurse.plantnurse.utils.SelectPicPopupWindow;
import com.plantnurse.plantnurse.utils.Util;

import java.io.File;

public class MyFragment extends KSimpleBaseFragmentImpl implements IBaseAction {
    private String urlpath;			// 图片本地路径
    private String resultStr = "";	// 服务端返回结果集
    private static ProgressDialog pd;// 等待进度圈
    private ImageView avatarview;
    private TextView usnview;
    private MainApplication mApp;
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记
    Bitmap avabitmap;
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
                File temp = new File(Environment.getExternalStorageDirectory() + "/avatar/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(getActivity(), "temphead.png", photo);
            avatarview.setImageDrawable(drawable);

            // 新线程后台上传服务端
            //pd = ProgressDialog.show(getActivity(), null, "正在上传图片，请稍候...");
            //new Thread(uploadImageRunnable).start();
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
        mApp=(MainApplication)getActivity().getApplication();

        return 0;
    }

    @Override
    public void initView(ViewGroup view) {

        avatarview=(ImageView)view.findViewById(R.id.ava_imv);
        usnview=(TextView)view.findViewById(R.id.usn_txv);
    }

    @Override
    public void initController() {

        if(mApp.isLogined()){
            UserInfo userInfo=(UserInfo) mApp.getUserModel();
            usnview.setText("Hi,"+userInfo.getuserName());
        }
        avatarview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mApp.isLogined()){
                    Intent intent=new Intent(getActivity(), SigninActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else{menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
                    menuWindow.showAtLocation(avatarview,
                            Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });
    }

    @Override
    public void onLoadingNetworkData() {
        if(mApp.isLogined()){
            UserInfo userInfo=(UserInfo) mApp.getUserModel();
            String temp= Constants.AVATAR_URL+"?id="+userInfo.getuserName();
            avabitmap=Util.getHttpBitmap(temp);
        }

    }

    @Override
    public void onLoadedNetworkData(View contentView) {
        if(mApp.isLogined()){
            avatarview.setImageBitmap(avabitmap);
        }

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_my;
    }
}
