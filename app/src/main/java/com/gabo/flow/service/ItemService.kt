package com.gabo.flow.service

import com.gabo.flow.model.Content
import retrofit2.Response
import retrofit2.http.GET

interface ItemService {
    @GET(END_POINT)
    suspend fun getItems(): Response<Content>

    companion object {
        const val END_POINT = "/v3/f4864c66-ee04-4e7f-88a2-2fbd912ca5ab"
    }
}