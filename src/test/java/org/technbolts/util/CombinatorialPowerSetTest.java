package org.technbolts.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.List;

import org.technbolts.util.CombinatorialPowerSet;
import org.technbolts.util.New;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CombinatorialPowerSetTest {

    private CombinatorialPowerSet combinatorialPowerSet;
    private Collector collector;
    
    @BeforeMethod
    public void setUp () {
        collector = new Collector();
    }
    
    @Test
    public void size1() {
        combinatorialPowerSet = new CombinatorialPowerSet(1);
        combinatorialPowerSet.evaluate(collector);
        
        List<Indices> collected = collector.allIndices; 
        assertThat(collected.size(), equalTo(2));
        assertThat(collected.get(0), equalTo(new Indices()));
        assertThat(collected.get(1), equalTo(new Indices(0)));
    }
    
    @Test
    public void size2() {
        combinatorialPowerSet = new CombinatorialPowerSet(2);
        combinatorialPowerSet.evaluate(collector);
        
        List<Indices> collected = collector.allIndices; 
        assertThat(collected.size(), equalTo(4));
        assertThat(collected, org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder(
                new Indices(), new Indices(0), new Indices(1), new Indices(0,1)));
    }
    
    @Test
    public void size3() {
        combinatorialPowerSet = new CombinatorialPowerSet(3);
        combinatorialPowerSet.evaluate(collector);
        
        List<Indices> collected = collector.allIndices; 
        assertThat(collected.size(), equalTo(8));
        assertThat(collected, org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder(
                new Indices(), 
                new Indices(0), new Indices(1), new Indices(2), 
                new Indices(0,1), new Indices(0,2), new Indices(1,2),
                new Indices(0,1,2)));
    }
    
    @Test
    public void size4() {
        combinatorialPowerSet = new CombinatorialPowerSet(4);
        combinatorialPowerSet.evaluate(collector);
        
        List<Indices> collected = collector.allIndices; 
        assertThat(collected.size(), equalTo(16));
        assertThat(collected, org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder(
                new Indices(), //
                new Indices(0), new Indices(1), new Indices(2), new Indices(3), //  
                new Indices(0,1), new Indices(0,2), new Indices(0,3), new Indices(1, 2), new Indices(1, 3), new Indices(2, 3), //
                new Indices(0,1,2), new Indices(0,1,3), new Indices(0,2,3), new Indices(1,2,3), 
                new Indices(0,1,2, 3)));
    }

    
    private static class Collector implements CombinatorialPowerSet.Cb {
        private List<Indices> allIndices = New.arrayList();
        private Indices current;
        public void startCombinaison() {
            current = new Indices();
        }
        public void index(int index) {
            current.addIndex(index);
        }
        public void endCombinaison() {
            allIndices.add(current);
            // make sure one has no more ref'
            current = null;
        }
    }
    
    private static class Indices {
        public final List<Integer> values;
        public Indices() {
            values = New.arrayList();
        }
        public Indices(Integer... values) {
            this();
            for(Integer i : values)
                addIndex(i);
        }
        public void addIndex(int index) {
            values.add(index);
        }
        @Override
        public boolean equals(Object obj) {
            if(obj==this)
                return true;
            if(obj==null)
                return false;
            Indices other = (Indices)obj;
            return containsInAnyOrder(values.toArray()).matches(other.values);
        }
    }
}
