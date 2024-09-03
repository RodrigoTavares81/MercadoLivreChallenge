package com.thedev.mercadolivre.core.network.di

import com.thedev.mercadolivre.core.network.products.ProductsApi
import com.thedev.mercadolivre.core.network.products.ProductsRepository
import com.thedev.mercadolivre.core.network.products.ProductsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitModule.provideRetrofit()

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductsApi =
        RetrofitModule.provideMercadoLibreApi(retrofit)

    @Provides
    @Singleton
    fun provideProductsRepository(productsApi: ProductsApi): ProductsRepository =
        ProductsRepositoryImpl(productsApi)
}