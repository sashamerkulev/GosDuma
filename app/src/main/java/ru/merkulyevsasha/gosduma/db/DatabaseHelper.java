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
import ru.merkulyevsasha.gosduma.models.Codifier;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.models.ListData;

public class DatabaseHelper {

    public final static String ASC = " asc";
    private final static String DESC = " desc";

    public final static String DATABASE_NAME = "gosduma.db";

    public final static String COMMITTEE_TABLE_NAME = "Committee";
    public final static String STAD_TABLE_NAME = "StageViewDb";
    public final static String REG_TABLE_NAME = "RegionalEntity";
    public final static String FED_TABLE_NAME = "FederalEntity";
    public final static String BLOCKS_TABLE_NAME = "Topic";
    public final static String OTRAS_TABLE_NAME = "IndustryLegislation";
    public final static String INST_TABLE_NAME = "InstanceView";

    private final static String ARTICLE_TABLE_NAME = "Article";

    private final static String DEPUTY_TABLE_NAME = "DeputyDb";
    private final static String DEPUTYINFO_TABLE_NAME = "DeputyInfoDb";

    private final static String LAW_TABLE_NAME = "LawDb";
    private final static String LAWDEPUTY_TABLE_NAME = "LawDeputy";

    private final static String DEPUTYREQUEST_TABLE_NAME = "DeputyRequestDb";

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
    private final static String RANKS = "ranks";
    private final static String DEGRESS = "degrees";

    private final static String SOURCE = "Source";
    private final static String LINK = "Link";
    private final static String TITLE = "Title";
    private final static String PUBDATE = "PublicationDate";
    private final static String DESCRIPTION = "Text";

    private final static String NUMBER = "number";
    private final static String COMMENTS = "comments";
    private final static String INTODUCTIONDATE = "introductionDate";
    private final static String LASTEVENTSOLUTION = "lastEventSolution";
    private final static String RESPONSIBLENAME = "responsibleName";
    private final static String TYPE = "type";
    private final static String LASTEVENTPHASEID = "lastEventPhaseId";
    private final static String LASTEVENTSTAGEID = "lastEventStageId";
    private final static String RESPONSIBLEID = "responsibleId";

    private final static String REQUESTID = "requestId";
    private final static String DOCUMENTNUMBER = "documentNumber";
    private final static String INITIATOR = "initiator";
    private final static String REQUESTDATE = "requestDate";
    private final static String RESOLUTION = "resolution";
    private final static String SIGNEDBY_NAME = "signedBy_name";
    private final static String ANSWER = "answer";
    private final static String ADDRESSEE_NAME = "addressee_name";

    private WeakReference<Context> mContext;

    // https://habrahabr.ru/post/27108/
    private static volatile DatabaseHelper mInstance;

