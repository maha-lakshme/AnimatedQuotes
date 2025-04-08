package com.maha.animatedquotes

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with id: $recyclerViewId at position: $position")
            }

            override fun matchesSafely(view: View): Boolean {
                val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId) ?: return false
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) ?: return false
                val targetView = viewHolder.itemView.findViewById<View>(targetViewId) ?: return false
                return view == targetView
            }
        }
    }
}