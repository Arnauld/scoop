package org.technbolts.scoop.factory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.technbolts.scoop.util.Classes.toArray;

import java.util.List;

import org.hamcrest.Matcher;
import org.technbolts.scoop.data.HasId;
import org.technbolts.scoop.data.HasIdImpl;
import org.technbolts.scoop.data.HasName;
import org.technbolts.scoop.data.HasNameAndIdImpl;
import org.technbolts.scoop.data.HasNameImpl;
import org.technbolts.scoop.data.basic.MemoryRepository;
import org.technbolts.scoop.data.basic.NamedMemoryRepository;
import org.technbolts.scoop.data.basic.Repository;
import org.technbolts.scoop.exception.MultipleMatchingCombinaisonsException;
import org.technbolts.scoop.factory.ImplementationCombinator.Callback;
import org.technbolts.scoop.util.Classes;
import org.technbolts.scoop.util.New;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ImplementationCombinatorTest {
    private List<Class<?>> allMixins;
    private List<ClassCombinaison> selections;

    @BeforeMethod
    public void setUp() {
        allMixins = Classes.toArrayList(HasNameImpl.class, NamedMemoryRepository.class, MemoryRepository.class);
        selections = New.arrayList();
    }
    
    @Test
    public void traverseAllCombinaisons() {
        ImplementationCombinator comb = new ImplementationCombinator(null, HasName.class, Repository.class);
        comb.traverseAllCombinaisons(allMixins, new Callback() {
            public void call(ClassCombinaison selection) {
                selections.add(selection);
            }
        });
        assertThat(selections.size(), equalTo(2));
        assertThat(selections.get(0).getValues(), equalTo(toArray(NamedMemoryRepository.class)));
    }
    
    @Test
    public void uniqueBestCombinaisonOrNone_singleCombinaison() {
        ImplementationCombinator comb = new ImplementationCombinator(null, HasName.class, Repository.class);
        ClassCombinaison bestCombinaison = comb.uniqueBestCombinaisonOrNone(allMixins);
        assertThat(bestCombinaison.size(), equalTo(1));
        assertThat(bestCombinaison.getValues(), equalTo(toArray(NamedMemoryRepository.class)));
    }
    
    @Test
    public void bestCombinaisons_singleCombinaison() {
        ImplementationCombinator comb = new ImplementationCombinator(null, HasName.class, Repository.class);
        List<ClassCombinaison> bestCombinaisons = comb.bestCombinaisons(allMixins);
        assertThat(bestCombinaisons.size(), equalTo(1));
        ClassCombinaison bestCombinaison = bestCombinaisons.get(0);
        assertThat(bestCombinaison.size(), equalTo(1));
        assertThat(bestCombinaison.getValues(), equalTo(toArray(NamedMemoryRepository.class)));
    }
    
    @Test
    public void bestCombinaisons_multipleCombinaisons() {
        ImplementationCombinator comb = new ImplementationCombinator(null, HasId.class, HasName.class, Repository.class);
        allMixins.add(HasIdImpl.class);
        allMixins.add(HasNameAndIdImpl.class);
        
        List<ClassCombinaison> bestCombinaisons = comb.bestCombinaisons(allMixins);
        assertThat(bestCombinaisons.size(), equalTo(2));
        
        ClassCombinaison bestCombinaison1 = bestCombinaisons.get(0);
        assertThat(bestCombinaison1.size(), equalTo(2));
        assertThat(bestCombinaison1.getValues(), hasItem(NamedMemoryRepository.class));
        assertThat(bestCombinaison1.getValues(), hasItem(HasIdImpl.class));

        ClassCombinaison bestCombinaison2 = bestCombinaisons.get(1);
        assertThat(bestCombinaison2.size(), equalTo(2));
        assertThat(bestCombinaison2.getValues(), hasItem(MemoryRepository.class));
        assertThat(bestCombinaison2.getValues(), hasItem(HasNameAndIdImpl.class));
    }
    
    @Test(expectedExceptions={MultipleMatchingCombinaisonsException.class})
    public void uniqueBestCombinaisonOrNone_throwOnMultiple() {
        ImplementationCombinator comb = new ImplementationCombinator(null, HasId.class, HasName.class, Repository.class);
        allMixins.add(HasIdImpl.class);
        allMixins.add(HasNameAndIdImpl.class);
        
        comb.uniqueBestCombinaisonOrNone(allMixins);
    }

    @SuppressWarnings("rawtypes")
    private Matcher<Class[]> hasItem(Class klazz) {
        Matcher<Class[]> m = hasItemInArray(klazz);
        return m;
    }
}
