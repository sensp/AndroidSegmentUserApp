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
public class LoginUITest {

    ViewInteraction ivLogo;
    ViewInteraction edtName;
    ViewInteraction edtPass;
    ViewInteraction btnLogin;
    ViewInteraction tvLinkSignup;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        ivLogo = onView(withId(R.id.iv_logo));
        edtName = onView(withId(R.id.input_name));
        edtPass = onView(withId(R.id.input_password));
        btnLogin = onView(withId(R.id.btn_login));
        tvLinkSignup = onView(withId(R.id.link_signup));
    }

    @Test
    public void initialScreen() {
        ivLogo.check(matches(isDisplayed()));
        edtName.check(matches(isDisplayed()));
        edtPass.check(matches(isDisplayed()));
        btnLogin.check(matches(withText("Login")));
        tvLinkSignup.check(matches(withText("No account yet? Create one")));
    }

    @Test
    public void validate_user_name() {
        edtName.perform(typeText(""));
        edtPass.perform(typeText("123456"), closeSoftKeyboard());
        btnLogin.perform(scrollTo()).perform(click());
        edtName.check(matches(hasErrorText("enter username")));
    }

    @Test
    public void validate_empty_pass() {
        edtName.perform(typeText("Hoge"));
        edtPass.perform(typeText(""), closeSoftKeyboard());
        btnLogin.perform(scrollTo()).perform(click());
        edtPass.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")));
    }

    @Test
    public void validate_less_pass_length() {
        edtName.perform(typeText("Hoge"));
        edtPass.perform(typeText("123"), closeSoftKeyboard());
        btnLogin.perform(scrollTo()).perform(click());
        edtPass.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")));
    }

    @Test
    public void validate_over_pass_length() {
        edtName.perform(typeText("Hoge"));
        edtPass.perform(typeText("12345678901"), closeSoftKeyboard());
        btnLogin.perform(scrollTo()).perform(click());
        edtPass.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")));
    }

    @Test
    public void openSignup() {
        tvLinkSignup.perform(scrollTo()).perform(click());
        onView(withId(R.id.btn_signup)).check(matches(withText("Create Account")));
    }

    @Test
    public void doLogin() {
        edtName.perform(typeText("Hoge"));
        edtPass.perform(typeText("123456"), closeSoftKeyboard());
        btnLogin.perform(scrollTo()).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(withText("ログインに成功しました。")).check(matches(isDisplayed()));
    }
}