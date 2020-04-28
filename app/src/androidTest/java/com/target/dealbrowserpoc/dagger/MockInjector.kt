package com.target.dealbrowserpoc.dagger

import com.target.dealbrowserpoc.App
import com.target.dealbrowserpoc.dagger.component.AppComponent
import com.target.dealbrowserpoc.dagger.component.DaggerMockAppComponent
import com.target.dealbrowserpoc.dagger.component.MockAppComponent
import com.target.dealbrowserpoc.dagger.component.MockViewComponent
import com.target.dealbrowserpoc.dagger.component.ViewComponent
import com.target.dealbrowserpoc.dagger.module.MockAppModule
import com.target.dealbrowserpoc.dagger.module.MockViewModule

class MockInjector : Injector {
  private lateinit var _appComponent: MockAppComponent
  private var _viewComponent: MockViewComponent? = null

  override fun initializeAppComponent(app: App) {
    val mockAppModule = MockAppModule(app = app)

    _appComponent = DaggerMockAppComponent.builder()
      .mockAppModule(mockAppModule)
      .build()
  }

  override val appComponent: AppComponent
    get() = _appComponent

  override val viewComponent: ViewComponent
    get() {
      if (_viewComponent == null) {
        _viewComponent = _appComponent.plusViewComponent(mockViewModule = MockViewModule())
      }

      return _viewComponent!!
    }
}