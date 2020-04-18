package uz.pop.mycontact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {
    private String relationship;
    EditText etName, etNumber, etEmail,etOrganization;
    Button btnRelationship, btnAdd;
    Context context;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        etName = findViewById(R.id.et_name);
        etNumber = findViewById(R.id.etNumber);
        etEmail = findViewById(R.id.etEmail);
        etOrganization = findViewById(R.id.etOrganization);

        btnRelationship = findViewById(R.id.add_btn_relationship);
        btnAdd = findViewById(R.id.add_add_btn);
        context = this;
        dbHandler = new DBHandler(this);

        String name = etName.getText().toString();
        String number = etNumber.getText().toString();
        String email = etEmail.getText().toString();
        String organization = etOrganization.getText().toString();


        btnRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRelationship();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

    }
    public void addContact(){
        String name = etName.getText().toString();
        String number = etNumber.getText().toString();
        String email = etEmail.getText().toString();
        String organization = etOrganization.getText().toString();
        relationship = "Unspecified";


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number) || !TextUtils.isEmpty(email)
        || !TextUtils.isEmpty(organization)){
            Contact contact = new Contact();
            contact.setName(name);
            contact.setNumber(number);
            contact.setEmail(email);
            contact.setRelationship(relationship);
            contact.setOrganization(organization);

            dbHandler.addContact(contact);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please fill the name and number fields!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    public void getRelationship(){
        final CharSequence options[] = {"Family", "Friend", "Neighbour", "Others"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose relationship");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        relationship = options[0].toString();
                        break;
                    case 1:
                        relationship = options[1].toString();
                        break;
                    case 2:
                        relationship = options[2].toString();
                        break;
                    case 3:
                        relationship = options[3].toString();
                        break;
                        default:
                            break;
                }

                Toast.makeText(context, relationship, Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}

