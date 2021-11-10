package xyz.xyz0z0.photoutils

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var btnSelectPhoto: Button
    private lateinit var btnTakePhoto: Button
    private lateinit var ivImage: ImageView

    private val getContent = registerForActivityResult(ActivityResultContracts.TakePicture()) {

    }

    val FILE_TIME_FORMAT = "yyyy_MM_dd_HH_mm_ss_SSS"
    val FILE_DATE_FORMAT: DateFormat = SimpleDateFormat(FILE_TIME_FORMAT, Locale.CHINA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findView()
        btnSelectPhoto.setOnClickListener {
            PhotoUtilsFragment.selectPhoto(this, object : OnPhotoCallback {
                override fun onSuccess(uri: Uri) {
                    LogUtils.json("xxx", uri.path)
                    ToastUtils.showShort("成功")
                    ivImage.setImageURI(uri)
                }


                override fun onFail() {
                    ToastUtils.showShort("失败")
                }

            })
        }


        btnTakePhoto.setOnClickListener {
            PhotoUtilsFragment.takePhoto(this, object : OnPhotoCallback {
                override fun onSuccess(uri: Uri) {
                    LogUtils.json("xxx", uri.path)
                    ToastUtils.showShort("成功")
                    ivImage.setImageURI(uri)
                }


                override fun onFail() {
                    ToastUtils.showShort("失败")
                }

            })
        }


    }

    private fun findView() {
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        ivImage = findViewById(R.id.ivImage)
    }


}