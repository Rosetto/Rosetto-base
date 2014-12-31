package org.ocsoft.rosetto.models.base.elements.values;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.utils.base.Values;

public class ValueTestUtils {
    
    /**
     * その値をリストとみなしたときに単一の要素として扱えるかのテスト.
     * @param value
     */
    public static void isSingleValue(RosettoValue sut) {
        assertThat(sut.first(), is((RosettoValue)sut));
        assertThat(sut.rest(), is((RosettoValue)Values.NULL));
        assertThat(sut.cons(Values.create(1)).asString(), is("(1 " + sut.toString() + ")"));
        assertThat(sut.size(), is(1));
        assertThat(sut.getAt(0), is((RosettoValue)sut));
        assertThat(sut.getAt(1), is((RosettoValue)Values.NULL));
    }

}
