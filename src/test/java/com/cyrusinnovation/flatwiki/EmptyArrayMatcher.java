package com.cyrusinnovation.flatwiki;

import org.hamcrest.*;
import org.junit.internal.matchers.*;

public class EmptyArrayMatcher extends TypeSafeMatcher<String[]> {
    public static Matcher<String[]> isEmptyArray() {
        return new EmptyArrayMatcher();
    }

    @Override public boolean matchesSafely(String[] item) {
        return item.length == 0;
    }

    public void describeTo(Description description) {
        description.appendText("an empty array");
    }
}
