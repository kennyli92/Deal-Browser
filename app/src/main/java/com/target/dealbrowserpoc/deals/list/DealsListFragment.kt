package com.target.dealbrowserpoc.deals.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.target.dealbrowserpoc.dagger.Injector
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.extensions.application
import com.target.dealbrowserpoc.utils.DisposableOnLifecycleChange
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_deals_list.view.deals_list_recycler_view

class DealsListFragment : Fragment() {
    private val disposables: CompositeDisposable by DisposableOnLifecycleChange()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application() as Injector
        app.viewComponent.inject(this)
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
}
