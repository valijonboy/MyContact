package uz.pop.mycontact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private int resource;
    private List<Contact> contactList;
    public ContactAdapter( Context context, int resource, List<Contact> contactList) {
        super(context, resource, contactList);
        this.context = context;
        this.resource = resource;
        this.contactList = contactList;
    }

    @Override
    public void add( Contact object) {
        contactList.add(object);
        super.add(object);
    }

    @Override
    public View getView(int position,  View convertView,ViewGroup parent) {
        String name = getItem(position).getName();
        ViewHolder viewHolder = new ViewHolder();
        Contact contact = new Contact(name);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        viewHolder.tvName = convertView.findViewById(R.id.adapter_tv_name);
        viewHolder.tvName.setText(contact.getName());
        convertView.setTag(viewHolder);
        return convertView;
    }
    static class ViewHolder{
        TextView tvName;
    }
}
