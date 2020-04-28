package com.target.dealbrowserpoc

import android.app.Application
import com.target.dealbrowserpoc.dagger.Injector
import com.target.dealbrowserpoc.dagger.MockInjector

class MockApp(injector: Injector = MockInjector()) : Application(), Injector by injector {

  override fun onCreate() {
    super.onCreate()
    initializeAppComponent(app = this)
  }
}