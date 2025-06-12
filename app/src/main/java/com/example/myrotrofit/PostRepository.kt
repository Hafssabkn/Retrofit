package com.example.myrotrofit

class PostRepository {
    private val apiService = RetrofitInstance.api
    suspend fun getPosts(): List<Post>? {
        return try {
            apiService.getPosts()
        } catch (e: Exception) {
            null
        }
    }
}
