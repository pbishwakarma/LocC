package disruptdc.locc.ui;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

import disruptdc.locc.R;
import disruptdc.locc.components.DataStorage;
import disruptdc.locc.components.Friend;

public class LeaderSelectionActivity extends AppCompatActivity {

    private CheckBoxList friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_selection);

        display();
    }

    public void display(){
        ArrayList<Friend> friends = new ArrayList<>();

        for (Friend friend : DataStorage.friends) {
            if (friend.getChecked()) friends.add(friend);
        }


        friendsList = new CheckBoxList(this, R.layout.friend_info, friends);
        ListView listView = (ListView) findViewById(R.id.leader_list_view);
        listView.setAdapter(friendsList);
    }

    private class CheckBoxList extends ArrayAdapter<Friend> {

        private ArrayList<Friend> friendList;

        public CheckBoxList(Context context, int textViewId, ArrayList<Friend> friendList){
            super(context, textViewId, friendList);
            this.friendList = new ArrayList<Friend>();
            this.friendList.addAll(friendList);
        }

        private class tempView{
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            tempView temp = null;

            if(convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.friend_info, null);

                temp = new tempView();
                temp.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(temp);

                temp.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Friend friend = (Friend) cb.getTag();
                        friend.setChecked(cb.isChecked());

                    }
                });
            }
            else{
                temp = (tempView) convertView.getTag();
            }

            Friend friend = friendList.get(position);
            temp.name.setText(friend.getName());
            temp.name.setChecked(friend.getChecked());
            temp.name.setTag(friend);

            return convertView;
        }

    }
}
