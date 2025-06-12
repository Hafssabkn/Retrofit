package com.example.myrotrofit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnFetch: Button
    private lateinit var btnSearch: Button
    private lateinit var btnCreatePost: Button
    private lateinit var etPostId: EditText
    private lateinit var tvPostDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        btnFetch = findViewById(R.id.btnFetch)
        btnSearch = findViewById(R.id.btnSearch)
        btnCreatePost = findViewById(R.id.btnCreatePost)
        etPostId = findViewById(R.id.etPostId)
        tvPostDetails = findViewById(R.id.tvPostDetails)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        btnFetch.setOnClickListener {
            viewModel.fetchPosts()
        }

        btnSearch.setOnClickListener {
            val postId = etPostId.text.toString().toIntOrNull()
            if (postId != null && postId > 0) {
                viewModel.fetchPostById(postId)
            } else {
                Toast.makeText(this, "Veuillez entrer un ID valide", Toast.LENGTH_SHORT).show()
            }
        }

        btnCreatePost.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        // Observer la liste des posts
        viewModel.posts.observe(this) { posts ->
            recyclerView.adapter = PostAdapter(posts)
        }

        viewModel.post.observe(this) { post ->
            if (post != null) {
                tvPostDetails.text = """
                    ID: ${post.id}
                    User ID: ${post.userId}
                    Titre: ${post.title}
                    Contenu: ${post.body}
                """.trimIndent()
            } else {
                tvPostDetails.text = "Aucun post trouvé avec cet ID"
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPosts()
    }
}