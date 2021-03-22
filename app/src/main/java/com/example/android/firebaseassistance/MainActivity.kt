package com.example.android.firebaseassistance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.android.firebaseassistance.authentication.AuthActivity
import com.example.android.firebaseassistance.cloudstorage.CloudStorageActivity
import com.example.android.firebaseassistance.databinding.ActivityMainBinding
import com.example.android.firebaseassistance.firestore.FirestoreActivity
import com.example.android.firebaseassistance.readtimedb.MemoActivity
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firebaseauthbtn.setOnClickListener(this)
        binding.firebaseRealTimeDbBtn.setOnClickListener(this)
        binding.firebaseCloudFireStoreBtn.setOnClickListener(this)
        binding.firebaseCloudStorageBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.firebaseauthbtn -> {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
            R.id.firebaseRealTimeDbBtn -> {
                val intent = Intent(this, MemoActivity::class.java)
                startActivity(intent)
            }
            R.id.firebaseCloudFireStoreBtn -> {
                val intent = Intent(this, FirestoreActivity::class.java)
                startActivity(intent)
            }
            R.id.firebaseCloudStorageBtn -> {
                val intent = Intent(this, CloudStorageActivity::class.java)
                startActivity(intent)
            }
        }
    }
}