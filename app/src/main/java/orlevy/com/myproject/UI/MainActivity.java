package orlevy.com.myproject.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

import orlevy.com.myproject.Adapter.MyAdapter;
import orlevy.com.myproject.Adapter.SimpleItemTouchHelperCallback;
import orlevy.com.myproject.Class.Note;
import orlevy.com.myproject.DB.DBHandler;
import orlevy.com.myproject.R;


public class MainActivity extends AppCompatActivity {
    private static ArrayList<Note> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private DBHandler handler;
    private static ImageView emptyNotebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        emptyNotebook = (ImageView) findViewById(R.id.empty_notebook);
        // DB
        handler = new DBHandler(MainActivity.this);
        list = handler.getAllNotes();
        checkIfEmpty();
        // RecView
        adapter = new MyAdapter(list, MainActivity.this) {
            @Override
            public void onItemDismiss(int position) {
                handler.archiveRecord(list.get(position).getId());
                adapter.archiveItem(position);
                notifyItemRemoved(position);
                Snackbar.make(findViewById(R.id.Coordinator), "Item was archived", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                checkIfEmpty();

            }

            public void onItemMove(int fromPosition, int toPosition) {
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(list, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(list, i, i - 1);
                    }
                }
                notifyItemMoved(fromPosition, toPosition);
            }

        };


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        //Add button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(MainActivity.this, AddNote.class);
                startActivity(add);
            }
        });
        // Edit
        adapter.setItemClickCallback(new MyAdapter.ItemClickCallback() {
            @Override
            public void onItemClick(int p) {
                Intent edit = new Intent(MainActivity.this, AddNote.class);
                edit.putExtra("id",list.get(p).getId());
                startActivity(edit);
            }
            @Override
            public void onStarredIconClick(int p) {
                Note noteToStar = list.get(p);
                int id = noteToStar.getId();
                handler.star(id,noteToStar);
                adapter.setStarred(p);
            }

        });

    }

    private void checkIfEmpty() {
        if(!list.isEmpty()) {
            emptyNotebook.setVisibility(View.GONE);
        } else {
            emptyNotebook.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_archive_all) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Are you sure?");
            builder.setMessage("Everything will be deleted");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.archiveAll();
                    list.clear();
                    list = handler.getAllNotes();
                    adapter.clearData();
                    adapter.notifyDataSetChanged();
                    checkIfEmpty();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();

        } else if(id == R.id.action_starred){
                Intent starred = new Intent(MainActivity.this,Starred.class);
                startActivity(starred);
        } else if(id == R.id.action_archived) {
            Intent archived = new Intent(MainActivity.this,Archived.class);
            startActivity(archived);
        }

        return true;
    }


}
//TODO: MOST IMPORTANT DESIGN THIS FUCKING SHIT!!!!