package com.target.dealbrowserpoc.deals.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.target.dealbrowserpoc.dagger.Injector
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.data.DealsRepository
import com.target.dealbrowserpoc.extensions.application
import com.target.dealbrowserpoc.extensions.plusAssign
import com.target.dealbrowserpoc.log.Logging
import com.target.dealbrowserpoc.utils.DisposableOnLifecycleChange
import com.target.dealbrowserpoc.utils.ResetDependencyOnDestroy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_deals_list.view.deals_list_recycler_view
import javax.inject.Inject

class DealsListFragment : Fragment() {
  @set:Inject
  var dealsRepository: DealsRepository by ResetDependencyOnDestroy<DealsRepository>()

  private val disposables: CompositeDisposable by DisposableOnLifecycleChange()
  private lateinit var vm: DealsListViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val app = application() as Injector
    app.viewComponent.inject(this)
    vm = ViewModelProvider(
      this,
      DealsListViewModelFactory(
        owner = this,
        dealsRepository = dealsRepository
      )
    ).get(DealsListViewModel::class.java)

    // set title
    (activity as AppCompatActivity?)!!.supportActionBar!!.title =
      resources.getString(R.string.deals_title)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val view = inflater.inflate(R.layout.fragment_deals_list, container, false)

    view.deals_list_recycler_view.layoutManager = LinearLayoutManager(context)

    return view
  }

  override fun onStart() {
    super.onStart()
    vm.restoreState()
    val rootView = requireView()

    // STATE
    disposables += vm.stateObs()
      .subscribe({ state ->
        when (state) {
          is DealsListState.Noop -> {}
          is DealsListState.Deals -> onLoadState(state = state)
        }
      }, Logging.logErrorAndThrow())

    // EVENT
    disposables += vm.eventObs()
      .subscribe({ event ->
        when (event) {
          is DealsListEvent.Noop -> {}
        }
      }, Logging.logErrorAndThrow())

    // Action Signals
    disposables += vm.actionHandler(actionSignal = Observable.just(DealsListAction.Load))
  }

  override fun onStop() {
    super.onStop()
    vm.saveState()
  }

  /** State Handlers **/
  private fun onLoadState(state: DealsListState.Deals) {

  }
}