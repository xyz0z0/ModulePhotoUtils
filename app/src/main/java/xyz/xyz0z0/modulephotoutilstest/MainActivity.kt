package xyz.xyz0z0.modulephotoutilstest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import xyz.xyz0z0.photoutils.PhotoUtils
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var btnDefaultTakePhoto: Button
    private lateinit var btnSelectPhoto: Button
    private lateinit var btnTakePhoto: Button
    private lateinit var btnTakePhotoSecond: Button
    private lateinit var btnGetFile: Button
    private lateinit var ivImage: ImageView
    private var mUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findView()

        btnDefaultTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val cameraPackage = getSystemCameraPackageName(this)
            if (cameraPackage.isNullOrEmpty()) {
                ToastUtils.showShort("No System Camera")
                return@setOnClickListener
            }
            intent.setPackage(cameraPackage)
            val saveFileDir = File(this.externalCacheDir, "PhotoUtils")
            saveFileDir.mkdirs()
            val fileName = fileDateFormat.format(Date()) + ".jpg"
            val file = File(saveFileDir, fileName)
            mUri = FileProvider.getUriForFile(this, "$packageName.provider", file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
            startActivityForResult(intent, 2000)
        }

        btnSelectPhoto.setOnClickListener {
            PhotoUtils.selectPhoto(this) {
                if (it == null) {
                    ToastUtils.showShort("失败")
                } else {
                    LogUtils.json("xxx", it.path)
                    ToastUtils.showShort("成功")
                    Glide.with(this).load(it).into(ivImage)
                }
            }
        }


        btnTakePhoto.setOnClickListener {
            PhotoUtils.takePhoto(this) { path ->
                if (path == null) {
                    ToastUtils.showShort("失败")
                } else {
                    LogUtils.json("ccc", path)
                    ToastUtils.showShort("成功")
                    Glide.with(this).load(path).into(ivImage)
                }
            }
        }

        btnTakePhotoSecond.setOnClickListener {
            PhotoUtils.takePhoto(this,false) { path ->
                if (path == null) {
                    ToastUtils.showShort("失败")
                } else {
                    Glide.with(this).load(path).into(ivImage)
                    LogUtils.json("ccc", path)
                    ToastUtils.showShort("成功")
                }
            }
        }

        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {

        }

        btnGetFile.setOnClickListener {
            launcher.launch("application/pdf")
        }


    }

    private fun getSystemCameraPackageName(context: Context): String? {
        var cameraInfo: ResolveInfo? = null
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val pkgList: List<ResolveInfo> = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (pkgList.isNotEmpty()) {
            cameraInfo = pkgList[0]
        }
        return cameraInfo?.activityInfo?.packageName
    }

    private val fileDateFormat: DateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.CHINA)


    private fun findView() {
        btnDefaultTakePhoto = findViewById(R.id.btnDefaultTakePhoto)
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        btnTakePhotoSecond = findViewById(R.id.btnTakePhotoSecond)
        btnGetFile = findViewById(R.id.btnGetFile)
        ivImage = findViewById(R.id.ivImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                if (mUri != null) {
                    Glide.with(this).load(mUri).into(ivImage)
                }
            } else {
                ToastUtils.showShort("用户取消了拍照")
            }
        }
    }


}
