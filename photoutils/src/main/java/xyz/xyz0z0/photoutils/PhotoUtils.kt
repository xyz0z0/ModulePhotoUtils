package xyz.xyz0z0.photoutils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity

/**
 * Author: Cheng
 * Date: 2021/11/16 17:17
 * Description: xyz.xyz0z0.photoutils
 */
object PhotoUtils {

    const val TAG = "PhotoUtils"
    const val CONTENT_IMAGE = "image/*"
    const val PHOTO_TYPE = "photo_type"
    const val SYSTEM_CAMERA = "system_camera"
    const val SELECT_PHOTO = 1
    const val TAKE_PHOTO = 2

    /**
     * 选择照片
     */
    fun selectPhoto(activity: FragmentActivity, callback: ((uri: Uri?) -> Unit)) {
        val fragment = PhotoUtilsFragment()
        val bundle = Bundle()
        bundle.putInt(PHOTO_TYPE, SELECT_PHOTO)
        fragment.arguments = bundle
        fragment.setSelectCallBack(callback)
        fragment.setPhotoFlag(true)
        fragment.attachActivity(activity)
    }

    /**
     * 调用系统相机拍照
     * systemCamera 调用系统相机拍照
     */
    fun takePhoto(activity: FragmentActivity, systemCamera: Boolean = true, callback: ((path: String?) -> Unit)) {
        val fragment = PhotoUtilsFragment()
        val bundle = Bundle()
        bundle.putInt(PHOTO_TYPE, TAKE_PHOTO)
        bundle.putBoolean(SYSTEM_CAMERA, systemCamera)
        fragment.arguments = bundle
        fragment.setTakeCallBack(callback)
        fragment.setPhotoFlag(true)
        fragment.attachActivity(activity)
    }

    private lateinit var mCameraPackage: String

    /**
     * 获取系统相机包名
     */
    fun getSystemCameraPackageName(context: Context): String {
        if (!this::mCameraPackage.isInitialized) {
            var cameraInfo: ResolveInfo? = null
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val pkgList: List<ResolveInfo> = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (pkgList.isNotEmpty()) {
                cameraInfo = pkgList[0]
            }
            mCameraPackage = cameraInfo?.activityInfo?.packageName ?: ""
        }
        return mCameraPackage
    }

}