package com.example.rickandmorty.ui.characters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.rickandmorty.data.entities.NewsDbEntity
import com.example.rickandmorty.data.repository.NewsRepository
import com.example.rickandmorty.utils.Resource
import kotlinx.coroutines.Dispatchers

class CharactersViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    fun getNewsFromRemote() =  liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getAllNewsRemote()))// get from remote
        }catch (e:Exception)
        {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
        }

    }

    fun getNewsFromDatabase():LiveData<List<NewsDbEntity>>
    {
       return repository.getAllNewsFromDB()// get from database
    }

    suspend fun insertAllNewsInDB(newsDBEntity:List<NewsDbEntity>)
    {
        return repository.insertAllNewsInDB(newsDBEntity)// get from database
    }

}
