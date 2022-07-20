package com.example.rickandmorty.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.entities.NewsDbEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM news_table")
    fun getAllNews() : LiveData<List<NewsDbEntity>>

    @Query("SELECT * FROM news_table WHERE id = :id")
    fun getNews(id: Int): LiveData<NewsDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newsDbEntity: List<NewsDbEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsDbEntity: NewsDbEntity)

}