package com.target.dealbrowserpoc.dagger.module

import android.app.Application
import dagger.Module

@Module
class MockAppModule(private val app: Application) {
  // DO NOT MOCK THIRD PARTY LIBRARIES OR ANDROID COMPONENTS
}