package com.example.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFY_ID = 101;
    private static final int NOTIFY_ID_2 = 102;
    private static final String CHANNEL_ID = "ExtremeImportance";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            // Важность (приоритет) канала уведомлений.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            // Создание канала уведомлений.
            // Поля: CHANNEL_ID - ID канала, name - название канала, importance - важность канала.
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Создаем канал уведомлений.
        createNotificationChannel();
        /* Вывод краткого всплывающего сообщения (Toast Notification). */
        Toast toastNote = Toast.makeText(this, R.string.notification, Toast.LENGTH_LONG);
        toastNote.show();

        // Создание намерения (???).
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }
        else {
            pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        // Создание конструктора уведомления, используя класс NotificationCompat, предназначенный
        //   для создания и поддержки уведомлений на старых и новых API.
        // Создание экземпляра конструктора уведомления.
        // Поля: this - контекст, CHANNEL_ID - id канала уведомлений.
        NotificationCompat.Builder builderCompat =
                new NotificationCompat.Builder(this, CHANNEL_ID);
        // Установка иконки для уведомления из каталога drawable ресурсов приложения.
        builderCompat.setSmallIcon(R.drawable.nerd_small);
        builderCompat.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nerd_small));
        // Установка заголовка уведомления.
        builderCompat.setContentTitle("Важнейшее уведомление");
        // Установка текста сообщения уведомления.
        builderCompat.setContentText("Необходимо форматирование! Необходимо форматирование!");
        // Установка приоритета для уведомления (для старых API - до Android 8.0).
        builderCompat.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        // Установка возможности расширить уведомления для прочтения всего текста сообщщения.
        builderCompat.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Необходимо форматирование! Необходимо форматирование!" +
                        " Необходимо форматирование! Необходимо форматирование! Необходимо форматирование!" +
                        "Необходимо форматирование! Необходимо форматирование! Необходимо форматирование!"));
        // Установка намерения для уведомления (???).
        builderCompat.setContentIntent(pendingIntent);
        builderCompat.setShowWhen(false);
        // Уведомление будет отключаться после того, как пользователь на него нажмет.
        builderCompat.setAutoCancel(true);
        // (???).
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Отображение уведомления.
        notificationManager.notify(NOTIFY_ID, builderCompat.build());

        // Отправка уведомления о возможности открыть сайт.
        Intent openWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://student.altstu.ru"));
        openWebIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent openWebPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openWebPendingIntent =
                    PendingIntent.getActivity(this, 0, openWebIntent, PendingIntent.FLAG_IMMUTABLE);
        }
        else {
            openWebPendingIntent =
                    PendingIntent.getActivity(this, 0, openWebIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder openWebBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID);
        openWebBuilder.setSmallIcon(R.drawable.nerd_small);
        openWebBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.university));
        openWebBuilder.setContentTitle("Откройте сайт универа!");
        openWebBuilder.setContentText("Только сегодня и только сейчас!");
        openWebBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        openWebBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Только сегодня и только сейчас! Вы можете открыть сайт универа без" +
                        " необходимости переходить через 100 других ссылок. Вот это удача!"));
        openWebBuilder.setContentIntent(openWebPendingIntent);
        openWebBuilder.setAutoCancel(true);

        NotificationManager notificationManager2 =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager2.notify(NOTIFY_ID_2, openWebBuilder.build());
    }
}