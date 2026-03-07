package com.artificialss.showcase.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache

object ApolloClientProvider {

    fun create(): ApolloClient = ApolloClient.Builder()
        .serverUrl(GRAPHQL_ZERO_URL)
        .normalizedCache(MemoryCacheFactory(maxSizeBytes = CACHE_SIZE_BYTES))
        .build()

    private const val GRAPHQL_ZERO_URL = "https://graphqlzero.almansi.me/api"
    private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024
}
