package com.anglus.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import static com.baidu.mapapi.SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR;
import static com.baidu.mapapi.SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR;

public class HelloBaiduMapActivity extends BaseActivity {

    private static final String TAG = "HelloBaiduMapActivity";
    // 湖科大 经纬度坐标
    protected LatLng hnustLatlng = new LatLng(27.8957165, 112.9203713);
    // 北京天安门 经纬度坐标
    protected LatLng beijinLatlng = new LatLng(39.9054936,116.395443);

    private MapView mMapView = null;
    private BroadcastReceiver receiver;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 注册SDK 检测监听广播
        registerSDKCheckReceiver();
        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        baiduMap = mMapView.getMap();// 获取地图控制器

//        1. 隐藏缩放按钮、比例尺
//        mMapView.showZoomControls(false);// 缩放
//        mMapView.showScaleControl(false);// 比例尺

//        2. 获取获取最小（3）、最大缩放级别（22）
        float minZoomLevel = baiduMap.getMinZoomLevel();
        float maxZoomLevel = baiduMap.getMaxZoomLevel();
        Log.i("ZoomLevel", "最小缩放基本为 : " + minZoomLevel);
        Log.i("ZoomLevel", "最大缩放基本为 : " + maxZoomLevel);

//        3. 更改默认地图中心点
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(hnustLatlng);
        baiduMap.setMapStatus(mapStatusUpdate);

//        4. 设置地图缩放为 18
        mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(hnustLatlng, 18);
        baiduMap.setMapStatus(mapStatusUpdate);

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
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
