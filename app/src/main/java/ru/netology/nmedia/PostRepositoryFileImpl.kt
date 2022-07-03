package ru.netology.nmedia

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostRepositoryFileImpl(private val context: Context) : PostRepository {

    private val gson = Gson()
    private val typeListOfPosts = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val fileName = "posts.json"
    private var nextId = 1
    override val data : MutableLiveData<List<Post>>

    init {

        var posts: List<Post> = emptyList()

        val fileOfPosts = context.filesDir.resolve(fileName)

        if (fileOfPosts.exists()) {


            context.openFileInput(fileName).bufferedReader().use {

                try { posts = gson.fromJson(it, typeListOfPosts) }
                catch(e: Exception){  }

            }

            nextId = posts.getMaxGuidInList()

            data = MutableLiveData(posts)

        } else {

            posts = listOf(

                Post(
                    nextId++,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "18–20 апреля в Москве проходила I Международная Ассамблея Российской академии образования «Ученик в современном мире: формула успеха». Миссия Ассамблеи заключалась в акцентуации новых и оптимизации имеющихся подходов к внедрению на уровнях основного общего и среднего общего образования релевантных методов обучения и воспитания, образовательных технологий, отвечающих запросам современного общества, создание эффективной среды для личностного и предпрофессионального развития обучающихся, личностного и профессионального развития педагогов. http://www.ivanovo.ac.ru/about_the_university/news/11502/",
                    "",
                    "15 мая 2022 года. 14:50:34",
                    false,
                    999,
                    2000,
                    131
                ),
                Post(
                    nextId++,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "Тест, второй пост",
                    "https://www.youtube.com/watch?v=JDxuBbsua_E",
                    "16 мая 2022 года. 14:50:34",
                    true,
                    121,
                    995,
                    1000000
                ),
                Post(
                    nextId++,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "Как писать крутые посты в VK, чтобы их сохраняли, лайкали и комментировали\n" +
                            "Вкусные тексты, которые взахлеб читают и делятся ими с друзьями - это ли не мечта каждого, кто продвигает свой аккаунт в социальных сетях?\n" +
                            "В VK есть свои нюансы, и их нужно учитывать, когда пишите посты. Людям не нужны сложные, заумные статьи. Они заходят сюда отдохнуть, расслабиться и почитать нечто легкое и увлекательное. Современный человек не может долго удерживать свое внимание, он читает бегло и быстро теряет интерес. Поделюсь своим опытом каким правилам следовал чтобы увеличить аудиторию группы магазина Фитнес Элита. Задача была не просто получить новых подписчиков, а сделать из них клиентов. Ниша спортивного питания сильно переполнена и чтобы привлечь подписчика, а впоследствии сделать из него покупателя нужно что-то уникальное.",
                    "",
                    "17 мая 2022 года. 14:50:34",
                    false,
                    999,
                    2000,
                    131
                ),
                Post(
                    nextId++,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "Тест, четвертый пост",
                    "",
                    "18 мая 2022 года. 14:50:34",
                    false,
                    999,
                    2000,
                    131
                ),
                Post(
                    nextId++,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "Тест, пятый пост",
                    "",
                    "19 мая 2022 года. 14:50:34",
                    false,
                    999,
                    2000,
                    131
                )
            )

            data = MutableLiveData(posts)

            sync()

        }
    }

    override fun likeById(id: Int) {

        data.value = data.value?.map {post ->
            if (post.id != id) { post } else { post.copy(likeByMe = !post.likeByMe, countLikes = post.countLikes + if (post.likeByMe) -1 else 1) }
        }

        sync()

    }

    override fun shareById(id: Int) {

        data.value = data.value?.map {post ->
            if (post.id != id) { post } else { post.copy(countShare = post.countShare + 1) }
        }

        sync()

    }

    override fun removeById(id: Int) {

        data.value = data.value?.filter { it.id != id }

        sync()

    }

    override fun saveNewPost(post: Post) {

        val listOfNewPost: List<Post> = listOf(
            post.copy(
                id = nextId++,
                author = "Me",
                publishedDate = "Now",
                likeByMe = false
            )
        )

        if (data.value == null) data.value = listOfNewPost else data.value?.let {
            data.value = listOfNewPost + data.value!!
        }

        sync()

    }

    override fun editPost(post: Post) {

        data.value = data.value?.map {oldPost ->
            if (oldPost.id != post.id) { oldPost } else { post }
        }

        sync()

    }

    override fun findPostById(id: Int): Post {

        sync()

        data.value?.map {post ->
            if (post.id != id) { return post }
        }

        return Post(0, "", "", "", "", false, 0, 0, 0)

    }

    private fun sync() {

        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {

            it.write(gson.toJson(data.value))

        }

    }


    //Получает максимальный идентификатор в коллекции, ограниченное использование
    private fun <T> List<T>.getMaxGuidInList(): Int {

        var currentMaxId = 0

        for (value in this) {
            val id: Int = when{
                (value is Post) -> (value as Post).id
                (value is Int) -> value
                else -> 0}
            if (id >= currentMaxId)  currentMaxId = id
        }

        return  currentMaxId + 1

    }

}