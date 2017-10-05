package me.genshen.onelock;

/**
 * Created by 根深 on 2015/10/22.
 */
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class LockReceiver extends DeviceAdminReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        System.out.println("onreceiver");
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        System.out.println("激活使用");
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        System.out.println("取消激活");
        super.onDisabled(context, intent);
    }


}