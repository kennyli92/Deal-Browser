package com.target.dealbrowserpoc.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    fun providesContext(): Context {
        return app.applicationContext
    }
}
