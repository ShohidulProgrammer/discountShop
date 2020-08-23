package alamin.game.discountappofshopers.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.LocationModel;
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
    private List<LocationModel> locationModelList = new ArrayList<>();
    private Activity context;

    public CustomInfoWindowAdapter(Activity context,List<LocationModel> locationModelList){
        this.locationModelList = locationModelList;
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.view_custom_marker, null);
        RelativeLayout custom_marker_view;
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
        custom_marker_view = view.findViewById(R.id.custom_marker_view);

        custom_marker_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked ", Toast.LENGTH_SHORT).show();
            }
        });
        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());

        return view;
    }
}
