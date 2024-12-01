package es.ucm.fdi.v3findmyroommate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        // Extraemos los datos del mensaje (puedes personalizar esto según el tipo de mensaje)
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        // Mostrar la notificación solo si hay un título y cuerpo
        if (title != null && body != null) {
            showNotification(title, body);
        }
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Crear el canal de notificación (solo para Android Oreo o superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notificaciones Generales",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)  // Asegúrate de tener un icono en 'res/drawable'
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true); // La notificación desaparecerá al tocarla

        // Mostrar la notificación
        notificationManager.notify(0, builder.build());
    }

    // Guarda el token de FCM en la base de datos de Firebase para poder enviarlo a otros usuarios
    private void sendTokenToDatabase(String token) {
        String userId = "currentUserId"; // Reemplaza esto con el ID real del usuario autenticado
        Log.d(TAG, "Token guardado para usuario: " + userId);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(userId).child("fcmToken").setValue(token)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Token guardado correctamente");
                    } else {
                        Log.e(TAG, "Error al guardar el token");
                    }
                });
    }
}
