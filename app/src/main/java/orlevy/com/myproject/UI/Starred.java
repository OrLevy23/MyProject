package orlevy.com.myproject.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import orlevy.com.myproject.Class.Note;
import orlevy.com.myproject.DB.DBHandler;
import orlevy.com.myproject.R;

public class Starred extends AppCompatActivity {
    private static ArrayList<Note> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private DBHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar123);
        mToolBar.setTitle("Toolbar");
        mToolBar.setNavigationIcon(R.drawable.back_toolbar);
        setSupportActionBar(mToolBar);
        // DB
        handler = new DBHandler(Starred.this);
        list = handler.getStarred();
        // RecView
        adapter = new MyAdapter(list, Starred.this);
        recyclerView = (RecyclerView) findViewById(R.id.rec_view_starred);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}
