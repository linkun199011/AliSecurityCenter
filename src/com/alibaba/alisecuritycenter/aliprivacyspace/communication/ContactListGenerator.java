package com.alibaba.alisecuritycenter.aliprivacyspace.communication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;

public class ContactListGenerator {
    private Context mContext;
    private static final String[] PROJECTION = { Phone.DISPLAY_NAME,
            Phone.NUMBER, //
            Phone.PHOTO_ID, // contacts 表中的photo_id字段
            Phone.CONTACT_ID, // raw_contacts 表中的contact_id字段
            Phone.RAW_CONTACT_ID // raw_contacts 表中的_id字段
    };
    private static final int DISPLAY_NAME_INDEX = 0;
    private static final int NUMBER_INDEX = 1;
    private static final int PHOTO_ID_INDEX = 2;
    private static final int CONTACT_ID_INDEX = 3;
    private static final int RAW_CONTACT_ID = 4;

    public ContactListGenerator(Context context) {
        this.mContext = context;
    }

    public List<ContactItem> generateContactList() throws SQLException{
        List<ContactItem> list = new ArrayList<ContactItem>();
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(Phone.CONTENT_URI, PROJECTION, null, null,
                    Phone.SORT_KEY_PRIMARY + " ASC");
            while (cursor.moveToNext()) {
                String name = cursor.getString(DISPLAY_NAME_INDEX);
                // Log.d("ContactListGenerator", name);
                String number = cursor.getString(NUMBER_INDEX);
                int photo_id = cursor.getInt(PHOTO_ID_INDEX);
                int contact_id = cursor.getInt(CONTACT_ID_INDEX);
                int raw_contact_id = cursor.getInt(RAW_CONTACT_ID);

                Bitmap head = null;
                if (photo_id > 0) {
                    Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
                            contact_id);
                    InputStream inputStream = Contacts
                            .openContactPhotoInputStream(resolver, uri);
                    head = BitmapFactory.decodeStream(inputStream);
                }

                ContactItem item = new ContactItem(name, number, head,
                        contact_id, raw_contact_id);
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return list;
    }
}
