package com.example.radmehr_bookapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.radmehr_bookapp.database.BookDatabase
import kotlinx.coroutines.launch

class BookDetailActivity : AppCompatActivity() {

    private lateinit var textViewDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        textViewDetails = findViewById(R.id.textViewBookDetails)
        val bookDao = BookDatabase.getDatabase(this).bookDao()

        val bookId = intent.getIntExtra("bookId", -1)
        if (bookId != -1) {
            lifecycleScope.launch {
                val books = bookDao.getAllBooks()
                val book = books.find { it.id == bookId }
                if (book != null) {
                    textViewDetails.text = """
                        Title: ${book.title}
                        Author: ${book.author}
                        Genre: ${book.genre}
                        Price: \$${book.price}
                        Quantity: ${book.quantity}
                    """.trimIndent()
                } else {
                    textViewDetails.text = "Book not found."
                }
            }
        } else {
            textViewDetails.text = "Invalid book ID."
        }
    }
}