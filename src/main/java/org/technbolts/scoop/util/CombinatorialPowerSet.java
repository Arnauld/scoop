package org.technbolts.scoop.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The power set is the set of all subsets of a set.
 * 
 * For example, the power set of the set {a, b, c} consists of the sets:
 * <ul>
 * <li><code>{}</code></li>
 * <li><code>{a}</code></li>
 * <li><code>{b}</code></li>
 * <li><code>{c}</code></li>
 * <li><code>{a, b}</code></li>
 * <li><code>{a, c}</code></li>
 * <li><code>{b, c}</code></li>
 * <li><code>{a,b,c}</code></li>
 * </ul>
 * 
 * A subset can be represented as an array of boolean values of the same size as the set, called a characteristic
 * vector. Each boolean value indicates whether the corresponding element in the set is present or absent in the subset.
 * 
 * This gives following correspondences for the set <code>{a, b, c}</code>:
 * 
 * <pre>
 * <code>
 * [0, 0, 0] = {}
 * [1, 0, 0] = {a}
 * [0, 1, 0] = {b}
 * [0, 0, 1] = {c}
 * [1, 1, 0] = {a, b}
 * [1, 0, 1] = {a, c}
 * [0, 1, 1] = {b, c}
 * [1, 1, 1] = {a, b, c}
 * </code>
 * </pre>
 * 
 * The algorithm then simply needs to produce the arrays shown above. 
 * 
 * One could just count in binary, but this would not
 * produce the subsets in a sensible order.
 * 
 * To produce the arrays in the above order (by length, then lexicographic), at each step:
 * 
 * <ol>
 * <li>Find the first 1 with a 0 on its right. </li>
 * <li>Move it right, replacing it with a 0. </li>
 * <li>If there are no 1s to move, add another 1 and put them all at the beginning.</li> 
 * </ol>
 * 
 * Note the the third step handles the case where there are not yet any 1s.
 * 
 * Source: http://www.martinbroadhurst.com/combinatorial-algorithms.html#power-set
 */
public class CombinatorialPowerSet {

    public static void main(String[] args) {
        new CombinatorialPowerSet(4).printAll();
    }
    
    private static Logger log = LoggerFactory.getLogger(CombinatorialPowerSet.class);

    public interface Cb {
        void startCombinaison();

        void index(int index);

        void endCombinaison();
    }

    private final int size;

    public CombinatorialPowerSet(int size) {
        super();
        if (size >= 64)
            throw new IllegalArgumentException("Only size below 64 is supported");
        this.size = size;
    }

    public void printAll() {
        evaluate(new Cb() {
            public void startCombinaison() {
            }

            public void index(int index) {
                System.out.print(index + ", ");
            }

            public void endCombinaison() {
                System.out.println();
            }
        });
    }

    public void evaluate(Cb cb) {
        int comb = 1 << size;
        log.debug("Evaluating #{} combinaisons", comb);
        
        for (int i = 0; i < comb; i++) {
            log.debug("Combinaison: {}", Integer.toBinaryString(i));
            cb.startCombinaison();
            for (int j = 0; j < size; j++) {
                if ((i & (1 << j)) > 0) {
                    cb.index(j);
                }
            }
            cb.endCombinaison();
        }
    }

}
