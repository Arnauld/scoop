package org.technbolts.scoop.factory;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.technbolts.scoop.util.Objects.o;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.technbolts.scoop.exception.MultipleMatchingCombinaisonsException;
import org.technbolts.scoop.exception.NoCombinaisonMatchingException;
import org.technbolts.scoop.util.CombinatorialPowerSet;
import org.technbolts.scoop.util.CombinatorialPowerSet.Cb;
import org.technbolts.scoop.util.Equals;
import org.technbolts.scoop.util.MethodSignatureBuilder;
import org.technbolts.scoop.util.MethodView;
import org.technbolts.scoop.util.New;

import com.google.common.collect.ArrayListMultimap;

import fj.F;
import fj.data.Array;

public class StructuralCombinator {
    private static Logger log = LoggerFactory.getLogger(StructuralCombinator.class);

    public interface Callback {
        void call(MethodCombinaison selection);
    }

    private final ArrayListMultimap<String,Method> methods;
    private final MethodSignatureBuilder signatureBuilder;
    private Comparator<MethodCombinaison> defaultComparator;

    public StructuralCombinator(Class<?> superClass, Class<?>... interfaces) {
        this.signatureBuilder = new MethodSignatureBuilder().excludeOwningClass();
        this.methods = unimplemented(superClass, interfaces, signatureBuilder);
        this.defaultComparator = new Comparator<MethodCombinaison>() {
            public int compare(MethodCombinaison c1, MethodCombinaison c2) {
                return c2.classCount() - c1.classCount();
            }
        };
    }

    public void setDefaultComparator(Comparator<MethodCombinaison> defaultComparator) {
        this.defaultComparator = defaultComparator;
    }
    
    private static ArrayListMultimap<String,Method> unimplemented(Class<?> superClass, Class<?>[] interfaces, MethodSignatureBuilder signatureBuilder) {
        ArrayListMultimap<String,Method> unimplemented = ArrayListMultimap.create();
        for(Class<?> interf : interfaces) {
            for(Method m : methodsOf(interf)) {
                String sig = signatureBuilder.formatSignature(m);
                unimplemented.put(sig, m);
            }
        }
        
        if(superClass!=null) {
            for(Method m : methodsOf(superClass)) {
                String sig = signatureBuilder.formatSignature(m);
                unimplemented.removeAll(sig);
            }
        }
        
        return unimplemented;
    }

    private static F<Method,Boolean> methodFilterF = new  F<Method,Boolean>() {
        @Override
        public Boolean f(Method method) {
            int modifiers = method.getModifiers();
            return isPublic(modifiers) && !isStatic(modifiers);
        }
    };
    
    protected static Array<Method> methodsOf(Class<?> interf) {
        // one does not rely on getMethods to prevent Object method
        return Array.array(interf.getDeclaredMethods()).filter(methodFilterF);
    }

    public MethodCombinaison uniqueBestCombinaisonOrNone(final List<Class<?>> mixins) {
        return bestUniqueCombinaisonOrNone(mixins, defaultComparator);
    }
    
    public MethodCombinaison bestUniqueCombinaisonOrNone(final List<Class<?>> mixins, final Comparator<MethodCombinaison> comparator) {
        List<MethodCombinaison> ref = bestCombinaisons(mixins, comparator);
        if(ref.size()==0) {
            throw new NoCombinaisonMatchingException();
        }
        else if(ref.size()>1) {
            throw new MultipleMatchingCombinaisonsException(ref);
        }
        return ref.get(0);
    }
    
    public List<MethodCombinaison> bestCombinaisons(final List<Class<?>> mixins) {
        return bestCombinaisons(mixins, defaultComparator);
    }
    
    public List<MethodCombinaison> bestCombinaisons(final List<Class<?>> mixins, final Comparator<MethodCombinaison> comparator) {
        final List<MethodCombinaison> ref = New.arrayList();
        traverseAllCombinaisons(mixins, new Callback() {
            public void call(MethodCombinaison combinaison) {
                if(ref.isEmpty()) {
                    ref.add(combinaison);
                    return;
                }
                MethodCombinaison c = ref.get(0);
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
        final List<MethodView> methodList = New.arrayList();
        for(Class<?> klazz : mixins) {
            Class<?> what = klazz;
            while(what!=null) {
                for(Method m : methodsOf(what)) {
                    String sig = signatureBuilder.formatSignature(m);
                    if(methods.containsKey(sig)) {
                        MethodView methodView = new MethodView(klazz, m);
                        String viewSig = signatureBuilder.formatSignature(methodView);
                        methodList.add(methodView.withSignature(viewSig));
                    }
                }
                what = what.getSuperclass();
                if(what == Object.class)
                    what = null;
            }
        }
        
        int size = methodList.size();
        log.debug("About to evaluate combinaisons around #{} methods", size);
        new CombinatorialPowerSet(size).evaluate(new Cb() {
            private List<MethodView> copy;

            public void startCombinaison() {
                copy = New.arrayList();
            }

            public void index(int index) {
                copy.add(methodList.get(index));
            }

            public void endCombinaison() {
                if (isSatisfiedBy(copy))
                    cb.call(MethodCombinaison.from(copy));
            }
        });
    }

    protected boolean isSatisfiedBy(List<MethodView> mixinMethods) {
        for (String mSignature : methods.keys()) {
            if (!isSatisfiedBy(mixinMethods, mSignature))
                return false;
        }
        return true;
    }

    
    /**
     * Default implementation check if one and only one method has the same specified signature.
     * If there are more than one method that have the same signature, it return <code>false</code>.
     */
    protected boolean isSatisfiedBy(List<MethodView> mixinMethods, String mSignature) {
        int count = 0;
        for (MethodView mixinMethod : mixinMethods) {
            String mixinSignature = mixinMethod.getSignature();
            boolean isSatisfied = Equals.areEquals(mSignature, mixinSignature);
            log.debug("Is <{}> similar to <{}>: " + isSatisfied, mixinSignature, mSignature);
            if (isSatisfied)
                count++;
        }
        
        if(count>1)
            log.debug("Method <{}> satisfied by <{}> but with #{} implementations: combinaison rejected!", 
                    o(mSignature, new ToString(mixinMethods), count));
        else if(count==0)
            log.debug("Interface <{}> not satisfied by <{}>: combinaison rejected!", 
                    o(mSignature, new ToString(mixinMethods)));
        else
            log.debug("Interface <{}> satisfied by <{}> with only one implementation: combinaison accepted!", 
                    o(mSignature, new ToString(mixinMethods)));
        // one and only one implementing each interface
        return (count==1);
    }
    
    private static class ToString {
        private final List<MethodView> methods;
        
        public ToString(List<MethodView> methods) {
            super();
            this.methods = methods;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for(MethodView m : methods) {
                builder.append(m.getSignature());
            }
            return builder.toString();
        }
    }
    
}
