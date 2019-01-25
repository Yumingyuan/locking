package buaa.irisking.scanner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDataBase extends SQLiteOpenHelper {

    // 数据库名、表名
    private static final String DATABASE_NAME = "IrisDemo.db";
    private static final String TABLE_NAME_USER_DATA = "user_data";
    // 数据库初始版本
    private static final int VERSION = 1;

    // 列名
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_UID = "uid";
    private static final String COLUMN_NAME_USER_FAVICON = "user_favicon";
    private static final String COLUMN_NAME_USER_NAME = "user_name";
    private static final String COLUMN_NAME_LEFT_FEATURE = "left_feature";
    private static final String COLUMN_NAME_RIGHT_FEATURE = "right_feature";
    private static final String COLUMN_NAME_LEFT_FEATURE_COUNT = "left_feature_count";
    private static final String COLUMN_NAME_RIGHT_FEATURE_COUNT = "right_feature_count";
    private static final String COLUMN_NAME_ENCROLL_TIME = "enroll_dateTime";

    // 创建用户表sql语句
    private static final String CREATE_USER_DATA_TABLE = "CREATE TABLE " + "if not exists "
            + TABLE_NAME_USER_DATA + " ("
            + COLUMN_NAME_ID + " integer primary key autoincrement,"
            + COLUMN_NAME_UID + " varchar(30), "
            + COLUMN_NAME_USER_FAVICON + " INTEGER, "
            + COLUMN_NAME_USER_NAME + " varchar(30), "
            + COLUMN_NAME_LEFT_FEATURE + " TEXT, "
            + COLUMN_NAME_RIGHT_FEATURE + " TEXT, "
            + COLUMN_NAME_LEFT_FEATURE_COUNT + " INTEGER, "
            + COLUMN_NAME_RIGHT_FEATURE_COUNT + " INTEGER, "
            + COLUMN_NAME_ENCROLL_TIME + " varchar(30))";

    // 删除用户表sql语句
    private static final String DROP_TABLE_NAME_USER_DATA = "drop table if exists "
            + TABLE_NAME_USER_DATA;

    private static SqliteDataBase sInstance;

    public static synchronized SqliteDataBase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SqliteDataBase(context);
        }
        return sInstance;
    }

    private SqliteDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_NAME_USER_DATA);
        onCreate(db);
    }


    /**
     * 根据左眼标志获取虹膜特征信息集合
     *
     * @return 返回查询到的虹膜特征信息集合
     */
    public List<IrisUserInfo> queryLeftFeature() {
        List<IrisUserInfo> temp = this.queryAll();
        List<IrisUserInfo> userArray = new ArrayList<IrisUserInfo>();

        for (IrisUserInfo userInfo: temp) {
            if (userInfo.m_LeftTemplate_Count > 0) {
                userArray.add(userInfo);
            }
        }

        return userArray;
    }


    /**
     * 根据右眼标志获取虹膜特征信息集合
     *
     * @return 返回查询到的虹膜特征信息集合
     */
    public List<IrisUserInfo> queryRightFeature() {

        List<IrisUserInfo> temp = this.queryAll();
        List<IrisUserInfo> userArray = new ArrayList<IrisUserInfo>();

        for (IrisUserInfo userInfo: temp) {
            if (userInfo.m_RightTemplate_Count > 0) {
                userArray.add(userInfo);
            }
        }

        return userArray;
    }



    /**
     * 查询全部用户实体
     *
     * @return 返回查询到的用户实体集合
     */
    public List<IrisUserInfo> queryAll() {
        SQLiteDatabase db = sInstance.getReadableDatabase();
        final String querySql = "select * from " + TABLE_NAME_USER_DATA;
        Cursor cursor = db.rawQuery(querySql, null);

        if (cursor == null) {
            throw new IllegalArgumentException("cursor is null");
        }

        List<IrisUserInfo> userArray = new ArrayList<IrisUserInfo>();

        IrisUserInfo user_info = null;
        while (cursor.moveToNext()) {
            user_info = new IrisUserInfo();
            user_info.m_Id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
            user_info.m_Uid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_UID));
            user_info.m_UserFavicon = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_USER_FAVICON));
            user_info.m_UserName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER_NAME));
            user_info.m_LeftTemplate = cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_LEFT_FEATURE));
            user_info.m_RightTemplate = cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_RIGHT_FEATURE));
            user_info.m_LeftTemplate_Count = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_LEFT_FEATURE_COUNT));
            user_info.m_RightTemplate_Count = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_RIGHT_FEATURE_COUNT));
            user_info.m_EnrollTime = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ENCROLL_TIME));
            userArray.add(user_info);
        }
        cursor.close();
        db.close();

        return userArray;
    }

}
