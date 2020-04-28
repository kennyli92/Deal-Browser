package com.target.dealbrowserpoc.dagger.component

import com.target.dealbrowserpoc.dagger.module.MockViewModule
import com.target.dealbrowserpoc.dagger.scope.ViewScope
import dagger.Subcomponent

/**
 * All injects should be declared in [ViewComponent]. All plus component methods lives here
 */
@ViewScope
@Subcomponent(modules = [MockViewModule::class])
interface MockViewComponent : ViewComponent