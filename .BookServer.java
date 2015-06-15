
import java.net.*;				
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;				
import java.text.SimpleDateFormat;
import java.util.*;				

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BookServer extends Frame {
	static TextArea display = null;
	private CardLayout cardLayout = null;
	private ObjectInputStream oi = null;
	private ObjectOutputStream fw = null;
	private Socket sock = null;
	private JScrollPane jp;
	static JTable listdis = null;
	static ArrayList<Book> bList = null;
	static ArrayList<Sales> sList = null;
	static Packit sum = null;
	static ArrayList<SellBook> state = new ArrayList();
	static Sales sales = null;
	static SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd", Locale.KOREA);
	static String today = f.format(new Date());//현재 시간 설정
	static Vector col;
		
	public BookServer(){
		super("도서 정보 서버");
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		Panel main = new Panel();
		GridBagConstraints f = new GridBagConstraints();
		main.setLayout(new GridBagLayout());
		col = new Vector();
        col.add("시간");
        col.add("ISBN");
        col.add("제목");
        col.add("가격");
        col.add("개수");
        DefaultTableModel model = new DefaultTableModel(getData(), col) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        listdis = new JTable(model);
        jTableSet();
        jp = new JScrollPane(listdis);
        f.fill=GridBagConstraints.BOTH;
        f.ipadx=8;
		f.ipady=8;
		f.gridwidth=6;
		f.gridheight=2;
		f.weightx=2;
		f.weighty=1;
		main.add(jp, f);
		display = new TextArea(10,50);
		display.setEditable(false);
		f.fill=GridBagConstraints.VERTICAL;
		f.gridwidth=2;
		f.gridheight=1;
		f.weightx=0;
		f.weighty=1;
		main.add(display, f);
		add("main",main);
		setSize(1000, 600);
		setResizable(false);
		addWindowListener(new WindowAdapter(){	
			public void windowClosing(WindowEvent e){
				try{	
					sock.close();
				}catch(Exception ex){}	
				JOptionPane.showMessageDialog(null, "종료합니다", "종료경고", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		});
		setVisible(true);
	}
	
	static Vector getData(){
		Vector data = new Vector();
		Iterator iter = state.iterator();
		while(iter.hasNext()){
			SellBook cList=(SellBook)iter.next();
			Vector row = new Vector();
			row.add(cList.getDate());
			row.add(cList.getIsbn());
			row.add(cList.getTitle());
			row.add(cList.getPrice());
			if(cList.getCount()==0) row.add("환불됨");
			else row.add(cList.getCount());
			data.add(row);
		}
		return data;
	}
	
	static void jTableRefresh() {
        DefaultTableModel model = new DefaultTableModel(getData(), col) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        listdis.setModel(model);
        jTableSet();
    }
	
	static void jTableSet() {
        listdis.getTableHeader().setReorderingAllowed(false);
        listdis.getTableHeader().setResizingAllowed(false);
        listdis.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
         
        DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
        celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer celAlignRight = new DefaultTableCellRenderer();
        celAlignRight.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer celAlignLeft = new DefaultTableCellRenderer();
        celAlignLeft.setHorizontalAlignment(JLabel.LEFT);
        
        listdis.getColumn("시간").setPreferredWidth(50);
        listdis.getColumn("시간").setCellRenderer(celAlignCenter);
        listdis.getColumn("ISBN").setPreferredWidth(10);
        listdis.getColumn("ISBN").setCellRenderer(celAlignCenter);
        listdis.getColumn("제목").setPreferredWidth(150);
        listdis.getColumn("제목").setCellRenderer(celAlignLeft);
        listdis.getColumn("가격").setPreferredWidth(10);
        listdis.getColumn("가격").setCellRenderer(celAlignRight);
        listdis.getColumn("개수").setPreferredWidth(5);
        listdis.getColumn("개수").setCellRenderer(celAlignCenter);
    }
	
	public static void main(String[] args) {			
		try{
			new BookServer();
			ServerSocket server = new ServerSocket(10001);
			display.append(getTime() + " 서버가 준비되었습니다.\n");
			HashMap hm = new HashMap();
			bList = new ArrayList<Book>();
			sList = new ArrayList<Sales>();
			sales = new Sales(today,0,state);
			sum = new Packit(bList,sales);
			state = new ArrayList();
			int num = 0;
			loading();
			while(true){
				display.append(getTime()+" 접속을 기다립니다.\n");
				Socket sock = server.accept();
				display.append(getTime() + sock.getInetAddress() + " 로부터 연결요청이 들어왔습니다.\n");
				loading();
				BookThread Bookthread = new BookThread(sock, hm, bList,sList,sales,sum,state,num, listdis);
				Bookthread.start();
				num++;
			} // while
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e, "종료경고", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}	
	} // main
	
	static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }
	
	static void loading(){
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try{
			fin = new FileInputStream("Booklist.dat");
			ois = new ObjectInputStream(fin);
			
			sum = (Packit)ois.readObject();
			bList = (ArrayList)sum.getbList();
			sList = (ArrayList)sum.getsList();
			if(sList.get(sList.size()-1).getDate().equalsIgnoreCase(today)){
				sales = sList.get(sList.size()-1);
				state = sList.get(sList.size()-1).getsList();
			}
			else sList.add(sales = new Sales(today,0,state));
			jTableRefresh();
			
		}catch(Exception ex){
		}finally{
			try{
				ois.close();
				fin.close();
			}catch(IOException ioe){}
		}
	}
}

class BookThread extends Thread{
	private Socket sock;
	private ObjectInputStream oi;
	private ObjectOutputStream fw;
	private HashMap hm;
	private ArrayList<Book> bList;
	private ArrayList<Sales> sList;
	private Packit sum = null;
	private ArrayList<SellBook> state =null;
	private Sales sales = null;
	private boolean initFlag = false;
	private int num;
	private JTable listdis;
	private Vector col;
	public BookThread(Socket sock, HashMap hm, ArrayList<Book> bList,ArrayList<Sales> sList ,Sales sales,Packit sum,ArrayList<SellBook> state,int num,JTable listdis){		
		this.sock = sock;	
		this.hm = hm;
		this.bList = bList;
		this.sList = sList;
		this.sales = sales;
		this.sum = sum;
		this.state = state;
		this.num=num;
		this.listdis=listdis;
		
		try{
			fw = new ObjectOutputStream(sock.getOutputStream());	
			oi = new ObjectInputStream(sock.getInputStream());
			sum=new Packit(bList,sales);
			synchronized(hm){
				hm.put(num,fw);
				fw.writeObject(sum);
	            fw.reset();
			}
			col = new Vector();
	        col.add("시간");
	        col.add("ISBN");
	        col.add("제목");
	        col.add("가격");
	        col.add("개수");
			initFlag = true;
		}catch(Exception ex){
			System.out.println(ex);
		}
	} // 생성자
	public void run(){			
		try{
			Object line = null;
			while((line = oi.readObject()) != null){
				sum=(Packit)line;
				bList=(ArrayList)sum.getbList();
				sales=(Sales)sum.getSales();
				state=(ArrayList)sales.getsList();
				Save();
				broadcast();
				jTableRefresh();
			}
		}catch(Exception ex){
			synchronized(hm){		
				hm.remove(num);
			}
			try{
				if(sock != null)	
					sock.close();
			}catch(Exception e){}
		}finally{			
			synchronized(hm){		
				hm.remove(num);
			}
			try{
				if(sock != null)	
					sock.close();
			}catch(Exception ex){}
		}
	} // run
	
	public void broadcast(){
		synchronized(hm){
			Collection collection = hm.values();
			Iterator iter = collection.iterator();
			while(iter.hasNext()){	
				ObjectOutputStream fw = (ObjectOutputStream)iter.next();
				try{
					fw.writeObject(sum);
		            fw.reset();
				}
				catch(Exception ex){
					System.out.println(ex);
				}
			}
		}
	} // broacast
	public void Save(){
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try{
			sList.get(sList.size()-1).setDate(sales.getDate());
			sList.get(sList.size()-1).setResult(sales.getResult());
			sList.get(sList.size()-1).setsList(sales.getsList());
			Packit sum = new Packit(bList,sList);
			fout = new FileOutputStream("Booklist.dat");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(sum);
			oos.reset();
		}catch(Exception ex){
		}finally{
			try{
				oos.close();
				fout.close();
			}catch(IOException ioe){}
		}
	}// save
	public Vector getData(){
		Vector data = new Vector();
		Iterator iter = state.iterator();
		while(iter.hasNext()){
			SellBook cList=(SellBook)iter.next();
			Vector row = new Vector();
			row.add(cList.getDate());
			row.add(cList.getIsbn());
			row.add(cList.getTitle());
			row.add(cList.getPrice());
			if(cList.getCount()==0) row.add("환불됨");
			else row.add(cList.getCount());
			data.add(row);
		}
		return data;
	}
	
	public void jTableRefresh() {
        DefaultTableModel model = new DefaultTableModel(getData(), col) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        listdis.setModel(model);
        jTableSet();
    }
	
	public void jTableSet() {
        listdis.getTableHeader().setReorderingAllowed(false);
        listdis.getTableHeader().setResizingAllowed(false);
        listdis.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
         
        DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
        celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer celAlignRight = new DefaultTableCellRenderer();
        celAlignRight.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer celAlignLeft = new DefaultTableCellRenderer();
        celAlignLeft.setHorizontalAlignment(JLabel.LEFT);
        
        listdis.getColumn("시간").setPreferredWidth(50);
        listdis.getColumn("시간").setCellRenderer(celAlignCenter);
        listdis.getColumn("ISBN").setPreferredWidth(10);
        listdis.getColumn("ISBN").setCellRenderer(celAlignCenter);
        listdis.getColumn("제목").setPreferredWidth(150);
        listdis.getColumn("제목").setCellRenderer(celAlignLeft);
        listdis.getColumn("가격").setPreferredWidth(10);
        listdis.getColumn("가격").setCellRenderer(celAlignRight);
        listdis.getColumn("개수").setPreferredWidth(5);
        listdis.getColumn("개수").setCellRenderer(celAlignCenter);
    }
}				

