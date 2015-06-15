

import java.io.Serializable;
import java.util.Vector;

public class Book implements Serializable {
	private String isbn;
	private String title;
	private String author;
	private int price;
	private String year;
	private String publish;
	private String popul;
	private int stock;
	
	public Book(){
	}
	public Book(String isbn, String title, String author, int price, String year, String publish,String popul,int stock){
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.price = price;
		this.year = year;
		this.publish = publish;
		this.popul = popul;
		this.stock = stock;
	}

	public String getAuthor() {
		return author;
	}

	public String getIsbn() {
		return isbn;
	}

	public int getPrice() {
		return price;
	}

	public String getTitle() {
		return title;
	}
	
	public String getYear(){
		return year;
	}
	
	public String getPublish(){
		return publish;
	}
	
	public String getPopul(){
		return popul;
	}
	
	public int getStock(){
		return stock;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public void setPublish(String publish){
		this.publish = publish;
	}
	
	public void setPopul(String popul){
		this.popul = popul;
	}
	
	public void setStock(int stock){
		this.stock = stock;
	}

	public SellBook getSales(String date,int count){
		SellBook Sales = new SellBook(date,getIsbn(),getTitle(),getPrice(),count);
		return Sales;
	}
	
}
