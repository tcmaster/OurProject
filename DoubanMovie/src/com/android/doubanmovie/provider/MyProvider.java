package com.android.doubanmovie.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyProvider extends ContentProvider {
	private static final UriMatcher MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		MATCHER.addURI(ProviderData.AUTHORITIES,
				ProviderData.SHOWPATHALL + "/", ProviderData.MATCHSHOWALL);
		MATCHER.addURI(ProviderData.AUTHORITIES, ProviderData.SHOWPATHITEM,
				ProviderData.MATCHSHOWITEM);
		MATCHER.addURI(ProviderData.AUTHORITIES, ProviderData.INTRODUCEPATHALL,
				ProviderData.MATCHINTRODUCEALL);
		MATCHER.addURI(ProviderData.AUTHORITIES, ProviderData.INTRODUCEITEM,
				ProviderData.MATCHINTRODUCEITEM);
	}
	private DatabaseHelper helper;
	private ContentResolver resolver;

	@Override
	public boolean onCreate() {
		helper = new DatabaseHelper(getContext());
		resolver = getContext().getContentResolver();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = null;
		long id = 0;
		String where = "";
		switch (MATCHER.match(uri)) {
		case ProviderData.MATCHSHOWALL:
			cursor = db.query(ProviderData.SHOW_TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			break;
		case ProviderData.MATCHSHOWITEM:
			id = ContentUris.parseId(uri);
			where = " id = " + id;
			if (selection != null) {
				where += " and " + selection;
			}
			cursor = db.query(ProviderData.SHOW_TABLE_NAME, projection, where,
					selectionArgs, null, null, null);
			break;
		case ProviderData.MATCHINTRODUCEALL:
			cursor = db.query(ProviderData.INTRODUCE_TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			break;
		case ProviderData.MATCHINTRODUCEITEM:
			id = ContentUris.parseId(uri);
			where = " id = " + id;
			if (selection != null) {
				where += " and " + selection;
			}
			cursor = db.query(ProviderData.INTRODUCE_TABLE_NAME, projection,
					where, selectionArgs, null, null, null);
			break;
		default:
			break;
		}
		cursor.setNotificationUri(resolver, uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = helper.getWritableDatabase();
		long id = 0;
		Uri resUri = null;
		Log.v("test", "we can enter insert");
		Log.v("test", uri.toString());
		switch (MATCHER.match(uri)) {
		case ProviderData.MATCHSHOWALL:
			// Log.v("test", "we can enter match");
			id = db.insert(ProviderData.SHOW_TABLE_NAME, null, values);
			resUri = ContentUris.withAppendedId(uri, id);
			break;
		case ProviderData.MATCHINTRODUCEALL:
			id = db.insert(ProviderData.INTRODUCE_TABLE_NAME, null, values);
			resUri = ContentUris.withAppendedId(uri, id);
			break;
		default:
			Log.v("test", MATCHER.match(uri) + "");
			break;
		}
		resolver.notifyChange(resUri, null);
		return resUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int result = 0;
		switch (MATCHER.match(uri)) {
		case ProviderData.MATCHSHOWALL:
			result = db.delete(ProviderData.SHOW_TABLE_NAME, selection,
					selectionArgs);
			break;
		case ProviderData.MATCHINTRODUCEALL:
			result = db.delete(ProviderData.INTRODUCE_TABLE_NAME, selection,
					selectionArgs);
		default:
			break;
		}
		resolver.notifyChange(uri, null);
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
