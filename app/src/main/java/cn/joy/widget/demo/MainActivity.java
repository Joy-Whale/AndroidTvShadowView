package cn.joy.widget.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * User: JiYu
 * Date: 2016-08-26
 * Time: 15-29
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.main_layout2).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.main_layout2:
				startActivity(new Intent(MainActivity.this, GridTestActivity.class));
				break;
		}
	}
}
