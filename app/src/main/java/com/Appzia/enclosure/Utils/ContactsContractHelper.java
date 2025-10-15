package com.Appzia.enclosure.Utils;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

import java.util.ArrayList;

public class ContactsContractHelper {

    public static void updateContact(ContentResolver contentResolver, String lookupKey, String newName, String newNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Update name
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(Data.LOOKUP_KEY + "=?", new String[]{lookupKey})
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newName)
                .build());

        // Add second mobile number
        ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValue(Data.RAW_CONTACT_ID, getRawContactId(contentResolver, lookupKey))
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, newNumber)
                .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                .build());

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (OperationApplicationException | RemoteException e) {
            e.printStackTrace();
        }
    }

    private static long getRawContactId(ContentResolver contentResolver, String lookupKey) {
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
        Cursor cursor = contentResolver.query(uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }
}
