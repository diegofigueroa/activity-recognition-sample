package com.example.arp;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityRecognitionManager implements ConnectionCallbacks, OnConnectionFailedListener{
	
	private boolean connecting;
	private boolean requestUpdates;
	
	private MainActivity context;
	private PendingIntent recognitionIntent;
	private ActivityRecognitionClient recognitionClient;
	
	public static final int FIX_PLAY_SERVICES = 1;
	private final int DETECTION_INTERVAL = 1000 * 30;	// 30s
	
	public ActivityRecognitionManager(MainActivity context){
		this.context = context;
		connecting = false;
        recognitionClient = new ActivityRecognitionClient(context, this, this);
        
        Intent intent = new Intent(context, ActivityRecognitionIntentService.class);
        recognitionIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	/**
     * Checks that Google Play services is available.
     *
     * @return true if Google Play services is available, otherwise false
     */
    public boolean servicesAvailable(){
    	
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        // If Google Play services is available
        if(resultCode == ConnectionResult.SUCCESS){
        	
            // Continue
            return true;

        }else{	// Google Play services is not available!
        	
            // Display an error dialog
        	Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, context, FIX_PLAY_SERVICES);
        	if(errorDialog != null){
        		errorDialog.show();
        	}
            return false;
        }
    }
	
    /**
     * Starts the Activity Recognition client and requests updates
     */
    public void start(){
    	requestUpdates = true;
    	connect();
    }
    
    /**
     * Stops requesting updates
     */
    public void stop(){
    	requestUpdates = false;
    	connect();
    }
    
    /**
    * Connects to the Activity Recognition Service
    */
	public void connect(){
        if(servicesAvailable()){	// Check for Google Play services
	        // If a request is not already underway
	        if(!connecting){
	        	if(requestUpdates){
	        		Utils.showInfoMessage(context, "Connecting...");
	        	}
	            // Indicate that a request is in progress
	            connecting = true;
	            // Request a connection to Location Services
	            recognitionClient.connect();
	        }
        }
    }
	
	/**
	 * Activity Recognition Service connection succeed callback
	 */
    @Override
	public void onConnected(Bundle connectionHint){
    	connecting = false;
    	if(requestUpdates){
    		Utils.showConfirmMessage(context, "Connected");
    		
    		// send request
    		recognitionClient.requestActivityUpdates(DETECTION_INTERVAL, recognitionIntent);
    	}else{
    		Utils.showConfirmMessage(context, "Disconnected");
    		// cancel updates
    		recognitionClient.removeActivityUpdates(recognitionIntent);
    	}
    	recognitionClient.disconnect();
	}
    
    /**
     * Activity Recognition Service disconnection succeed callback
     */
	@Override
	public void onDisconnected(){
		connecting = false;
	}
    
	/**
	 * Activity Recognition Service connection failed callback
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result){
		/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve error.
         */
        if(result.hasResolution()){
            try{
                result.startResolutionForResult(context, FIX_PLAY_SERVICES);
                // Thrown if Google Play services canceled the original PendingIntent
            }catch(SendIntentException e){
                // display an error message
            	Utils.showAlertMessage(context, "Connection failed");
            	Log.e("recognitionManager", e.getMessage());
            }
            
        /*
         * Display Google Play service error dialog. This may direct the
         * user to Google Play Store if Google Play services is out of date.
         */
        }else{
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), context, FIX_PLAY_SERVICES);
            if(dialog != null){
                dialog.show();
            }
        }
	}
}
