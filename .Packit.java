import java.io.Serializable;
import java.util.ArrayList;


public class Packit implements Serializable{
	private ArrayList<Book> bList = new ArrayList();
	private ArrayList<Sales> sList = null;
	private Sales sales = null;
	public Packit(ArrayList<Book> bList, ArrayList<Sales> sList){
		this.bList =bList;
		this.sList = new ArrayList();
		this.sList =sList;
	}
	public Packit(ArrayList<Book> bList, Sales sales){
		this.bList = bList;
		this.sales = sales;
	}
	
	public ArrayList getbList(){
		return bList;
	}
	
	public ArrayList getsList(){
		return sList;
	}
	public Sales getSales(){
		return sales;
	}
}
