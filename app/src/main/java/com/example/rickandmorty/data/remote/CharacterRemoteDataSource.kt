package com.example.rickandmorty.data.remote

import com.example.rickandmorty.utils.Resource
import javax.inject.Inject

class CharacterRemoteDataSource @Inject constructor(
    private val newsBackgroundService: NewsBackgroundService
): BaseDataSource() {

//    suspend fun getNewsFromRemote():Resource<T> = getResult { characterService.getAllCharacters() }

    suspend fun getNewsFromRemote() = newsBackgroundService.getMostViewedNews(period = 7)
}