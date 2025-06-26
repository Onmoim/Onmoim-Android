package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Location
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.LocationApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationApi: LocationApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : LocationRepository {
    override fun searchLocation(query: String): Flow<List<Location>> = flow {
        val resp = locationApi.searchLocation(query)
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val locations = data.map {
                Location(
                    id = it.locationId,
                    code = it.code,
                    city = it.city,
                    district = it.district,
                    dong = it.dong
                )
            }
            emit(locations)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)
}