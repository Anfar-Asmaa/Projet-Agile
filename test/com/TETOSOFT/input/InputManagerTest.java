package com.TETOSOFT.input;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class InputManagerTest {
    InputManager inputManager;

    @BeforeEach
    public  void init(){
        inputManager = new InputManager(new Component() {});
    }

    @Test
    void isRelativeMouseMode() {
        assertFalse( inputManager.isRelativeMouseMode());;
    }




    @Test
    void getMouseX() {
        assertEquals(0 , inputManager.getMouseX());
    }

    @Test
    void getMouseY() {
        assertEquals(0 , inputManager.getMouseY());

    }
}