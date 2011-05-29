package net.impjq.ftclient;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class UpdateStatus extends BaseActivity implements OnClickListener {
	public static final String TAG = UpdateStatus.class.getSimpleName();

	private EditText mUpdateStatusInputEditText;
	private Button mUpdateStatusUpdateButton;
	private ProgressBar mUpdateStatusProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.update_status_layout);

	}

	private void init() {
		// TODO Auto-generated method stub
		mUpdateStatusInputEditText = (EditText) findViewById(R.id.update_status_input_edittext);
		mUpdateStatusUpdateButton = (Button) findViewById(R.id.update_status_update_button);
		mUpdateStatusProgressBar = (ProgressBar) findViewById(R.id.update_status_progressbar);

		mUpdateStatusUpdateButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		int id = v.getId();

		switch (id) {
		case R.id.update_status_update_button: {
			updateStatus();

			break;
		}

		default:
			break;
		}

	}

	private void updateStatus() {

	}

}
