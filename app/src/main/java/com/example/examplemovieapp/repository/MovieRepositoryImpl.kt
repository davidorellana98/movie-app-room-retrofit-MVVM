package com.example.examplemovieapp.repository

import com.example.examplemovieapp.core.InternetCheck
import com.example.examplemovieapp.data.local.LocalMovieDataSource
import com.example.examplemovieapp.data.model.MovieList
import com.example.examplemovieapp.data.model.toMovieEntity
import com.example.examplemovieapp.data.remove.RemoteMovieDataSource

class MovieRepositoryImpl(
    private val dataSourceRemote: RemoteMovieDataSource,
    private val dataSourceLocal: LocalMovieDataSource
) : MovieRepository {

    override suspend fun getUpcomingMovie(): MovieList {
        return if (InternetCheck.isNetworkAvailable()) {
            dataSourceRemote.getUpcomingMovie().results.forEach { movie ->
                dataSourceLocal.saveMovie(movie.toMovieEntity("upcoming"))
            }
            dataSourceLocal.getUpcomingMovie()
        } else {
            dataSourceLocal.getUpcomingMovie()
        }
    }

    override suspend fun getTopRatedMovie(): MovieList {
        return if (InternetCheck.isNetworkAvailable()) {
            dataSourceRemote.getTopRatedMovies().results.forEach { movie ->
                dataSourceLocal.saveMovie(movie.toMovieEntity("toprated"))
            }
            dataSourceLocal.getTopRatedMovies()
        } else {
            dataSourceLocal.getTopRatedMovies()
        }
    }

    override suspend fun getPopularMovie(): MovieList {
        return if (InternetCheck.isNetworkAvailable()) {
            dataSourceRemote.getPopularMovie().results.forEach { movie ->
                dataSourceLocal.saveMovie(movie.toMovieEntity("popular"))
            }
            dataSourceLocal.getPopularMovie()
        } else {
            dataSourceLocal.getPopularMovie()
        }
    }
}