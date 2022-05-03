package science.example.week12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var button_service: Button? = null
    private var button_receiver: Button? = null
    private var button_provider: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_service = findViewById<View>(R.id.button_service) as Button
        button_service?.setOnClickListener {
            val intent = Intent(this@MainActivity, ServiceExampleActivity::class.java)
            startActivity(intent)
        }

        button_receiver = findViewById<View>(R.id.button_receiver) as Button
        button_receiver?.setOnClickListener {
            val intent = Intent(this@MainActivity, ReceiverExampleActivity::class.java)
            startActivity(intent)
        }

        button_provider = findViewById<View>(R.id.button_provider) as Button
        button_provider?.setOnClickListener {
            val intent = Intent(this@MainActivity, ProviderExampleActivity::class.java)
            startActivity(intent)
        }
    }
}

//Activity - это основной компонент приложения взаимодейтсвующий с пользователем. Первое окно,
//которое видет ползователь при запуске программы, это и есть Activity.
//Изначально экран активности пуст. Чтобы разместить пользовательский интерфейс,
//необходимо вызвать метод setContentView(). В качестве параметра передаем идентификатор ресурса.

//При запуске данного приложения, пользователь увидет экран(Activity) с 3 кнопками(Button): Service Activity,
//Broadcast Receiver Activity и Content Provider Activity. При нажание на каждую из них,
//будет открываться новый экран(Activity).

//Каждое приложение использует Activity. В качестве примера приложений, которые включают в себя именно
// Activity с кнопками и переход на другой экран, я могу привести: Самокат(при нажатии на главном Activity
//на необходимую категорию товара, открывается новое Activity(или фрагмент), аналогичные приложения SHEIN,
//Я.Маркет и т.д.