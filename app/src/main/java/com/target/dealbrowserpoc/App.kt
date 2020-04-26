package com.target.dealbrowserpoc

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.target.dealbrowserpoc.dagger.Injector
import com.target.dealbrowserpoc.dagger.ProdInjector

class App(injector: Injector = ProdInjector()) : Application(), Injector by injector {

  override fun onCreate() {
    super.onCreate()
    initializeAppComponent(app = this)
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }
}