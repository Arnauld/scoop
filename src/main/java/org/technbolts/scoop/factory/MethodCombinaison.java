package org.technbolts.scoop.factory;

import org.technbolts.util.MethodSignatureBuilder;
import org.technbolts.util.MethodView;

import fj.data.Array;
import fj.data.List;

public class MethodCombinaison implements Combinaison {
    
    public static MethodCombinaison from(MethodView...views) {
        return new MethodCombinaison(Array.array(views));
    }
    public static MethodCombinaison from(Iterable<MethodView> list) {
        return new MethodCombinaison(Array.iterableArray(list));
    }
    
    private final Array<MethodView> values;
    private List<Class<?>> classes;

    public MethodCombinaison(Array<MethodView> values) {
        super();
        this.values = values;
    }
    
    public int size() {
        return values.length();
    }

    public Array<MethodView> getValues() {
        return values;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MethodCombinaison [");
        MethodSignatureBuilder sig = new MethodSignatureBuilder().includeOwningClass();
        boolean first=true;
        for(MethodView m : values) {
            if(first)
                first = false;
            else
                builder.append(", ");
            builder.append("\n  ");
            sig.formatSignature(m, builder);
        }
        return builder.append("]").toString();
    }

    public MethodView valueAt(int i) {
        return values.get(i);
    }
    
    public List<Class<?>> classesAsList() {
        if(classes==null)
            classes = values.map(MethodView.baseClassF()).toList().nub();
        return classes;
    }
    
    public int classCount() {
        return classesAsList().length();
    }


}
