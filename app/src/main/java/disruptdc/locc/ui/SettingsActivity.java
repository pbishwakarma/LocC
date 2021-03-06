package disruptdc.locc.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import disruptdc.locc.components.DataStorage;
import disruptdc.locc.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch groupType;

    private TextView leaderLabel;
    private TextView areaLabel;

    private SeekBar radiusSeekbar;

    private int radiusValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataStorage.teamLeader = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        groupType = (Switch) findViewById(R.id.group_switch);

        leaderLabel = (TextView) findViewById(R.id.leader_label);
        areaLabel = (TextView) findViewById(R.id.area_label);

        // if location already selected, modify view accordingly
//        if (DataStorage.locationSelected) {
//            groupType.setChecked(true);
//
//            leaderLabel.setTypeface(null, Typeface.NORMAL);
//            areaLabel.setTypeface(null, Typeface.BOLD);
//
//            areaSelected.setVisibility(View.VISIBLE);
//        }

        // set toggle behavior
        groupType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    DataStorage.teamLeader = false;

                    leaderLabel.setTypeface(null, Typeface.NORMAL);
                    areaLabel.setTypeface(null, Typeface.BOLD);
                } else {
                    DataStorage.teamLeader = true;

                    leaderLabel.setTypeface(null, Typeface.BOLD);
                    areaLabel.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        // makes appropriate changes when complexity is changed
        radiusSeekbar = (SeekBar) findViewById(R.id.radius_seekbar);

        radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView radiusLabel;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusValue = (progress + 1) * 100;
                radiusLabel = (TextView) findViewById(R.id.radius_label);
                radiusLabel.setText("Radius: " + radiusValue + " meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void loccIn(View view) {
        DataStorage.radius = radiusValue;

        if (DataStorage.teamLeader == true) {
            Intent intent = new Intent(this, LeaderSelectionActivity.class);

            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MapsActivity.class);

            DataStorage.pinDroppable = true;
            DataStorage.shouldDraw = true;

            startActivity(intent);
        }

    }
}
