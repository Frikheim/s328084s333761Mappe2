package com.example.s328084s333761mappe2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class KontaktProvider extends ContentProvider {

    public final static String PROVIDER = "com.example.contentproviderkontakt";
    private static final int KONTAKT = 1;
    private static final int MKONTAKT = 2;
    DBHandler db;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "kontakt", MKONTAKT);
        uriMatcher.addURI(PROVIDER, "kontakt/#", KONTAKT);
    }


    @Override
    public boolean onCreate() {
        db = new DBHandler(getContext());
        db.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uriMatcher.match(uri) == KONTAKT) {
            return db.contentProviderHentEn(uri,projection,selectionArgs,sortOrder);
        }
        else {
            return db.contentProviderHentAlle();
        }
    }


    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case MKONTAKT:
                return"vnd.android.cursor.dir/vnd.example.kontakt";
            case KONTAKT:
                return"vnd.android.cursor.item/vnd.example.kontakt";
            default:
                throw new IllegalArgumentException("Ugyldig URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Cursor c = db.contentProviderLeggTil(values);
        c.moveToLast();
        long minid= c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
