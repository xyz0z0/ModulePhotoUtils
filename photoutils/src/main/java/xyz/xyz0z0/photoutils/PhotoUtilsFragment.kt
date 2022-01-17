package xyz.xyz0z0.photoutils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PhotoUtilsFragment : Fragment() {


    private val fileDateFormat: DateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.CHINA)

    private var mPhotoFlag: Boolean = false


    private var mSelectPhotoCallBack: ((uri: Uri?) -> Unit) = {

    }

    private var mTakeCallBack: ((path: String?) -> Unit) = {

    }

    fun setSelectCallBack(callback: (uri: Uri?) -> Unit) {
        this.mSelectPhotoCallBack = callback
    }

    fun setTakeCallBack(callback: (path: String?) -> Unit) {
        this.mTakeCallBack = callback
    }


    // 绑定 Activity
    fun attachActivity(activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction().add(this, this.toString())
            .commitAllowingStateLoss()
    }

    // 解绑 Activity
    private fun detachActivity(activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }

    fun setPhotoFlag(flag: Boolean) {
        this.mPhotoFlag = flag
    }

    override fun onResume() {
        super.onResume()
        if (!mPhotoFlag) {
            detachActivity(requireActivity())
            return
        }
        handleByMe()
    }

    private var mTempUri: Uri? = null
    private var mTempFile: File? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            detachActivity(requireActivity())
            val uri = data?.data
            if (resultCode == Activity.RESULT_OK && uri != null) {
                mSelectPhotoCallBack.invoke(uri)
            } else {
                mSelectPhotoCallBack.invoke(null)
            }
        } else if (requestCode == 200) {
            detachActivity(requireActivity())
            val file = mTempFile
            if (resultCode == Activity.RESULT_OK && file != null) {
                mTakeCallBack.invoke(file.absolutePath)
            } else {
                mTakeCallBack.invoke(null)
            }
        }

    }

    private fun handleByMe() {
        val arguments: Bundle? = arguments
        val activity: Activity? = activity
        if (arguments == null || activity == null) {
            return
        }
        when (val type = arguments.getInt(PhotoUtils.PHOTO_TYPE)) {
            PhotoUtils.SELECT_PHOTO -> {
                mTempUri = null
                mTempFile = null
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = PhotoUtils.CONTENT_IMAGE
                startActivityForResult(intent, 100)
            }
            PhotoUtils.TAKE_PHOTO -> {
                val systemCamera = arguments.getBoolean(PhotoUtils.SYSTEM_CAMERA)
                val saveFileDir = File(requireContext().externalCacheDir, "PhotoUtils")
                saveFileDir.mkdirs()
                val fileName = fileDateFormat.format(Date()) + ".jpg"
                val file = File(saveFileDir, fileName)
                mTempFile = file
                mTempUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (systemCamera) {
                    val cameraPackage = PhotoUtils.getSystemCameraPackageName(requireContext())
                    if (cameraPackage.isEmpty()) {
                        Log.e(PhotoUtils.TAG, "Camera Package Name Is Empty")
                        return
                    }
                    intent.setPackage(cameraPackage)
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri)
                startActivityForResult(intent, 200)

            }
            else -> {
                throw RuntimeException("Not Support This Type $type")
            }
        }
    }


}