package xyz.xyz0z0.photoutils

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * Author: Cheng
 * Date: 2021/11/16 17:17
 * Description: xyz.xyz0z0.photoutils
 */
object PhotoUtils {

    const val CONTENT_IMAGE = "image/*"
    const val PHOTO_TYPE = "photo_type"
    const val SELECT_PHOTO = 1
    const val TAKE_PHOTO = 2

    fun selectPhoto(activity: FragmentActivity, callback: SelectCallback) {
        val fragment = PhotoUtilsFragment()
        val bundle = Bundle()
        bundle.putInt(PHOTO_TYPE, SELECT_PHOTO)
        fragment.arguments = bundle
        fragment.setSelectCallBack(callback)
        fragment.setPhotoFlag(true)
        fragment.attachActivity(activity)
    }

    fun takePhoto(activity: FragmentActivity, callback: TakePhotoCallback) {
        val fragment = PhotoUtilsFragment()
        val bundle = Bundle()
        bundle.putInt(PHOTO_TYPE, TAKE_PHOTO)
        fragment.arguments = bundle
        fragment.setTakeCallBack(callback)
        fragment.setPhotoFlag(true)
        fragment.attachActivity(activity)
    }

}