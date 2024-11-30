package es.ucm.fdi.v3findmyroommate.ui.home;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import es.ucm.fdi.v3findmyroommate.LocaleUtils;
import es.ucm.fdi.v3findmyroommate.R;

public class FiltersFragment extends DialogFragment {

    private Spinner sCategoria, sTipoCasa, sNumHabs, sNumBanos, sOrientacion, sNumComps, sGenero, sTipoBano;
    private ListenerFiltrosAplicados listener;
    private View filterscasa, filtershabitacion;
    private Context application;

    public FiltersFragment (Context a){
        this.application = a;
    }
    public void onStart (){
        super.onStart();
        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setLayout((int) (requireContext().getResources().getDisplayMetrics().widthPixels* 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

    }

    public interface ListenerFiltrosAplicados {
        void filtrosAplicados(String categoria, String tipocasa, String numHabs, String numBanos, String orientacion, String genero, String numComps, String tipoBano );
    }

    public void setListenerFiltrosAplicados (ListenerFiltrosAplicados listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_dialog, container, false);

        filterscasa = view.findViewById(R.id.filtrosCasa);
        filtershabitacion = view.findViewById(R.id.filtrosHabitacion);
        sCategoria = view.findViewById(R.id.spinnerCategorias);
        sTipoCasa = view.findViewById(R.id.spinnerTipoCasas);
        sNumHabs = view.findViewById(R.id.spinnerNumHabs);
        sNumBanos = view.findViewById(R.id.spinnerNumBanos);
        sOrientacion = view.findViewById(R.id.spinnerOrientacion);
        sGenero = view.findViewById(R.id.spinnerGenero);
        sNumComps = view.findViewById(R.id.spinnerNumComps);
        sTipoBano = view.findViewById(R.id.spinnerTipoBano);
        sCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categoriaSel = adapterView.getItemAtPosition(i).toString();

                // Se muestran los filtros dependeindo de la categoría seleccionada

                //if ("Casa".equals(categoriaSel)){
                if (LocaleUtils.doesStringMatchAnyLanguage(application,categoriaSel, R.string.house_property_type_label)){
                        System.out.println("SE HA SELECCIONADO FILTRO CASA");
                        filterscasa.setVisibility(View.VISIBLE);
                        filtershabitacion.setVisibility(View.GONE);
                }else if (LocaleUtils.doesStringMatchAnyLanguage(application,categoriaSel, R.string.room_property_type_label)){
                    System.out.println("SE HA SELECCIONADO FILTRO HABITACION");
                    filtershabitacion.setVisibility(View.VISIBLE);
                    filterscasa.setVisibility(View.GONE);
                }else{
                    filterscasa.setVisibility(View.GONE);
                    filtershabitacion.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button applyfiltersbutton = view.findViewById(R.id.buttonApplyFilters);
        Button cancel = view.findViewById(R.id.buttonCancelFilters);

        applyfiltersbutton.setOnClickListener(v -> {
            String ftipocasa = "";
            String fnumhabs = "";
            String fnumbanos = "";
            String forientacion = "";
            String fgenero = "";
            String fnumComps = "";
            String ftipoBano = "";
            if (listener != null){
                if (sTipoCasa != null){
                    ftipocasa = sTipoCasa.getSelectedItem() != null ? sTipoCasa.getSelectedItem().toString() : "";
                }
                if (sNumHabs != null){
                    fnumhabs = sNumHabs.getSelectedItem() != null ? sNumHabs.getSelectedItem().toString() : "";
                }
                if (sNumBanos != null){
                    fnumbanos = sNumBanos.getSelectedItem() != null ? sNumBanos.getSelectedItem().toString() : "";
                }
                if (sOrientacion != null){
                    forientacion = sOrientacion.getSelectedItem() != null ? sOrientacion.getSelectedItem().toString() : "";
                }
                if (sGenero != null){
                    fgenero = sGenero.getSelectedItem() != null ? sGenero.getSelectedItem().toString() : "";
                }
                if (sNumComps != null){
                    fnumComps = sNumComps.getSelectedItem() != null ? sNumComps.getSelectedItem().toString() : "";
                }
                if (sTipoBano != null){
                    ftipoBano = sTipoBano.getSelectedItem() != null ? sTipoBano.getSelectedItem().toString() : "";
                }

                listener.filtrosAplicados(sCategoria.getSelectedItem().toString(), ftipocasa, fnumhabs,fnumbanos, fnumComps,fgenero, forientacion,ftipoBano);
            }
            dismiss();
        });

        cancel.setOnClickListener(v -> dismiss());

        return view;

    }



}
