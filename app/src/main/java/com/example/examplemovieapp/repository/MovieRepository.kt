package com.example.examplemovieapp.repository

import com.example.examplemovieapp.data.model.MovieList

interface MovieRepository {
    suspend fun getUpcomingMovie(): MovieList
    suspend fun getTopRatedMovie(): MovieList
    suspend fun getPopularMovie(): MovieList
}