package disruptdc.locc.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import disruptdc.locc.components.DataStorage;
import disruptdc.locc.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch groupType;

    private TextView leaderLabel;
    private TextView areaLabel;

    private TextView areaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        groupType = (Switch) findViewById(R.id.group_switch);

        leaderLabel = (TextView) findViewById(R.id.leader_label);
        areaLabel = (TextView) findViewById(R.id.area_label);

        areaSelected = (TextView) findViewById(R.id.area_selected);

        // if location already selected, modify view accordingly
        if (DataStorage.locationSelected) {
            groupType.setChecked(true);

            leaderLabel.setTypeface(null, Typeface.NORMAL);
            areaLabel.setTypeface(null, Typeface.BOLD);

            areaSelected.setVisibility(View.VISIBLE);
        }

        // set toggle behavior
        groupType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // send to map screen
                Intent intent = new Intent(getApplicationContext(), MapPlaceHolderActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
