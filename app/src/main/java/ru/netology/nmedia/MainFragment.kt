package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val adapter =  PostAdapter(viewModel)

        viewModel.onCancelEdit()
        viewModel.onCancelOpen()

        binding.listOfPosts.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { postsList ->

            adapter.submitList(postsList)

        }

        binding.fabNewPost.setOnClickListener {

            viewModel.onCancelEdit()

            findNavController().navigate(R.id.action_main_fragment_to_edit_post_fragment)

        }


        viewModel.editedPost.observe(viewLifecycleOwner) { post ->

            if (post.id != 0) {

                findNavController().navigate(R.id.action_main_fragment_to_edit_post_fragment)

            }

        }

        viewModel.openedPost.observe(viewLifecycleOwner) { post ->

            if (post.id != 0) {

                findNavController().navigate(R.id.action_nav_main_fragment_to_alone_post_fragment)

            }

        }

        return binding.root

    }

    companion object {

        fun newInstance() = MainFragment()

    }

}