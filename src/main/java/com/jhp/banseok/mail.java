package com.jhp.banseok;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;

public class mail extends Fragment {

    private EditText recipient;
    private EditText subject;
    private EditText body;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mail, container, false);

        recipient = (EditText) view.findViewById(R.id.recipient);
        subject = (EditText) view.findViewById(R.id.subject);
        body = (EditText) view.findViewById(R.id.body);

        ButtonRectangle sendBtn = (ButtonRectangle) view.findViewById(R.id.sendEmail);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
                // after sending the email, clear the fields
                recipient.setText("");
                subject.setText("");
                body.setText("");
            }
        });

   return view; }
    protected void sendEmail() {

        String[] recipients = {recipient.getText().toString()};
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        // prompts email clients only
        email.setType("message/rfc822");

        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
        email.putExtra(Intent.EXTRA_TEXT, body.getText().toString());

        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "이메일 앱 선택"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "이메일 앱 설치 안되있음.",
                    Toast.LENGTH_LONG).show();
        }
    }

}
