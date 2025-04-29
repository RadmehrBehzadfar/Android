package com.example.radmehr_bookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val books: MutableList<Book>,
    private val onEditClick: (Book) -> Unit,
    private val onDeleteClick: (Book) -> Unit,
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBookTitle: TextView = itemView.findViewById(R.id.textViewBookTitle)
        val textViewBookAuthor: TextView = itemView.findViewById(R.id.textViewBookAuthor)
        val textViewBookGenre: TextView = itemView.findViewById(R.id.textViewBookGenre)
        val textViewBookPrice: TextView = itemView.findViewById(R.id.textViewBookPrice)
        val textViewBookQuantity: TextView = itemView.findViewById(R.id.textViewBookQuantity)
        val buttonEditBook: ImageButton = itemView.findViewById(R.id.buttonEditBook)
        val buttonDeleteBook: ImageButton = itemView.findViewById(R.id.buttonDeleteBook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.textViewBookTitle.text = book.title
        holder.textViewBookAuthor.text = "Author: ${book.author}"
        holder.textViewBookGenre.text = "Genre: ${book.genre}"
        holder.textViewBookPrice.text = "Price: \$${book.price}"
        holder.textViewBookQuantity.text = "Qty: ${book.quantity}"

        holder.buttonEditBook.setOnClickListener { onEditClick(book) }
        holder.buttonDeleteBook.setOnClickListener { onDeleteClick(book) }
        holder.itemView.setOnClickListener { onItemClick(book) }
    }

    override fun getItemCount(): Int = books.size
}