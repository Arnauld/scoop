package org.technbolts.scoop.factory;

import static fj.function.Booleans.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.technbolts.scoop.testutil.hamcrest.IsInUsingComparator.isInUsingComparator;
import static org.technbolts.scoop.util.MethodView.sameBaseClassAndMethodSignatureAs;
import static org.technbolts.scoop.util.MethodView.viewOf;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.technbolts.scoop.data.HasId;
import org.technbolts.scoop.data.HasName;
import org.technbolts.scoop.data.HasNameImpl;
import org.technbolts.scoop.data.basic.MemoryRepository;
import org.technbolts.scoop.data.basic.NamedMemoryRepository;
import org.technbolts.scoop.data.basic.Repository;
import org.technbolts.scoop.factory.StructuralCombinator.Callback;
import org.technbolts.scoop.util.Classes;
import org.technbolts.scoop.util.MethodView;
import org.technbolts.scoop.util.New;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import fj.F;
import fj.data.Array;

public class StructuralCombinatorTest {
    private List<Class<?>> allMixins;
    private List<MethodCombinaison> selections;
    private Comparator<MethodCombinaison> combinaisonComparator;

    @BeforeMethod
    public void setUp() {
        allMixins = Classes.toArrayList(HasNameImpl.class, NamedMemoryRepository.class, MemoryRepository.class);
        selections = New.arrayList();
        combinaisonComparator = new Comparator<MethodCombinaison>() {
            public int compare(MethodCombinaison o1, MethodCombinaison o2) {
                Array<MethodView> values1 = o1.getValues();
                Array<MethodView> values2 = o2.getValues();
                if (values1.length() == values2.length()) {
                    Array<MethodView> notFound = values1.filter(not(elementOf(values2)));
                    return notFound.length();
                }
                return values1.length() - values2.length();
            }
        };
    }

    protected static F<MethodView, Boolean> elementOf(final Array<MethodView> values) {
        return new F<MethodView, Boolean>() {
            @Override
            public Boolean f(MethodView a) {
                return values.exists(sameBaseClassAndMethodSignatureAs(a));
            }
        };
    }

    @Test
    public void traverseAllCombinaisons() throws Exception {
        StructuralCombinator comb = new StructuralCombinator(null, HasName.class, Repository.class);
        comb.traverseAllCombinaisons(allMixins, new Callback() {
            public void call(MethodCombinaison selection) {
                selections.add(selection);
            }
        });
        for (MethodCombinaison combi : selections) {
            System.out.println(combi);
        }
        assertThat(selections.size(), equalTo(8));

        List<MethodCombinaison> expecteds = New.arrayList(//
                MethodCombinaison.from(//
                        viewOf(HasNameImpl.class, "name"),//
                        viewOf(NamedMemoryRepository.class, "load", long.class),//
                        viewOf(NamedMemoryRepository.class, "save", HasId.class)//
                        ),//
                MethodCombinaison.from(//
                        viewOf(NamedMemoryRepository.class, "name"),//
                        viewOf(NamedMemoryRepository.class, "load", long.class),//
                        viewOf(NamedMemoryRepository.class, "save", HasId.class)//
                        ),//
                MethodCombinaison.from(//
                        viewOf(HasNameImpl.class, "name"),//
                        viewOf(NamedMemoryRepository.class, "load", long.class),//
                        viewOf(MemoryRepository.class, "save", HasId.class)//
                        ),//
                MethodCombinaison.from(//
                        viewOf(NamedMemoryRepository.class, "name"),//
                        viewOf(NamedMemoryRepository.class, "load", long.class),//
                        viewOf(MemoryRepository.class, "save", HasId.class)//
                        ),//
                MethodCombinaison.from(//
                        viewOf(HasNameImpl.class, "name"),//
                        viewOf(NamedMemoryRepository.class, "load", long.class),//
                        viewOf(MemoryRepository.class, "save", HasId.class)//
                        ),//
                MethodCombinaison.from(//
                        viewOf(NamedMemoryRepository.class, "name"),//
                        viewOf(NamedMemoryRepository.class, "load", long.class),//
                        viewOf(MemoryRepository.class, "save", HasId.class)//
                        ),//
                MethodCombinaison.from(//
                        viewOf(HasNameImpl.class, "name"),//
                        viewOf(MemoryRepository.class, "load", long.class),//
                        viewOf(MemoryRepository.class, "save", HasId.class)//
                        ),//
                MethodCombinaison.from(//
                        viewOf(NamedMemoryRepository.class, "name"),//
                        viewOf(MemoryRepository.class, "load", long.class),//
                        viewOf(MemoryRepository.class, "save", HasId.class)//
                        )//

                );

        Iterator<MethodCombinaison> iterator = expecteds.iterator();
        while (iterator.hasNext()) {
            MethodCombinaison combi = iterator.next();
            assertThat(combi, isInUsingComparator(combinaisonComparator, selections));
            iterator.remove();
        }
    }

    @Test
    public void bestCombinaisons() {
        StructuralCombinator comb = new StructuralCombinator(null, HasName.class, Repository.class);
        selections = comb.bestCombinaisons(allMixins);

        assertThat(selections.size(), equalTo(1));
        assertThat(selections.get(0).classesAsList().head(), equalTo(c(NamedMemoryRepository.class)));
        assertThat(selections.get(0).classesAsList().length(), equalTo(1));
    }

    @SuppressWarnings("rawtypes")
    private static Class c(Class<?> klazz) {
        return klazz;
    }
}
