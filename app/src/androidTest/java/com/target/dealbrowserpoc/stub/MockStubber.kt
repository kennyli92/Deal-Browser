package com.target.dealbrowserpoc.stub

import com.target.dealbrowserpoc.dagger.Injector

/**
 * This class should provide the defined stubbed behaviors for the mock doubles used in the UI
 * tests for all app states
 */
class MockStubber(injector: Injector) {
  private val mockViewModuleStubber = MockViewModuleStubber(injector = injector)

  init {
    mockViewModuleStubber.inject()
  }

  private val dealsStateStubber = DealsStateStubber(
    mockViewModuleStubber = mockViewModuleStubber
  )

  fun dealsState(dealsState: DealsState) {
    when (dealsState) {
      is DealsState.GetDeals -> dealsStateStubber.onGetDeals(state = dealsState)
    }
  }
}