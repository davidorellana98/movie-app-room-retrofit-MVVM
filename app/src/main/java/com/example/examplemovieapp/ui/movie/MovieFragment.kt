package com.example.examplemovieapp.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.example.examplemovieapp.R
import com.example.examplemovieapp.core.Resource
import com.example.examplemovieapp.data.local.AppDataBase
import com.example.examplemovieapp.data.local.LocalMovieDataSource
import com.example.examplemovieapp.data.model.Movie
import com.example.examplemovieapp.data.remove.RemoteMovieDataSource
import com.example.examplemovieapp.databinding.FragmentMovieBinding
import com.example.examplemovieapp.presentation.MovieViewModel
import com.example.examplemovieapp.presentation.MovieViewModelFactory
import com.example.examplemovieapp.repository.MovieRepositoryImpl
import com.example.examplemovieapp.repository.RetrofitClient
import com.example.examplemovieapp.ui.movie.adapter.MovieAdapter
import com.example.examplemovieapp.ui.movie.adapter.concat.PopularConcatAdapter
import com.example.examplemovieapp.ui.movie.adapter.concat.TopRatedConcatAdapter
import com.example.examplemovieapp.ui.movie.adapter.concat.UpcomingConcatAdapter


class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory(
            MovieRepositoryImpl(
                RemoteMovieDataSource(RetrofitClient.webservice),
                LocalMovieDataSource(AppDataBase.getDataBase(requireContext()).movieDao())
            )
        )
    }

    private lateinit var concatAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        concatAdapter = ConcatAdapter()

        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(0, UpcomingConcatAdapter(MovieAdapter(result.data.first.results, this@MovieFragment)))
                        addAdapter(1, TopRatedConcatAdapter(MovieAdapter(result.data.second.results, this@MovieFragment)))
                        addAdapter(2, PopularConcatAdapter(MovieAdapter(result.data.third.results, this@MovieFragment)))
                    }

                    binding.rvMovies.adapter = concatAdapter
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("Error", "${result.exception}")
                }
            }
        })
    }

    override fun onMovieClick(movie: Movie) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
            movie.poster_path,
            movie.backdrop_path,
            movie.vote_average.toFloat(),
            movie.vote_count,
            movie.overview,
            movie.title,
            movie.original_language,
            movie.release_date
        )
        findNavController().navigate(action)
    }
}