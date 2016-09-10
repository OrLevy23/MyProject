package orlevy.com.myproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<Note> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private DBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        // DB
        handler = new DBHandler(MainActivity.this);
        list = handler.getAllNotes();
        // RecView
        adapter = new MyAdapter(list, MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Everything will be deleted");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.removeAll();
                        list.clear();
                        list = handler.getAllNotes();
                        adapter.clearData();
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });


        // Floating button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this, AddNote.class);
                startActivity(a);
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

            //Delete
            @Override
            public void onSecondaryIconClick(final int p) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Item will be deleted");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = list.get(p).getId();
                        handler.deleteRecord(id);
                        adapter.clearItem(p);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
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
        if (id == R.id.action_delete_all) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Are you sure?");
            builder.setMessage("Everything will be deleted");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.removeAll();
                    list.clear();
                    list = handler.getAllNotes();
                    adapter.clearData();
                    adapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.show();
        } else if(id == R.id.action_add_note) {
            Intent a = new Intent(MainActivity.this, AddNote.class);
            startActivity(a);
        } else if(id == R.id.action_starred){
            list = handler.getStarred();
            adapter.notifyDataSetChanged();
        }


        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
//TODO: MOST IMPORTANT DESIGN THIS FUCKING SHIT!!!!