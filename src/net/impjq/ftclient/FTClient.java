package net.impjq.ftclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FTClient extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	public static final String TAG = FTClient.class.getSimpleName();

	private Button mUpdateStatusButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		init();
		Intent intent = new Intent();
        intent.setClass(this, UpdateStatusActivity.class);
        startActivity(intent);
        finish();
	}

	void init() {
		mUpdateStatusButton = (Button) findViewById(R.id.update_status_button);

		mUpdateStatusButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		int id = v.getId();

		switch (id) {
		case R.id.update_status_button: {
			Intent intent = new Intent();
			intent.setClass(this, UpdateStatusActivity.class);
			startActivity(intent);

			break;
		}

		default:
			break;
		}

	}
}