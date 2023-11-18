package com.exydos.redsocial.fragments;

import static com.exydos.redsocial.fragments.CreateAccount.EMAIL_REGEX;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exydos.redsocial.FragmentReplace;
import com.exydos.redsocial.MainActivity;
import com.exydos.redsocial.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private EditText txtCorreo, txtContrasena;
    private TextView tvLogin, tvCambiarContrasena;
    private Button btnIniciarS, btnIniciarGoogle;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;

    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        clickListener();
    }

    private void clickListener(){
        btnIniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo  = txtCorreo.getText().toString();
                String contrasena = txtContrasena.getText().toString();

                if (correo.isEmpty() || correo.matches(EMAIL_REGEX)){
                    txtCorreo.setError("Correo ingresado no valido");
                    return;
                }
                if (contrasena.isEmpty() || contrasena.length() < 6){
                    txtCorreo.setError("Contrasena debe contener 6 o mas digitos");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser usuario = auth.getCurrentUser();

                        if (!usuario.isEmailVerified()){
                            Toast.makeText(getContext(), "Por favor verifica tu correo", Toast.LENGTH_SHORT).show();
                        }

                        sendUserToMainActivity();
                    }else {
                        String exception = "Error: "+task.getException().getMessage();
                        Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        btnIniciarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentReplace) getActivity()).setFragment(new CreateAccount());
            }
        });
    }

    private  void sendUserToMainActivity(){
        if (getActivity() == null)
            return;

        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
        getActivity().finish();
    }

    private void init(View view){
        txtCorreo = view.findViewById(R.id.txtEmail);
        txtContrasena = view.findViewById(R.id.txtContrasenaS);
        btnIniciarS = view.findViewById(R.id.btnIniciarSesion);
        btnIniciarGoogle = view.findViewById(R.id.btnSesionGoogle);

        tvLogin = view.findViewById(R.id.tvLogin);
        tvCambiarContrasena = view.findViewById(R.id.tvCambiarContrasena);


        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    assert account != null;
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    e.printStackTrace();
                }
        }
    }



    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser usuario = auth.getCurrentUser();
                    updateUi(usuario);
                }else {
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    private void updateUi(FirebaseUser usuario){

        GoogleSignInAccount cuenta = GoogleSignIn.getLastSignedInAccount(getActivity());

        Map<String, Object> map = new HashMap<>();

        map.put("nombre", cuenta.getDisplayName());
        map.put("correo", cuenta.getEmail());
        map.put("imagenPerfil", String.valueOf(cuenta.getPhotoUrl()));
        map.put("uid", usuario.getUid());

        FirebaseFirestore.getInstance().collection("usuarios").document(usuario.getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    assert getActivity() != null;
                    progressBar.setVisibility(View.GONE);
                    sendUserToMainActivity();
                } else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}