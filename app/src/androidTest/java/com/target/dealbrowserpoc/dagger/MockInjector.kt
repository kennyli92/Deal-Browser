package com.target.dealbrowserpoc.dagger

import android.app.Application
import com.target.dealbrowserpoc.dagger.component.AppComponent
import com.target.dealbrowserpoc.dagger.component.MockAppComponent
import com.target.dealbrowserpoc.dagger.component.MockViewComponent
import com.target.dealbrowserpoc.dagger.component.ViewComponent
import com.target.dealbrowserpoc.dagger.module.AppModule
import com.target.dealbrowserpoc.dagger.module.MockViewModule

class MockInjector : Injector {
  private lateinit var _appComponent: MockAppComponent
  private var _viewComponent: MockViewComponent? = null

  override fun initializeAppComponent(app: Application) {
    val appModule = AppModule(app = app)

    _appComponent = DaggerMockAppComponent.builder()
      .appModule(appModule)
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