package com.example.arp;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class MainActivity extends ListActivity{
	
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;
	
	private final String LIST = "list";
	private boolean getUpdates = true;
	
	private ActivityRecognitionManager manager;
	private BroadcastReceiver broadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
        String[] buffer = new String[]{};
        if(savedInstanceState != null){
	        buffer = savedInstanceState.getStringArray(LIST);
        }
        
        list = new ArrayList<String>(Arrays.asList(buffer));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        manager = new ActivityRecognitionManager(this);
        
        setListAdapter(adapter);
        broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){
            	Object tmp = intent.getParcelableExtra(ActivityRecognitionIntentService.RECOGNITION_RESULT);
            	addUpdate((ActivityRecognitionResult)tmp);
            }
        };
	}
	
	@Override
	public void onResume(){
		super.onResume();
		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
		manager.registerReceiver(broadcastReceiver, new IntentFilter(ActivityRecognitionIntentService.BROADCAST_UPDATE));
	}
	
	@Override
	public void onPause(){
		super.onPause();
		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
		manager.unregisterReceiver(broadcastReceiver);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle){
		String[] buffer = list.toArray(new String[0]);
		bundle.putStringArray(LIST, buffer);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_start:
            	getUpdates = true;
            	manager.start();
                return true;
            case R.id.action_stop:
            	getUpdates = false;
            	manager.stop();
                return true;
            case R.id.action_clear:
            	list.clear();
            	adapter.notifyDataSetChanged();
                return true;
            // For any other choice, pass it to super()
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch(requestCode){
        case ActivityRecognitionManager.FIX_PLAY_SERVICES:
        	if(resultCode == Activity.RESULT_OK){	// Play Services are available now, yey!
        		if(getUpdates){
        			manager.start();
        		}else{
        			manager.stop();
        		}
        	}
        }
	}
    
	
    private void addUpdate(ActivityRecognitionResult result){
    	DetectedActivity activity = result.getMostProbableActivity();
    	
    	Utils.showActivityNotification(this, activity);
    	String msg = Utils.parseActivity(activity);
    	
    	list.add(msg);
		adapter.notifyDataSetChanged();
    }
    
}
