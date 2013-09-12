package com.yukuan.bpm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AudioDisplay extends JPanel{
	
	private int oldplay = 0;
	private int playline = 0;
	private int playstate = 0;
	private File file;
	private Clip clip;
	private long length;
	private int numDisp = 0;
	
	public AudioDisplay() {
		setLayout(new GridLayout(0,1));
	}
	
	public int getPlayState() {
		return playstate;
	}

	public void setFile(String filename){
		playline = 0;
		this.file = new File(filename);
		Sample s = new Sample();
		s.sampleFile(filename);
		
		// Technically should be 1,000,000 / sample rate
		length = (long) (s.samples[0].length * 22.675737);
		
		
		AudioInputStream stream = null;
		AudioFormat format = null;
		try {
			stream = AudioSystem.getAudioInputStream(file);
			format = stream.getFormat();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int t = 0; t<format.getChannels(); t++){
			SingleWaveformPanel waveformPanel
				= new SingleWaveformPanel(s.samples[t]);
			add(createChannelDisplay(waveformPanel, t));
			numDisp++;
		}
	}
	
	public void displayData(int[] data) {
		SingleWaveformPanel waveformPanel
		= new SingleWaveformPanel(data);
		add(createChannelDisplay(waveformPanel, numDisp));
		numDisp++;
	}
	
	public void updatePlayline(long ms){
		oldplay = playline;
		playline = (int) (ms * this.getWidth() / length);
		this.paintComponent(this.getGraphics());
	}
	
	public void play() {
		if(playstate == 0){
			playstate = 1;
			Thread t = new Thread() {
				public void run() {
					try{
					    AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
					    clip = AudioSystem.getClip();
					    clip.open(audioIn);
					    clip.start();
					    while(clip.getMicrosecondPosition() < clip.getMicrosecondLength()) {
							  updatePlayline(clip.getMicrosecondPosition());
						}
						updatePlayline(0);
					    clip.close();
					    playstate = 0;
					    		
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
			
		} else if(playstate == 1) {
			playstate = 2;
			clip.stop();
		} else if(playstate == 2) {
			playstate = 1;
			clip.start();
		}
	}
	
	public void paint(Graphics g) {
		paintChildren(g);
		paintComponent(g);
		paintBorder(g);
	}
	
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		if(oldplay > playline){
			g.clearRect(playline, 0, oldplay-playline, this.getHeight());
			this.paintChildren(g);
		}
	    g.setColor(new Color(1.0f, 0.0f, 0.0f, .3f));
	    //g.drawLine(playline, 0, playline, this.getHeight());
	    g.fillRect(oldplay, 0, playline-oldplay, this.getHeight());
    }  
	
	
	private JComponent createChannelDisplay(
			SingleWaveformPanel waveformPanel,
			int index) {

       JPanel panel = new JPanel(new BorderLayout());
	   panel.add(waveformPanel, BorderLayout.CENTER);

	   JLabel label = new JLabel("Channel " + ++index);
	   panel.add(label, BorderLayout.NORTH);

	   return panel; 
	} 



}
