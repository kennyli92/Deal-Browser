package com.target.dealbrowserpoc.dagger

import com.target.dealbrowserpoc.App
import com.target.dealbrowserpoc.dagger.component.AppComponent
import com.target.dealbrowserpoc.dagger.component.ViewComponent

interface Injector {
    fun initializeAppComponent(app: App)

    val appComponent: AppComponent

    val viewComponent: ViewComponent
}