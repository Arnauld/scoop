package org.technbolts.scoop.exception;

import java.util.List;

import org.technbolts.scoop.factory.ClassCombinaison;

@SuppressWarnings("serial")
public class MultipleMatchingCombinaisonsException extends CombinaisonsException {
    private List<ClassCombinaison> matchings;
    public MultipleMatchingCombinaisonsException(List<ClassCombinaison> matchings) {
        super("Non unique combinaison found: #" + matchings.size());
        this.matchings = matchings;
    }
    public List<ClassCombinaison> getMatchings() {
        return matchings;
    }
}