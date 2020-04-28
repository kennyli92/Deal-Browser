package com.target.dealbrowserpoc.deals.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.target.dealbrowserpoc.dagger.Injector
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.data.Deal
import com.target.dealbrowserpoc.deals.data.DealsRepository
import com.target.dealbrowserpoc.extensions.application
import com.target.dealbrowserpoc.extensions.plusAssign
import com.target.dealbrowserpoc.extensions.showSnackBar
import com.target.dealbrowserpoc.log.Logging
import com.target.dealbrowserpoc.utils.DisposableOnLifecycleChange
import com.target.dealbrowserpoc.utils.ResetDependencyOnDestroy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DealDetailsFragment : Fragment() {
  @set:Inject
  var dealsRepository: DealsRepository by ResetDependencyOnDestroy<DealsRepository>()

  private val disposables: CompositeDisposable by DisposableOnLifecycleChange()
  private lateinit var vm: DealDetailsViewModel

  private val backClickSignal = PublishSubject.create<Deal>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val app = application() as Injector
    app.viewComponent.inject(this)
    vm = ViewModelProvider(
      this,
      DealDetailsViewModelFactory(
        owner = this,
        dealsRepository = dealsRepository
      )
    ).get(DealDetailsViewModel::class.java)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.fragment_deal_details, container, false)
  }

  override fun onStart() {
    super.onStart()
    // set title
    (activity as AppCompatActivity?)!!.supportActionBar!!.title =
      resources.getString(R.string.deal_details_title)

    vm.restoreState()
    val rootView = requireView()

    // STATE
    disposables += vm.stateObs()
      .subscribe({ state ->
        when (state) {
          is DealDetailsState.Noop -> {}
          is DealDetailsState.Detail -> onDetailState(state = state)
        }
      }, Logging.logErrorAndThrow())

    // EVENT
    disposables += vm.eventObs()
      .subscribe({ event ->
        when (event) {
          is DealDetailsEvent.Noop -> {}
          is DealDetailsEvent.Back -> onBackEvent(event = event)
          is DealDetailsEvent.Snackbar -> onSnackbarEvent(event = event)
        }
      }, Logging.logErrorAndThrow())

    // Action Signals
    val actionSignal = Observable.merge(
      // TODO: pass in deal id
      Observable.just(DealDetailAction.Load(dealId = "")),
      backClickSignal.map { DealDetailAction.Back }
    )

    disposables += vm.actionHandler(actionSignal = actionSignal)
  }

  override fun onStop() {
    super.onStop()
    vm.saveState()
  }

  /** State Handlers **/
  private fun onDetailState(state: DealDetailsState.Detail) {
    // TODO: populate data
  }

  /** Event Handlers **/
  private fun onBackEvent(event: DealDetailsEvent.Back) {
    // TODO: navigate back
  }

  private fun onSnackbarEvent(event: DealDetailsEvent.Snackbar) {
    requireView().showSnackBar(snackbarViewModel = event.vm)
  }
}