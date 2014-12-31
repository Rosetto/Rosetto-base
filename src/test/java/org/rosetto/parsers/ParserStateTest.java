package org.rosetto.parsers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.rosetto.parsers.ParserState;


public class ParserStateTest {

    @Test
    public void インスタンス化テスト() {
        ParserState sut = new ParserState();
        assertThat(sut.getTokens(), is(notNullValue()));
        assertThat(sut.getParsingUnit(), is(nullValue()));
        assertThat(sut.getUnitIndex(), is(0));
    }
}
