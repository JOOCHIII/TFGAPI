package com.example.logins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_config);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<OpcionConfig> lista = Arrays.asList(
                new OpcionConfig("Perfil", R.drawable.persona),
                new OpcionConfig("Notificaciones", R.drawable.baseline_notifications_24),
                new OpcionConfig("Apariencia", R.drawable.baseline_brightness_4_24),
                new OpcionConfig("Eliminar cuenta", R.drawable.baseline_delete_24)
        );

        ConfigAdapter adapter = new ConfigAdapter(lista, position -> {
            switch (position) {
                case 0:
                    // abrir perfil
                    Fragment perfilFragment = new Fragment_profile_config();
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, perfilFragment); // Asegúrate de que este ID es el de tu contenedor de fragmentos
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case 1:
                    // abrir configuración de notificaciones
                    break;
                case 2:
                    // abrir ajustes de apariencia (esto ya lo manejamos con el Switch)
                    break;
                case 3:
                    // confirmar y eliminar cuenta
                    break;
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}
