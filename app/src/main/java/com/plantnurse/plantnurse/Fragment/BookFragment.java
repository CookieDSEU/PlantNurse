package com.plantnurse.plantnurse.Fragment;


import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.github.promeg.pinyinhelper.Pinyin;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.Activity.AddplantActivity;
import com.plantnurse.plantnurse.Activity.MainActivity;
import com.plantnurse.plantnurse.Activity.ShowActivity;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.JsonParser;
import com.plantnurse.plantnurse.utils.PinyinComparator;
import com.plantnurse.plantnurse.utils.SideBar;
import com.plantnurse.plantnurse.utils.SortAdapter;
import com.plantnurse.plantnurse.model.SortModel;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Heloise on 2016/8/31.
 */

public class BookFragment extends KSimpleBaseFragmentImpl implements IBaseAction {

    private ListView sortListView;
    private SideBar sideBar; // 右边的引导
    private TextView dialog;
    private SortAdapter adapter; // 排序的适配器
    private FloatingSearchView searchView;
    private PinyinComparator pinyinComparator;
    private LinearLayout xuanfuLayout; // 顶部悬浮的layout
    private TextView xuanfaText; // 悬浮的文字， 和左上角的群发
    private int lastFirstVisibleItem = -1;
    private List<SortModel> sourceDateList;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private SpeechRecognizer mIat;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private RecognizerDialog mIatDialog;

    @Override
    public int initLocalData() {

        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        pinyinComparator = new PinyinComparator();
        xuanfuLayout = (LinearLayout) view.findViewById(R.id.top_layout);
        xuanfaText = (TextView) view.findViewById(R.id.top_char);
        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        searchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mIat = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
        mIatDialog = new RecognizerDialog(getActivity(), mInitListener);


    }
    /**
     * 初始化监听器。
     */
    /**
     * Created by Cookie_D on 2016/9/7.
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                ToastUtil.showShort("初始化失败，错误码：" + code);
            }
        }
    };


    //核心 成功生成了List<SortModel>,去生成那一排排列表状的东西
    private List<SortModel> filledData(String param) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < DataManager.getPlantIndex().response.size(); i++) {
            if (DataManager.getPlantIndex().response.get(i).name.contains(param)) {
                SortModel sortModel = new SortModel();
                sortModel.setName(DataManager.getPlantIndex().response.get(i).name);
                sortModel.setId(DataManager.getPlantIndex().response.get(i).id);
                sortModel.setUrl(Constants.PLANTICON_URL + DataManager.getPlantIndex().response.get(i).id);
                String pinyin = Pinyin.toPinyin(DataManager.getPlantIndex().response.get(i).name.charAt(0));
                String sortString = pinyin.substring(0, 1).toUpperCase();
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }

                mSortList.add(sortModel);
            }
        }
        if (mSortList.isEmpty()) {
            SortModel sortModel = new SortModel();
            sortModel.setName("抱歉，没有找到，点击新建");
            sortModel.setId(0);
            sortModel.setUrl(Constants.PLANTICON_URL + 0);
            String pinyin = Pinyin.toPinyin("抱歉".charAt(0));
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;
    }
    //核心 成功生成了List<SortModel>,去生成那一排排列表状的东西

    /**
     * Created by Cookie_D on 2016/9/7.
     */
    @Override
    public void initController() {
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                sourceDateList = filledData(newQuery);
                adapter.updateListView(sourceDateList);
            }
        });
        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_voice_rec) {

                    FlowerCollector.onEvent(getActivity(), "iat_recognize");

                    //searchView.setSearchText("");// 清空显示内容
                    mIatResults.clear();
                    // 设置参数
                    setParam();
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    ToastUtil.showShort("请开始说话...");
                }
            }
        });
    }

    /**
     * Created by Cookie_D on 2016/9/7.
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            ToastUtil.showShort(error.getPlainDescription(true));
        }

    };

    /**
     * Created by Cookie_D on 2016/9/7.
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }


        searchView.setSearchFocused(true);
        searchView.setSearchText(resultBuffer.toString());
    }

    @Override
    public void onLoadingNetworkData() {


    }

    public void updateindex() {
        // 填充数据
        Collections.sort(sourceDateList, pinyinComparator);
        adapter = new SortAdapter(getActivity(), sourceDateList);
        adapter.updateListView(sourceDateList);
        sortListView.setAdapter(adapter);
        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int section = adapter.getSectionForPosition(firstVisibleItem);
                int nextSecPosition = adapter
                        .getPositionForSection(section + 1);
                if (firstVisibleItem != lastFirstVisibleItem) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) xuanfuLayout
                            .getLayoutParams();
                    params.topMargin = 0;
                    xuanfuLayout.setLayoutParams(params);
                    xuanfaText.setText(String.valueOf((char) section));
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = xuanfuLayout.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) xuanfuLayout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            xuanfuLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                xuanfuLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //点击事件，以后会实现跳转
                if (((SortModel) adapter.getItem(position)).getId() != 0) {
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
                    String na = ((SortModel) adapter.getItem(position)).getName();
                    intent.putExtra("name", na);
                    intent.putExtra("id", ((SortModel) adapter.getItem(position)).getId());
                    startActivity(intent);
                } else {
                    MainActivity ma = (MainActivity) getActivity();
                    if (ma.getSimpleApplicationContext().isLogined()) {
                        Intent intent = new Intent(getActivity(), AddplantActivity.class);
                        intent.putExtra("addplant", 0);
                        startActivity(intent);
                    } else {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("请先登录!")
                                .show();
                    }
                }
            }
        });
        /**
         * 为右边添加触摸事件
         */
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });
    }

    @Override
    public void onLoadedNetworkData(View view) {
        sourceDateList = filledData("");
        updateindex();
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_book;
    }

    /**
     * Created by Cookie_D on 2016/9/7.
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }
}
