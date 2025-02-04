package com.soten.electronic_frame

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    private val addPhotoButton: Button by lazy {
        findViewById(R.id.addPhotoButton)
    }

    private val startPhotoFrameModeButton: Button by lazy {
        findViewById(R.id.startPhotoFrameModeButton)
    }

    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.image_1_1))
            add(findViewById(R.id.image_1_2))
            add(findViewById(R.id.image_1_3))
            add(findViewById(R.id.image_2_1))
            add(findViewById(R.id.image_2_2))
            add(findViewById(R.id.image_2_3))
        }
    }

    private val imageUriList: MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAddPhotoButton()
        initStartPhotoFrameModeButton()
    }

    private fun initAddPhotoButton() {
        addPhotoButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    navigatePhotos()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else -> {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
            }
        }
    }

    private fun initStartPhotoFrameModeButton() {
        startPhotoFrameModeButton.setOnClickListener {
            val intent = Intent(this, PhotoFrameActivity::class.java)
            imageUriList.forEachIndexed { index, uri ->
                intent.putExtra("photo$index", uri.toString())
            }
            intent.putExtra("photoListSize", imageUriList.size)
            startActivity(intent)
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
                .setTitle("권한이 필요합니다.")
                .setMessage("전자액자 앱에서 사진을 불러오기 위해 권한이 필요합니다.")
                .setPositiveButton("동의하기") { _, _ ->
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
                .setNegativeButton("취소하기") { _, _ -> }
                .create()
                .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navigatePhotos()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {  }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when(requestCode) {
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                selectedImageUri?.let {
                    if (imageUriList.size == 6) {
                        Toast.makeText(this, "이미 사진이 꽉 찼습니다다.", Toast.LENGTH_SHORT).show()
                        return@let
                    }

                    imageUriList.add(selectedImageUri)
                    imageViewList[imageUriList.size - 1].setImageURI(selectedImageUri)
                } ?: run {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
             }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }
}