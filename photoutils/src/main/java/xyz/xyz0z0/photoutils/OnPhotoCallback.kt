package xyz.xyz0z0.photoutils

import android.net.Uri

/**
 * Author: Cheng
 * Date: 2021/11/10 16:24
 * Description: xyz.xyz0z0.modulephotoutils
 */
interface OnPhotoCallback {

    fun onSuccess(uri: Uri)

    fun onFail()
}