package com.TETOSOFT.tilegame;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapLoaderTest {
    MapLoader mapLoader;
    Image image;

    public void init(){
        mapLoader = new MapLoader();
        this.image = mapLoader.loadImage("play/menu.png");
    }

    @Test
    public void loadImage() {
        System.out.println("Testing Images Loading ....");
        init();
        assertNotNull(mapLoader.loadImage("play/menu.png"));
        System.out.println("Testing Images ends with success.");
    }

}