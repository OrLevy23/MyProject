package orlevy.com.myproject.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import orlevy.com.myproject.Adapter.MyAdapter;
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
        // DB
        handler = new DBHandler(Starred.this);
        list = handler.getStarred();
        // RecView
        adapter = new MyAdapter(list, Starred.this);
        recyclerView = (RecyclerView) findViewById(R.id.rec_view_starred);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setItemClickCallback(new MyAdapter.ItemClickCallback() {
            @Override
            public void onItemClick(int p) {
                Intent edit = new Intent(Starred.this,AddNote.class);
                edit.putExtra("id",list.get(p).getId());
                edit.putExtra("fromStarred",true);
                startActivity(edit);
            }


            @Override
            public void onStarredIconClick(int p) {
                Note noteToStar = list.get(p);
                int id = noteToStar.getId();
                handler.star(id,noteToStar);
                adapter.removeStare(p);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_starred,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_back_to_notes_from_starred) {
            Intent back = new Intent(Starred.this,MainActivity.class);
            startActivity(back);
        } else if(id == R.id.action_add_note_starred) {
            Intent addStarredNote = new Intent(Starred.this,AddNote.class);
            addStarredNote.putExtra("fromStarred", true);
            startActivity(addStarredNote);
        } else if(id == R.id.action_archive_all_starred){

        } else if(id == R.id.action_archived_from_starred) {
            Intent archived = new Intent(Starred.this,Archived.class);
            startActivity(archived);
        }
        return true;
    }
}
