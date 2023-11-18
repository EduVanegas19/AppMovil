package com.exydos.redsocial.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exydos.redsocial.FragmentReplace;
import com.exydos.redsocial.MainActivity;
import com.exydos.redsocial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.ViewOverlayImpl;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends Fragment {

    private EditText txtNombre, txtCorreo, txtContrasena, txtConfirmarContrasena;
    private ProgressBar progressBar;
    private TextView txtIniciarS;
    private Button btnCrearCuenta;
    private FirebaseAuth auth;

    public  static  final String EMAIL_REGEX = "^(.+)@(.+)$";
                                                //"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"

    public CreateAccount() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        clickListener();
    }

    private void clickListener() {
        txtIniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((FragmentReplace) getActivity()).setFragment(new LoginFragment());
            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String nombre = txtNombre.getText().toString();
                String correo = txtCorreo.getText().toString();
                String contrasena = txtContrasena.getText().toString();
                String confirmarContra = txtConfirmarContrasena.getText().toString();

                if(nombre.isEmpty() || nombre.equals(" ")){
                    txtNombre.setError("Por favor ingrese un nombre valido");
                    return;
                }
                if(correo.isEmpty() || !correo.matches(EMAIL_REGEX)){
                    txtCorreo.setError("Por favor ingrese un correo valido");
                    return;
                }
                if(contrasena.isEmpty() || contrasena.length() < 6){
                    txtContrasena.setError("Por favor ingrese un contrasena que contenga mas de 6 digitos");
                    return;
                }
                if(!contrasena.equals(confirmarContra)){
                    txtConfirmarContrasena.setError("Contrasenas no coinciden");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                createAccount(nombre, correo, contrasena);
            }
        });
    }

    private void createAccount(String nombre, String correo, String contrasena){
        auth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser usuario = auth.getCurrentUser();
                    usuario.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Verifica tu correo en el link", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    uploadUser(usuario, nombre, correo);
                }else{
                    progressBar.setVisibility(View.GONE);
                    String exception = task.getException().getMessage();
                    Toast.makeText(getContext(), "Error: "+exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadUser(FirebaseUser usuario, String nombre, String correo){
        Map<String, Object> map = new HashMap<>();

        map.put("nombre", nombre);
        map.put("correo", correo);
        map.put("imagenPerfil", " ");
        map.put("uid", usuario.getUid());

        FirebaseFirestore.getInstance().collection("usuarios").document(usuario.getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    assert getActivity() != null;
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));
                    getActivity().finish();
                } else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void init(View view){
        txtNombre = view.findViewById(R.id.txtNombre);
        txtCorreo = view.findViewById(R.id.txtEmail);
        txtContrasena = view.findViewById(R.id.txtContrasenaCC);
        txtConfirmarContrasena = view.findViewById(R.id.txtConfirmarContrasena);

        txtIniciarS = view.findViewById(R.id.txtIniciarSesion);
        btnCrearCuenta = view.findViewById(R.id.btnCrearCuenta);
        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

    }

}