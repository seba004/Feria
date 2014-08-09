package frisbit.profit;



import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ProximityActivity extends Activity {
	String notificationTitle;
    String notificationContent;
    String tickerMessage;
    
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
 
        boolean proximity_entering = getIntent().getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);
        String id= getIntent().getStringExtra("Id");
        
        if(proximity_entering && id.equals("1")){
            Toast.makeText(getBaseContext(),"Entering the region 1"  ,Toast.LENGTH_LONG).show();
            notificationTitle="Geocerca 1";
            notificationContent="Entrado a la region 1 ";
            tickerMessage = "Pipin";
        }
        
        else if(proximity_entering && id.equals("2")){
            Toast.makeText(getBaseContext(),"Entrando a UTFSM",Toast.LENGTH_LONG).show();
            notificationTitle="UTFSM";
            notificationContent="¿Quieres entrenar en UTFSM?";
            tickerMessage = "Profit ha encontrado un lugar para entrenar!";
            
        }
        
        
        else if(proximity_entering && id.equals("3")){
            Toast.makeText(getBaseContext(),"Entrando a casa Eva",Toast.LENGTH_LONG).show();
            notificationTitle="EVA";
            notificationContent="¿Quieres entrenar en casa Eva?";
            tickerMessage = "Profit ha encontrado un lugar para entrenar!";
            
        }
        
        
        
        
        //Comentar esto si no se desea mostrar la salida de la geocerca.
        
        else{
            Toast.makeText(getBaseContext(),"Saliendo de la región"  ,Toast.LENGTH_LONG).show();
            notificationTitle="Has salido de la zona de entrenamiento";
            notificationContent="Has salido de la zona de entrenamiento";
            tickerMessage = "Exited the region";
        }
        
        
        
        Intent notificationIntent = new Intent(getApplicationContext(),NotificationView.class);
        notificationIntent.putExtra("content", notificationContent );
        
        // This is needed to make this intent different from its previous intents 
        notificationIntent.setData(Uri.parse("tel:/"+ (int)System.currentTimeMillis()));
 
        //Creating different tasks for each notification. See the flag Intent.FLAG_ACTIVITY_NEW_TASK 
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
 
        // Getting the System service NotificationManager 
        NotificationManager nManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
 
        // Configuring notification builder to create a notification 
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setWhen(System.currentTimeMillis())
                .setContentText(notificationContent)
                .setContentTitle(notificationTitle)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setTicker(tickerMessage)
                .setContentIntent(pendingIntent);
 
        // Creating a notification from the notification builder 
        Notification notification = notificationBuilder.build();
 
        // Sending the notification to system.
        //* The first argument ensures that each notification is having a unique id
        //* If two notifications share same notification id, then the last notification replaces the first notification
        //* 
        nManager.notify((int)System.currentTimeMillis(), notification);
 
        // Finishes the execution of this activity 
        finish();
               
    
 
        }
}
