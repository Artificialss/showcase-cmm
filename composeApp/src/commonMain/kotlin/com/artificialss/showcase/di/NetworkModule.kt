package com.artificialss.showcase.di

import com.apollographql.apollo.ApolloClient
import com.artificialss.showcase.data.remote.ApolloClientProvider
import org.koin.dsl.module

val networkModule = module {
    single<ApolloClient> { ApolloClientProvider.create() }
}
