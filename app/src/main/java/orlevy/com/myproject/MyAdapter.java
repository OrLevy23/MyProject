package orlevy.com.myproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Or.levy on 05/09/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private List<Note> listData;
    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;
    private static ImageView starred;


    public MyAdapter(List<Note> listData, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;

    }

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

        void onStarredIconClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public void clearData() {
        int size = this.listData.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listData.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void clearItem(int id) {
        this.listData.remove(id);
        this.notifyDataSetChanged();
    }
    public void setStarred(int id) {
        Note temp = this.listData.get(id);
        temp.setStar(!temp.isStarred());
        this.listData.set(id,temp);
        this.notifyDataSetChanged();

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Note note = listData.get(position);
        holder.subject.setText(note.getSubject());
        holder.note.setText(note.getNote());
        if(note.isStarred()) {
            starred.setImageResource(R.drawable.starred);
        } else {
            starred.setImageResource(R.drawable.not_starred);
        }
//        holder.icon.setImageResource(item.getImageResId());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView subject;
        //        private ImageView icon;
        private TextView note;
        private View container;

        public MyHolder(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.subject);
            note = (TextView) itemView.findViewById(R.id.note);
            ImageView icon = (ImageView) itemView.findViewById(R.id.delete_recycler);
            starred = (ImageView)itemView.findViewById(R.id.star_recycler);
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
            icon.setOnClickListener(this);
            starred.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cont_item_root) {
                itemClickCallback.onItemClick(getAdapterPosition());
            } else if (view.getId() == R.id.star_recycler) {
                itemClickCallback.onStarredIconClick(getAdapterPosition());
            } else if (view.getId() == R.id.delete_recycler ){
                itemClickCallback.onSecondaryIconClick(getAdapterPosition());
            }
        }
    }


}
