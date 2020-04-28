package com.target.dealbrowserpoc.dagger.module

import android.app.Application
import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
class MockAppModule(private val app: Application) {
  @Singleton
  @Provides
  fun providesContext(): Context {
    return mock()
  }

  @Singleton
  @Provides
  fun providesMoshi(): Moshi {
    return mock()
  }

  @Singleton
  @Provides
  fun providesRetrofit(
    moshi: Moshi
  ): Retrofit {
    return mock()
  }
}