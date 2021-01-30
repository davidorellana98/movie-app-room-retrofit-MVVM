package com.example.examplemovieapp.data.remove

import com.example.examplemovieapp.aplication.AppConstants
import com.example.examplemovieapp.data.model.MovieList
import com.example.examplemovieapp.repository.WebService

class RemoteMovieDataSource(private val webService: WebService) {

    suspend fun getUpcomingMovie(): MovieList = webService.getUpcomingMovie(AppConstants.API_KEY)
    suspend fun getTopRatedMovies(): MovieList = webService.getTopRatedMovie(AppConstants.API_KEY)
    suspend fun getPopularMovie(): MovieList = webService.getPopularMovie(AppConstants.API_KEY)
}