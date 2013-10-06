package com.example.arp;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;

public class ActivityRecognitionIntentService extends IntentService{
	
	public static final String RECOGNITION_RESULT = "result";
	public static final String BROADCAST_UPDATE = "new_update";
	
    public ActivityRecognitionIntentService(){
		super("ActivityRecognitionService");
	}
	
    // Called when a new activity detection update is available.
    @Override
    protected void onHandleIntent(Intent intent){
    	if(ActivityRecognitionResult.hasResult(intent)){
    		Log.d("intentService", "got new update!");
    		
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            
            // notify main activity
            Intent i = new Intent(BROADCAST_UPDATE);
            i.putExtra(RECOGNITION_RESULT, result);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            manager.sendBroadcast(i);
        }
    }
}
