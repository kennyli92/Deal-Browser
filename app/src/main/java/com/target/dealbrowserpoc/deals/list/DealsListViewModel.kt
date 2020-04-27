package com.target.dealbrowserpoc.deals.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.data.DealsRepository
import com.target.dealbrowserpoc.deals.data.DealsResponse
import com.target.dealbrowserpoc.deals.recyclerView.DealsListItem
import com.target.dealbrowserpoc.dialog.SnackbarViewModel
import com.target.dealbrowserpoc.log.Logging
import com.target.dealbrowserpoc.utils.StateEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class DealsListViewModel(
  private val handle: SavedStateHandle,
  private val dealsRepository: DealsRepository
) : ViewModel() {
  companion object {
    private const val STATE_KEY = "state"
  }

  @Volatile
  private var state: DealsListState = DealsListState.Noop

  /**
   * Should be called onStop() to save state upon backgrounding or during a process death
   */
  fun saveState() {
    handle[STATE_KEY] = state
  }

  /**
   * should be called onStart() to recover state upon foregrounding
   */
  fun restoreState() {
    state = handle[STATE_KEY] ?: DealsListState.Noop
  }

  private val stateEventObs =
    PublishSubject.create<StateEvent<DealsListState, DealsListEvent>>().toSerialized()

  /**
   * emit State that should persist on screen upon foreground from backgrounding or process death
   */
  fun stateObs(): Observable<DealsListState> {
    return stateEventObs.map {
      it.state
    }.distinctUntilChanged().observeOn(AndroidSchedulers.mainThread())
  }

  /**
   * Emit single Event that should happen once and not persist on the screen such as dialogs,
   * snackbars, navigations
   */
  fun eventObs(): Observable<DealsListEvent> {
    return stateEventObs.map {
      it.event
    }.observeOn(AndroidSchedulers.mainThread())
  }

  /**
   * Observe actions to be handled created from click streams
   */
  fun actionHandler(actionSignal: Observable<DealsListAction>): Disposable {
    return actionSignal
      .observeOn(Schedulers.computation())
      .flatMap { action ->
        when (action) {
          is DealsListAction.Load -> onLoad(action = action)
          is DealsListAction.DealClick -> onDealClick(action = action)
        }
      }.subscribe({
        this.state = it.state
        stateEventObs.onNext(it)
      }, Logging.logErrorAndThrow())
  }

  /**
   * We can do state recovery onLoad() should there be a need
   */
  private fun onLoad(
    action: DealsListAction
  ): Observable<StateEvent<DealsListState, DealsListEvent>> {
    return dealsRepository.getDeals()
      .observeOn(Schedulers.computation())
      .flatMapObservable { dealsResponse ->
        when (dealsResponse) {
          is DealsResponse.Deals -> {
            val dealsListItem = dealsResponse.deals.map { deal ->
              DealsListItem.DealView(deal = deal)
            }
            Observable.just(StateEvent(
              DealsListState.ListItem(dealsListItem = dealsListItem),
              DealsListEvent.Noop
            ))
          }
          is DealsResponse.NotFound -> Observable.just(
            StateEvent(
              DealsListState.Noop,
              DealsListEvent.Snackbar(
                vm = SnackbarViewModel(
                  messageResId = R.string.deals_get_not_found,
                  duration = LENGTH_LONG
                )
              )
            ))
          is DealsResponse.UnknownError -> Observable.just(
            StateEvent(
              DealsListState.Noop,
              DealsListEvent.Snackbar(
                vm = SnackbarViewModel(
                  messageResId = R.string.deals_get_unknown_error,
                  duration = LENGTH_LONG
                )
              )
            ))
        }
      }
  }

  /**
   * Observes deal click action and emits deal details navigation event
   */
  private fun onDealClick(
    action: DealsListAction.DealClick
  ): Observable<StateEvent<DealsListState, DealsListEvent>> {
    return Observable.just(
      StateEvent(
        state = DealsListState.Noop,
        event = DealsListEvent.NavigateToDealDetails(id = action.deal.id)
      )
    )
  }
}