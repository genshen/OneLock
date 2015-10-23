package com.holo.onelock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;


public class MainActivity extends Activity {

    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private boolean both = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, LockReceiver.class);

        setUpAnimator(R.id.up);
        setUpAnimator(R.id.down);
    }



    @Override
    protected void onResume() {//

        if (policyManager.isAdminActive(componentName) && both) {
            policyManager.lockNow();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        super.onResume();
    }

    private void setUpAnimator(final int id) {
        final View v = this.findViewById(id);

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                setAnimatorShow(v);
            }
        });
    }


    private void  lockScreen(){
        if (policyManager.isAdminActive(componentName)) {
            policyManager.lockNow();
            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            activeManager();
        }
    }
    private void activeManager() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "lock screen");
        startActivity(intent);
    }

     private void setAnimatorShow(final View v) {
         Animator animator = ViewAnimationUtils.createCircularReveal(
                 v, v.getWidth() / 2, v.getHeight() / 2, 0, v.getWidth());
         animator.setInterpolator(new AccelerateDecelerateInterpolator());
         animator.setDuration(2000);
         animator.addListener(new AnimatorListenerAdapter() {
             @Override
             public void onAnimationEnd(Animator animation) {
                 setAnimatorDoubleHidden(v);
             }
         });
         animator.start();
     }

    private void setAnimatorDoubleHidden(final View v) {

        Animator animator = ViewAnimationUtils.createCircularReveal(
                v, v.getWidth() / 2, v.getHeight() / 2, v.getWidth(),0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(2000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.INVISIBLE);
                 if (both) {
                     lockScreen();
                 } else {
                     View con = findViewById(R.id.container);
                     con.setBackgroundColor(0xFF000000);    //第一个动画完成就应该背景变黑
                     both = true;
                 }
            }
        });
        animator.start();
    }

}