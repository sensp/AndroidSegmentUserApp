package mbaas.com.nifcloud.androidsegmentuserapp;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static mbaas.com.nifcloud.androidsegmentuserapp.Utils.waitFor;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpUITest {

    ViewInteraction ivLogo;
    ViewInteraction edtName;
    ViewInteraction edtPass;
    ViewInteraction btnSignup;
    ViewInteraction tvLinkLogin;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        onView(withId(R.id.link_signup)).perform(click());
        ivLogo = onView(withId(R.id.iv_logo));
        edtName = onView(withId(R.id.input_name));
        edtPass = onView(withId(R.id.input_password));
        btnSignup = onView(withId(R.id.btn_signup));
        tvLinkLogin = onView(withId(R.id.link_login));
    }

    @Test
    public void initialScreen() {
        ivLogo.check(matches(isDisplayed()));
        edtName.check(matches(isDisplayed()));
        edtPass.check(matches(isDisplayed()));
        btnSignup.check(matches(withText("Create Account")));
        tvLinkLogin.check(matches(withText("Already a member? Login")));
    }

    @Test
    public void validate_empty_user_name() {
        edtName.perform(typeText(""));
        edtPass.perform(typeText("123456"), closeSoftKeyboard());
        btnSignup.perform(scrollTo()).perform(click());
        edtName.check(matches(hasErrorText("at least 3 characters")));
    }

    @Test
    public void validate_less_user_name_length() {
        edtName.perform(typeText("ho"));
        edtPass.perform(typeText("123456"), closeSoftKeyboard());
        btnSignup.perform(scrollTo()).perform(click());
        edtName.check(matches(hasErrorText("at least 3 characters")));
    }

    @Test
    public void validate_empty_pass() {
        edtName.perform(typeText("hoge"));
        edtPass.perform(typeText(""), closeSoftKeyboard());
        btnSignup.perform(scrollTo()).perform(click());
        edtPass.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")));
    }

    @Test
    public void validate_less_pass_length() {
        edtName.perform(typeText("hoge"));
        edtPass.perform(typeText("123"), closeSoftKeyboard());
        btnSignup.perform(scrollTo()).perform(click());
        edtPass.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")));
    }

    @Test
    public void validate_over_pass_length() {
        edtName.perform(typeText("hoge"));
        edtPass.perform(typeText("12345678901"), closeSoftKeyboard());
        btnSignup.perform(scrollTo()).perform(click());
        edtPass.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")));
    }

    @Test
    public void openLogin() {
        tvLinkLogin.perform(scrollTo()).perform(click());
        onView(withId(R.id.btn_login)).check(matches(withText("Login")));
    }

    @Test
    public void doLogin() {
        edtName.perform(typeText("AndrewNg"));
        edtPass.perform(typeText("123456"), closeSoftKeyboard());
        btnSignup.perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(withText("ログインに成功しました。")).check(matches(isDisplayed()));
    }
}
