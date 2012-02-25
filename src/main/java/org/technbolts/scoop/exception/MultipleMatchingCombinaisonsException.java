package org.technbolts.scoop.exception;

import java.util.List;

import org.technbolts.scoop.factory.Combinaison;

@SuppressWarnings("serial")
public class MultipleMatchingCombinaisonsException extends CombinaisonsException {
    private List<? extends Combinaison> combinaisons;
    public MultipleMatchingCombinaisonsException(List<? extends Combinaison> matchings) {
        super("Non unique combinaison found: #" + matchings.size());
        this.combinaisons = matchings;
    }
    public List<? extends Combinaison> getClassCombinaisons() {
        return combinaisons;
    }
}