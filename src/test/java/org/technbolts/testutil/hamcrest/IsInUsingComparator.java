package org.technbolts.testutil.hamcrest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class IsInUsingComparator<T> extends BaseMatcher<T> {
    private final Collection<T> collection;
    private final Comparator<T> comparator;

    public IsInUsingComparator(Comparator<T> comparator, Collection<T> collection) {
        this.collection = collection;
        this.comparator = comparator;
    }
    
    public IsInUsingComparator(Comparator<T> comparator, T[] elements) {
        this.collection = Arrays.asList(elements);
        this.comparator = comparator;
    }
    
    @SuppressWarnings({"unchecked"})
    public boolean matches(Object o) {
        for(T elem : collection) {
            if(comparator.compare(elem, (T)o)==0)
                return true;
        }
        return false;
    }

    public void describeTo(Description buffer) {
        buffer.appendText("one of (according to given comparator) ");
        buffer.appendValueList("{", ", ", "}", collection);
    }
    
    @Factory
    public static <T> Matcher<T> isInUsingComparator(Comparator<T> comparator, Collection<T> collection) {
        return new IsInUsingComparator<T>(comparator, collection);
    }
    
    @Factory
    public static <T> Matcher<T> isInUsingComparator(Comparator<T> comparator, T[] elements) {
        return new IsInUsingComparator<T>(comparator, elements);
    }
    
    @Factory
    public static <T> Matcher<T> isOneOfUsingComparator(Comparator<T> comparator, T... elements) {
        return isInUsingComparator(comparator, elements);
    }

}
