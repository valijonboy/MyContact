package uz.pop.mycontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<Contact> contactsList;
    Contact contact;
    Context context;
    DBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        contactsList = new ArrayList<>();
        contact = new Contact();
        context = this;
        handler = new DBHandler(context);
        contactsList = handler.getAllContact();

        showAllContacts();
        viewUpdateDeleteContact();

    }
    private void showAllContacts(){
        ContactAdapter adapter = new ContactAdapter(context, R.layout.contact_adapter, handler.getAllContact());
        listView.setAdapter(adapter);
    }

    public void viewUpdateDeleteContact(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence options[] = {"View", "Update", "Delete"};
                final Contact contact = (Contact)parent.getAdapter().getItem(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                getContactDetails(contact);
                                break;
                            case 1:
                               sendContactsDetails(contact);
                               break;

                            case 2:
                              deleteContact(contact);
                              break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void getContactDetails(Contact contact){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(contact.getName());
        builder1.setMessage(contact.getNumber() + "\n"
                +contact.getEmail() + "\n"
                + contact.getOrganization() + "\n"
                + contact.getRelationship());
        builder1.show();
    }

    private void sendContactsDetails(Contact contact){
        Intent intent = new Intent(context, UpdateContact.class);
        intent.putExtra("id", contact.getId());
        intent.putExtra("name", contact.getNumber());
        intent.putExtra("email", contact.getEmail());
        intent.putExtra("organization", contact.getOrganization());
        intent.putExtra("relationship", contact.getRelationship());
        startActivity(intent);
    }

    private void deleteContact(final Contact contact){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setTitle("Do you want to delete this contact");
        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.deleteContact(contact);
                showAllContacts();
                Toast.makeText(context, "the contact has been deleted succesfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAllContacts();
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ContactAdapter adapter = new ContactAdapter(context, R.layout.contact_adapter, handler.searchContact(newText));
                listView.setAdapter(adapter);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add_btn) {
            Intent intent = new Intent(MainActivity.this, AddContact.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
