package com.example.myrotrofit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class CreatePostActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    private lateinit var etTitle: EditText
    private lateinit var etBody: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        initViews()
        setupClickListeners()
        observeViewModel()
    }

    private fun initViews() {
        etTitle = findViewById(R.id.etTitle)
        etBody = findViewById(R.id.etBody)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun setupClickListeners() {
        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val body = etBody.text.toString().trim()

            if (title.isNotEmpty() && body.isNotEmpty()) {
                btnSave.isEnabled = false
                btnSave.text = "Enregistrement..."
                viewModel.createPost(title, body)
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.createPostResult.observe(this) { success ->
            btnSave.isEnabled = true
            btnSave.text = "Enregistrer"

            if (success) {
                Toast.makeText(this, "Post créé avec succès!", Toast.LENGTH_LONG).show()
                // Nettoyer les champs après succès
                etTitle.text.clear()
                etBody.text.clear()
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            btnSave.isEnabled = true
            btnSave.text = "Enregistrer"
        }
    }
}