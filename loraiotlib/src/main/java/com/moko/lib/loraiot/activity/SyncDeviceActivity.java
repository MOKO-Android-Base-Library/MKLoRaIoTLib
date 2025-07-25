package com.moko.lib.loraiot.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.DisplayCutout;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.moko.lib.loraiot.IoTDMConstants;
import com.moko.lib.loraiot.R;
import com.moko.lib.loraiot.Urls;
import com.moko.lib.loraiot.adapter.SyncDeviceAdapter;
import com.moko.lib.loraiot.databinding.ActivityDevicesBinding;
import com.moko.lib.loraiot.dialog.LogoutDialog;
import com.moko.lib.loraiot.entity.CommonResp;
import com.moko.lib.loraiot.entity.SyncDevice;
import com.moko.lib.loraui.dialog.LoadingDialog;
import com.moko.lib.loraui.utils.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.RequestBody;

public class SyncDeviceActivity extends FragmentActivity implements BaseQuickAdapter.OnItemClickListener {
    private ActivityDevicesBinding mBind;
    private ArrayList<SyncDevice> devices;
    private SyncDeviceAdapter adapter;
    public Handler mHandler;
    private int mDeviceModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setOnApplyWindowInsetsListener((v, insets) -> {
            DisplayCutout cutout = insets.getDisplayCutout();
            if (cutout != null) {
                List<Rect> rects = cutout.getBoundingRects();
                if (rects.size() != 0) {
                    getWindow().getDecorView().setPadding(cutout.getSafeInsetLeft(), cutout.getSafeInsetTop(),
                            cutout.getSafeInsetRight(), cutout.getSafeInsetBottom());
                }
            }
            return insets;
        });
        mBind = ActivityDevicesBinding.inflate(getLayoutInflater());
        devices = getIntent().getParcelableArrayListExtra(IoTDMConstants.EXTRA_KEY_SYNC_DEVICES);
        mDeviceModel = getIntent().getIntExtra(IoTDMConstants.EXTRA_KEY_DEVICE_MODEL, 0);
        adapter = new SyncDeviceAdapter();
        adapter.openLoadAnimation();
        adapter.replaceData(devices);
        adapter.setOnItemClickListener(this);
        mBind.rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        mBind.rvDeviceList.setAdapter(adapter);
        mHandler = new Handler(Looper.getMainLooper());
        setContentView(mBind.getRoot());
    }


    public void onSyncDevices(View view) {
        if (isWindowLocked()) return;
        if (devices.isEmpty()) {
            ToastUtils.showToast(this, "Add devices first");
            return;
        }
        List<SyncDevice> syncDevices = adapter.getData();
        List<SyncDevice> selectedDevices = new ArrayList<>(syncDevices);
        selectedDevices.removeIf(device -> !device.isSelected);
        syncDevices(selectedDevices);
    }

    private void syncDevices(List<SyncDevice> syncDevices) {
        RequestBody body = RequestBody.create(Urls.JSON, new Gson().toJson(syncDevices));
        String url = Urls.syncGatewayApi(getApplicationContext());
        if (mDeviceModel >= 200)
            url = Urls.syncCellularGatewayApi(getApplicationContext());
        OkGo.<String>post(url)
                .upRequestBody(body)
                .execute(new StringCallback() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingProgressDialog();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        Type type = new TypeToken<CommonResp<JsonObject>>() {
                        }.getType();
                        CommonResp<JsonObject> commonResp = new Gson().fromJson(response.body(), type);
                        if (commonResp.code != 200) {
                            ToastUtils.showToast(SyncDeviceActivity.this, commonResp.msg);
                            return;
                        }
                        ToastUtils.showToast(SyncDeviceActivity.this, "Sync Success");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showToast(SyncDeviceActivity.this, R.string.request_error);
                    }

                    @Override
                    public void onFinish() {
                        dismissLoadingProgressDialog();
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SyncDevice syncDevice = (SyncDevice) adapter.getItem(position);
        if (syncDevice == null)
            return;
        boolean isSelected = syncDevice.isSelected;
        syncDevice.isSelected = !isSelected;
        adapter.notifyItemChanged(position);
    }


    public void onBack(View view) {
        if (isWindowLocked()) return;
        back();
    }

    @Override
    public void onBackPressed() {
        if (isWindowLocked()) return;
        back();
    }

    private void back() {
        finish();
    }

    public void onAccount(View view) {
        if (isWindowLocked()) return;
        LogoutDialog dialog = new LogoutDialog();
        dialog.setOnLogoutClicked(() -> {
            back();
        });
        dialog.show(getSupportFragmentManager());
    }

    // 记录上次页面控件点击时间,屏蔽无效点击事件
    protected long mLastOnClickTime = 0;

    public boolean isWindowLocked() {
        long current = SystemClock.elapsedRealtime();
        if (current - mLastOnClickTime > 500) {
            mLastOnClickTime = current;
            return false;
        } else {
            return true;
        }
    }

    private LoadingDialog mLoadingDialog;

    public void showLoadingProgressDialog() {
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.show(getSupportFragmentManager());

    }

    public void dismissLoadingProgressDialog() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismissAllowingStateLoss();
    }
}
