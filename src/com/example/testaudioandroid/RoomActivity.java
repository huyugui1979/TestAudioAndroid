package com.example.testaudioandroid;

import java.util.ArrayList;
import java.util.Hashtable;

import com.example.testaudioandroid.NotificationCenter.Notifiable;
import com.example.testaudioandroid.NotificationCenter.Notification;
import com.example.testaudioandroid.NotificationCenter.NotificationID;
import com.example.testaudioandroid.R.id;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class RoomActivity extends ActionBarActivity implements  Notifiable {

	private ArrayAdapter<String>	ada;
	private ArrayList<String> data;
	
	void showMessage(String msg)
	{
		Dialog dlg = new AlertDialog.Builder(RoomActivity .this)
		.setMessage(msg)
		.setCancelable(false)   // disable cancel action, hardware back button 
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				RoomActivity.this.finish();
			}

		}).create();


		dlg.show();
	}
	private int roomId;
	private ListView listView ;
	
	public void onNotification(Notification notify) {
		switch (notify.getId()) {
		case PLAYER_JOIN_ROOM:
			Hashtable<String,String> ht1 = (Hashtable<String,String>)notify.getInfo();
			data.add((String)ht1.get("playerId"));
			break;
		case PLAYER_LEFT_ROOM:
			Hashtable<String,String> ht2 = (Hashtable<String,String>)notify.getInfo();
			data.remove((String)ht2.get("playerId"));
			break;
		case PLAYER_BEGIN_TALK:
		{
			Hashtable<String,String> ht3 = (Hashtable<String,String>)notify.getInfo();
			
			int pos =data.indexOf((String)ht3.get("playerId"));
			CheckedTextView cv = (CheckedTextView)listView.getChildAt(pos);
			if(cv != null)
			cv.setTextColor(Color.RED);
		}
			break;
		case PLAYER_STOP_TALK:
		{
			Hashtable<String,String> ht4 = (Hashtable<String,String>)notify.getInfo();
			
			int pos =data.indexOf((String)ht4.get("playerId"));
			CheckedTextView cv = (CheckedTextView)listView.getChildAt(pos);
			if(cv !=null)
			cv.setTextColor(Color.BLACK);
		}
			break;
		default:
			break;
		}
		RoomActivity.this.ada.notifyDataSetChanged();
	}
	private boolean LoadData()
	{
		data.clear();
		int[] members = new int[100];
		int res = VoiceSession.getInstance().GetRoomMember(roomId, members);
		if(res !=0)
		{

			showMessage("get rooms failed");
			VoiceSession.getInstance().Reset();
			return false;
		}
		int i =0;
		while(true){
			int temp = members[i];
			if(temp ==0)
				break;
			else
			{
				data.add(String.valueOf(temp));
				i++;
			}
		}
		//
		listView.setAdapter(ada);
		//
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		for(int pos=0;pos<data.size();pos++)
		{
		listView.setItemChecked(pos, true);
	
		}
	
		return true;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		setContentView(R.layout.activity_room);
		//
		NotificationCenter.singelton().addObserver(NotificationID.PLAYER_JOIN_ROOM, this);
		NotificationCenter.singelton().addObserver(NotificationID.PLAYER_LEFT_ROOM, this);
		NotificationCenter.singelton().addObserver(NotificationID.PLAYER_BEGIN_TALK, this);
		NotificationCenter.singelton().addObserver(NotificationID.PLAYER_STOP_TALK, this);
		
		//
		Button btnBack = (Button)this.findViewById(id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int res = VoiceSession.getInstance().LeaveRoom();
				if( res !=0)
				{
					
					return ;
				}
				AudioPlayer.getInstance().setRunning(false);
				RoomActivity.this.finish();
				//
			}

		});
		//
		String RoomId = this.getIntent().getStringExtra("roomId");
		int[] members = new int[10];
		roomId = Integer.parseInt(RoomId);
		data = new ArrayList<String>();
		ada = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,data); 
		listView = (ListView) this.findViewById(id.listMemeber);
		
		//
	
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//
				CheckedTextView cv = (CheckedTextView)arg1;
				 boolean b = cv.isChecked();
				 //
			     listView.setItemChecked(arg2, !b);
			     //
			     VoiceSession.getInstance().SetRecvData(Integer.parseInt(data.get(arg2)),!b);				// TODO Auto-generated method stub
			
			} 

		});
		LoadData();
		//
		final Button butRecored=(Button)this.findViewById(id.btnRecord);
		butRecored.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				  if(v.getId() == R.id.btnRecord){  
		                if(event.getAction() == MotionEvent.ACTION_UP){ 
		                	Log.d("m1","action_up");
		                	 VoiceSession.getInstance().StopTalk();
		                	 Log.d("m1","action_up1");
		                	AudioRecorder.getInstance().setRunning(false);
		                	Log.d("m1","action_up2");
		                	butRecored.setBackgroundColor(Color.DKGRAY);
		                	Log.d("m1","action_up3");
		                }   
		                if(event.getAction() == MotionEvent.ACTION_DOWN){ 
		                 	Log.d("m1","action_down");
		                	 VoiceSession.getInstance().BeginTalk();
		                	
		                	AudioRecorder.getInstance().setRunning(true);
		            		Thread th = new Thread(AudioRecorder.getInstance());
		            		th.start();
		                	 butRecored.setBackgroundColor(Color.RED);  
		                }  
		            }  
		           
				return false;
			}
		});
		//
		AudioPlayer.getInstance().setRunning(true);
		Thread th = new Thread(AudioPlayer.getInstance());
		th.start();
	}


}
