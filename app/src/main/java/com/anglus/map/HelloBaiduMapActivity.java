package com.anglus.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;

import static com.baidu.mapapi.SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR;
import static com.baidu.mapapi.SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR;

public class HelloBaiduMapActivity extends BaseActivity {

    private static final String TAG = "HelloBaiduMapActivity";

    private BroadcastReceiver receiver;

    @Override
    public void init() {
        // 注册SDK 检测监听广播
        registerSDKCheckReceiver();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

//        5. 更新地图状态
        MapStatusUpdate mapStatusUpdate = null;
        switch (keyCode) {
            case KeyEvent.KEYCODE_1:// 1) 缩小
                mapStatusUpdate = MapStatusUpdateFactory.zoomOut();
                break;

            case KeyEvent.KEYCODE_2:// 2) 放大
                mapStatusUpdate = MapStatusUpdateFactory.zoomIn();
                break;

            case KeyEvent.KEYCODE_3:// 3) 旋转（0 ~ 360），每次在原来的基础上再旋转30度
                MapStatus currentMapStatus = baiduMap.getMapStatus();
                float rotate = currentMapStatus.rotate + 30;
                Log.i(TAG, "newRotate = " + rotate);
                MapStatus mapStatus = new MapStatus.Builder().rotate(rotate).build();
                mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                break;

            case KeyEvent.KEYCODE_4:// 4) 俯仰（0 ~ -45），每次在原来的基础上再俯仰-5度
                currentMapStatus = baiduMap.getMapStatus();
                float overlook = currentMapStatus.overlook - 5;
                mapStatus = new MapStatus.Builder().rotate(overlook).build();
                mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                break;

            case KeyEvent.KEYCODE_5:// 5) 移动 --- 切换至北京
                mapStatusUpdate = MapStatusUpdateFactory.newLatLng(beijinLatlng);
                baiduMap.animateMapStatus(mapStatusUpdate);
                return super.onKeyDown(keyCode, event);
        }
        baiduMap.setMapStatus(mapStatusUpdate);
        return super.onKeyDown(keyCode, event);
    }

    private void registerSDKCheckReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR.equals(action)) {
                    showToast("网络出问题了");
                } else if (SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR.equals(action)) {
                    showToast("key 出错了");
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        // 监听网络错误
        filter.addAction(SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        // 监听百度SDK 的 key 是否正确
        filter.addAction(SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
