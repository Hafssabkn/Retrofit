package com.example.myrotrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    private val _post = MutableLiveData<Post?>()
    val post: LiveData<Post?> = _post

    private val _createPostResult = MutableLiveData<Boolean>()
    val createPostResult: LiveData<Boolean> = _createPostResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchPosts() {
        viewModelScope.launch {
            val response = repository.getPosts()
            if (response != null) {
                _posts.value = response
            } else {
                _errorMessage.value = "Erreur lors du chargement des posts"
            }
        }
    }

    fun fetchPostById(postId: Int) {
        viewModelScope.launch {
            val response = repository.getPostById(postId)
            _post.value = response
            if (response == null) {
                _errorMessage.value = "Post non trouvé"
            }
        }
    }

    fun createPost(title: String, body: String) {
        if (title.isEmpty() || body.isEmpty()) {
            _errorMessage.value = "Veuillez remplir tous les champs"
            return
        }

        viewModelScope.launch {
            val newPost = Post(
                userId = 1,
                id = 0,
                title = title,
                body = body
            )
            val response = repository.createPost(newPost)
            _createPostResult.value = response != null
            if (response == null) {
                _errorMessage.value = "Erreur lors de la création du post"
            }
        }
    }
}