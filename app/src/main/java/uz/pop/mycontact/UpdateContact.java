package uz.pop.mycontact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateContact extends AppCompatActivity {
    private String relationship;
    EditText etName, etNumber, etEmail,etOrganization;
    Button btnRelationship, btnUpdate;
    Context context;
    DBHandler dbHandler;
    String mainId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        etName = findViewById(R.id.update_et_name);
        etNumber = findViewById(R.id.update_etNumber);
        etEmail = findViewById(R.id.update_etEmail);
        etOrganization = findViewById(R.id.update_etOrganization);

        btnRelationship = findViewById(R.id.update__btn_relationship);
        btnUpdate = findViewById(R.id.update_update__btn);
        context = this;
        dbHandler = new DBHandler(this);
         relationship = "Unspecified";

         getUpdateContact();

        btnRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRelationship();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChangedContact();
            }
        });


    }

    public void getUpdateContact(){
        String name, number, email, organization, relationship;
        mainId = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        number = getIntent().getExtras().getString("number");
        email = getIntent().getExtras().getString("email");
        organization = getIntent().getExtras().getString("organization");
        relationship = getIntent().getExtras().getString("relationship");

        etName.setText(name);
        etNumber.setText(number);
        etEmail.setText(email);
        etOrganization.setText(organization);
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

    public void updateChangedContact(){
        String  eName, eNumber, eEmail, eOrganization, eRelationship;

        eName = etName.getText().toString();
        eNumber = etNumber.getText().toString();
        eEmail = etEmail.getText().toString();
        eOrganization = etOrganization.getText().toString();
        eRelationship = relationship;

        Contact contact = new Contact(mainId, eName, eNumber, eEmail, eOrganization, eRelationship);
        dbHandler.updateContact(contact);
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
