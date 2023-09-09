package com.hirno.weather.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf

fun atPositionOnView(
    position: Int,
    @IdRes targetViewId: Int,
    itemMatcher: Matcher<View>,
) = object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("has item at position $position: ")
        itemMatcher.describeTo(description)
    }

    override fun matchesSafely(view: RecyclerView): Boolean {
        val viewHolder = view.findViewHolderForAdapterPosition(position)
            ?:
            return false // has no item on such position
        return itemMatcher.matches(viewHolder.itemView.findViewById(targetViewId))
    }
}

fun ViewInteraction.checkMatchesAtPositionOnView(
    position: Int,
    @IdRes
    targetViewId: Int,
    matcher: Matcher<View>,
): ViewInteraction {
    return perform(ScrollToPositionViewAction(position))
        .check(ViewAssertions.matches(atPositionOnView(position, targetViewId, matcher)))
}

class ScrollToPositionViewAction(private val position: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> = AllOf.allOf(
        ViewMatchers.isAssignableFrom(RecyclerView::class.java),
        ViewMatchers.isDisplayed()
    )

    override fun getDescription() = "scroll RecyclerView to position: $position"

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView
        recyclerView.scrollToPosition(position)
    }
}