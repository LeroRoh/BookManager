import java.io.Serializable;
import java.util.ArrayList;

public class Sales implements Serializable{
	private String date;
	private int result;
	private ArrayList<SellBook> sList; 
	
	public Sales(String date, int result, ArrayList<SellBook> sList){
		this.date=date;
		this.result=result;
		this.sList=sList;
	}
	
	public String getDate(){
		return date;
	}
	public int getResult(){
		return result;
	}
	public ArrayList<SellBook> getsList(){
		return sList;
	}
	
	public void setDate(String date){
		this.date=date;
	}
	public void setResult(int result){
		this.result = result;
	}
	public void setsList(ArrayList<SellBook> sList){
		this.sList=sList;
	}
}
