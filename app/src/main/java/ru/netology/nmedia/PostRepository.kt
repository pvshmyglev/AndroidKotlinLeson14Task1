package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {

    val data : LiveData<List<Post>>
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun saveNewPost(post: Post)
    fun editPost(post: Post)
    fun findPostById(id: Int) : Post
}