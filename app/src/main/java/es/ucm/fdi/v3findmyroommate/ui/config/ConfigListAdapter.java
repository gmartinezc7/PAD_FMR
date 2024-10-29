package es.ucm.fdi.v3findmyroommate.ui.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigListAdapter extends ArrayAdapter<ConfigListItem> {

    public ConfigListAdapter(Context context, List<ConfigListItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        ConfigListItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.config_options_list, parent, false);
        }

        // Lookup view for data population
        TextView titleTextView = convertView.findViewById(R.id.option_title);
        TextView subtitleTextView = convertView.findViewById(R.id.option_subtitle);

        // Populate the data into the template view using the data object
        titleTextView.setText(item.getTitle());
        subtitleTextView.setText(item.getSubtitle());

        // Return the completed view to render on screen
        return convertView;
    }
}