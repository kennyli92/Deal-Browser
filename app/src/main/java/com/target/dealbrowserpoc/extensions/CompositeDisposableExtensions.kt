package com.target.dealbrowserpoc.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

infix operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
  add(disposable)
}