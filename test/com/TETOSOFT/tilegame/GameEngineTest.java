package com.TETOSOFT.tilegame;

import org.junit.Test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import static org.junit.Assert.*;

public class GameEngineTest {
    private Clip jumpCoins,fail;



    @Test
    public void soundsLoadingTest(){
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

        }

    }
}