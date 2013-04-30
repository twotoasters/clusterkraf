package com.twotoasters.clusterkraf.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class TwoToastersActivity extends FragmentActivity implements OnClickListener, OnLongClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.two_toasters);
		setContentView(R.layout.activity_two_toasters);
		View url = findViewById(R.id.two_toasters_url);
		url.setOnClickListener(this);
		View logo = findViewById(R.id.two_toasters_logo);
		logo.setOnClickListener(this);
		logo.setOnLongClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://" + getString(R.string.two_toasters_url)));
		startActivity(i);
	}

	@Override
	public boolean onLongClick(View v) {
		Toast.makeText(TwoToastersActivity.this, "Boom Toasted!", Toast.LENGTH_SHORT).show();
		return true;
	}
}
