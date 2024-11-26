package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import es.ucm.fdi.v3findmyroommate.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Guarda el token para cada usuario en la base de datos.
        Log.d("FCM Token", "Token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Maneja el mensaje recibido.
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        // Mostrar notificación (opcional).
        showNotification(title, body);
    }

    private void showNotification(String title, String message) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "messages_channel")
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);
        manager.notify(0, builder.build());
    }
}
