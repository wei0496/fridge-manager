package com.androidtutorialpoint.firebasegrocerylistapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private Button btnLogin, btnLinkToSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private EditText loginInputEmail, loginInputPassword;
    private TextInputLayout loginInputLayoutEmail, loginInputLayoutPassword;

    private View view;
    //facebook log in related
    private CallbackManager fbCallBack;
    private LoginButton fbLogBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);

        auth = FirebaseAuth.getInstance();
        //qpx
        //check user is login:
        if(auth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        view = this.getWindow().getDecorView().findViewById(android.R.id.content);

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
                    return false;
                }
            });
        }

        loginInputLayoutEmail = (TextInputLayout) findViewById(R.id.login_input_layout_email);
        loginInputLayoutPassword = (TextInputLayout) findViewById(R.id.login_input_layout_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        loginInputEmail = (EditText) findViewById(R.id.login_input_email);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLinkToSignUp = (Button) findViewById(R.id.btn_link_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        btnLinkToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });



        //facebook log in related:
        fbCallBack = CallbackManager.Factory.create();
        fbLogBtn = (LoginButton) findViewById(R.id.fbLoginBtn);
        fbLogBtn.setReadPermissions("email","public_profile","user_photos");
        fbLogBtn.registerCallback(fbCallBack, new FacebookCallback<LoginResult>() {
            private ProfileTracker profileTracker;
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.w("facebookLogin:","success");

                handleFacebookLogin(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.w("facebookLogin:","fail");

            }

            @Override
            public void onError(FacebookException error) {
                Log.w("facebookLogin:","error");

            }
        });


    }

    // facebook login:
    private void handleFacebookLogin(AccessToken token)
    {
        progressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Authentication fails", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    /**
     * Validating form
     */
    private void submitForm() {
        String email = loginInputEmail.getText().toString().trim();
        String password = loginInputPassword.getText().toString().trim();

        if(!checkEmail()) {
            return;
        }
        if(!checkPassword()) {
            return;
        }
        loginInputLayoutEmail.setErrorEnabled(false);
        loginInputLayoutPassword.setErrorEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, Log a message to the LogCat. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                             Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                        } else {
                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private boolean checkEmail() {
        String email = loginInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isEmailValid(email)) {

            loginInputLayoutEmail.setErrorEnabled(true);
            loginInputLayoutEmail.setError(getString(R.string.err_msg_email));
            loginInputEmail.setError(getString(R.string.err_msg_required));
            requestFocus(loginInputEmail);
            return false;
        }
        loginInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {

        String password = loginInputPassword.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            loginInputLayoutPassword.setError(getString(R.string.err_msg_password));
            loginInputPassword.setError(getString(R.string.err_msg_required));
            requestFocus(loginInputPassword);
            return false;
        }
        loginInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0
                );
    }

    private static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isPasswordValid(String password){
        return (password.length() >= 6);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    // for facebook login:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallBack.onActivityResult(requestCode,resultCode,data);
    }
}