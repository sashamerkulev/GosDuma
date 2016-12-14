package ru.merkulyevsasha.gosduma.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.ListData;

public class DatabaseHelper {

    public final static String ASC = " asc";
    public final static String DESC = " desc";

    public final static String DATABASE_NAME = "gosduma.db";

    public final static String COMMITTEE_TABLE_NAME = "Committee";
    public final static String STAD_TABLE_NAME = "StageViewDb";
    public final static String REG_TABLE_NAME = "RegionalEntity";
    public final static String FED_TABLE_NAME = "FederalEntity";
    public final static String BLOCKS_TABLE_NAME = "Topic";
    public final static String OTRAS_TABLE_NAME = "IndustryLegislation";
    public final static String INST_TABLE_NAME = "InstanceView";

    public final static String ARTICLE_TABLE_NAME = "Article";

    public final static String DEPUTY_TABLE_NAME = "DeputyDb";
    public final static String DEPUTYINFO_TABLE_NAME = "DeputyInfoDb";

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String IS_CURRENT = "isCurrent";
    private final static String POSITION = "position";
    private final static String FACTIONNAME = "factionName";
    private final static String FACTIONROLE = "factionRole";
    private final static String FACTIONREGION = "factionRegion";
    private final static String BIRTHDATE = "birthdate";
    private final static String CREDENTIALSSTART = "credentialsStart";
    private final static String CREDENTIALSEND = "credentialsEnd";

    private final static String SOURCE = "Source";
    private final static String LINK = "Link";
    private final static String TITLE = "Title";
    private final static String PUBDATE = "PublicationDate";
    private final static String DESCRIPTION = "Text";


    private WeakReference<Context> mContext;

    // https://habrahabr.ru/post/27108/
    private static volatile DatabaseHelper mInstance;

    public static DatabaseHelper getInstance(final Context context) {
        if (mInstance == null) {
            synchronized (DatabaseHelper.class) {
                if (mInstance == null) {
                    mInstance = new DatabaseHelper(context);
                }
            }
        }
        mInstance.mContext = new WeakReference<Context>(context);
        return mInstance;
    }

    private SQLiteDatabase openOrCreateDatabase() {
        Context context = mContext.get();
        if (context != null) {
            File fileDb = new File(context.getFilesDir(), DATABASE_NAME);
            //noinspection ResultOfMethodCallIgnored
            return SQLiteDatabase.openOrCreateDatabase(fileDb.getPath(), null);
        }
        return null;
    }

    private DatabaseHelper(final Context context) {
        mContext = new WeakReference<Context>(context);
        SQLiteDatabase mSqlite = openOrCreateDatabase();
        if (mSqlite != null && mSqlite.getVersion() == 0) {
//            mSqlite.execSQL(DATABASE_CREATE);
//            mSqlite.setVersion(DATABASE_VERSION);
//            mSqlite.close();
        }
    }

    private ListData getListData(Cursor cursor) {
        ListData item = new ListData();
        item.id = cursor.getInt(cursor.getColumnIndex(ID));
        item.name = cursor.getString(cursor.getColumnIndex(NAME));
        int index = cursor.getColumnIndex(IS_CURRENT);
        if (index > 0) {
            item.isCurrent = cursor.getInt(index) > 0;
        }
        return item;
    }

