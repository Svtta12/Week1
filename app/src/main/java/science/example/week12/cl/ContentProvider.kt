package science.example.week12.cl

import android.content.*
import android.content.ContentProvider
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils

class ContentProvider: ContentProvider() {

    companion object {
        val PROVIDER_NAME: String = "com.example.contentproviderexample.ContentProvider"

        val URL: String = "content://$PROVIDER_NAME/products"
        val CONTENT_URI : Uri = Uri.parse(URL)
        val PRODUCTS = 1
        val PRODUCTS_ID = 2

        //Объявим базу данных и св-ва таблицы
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "Depot"
        val PRODUCTS_TABLE_NAME = "products"

        //обьявим имена столбца таблицы "products"
        val ID: String = "id"
        val NAME: String = "name"
        val MANUFACTURER: String = "manufacturer"
        val uriMacher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        val PRODUCTS_PROJECTION_MAP: HashMap<String, String> = HashMap()

        private class DatabaseHelper(context: Context?) :
            SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
            override fun onCreate(db: SQLiteDatabase?) {
                val query = " CREATE TABLE " + PRODUCTS_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " name VARCHAR(255) NOT NULL, " + " manufacturer VARCHAR(255) NOT NULL);"
                db!!.execSQL(query)
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                val query = "DROP TABLE IF EXISTS $PRODUCTS_TABLE_NAME"
                db!!.execSQL(query)
                onCreate(db)
            }
        }

    }

    private lateinit var db: SQLiteDatabase

    //Используем SQLiteOpenHelper для получения доступной для записи базы данных(БД)
    override fun onCreate(): Boolean {
        uriMacher.addURI(PROVIDER_NAME, "products", PRODUCTS)
        uriMacher.addURI(PROVIDER_NAME, "products/#", PRODUCTS_ID)
        val helper = DatabaseHelper(context)
        //новая база данных создается, если она еще не существует
        db = helper.writableDatabase
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        //Вставка новой записи о продукте в таблицу products
        val rowId = db.insert(PRODUCTS_TABLE_NAME, "", values)
        //Если rowId больше 0, то запись о продукте была успешно добавлена
        if (rowId > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowId)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }
        //Генерируем исключение, если запись о продукте не была добавлена
        throw SQLException ("Не удалось добавить товар в" + uri)

    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = PRODUCTS_TABLE_NAME
        when (uriMacher.match(uri)) {
            PRODUCTS -> queryBuilder.setProjectionMap(PRODUCTS_PROJECTION_MAP)
            PRODUCTS_ID -> queryBuilder.appendWhere(
                "$ID = ${uri.pathSegments[1]}"
            )
        }
        val cursor: Cursor = queryBuilder.query(db, projection,selection,
            selectionArgs, null, null, sortOrder)
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val count = when (uriMacher.match(uri)) {
            PRODUCTS -> db.delete(PRODUCTS_TABLE_NAME, selection, selectionArgs)
            PRODUCTS_ID -> {
                val id = uri.pathSegments[1]
                db.delete(
                    PRODUCTS_TABLE_NAME, "$ID = $id" +
                        if (!TextUtils.isEmpty(selection)) "AND ($selection)" else "", selectionArgs)
            }
            else -> throw IllegalArgumentException ("Неизвестный URI: $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val count = when(uriMacher.match(uri)) {
            PRODUCTS -> db.update(PRODUCTS_TABLE_NAME, values, selection, selectionArgs)
            PRODUCTS_ID -> {
                db.update(
                    PRODUCTS_TABLE_NAME, values, "$ID = ${uri.pathSegments[1]}" +
                        if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "", selectionArgs)
            }
            else -> throw IllegalArgumentException ("Неизвестный URI: $uri")

        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        return when (uriMacher.match(uri)){
            PRODUCTS -> "vnd.android.cursor.dir/vnd.example.products"
            PRODUCTS_ID -> "vnd.android.cursor.item/vnd.example.products"
            else -> throw IllegalArgumentException ("Неизвестный URI: $uri")
        }
    }
}