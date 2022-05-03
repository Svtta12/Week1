package science.example.week12.cl

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings

class Service: Service() {

    // объявление объекта MediaPlayer
    private lateinit var player: MediaPlayer

    // начнется выполнение сервиса при вызове этого метода
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // создание медиаплеера, который будет воспроизводить звук по умолчанию рингтон на андроид устройстве
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        //предоставление логического значения как true для воспроизведения звука в цикле
        player.setLooping(true)
        //запуск процесса
        player.start()
        //возвращает статус программы
        return START_STICKY
    }

    //выполнение службы остановится при вызове этого метода
    override fun onDestroy() {
        super.onDestroy()
        //остановка процесса
        player.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}