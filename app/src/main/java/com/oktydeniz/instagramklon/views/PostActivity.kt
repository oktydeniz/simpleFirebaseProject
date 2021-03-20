package com.oktydeniz.instagramklon.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.oktydeniz.instagramklon.databinding.ActivityPostBinding
import java.util.*
import kotlin.collections.HashMap

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private var uri: Uri? = null
    private lateinit var bitmap: Bitmap
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var firebasefirestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInit()
        actions()
    }

    private fun firebaseInit() {
        auth = FirebaseAuth.getInstance()
        firebasefirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
    }

    private fun actions() {
        binding.setImageView.setOnClickListener {
            permissions()
        }
        binding.sendPotsButton.setOnClickListener {
            sendData()
        }
    }


    private fun sendData() {
        uri?.let {
            val currentUser = auth.currentUser
            val comment = binding.postCommentText.text.toString()
            val uuid = UUID.randomUUID()
            val imageName = "images/$uuid.jpg"
            storageReference.child(imageName).putFile(uri!!).addOnSuccessListener {

                //Download Url
                val reference = FirebaseStorage.getInstance().getReference(imageName)
                reference.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    val userMail = currentUser!!.email

                    val hashMap = HashMap<String, Any>()
                    hashMap["userMail"] = userMail!!
                    hashMap["comment"] = comment
                    hashMap["url"] = url
                    hashMap["date"] = FieldValue.serverTimestamp()
                    //When image added to Storage next step will be add the data to firestore database
                    firebasefirestore.collection("Posts").add(hashMap).addOnCompleteListener {
                        Toast.makeText(this@PostActivity, "Done !", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@PostActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)

                    }
                }
            }
        } ?: Toast.makeText(this, "Image can not be empty", Toast.LENGTH_SHORT).show()
    }

    private fun permissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1001
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1002)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 1002)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            data?.let {
                uri = data.data
                try {
                    bitmap = if (Build.VERSION.SDK_INT >= 28) {
                        val imageSource = ImageDecoder.createSource(this.contentResolver, uri!!)
                        ImageDecoder.decodeBitmap(imageSource)
                    } else {
                        MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    }
                    binding.setImageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}