    public static DatabaseHelper getInstance(final Context context) {
        if (mInstance == null) {
            synchronized (DatabaseHelper.class) {
                if (mInstance == null) {
                    mInstance = new DatabaseHelper();
                }
            }
        }
        mInstance.mContext = new WeakReference<>(context);
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

    private DatabaseHelper() {
    }

    public static String getSortDirection(int oldSort, int newSort, String direction){
        if (oldSort == newSort){
            if (direction.trim().equals(DatabaseHelper.DESC.trim())){
                return DatabaseHelper.ASC;
            } else {
                return DatabaseHelper.DESC;
            }
        } else {
            return DatabaseHelper.ASC;
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
        List<ListData> items = new ArrayList<>();
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
        List<Article> items = new ArrayList<>();
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
                cursor.close();
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

        item.birthdate = cursor.getLong(cursor.getColumnIndex(BIRTHDATE));
        item.credentialsStart = cursor.getLong(cursor.getColumnIndex(CREDENTIALSSTART));
        item.credentialsEnd = cursor.getLong(cursor.getColumnIndex(CREDENTIALSEND));
        item.degrees = cursor.getString(cursor.getColumnIndex(DEGRESS));
        item.ranks = cursor.getString(cursor.getColumnIndex(RANKS));

        return item;
    }

    public List<Deputy> search(String searchText, String orderBy, String position, int isCurrent) {
        List<Deputy> items = new ArrayList<>();
        String selectQuery = "SELECT  d.id, d.name, d.position, d.isCurrent, di.birthdate, di.credentialsStart, di.credentialsEnd "
                + " , di.factionName, di.factionRole, di.factionRegion, di.ranks, di.degrees "
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

    private Law getLaw(Cursor cursor){
        Law item = new Law();

        item.id = cursor.getInt(cursor.getColumnIndex(ID));
        item.name = cursor.getString(cursor.getColumnIndex(NAME));
        item.number = cursor.getString(cursor.getColumnIndex(NUMBER));
        item.comments = cursor.getString(cursor.getColumnIndex(COMMENTS));
        item.introductionDate = cursor.getLong(cursor.getColumnIndex(INTODUCTIONDATE));
        item.lastEventSolution = cursor.getString(cursor.getColumnIndex(LASTEVENTSOLUTION));
        item.responsibleName = cursor.getString(cursor.getColumnIndex(RESPONSIBLENAME));
        item.type = cursor.getString(cursor.getColumnIndex(TYPE));
        item.lastEventPhaseId = cursor.getInt(cursor.getColumnIndex(LASTEVENTPHASEID));
        item.lastEventStageId = cursor.getInt(cursor.getColumnIndex(LASTEVENTSTAGEID));
        //item.responsibleId = cursor.getInt(cursor.getColumnIndex(RESPONSIBLEID));

        return item;
    }

    public List<Law> getDeputyLaws(int deputyId, String searchText, String orderBy) {
        List<Law> items = new ArrayList<>();
        String selectQuery = "select l.id, l.number, l.name, l.comments, l.name_lowcase, l.comments_lowcase, l.introductionDate,  "
                + " l.lastEventStageId, l.lastEventPhaseId, l.responsibleName, l.responsibleName_lower, l.lastEventSolution, l.lastEventSolution_lowcase, l.type "
                +" from "+LAW_TABLE_NAME+" l"
                + " join "+LAWDEPUTY_TABLE_NAME+" ld on ld.LawId = l.id"
                + " where ld.DeputyId = @deputyId ";

        if (!searchText.isEmpty()) {
            selectQuery = selectQuery + " and (l.number like @search or l.comments_lowcase like @search or l.name_lowcase like @search or l.lastEventSolution_lowcase like @search or l.responsibleName_lower like @search) ";
        }

        selectQuery = selectQuery + " order by " + orderBy;

        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {

                Cursor cursor;
                if (searchText.isEmpty()) {
                    cursor = mSqlite.rawQuery(selectQuery, new String[]{String.valueOf(deputyId)});
                } else {
                    cursor = mSqlite.rawQuery(selectQuery, new String[]{String.valueOf(deputyId), "%" + searchText.toLowerCase() + "%"});
                }

                if (cursor.moveToFirst()) {
                    do {
                        Law item = getLaw(cursor);
                        items.add(item);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
        return items;
    }

    public List<Law> getLaws(String searchText, String orderBy) {
        List<Law> items = new ArrayList<>();
        String selectQuery = "select id, number, name, comments, name_lowcase, comments_lowcase, introductionDate,  " +
                " lastEventStageId, lastEventPhaseId, responsibleName, responsibleName_lower, lastEventSolution, lastEventSolution_lowcase, type "
                +" from "+LAW_TABLE_NAME +
                " where name_lowcase like @search or number like @search or responsibleName_lower like @search or lastEventSolution_lowcase like @search or introductionDate like @search";

        selectQuery = selectQuery + " order by " + orderBy;

        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {

                Cursor cursor = mSqlite.rawQuery(selectQuery, new String[]{"%" + searchText.toLowerCase() + "%"});

                if (cursor.moveToFirst()) {
                    do {
                        Law item = getLaw(cursor);
                        items.add(item);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
        return items;
    }

    public List<DeputyRequest> getDeputyRequests(String searchText, String orderBy) {
        List<DeputyRequest> items = new ArrayList<>();
        String selectQuery = "select requestId, initiator, requestDate, name,  " +
                " documentNumber, resolution,  answer,  signedBy_name, " +
                " addressee_name " +
                " from " + DEPUTYREQUEST_TABLE_NAME;

        if (!searchText.isEmpty()) {
            selectQuery = selectQuery + " where initiator_lowcase like @search or name_lowcase like @search or resolution_lowcase like @search or " +
                    " answer_lowcase like @search or signedBy_name_lowcase like @search or addressee_name_lowcase like @search or " +
                    " documentNumber like @search ";
        }

        selectQuery = selectQuery + " order by " + orderBy;

        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {

                Cursor cursor;
                if (searchText.isEmpty()) {
                    cursor = mSqlite.rawQuery(selectQuery, null);
                } else {
                    cursor = mSqlite.rawQuery(selectQuery, new String[]{"%" + searchText.toLowerCase() + "%"});
                }

                if (cursor.moveToFirst()) {
                    do {
                        DeputyRequest item = new DeputyRequest();

                        item.requestId = cursor.getInt(cursor.getColumnIndex(REQUESTID));
                        item.name = cursor.getString(cursor.getColumnIndex(NAME));
                        item.documentNumber = cursor.getString(cursor.getColumnIndex(DOCUMENTNUMBER));
                        item.initiator = cursor.getString(cursor.getColumnIndex(INITIATOR));
                        item.requestDate = cursor.getLong(cursor.getColumnIndex(REQUESTDATE));
                        item.resolution = cursor.getString(cursor.getColumnIndex(RESOLUTION));
                        item.signedBy_name = cursor.getString(cursor.getColumnIndex(SIGNEDBY_NAME));
                        item.answer = cursor.getString(cursor.getColumnIndex(ANSWER));
                        item.addressee_name = cursor.getString(cursor.getColumnIndex(ADDRESSEE_NAME));

                        items.add(item);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
        return items;
    }

    private List<Codifier> getCodifiers(String selectQuery, int id) {
        List<Codifier> items = new ArrayList<>();

        SQLiteDatabase mSqlite = openOrCreateDatabase();
        try {
            if (mSqlite != null) {

                Cursor cursor = mSqlite.rawQuery(selectQuery, new String[]{String.valueOf(id)});

                if (cursor.moveToFirst()) {
                    do {
                        Codifier item = new Codifier();
                        item.id = cursor.getInt(cursor.getColumnIndex(ID));
                        item.name = cursor.getString(cursor.getColumnIndex(NAME));
                        if (item.name == null || item.name.isEmpty())
                            continue;
                        items.add(item);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        } finally {
            if (mSqlite != null && mSqlite.isOpen())
                mSqlite.close();
        }
        return items;
    }

    public Codifier getPhaseById(int id) {
        List<Codifier> items = getCodifiers("select id, name from phase where id = @id", id);
        return items == null || items.size() == 0? new Codifier() : items.get(0);
    }

    public Codifier getStageById(int id) {
        List<Codifier> items = getCodifiers("select id, name from StageViewDb where id = @id", id);
        return items == null || items.size() == 0? new Codifier() : items.get(0);
    }

    public List<Codifier> getProfileComittees(int id) {
        return getCodifiers("select c.id, c.name from LawProfile lp left join Committee c on c.id = lp.CommitteeId where lp.LawId = @id", id);
    }

    public List<Codifier> getCoexecutorCommittees(int id) {
        return getCodifiers("select c.id, c.name from LawCoexecutor lp left join Committee c on c.id = lp.CommitteeId where lp.LawId = @id", id);
    }

    public List<Codifier> getLawDeputies(int id) {
        return getCodifiers("select c.id, c.name from LawDeputy lp left join DeputyDb c on c.id = lp.DeputyId where lp.LawId = @id", id);
    }

    public List<Codifier> getLawRegionals(int id) {
        return getCodifiers("select c.id, c.name from LawDepartment lp left join RegionalEntity c on c.id = lp.DepartmentId where lp.LawId = @id", id);
    }

    public List<Codifier> getLawFederals(int id) {
        return getCodifiers("select c.id, c.name from LawDepartment lp left join FederalEntity c on c.id = lp.DepartmentId where lp.LawId = @id", id);
    }

}


