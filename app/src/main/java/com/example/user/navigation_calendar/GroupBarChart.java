package com.example.user.navigation_calendar;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;

import java.util.ArrayList;
import java.util.List;

public class GroupBarChart extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    ImageButton back;
    private SeekBar seekBar;
    private TextView textView;
    private StringBuffer stringBuffer;

    private RecyclerView recyclerView;
    private BCmemberAdapter adapter;
    private List<BCMemberCard> trans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_bar_chart);

        back=findViewById(R.id.bc_back);
        back.setOnClickListener(this);

        recyclerView = findViewById(R.id.bc_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        trans = new ArrayList<>();
        adapter = new BCmemberAdapter(trans);
        recyclerView.setAdapter(adapter);


        AnyChartView anyChartView = findViewById(R.id.Bar_anychart_view);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));


        //groupName -->bar
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> Gdata = new ArrayList<>();
        Gdata.add(new ValueDataEntry("Rouge", 80540));
        Gdata.add(new ValueDataEntry("Foundation", 94190));
        Gdata.add(new ValueDataEntry("Mascara", 102610));
        Gdata.add(new ValueDataEntry("Lip gloss", 110430));
        Gdata.add(new ValueDataEntry("Lipstick", 128000));

        cartesian.column(Gdata);
        //Column column = cartesian.column(data);
        anyChartView.setChart(cartesian);

        /*
        //user-->pie
        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));

        pie.setData(data);
        anyChartView.setChart(pie);
        */

        seekBar=(SeekBar)findViewById(R.id.seekBar);
        //textView=(TextView)findViewById(R.id.tv);
        seekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bc_back:
                finish();
                break;
        }
    }

    @Override
    //seelbar值變化時觸發
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        stringBuffer.append("正在拖动"+progress+"\n");
        textView.setText(stringBuffer);
    }

    @Override
    //seekbar開始拖動時觸發
    public void onStartTrackingTouch(SeekBar seekBar) {
        stringBuffer=new StringBuffer();
        stringBuffer.append("开始拖动+\n");
        textView.setText(stringBuffer);
    }

    @Override
    //seekbar停止拖動時觸發
    public void onStopTrackingTouch(SeekBar seekBar) {
        stringBuffer.append("停止拖动+\n");
        textView.setText(stringBuffer);
    }
}

//JSON-->data
class BCMemberCard {

    private String username;

    public BCMemberCard(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String title) {
        this.username= username;
    }

}

class BCmemberAdapter extends RecyclerView.Adapter<BCmemberAdapter.ViewHolder> {
    private List<BCMemberCard> data;

    public BCmemberAdapter(List<BCMemberCard> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;

        public ViewHolder(View v) {
            super(v);
            username = v.findViewById(R.id.BC_username);
        }
    }

    @Override
    public BCmemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.barchart_member_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //holder.username.setText(data.get(position).getUsername());
        holder.username.setText("Isabel");
        holder.username.setText("Leah");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}