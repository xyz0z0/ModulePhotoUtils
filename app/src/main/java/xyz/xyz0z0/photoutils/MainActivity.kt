package xyz.xyz0z0.photoutils

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils


class MainActivity : AppCompatActivity() {

    private lateinit var btnSelectPhoto: Button
    private lateinit var btnTakePhoto: Button
    private lateinit var ivImage: ImageView


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