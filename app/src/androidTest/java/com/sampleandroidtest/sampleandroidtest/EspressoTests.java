package com.sampleandroidtest.sampleandroidtest;

import android.widget.Button;
import android.widget.EditText;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertThat;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(AndroidJUnit4.class)
public class EspressoTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource idlingResources;

    private UiDevice device;

    @Before
    public void registerIdlingResource() {
        IdlingPolicies.setMasterPolicyTimeout(60, SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(26, SECONDS);
    }

    @Test
    public void assertLoginButton() {
        /**
         * UI test to assert button is visible
         */
        onView(withId(R.id.button_id)).check(matches(withText("Login")));
    }

    @Test
    public void sentToTwitter() {
        /**
         * Click on the button to be sent to twitter
         */
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        onView(withId(R.id.button_id)).check(matches(isDisplayed()));
        onView(withId(R.id.button_id)).perform(click());

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5 * 2, SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5 * 2, SECONDS);

        idlingResources = new ElapsedTimeIdlingResource(15);
        IdlingRegistry.getInstance().register(idlingResources);

        UiObject joinText = device.findObject(new UiSelector().text("Join Twitter today."));

        assertThat(joinText, notNullValue());
    }

    @Test
    public void canTwitterAuth() throws UiObjectNotFoundException {
        /**
         * Test to auth with twitter.
         */
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        onView(withId(R.id.button_id)).check(matches(isDisplayed()));
        onView(withId(R.id.button_id)).perform(click());

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5 * 2, SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5 * 2, SECONDS);

        idlingResources = new ElapsedTimeIdlingResource(15);
        IdlingRegistry.getInstance().register(idlingResources);


        //Espresso will not work for Twitter webview auth see
        // https://developer.chrome.com/extensions/contentSecurityPolicy
        UiObject firstLoginButton = device.findObject(new UiSelector().text("Log in"));
        firstLoginButton.click();
        UiObject forgotPasswordText = device.findObject(new UiSelector().text("Forgot password?"));
        assertThat(forgotPasswordText, notNullValue());
        UiObject usernameField = device.findObject(new UiSelector().className(EditText.class).instance(0));
        UiObject passwordField = device.findObject(new UiSelector().className(EditText.class).instance(1));

        //Failing this intentially with fake account information
        //To make this pass the preferred way is to store the credentials as part of hte build config
        usernameField.setText("FakeUser");
        passwordField.setText("FakePass");
        closeSoftKeyboard();
        UiObject secondLoginButton = device.findObject(new UiSelector().className(Button.class).text("Log in"));
        secondLoginButton.click();
        assertThat(usernameField, nullValue());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResources);
    }
}
