package com.target.dealbrowserpoc.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    fun providesContext(): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://target-deals.herokuapp.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }
}
