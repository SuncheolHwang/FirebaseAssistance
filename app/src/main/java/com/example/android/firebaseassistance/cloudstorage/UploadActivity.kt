package com.example.android.firebaseassistance.cloudstorage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.android.firebaseassistance.R
import com.example.android.firebaseassistance.databinding.ActivityUploadBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.io.File

class UploadActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUploadBinding

    val REQ_CODE_SELECT_MODE = 1000

    var mImgPath: String? = null
    var mImgTitle: String? = null
    var mImgOrient: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgUploadBtn.setOnClickListener(this)

        getGallery()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_SELECT_MODE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                getImageNameToUri(uri)

                try {
                    var bitmap: Bitmap? = null
                    bitmap = if (Build.VERSION.SDK_INT >= 29) {
                        val source = ImageDecoder.createSource(contentResolver, uri!!)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    }
                    binding.showImg.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgUploadBtn -> uploadFile(mImgPath!!)
        }
    }

    private fun getGallery() {
        var intent: Intent? = null

        intent = if (Build.VERSION.SDK_INT >= 19) {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        } else {
            Intent(Intent.ACTION_GET_CONTENT)
        }

        intent.type = "image/*"
        startActivityForResult(intent, REQ_CODE_SELECT_MODE)
    }

    private fun getImageNameToUri(data: Uri?) {
        val proj = arrayOf(MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.ORIENTATION)

        val cursor = data?.let { this.contentResolver.query(it, proj, null, null, null) }
        cursor?.moveToFirst()

        val columnData = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val columnTitle = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
        val columnOrientation = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION)

        mImgPath = cursor?.getString(columnData!!)
        mImgTitle = cursor?.getString(columnTitle!!)
        mImgOrient = cursor?.getString(columnOrientation!!)

        Log.d("namjinha", "mImgPath = $mImgPath")
        Log.d("namjinha", "mImgTitle = $mImgTitle")
        Log.d("namjinha", "mImgOrient = $mImgOrient")
    }

    /**
     * Firebase Cloud Storage 파일 업로드
     */
    private fun uploadFile(aFilePath: String) {
        val file = Uri.fromFile(File(aFilePath))
        val metaData = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val uploadTask = storageRef.child("storage/${file.lastPathSegment}")
                .putFile(file, metaData)

        uploadTask.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred) / (it.totalByteCount)
            Toast.makeText(this, "Upload is $progress% done", Toast.LENGTH_SHORT).show()
        }.addOnPausedListener {
            Log.d("namjinha", "Upload is paused")
        }.addOnFailureListener {
            Log.d("namjinha", "Upload Exception!!")
        }.addOnSuccessListener {
            Toast.makeText(this, "업로드가 완료되었습니다.!!",
                Toast.LENGTH_SHORT).show()

            val name = storageRef.child("storage/${file.lastPathSegment}").name
            val path = storageRef.child("storage/${file.lastPathSegment}").path

            Log.d("namjinha", "name = $name")
            Log.d("namjinha", "path = $path")

            //실시간 데이터베이스 업데이트 합니다.
            writeNewImageInfoToDB(name, path)
        }
    }

    private fun writeNewImageInfoToDB(name: String, path: String) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("images")

        val info = UploadInfo()
        info.name = name
        info.path = path

        val key = databaseReference.push().key
        Log.d("namjinha", "key = $key")
        databaseReference.child(key!!).setValue(info)
    }
}