package com.target.dealbrowserpoc.dagger

import com.target.dealbrowserpoc.App
import com.target.dealbrowserpoc.dagger.component.AppComponent
import com.target.dealbrowserpoc.dagger.component.DaggerProdAppComponent
import com.target.dealbrowserpoc.dagger.component.ProdAppComponent
import com.target.dealbrowserpoc.dagger.component.ProdViewComponent
import com.target.dealbrowserpoc.dagger.component.ViewComponent
import com.target.dealbrowserpoc.dagger.module.AppModule
import com.target.dealbrowserpoc.dagger.module.ViewModule

class ProdInjector : Injector {
    private lateinit var _appComponent: ProdAppComponent
    private var _viewComponent: ProdViewComponent? = null

    override fun initializeAppComponent(app: App) {
        val appModule = AppModule(app = app)

        _appComponent = DaggerProdAppComponent.builder()
                .appModule(appModule)
                .build()
    }

    override val appComponent: AppComponent
        get() = _appComponent

    override val viewComponent: ViewComponent
        get() {
            if (_viewComponent == null) {
                _viewComponent = _appComponent.plusViewComponent(viewModule = ViewModule())
            }

            return _viewComponent!!
        }
}
