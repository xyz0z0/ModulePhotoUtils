package xyz.xyz0z0.photoutils

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
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

    private val takePhotoResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        detachActivity(requireActivity())
        val uri = mTempUri
        if (it && uri != null) mCallBack?.onSuccess(uri) else mCallBack?.onFail()
    }


    private val selectPhotoResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        detachActivity(requireActivity())
        if (it == null) mCallBack?.onFail() else mCallBack?.onSuccess(it)
    }

    private var mCallBack: OnPhotoCallback? = null

    fun setCallBack(callback: OnPhotoCallback) {
        this.mCallBack = callback
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

    private fun handleByMe() {
        val arguments: Bundle? = arguments
        val activity: Activity? = activity
        if (arguments == null || activity == null) {
            return
        }
        when (val type = arguments.getInt(PHOTO_TYPE)) {
            SELECT_PHOTO -> {
                mTempUri = null
                selectPhotoResult.launch(CONTENT_IMAGE)
            }
            TAKE_PHOTO -> {
                val saveFileDir = File(requireContext().externalCacheDir, "PhotoUtils")
                saveFileDir.mkdirs()
                val fileName = fileDateFormat.format(Date()) + ".jpg"
                val file = File(saveFileDir, fileName)
                mTempUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)
                takePhotoResult.launch(mTempUri)
            }
            else -> {
                throw RuntimeException("Not Support This Type $type")
            }
        }

    }

    companion object {

        private const val CONTENT_IMAGE = "image/*"
        private const val PHOTO_TYPE = "photo_type"
        private const val SELECT_PHOTO = 1
        private const val TAKE_PHOTO = 2


        fun selectPhoto(activity: FragmentActivity, callback: OnPhotoCallback) {
            val fragment = PhotoUtilsFragment()
            val bundle = Bundle()
            bundle.putInt(PHOTO_TYPE, SELECT_PHOTO)
            fragment.arguments = bundle
            fragment.setCallBack(callback)
            fragment.setPhotoFlag(true)
            fragment.attachActivity(activity)
        }

        fun takePhoto(activity: FragmentActivity, callback: OnPhotoCallback) {
            val fragment = PhotoUtilsFragment()
            val bundle = Bundle()
            bundle.putInt(PHOTO_TYPE, TAKE_PHOTO)
            fragment.arguments = bundle
            fragment.setCallBack(callback)
            fragment.setPhotoFlag(true)
            fragment.attachActivity(activity)
        }


    }


}