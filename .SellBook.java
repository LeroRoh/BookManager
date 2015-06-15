import java.io.Serializable;



public class SellBook extends Book implements Serializable{
	private String date;
	private String isbn;
	private String title;
	private int price;
	private int count=0;
	
	public SellBook(){
	}
	
	public SellBook(String date,String isbn ,String title,int price, int count){
		this.date=date;
		this.isbn=isbn;
		this.title=title;
		this.price=price;
		this.count=count;
	}
	
	public String getDate() {
		return date;
	}

	public String getIsbn() {
		return isbn;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getCount() {
		return count;
	}

	public void setDate(String date) {
		this.date=date;
	}

	public void setIsbn(String isbn) {
		this.isbn=isbn;
	}
	
	public void setTitle(String title) {
		this.title=title;
	}
	
	public void setPrice(int price) {
		this.price=price;
	}
	
	public void setCount(int count) {
		this.count=count;
	}
	public String toString(){
		return getDate()+getIsbn()+getTitle()+getPrice()+getCount();
	}
}
