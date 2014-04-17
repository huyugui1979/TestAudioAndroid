package com.example.testaudioandroid;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class AudioPlayer implements Runnable {
	byte[] data;
	AudioTrack mAudioTrack;                        
	private volatile boolean isRunning;
	private final Object mutex = new Object();
	private static AudioPlayer instance;  
	private AudioPlayer (){
		int minBufSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,AudioFormat.ENCODING_PCM_16BIT);
		
		mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				8000, 
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				minBufSize,
				AudioTrack.MODE_STREAM);
	}   
	public static AudioPlayer getInstance() {  
		if (instance == null) {  
			instance = new AudioPlayer();  
		}  
		return instance;  
	}
	public int getPrimePlaySize(){
		int minBufSize = AudioTrack.getMinBufferSize(8000, 
				1,
				8);

		return minBufSize * 2;
	}
	public void stop(){
		setRunning(false);
	}
	
	public void setRunning(boolean isRunning) {
		synchronized (mutex) {
			this.isRunning = isRunning;
			if (this.isRunning) {
				mutex.notify();
			}
		}
	}
	public boolean isRunning() {
		synchronized (mutex) {
			return isRunning;
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int minBufSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,AudioFormat.ENCODING_PCM_16BIT);
		
	
		BufferQueue queue = new BufferQueue(minBufSize*2);
		byte[] temp = new byte[minBufSize];
		byte[] data = new byte[320];
		while (this.isRunning) {
			VoiceSession.getInstance().GetData(data);
			queue.append(data,0,320);
			if(queue.getCount()>=minBufSize)
			{
				queue.read(temp,0,minBufSize);
				mAudioTrack.write(temp, 0,minBufSize);
				mAudioTrack.play();
			}
		}
		mAudioTrack.stop();
	}
}
