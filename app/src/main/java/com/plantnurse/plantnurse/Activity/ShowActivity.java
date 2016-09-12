package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.ChangeInfoResponse;
import com.plantnurse.plantnurse.Network.GetPlantInfoResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.CommentModel;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.CommentAdapter;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.plantnurse.plantnurse.R.id.button_send;


/**
 * Created by Heloise on 2016/9/1.
 */

public class ShowActivity extends KSimpleBaseActivityImpl implements IBaseAction {

    //需要动态填充的数据
    private String mName;
    private String mSpecies;
    private String mdifficulty;
    private int mSunIndex;
    private int mWaterIndex;
    private int mColdIndex;
    private String mIntrodution;
    private int mId;
    private List<CommentModel> sourceDateList;

    //需要初始化的控件
    private ImageView banner_planticon;//放图片的
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout_name;//放名字的
    private TextView text_species;
    private TextView text_difficulty;
    private RatingBar ratingBar_sun;
    private RatingBar ratingBar_water;
    private RatingBar ratingBar_cold;
    private TextView text_introdution;
    private FloatingActionButton button_collect;
    private FloatingActionButton button_adopt;
    private FloatingActionsMenu menu;
    private EditText comment;
    private Button send;
    private RecyclerView commentList;
    private static CommentAdapter adapter;//评论的适配器

    private boolean iscollected = false;
    private boolean isadopted = false;
    private String username;
    private String mDate;
    private String mComment;

    @Override
    public int initLocalData() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView(ViewGroup view) {
        banner_planticon = (ImageView) view.findViewById(R.id.bannner);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout_name = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout2);
        text_species = (TextView) view.findViewById(R.id.text_species);
        text_difficulty = (TextView) view.findViewById(R.id.text_difficulty);
        ratingBar_sun = (RatingBar) view.findViewById(R.id.rating_sun);
        ratingBar_water = (RatingBar) view.findViewById(R.id.rating_water);
        ratingBar_cold = (RatingBar) view.findViewById(R.id.rating_cold);
        text_introdution = (TextView) view.findViewById(R.id.text_introdution);
        menu = (FloatingActionsMenu) view.findViewById(R.id.floatingmenu);
        send = (Button) view.findViewById(button_send);
        comment = (EditText) view.findViewById(R.id.editText_conmment);
        commentList = (RecyclerView) view.findViewById(R.id.commentListView);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSimpleApplicationContext().isLogined()) {
                    //获取需要的信息，发给服务器
                    UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
                    username = userInfo.getuserName();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    mDate = formatter.format(curDate);
                    mComment = comment.getText().toString();
                    //名字mName，用户名username，时间mDate，内容mComment
                    Log.e("时间格式", mDate);
                    if (mComment.equals("")) {
                        ToastUtil.showShort("发送的内容不能为空！");
                    } else {
                        //给你空间发挥
                        HashMap<String, String> param = new HashMap<String, String>();
                        param.put("plantName", mName);
                        param.put("date", mDate);
                        param.put("userName", username);
                        param.put("comment", mComment);
                        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), ShowActivity.this, ChangeInfoResponse.class, param, Constants.ADDCOMMENT_URL, NetworkTask.GET) {
                            @Override
                            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                                ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                                if (response.getresponseCode() == 1) {
                                    //发送成功
                                    CommentModel commentModel = new CommentModel();
                                    commentModel.setName(username);
                                    commentModel.setTime(mDate.substring(0, 4) + "." + mDate.substring(4, 6) + "." + mDate.substring(6, 8)
                                            + " " + mDate.substring(8, 10) + ":" + mDate.substring(10, 12));
                                    commentModel.setIconUrl(Constants.AVATAR_URL + "?id=" + username);
                                    commentModel.setComment(mComment);
                                    sourceDateList.add(commentModel);
                                    adapter.updatelist(sourceDateList);
                                    ToastUtil.showLong("发送成功，下滑刷新哦");
                                    comment.setText("");
                                    comment.setHint("说两句");
                                }
                            }

                            @Override
                            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                            }
                        });
                    }
                } else {
                    new SweetAlertDialog(ShowActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请先登录")
                            .show();
                }


            }
        });

        button_collect = (FloatingActionButton) view.findViewById(R.id.button_collect);
        button_collect.setSize(FloatingActionButton.SIZE_MINI);
        iscollected = false;
        if (getSimpleApplicationContext().isLogined()) {
            for (int i = 0; i < DataManager.getMyStar().response.size(); i++) {
                if (DataManager.getMyStar().response.get(i).name.equals(mName)) {
                    iscollected = true;
                }
            }
        }
        if (iscollected) {
            button_collect.setIcon(R.drawable.ic_collection2);
            button_collect.setTitle(" 取消收藏 ");
        } else {
            button_collect.setIcon(R.drawable.ic_collection1);
            button_collect.setTitle(" 我要收藏 ");
        }
        button_collect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (!iscollected) {
                    //收藏按钮，实现收藏界面更新(未实现)
                    //判断是否登录
                    if (!getSimpleApplicationContext().isLogined()) {
                        new SweetAlertDialog(ShowActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("请先登录!")
                                .show();
                    } else {
                        button_collect.setIcon(R.drawable.ic_collection2);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String mDate = formatter.format(curDate);
                        UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
                        String username = userInfo.getuserName();
                        HashMap<String, String> param = new HashMap<String, String>();
                        param.put("owner", username);
                        param.put("date", mDate);
                        param.put("plant_id", mId + "");
                        param.put("name", mName);
                        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), ShowActivity.this, ChangeInfoResponse.class, param, Constants.NEWSTAR_URL, NetworkTask.GET) {
                            @Override
                            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                                ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                                if (response.getresponseCode() == 1) {
                                    new SweetAlertDialog(ShowActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("收藏成功!")
                                            .show();
                                    button_collect.setTitle(" 取消收藏 ");
                                }
                            }

                            @Override
                            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                            }
                        });
                        iscollected = true;
                    }
                } else {
                    //取消收藏
                    UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
                    String username = userInfo.getuserName();
                    HashMap<String, String> param = new HashMap<String, String>();
                    param.put("userName", username);
                    param.put("name", mName);
                    SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), ShowActivity.this, ChangeInfoResponse.class, param, Constants.DELETESTAR_URL, NetworkTask.GET) {
                        @Override
                        public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                            ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                            if (response.getresponseCode() == 1) {
                                new SweetAlertDialog(ShowActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("取消成功!")
                                        .show();
                            }
                            button_collect.setIcon(R.drawable.ic_collection1);
                            button_collect.setTitle(" 我要收藏 ");
                            iscollected = false;
                        }

                        @Override
                        public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                        }
                    });
                }
            }
        });


        button_adopt = (FloatingActionButton) view.findViewById(R.id.button_adopt);
        button_adopt.setSize(FloatingActionButton.SIZE_MINI);
        button_adopt.setTitle(" 我要养它 ");
        button_adopt.setIcon(R.drawable.ic_adoption1);
        //收养按钮，跳转到增加植物界面
        button_adopt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (getSimpleApplicationContext().isLogined()) {
                    if (!isadopted) {
                        button_adopt.setTitle(" 我已收养 ");
                        isadopted = true;
                        Intent intent = new Intent(ShowActivity.this, AddplantActivity.class);
                        intent.putExtra("addplant", 1);
                        intent.putExtra("name", mName);
                        intent.putExtra("sun", mSunIndex);
                        intent.putExtra("water", mWaterIndex);
                        intent.putExtra("snow", mColdIndex);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ShowActivity.this, AddplantActivity.class);
                        startActivity(intent);
                    }
                } else {
                    new SweetAlertDialog(ShowActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请先登录")
                            .show();
                }

            }
        });
