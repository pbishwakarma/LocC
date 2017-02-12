package disruptdc.locc.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


import disruptdc.locc.components.Friend;
import disruptdc.locc.R;

public class FriendsActivity extends Activity {

    private CheckBoxList friendsList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        display();

        checkButtonPress();


    }

    public void display(){
        ArrayList<Friend> friends = new ArrayList<>();

        Friend user1 = new Friend("Sid", false);
        Friend user2 = new Friend("Ben", false);
        Friend user3 = new Friend("Prajal", false);
        Friend user4 = new Friend("Turner", false);
        Friend user5 = new Friend("Sally", false);
        Friend user6 = new Friend("Timmy", false);

        friends.add(user1);
        friends.add(user2);
        friends.add(user3);
        friends.add(user4);
        friends.add(user5);
        friends.add(user6);

        friendsList = new CheckBoxList(this, R.layout.friend_info, friends);
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(friendsList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Friend friend = (Friend) adapterView.getItemAtPosition(pos);
            }
        });


    }
    private class CheckBoxList extends ArrayAdapter<Friend>{

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
            //Log.v("ConvertView", String.valueOf(position));

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

    private void checkButtonPress() {
        Button aButton = (Button) findViewById(R.id.findSelected);
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer response = new StringBuffer();
                response.append("The following friends were added to your LocC \n");

                ArrayList<Friend> friends = friendsList.friendList;
                for (int i = 0; i < friends.size(); i++) {
                    Friend tempFriend = friends.get(i);
                    response.append("\n" + tempFriend.getName());
                }

                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

            }
        });
    }


}
