package org.technbolts.util;

import fj.F2;

public class Strings {

    public static F2<StringBuilder,String,StringBuilder> joinF(final String separator) {
        return new F2<StringBuilder, String, StringBuilder>() {
            @Override
            public StringBuilder f(StringBuilder buffer, String content) {
                if(buffer.length()>0)
                    buffer.append(separator);
                return buffer.append(content);
            }
        };
    }
}
