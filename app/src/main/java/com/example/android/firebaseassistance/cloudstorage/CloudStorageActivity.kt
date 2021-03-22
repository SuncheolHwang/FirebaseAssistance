package com.example.android.firebaseassistance.cloudstorage

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.android.firebaseassistance.R
import com.example.android.firebaseassistance.databinding.ActivityCloudStorageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CloudStorageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCloudStorageBinding

    companion object {
        const val REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloudStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.also {
            it.uploadBtn.setOnClickListener(this)
            it.downloadBtn.setOnClickListener(this)
            it.metaInfoBtn.setOnClickListener(this)
            it.deleteBtn.setOnClickListener(this)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
                Toast.makeText(this,
                    "안드로이드 6.0부터 마시멜로부터 일부 권한에 대해 사용자에게 동의 필요!",
                    Toast.LENGTH_LONG).show()

                binding.apply {
                    uploadBtn.isEnabled = false
                    downloadBtn.isEnabled = false
                    metaInfoBtn.isEnabled = false
                    deleteBtn.isEnabled = false
                }
            }
        }
    }

    override fun onClick(v: View?) {
        var intent: Intent? = null

        when (v?.id) {
            R.id.uploadBtn -> intent = Intent(this, UploadActivity::class.java)
            R.id.downloadBtn -> intent = Intent(this, DownloadActivity::class.java)
            R.id.metaInfoBtn -> intent = Intent(this, MetaInfoActivity::class.java)
            R.id.deleteBtn -> deleteFile()
        }

        if (intent != null) {
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    binding.apply {
                        uploadBtn.isEnabled = true
                        downloadBtn.isEnabled = true
                        metaInfoBtn.isEnabled = true
                        deleteBtn.isEnabled = true
                    }
                }
            }
        }
    }

    private fun deleteFile() {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val desertRef = storageRef.child("storage/IMG_20210320_132159.jpg")
        desertRef.delete().addOnSuccessListener {

        }.addOnFailureListener {

        }
    }
}