package com.nothing.settings.glyphs.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ContactsManager {
    public static Uri CONTACTSURI = ContactsContract.Contacts.CONTENT_URI;
    private static volatile ContactsManager instance;

    public static ContactsManager getInstance() {
        if (instance == null) {
            synchronized (ContactsManager.class) {
                if (instance == null) {
                    instance = new ContactsManager();
                }
            }
        }
        return instance;
    }

    public List<Contact> searchCustomRingtoneContactsList(Context context) {
        ArrayList arrayList = new ArrayList();
        String[] strArr = {"_id", "display_name", "custom_ringtone"};
        Cursor query = context.getContentResolver().query(CONTACTSURI, strArr, (String) null, (String[]) null, "sort_key ASC");
        int columnIndexOrThrow = query.getColumnIndexOrThrow(strArr[0]);
        int columnIndexOrThrow2 = query.getColumnIndexOrThrow(strArr[1]);
        int columnIndexOrThrow3 = query.getColumnIndexOrThrow(strArr[2]);
        if (query.getCount() > 0) {
            arrayList = new ArrayList();
        }
        while (query.moveToNext()) {
            String string = query.getString(columnIndexOrThrow3);
            if (string != null) {
                arrayList.add(new Contact(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2), string, getRingtoneTitle(string)));
            }
        }
        query.close();
        return arrayList;
    }

    public static String getRingtoneTitle(String str) {
        String[] split;
        if (str == null) {
            return null;
        }
        try {
            str = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] split2 = str.split("[?]");
        if (split2.length == 2 && (split = split2[1].split("&")) != null) {
            for (String str2 : split) {
                if (str2.length() > 0) {
                    String[] split3 = str2.split("=");
                    if (split3.length == 2 && split3[0].equals("title")) {
                        return split3[1];
                    }
                }
            }
        }
        return null;
    }

    @TargetApi(5)
    public void updateContactRingtone(Context context, String str, String str2) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put("custom_ringtone", str2);
        context.getContentResolver().update(uri, contentValues, "_id=?", new String[]{str});
        Log.d("ContactsManager", "contactId " + str + " ringtoneUri " + str2);
    }

    @TargetApi(5)
    public void updateAllContactRingtone(Context context, String str) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put("custom_ringtone", "");
        ContentResolver contentResolver = context.getContentResolver();
        int update = contentResolver.update(uri, contentValues, "custom_ringtone like '" + str + "%'", (String[]) null);
        Log.d("ContactsManager", " ringtoneUri " + str + "  result " + update);
    }

    public static class Contact {
        private String contactId;
        private String displayName;
        private int itemType = 2;
        private String ledId;
        private String ledUri;
        private String ringtoneTitle;
        private int ringtoneType;
        private String ringtoneUri;
        private long updateTime;

        public Contact() {
        }

        public Contact(String str, String str2, String str3, String str4) {
            this.contactId = str;
            this.displayName = str2;
            this.ringtoneTitle = str4;
            this.ringtoneUri = str3;
        }

        public Contact(String str, String str2, String str3, String str4, String str5, int i, long j) {
            this.displayName = str;
            this.ledId = str2;
            this.ledUri = str3;
            this.ringtoneUri = str4;
            this.contactId = str5;
            this.ringtoneType = i;
            this.updateTime = j;
            this.ringtoneTitle = ContactsManager.getRingtoneTitle(str4);
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getRingtoneUri() {
            return this.ringtoneUri;
        }

        public void setRingtoneUri(String str) {
            this.ringtoneUri = str;
        }

        public String getRingtoneTitle() {
            return this.ringtoneTitle;
        }

        public String getContactId() {
            return this.contactId;
        }
    }
}
