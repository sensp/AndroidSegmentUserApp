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
public class SegmentUserUITest {

    ViewInteraction ivLogo;
    ViewInteraction edtName;
    ViewInteraction edtPass;
    ViewInteraction btnLogin;
    ViewInteraction edtNewKey;
    ViewInteraction edtNewValue;
    ViewInteraction btnUpdate;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        ivLogo = onView(withId(R.id.iv_logo));
        edtName = onView(withId(R.id.input_name));
        edtPass = onView(withId(R.id.input_password));
        btnLogin = onView(withId(R.id.btn_login));
        edtNewKey = onView(withId(R.id.txtNewKey));
        edtNewValue = onView(withId(R.id.txtNewValue));
        btnUpdate = onView(withId(R.id.btnUpdate));

        edtName.perform(typeText("AndrewNg"));
        edtPass.perform(typeText("123456"), closeSoftKeyboard());
        btnLogin.perform(scrollTo()).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(withText("ログインに成功しました。")).check(matches(isDisplayed()));
    }

    @Test
    public void validate_empty_key() {
        edtNewKey.perform(typeText(""));
        edtNewValue.perform(typeText("values"), closeSoftKeyboard());
        btnUpdate.check(matches(isDisplayed())).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(withText("新規key値を入力ください!")).check(matches(isDisplayed()));
    }

    @Test
    public void validate_special_key() {
        edtNewKey.perform(typeText("key key"));
        edtNewValue.perform(typeText("values"), closeSoftKeyboard());
        btnUpdate.check(matches(isDisplayed())).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(withText("key値は半角英数字のみです!")).check(matches(isDisplayed()));
    }

    @Test
    public void validate_same_default_key() {
        edtNewKey.perform(typeText("acl"));
        edtNewValue.perform(typeText("values"), closeSoftKeyboard());
        btnUpdate.check(matches(isDisplayed())).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(withText("key値は更新対象名ではありません!")).check(matches(isDisplayed()));
    }

    @Test
    public void updateSuccess() {
        edtNewKey.perform(typeText("keyName"));
        edtNewValue.perform(typeText("values"), closeSoftKeyboard());
        btnUpdate.check(matches(isDisplayed())).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(withText("更新に成功しました。")).check(matches(isDisplayed()));
    }
}