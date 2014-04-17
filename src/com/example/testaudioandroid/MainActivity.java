package com.example.testaudioandroid;

import java.util.ArrayList;
import java.util.Random;

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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements  Notifiable {

	private boolean connect=false;
	private ArrayAdapter<String>	ada;
	void showMessage(String msg)
	{
		Dialog dlg = new AlertDialog.Builder(MainActivity .this)
		.setMessage(msg)
		.setCancelable(false)   // disable cancel action, hardware back button 
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		}).create();


		dlg.show();
	}
	private boolean LoadData()
	{
		data.clear();
		int[] rooms = new int[100];
		int res = VoiceSession.getInstance().GetRoomList(rooms);
		if(res !=0)
		{

			showMessage("get rooms failed");
			VoiceSession.getInstance().Reset();
			return false;
		}
		int i =0;
		while(true){
			int temp = rooms[i];
			if(temp ==0)
				break;
			else
			{
				data.add(String.valueOf(temp));
				i++;
			}
		}

		MainActivity.this.ada.notifyDataSetChanged();
		return true;
	}
	private ArrayList<String> data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		NotificationCenter.singelton().addObserver(NotificationID.DISCONNECT_SERVER, this);
		//
		data = new ArrayList<String>();
		ada = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,data); 
		ListView listView = (ListView) this.findViewById(id.listViewRoom);
		listView.setAdapter(ada);

		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// TODO Auto-generated method stub
				//
				String roomId = ada.getItem(arg2);
				int res = VoiceSession.getInstance().EnterRoom(Integer.parseInt(roomId));
				if(res ==0)
				{
				//
				Intent intent = new Intent();
				intent.putExtra("roomId",roomId );
				intent.setClass(MainActivity.this, RoomActivity.class);  
				startActivity(intent); 
				}else
				{
					showMessage("enter room failed");
				}
				//
			} 

		});
		//
		final Button butConfig=(Button)this.findViewById(id.btnConfig);
		butConfig.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
			
				intent.setClass(MainActivity.this, Config.class);  
				startActivity(intent); 
			}
			
		});
		//
		final Button butFreshRoom=(Button)this.findViewById(id.btnFreshRoom);
		butFreshRoom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoadData();
			}
			
		});
		//
		final Button butAddRoom=(Button)this.findViewById(id.btnAddRoom);
		butAddRoom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Random r = new Random();
				int res = VoiceSession.getInstance().CreateRoom(r.nextInt()%100);
				if(res != 0)
				{
					showMessage("create room failed");
					return;
				}
				
				MainActivity.this.LoadData();
			}
			
		});
		//
		final Button but=(Button)this.findViewById(id.btnConnect);

		but.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vv) {
				if(MainActivity.this.connect == false)
				{
					String addr = Param.getInstance().server_ip;
					short port = (short) Integer.parseInt(Param.getInstance().server_port);
						int res = VoiceSession.getInstance().Init(addr,port);
					if(res != 0)
					{
						showMessage("connect failed");
						VoiceSession.getInstance().Reset();
						return ;
					}
					Random r = new Random();
					res = VoiceSession.getInstance().LoginServer(r.nextInt()%100);
					if(res !=0)
					{
						showMessage("login failed");
						VoiceSession.getInstance().Reset();
						return;
					}
					
					
					if(MainActivity.this.LoadData()==false)
						return ;
					//
					but.setText("disconnect");connect=true;
				}else
				{
					VoiceSession.getInstance().Reset();
					MainActivity.this.ada.clear();
					MainActivity.this.ada.notifyDataSetChanged();
					but.setText("connect server");
					connect=false;
				}
			}
		});
	}
	@Override
	public void onNotification(Notification notify) {
		switch (notify.getId()) {
		case DISCONNECT_SERVER:
			if(connect == true)
			{
				showMessage("server disconnect");
				connect= false;
			}

			break;
		case PLAYER_JOIN_ROOM:
			break;
		case PLAYER_LEFT_ROOM:
			break;
		default:
			break;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */

}
