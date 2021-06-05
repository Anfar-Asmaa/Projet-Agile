package com.TETOSOFT.tilegame;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class MapLoaderTest {
    MapLoader mapLoader;
    Image image;
    @Before
    public void init(){
        mapLoader = new MapLoader();
        this.image = mapLoader.loadImage("play/menu.png");
    }

    @Test
    public void loadImage() {
        assertNotNull(mapLoader.loadImage("play/menu.png"));

    }

}