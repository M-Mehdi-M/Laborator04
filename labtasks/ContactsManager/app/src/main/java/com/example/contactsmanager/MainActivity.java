package com.example.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CONTACTS_MANAGER_REQUEST_CODE = 2017;

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText jobTitleEditText;
    private EditText companyEditText;
    private EditText websiteEditText;
    private EditText imEditText;

    private Button showHideAdditionalFieldsButton;
    private LinearLayout additionalFieldsContainer;

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.show_hide_additional_fields) {
                if (additionalFieldsContainer.getVisibility() == View.VISIBLE) {
                    showHideAdditionalFieldsButton.setText(getResources().getString(R.string.show_additional_fields));
                    additionalFieldsContainer.setVisibility(View.GONE);
                } else {
                    showHideAdditionalFieldsButton.setText(getResources().getString(R.string.hide_additional_fields));
                    additionalFieldsContainer.setVisibility(View.VISIBLE);
                }
            } else if (viewId == R.id.save_button) {
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String jobTitle = jobTitleEditText.getText().toString();
                String company = companyEditText.getText().toString();
                String website = websiteEditText.getText().toString();
                String im = imEditText.getText().toString();

                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                if (!name.isEmpty()) intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                if (!phone.isEmpty()) intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                if (!email.isEmpty()) intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                if (!address.isEmpty()) intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                if (!jobTitle.isEmpty()) intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                if (!company.isEmpty()) intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);

                ArrayList<ContentValues> contactData = new ArrayList<>();
                if (!website.isEmpty()) {
                    ContentValues websiteRow = new ContentValues();
                    websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                    websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                    contactData.add(websiteRow);
                }
                if (!im.isEmpty()) {
                    ContentValues imRow = new ContentValues();
                    imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                    imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                    contactData.add(imRow);
                }
                if (!contactData.isEmpty()) {
                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                }
                startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);

            } else if (viewId == R.id.cancel_button) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.name_edit_text);
        phoneEditText = findViewById(R.id.phone_number_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        jobTitleEditText = findViewById(R.id.job_title_edit_text);
        companyEditText = findViewById(R.id.company_edit_text);
        websiteEditText = findViewById(R.id.website_edit_text);
        imEditText = findViewById(R.id.im_edit_text);

        showHideAdditionalFieldsButton = findViewById(R.id.show_hide_additional_fields);
        showHideAdditionalFieldsButton.setOnClickListener(buttonClickListener);
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(buttonClickListener);
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(buttonClickListener);

        additionalFieldsContainer = findViewById(R.id.additional_fields_container);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("com.example.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CONTACTS_MANAGER_REQUEST_CODE) {
            setResult(resultCode, new Intent());
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
}
