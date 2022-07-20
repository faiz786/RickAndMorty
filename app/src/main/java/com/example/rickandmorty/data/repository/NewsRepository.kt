package com.example.rickandmorty.data.repository

import androidx.lifecycle.LiveData
import com.example.rickandmorty.data.entities.NewsDbEntity
import com.example.rickandmorty.data.local.CharacterDao
import com.example.rickandmorty.data.models.NewsResponse
import com.example.rickandmorty.data.remote.CharacterRemoteDataSource
import com.example.rickandmorty.utils.Resource
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterDao
) {


    suspend fun getAllNewsRemote():Response<NewsResponse?> // get news from remote
    {
       return remoteDataSource.getNewsFromRemote()
    }

    fun getAllNewsFromDB(): LiveData<List<NewsDbEntity>> {   // get news from database
        return localDataSource.getAllNews()
    }

    suspend fun insertAllNewsInDB(newsDbEntity: List<NewsDbEntity>)
    {
        localDataSource.insertAll(newsDbEntity)
    }
}