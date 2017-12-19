package cimdata.android.dez2017.implizitintents;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final int REQUEST_CODE_INTENT_CONTACT = 100;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.actions)
        );
        */

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.actions,
                android.R.layout.simple_list_item_1
        );

        list = findViewById(R.id.list_acmain_intents);
        list.setAdapter(adapter);

        list.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {

            case 0: {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "www.google.com");
                startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                startActivity(intent);
                break;
            }
            case 2: {
                Uri receiver = Uri.parse("smsto: +491234567890");
                Intent intent = new Intent(Intent.ACTION_SENDTO, receiver);
                intent.putExtra("sms_body", "Dies ist eine Test-SMS!");
                startActivity(intent);
                break;
            }
            case 3: {
                Uri receiver = Uri.parse("tel: +491234567890");
                Intent intent = new Intent(Intent.ACTION_VIEW, receiver);
                startActivity(intent);
                break;
            }
            case 4: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rtc2822");
                intent.putExtra(Intent.EXTRA_EMAIL, "foo@bar.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "foobar subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Body text goes here");
                startActivity(intent);
                break;
            }
            case 5: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE_INTENT_CONTACT);
                break;
            }
            default:

        }

    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == REQUEST_CODE_INTENT_CONTACT) {

                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "Fehler bei der Anfrage", Toast.LENGTH_SHORT).show();
                    return;
                }

                int id;
                String displayName;

                Uri contactUri = data.getData();

                Cursor cursor = getContentResolver().query(
                    contactUri,
                    null,
                    null,
                    null,
                    null
                );

                if (cursor.moveToFirst()) {

                    int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    id = cursor.getInt(idIndex);

                    int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    displayName = cursor.getString(displayNameIndex);

                    String message = "id: " + id + "\n" + "display name: " + displayName;

                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }


            }




    }
}
