package disruptdc.locc.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.BassBoost;
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


import disruptdc.locc.components.DataStorage;
import disruptdc.locc.components.Friend;
import disruptdc.locc.R;

public class FriendsActivity extends Activity {
    private boolean friendSelected = false;
    private int checkCount = 0;
    private CheckBoxList friendsList = null;
    private Button aButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        aButton = (Button) findViewById(R.id.nextButton1);

        display();
        checkButtonPress();


    }

    public void display(){
        DataStorage.friends = new ArrayList<>();

        Friend user2 = new Friend("Ben", false, 38.905361,-77.03797);
        Friend user3 = new Friend("Prajal", false, 38.9035410,-77.033987 );
        Friend user4 = new Friend("Turner", false, 38.906346354, -77.035177946);
        Friend user5 = new Friend("Sally", false, 38.90729812, -77.03649759);
        Friend user6 = new Friend("Timmy", false, 38.9080662064, -77.009224891);

        DataStorage.friends.add(user2);
        DataStorage.friends.add(user3);
        DataStorage.friends.add(user4);
        DataStorage.friends.add(user5);
        DataStorage.friends.add(user6);

        friendsList = new CheckBoxList(this, R.layout.friend_info, DataStorage.friends);
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
                        if(cb.isChecked()){
                            checkCount++;
                        } else{
                            checkCount--;
                        }

                        if(checkCount > 0){
                            aButton.setClickable(true);
                        } else{
                            aButton.setClickable(false);
                        }

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
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuffer response = new StringBuffer();
                response.append("The following friends were added to your LocC \n");

                ArrayList<Friend> friends = friendsList.friendList;
                for (int i = 0; i < friends.size(); i++) {
                    Friend tempFriend = friends.get(i);
                    if(tempFriend.getChecked()){
                        response.append("\n" + tempFriend.getName());
                    }
                }

                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                startActivity(new Intent(FriendsActivity.this, SettingsActivity.class));

            }
        });
    }


}
