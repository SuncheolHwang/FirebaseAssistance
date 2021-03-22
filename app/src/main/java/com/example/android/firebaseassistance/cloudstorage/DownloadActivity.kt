package com.example.android.firebaseassistance.cloudstorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.android.firebaseassistance.R
import com.example.android.firebaseassistance.databinding.ActivityDownloadBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class DownloadActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDownloadBinding

    private var localFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.localImgDownloadBtn.setOnClickListener(this)
        binding.imgFirebaseUidnBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.localImgDownloadBtn -> showDownloadLocalFileImageView()
            R.id.imgFirebaseUidnBtn -> showFirebaseDownloadImageView()
        }
    }

    private fun showDownloadLocalFileImageView() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val pathReference = storageRef.child("storage/IMG_20210320_132159.jpg")

        try {
            localFile = File.createTempFile("images", "jpg")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (localFile != null) {
            pathReference.getFile(localFile!!)
                .addOnSuccessListener {
                val fileSize = it.totalByteCount
                Log.d("namjinha", "File Size = $fileSize")
                Log.d("namjinha", "FIle Name = ${localFile!!.absolutePath}")

                Glide.with(this)
                    .load(File(localFile!!.absolutePath))
                    .into(binding.fcStorageImg)
            }.addOnFailureListener {
                Log.d("namjibnha", "onFailure in")
            }
        }
    }

    private fun showFirebaseDownloadImageView() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val pathReference = storageRef.child("storage/IMG_20210320_132159.jpg")

        GlideApp.with(this)
            .load(pathReference)
            .into(binding.fcStorageImg)
    }
}