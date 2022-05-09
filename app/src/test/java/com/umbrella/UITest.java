package com.umbrella;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.widget.RadioGroup;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.umbrella.android.R;
import com.umbrella.android.activities.NetworkActivity;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import kotlin.jvm.JvmField;

@RunWith(RobolectricTestRunner.class)
public class UITest {
    private Activity mActivity;
    private RadioGroup mRadioGroup;
    @ClassRule
    @JvmField
    public static final ActivityTestRule<NetworkActivity> activityTestRule =
            new ActivityTestRule<>(NetworkActivity.class);

    @Before
    public void setup() {
        mActivity = activityTestRule.getActivity();
        mRadioGroup = (RadioGroup) mActivity.findViewById(R.id.radioButtonBackProp);
    }
    @Test
    public void validateEditText() throws IOException {
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibility() {
        Espresso.onView(ViewMatchers.withId(R.id.radioButtonBackProp))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.radioButtonIpprop))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.radioButtonRprop))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.textView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.upload))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }
    @Test
    public void test_isVisibility() {
        onView(withId(R.id.radio))
                .check(matches(isDisplayed()));

        onView(withId(R.id.split_action_bar))
                .check(matches(isDisplayed()));

        onView(withId(R.id.textView))
                .check(matches(isDisplayed()));

    }

}
