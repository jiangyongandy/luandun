package com.jy.xinlangweibo.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.jy.xinlangweibo.models.net.sinaapi.sinabean.GeoBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.GeoBeanConverter;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.PicUrlsBeanConverter;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusBeanConverter;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBeanConverter;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STATUS_BEAN".
*/
public class StatusBeanDao extends AbstractDao<StatusBean, Void> {

    public static final String TABLENAME = "STATUS_BEAN";

    /**
     * Properties of entity StatusBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property OwnerId = new Property(0, long.class, "ownerId", false, "OWNER_ID");
        public final static Property CacheType = new Property(1, String.class, "cacheType", false, "CACHE_TYPE");
        public final static Property Created_at = new Property(2, String.class, "created_at", false, "CREATED_AT");
        public final static Property Id = new Property(3, long.class, "id", false, "ID");
        public final static Property Mid = new Property(4, String.class, "mid", false, "MID");
        public final static Property Idstr = new Property(5, String.class, "idstr", false, "IDSTR");
        public final static Property Text = new Property(6, String.class, "text", false, "TEXT");
        public final static Property TextLength = new Property(7, int.class, "textLength", false, "TEXT_LENGTH");
        public final static Property Source_allowclick = new Property(8, int.class, "source_allowclick", false, "SOURCE_ALLOWCLICK");
        public final static Property Source_type = new Property(9, int.class, "source_type", false, "SOURCE_TYPE");
        public final static Property Source = new Property(10, String.class, "source", false, "SOURCE");
        public final static Property Favorited = new Property(11, boolean.class, "favorited", false, "FAVORITED");
        public final static Property Truncated = new Property(12, boolean.class, "truncated", false, "TRUNCATED");
        public final static Property In_reply_to_status_id = new Property(13, String.class, "in_reply_to_status_id", false, "IN_REPLY_TO_STATUS_ID");
        public final static Property In_reply_to_user_id = new Property(14, String.class, "in_reply_to_user_id", false, "IN_REPLY_TO_USER_ID");
        public final static Property In_reply_to_screen_name = new Property(15, String.class, "in_reply_to_screen_name", false, "IN_REPLY_TO_SCREEN_NAME");
        public final static Property Thumbnail_pic = new Property(16, String.class, "thumbnail_pic", false, "THUMBNAIL_PIC");
        public final static Property Bmiddle_pic = new Property(17, String.class, "bmiddle_pic", false, "BMIDDLE_PIC");
        public final static Property Original_pic = new Property(18, String.class, "original_pic", false, "ORIGINAL_PIC");
        public final static Property User = new Property(19, String.class, "user", false, "USER");
        public final static Property Retweeted_status = new Property(20, String.class, "retweeted_status", false, "RETWEETED_STATUS");
        public final static Property Geo = new Property(21, String.class, "geo", false, "GEO");
        public final static Property Reposts_count = new Property(22, int.class, "reposts_count", false, "REPOSTS_COUNT");
        public final static Property Comments_count = new Property(23, int.class, "comments_count", false, "COMMENTS_COUNT");
        public final static Property Attitudes_count = new Property(24, int.class, "attitudes_count", false, "ATTITUDES_COUNT");
        public final static Property IsLongText = new Property(25, boolean.class, "isLongText", false, "IS_LONG_TEXT");
        public final static Property Mlevel = new Property(26, int.class, "mlevel", false, "MLEVEL");
        public final static Property Biz_feature = new Property(27, long.class, "biz_feature", false, "BIZ_FEATURE");
        public final static Property Page_type = new Property(28, int.class, "page_type", false, "PAGE_TYPE");
        public final static Property HasActionTypeCard = new Property(29, int.class, "hasActionTypeCard", false, "HAS_ACTION_TYPE_CARD");
        public final static Property UserType = new Property(30, int.class, "userType", false, "USER_TYPE");
        public final static Property Positive_recom_flag = new Property(31, int.class, "positive_recom_flag", false, "POSITIVE_RECOM_FLAG");
        public final static Property Gif_ids = new Property(32, String.class, "gif_ids", false, "GIF_IDS");
        public final static Property Is_show_bulletin = new Property(33, int.class, "is_show_bulletin", false, "IS_SHOW_BULLETIN");
        public final static Property Pic_urls = new Property(34, String.class, "pic_urls", false, "PIC_URLS");
    };

    private final UserBeanConverter userConverter = new UserBeanConverter();
    private final StatusBeanConverter retweeted_statusConverter = new StatusBeanConverter();
    private final GeoBeanConverter geoConverter = new GeoBeanConverter();
    private final PicUrlsBeanConverter pic_urlsConverter = new PicUrlsBeanConverter();

    public StatusBeanDao(DaoConfig config) {
        super(config);
    }
    
    public StatusBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STATUS_BEAN\" (" + //
                "\"OWNER_ID\" INTEGER NOT NULL ," + // 0: ownerId
                "\"CACHE_TYPE\" TEXT," + // 1: cacheType
                "\"CREATED_AT\" TEXT," + // 2: created_at
                "\"ID\" INTEGER NOT NULL ," + // 3: id
                "\"MID\" TEXT," + // 4: mid
                "\"IDSTR\" TEXT," + // 5: idstr
                "\"TEXT\" TEXT," + // 6: text
                "\"TEXT_LENGTH\" INTEGER NOT NULL ," + // 7: textLength
                "\"SOURCE_ALLOWCLICK\" INTEGER NOT NULL ," + // 8: source_allowclick
                "\"SOURCE_TYPE\" INTEGER NOT NULL ," + // 9: source_type
                "\"SOURCE\" TEXT," + // 10: source
                "\"FAVORITED\" INTEGER NOT NULL ," + // 11: favorited
                "\"TRUNCATED\" INTEGER NOT NULL ," + // 12: truncated
                "\"IN_REPLY_TO_STATUS_ID\" TEXT," + // 13: in_reply_to_status_id
                "\"IN_REPLY_TO_USER_ID\" TEXT," + // 14: in_reply_to_user_id
                "\"IN_REPLY_TO_SCREEN_NAME\" TEXT," + // 15: in_reply_to_screen_name
                "\"THUMBNAIL_PIC\" TEXT," + // 16: thumbnail_pic
                "\"BMIDDLE_PIC\" TEXT," + // 17: bmiddle_pic
                "\"ORIGINAL_PIC\" TEXT," + // 18: original_pic
                "\"USER\" TEXT," + // 19: user
                "\"RETWEETED_STATUS\" TEXT," + // 20: retweeted_status
                "\"GEO\" TEXT," + // 21: geo
                "\"REPOSTS_COUNT\" INTEGER NOT NULL ," + // 22: reposts_count
                "\"COMMENTS_COUNT\" INTEGER NOT NULL ," + // 23: comments_count
                "\"ATTITUDES_COUNT\" INTEGER NOT NULL ," + // 24: attitudes_count
                "\"IS_LONG_TEXT\" INTEGER NOT NULL ," + // 25: isLongText
                "\"MLEVEL\" INTEGER NOT NULL ," + // 26: mlevel
                "\"BIZ_FEATURE\" INTEGER NOT NULL ," + // 27: biz_feature
                "\"PAGE_TYPE\" INTEGER NOT NULL ," + // 28: page_type
                "\"HAS_ACTION_TYPE_CARD\" INTEGER NOT NULL ," + // 29: hasActionTypeCard
                "\"USER_TYPE\" INTEGER NOT NULL ," + // 30: userType
                "\"POSITIVE_RECOM_FLAG\" INTEGER NOT NULL ," + // 31: positive_recom_flag
                "\"GIF_IDS\" TEXT," + // 32: gif_ids
                "\"IS_SHOW_BULLETIN\" INTEGER NOT NULL ," + // 33: is_show_bulletin
                "\"PIC_URLS\" TEXT);"); // 34: pic_urls
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STATUS_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, StatusBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getOwnerId());
 
        String cacheType = entity.getCacheType();
        if (cacheType != null) {
            stmt.bindString(2, cacheType);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(3, created_at);
        }
        stmt.bindLong(4, entity.getId());
 
        String mid = entity.getMid();
        if (mid != null) {
            stmt.bindString(5, mid);
        }
 
        String idstr = entity.getIdstr();
        if (idstr != null) {
            stmt.bindString(6, idstr);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(7, text);
        }
        stmt.bindLong(8, entity.getTextLength());
        stmt.bindLong(9, entity.getSource_allowclick());
        stmt.bindLong(10, entity.getSource_type());
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(11, source);
        }
        stmt.bindLong(12, entity.getFavorited() ? 1L: 0L);
        stmt.bindLong(13, entity.getTruncated() ? 1L: 0L);
 
        String in_reply_to_status_id = entity.getIn_reply_to_status_id();
        if (in_reply_to_status_id != null) {
            stmt.bindString(14, in_reply_to_status_id);
        }
 
        String in_reply_to_user_id = entity.getIn_reply_to_user_id();
        if (in_reply_to_user_id != null) {
            stmt.bindString(15, in_reply_to_user_id);
        }
 
        String in_reply_to_screen_name = entity.getIn_reply_to_screen_name();
        if (in_reply_to_screen_name != null) {
            stmt.bindString(16, in_reply_to_screen_name);
        }
 
        String thumbnail_pic = entity.getThumbnail_pic();
        if (thumbnail_pic != null) {
            stmt.bindString(17, thumbnail_pic);
        }
 
        String bmiddle_pic = entity.getBmiddle_pic();
        if (bmiddle_pic != null) {
            stmt.bindString(18, bmiddle_pic);
        }
 
        String original_pic = entity.getOriginal_pic();
        if (original_pic != null) {
            stmt.bindString(19, original_pic);
        }
 
        UserBean user = entity.getUser();
        if (user != null) {
            stmt.bindString(20, userConverter.convertToDatabaseValue(user));
        }
 
        StatusBean retweeted_status = entity.getRetweeted_status();
        if (retweeted_status != null) {
            stmt.bindString(21, retweeted_statusConverter.convertToDatabaseValue(retweeted_status));
        }
 
        GeoBean geo = entity.getGeo();
        if (geo != null) {
            stmt.bindString(22, geoConverter.convertToDatabaseValue(geo));
        }
        stmt.bindLong(23, entity.getReposts_count());
        stmt.bindLong(24, entity.getComments_count());
        stmt.bindLong(25, entity.getAttitudes_count());
        stmt.bindLong(26, entity.getIsLongText() ? 1L: 0L);
        stmt.bindLong(27, entity.getMlevel());
        stmt.bindLong(28, entity.getBiz_feature());
        stmt.bindLong(29, entity.getPage_type());
        stmt.bindLong(30, entity.getHasActionTypeCard());
        stmt.bindLong(31, entity.getUserType());
        stmt.bindLong(32, entity.getPositive_recom_flag());
 
        String gif_ids = entity.getGif_ids();
        if (gif_ids != null) {
            stmt.bindString(33, gif_ids);
        }
        stmt.bindLong(34, entity.getIs_show_bulletin());
 
        List pic_urls = entity.getPic_urls();
        if (pic_urls != null) {
            stmt.bindString(35, pic_urlsConverter.convertToDatabaseValue(pic_urls));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, StatusBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getOwnerId());
 
        String cacheType = entity.getCacheType();
        if (cacheType != null) {
            stmt.bindString(2, cacheType);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(3, created_at);
        }
        stmt.bindLong(4, entity.getId());
 
        String mid = entity.getMid();
        if (mid != null) {
            stmt.bindString(5, mid);
        }
 
        String idstr = entity.getIdstr();
        if (idstr != null) {
            stmt.bindString(6, idstr);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(7, text);
        }
        stmt.bindLong(8, entity.getTextLength());
        stmt.bindLong(9, entity.getSource_allowclick());
        stmt.bindLong(10, entity.getSource_type());
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(11, source);
        }
        stmt.bindLong(12, entity.getFavorited() ? 1L: 0L);
        stmt.bindLong(13, entity.getTruncated() ? 1L: 0L);
 
        String in_reply_to_status_id = entity.getIn_reply_to_status_id();
        if (in_reply_to_status_id != null) {
            stmt.bindString(14, in_reply_to_status_id);
        }
 
        String in_reply_to_user_id = entity.getIn_reply_to_user_id();
        if (in_reply_to_user_id != null) {
            stmt.bindString(15, in_reply_to_user_id);
        }
 
        String in_reply_to_screen_name = entity.getIn_reply_to_screen_name();
        if (in_reply_to_screen_name != null) {
            stmt.bindString(16, in_reply_to_screen_name);
        }
 
        String thumbnail_pic = entity.getThumbnail_pic();
        if (thumbnail_pic != null) {
            stmt.bindString(17, thumbnail_pic);
        }
 
        String bmiddle_pic = entity.getBmiddle_pic();
        if (bmiddle_pic != null) {
            stmt.bindString(18, bmiddle_pic);
        }
 
        String original_pic = entity.getOriginal_pic();
        if (original_pic != null) {
            stmt.bindString(19, original_pic);
        }
 
        UserBean user = entity.getUser();
        if (user != null) {
            stmt.bindString(20, userConverter.convertToDatabaseValue(user));
        }
 
        StatusBean retweeted_status = entity.getRetweeted_status();
        if (retweeted_status != null) {
            stmt.bindString(21, retweeted_statusConverter.convertToDatabaseValue(retweeted_status));
        }
 
        GeoBean geo = entity.getGeo();
        if (geo != null) {
            stmt.bindString(22, geoConverter.convertToDatabaseValue(geo));
        }
        stmt.bindLong(23, entity.getReposts_count());
        stmt.bindLong(24, entity.getComments_count());
        stmt.bindLong(25, entity.getAttitudes_count());
        stmt.bindLong(26, entity.getIsLongText() ? 1L: 0L);
        stmt.bindLong(27, entity.getMlevel());
        stmt.bindLong(28, entity.getBiz_feature());
        stmt.bindLong(29, entity.getPage_type());
        stmt.bindLong(30, entity.getHasActionTypeCard());
        stmt.bindLong(31, entity.getUserType());
        stmt.bindLong(32, entity.getPositive_recom_flag());
 
        String gif_ids = entity.getGif_ids();
        if (gif_ids != null) {
            stmt.bindString(33, gif_ids);
        }
        stmt.bindLong(34, entity.getIs_show_bulletin());
 
        List pic_urls = entity.getPic_urls();
        if (pic_urls != null) {
            stmt.bindString(35, pic_urlsConverter.convertToDatabaseValue(pic_urls));
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public StatusBean readEntity(Cursor cursor, int offset) {
        StatusBean entity = new StatusBean( //
            cursor.getLong(offset + 0), // ownerId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // cacheType
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // created_at
            cursor.getLong(offset + 3), // id
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // mid
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // idstr
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // text
            cursor.getInt(offset + 7), // textLength
            cursor.getInt(offset + 8), // source_allowclick
            cursor.getInt(offset + 9), // source_type
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // source
            cursor.getShort(offset + 11) != 0, // favorited
            cursor.getShort(offset + 12) != 0, // truncated
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // in_reply_to_status_id
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // in_reply_to_user_id
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // in_reply_to_screen_name
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // thumbnail_pic
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // bmiddle_pic
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // original_pic
            cursor.isNull(offset + 19) ? null : userConverter.convertToEntityProperty(cursor.getString(offset + 19)), // user
            cursor.isNull(offset + 20) ? null : retweeted_statusConverter.convertToEntityProperty(cursor.getString(offset + 20)), // retweeted_status
            cursor.isNull(offset + 21) ? null : geoConverter.convertToEntityProperty(cursor.getString(offset + 21)), // geo
            cursor.getInt(offset + 22), // reposts_count
            cursor.getInt(offset + 23), // comments_count
            cursor.getInt(offset + 24), // attitudes_count
            cursor.getShort(offset + 25) != 0, // isLongText
            cursor.getInt(offset + 26), // mlevel
            cursor.getLong(offset + 27), // biz_feature
            cursor.getInt(offset + 28), // page_type
            cursor.getInt(offset + 29), // hasActionTypeCard
            cursor.getInt(offset + 30), // userType
            cursor.getInt(offset + 31), // positive_recom_flag
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // gif_ids
            cursor.getInt(offset + 33), // is_show_bulletin
            cursor.isNull(offset + 34) ? null : pic_urlsConverter.convertToEntityProperty(cursor.getString(offset + 34)) // pic_urls
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, StatusBean entity, int offset) {
        entity.setOwnerId(cursor.getLong(offset + 0));
        entity.setCacheType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreated_at(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setId(cursor.getLong(offset + 3));
        entity.setMid(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIdstr(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setText(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTextLength(cursor.getInt(offset + 7));
        entity.setSource_allowclick(cursor.getInt(offset + 8));
        entity.setSource_type(cursor.getInt(offset + 9));
        entity.setSource(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFavorited(cursor.getShort(offset + 11) != 0);
        entity.setTruncated(cursor.getShort(offset + 12) != 0);
        entity.setIn_reply_to_status_id(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setIn_reply_to_user_id(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setIn_reply_to_screen_name(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setThumbnail_pic(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setBmiddle_pic(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setOriginal_pic(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setUser(cursor.isNull(offset + 19) ? null : userConverter.convertToEntityProperty(cursor.getString(offset + 19)));
        entity.setRetweeted_status(cursor.isNull(offset + 20) ? null : retweeted_statusConverter.convertToEntityProperty(cursor.getString(offset + 20)));
        entity.setGeo(cursor.isNull(offset + 21) ? null : geoConverter.convertToEntityProperty(cursor.getString(offset + 21)));
        entity.setReposts_count(cursor.getInt(offset + 22));
        entity.setComments_count(cursor.getInt(offset + 23));
        entity.setAttitudes_count(cursor.getInt(offset + 24));
        entity.setIsLongText(cursor.getShort(offset + 25) != 0);
        entity.setMlevel(cursor.getInt(offset + 26));
        entity.setBiz_feature(cursor.getLong(offset + 27));
        entity.setPage_type(cursor.getInt(offset + 28));
        entity.setHasActionTypeCard(cursor.getInt(offset + 29));
        entity.setUserType(cursor.getInt(offset + 30));
        entity.setPositive_recom_flag(cursor.getInt(offset + 31));
        entity.setGif_ids(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setIs_show_bulletin(cursor.getInt(offset + 33));
        entity.setPic_urls(cursor.isNull(offset + 34) ? null : pic_urlsConverter.convertToEntityProperty(cursor.getString(offset + 34)));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(StatusBean entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(StatusBean entity) {
        return null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
