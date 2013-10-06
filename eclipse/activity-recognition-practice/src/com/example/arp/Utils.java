package com.example.arp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Utils{
	
	private static int previousActivity = DetectedActivity.UNKNOWN;
	
	/**
	 * Parses a detected activity into a human readable string
	 * @param activity
	 * @return
	 */
	public static String parseActivity(DetectedActivity activity){
    	String log;
    	
    	switch(activity.getType()){
    	case DetectedActivity.IN_VEHICLE:
    		log = "In car.";
    		break;
    	case DetectedActivity.ON_BICYCLE:
    		log = "On bike.";
    		break;
    	case DetectedActivity.ON_FOOT:
    		log = "On foot.";
    		break;
    	case DetectedActivity.STILL:
    		log = "Standing still.";
    		break;
    	case DetectedActivity.TILTING:
    		log = "Tilting.";
    		break;
		default:
			log = "Unknown.";
    	}
    	
    	return activity.getConfidence() + "%:\t\t\t" + log;
    }
    
	/**
	 * Shows a notification displaying a detected activity
	 * @param context
	 * @param activity
	 */
    public static void showActivityNotification(Context context, DetectedActivity activity){
    	// Implement me!
    }
    
    /**
     * Builds ands displays a cool notification 
     * @param context
     * @param title
     * @param body
     */
    public static void showNotification(Context context, String title, String body){
    	int notificationId = 1;
    	Notification.Builder builder = new Notification.Builder(context)
    		.setSmallIcon(R.drawable.ic_stat_action_about)
	        .setContentTitle(title)
	        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL)
	        .setContentText(body);
    	Intent resultIntent = new Intent(context, MainActivity.class);
    	builder.setContentIntent(PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    	NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	notificationManager.notify(notificationId, builder.build());
    }
    
    /**
     * Shows a nice information message
     * @param context
     * @param msg
     */
    public static void showInfoMessage(Activity context, String msg){
    	Crouton.showText(context, msg, Style.INFO);
    }
    
    /**
     * Shows a nice confirmation message
     * @param context
     * @param msg
     */
    public static void showConfirmMessage(Activity context, String msg){
    	Crouton.showText(context, msg, Style.CONFIRM);
    }
    
    /**
     * Shows a nice alert message
     * @param context
     * @param msg
     */
    public static void showAlertMessage(Activity context, String msg){
    	Crouton.showText(context, msg, Style.ALERT);
    }
    
    /**
     * Adds a log entry for every probable activity the user is doing
     * @param result
     */
    public static void logActivities(ActivityRecognitionResult result){
    	for(DetectedActivity activity : result.getProbableActivities()){
    		Log.d("activities", parseActivity(activity));
    	}
    }
}
