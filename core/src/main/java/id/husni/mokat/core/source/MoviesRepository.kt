package id.husni.mokat.core.source

import id.husni.mokat.core.source.local.LocalDataSource
import id.husni.mokat.core.source.remote.RemoteDataSource
import id.husni.mokat.core.source.remote.network.ApiResponse
import id.husni.mokat.core.source.remote.response.MoviesItem
import id.husni.mokat.core.domain.model.Movies
import id.husni.mokat.core.domain.repository.IMoviesRepository
import id.husni.mokat.core.utils.AppExecutors
import id.husni.mokat.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors): IMoviesRepository{

    override fun getAllMovies(): Flow<Resources<List<Movies>>> =
        object :NetworkBoundResource<List<Movies>, List<MoviesItem>>(){
            override fun loadFromDB(): Flow<List<Movies>> {
                return localDataSource.getAllMovies().map { DataMapper.mapEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Movies>?): Boolean =
                data == null || data.isEmpty()


            override suspend fun createCall(): Flow<ApiResponse<List<MoviesItem>>> =
                remoteDataSource.getAllMovies()


            override suspend fun saveCallResult(data: List<MoviesItem>) {
                val movieList = DataMapper.mapResponseToEntities(data)
                localDataSource.insertMovies(movieList)
            }
        }.asFlow()

    override fun getFavouriteMovies(): Flow<List<Movies>> {
        return localDataSource.getFavorite().map { DataMapper.mapEntitiesToDomain(it) }
    }

    override fun setFavouriteMovies(movies: Movies, newState: Boolean){
        val moviesEntity = DataMapper.mapDomainToEntity(movies)
        appExecutors.diskIO().execute { localDataSource.setFavorite(moviesEntity,newState) }
    }
}