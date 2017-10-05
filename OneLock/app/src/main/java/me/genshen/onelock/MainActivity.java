package me.genshen.onelock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    private DevicePolicyManager policyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, LockReceiver.class);
        if (policyManager.isAdminActive(componentName)) {//
            policyManager.lockNow();//
            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            activeManager();//
        }
    }

    @Override
    protected void onResume() {//
        if (policyManager.isAdminActive(componentName)) {
            policyManager.lockNow();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        super.onResume();
    }

    private void activeManager() {
        //
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "lock screen");
        startActivity(intent);
    }

}
