package com.anglus.map;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Anglus on 2016/10/18.
 */

public class BaseActivity extends Activity {

    /**
     * 在屏幕中心显示一个 Toast
     * @param text
     */
    public void showToast(CharSequence text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
