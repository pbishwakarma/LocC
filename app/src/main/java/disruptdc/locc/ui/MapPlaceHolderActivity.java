package disruptdc.locc.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import disruptdc.locc.components.DataStorage;
import disruptdc.locc.R;

public class MapPlaceHolderActivity extends AppCompatActivity {

    Button selectLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_place_holder);

        selectLocation = (Button) findViewById(R.id.select_location);

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStorage.locationSelected = true;

                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);

                startActivity(intent);
            }
        });
    }
}
