package com.yukuan.bpm;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sample {
	
	public int[][] samples;
	
	public int windowSize = 1024;
	

	public int sampleFile (String filename) {

		File file = new File(filename);
		
		//Try to open the file as an AudioInputStream
		AudioInputStream stream = null;
		AudioFormat format = null;
		try {
			stream = AudioSystem.getAudioInputStream(file);
			format = stream.getFormat();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		
		int frameLength = (int) stream.getFrameLength();
		int frameSize = (int) format.getFrameSize();
		
		byte[] raw = new byte[frameLength * frameSize];
		
		int read_byte = 0;
		try {
			read_byte = stream.read(raw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int numChannels = format.getChannels();
		
		samples = new int[numChannels][frameLength];
		
		for(int i = 0, sampleIndex = 0; i < raw.length; sampleIndex++){
			for(int ch = 0; ch < numChannels; ch++) {
				int lo = (int) raw[i++];
				int hi = (int) raw[i++];
				//for 16 bit samples only
				int sample = (hi << 8) + (lo & 0x00ff);
				samples[ch][sampleIndex] = sample;
			}
		}
		return 1;		
	}
	
	public int[] getHanningWindow(int sampleIndex, int startingSample) {
		int[] hw = new int[windowSize];
		for(int i = 0; i < windowSize; i++) {
			int sample = samples[sampleIndex][startingSample + i];
			hw[i] = (int) (sample * 0.5 * (1 - Math.cos(2 * Math.PI * i / (windowSize - 1))));
		}
		return hw;
	}


}
