package com.target.dealbrowserpoc.stub

import com.target.dealbrowserpoc.dagger.Injector
import com.target.dealbrowserpoc.dagger.component.MockViewComponent
import com.target.dealbrowserpoc.dagger.module.MockViewModule
import com.target.dealbrowserpoc.deals.data.DealsApi
import com.target.dealbrowserpoc.deals.data.DealsRepository
import javax.inject.Inject

/**
 * This class provides [MockViewModule] dependencies
 */
class MockViewModuleStubber(private val injector: Injector) : MockModuleStubber {
  override fun inject() {
    (injector.viewComponent as MockViewComponent).inject(this)
  }

  @set:Inject
  lateinit var dealsApi: DealsApi

  @set:Inject
  lateinit var dealsRepository: DealsRepository
}