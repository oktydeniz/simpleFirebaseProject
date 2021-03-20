package com.oktydeniz.instagramklon.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.oktydeniz.instagramklon.R
import com.oktydeniz.instagramklon.adapters.PostAdapter
import com.oktydeniz.instagramklon.databinding.ActivityMainBinding
import com.oktydeniz.instagramklon.models.PostModel

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private var postsList = ArrayList<PostModel>()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authInit()

        getData()
    }

    private fun getData() {
        val reference = firestore.collection("Posts")
        reference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null) Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT)
                .show()
            value?.let {
                for (item in it) {
                    val comment = item.get("comment").toString()
                    val url = item.get("url").toString()
                    val user = item.get("userMail").toString()
                    val post = PostModel(comment, url, user)
                    postsList.add(post)
                }
                adapter = PostAdapter(postsList)
                binding.feedRecyclerView.adapter = adapter
            }
        }
    }

    private fun authInit() {
        auth = FirebaseAuth.getInstance()

        userStatus()
    }

    private fun userStatus() {
        val user = auth.currentUser
        if (user == null) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.add_new_post -> {
                val intent = Intent(this, PostActivity::class.java)
                startActivity(intent)
            }
            R.id.sing_out -> {
                auth.signOut()
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}