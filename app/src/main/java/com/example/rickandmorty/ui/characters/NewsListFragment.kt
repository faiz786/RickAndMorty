package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.data.entities.NewsDbEntity
import com.example.rickandmorty.data.models.News
import com.example.rickandmorty.data.models.NewsImage
import com.example.rickandmorty.databinding.CharactersFragmentBinding
import com.example.rickandmorty.utils.Resource
import com.example.rickandmorty.utils.autoCleared
import com.example.rickandmorty.utils.isConnectedToInternet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*

@AndroidEntryPoint
class NewsListFragment : Fragment()/* CharactersAdapter.CharacterItemListener*/ {

    private var binding: CharactersFragmentBinding by autoCleared()
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        val listener = NewsAdapter.OnClickListener(){
                employeeSelected ->

            val action = NewsListFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(employeeSelected)
            findNavController().navigate(
                action
            )
        }
        adapter = NewsAdapter (listener)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        if (requireActivity().isConnectedToInternet()) {
            viewModel.getNewsFromRemote().observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        binding.linearProgressIndicator.visibility = View.GONE
                        if (it.data != null) {
                            var newsList: ArrayList<News> = ArrayList()
                            val images = mutableListOf<NewsImage>()
                            var newsDBList: ArrayList<NewsDbEntity> = ArrayList()
                            lateinit var newsDbEntity: NewsDbEntity
                            for (i in it.data!!.body()!!.newsItemRetros) {

                                for (media in i.mediaItemRetros) {
                                    if (media.type == "image" && media.mediaMetadataRetros.isNotEmpty()) {
                                        val largestImageUrl =
                                            media.mediaMetadataRetros[media.mediaMetadataRetros.size - 1].url
                                        images.add(NewsImage(media.caption, media.copyright, largestImageUrl))
                                    }

//                                for (j in i.mediaItemRetros) {
//                                    var newsImage = NewsImage(
//                                        j.caption,
//                                        j.copyright,
//                                        j.mediaMetadataRetros[0].url
//                                    )
//                                    newsImages.add(newsImage)
                                    newsDbEntity = NewsDbEntity(
                                        i.id,
                                        i.title,
                                        i.newsAbstract,
                                        i.publishedDate,
                                        i.type,
                                        i.author,
                                        i.source,
                                        i.url,
                                        media.caption,
                                        media.copyright,
                                        media.mediaMetadataRetros[media.mediaMetadataRetros.size - 1].url
                                    )
//                                }
                                }
                                var news = News(
                                    i.id,
                                    i.title,
                                    i.newsAbstract,
                                    i.publishedDate,
                                    (i.section + " " + i.subsection).trim(),
                                    i.author,
                                    i.source,
                                    i.url,
                                    images
                                )
                                newsList.add(news)
                                newsDBList.add(newsDbEntity)
                            }

                            adapter.submitList(newsList)
                            viewModel.viewModelScope.launch {
                                viewModel.insertAllNewsInDB(newsDBList)
                            }


                        }
                    }
                    Resource.Status.ERROR ->
                        Toast.makeText(requireContext(), "Error in fetching news "+it.message, Toast.LENGTH_SHORT).show()

                    Resource.Status.LOADING ->
                        binding.linearProgressIndicator.visibility = View.VISIBLE
                }
            })
        }
        else {
            viewModel.getNewsFromDatabase().observe(requireActivity(), Observer {
                var newsList: ArrayList<News> = ArrayList()
                var newsImages: ArrayList<NewsImage> = ArrayList()

                for (i in it) {
                    newsImages.add(NewsImage(i.caption, i.copyright, i.imageUrl))
                    var news = News(
                        i.id,
                        i.title,
                        i.newsAbstract,
                        i.publishDate,
                        i.category,
                        i.author,
                        i.source,
                        i.url,
                        newsImages
                    )
                    newsList.add(news)
                }
                adapter.submitList(newsList)
            })
        }
    }

//    override fun onClickedCharacter(characterId: Int) {
//        findNavController().navigate(
//            R.id.action_charactersFragment_to_characterDetailFragment,
//            bundleOf("id" to characterId)
//        )
//    }
}
