package com.target.dealbrowserpoc.deals.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.target.dealbrowserpoc.log.Logging
import com.target.dealbrowserpoc.utils.StateEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class DealsListViewModel(
        private val handle: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val STATE_KEY = "state"
    }

    private var state: DealsListState
        get() = handle[STATE_KEY] ?: DealsListState.Noop
        set(state) {
            handle[STATE_KEY] = state
        }

    private val stateEventObs =
            PublishSubject.create<StateEvent<DealsListState, DealsListEvent>>().toSerialized()

    fun stateObs(): Observable<DealsListState> {
        return stateEventObs.map {
            it.state
        }.distinctUntilChanged().observeOn(AndroidSchedulers.mainThread())
    }

    fun eventObs(): Observable<DealsListEvent> {
        return stateEventObs.map {
            it.event
        }.observeOn(AndroidSchedulers.mainThread())
    }

    fun actionHandler(actionSignal: Observable<DealsListAction>): Disposable {
        return actionSignal
                .observeOn(Schedulers.computation())
                .flatMap { action ->
                    when (action) {
                        is DealsListAction.Load -> onLoad(action = action)
                    }
                }.subscribe({
                    this.state = it.state
                    stateEventObs.onNext(it)
                }, Logging.logErrorAndThrow())
    }

    private fun onLoad(
            action: DealsListAction.Load
    ): Observable<StateEvent<DealsListState, DealsListEvent>> {

        return Observable.just(StateEvent(DealsListState.Noop, DealsListEvent.Noop))
    }
}