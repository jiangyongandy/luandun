package com.jy.xinlangweibo.ui.activity.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by JIANG on 2017/1/19.
 */
@RuntimePermissions
public class BasePermissionActivity extends BaseActivity {

    /**
     * 权限回调接口
     */
    public abstract class PermissionHandler {
        /**
         * 权限通过
         */
        public abstract void onGranted();

        /**
         * 权限拒绝
         */
        public void onDenied() {
        }
    }

    private PermissionHandler mHandler;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BasePermissionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //-----------------------------------------------------------
    /**
     * 请求相机权限
     *
     * @param permissionHandler
     */
    protected void requestCameraPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BasePermissionActivityPermissionsDispatcher.handleCameraPermissionWithCheck(this);
    }


    @NeedsPermission(Manifest.permission.CAMERA)
    void handleCameraPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void deniedCameraPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void OnCameraNeverAskAgain() {

        showDialog("[相机]");
    }

    //-----------------------------------------------------------
    /**
     * 请求SD卡读取存储权限
     *
     * @param permissionHandler
     */
    protected void requestStoragePermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BasePermissionActivityPermissionsDispatcher.handleStoragePermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void handleStoragePermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void deniedStoragePermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void OnStorageNeverAskAgain() {
        showDialog("[存储及访问SD卡存储]");
    }


    //-----------------------------------------------------------
    /**
     * 请求感应器访问权限
     *
     * @param permissionHandler
     */
    protected void requestSensorsPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BasePermissionActivityPermissionsDispatcher.handleSensorsPermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.BODY_SENSORS)
    void handleSensorsPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnPermissionDenied(Manifest.permission.BODY_SENSORS)
    void deniedSensorsPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.BODY_SENSORS)
    void OnSensorsNeverAskAgain() {
        showDialog("[感应器]");
    }

    public void showDialog(String permission) {
        new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage("在设置-应用-荟医医生-权限中开启" + permission + "权限，以正常使用荟医功能")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mHandler != null) mHandler.onDenied();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

}
