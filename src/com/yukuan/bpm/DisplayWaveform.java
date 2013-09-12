package com.yukuan.bpm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.sampled.AudioInputStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DisplayWaveform {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			JFrame frame = new JFrame("Waveform Display Simulator"); 
			frame.setBounds(200, 200, 900, 350);
			
			final JButton button = new JButton("Play");
			

        
			final AudioDisplay display = new AudioDisplay(); 
			display.setFile("buy_something_small.wav");
		    
		    button.setBounds(50, 60, 80, 30);
		    button.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent event) {
		           display.play();
		           final int i = display.getPlayState();
		           if(i == 0 || i == 2) {
		        	   button.setText("Play");
		           } else if(i == 1) {
		        	   button.setText("Pause");
		           } 
		       }
		    });
		    
		    
		    
       
			frame.getContentPane().setLayout(new BorderLayout());		
			frame.getContentPane().add(display, BorderLayout.CENTER);
		
			frame.getContentPane().add(button, BorderLayout.SOUTH);


			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
			frame.setVisible(true);
			frame.validate();

		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
