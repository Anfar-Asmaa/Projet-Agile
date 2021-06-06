package com.TETOSOFT.tilegame;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;


public class GameEngineTest {
    private Clip jumpCoins,fail;



    @Test
    public void soundsLoadingTest(){
        System.out.println("Testing sounds loading...");

        try{
            File music_jumpCoin= new File("jump-coin.wav");
            AudioInputStream audioInput= AudioSystem.getAudioInputStream(music_jumpCoin);
            jumpCoins = AudioSystem.getClip();
            jumpCoins.open(audioInput);
            // Fast game over
            File music_fail= new File("fail.wav");
            AudioInputStream audioInput2= AudioSystem.getAudioInputStream(music_fail);
            fail = AudioSystem.getClip();
            fail.open(audioInput2);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Testing Sounds ends ...");

    }
}