    public List<ListData> selectAll(String tableName) {
        List<ListData> items = new ArrayList<ListData>();
        String selectQuery = "SELECT  * FROM " + tableName;
        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {

                Cursor cursor = mSqlite.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        ListData item = getListData(cursor);
                        items.add(item);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
        return items;
    }

    public void deleteAll(String tableName) {
        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {
                mSqlite.delete(tableName, null, null);
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
    }

    public void deleteArticles(int source) {
        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {
                mSqlite.delete(ARTICLE_TABLE_NAME, SOURCE + " = ?", new String[]{String.valueOf(source)});
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
    }


    public void addArticles(int source, List<Article> articles) {
        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {
                mSqlite.beginTransaction();
                for (Article item : articles) {
                    ContentValues values = new ContentValues();
                    values.put(SOURCE, String.valueOf(source));
                    values.put(LINK, item.Link);
                    values.put(TITLE, item.Title);
                    values.put(DESCRIPTION, item.Description == null ? "" : item.Description);
                    if (item.PubDate != null) {
                        values.put(PUBDATE, item.PubDate.getTime());
                        mSqlite.insert(ARTICLE_TABLE_NAME, null, values);
                    }
                }
                mSqlite.setTransactionSuccessful();
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.inTransaction())
                mSqlite.endTransaction();
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
    }

    public List<Article> getArticles(int source) {
        List<Article> items = new ArrayList<Article>();
        String selectQuery = "SELECT  * FROM " + ARTICLE_TABLE_NAME + " WHERE Source=@source";

        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {

                Cursor cursor = mSqlite.rawQuery(selectQuery, new String[]{String.valueOf(source)});

                if (cursor.moveToFirst()) {
                    do {
                        Article item = new Article();

                        item.Source = cursor.getString(cursor.getColumnIndex(SOURCE));
                        item.Link = cursor.getString(cursor.getColumnIndex(LINK));
                        item.Title = cursor.getString(cursor.getColumnIndex(TITLE));
                        item.Description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                        item.PubDate = new Date(cursor.getLong(cursor.getColumnIndex(PUBDATE)));

                        items.add(item);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
        return items;
    }

    private Deputy getDeputy(Cursor cursor) {
        Deputy item = new Deputy();

        item.id = cursor.getInt(cursor.getColumnIndex(ID));
        item.name = cursor.getString(cursor.getColumnIndex(NAME));
        item.position = cursor.getString(cursor.getColumnIndex(POSITION));
        item.isCurrent = cursor.getInt(cursor.getColumnIndex(IS_CURRENT)) > 0;

        item.fractionName = cursor.getString(cursor.getColumnIndex(FACTIONNAME));
        item.fractionRole = cursor.getString(cursor.getColumnIndex(FACTIONROLE));
        item.fractionRegion = cursor.getString(cursor.getColumnIndex(FACTIONREGION));

        item.birthdate = new Date(cursor.getLong(cursor.getColumnIndex(BIRTHDATE)));
        item.credentialsStart = new Date(cursor.getLong(cursor.getColumnIndex(CREDENTIALSSTART)));
        item.credentialsEnd = new Date(cursor.getLong(cursor.getColumnIndex(CREDENTIALSEND)));

        return item;
    }

    public List<Deputy> search(String searchText, String orderBy, String position, int isCurrent) {
        List<Deputy> items = new ArrayList<Deputy>();
        String selectQuery = "SELECT  d.id, d.name, d.position, d.isCurrent, di.birthdate, di.credentialsStart, di.credentialsEnd "
                + " , di.factionName, di.factionRole, di.factionRegion "
                + " FROM " + DEPUTY_TABLE_NAME + " d JOIN " + DEPUTYINFO_TABLE_NAME + " di on di.id = d.id ";
        selectQuery = selectQuery + " where d.position = @position and d.isCurrent = @isCurrent";
        if (!searchText.isEmpty()) {
            selectQuery = selectQuery + " and (d.name_lowcase like @search or di.factionName_lowcase like @search or di.factionRole_lowcase like @search or di.factionRegion_lowcase like @search) ";
        }

        selectQuery = selectQuery + " order by " + orderBy;

        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {

                Cursor cursor;
                if (searchText.isEmpty()) {
                    cursor = mSqlite.rawQuery(selectQuery, new String[]{position, String.valueOf(isCurrent)});
                } else {
                    cursor = mSqlite.rawQuery(selectQuery, new String[]{position, String.valueOf(isCurrent), "%" + searchText.toLowerCase() + "%"});
                }

                if (cursor.moveToFirst()) {
                    do {
                        Deputy item = getDeputy(cursor);
                        items.add(item);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
        return items;
    }

}


