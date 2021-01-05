package com.quewea.booknetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Adapter extends FirestoreRecyclerAdapter<Book, Adapter.ViewHolder> {
    private OnItemClickListener listener;
    private LayoutInflater layoutInflater;
    //private List<Book> data;
    //private View.OnClickListener listener;

    public Adapter(Context context, @NonNull FirestoreRecyclerOptions<Book> data){
        super(data);
        this.layoutInflater = LayoutInflater.from(context);
//        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.tarjeta, viewGroup, false);
        //view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Book book) {
        //Book book = data.get(position);
        holder.txtTitle.setText(book.getTitle());
        holder.txtAuthor.setText(book.getAuthor());
        holder.txtEditorial.setText(book.getEditorial());
        holder.txtYear.setText(book.getYear());
        holder.txtIsbn.setText(book.getIsbn());
        holder.txtLanguage.setText(book.getLanguage());
        holder.txtPages.setText(book.getPages());
        holder.txtDeal.setText(book.getDeal());

    }

    //@Override
    //public int getItemCount() {
    //    return data.size();
    //}

    /*public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtAuthor, txtEditorial, txtYear, txtIsbn, txtLanguage, txtPages, txtDeal;
        ImageView imgBook;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitulo);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtEditorial = itemView.findViewById(R.id.txtEditorial);
            txtYear = itemView.findViewById(R.id.txtYear);
            txtIsbn = itemView.findViewById(R.id.txtISBN);
            txtLanguage = itemView.findViewById(R.id.txtLanguage);
            txtPages = itemView.findViewById(R.id.txtPages);
            txtDeal = itemView.findViewById(R.id.txtDeal);
            imgBook = itemView.findViewById(R.id.imgBook);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
