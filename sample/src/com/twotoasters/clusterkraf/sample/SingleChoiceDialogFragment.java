/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf.sample;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SingleChoiceDialogFragment extends DialogFragment {

	private WeakReference<Host> hostRef;

	public static final String KEY_TITLE = "title";
	public static final String KEY_OPTIONS = "options";
	public static final String KEY_SELECTED_OPTION = "selected option";

	public SingleChoiceDialogFragment() {
		// no-op
	}

	public static SingleChoiceDialogFragment newInstance(Host host, Bundle args) {
		if (args != null) {
			if (args.containsKey(KEY_TITLE) == false || args.containsKey(KEY_OPTIONS) == false || args.containsKey(KEY_SELECTED_OPTION) == false) {
				throw new RuntimeException("at least one required arg was missing");
			}
		} else {
			throw new RuntimeException("args must not be null");
		}
		SingleChoiceDialogFragment fragment = new SingleChoiceDialogFragment();
		fragment.setArguments(args);
		fragment.setHost(host);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = null;
		Activity activity = getActivity();
		Bundle args = getArguments();
		String title = args.getString(KEY_TITLE);
		String[] options = args.getStringArray(KEY_OPTIONS);
		if (options == null || options.length == 0) {
			int[] intOptions = args.getIntArray(KEY_OPTIONS);
			options = new String[intOptions.length];
			NumberFormat nf = NumberFormat.getInstance();
			for (int i = 0; i < intOptions.length; i++) {
				options[i] = nf.format(intOptions[i]);
			}
		}
		int selectedOption = args.getInt(KEY_SELECTED_OPTION);
		if (activity != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle(title);
			builder.setSingleChoiceItems(options, selectedOption, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Host host = hostRef.get();
					if (host != null) {
						host.onOptionChosen(getTag(), which);
					}
					dialog.dismiss();
				}
			});
			dialog = builder.create();
		}
		return dialog;
	}

	public void setHost(Host host) {
		hostRef = new WeakReference<Host>(host);
	}

	public interface Host {
		void onOptionChosen(String tag, int index);
	}
}