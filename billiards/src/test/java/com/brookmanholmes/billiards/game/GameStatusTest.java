package com.brookmanholmes.billiards.game;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Created by Brookman Holmes on 11/6/2015.
 */
public class GameStatusTest {

    @Test
    public void gameStatusEqualityTest() {
        EqualsVerifier.forClass(GameStatus.class)
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }
}
