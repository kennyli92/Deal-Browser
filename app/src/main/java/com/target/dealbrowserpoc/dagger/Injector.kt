package com.target.dealbrowserpoc.dagger

import android.app.Application
import com.target.dealbrowserpoc.dagger.component.AppComponent
import com.target.dealbrowserpoc.dagger.component.ViewComponent

interface Injector {
    fun initializeAppComponent(app: Application)

    val appComponent: AppComponent

    val viewComponent: ViewComponent
}