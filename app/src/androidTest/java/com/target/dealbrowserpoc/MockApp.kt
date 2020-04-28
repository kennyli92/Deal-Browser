package com.target.dealbrowserpoc

import com.target.dealbrowserpoc.dagger.Injector
import com.target.dealbrowserpoc.dagger.MockInjector

class MockApp(injector: Injector = MockInjector()) : App(injector = injector) {
  override fun onCreate() {
    super.onCreate()
    initializeAppComponent(app = this)
  }
}