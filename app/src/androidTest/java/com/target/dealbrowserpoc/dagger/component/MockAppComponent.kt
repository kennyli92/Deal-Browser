package com.target.dealbrowserpoc.dagger.component

import com.target.dealbrowserpoc.dagger.module.AppModule
import com.target.dealbrowserpoc.dagger.module.MockViewModule
import dagger.Component
import javax.inject.Singleton

/**
 * All injects should be declared in [AppComponent]. All plus component methods lives here
 */
@Singleton
@Component(modules = [AppModule::class])
interface MockAppComponent : AppComponent {
  fun plusViewComponent(mockViewModule: MockViewModule): MockViewComponent
}