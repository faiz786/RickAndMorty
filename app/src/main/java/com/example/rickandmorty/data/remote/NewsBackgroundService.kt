package com.example.rickandmorty.data.remote

////import com.example.rickandmorty.data.entities.CharacterList
//import com.example.rickandmorty.data.entities.NewsDbEntity
import androidx.viewbinding.BuildConfig
import com.example.rickandmorty.data.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsBackgroundService {
    @GET("/svc/mostpopular/v2/mostviewed/{newsType}/{period}.json")
    suspend fun getMostViewedNews(
        @Path("newsType") newsType: String = "all-sections",
        @Path("period") period: Int,
        @Query("api-key") apiKey: String = "CLXJpRmEttXp48ZNFE05rw9s2X8YglY3"
    ): Response<NewsResponse?>

//    @GET("character/{id}")
//    suspend fun getCharacter(@Path("id") id: Int): Response<Character>
}