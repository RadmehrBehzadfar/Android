package com.example.radmehr_bookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.radmehr_bookapp.database.BookDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextAuthor: EditText
    private lateinit var editTextGenre: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextQuantity: EditText
    private lateinit var buttonAddBook: Button
    private lateinit var buttonUpdateBook: Button
    private lateinit var recyclerViewBooks: RecyclerView

    private lateinit var bookAdapter: BookAdapter
    private val bookList = mutableListOf<Book>()

    private var selectedBook: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind Views
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextAuthor = findViewById(R.id.editTextAuthor)
        editTextGenre = findViewById(R.id.editTextGenre)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextQuantity = findViewById(R.id.editTextQuantity)
        buttonAddBook = findViewById(R.id.buttonAddBook)
        buttonUpdateBook = findViewById(R.id.buttonUpdateBook)
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks)

        // Setup RecyclerView
        bookAdapter = BookAdapter(
            bookList,
            onEditClick = { book -> editBook(book) },
            onDeleteClick = { book -> deleteBook(book) },
            onItemClick = { book -> viewBookDetails(book) }
        )
        recyclerViewBooks.layoutManager = LinearLayoutManager(this)
        recyclerViewBooks.adapter = bookAdapter

        // Load existing data
        loadBooks()

        // Add
        buttonAddBook.setOnClickListener {
            addBook()
        }
        // Update
        buttonUpdateBook.setOnClickListener {
            updateBook()
        }
    }

    private fun loadBooks() {
        val bookDao = BookDatabase.getDatabase(this).bookDao()
        lifecycleScope.launch {
            val booksFromDb = bookDao.getAllBooks()
            bookList.clear()
            bookList.addAll(booksFromDb)
            bookAdapter.notifyDataSetChanged()
        }
    }

    private fun addBook() {
        val title = editTextTitle.text.toString().trim()
        val author = editTextAuthor.text.toString().trim()
        val genre = editTextGenre.text.toString().trim()
        val priceText = editTextPrice.text.toString().trim()
        val quantityText = editTextQuantity.text.toString().trim()

        if (title.isEmpty() || author.isEmpty() || genre.isEmpty() ||
            priceText.isEmpty() || quantityText.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toDoubleOrNull()
        val quantity = quantityText.toIntOrNull()
        if (price == null || quantity == null) {
            Toast.makeText(this, "Invalid price or quantity", Toast.LENGTH_SHORT).show()
            return
        }

        val book = Book(title = title, author = author, genre = genre, price = price, quantity = quantity)

        val bookDao = BookDatabase.getDatabase(this).bookDao()
        lifecycleScope.launch {
            bookDao.insertBook(book)
            loadBooks()
        }
        clearForm()
    }

    private fun updateBook() {
        val book = selectedBook ?: run {
            Toast.makeText(this, "No book selected to update", Toast.LENGTH_SHORT).show()
            return
        }

        val title = editTextTitle.text.toString().trim()
        val author = editTextAuthor.text.toString().trim()
        val genre = editTextGenre.text.toString().trim()
        val priceText = editTextPrice.text.toString().trim()
        val quantityText = editTextQuantity.text.toString().trim()

        if (title.isEmpty() || author.isEmpty() || genre.isEmpty() ||
            priceText.isEmpty() || quantityText.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toDoubleOrNull()
        val quantity = quantityText.toIntOrNull()
        if (price == null || quantity == null) {
            Toast.makeText(this, "Invalid price or quantity", Toast.LENGTH_SHORT).show()
            return
        }

        book.title = title
        book.author = author
        book.genre = genre
        book.price = price
        book.quantity = quantity

        val bookDao = BookDatabase.getDatabase(this).bookDao()
        lifecycleScope.launch {
            bookDao.updateBook(book)
            loadBooks()
        }
        clearForm()
        selectedBook = null
        buttonUpdateBook.isEnabled = false
        buttonAddBook.isEnabled = true
    }

    private fun deleteBook(book: Book) {
        val bookDao = BookDatabase.getDatabase(this).bookDao()
        lifecycleScope.launch {
            bookDao.deleteBook(book)
            loadBooks()
        }
    }

    private fun editBook(book: Book) {
        selectedBook = book
        editTextTitle.setText(book.title)
        editTextAuthor.setText(book.author)
        editTextGenre.setText(book.genre)
        editTextPrice.setText(book.price.toString())
        editTextQuantity.setText(book.quantity.toString())
        buttonUpdateBook.isEnabled = true
        buttonAddBook.isEnabled = false
    }

    private fun clearForm() {
        editTextTitle.text.clear()
        editTextAuthor.text.clear()
        editTextGenre.text.clear()
        editTextPrice.text.clear()
        editTextQuantity.text.clear()
    }

    private fun viewBookDetails(book: Book) {
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra("bookId", book.id)
        startActivity(intent)
    }
}