package com.example.arp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.DetectedActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Utils{
	
	private static int previousActivity = DetectedActivity.UNKNOWN;
	
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
    
    public static void showActivityNotification(Context context, DetectedActivity activity){
    	String msg;
    	
    	switch(activity.getType()){
    	case DetectedActivity.IN_VEHICLE:
    		msg = "Hey you are traveling in a car!";
    		break;
    	case DetectedActivity.ON_BICYCLE:
    		msg = "Hey you are biking, good for you!";
    		break;
    	case DetectedActivity.ON_FOOT:
    		msg = "Hey you are moving on foot, how is the weather?";
    		break;
    	case DetectedActivity.STILL:
    		msg = "Hey you are standing still, go out there!";
    		break;
    	case DetectedActivity.TILTING:
    		msg = "Hey stop tilting me, i'm getting dizzy!";
    		break;
		default:
			msg = "Sorry, didn't get that, what are you doing?";
    	}
    	
    	if(previousActivity != activity.getType() && activity.getConfidence() > 50){
    		previousActivity = activity.getType();
    		
	    	int notificationId = 1;
	    	Notification.Builder builder = new Notification.Builder(context)
	    		.setSmallIcon(R.drawable.ic_stat_action_about)
    	        .setContentTitle("New activity detected")
    	        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL)
    	        .setContentText(msg);
	    	Intent resultIntent = new Intent(context, MainActivity.class);
	    	builder.setContentIntent(PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT));
	    	NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	    	
	    	notificationManager.notify(notificationId, builder.build());
    	}
    }
    
    public static void showInfoMessage(Activity context, String msg){
    	Crouton.showText(context, msg, Style.INFO);
    }
    
    public static void showConfirmMessage(Activity context, String msg){
    	Crouton.showText(context, msg, Style.CONFIRM);
    }
    
    public static void showAlertMessage(Activity context, String msg){
    	Crouton.showText(context, msg, Style.ALERT);
    }
}
