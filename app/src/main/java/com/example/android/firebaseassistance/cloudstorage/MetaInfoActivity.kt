package com.example.android.firebaseassistance.cloudstorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.android.firebaseassistance.R
import com.example.android.firebaseassistance.databinding.ActivityMetaInfoBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MetaInfoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMetaInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMetaInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.metaBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.metaBtn -> getMetaData()
        }
    }

    private fun getMetaData() {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val forestRef = storageRef.child("storage/IMG_20210320_132159.jpg")

        forestRef.metadata.addOnSuccessListener {
            val metaData = it.name + "\n" +
                    it.path + "\n" +
                    it.bucket

            binding.metaInfoTxt.text = metaData
        }.addOnFailureListener {

        }
    }
}