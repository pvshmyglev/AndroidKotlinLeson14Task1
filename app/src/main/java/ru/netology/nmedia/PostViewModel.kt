package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class PostViewModel (application: Application) : AndroidViewModel(application), PostInteractionCommands{

    private val repository : PostRepository = PostRepositoryRoomImpl( AppDb.getInstance(context = application).postDao() )
    val data by repository::data
    private val emptyPost = Post(
        0,
        "",
        "",
        "",
        "",
        false,
        0,
        0,
        0
    )
    val editedPost = MutableLiveData(emptyPost)
    val openedPost = MutableLiveData(emptyPost)

    private fun setObserveEditOpenPost(id: Int) {

        if (editedPost.value?.id != 0 && editedPost.value?.id == id) {

            data.value?.map { post ->
                if (post.id == editedPost.value?.id) { editedPost.value = post }
            }

        }

        if (openedPost.value?.id != 0 && openedPost.value?.id == id) {

            data.value?.map { post ->
                if (post.id == openedPost.value?.id) { openedPost.value = post }
            }

        }

    }

    override fun onLike(post: Post) {

        repository.likeById(post.id)

        setObserveEditOpenPost(post.id)

    }

    override fun onShare(post: Post) {

        repository.shareById(post.id)

        setObserveEditOpenPost(post.id)

    }

    override fun onRemove(post: Post) {
        repository.removeById(post.id)

        onCancelEdit()
        onCancelOpen()

    }

    override fun onEditPost(post: Post) {

        editedPost.value = post

    }

    override fun onSaveContent(newContent: String) {

        val text = newContent.trim()

        editedPost.value?.let { thisEditedPost ->

            if (thisEditedPost.content != text) {

                if (thisEditedPost.id == 0) {
                    repository.saveNewPost(thisEditedPost.copy(content = text))
                } else {
                    repository.editPost(thisEditedPost.copy(content = text))
                }
            }

            editedPost.value = emptyPost

            setObserveEditOpenPost(thisEditedPost.id)

        }


    }

    override fun onCancelEdit() {

        editedPost.value = emptyPost

    }

    override fun onOpenPost(post: Post) {

        openedPost.value = post

    }

    override fun onCancelOpen() {

        openedPost.value = emptyPost

    }

}
