package es.ucm.fdi.v3findmyroommate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    private static final String CHANNEL_ID = "default_channel";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Nuevo token FCM: " + token);
        sendTokenToDatabase(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Mensaje recibido: " + remoteMessage);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        if (title != null && body != null) {
            showNotification(title, body);
        }
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notificaciones Generales",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }

    private void sendTokenToDatabase(String token) {
        String userId = "currentUser"; // Obtén dinámicamente el ID del usuario
        Log.d(TAG, "Token guardado para usuario: " + userId);

        // Simula guardar el token en Firebase Database
        // DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        // database.child("users").child(userId).child("fcmToken").setValue(token);
    }
}
