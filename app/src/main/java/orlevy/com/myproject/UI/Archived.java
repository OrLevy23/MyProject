package orlevy.com.myproject.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;

import orlevy.com.myproject.Class.Note;
import orlevy.com.myproject.DB.DBHandler;
import orlevy.com.myproject.R;

public class Archived extends AppCompatActivity {
    private static ArrayList<Note> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private DBHandler handler;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived);
        // DB
        handler = new DBHandler(Archived.this);
        list = handler.getArchived();
        // RecView
        adapter = new MyAdapter(list, Archived.this);
        recyclerView = (RecyclerView) findViewById(R.id.rec_view_archived);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setItemClickCallback(new MyAdapter.ItemClickCallback() {
            @Override
            public void onItemClick(int p) {
                Intent edit = new Intent(Archived.this, AddNote.class);
                edit.putExtra("id",list.get(p).getId());
                edit.putExtra("archived",true);
                startActivity(edit);
            }

            @Override
            public void onSecondaryIconClick(int p) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Archived.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Note will be deleted");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.removeAll();
                        adapter.clearData();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }

            @Override
            public void onStarredIconClick(int p) {

            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_archive,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_back) {
            finish();
        } else if(id == R.id.action_delete_all) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Archived.this);
            builder.setTitle("Are you sure?");
            builder.setMessage("Everything will be deleted");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.removeAll();
                    adapter.clearData();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();


        }
        return true;
    }

}
