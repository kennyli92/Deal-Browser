package com.target.dealbrowserpoc.dagger.component

import com.target.dealbrowserpoc.dagger.module.ViewModule
import com.target.dealbrowserpoc.dagger.scope.ViewScope
import dagger.Subcomponent

/**
 * All injects should be declared in [ViewComponent]. All plus component methods lives here
 */
@ViewScope
@Subcomponent(modules = [ViewModule::class])
interface ProdViewComponent : ViewComponent