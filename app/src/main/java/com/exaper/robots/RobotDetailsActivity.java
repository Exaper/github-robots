package com.exaper.robots;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.exaper.robots.data.LoginToRobotImageConverter;
import com.exaper.robots.data.model.Robot;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class RobotDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ROBOT = "robot";

    @Inject
    protected LoginToRobotImageConverter mLoginToRobotImageConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RobotsApplication.get(this).getComponent().inject(this);
        setContentView(R.layout.activity_robot_details);

        Robot robot = getIntent().getParcelableExtra(EXTRA_ROBOT);
        ImageView robotImage = (ImageView) findViewById(R.id.robotImage);
        Picasso.with(this)
                .load(mLoginToRobotImageConverter.convert(robot.getLogin()))
                .into(robotImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(robot.getLogin());
        TextView robotDetails = (TextView) findViewById(R.id.robotDetails);
        // TODO
        //robotDetails.setText(robot.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled;
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                handled = true;
                break;
            default:
                handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
