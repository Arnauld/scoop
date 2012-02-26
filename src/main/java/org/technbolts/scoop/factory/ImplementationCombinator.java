package org.technbolts.scoop.factory;

import static org.technbolts.util.Objects.o;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.technbolts.scoop.exception.MultipleMatchingCombinaisonsException;
import org.technbolts.scoop.exception.NoCombinaisonMatchingException;
import org.technbolts.util.Classes;
import org.technbolts.util.CombinatorialPowerSet;
import org.technbolts.util.New;
import org.technbolts.util.CombinatorialPowerSet.Cb;

public class ImplementationCombinator {
    
    private static Logger log = LoggerFactory.getLogger(ImplementationCombinator.class);

    public interface Callback {
        void call(ClassCombinaison selection);
    }

    private final Class<?>[] interfaces;
    private Comparator<ClassCombinaison> defaultComparator;

    public ImplementationCombinator(Class<?> superClass, Class<?>... interfaces) {
        this.interfaces = unimplemented(superClass, interfaces);
        this.defaultComparator = new Comparator<ClassCombinaison>() {
            public int compare(ClassCombinaison c1, ClassCombinaison c2) {
                return c2.size() - c1.size();
            }
        };
    }
    
    public void setDefaultComparator(Comparator<ClassCombinaison> defaultComparator) {
        this.defaultComparator = defaultComparator;
    }

    private static Class<?>[] unimplemented(Class<?> superClass, Class<?>[] interfaces) {
        if(superClass==null)
            return interfaces;
        
        List<Class<?>> unimplemented = New.arrayList();
        for(Class<?> interf : interfaces) {
            if(!interf.isAssignableFrom(superClass))
                unimplemented.add(interf);
        }
        return unimplemented.toArray(new Class<?>[unimplemented.size()]);
    }
    
    public ClassCombinaison uniqueBestCombinaisonOrNone(final List<Class<?>> mixins) {
        return bestUniqueCombinaisonOrNone(mixins, defaultComparator);
    }
    
    public ClassCombinaison bestUniqueCombinaisonOrNone(final List<Class<?>> mixins, final Comparator<ClassCombinaison> comparator) {
        List<ClassCombinaison> ref = bestCombinaisons(mixins, comparator);
        if(ref.size()==0) {
            throw new NoCombinaisonMatchingException();
        }
        else if(ref.size()>1) {
            throw new MultipleMatchingCombinaisonsException(ref);
        }
        return ref.get(0);
    }
    
    public List<ClassCombinaison> bestCombinaisons(final List<Class<?>> mixins) {
        return bestCombinaisons(mixins, defaultComparator);
    }
    
    public List<ClassCombinaison> bestCombinaisons(final List<Class<?>> mixins, final Comparator<ClassCombinaison> comparator) {
        final List<ClassCombinaison> ref = New.arrayList();
        traverseAllCombinaisons(mixins, new Callback() {
            public void call(ClassCombinaison combinaison) {
                if(ref.isEmpty()) {
                    ref.add(combinaison);
                    return;
                }
                ClassCombinaison c = ref.get(0);
                int comp = comparator.compare(combinaison, c);
                if(comp>0) {
                    ref.clear();
                    ref.add(combinaison);
                }
                else if(comp==0) {
                    ref.add(combinaison);
                }
            }
        });
        return ref;
    }

    public void traverseAllCombinaisons(final List<Class<?>> mixins, final Callback cb) {
        new CombinatorialPowerSet(mixins.size()).evaluate(new Cb() {
            private List<Class<?>> copy;

            public void startCombinaison() {
                copy = New.arrayList();
            }

            public void index(int index) {
                copy.add(mixins.get(index));
            }

            public void endCombinaison() {
                if (isSatisfiedBy(copy))
                    cb.call(ClassCombinaison.from(copy));
            }
        });
    }

    protected boolean isSatisfiedBy(List<Class<?>> mixins) {
        for (Class<?> interf : interfaces) {
            if (!isSatisfiedBy(mixins, interf))
                return false;
        }
        return true;
    }

    
    /**
     * Default implementation check if one and only ony mixin implements the given interface.
     * If there are more than one mixin that implement the interface, it return <code>false</code>.
     * @param mixins
     * @param interf
     * @return
     */
    protected boolean isSatisfiedBy(List<Class<?>> mixins, Class<?> interf) {
        int count = 0;
        for (Class<?> mixin : mixins) {
            boolean isSatisfied = interf.isAssignableFrom(mixin);
            log.debug("Is <{}> satisfied by <{}>: " + isSatisfied, interf.getSimpleName(), mixin.getSimpleName());
            if (isSatisfied)
                count++;
        }
        
        if(count>1)
            log.debug("Interface <{}> satisfied by <{}> but with #{} implementations: combinaison rejected!", 
                    o(interf.getSimpleName(), Classes.toSimpleNames(mixins), count));
        else if(count==0)
            log.debug("Interface <{}> not satisfied by <{}>: combinaison rejected!", 
                    o(interf.getSimpleName(), Classes.toSimpleNames(mixins)));
        else
            log.debug("Interface <{}> satisfied by <{}> with only one implementation: combinaison accepted!", 
                    o(interf.getSimpleName(), Classes.toSimpleNames(mixins)));
        // one and only one implementing each interface
        return (count==1);
    }
}
