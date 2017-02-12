package disruptdc.locc.ui;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import disruptdc.locc.R;
import disruptdc.locc.components.DataStorage;
import disruptdc.locc.components.Friend;

public class LeaderSelectionActivity extends AppCompatActivity {

    private RadioGroup leads;
    private Button chooseLeader;
    private ArrayList<Friend> friends;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_selection);

        chooseLeader = (Button)findViewById(R.id.pickleader);
        chooseLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LeaderSelectionActivity.this, MapsActivity.class));
            }
        });
        chooseLeader.setEnabled(false);

        leads = (RadioGroup)findViewById(R.id.leaders);
        leads.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id == 0){
                    DataStorage.leader = null;
                } else{
                    DataStorage.leader = friends.get(id);
                }
                chooseLeader.setEnabled(true);
            }
        });

        populateFriends();
        populateLeaders();



    }

    public void populateFriends(){
        friends = new ArrayList<>();
        friends.add(new Friend("Sid", true, 0, 0)); // make sure this doesn't make it into DataStorage
        for(int i = 0; i < DataStorage.friends.size(); i++){
            if(DataStorage.friends.get(i).getChecked()){
                friends.add(DataStorage.friends.get(i));
            }
        }
    }
    public void populateLeaders(){
        for(int i = 0; i < friends.size(); i++){
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setText(friends.get(i).getName());
            rdbtn.setId(i);
            leads.addView(rdbtn);
        }
    }



}
