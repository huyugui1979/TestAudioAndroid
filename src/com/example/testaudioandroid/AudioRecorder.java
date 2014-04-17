package com.example.testaudioandroid;

import java.util.concurrent.Semaphore;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioRecorder implements Runnable {

	private volatile boolean isRunning;
	private final Object mutex = new Object();
	private static final int frequency = 8000;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	AudioRecord recordInstance;
	private static AudioRecorder instance;  
	private AudioRecorder (){
		int bufferSize = AudioRecord.getMinBufferSize(frequency,
				AudioFormat.CHANNEL_IN_MONO , audioEncoding);
		 recordInstance = new AudioRecord(
				 
				MediaRecorder.AudioSource.MIC, frequency,
				AudioFormat.CHANNEL_IN_MONO , audioEncoding, bufferSize);
	}   
	public static AudioRecorder getInstance() {  
		if (instance == null) {  
			instance = new AudioRecorder();  
		
		}  
		return instance;  
	}

	public void run() {


		android.os.Process
		.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

		int bufferRead = 0;
		int bufferSize = AudioRecord.getMinBufferSize(frequency,
				AudioFormat.CHANNEL_IN_MONO , audioEncoding);
		BufferQueue buffer = new BufferQueue(bufferSize*2);

	

		recordInstance.startRecording();

		while (this.isRunning) {
			byte[] tempBuffer = new byte[bufferSize];
			byte[] t = new byte[320];
			//bufferRead = recordInstance.read(tempBuffer, 0, bufferSize);
			bufferRead = recordInstance.read(tempBuffer, 0, bufferSize);
			buffer.append(tempBuffer,0,bufferRead);

			while(true)
			{
				int n=	buffer.peek(t,0,320);
				if(n == 320)
				{
					buffer.read(t,0,320);
					VoiceSession.getInstance().SendData(t, 320);
				}else
				{	
					break;
				}
			}
		}
		recordInstance.stop();
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
}