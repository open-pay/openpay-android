/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.android.example;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class MessageDialogFragment extends DialogFragment {
    public static MessageDialogFragment newInstance(int title, String message) {
        MessageDialogFragment fragment = new MessageDialogFragment();

        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("message", message);

        fragment.setArguments(args);

        return fragment;
   }

    public MessageDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        String message = getArguments().getString("message");

        return new AlertDialog.Builder(getActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            })
            .create();
    }
}