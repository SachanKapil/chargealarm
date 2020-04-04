package com.chargealarm.pagination

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationScrollListener : RecyclerView.OnScrollListener {

    private var mLinearLayoutManager: LinearLayoutManager
    private var isLoading = false
    private var isLastPage = false
    private var thresholdItemCount: Long = 3
    private var paginationListenerCallbacks: PaginationListenerCallbacks

    constructor(layoutManager: LinearLayoutManager, callbacks: PaginationListenerCallbacks) {
        mLinearLayoutManager = layoutManager
        paginationListenerCallbacks = callbacks
    }

    constructor(layoutManager: GridLayoutManager, callbacks: PaginationListenerCallbacks) {
        mLinearLayoutManager = layoutManager
        paginationListenerCallbacks = callbacks
    }

    constructor(
        layoutManager: LinearLayoutManager, callbacks: PaginationListenerCallbacks,
        thresholdItemCount: Long
    ) {
        mLinearLayoutManager = layoutManager
        paginationListenerCallbacks = callbacks
        this.thresholdItemCount = thresholdItemCount
    }

    fun setLastPageStatus(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }

    fun setFetchingStatus(isLoadingDone: Boolean) {
        isLoading = isLoadingDone
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0 && !isLoading && !isLastPage) {
            if (mLinearLayoutManager != null) {
                val visibleItemCount = mLinearLayoutManager.childCount
                val totalItemCount = mLinearLayoutManager.itemCount
                val firstVisibleItemPosition =
                    mLinearLayoutManager!!.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - thresholdItemCount) {
                    isLoading = true
                    paginationListenerCallbacks!!.loadMoreItems()
                }
            }
        }
    }

    interface PaginationListenerCallbacks {
        fun loadMoreItems()
    }
}