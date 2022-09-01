package com.example.biplobi_cr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mEditText;
    private CheckBox mCheckBox;
    private EditText signUpEmailEditText, signUpPasswordEditText,signUpNameEditText,signUpRollEditText,signUpStatusEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Spinner statusSpinner;
    String[] statusString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");

        statusString=getResources().getStringArray(R.array.status_string);

        mAuth = FirebaseAuth.getInstance();

        mEditText= (EditText)findViewById(R.id.signUpPasswordEditTextId);
        mCheckBox= (CheckBox)findViewById(R.id.IncheckBox);

        signUpEmailEditText= (EditText)findViewById(R.id.signUpEmailEditTextId);
        signUpPasswordEditText= (EditText)findViewById(R.id.signUpPasswordEditTextId);
        signUpNameEditText= (EditText)findViewById(R.id.signUpNameEditTextId);
        signUpRollEditText= (EditText)findViewById(R.id.signUpRollEditTextId);
        statusSpinner= (Spinner) findViewById(R.id.statusSpinnerId);

        signUpButton= (Button) findViewById(R.id.signUpButtonId);
        signInTextView= (TextView) findViewById(R.id.signInTextViewId);
        progressBar=(ProgressBar)findViewById(R.id.progressbarId);
        signInTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.sample_view,R.id.textViewsampleId,statusString);
        statusSpinner.setAdapter(adapter);


        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // show password
                    mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUpButtonId:
                userRegister();
                break;

            case R.id.signInTextViewId:
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void userRegister() {

        final String email= signUpEmailEditText.getText().toString().trim();
        String password=signUpPasswordEditText.getText().toString().trim();
        final String name=signUpNameEditText.getText().toString().trim();
        final String roll=signUpRollEditText.getText().toString().trim();
        final String status=statusSpinner.getSelectedItem().toString();

        if(email.isEmpty())
        {
            signUpEmailEditText.setError("Enter an email address");
            signUpEmailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpEmailEditText.setError("Enter a valid email address");
            signUpEmailEditText.requestFocus();
            return;

        }

        if(password.isEmpty())
        {
            signUpPasswordEditText.setError("Enter a password");
            signUpPasswordEditText.requestFocus();
            return;

        }


        if(password.length()<6)
        {
            signUpPasswordEditText.setError("Minimu Length of a password should be 6 characters");
            signUpPasswordEditText.requestFocus();
            return;

        }
        if(name.isEmpty())
        {

            signUpNameEditText.setError("Enter a Name");
            signUpNameEditText.requestFocus();
            return;

        }
        if(roll.isEmpty())
        {


            signUpRollEditText.setError("Enter a Roll");
            signUpRollEditText.requestFocus();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {

                    Student student=new Student(name,roll,email,status);
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    FirebaseDatabase.getInstance()
                            .getReference("Students/"+uid).setValue(student);
                    Toast.makeText(getApplicationContext(),"Register Successfull",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(),"Already Registered",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });




    }
}
