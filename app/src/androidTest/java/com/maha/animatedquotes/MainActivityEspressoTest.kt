package com.maha.animatedquotes

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maha.animatedquotes.ui.view.MainActivity
import com.maha.animatedquotes.util.EspressoIdlingResource
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.emptyOrNullString
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This Espresso test launches MainActivity,
 * waits for network operations (using our idling resource),
 * and verifies that the RecyclerView displays combined video/quote items.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {
   @Before
   fun setUp(){
       IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
   }
    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testMainActivityLoadsAndDisplaysData() {
        // Launch MainActivity.
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.rvCombined)).check(matches(isDisplayed()))
        // Scroll the RecyclerView to position 0 to make sure the first item is visible.
        onView(withId(R.id.rvCombined))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))

        // Check that the first found instance of tvQuote is displayed and that its text is not empty.
        onView(RecyclerViewMatcher(R.id.rvCombined).atPositionOnView(0,R.id.tvQuote))
            .check(matches(allOf(isDisplayed(), not(withText(emptyOrNullString())))))

        // Check that the first found instance of tvAuthor is displayed and that its text is not empty.
        onView(RecyclerViewMatcher(R.id.rvCombined).atPositionOnView(0,R.id.tvAuthor))
            .check(matches(allOf(isDisplayed(), not(withText(emptyOrNullString())))))

        var itemCount =0
        onView(withId(R.id.rvCombined)).check{ view,_ ->
            val recyclerView = view as RecyclerView
            itemCount = recyclerView.adapter?.itemCount?:0

        }
            for(position in 0 until itemCount){

                // Scroll to make sure the item at this position is bound
                onView(withId(R.id.rvCombined)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))

                onView(RecyclerViewMatcher(R.id.rvCombined).atPositionOnView(position,R.id.tvQuote)).check(
                    matches(allOf(isDisplayed(),not(withText(emptyOrNullString())))
                ))
                onView(RecyclerViewMatcher(R.id.rvCombined).atPositionOnView(position,R.id.tvAuthor)).check(
                    matches(allOf(isDisplayed(), not(withText(emptyOrNullString()))))
                )

        }

        scenario.close()
    }
}