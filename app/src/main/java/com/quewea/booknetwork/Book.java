package com.quewea.booknetwork;

public class Book {
    private String title;
    private String author;
    private String editorial;
    private String year;
    private String isbn;
    private String language;
    private String pages;
    private String deal;
    private String img;

    public Book () { }

    public Book (String title, String author, String editorial, String year,
                 String isbn, String language, String pages, String deal, String img) {
        this.title = title;
        this.author = author;
        this.editorial = editorial;
        this.year = year;
        this.isbn = isbn;
        this.language = language;
        this.pages = pages;
        this.deal = deal;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
