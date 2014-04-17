package com.example.testaudioandroid;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;

import com.example.testaudioandroid.NotificationCenter.NotificationID;

public class VoiceSession{
	
	private static VoiceSession instance;  
	private VoiceSession (){}   
	public static VoiceSession getInstance() {  
		if (instance == null) {  
			instance = new VoiceSession();  
		}  
		return instance;  
	}
	static {  
		 try{
			    System.loadLibrary("speex");
			    }catch(UnsatisfiedLinkError ule){
			      ule.printStackTrace();
			    }
	      ;  
	    }
	public void OnServerDisconnect(){
		NotificationCenter.singelton().postNotification(this,NotificationID.DISCONNECT_SERVER,null);
	}
	public void OnPlayerJoinRoom(int playerId,int roomId)
	{
		Log.d("androidtest","somebody comeing");
		Hashtable<String, String> ht = new Hashtable<String,String>();
		ht.put("playerId", String.valueOf(playerId));
		ht.put("roomId", String.valueOf(roomId));
		NotificationCenter.singelton().postNotification(this,NotificationID.PLAYER_JOIN_ROOM,ht);

	}
	public void OnPlayerBeginTalk(int playerId)
	{
		Log.d("androidtest","somebody comeing");
		Hashtable<String, String> ht = new Hashtable<String,String>();
		ht.put("playerId", String.valueOf(playerId));
		
		NotificationCenter.singelton().postNotification(this,NotificationID.PLAYER_BEGIN_TALK,ht);

	}
	public void OnPlayerStopTalk(int playerId)
	{
		Log.d("androidtest","somebody comeing");
		Hashtable<String, String> ht = new Hashtable<String,String>();
		ht.put("playerId", String.valueOf(playerId));
		
		NotificationCenter.singelton().postNotification(this,NotificationID.PLAYER_STOP_TALK,ht);

	}
	public void OnPlayerLeftRoom(int playerId,int roomId)
	{
		Log.d("androidtest","somebody goin");
		Hashtable<String, String> ht = new Hashtable<String,String>();
		ht.put("playerId", String.valueOf(playerId));
		ht.put("roomId", String.valueOf(roomId));
		NotificationCenter.singelton().postNotification(this,NotificationID.PLAYER_LEFT_ROOM,ht);

	}
	public void OnMemberJoinRoom(int RoomId,int playerId){}
	public void OnMemberLeftRoom(int RoomId,int playerId){}
	public native    int Init(String address,short i);
	public native    void Reset();
	public native    int BeginTalk();
	public native    int StopTalk();
	public native    int LoginServer(int playerId);//m=1s=3
	public native    int LoginOutServer();//m=1,s=7
	public native    int CreateRoom(int roomId);//m=1,s=9
	public native    int DestroyRoom(int roomId);//m=1,s=25
	public native    int EnterRoom(int roomId);//m=1,s=11
	public native    int LeaveRoom();
	public native    int GetRoomList(int[] vs);
	public native    int SendData( byte[] data,int len);//m=1,s=17
	public native    int SetRecvData(int playerId,boolean recv);
	public native    int GetData(byte[] data);
	public native    int GetRoomMember(int roomId,int[] vs);//m=1,s=15
}
