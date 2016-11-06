package com.anglus.map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Anglus on 2016/10/18.
 */

public class BaseActivity extends Activity {

    // 湖科大 经纬度坐标
    protected LatLng hnustLatlng = new LatLng(27.8957165, 112.9203713);
    // 北京天安门 经纬度坐标
    protected LatLng beijinLatlng = new LatLng(39.9054936, 116.395443);

    protected MapView mMapView = null;
    protected BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

//        6. 获取地图 Ui 控制器，隐藏指南针
//        UiSettings uiSettings = baiduMap.getUiSettings();
//        uiSettings.setCompassEnabled(false);
    }

    /**
     * 在屏幕中心显示一个 Toast
     *
     * @param text
     */
    public void showToast(CharSequence text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
