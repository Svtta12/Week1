package science.example.week12

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import science.example.week12.cl.Service

class ServiceExampleActivity: AppCompatActivity(), View.OnClickListener {


    //объявление объектов класса Button
    private var start: Button? = null
    private var stop: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        //присвоение ID startButton старту объекта
        start = findViewById<View>(R.id.button_play) as Button
        //присвоение ID stopButton остановке объекта
        stop = findViewById<View>(R.id.button_stop) as Button

        //объявление прослушивателей для кнопок, чтобы они правильно реагировали в соответствии с процессом
        start!!.setOnClickListener(this)
        stop!!.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        //процесс, который будет выполнен, если нажата кнопка запуска
        if (v === start) {

            // запуск службы
            startService(Intent(this, Service::class.java))
        }

        // процесс, который будет выполнен, если нажата кнопка остановки
        else if (v === stop) {

            // остановка службы
            stopService(Intent(this, Service::class.java))
        }
    }

}


//Service - компонент приложения, без видимого интерфейса, который работает в фоновом режиме.

//При запуске данного приложения, пользователь увидет 2 кнопки(Старт и Стоп). При нажатии на кнопку старт
//начнет воспроизводиться мелодия, которая установлена по умолчанию рингтоном на андроид устройстве.
//При нажатии на кнопку стоп, воспроизведение завержиться.
//Если же пользователь вернется назад(первое Activity) или свернет приложение, то
//воспроизведение музыки не остановится.

//Приложения: Vk Музыка, Apple Music, Spotify и др., используют данный функционал.
//Только вместо рингтона, воспроизводится определенная песня.