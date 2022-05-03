package science.example.week12

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import science.example.week12.cl.ContentProvider

class ProviderExampleActivity: AppCompatActivity(), View.OnClickListener  {

    private lateinit var etProductName : EditText
    private lateinit var etProductManufacturer : EditText
    private lateinit var btnAddProduct: Button
    private lateinit var btnShowProduct: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)
        bindViews()
        setupInstances()
    }

    private fun bindViews() {
        etProductName = findViewById(R.id.et_product_name)
        etProductManufacturer = findViewById(R.id.et_product_manufacturer)
        btnAddProduct = findViewById(R.id.btn_add_products)
        btnShowProduct = findViewById(R.id.btn_show_products)
    }

    private fun setupInstances() {
        btnAddProduct.setOnClickListener(this)
        btnShowProduct.setOnClickListener(this)
        supportActionBar?.hide()
    }

    private fun inputsValid(): Boolean {
        var inputsValid = true
        if(TextUtils.isEmpty(etProductName.text)) {
            etProductName.error = "Обязательное поле"
            etProductName.requestFocus()
            inputsValid = false
        }
        else if (TextUtils.isEmpty(etProductManufacturer.text)) {
            etProductManufacturer.error = "Обязательное поле"
            etProductManufacturer.requestFocus()
            inputsValid = false
        }
        return inputsValid
    }

    private fun addProduct() {
        val contentValues = ContentValues()
        //Сохраняем данные о продуктев экземпляре contentValues, а затем вставляем эти данные
        //в базу данных SQLite, вызывая contentResolver.
        contentValues.put(ContentProvider.NAME, etProductName.text.toString())
        contentValues.put(ContentProvider.MANUFACTURER, etProductManufacturer.text.toString())
        contentResolver.insert(ContentProvider.CONTENT_URI, contentValues)
        showSaveSuccess()
    }

    //Функция для отображения продуктов, находящихся в БД
    @SuppressLint("Range", "Recycle")
    private fun showProducts() {
        val uri = Uri.parse(ContentProvider.URL)
//        val cursor = managedQuery(uri, null, null, null, "name")
        val cursor = contentResolver.query(uri, null, null, null, "name")
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val res = "ID: ${cursor.getString(cursor.getColumnIndex(ContentProvider.ID))}" + ", " +
                            "\nPRODUCT NAME: ${cursor.getString(cursor.getColumnIndex(
                                ContentProvider.NAME))}" + ", " +
                            "\nPRODUCT MANUFACTURER: ${cursor.getString(cursor.getColumnIndex(
                                ContentProvider.MANUFACTURER))}"

                    Toast.makeText(this, res, Toast.LENGTH_LONG).show()
                } while (cursor.moveToNext())
            }
        } else {
            Toast.makeText(this, "Упс! Что-то пошло не так", Toast.LENGTH_LONG).show()
        }
    }

    private fun showSaveSuccess() {
        Toast.makeText(this, "Продукт успешно сохранен", Toast.LENGTH_LONG).show()
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.btn_add_products) {
            if (inputsValid()) {
                addProduct()
            }
        } else if (id == R.id.btn_show_products) {
            showProducts()
        }
    }
}

//Content Provider - это компонент приложения, который позволяет управлять доступом к данным нашей программы,
//предоставляя его другим приложениям.

//При запуске пользователь увидет экран(Activity) с 2 полями для ввода текста(EditText) и
//2 кнопками(Button). Приложение позволяет пользователю вводить в текстовые поля информацию
//о продукте(название продукта и его производитель) и, при нажатие на первую кнопку
//(Добавить продукт), сохраняет его в базе данных SQLite, а при нажатие на вторую кнопку (Показать
//продукты), пользователь может посмотреть информацию о сохраненных продуктах. Отображение информации происходит
//во всплывающих сообщениях(Toast).

//В кажестве примера можно привести любую социальную сеть, которая просит пользователя при регистрации вводить
// свои данные, сохраняет их в БД и затем при каждом входе в эту социальную сеть, пользователь вводит
//данные, которые сравниваются с имеющимися в БД.
//Vk,Instagram,OK и т.д

//Так же примером может быть игра Морской бой, которая получая ходы пользователя, сохраняет их в БД
//и, при игре по сети, передает эти данные на другое устройство, отображая их другому пользователю.