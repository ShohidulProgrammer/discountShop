package alamin.game.discountappofshopers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.ReportAndHelpModel;

public class ReportAndHelpAdapter extends RecyclerView.Adapter<ReportAndHelpAdapter.ReportAndHelpListHolder>{
    private Context context;
    private ArrayList<ReportAndHelpModel> reportAndHelpAdapterArrayList = new ArrayList<>();
    private ReportAndHelpModel reportAndHelpModel;

    public ReportAndHelpAdapter(Context context, ArrayList<ReportAndHelpModel> reportAndHelpAdapterArrayList) {
        this.context = context;
        this.reportAndHelpAdapterArrayList = reportAndHelpAdapterArrayList;

    }

    @NonNull
    @Override
    public ReportAndHelpListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_and_help_list, parent,false);
        return new ReportAndHelpListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAndHelpListHolder holder, int position) {
        reportAndHelpModel = reportAndHelpAdapterArrayList.get(position);

        holder.tv_report_and_help_mag.setText(reportAndHelpModel.getMessage());
        holder.tv_report_and_help_date.setText(reportAndHelpModel.getDate());
        holder.tv_report_and_help_time.setText(reportAndHelpModel.getTime());
    }

    @Override
    public int getItemCount() {
        return reportAndHelpAdapterArrayList.size();
    }

    class ReportAndHelpListHolder extends RecyclerView.ViewHolder {
        TextView tv_report_and_help_mag,tv_report_and_help_date,tv_report_and_help_time;

        public ReportAndHelpListHolder(@NonNull View itemView) {
            super(itemView);
            tv_report_and_help_mag = itemView.findViewById(R.id.tv_report_and_help_mag);
            tv_report_and_help_date = itemView.findViewById(R.id.tv_report_and_help_date);
            tv_report_and_help_time = itemView.findViewById(R.id.tv_report_and_help_time);
        }
    }
}
