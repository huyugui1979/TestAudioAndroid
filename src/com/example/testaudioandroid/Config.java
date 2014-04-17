package com.example.testaudioandroid;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.os.Build;

 class Param {    
	   
	  static class SingletonHolder {    
	    static Param instance = new Param();    
	  }    
	  public String server_ip="192.168.1.248";
	  public String server_port="9009";
	  public static Param getInstance() {    
	    return SingletonHolder.instance;    
	  }    
	   
	} 
public class Config extends ActionBarActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		//
		EditText server_ip = (EditText)Config.this.findViewById(R.id.server_ip);
		EditText server_port = (EditText)Config.this.findViewById(R.id.server_port);
		server_ip.setText(Param.getInstance().server_ip);
		server_port.setText(Param.getInstance().server_port);
		//
		Button b = (Button)this.findViewById(R.id.back);
		
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText server_ip = (EditText)Config.this.findViewById(R.id.server_ip);
				EditText server_port = (EditText)Config.this.findViewById(R.id.server_port);
				Param.getInstance().server_ip = server_ip.getText().toString();
				Param.getInstance().server_port = server_port.getText().toString();
				Config.this.finish();
			}
		});
		//
		
		//
	}


}
