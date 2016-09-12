package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.design.widget.CollapsingToolbarLayout;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.Util;

/**
 * Created by Cookie_D on 2016/8/12.
 */
public class AboutActivity extends KSimpleBaseActivityImpl implements IBaseAction {
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView tvVersion;
    private Button btCode;
    private Button btBlog;
    private Button btPay;
    private Button btShare;
    private Button btUpdate;
    private Button btBug;

    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        btCode = (Button) findViewById(R.id.bt_code);
        btBlog = (Button) findViewById(R.id.bt_blog);
        btPay = (Button) findViewById(R.id.bt_pay);
        btShare = (Button) findViewById(R.id.bt_share);
        btUpdate = (Button) findViewById(R.id.bt_update);
        btBug = (Button) findViewById(R.id.bt_bug);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle("PlantNurse");
        tvVersion.setText(String.format("当前版本: %s (Build %s)", Util.getVersion(this), Util.getVersionCode(this)));
    }

    @Override
    public void initController() {

        btCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHtml(getString(R.string.app_html));
            }
        });
        btBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHtml("http://cookied.top");
            }
        });

        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.copyToClipboard(getString(R.string.alipay), getApplicationContext());
            }
        });
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt));
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_app)));
            }
        });
        btBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHtml(Constants.REPORT_URL);
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.checkVersion(AboutActivity.this);
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
        return R.layout.activity_about;
    }

    private void goToHtml(String url) {
        Uri uri = Uri.parse(url);   //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);           //指定Action
        intent.setData(uri);                            //设置Uri
        startActivity(intent);        //启动Activity
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
