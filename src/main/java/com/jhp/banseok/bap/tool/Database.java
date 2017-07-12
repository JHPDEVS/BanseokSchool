package com.jhp.banseok.bap.tool;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by whdghks913 on 2015-02-23.
 */
public class Database {
    private SQLiteDatabase mDatabase;
    private ContentValues recordValues;

    /**
     * 데이터베이스를 엽니다.
     *
     * @param path
     * @param dbName
     */
    public void openDatabase(String path, String dbName) {
        try {
            if (mDatabase == null)
                mDatabase = SQLiteDatabase.openDatabase(path + dbName, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스가 없으면 만들고, 있으면 엽니다 또한 db파일을 만들때 table까지 함께 만듭니다
     *
     * @param path /sdcard/database/와 같이 완전한 풀 경로
     */
    public void openOrCreateDatabase(String path, String dbName, String tableName, String Column) {
        try {
            if (new File(path + dbName).exists()) {
                mDatabase = SQLiteDatabase.openDatabase(path + dbName, null, SQLiteDatabase.OPEN_READWRITE);
            } else {
                File mFolder = new File(path);
                if (!mFolder.exists())
                    mFolder.mkdirs();

                mDatabase = SQLiteDatabase.openOrCreateDatabase(path + dbName, null);
            }
            createTable(tableName, Column);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * table을 만듭니다
     * openOrCreateDatabase메소드에서 파일이 없을때 테이블도 함께 만듭니다.
     * 그러므로 따로 table을 추가할때 말고는 사용하지 마세요
     *
     * @param tableName table의 이름을 입력합니다
     * @param Column    만들 column을 입력합니다 "title text, data integer"
     */
    public void createTable(String tableName, String Column) {
        try {
            String CREATE_SQL = "create table if not exists " + tableName + "("
                    + " _id integer PRIMARY KEY autoincrement, " + Column + ")";
            mDatabase.execSQL(CREATE_SQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTable(String tableName) {
        try {
            String DROP_SQL = "drop table if exists " + tableName;
            mDatabase.execSQL(DROP_SQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 추가할 데이터를 입력하세요
     * 한번에 한 key당 한개의 values를 입력할수 있으며 commit후 적용됩니다
     */
    public Database addData(String key, boolean value) {
        if (recordValues == null) {
            recordValues = new ContentValues();
        }

        recordValues.put(key, value);

        return this;
    }

    public Database addData(String key, int value) {
        if (recordValues == null) {
            recordValues = new ContentValues();
        }

        recordValues.put(key, value);

        return this;
    }

    public Database addData(String key, long value) {
        if (recordValues == null) {
            recordValues = new ContentValues();
        }

        recordValues.put(key, value);

        return this;
    }

    public Database addData(String key, float value) {
        if (recordValues == null) {
            recordValues = new ContentValues();
        }

        recordValues.put(key, value);

        return this;
    }

    public Database addData(String key, double value) {
        if (recordValues == null) {
            recordValues = new ContentValues();
        }

        recordValues.put(key, value);

        return this;
    }

    public Database addData(String key, String value) {
        if (recordValues == null) {
            recordValues = new ContentValues();
        }

        recordValues.put(key, value);

        return this;
    }

    public void commit(String tableName) {
        try {
            mDatabase.insert(tableName, null, recordValues);
            recordValues = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * update TableName set MyName=Mir where age=18
     *
     * @param tableName
     * @param key
     * @param value
     */
    public void update(String tableName, String key, String value) {
        try {
            String UpdateSQL = "update " + tableName + " set " + key + "='" + value + "'";
            mDatabase.execSQL(UpdateSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String tableName, String key, String value, String checkKey, String checkValue) {
        try {
            String UpdateSQL = "update " + tableName + " set " + key + "='" + value + "' where " + checkKey + "='" + checkValue + "'";
            mDatabase.execSQL(UpdateSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터를 반환할때 사용합니다
     * Cursor를 반환하며 더 정확한 데이터 추출은 아래 Example을 확인하세요
     *
     * @param tableName
     * @param Column    table만들때 입력한거 "title, data"
     * @return
     */
    public Cursor getData(String tableName, String Column) {
        String SQL = "select " + Column + " from " + tableName;
        Cursor mCursor = mDatabase.rawQuery(SQL, null);

        return mCursor;

        // int recordCount = mCursor.getCount();
        //
        // for (int i = 0; i < recordCount; i++) {
        // mCursor.moveToNext();
        // String title = mCursor.getString(0);
        // String memo = mCursor.getString(1);
        // String date = mCursor.getString(2);
        // }
    }

    public Cursor getData(String tableName) {
        String SQL = "select * from " + tableName;
        Cursor mCursor = mDatabase.rawQuery(SQL, null);

        return mCursor;
    }

    public Cursor getLastData(String tableName) {
        String SQL = "select * from " + tableName;
        Cursor mCursor = mDatabase.rawQuery(SQL, null);

        mCursor.moveToLast();

        return mCursor;
    }

    /**
     * db에서 데이터를 삭제하기 위한 메소드
     * checkKey와 checkValue는 중복 데이터가 있을경우 선택 삭제가 가능하기 위해 구현
     * checkKey = "    _id='2'      "
     * exam) mData.remove("TableTable", "title", "HIHI", "_id='5'");
     *
     * @param tableName
     * @param key
     * @param value
     * @return
     */
    public boolean remove(String tableName, String key, boolean value) {
        return mDatabase.delete(tableName, key + "= '" + value + "'", null) > 0;
    }

    public boolean remove(String tableName, String key, boolean value,
                          String checkKey) {
        return mDatabase.delete(tableName, key + "= '" + value + "'" + " and "
                + checkKey, null) > 0;
    }

    public boolean remove(String tableName, String key, int value) {
        return mDatabase.delete(tableName, key + "= '" + value + "'", null) > 0;
    }

    public boolean remove(String tableName, String key, int value,
                          String checkKey) {
        return mDatabase.delete(tableName, key + "= '" + value + "'" + " and "
                + checkKey, null) > 0;
    }

    public boolean remove(String tableName, String key, long value) {
        return mDatabase.delete(tableName, key + "= '" + value + "'", null) > 0;
    }

    public boolean remove(String tableName, String key, long value,
                          String checkKey) {
        return mDatabase.delete(tableName, key + "= '" + value + "'" + " and "
                + checkKey, null) > 0;
    }

    public boolean remove(String tableName, String key, float value) {
        return mDatabase.delete(tableName, key + "= '" + value + "'", null) > 0;
    }

    public boolean remove(String tableName, String key, float value,
                          String checkKey) {
        return mDatabase.delete(tableName, key + "= '" + value + "'" + " and "
                + checkKey, null) > 0;
    }

    public boolean remove(String tableName, String key, double value) {
        return mDatabase.delete(tableName, key + "= '" + value + "'", null) > 0;
    }

    public boolean remove(String tableName, String key, double value,
                          String checkKey) {
        return mDatabase.delete(tableName, key + "= '" + value + "'" + " and "
                + checkKey, null) > 0;
    }

    public boolean remove(String tableName, String key, String value) {
        return mDatabase.delete(tableName, key + "= '" + value + "'", null) > 0;
    }

    public boolean remove(String tableName, String key, String value,
                          String checkKey) {
        return mDatabase.delete(tableName, key + "= '" + value + "'" + " and "
                + checkKey, null) > 0;
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public void release() {
        SQLiteDatabase.releaseMemory();
        mDatabase.close();
    }
}
