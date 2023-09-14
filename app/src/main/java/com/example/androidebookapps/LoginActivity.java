package com.example.androidebookapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidebookapps.databinding.ActivityLoginBinding;
import com.example.response.LoginRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Constant;
import com.example.util.Method;
import com.example.util.StatusBar;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cn.refactor.library.SmoothCheckBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding viewLoginBinding;
    public static final String mypreference = "mypref";
    public static final String pref_email = "pref_email";
    public static final String pref_password = "pref_password";
    public static final String pref_check = "pref_check";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 007;

    //Facebook login
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    Method method;
    ProgressDialog progressDialog;
    LoginRP.ItemUser itemUser;
    boolean isWhichScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.init(LoginActivity.this);

        viewLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = viewLoginBinding.getRoot();
        setContentView(view);

        pref = getSharedPreferences(mypreference, 0); // 0 - for private mode
        editor = pref.edit();
        method = new Method(LoginActivity.this);
        method.saveFirstIsLogin(true);
        method.forceRTLIfSupported();

        progressDialog = new ProgressDialog(LoginActivity.this,R.style.MyAlertDialogStyle);

        Intent intent = getIntent();
        isWhichScreen = intent.getBooleanExtra("isFromDetail", false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //facebook button
        callbackManager = CallbackManager.Factory.create();

        viewLoginBinding.cbRemMe.setChecked(false);
        if (pref.getBoolean(pref_check, false)) {
            viewLoginBinding.edtEmail.setText(pref.getString(pref_email, null));
            viewLoginBinding.edtPass.setText(pref.getString(pref_password, null));
            viewLoginBinding.cbRemMe.setChecked(true);
        } else {
            viewLoginBinding.edtEmail.setText("");
            viewLoginBinding.edtPass.setText("");
            viewLoginBinding.cbRemMe.setChecked(false);
        }

        viewLoginBinding.tvPrivacyTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intentPage = new Intent(LoginActivity.this, PagesActivity.class);
                    intentPage.putExtra("PAGE_TITLE", Constant.appListData.getPageList().get(1).getPageTitle());
                    intentPage.putExtra("PAGE_DESC", Constant.appListData.getPageList().get(1).getPageContent());
                    startActivity(intentPage);
            }
        });

        viewLoginBinding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_skip = new Intent(LoginActivity.this, MainActivity.class);
                intent_skip.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_skip);
                finishAffinity();
            }
        });

        viewLoginBinding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
                intent_register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_register);
            }
        });

        viewLoginBinding.cbRemMe.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

            }
        });

        viewLoginBinding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = viewLoginBinding.edtEmail.getText().toString();
                String strPassword = viewLoginBinding.edtPass.getText().toString();

                viewLoginBinding.edtEmail.setError(null);
                viewLoginBinding.edtPass.setError(null);

                if (!isValidMail(strEmail) || strEmail.isEmpty()) {
                    viewLoginBinding.edtEmail.requestFocus();
                    viewLoginBinding.edtEmail.setError(getResources().getString(R.string.please_enter_email));
                } else if (strPassword.isEmpty()) {
                    viewLoginBinding.edtPass.requestFocus();
                    viewLoginBinding.edtPass.setError(getResources().getString(R.string.please_enter_password));
                } else {
                    if (viewLoginBinding.cbPrivacyTerms.isChecked()) {

                        if (viewLoginBinding.cbRemMe.isChecked()) {
                            editor.putString(pref_email, viewLoginBinding.edtEmail.getText().toString());
                            editor.putString(pref_password, viewLoginBinding.edtPass.getText().toString());
                            editor.putBoolean(pref_check, true);
                            editor.commit();
                        } else {
                            editor.putBoolean(pref_check, false);
                            editor.commit();
                        }

                        if (method.isNetworkAvailable()) {
                            login(strEmail, strPassword);
                        } else {
                            method.alertBox(getResources().getString(R.string.internet_connection));
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.please_accept), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewLoginBinding.cbPrivacyTerms.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

            }
        });

        viewLoginBinding.llFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewLoginBinding.cbPrivacyTerms.isChecked()) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(EMAIL, "public_profile"));
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.please_accept), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewLoginBinding.llGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewLoginBinding.cbPrivacyTerms.isChecked()) {
                    signIn();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.please_accept), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewLoginBinding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_forgot = new Intent(LoginActivity.this, ForgotPassActivity.class);
                intent_forgot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_forgot);
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbUser(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void login(final String sendEmail, final String sendPassword) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(LoginActivity.this));
        jsObj.addProperty("email", sendEmail);
        jsObj.addProperty("password", sendPassword);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginRP> call = apiService.getLoginData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<LoginRP>() {
            @Override
            public void onResponse(@NotNull Call<LoginRP> call, @NotNull Response<LoginRP> response) {

                try {
                    LoginRP loginRP = response.body();

                    if (loginRP !=null && loginRP.getSuccess().equals("1")) {
                        if (loginRP.getItemUserList().get(0).getSuccess().equals("1")) {
                            itemUser = loginRP.getItemUserList().get(0);
                            method.saveIsLogin(true);
                            method.saveLogin(itemUser.getUser_id(), itemUser.getName(), itemUser.getEmail(), "normal", "");
                            if (isWhichScreen) {
                                finish();
                            } else {
                                ActivityCompat.finishAffinity(LoginActivity.this);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } else {
                            method.alertBox(loginRP.getItemUserList().get(0).getMsg());
                        }

                    } else {
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<LoginRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    public void registerSocialNetwork(String aid, String sendName, String sendEmail, String type) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(LoginActivity.this));
        jsObj.addProperty("name", sendName);
        jsObj.addProperty("email", sendEmail);
        jsObj.addProperty("social_id", aid);
        jsObj.addProperty("login_type", type);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginRP> call = apiService.getLoginSocialData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<LoginRP>() {
            @Override
            public void onResponse(@NotNull Call<LoginRP> call, @NotNull Response<LoginRP> response) {

                try {
                    LoginRP loginRPSocial = response.body();

                    if (loginRPSocial !=null && loginRPSocial.getSuccess().equals("1")) {
                        if (loginRPSocial.getItemUserList().get(0).getSuccess().equals("1")) {
                            itemUser = loginRPSocial.getItemUserList().get(0);
                            method.saveIsLogin(true);
                            method.saveLogin(itemUser.getUser_id(), itemUser.getName(), itemUser.getEmail(), type, aid);
                            if (isWhichScreen) {
                                finish();
                            } else {
                                ActivityCompat.finishAffinity(LoginActivity.this);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } else {
                            method.alertBox(loginRPSocial.getItemUserList().get(0).getMsg());
                        }
                    } else {
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<LoginRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }


    //Google login
    private void signIn() {
        if (method.isNetworkAvailable()) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

    }

    //Google login get callback
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    //Google login
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            assert account != null;
            String id = account.getId();
            String name = account.getDisplayName();
            String email = account.getEmail();

            registerSocialNetwork(id, name, email, "google");

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }

    //facebook login get email and name
    private void fbUser(LoginResult loginResult) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String email = object.getString("email");
                    registerSocialNetwork(id, name, email, "facebook");
                } catch (JSONException e) {
                    try {
                        String id = object.getString("id");
                        String name = object.getString("name");
                        registerSocialNetwork(id, name, "", "facebook");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email"); // Parameters that we ask for facebook
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
}