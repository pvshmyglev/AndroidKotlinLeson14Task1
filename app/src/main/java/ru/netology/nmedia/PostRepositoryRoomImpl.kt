package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {

    val postsInBase = dao.getAll()

    override val data: MutableLiveData<List<Post>> = MutableLiveData(postsInBase.map { it.toPost() })

    override fun likeById(id: Int) {

        data.value = data.value?.map {post ->
            if (post.id != id) {

                post

            } else {

                val updatedPost = post.copy(likeByMe = !post.likeByMe, countLikes = post.countLikes + if (post.likeByMe) -1 else 1)

                dao.save(updatedPost.toPostEntity()).toPost()

            }
        }

    }

    override fun shareById(id: Int) {

        data.value = data.value?.map {post ->
            if (post.id != id) {

                post

            } else {

                val updatedPost = post.copy(countShare = post.countShare + 1)

                dao.save(updatedPost.toPostEntity()).toPost()

            }
        }

    }

    override fun removeById(id: Int) {

        dao.removeById(id)

        data.value = data.value?.filter { it.id != id }

    }

    override fun saveNewPost(post: Post) {

        val listOfNewPost: List<Post> = listOf(

            dao.save( post.copy(
                id = 0,
                author = "Me",
                publishedDate = "Now",
                likeByMe = false
            ).toPostEntity()).toPost()
        )

        if (data.value == null) data.value = listOfNewPost else data.value?.let {
            data.value = listOfNewPost + data.value!!
        }

    }

    override fun editPost(post: Post) {

        data.value = data.value?.map {oldPost ->
            if (oldPost.id != post.id) {

                oldPost

            } else {

                dao.save(post.toPostEntity()).toPost()

            }
        }

    }

    override fun findPostById(id: Int): Post {

        return dao.findPostById(id).toPost()

    }

}