package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ADMIN on 5/31/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View ListItemView= convertView;
        if(ListItemView==null)
        {
            ListItemView= LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent,false);
        }
        Earthquake currentQuake= getItem(position);

        TextView magView=(TextView)ListItemView.findViewById(R.id.magnitude);
        magView.setText(String.valueOf(currentQuake.getMagnitude()));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentQuake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        String location= currentQuake.getLocation();

        String[] lcn={ "Near the", location};

        if(location.contains(" of "))
        {
              lcn= location.split( " of ");
        }



        TextView locationView1=(TextView)ListItemView.findViewById(R.id.location1);
        locationView1.setText(lcn[0]);

        TextView locationView2=(TextView)ListItemView.findViewById(R.id.location2);
        locationView2.setText(lcn[1]);

        String date=currentQuake.getDate();
        String Date[]= date.split(" ");

        TextView dateView1=(TextView)ListItemView.findViewById(R.id.date);
        dateView1.setText(Date[0]);

        TextView dateView2=(TextView)ListItemView.findViewById(R.id.time);
        dateView2.setText(Date[1]);

        return ListItemView;

    }

    private int getMagnitudeColor(double magnitude) {

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

}
