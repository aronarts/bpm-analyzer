package com.yukuan.bpm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class SingleWaveformPanel extends JPanel {
	int heightInt;
	int widthInt;
	int[] samples;

	public SingleWaveformPanel(int[] samples) {
		heightInt = this.getHeight();
		widthInt = this.getWidth();
		this.samples = samples;
	}
	
	public void paintComponent(Graphics g) {
		int oldY = (int) (this.getHeight() / 2);
		int heightInt = this.getHeight();
		widthInt = this.getWidth();
		g.setColor(new Color(.9f, .9f, .9f, 1f));
		g.fillRect(0, 0, widthInt, heightInt);

		g.setColor(new Color(.1f, .1f, .2f, 1f));
		g.drawLine(0, heightInt/2, widthInt, heightInt/2); 
		
		int increment = Math.max(1, this.samples.length/widthInt);

		
		for (int p = 0; p < widthInt; p ++) {
			int t = p * samples.length / widthInt; 
			double scaledSample = samples[t] * heightInt / 66000;
			double scaledMin = scaledSample;
			double scaledMax = scaledSample;
			int sqSum = 0;
			int n = 0;
			for(int i = Math.max(0, t - increment / 2); i <= Math.min(samples.length-1, t + increment / 2); i++){
				int rangedSample = samples[i] * heightInt / 66000;
				sqSum += rangedSample * rangedSample;
				n++;
				if(rangedSample < scaledMin) {
					scaledMin = rangedSample;
				}
				if(rangedSample > scaledMax) {
					scaledMax = rangedSample;
				}
			}
			int rms = (int) Math.sqrt(((double) sqSum) / n);			
			
			int y = (int) ((heightInt / 2) - (scaledSample));
			int miny = (int) ((heightInt / 2) - (scaledMin));
			int maxy = (int) ((heightInt / 2) - (scaledMax));
			int rmsl = (int) ((heightInt / 2) - (rms));
			g.setColor(new Color(.1f, .1f, .2f, 1f));
			if(increment > 4){
				g.drawLine(p, miny, p, maxy);
			} else {
				g.drawLine(p-1, oldY, p, y);
			}
			g.setColor(new Color(.05f, .05f, .1f, 1f));
			g.drawLine(p, rmsl, p, rmsl + 2 * rms);
			oldY = y;
			
		}

	}

}
