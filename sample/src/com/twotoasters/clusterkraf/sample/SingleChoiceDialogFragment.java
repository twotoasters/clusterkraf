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
import android.support.v4.app.Fragment;

public class SingleChoiceDialogFragment extends DialogFragment {

	private WeakReference<Host> hostRef;

	public static final String KEY_TITLE = "title";
	public static final String KEY_OPTION = "option";
	public static final String KEY_CHOICES_STRINGS = "choices strings";
	public static final String KEY_CHOICES_INTS = "choices ints";
	public static final String KEY_SELECTION = "selection";

	public SingleChoiceDialogFragment() {
		// no-op
	}

	public static SingleChoiceDialogFragment newInstance(Bundle args) {
		if (args != null) {
			if (args.containsKey(KEY_TITLE) == false || args.containsKey(KEY_OPTION) == false
					|| (args.containsKey(KEY_CHOICES_STRINGS) == false && args.containsKey(KEY_CHOICES_INTS) == false)
					|| args.containsKey(KEY_SELECTION) == false) {
				throw new RuntimeException("at least one required arg was missing");
			}
		} else {
			throw new RuntimeException("args must not be null");
		}
		SingleChoiceDialogFragment fragment = new SingleChoiceDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = null;
		Activity activity = getActivity();
		Bundle args = getArguments();
		String title = args.getString(KEY_TITLE);
		String[] choices = new String[0];
		if (args.containsKey(KEY_CHOICES_STRINGS)) {
			choices = args.getStringArray(KEY_CHOICES_STRINGS);
		} else if (args.containsKey(KEY_CHOICES_INTS)) {
			int[] choicesInts = args.getIntArray(KEY_CHOICES_INTS);
			choices = new String[choicesInts.length];
			NumberFormat nf = NumberFormat.getInstance();
			for (int i = 0; i < choicesInts.length; i++) {
				choices[i] = nf.format(choicesInts[i]);
			}
		}
		int selection = args.getInt(KEY_SELECTION);
		if (activity != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle(title);
			builder.setSingleChoiceItems(choices, selection, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Host host = hostRef.get();
					if (host != null) {
						host.onOptionChosen(getArguments().getInt(KEY_OPTION), which);
					} else {
						throw new RuntimeException("No host");
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Fragment parent = getParentFragment();
		if (parent instanceof Host) {
			setHost((Host)parent);
		} else {
			throw new RuntimeException("Parent fragment must implement Host");
		}
	}

	public interface Host {
		void onOptionChosen(int option, int index);
	}
}