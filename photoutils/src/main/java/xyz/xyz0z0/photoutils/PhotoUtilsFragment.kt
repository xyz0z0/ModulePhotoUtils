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
        val file = mTempFile
        if (it && file != null) mTakeCallBack?.onSuccess(file.absolutePath) else mSelectPhotoCallBack?.onFail()
    }


    private val selectPhotoResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        detachActivity(requireActivity())
        if (it == null) mSelectPhotoCallBack?.onFail() else mSelectPhotoCallBack?.onSuccess(it)
    }

    private var mSelectPhotoCallBack: SelectCallback? = null
    private var mTakeCallBack: TakePhotoCallback? = null

    fun setSelectCallBack(callback: SelectCallback) {
        this.mSelectPhotoCallBack = callback
    }

    fun setTakeCallBack(callback: TakePhotoCallback) {
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
                selectPhotoResult.launch(PhotoUtils.CONTENT_IMAGE)
            }
            PhotoUtils.TAKE_PHOTO -> {
                val saveFileDir = File(requireContext().externalCacheDir, "PhotoUtils")
                saveFileDir.mkdirs()
                val fileName = fileDateFormat.format(Date()) + ".jpg"
                val file = File(saveFileDir, fileName)
                mTempFile = file
                mTempUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)
                takePhotoResult.launch(mTempUri)
            }
            else -> {
                throw RuntimeException("Not Support This Type $type")
            }
        }

    }


}