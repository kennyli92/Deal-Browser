package com.target.dealbrowserpoc.deals.details

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.target.dealbrowserpoc.dagger.Injector
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.data.DealsRepository
import com.target.dealbrowserpoc.extensions.application
import com.target.dealbrowserpoc.extensions.plusAssign
import com.target.dealbrowserpoc.extensions.showSnackBar
import com.target.dealbrowserpoc.log.Logging
import com.target.dealbrowserpoc.navigation.Navigator
import com.target.dealbrowserpoc.utils.DisposableOnLifecycleChange
import com.target.dealbrowserpoc.utils.ResetDependencyOnDestroy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_deal_details.view.deal_details_image_view
import kotlinx.android.synthetic.main.fragment_deal_details.view.deal_details_product_description
import kotlinx.android.synthetic.main.fragment_deal_details.view.deal_details_regular_price
import kotlinx.android.synthetic.main.fragment_deal_details.view.deal_details_sales_price
import kotlinx.android.synthetic.main.fragment_deal_details.view.deal_details_title

class DealDetailsFragment : Fragment() {
  @set:Inject
  var dealsRepository: DealsRepository by ResetDependencyOnDestroy<DealsRepository>()

  private val disposables: CompositeDisposable by DisposableOnLifecycleChange()
  private lateinit var vm: DealDetailsViewModel

  private val backClickSignal = PublishSubject.create<DealDetailsAction.Back>()
  private lateinit var dealId: String
  private lateinit var navigator: Navigator

  companion object {
    const val DEAL_ID_ARG = "dealId"
    private const val TIMEOUT_IN_SECONDS = 30000
  }

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

    if (activity is Navigator) {
      navigator = activity as Navigator
    } else {
      throw IllegalStateException("Activity Needs to Implement Navigator!")
    }

    dealId = arguments?.getString(DEAL_ID_ARG) ?: ""
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
    setupActionBar()

    vm.restoreState()

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
          is DealDetailsEvent.Back -> onBackEvent()
          is DealDetailsEvent.Snackbar -> onSnackbarEvent(event = event)
        }
      }, Logging.logErrorAndThrow())

    // Action Signals
    val actionSignal = Observable.merge(
      Observable.just(DealDetailsAction.Load(dealId = dealId)),
      backClickSignal
    )

    disposables += vm.actionHandler(actionSignal = actionSignal)
  }

  override fun onStop() {
    super.onStop()
    vm.saveState()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        backClickSignal.onNext(DealDetailsAction.Back)
        return true
      }
    }
    return false
  }

  /** State Handlers **/
  private fun onDetailState(state: DealDetailsState.Detail) {
    val rootView = requireView()

    Glide.with(rootView)
      .load(state.imageUrl)
      .timeout(TIMEOUT_IN_SECONDS)
      .placeholder(R.drawable.ic_wallpaper_24dp)
      .error(R.drawable.ic_error_outline_24dp)
      .into(rootView.deal_details_image_view)

    rootView.deal_details_sales_price.text = state.salePrice
    rootView.deal_details_regular_price.text = state.regularPrice
    rootView.deal_details_regular_price.paintFlags =
      rootView.deal_details_regular_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    rootView.deal_details_title.text = state.title
    rootView.deal_details_product_description.text = state.description
  }

  /** Event Handlers **/
  private fun onBackEvent() {
    navigator.back()
  }

  private fun onSnackbarEvent(event: DealDetailsEvent.Snackbar) {
    requireView().showSnackBar(snackbarViewModel = event.vm)
  }

  /** Other **/
  private fun setupActionBar() {
    setHasOptionsMenu(true)
    // set title
    val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
    actionBar.title = resources.getString(R.string.deal_details_title)
    actionBar.setDisplayHomeAsUpEnabled(true)
    actionBar.setDisplayShowHomeEnabled(true)
  }
}