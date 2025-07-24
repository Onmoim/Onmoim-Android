package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Place
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.KakaoApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class KakaoRepositoryImpl @Inject constructor(
    private val kakaoApi: KakaoApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : KakaoRepository {
    override fun searchKeyword(
        query: String
    ): Flow<List<Place>> = flow {
        val resp = kakaoApi.localSearchKeyword(
            query = query
        )
        val data = resp.body()

        if (resp.isSuccessful && data != null) {
            val places = data.documents?.filter {
                it.id != null && it.placeName != null && it.x != null && it.y != null
            }?.map {
                Place(
                    id = it.id!!,
                    title = it.placeName!!,
                    address = it.roadAddressName ?: it.addressName ?: "",
                    phoneNumber = it.phone ?: "",
                    latitude = it.y!!.toDouble(),
                    longitude = it.x!!.toDouble()
                )
            } ?: emptyList()
            emit(places)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)
}