//

    }


    private void updateIndex() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentList.setLayoutManager(linearLayoutManager);
        adapter = new CommentAdapter(ShowActivity.this, sourceDateList);
        commentList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initController() {

    }

    @Override
    public void onLoadingNetworkData() {
        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mId = intent.getIntExtra("id", 0);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("name", mName);
        //判断是否已经收藏
        //是否登录
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), this, GetPlantInfoResponse.class, param, Constants.PLANTINFO_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                GetPlantInfoResponse response = (GetPlantInfoResponse) result.resultObject;
                mSpecies = response.species;
                mSunIndex = response.sun;
                mWaterIndex = response.water;
                mColdIndex = response.cold;
                int difficult = response.difficulty;
                switch (difficult) {
                    case 0:
                        mdifficulty = "种植难度：非常简单";
                        break;
                    case 1:
                        mdifficulty = "种植难度：简单";
                        break;
                    case 2:
                        mdifficulty = "种植难度：一般";
                        break;
                    case 3:
                        mdifficulty = "种植难度：有点难";
                        break;
                    case 4:
                        mdifficulty = "种植难度：困难";
                        break;
                    case 5:
                        mdifficulty = "种植难度：非常困难";
                        break;
                    default:
                        mdifficulty = " ";
                        break;
                }

                mIntrodution = response.introduction;
                toolbarLayout_name.setTitle(mName);
                Picasso.with(ShowActivity.this).load(Constants.PLANTPIC_URL + mId).into(banner_planticon);

                text_species.setText(mSpecies);
                text_difficulty.setText(mdifficulty);
                ratingBar_sun.setProgress(mSunIndex);
                ratingBar_water.setProgress(mWaterIndex);
                ratingBar_cold.setProgress(mColdIndex);
                text_introdution.setText(mIntrodution);
                sourceDateList = new ArrayList<CommentModel>();
                for (int i = 0; i < response.commentlist.size(); i++) {
                    CommentModel commentModel = new CommentModel();
                    commentModel.setName(response.commentlist.get(i).userName);
                    commentModel.setTime(response.commentlist.get(i).date.substring(0, 4) + "."
                            + response.commentlist.get(i).date.substring(4, 6) + "." +
                            response.commentlist.get(i).date.substring(6, 8) + " " + response.
                            commentlist.get(i).date.substring(8, 10) + ":" + response.commentlist.
                            get(i).date.substring(10, 12));
                    commentModel.setIconUrl(Constants.AVATAR_URL + "?id=" + response.commentlist.get(i).userName);
                    commentModel.setComment(response.commentlist.get(i).comment);
                    sourceDateList.add(commentModel);
                }
                updateIndex();
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }

    @Override
    public void onLoadedNetworkData(View view) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_showplant;
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
