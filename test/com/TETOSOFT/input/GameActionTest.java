package com.TETOSOFT.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameActionTest {

    private GameAction gameAction;
    @BeforeEach
    void setUp() {
        this.gameAction = new GameAction("fly");
    }

    @Test
    void getName() {
        assertNotNull( gameAction.getName());
    }




    @Test
    void isPressed() {
        assertFalse( gameAction.isPressed());
    }

    @Test
    void getAmount() {
        assertEquals( 0, gameAction.getAmount());
    }
}