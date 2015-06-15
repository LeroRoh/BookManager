

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.*;

import java.awt.*;		
import java.awt.event.*;		
import java.net.*;		
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class BookManager extends JFrame{
	private JPanel main = new JPanel();
	private JPanel menuAll = new JPanel();
	static ArrayList<Book> bList = new ArrayList<Book>();
	static Sales sales = null;
	static ArrayList<SellBook> state = new ArrayList();
	static Packit sum = null;
	static JTable mi;
	final JTextField serch = new JTextField(30);
	final JTextField con = new JTextField(10);
	private JButton sell = null;
	private JButton recash = null;
	private JScrollPane jp;
	private ObjectInputStream oi = null;
	private ObjectOutputStream fw = null;
	private Socket sock = null;
	static Vector col;
	static SimpleDateFormat f = new SimpleDateFormat("[a hh시 mm분]", Locale.KOREA);
	
	public BookManager(String ip){
		try{
			sock = new Socket(ip, 10001);
			fw = new ObjectOutputStream(sock.getOutputStream());
			oi = new ObjectInputStream(sock.getInputStream());
			WinInputThread wit = new WinInputThread(sock, oi);	
			wit.start();
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null,ex,"서버와 연결이 올바르지 않습니다.",JOptionPane.ERROR_MESSAGE);//팝업창 처리
			System.exit(1);
		}
		sum = new Packit(bList,sales);
		menuAll.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		setLayout(new BorderLayout(10,0));
		setTitle("도서 판매 클라이언트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		col = new Vector();
        col.add("ISBN");
        col.add("제목");
        col.add("저자");
        col.add("가격");
        col.add("출판년도");
        col.add("출판사");
        col.add("평가");
        col.add("재고");
        
		DefaultTableModel model = new DefaultTableModel(getData(), col) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        mi = new JTable(model);
        JPopupMenu popup = new JPopupMenu();
        jp = new JScrollPane(mi);
        jTableSet();
        add(jp,BorderLayout.CENTER);
		JMenuBar mb = new JMenuBar();
		JMenu menu1 = new JMenu("파일");
		JMenuItem menuOpen = new JMenuItem("파일 추가");
		menuOpen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				File f;
				JFileChooser fc= new JFileChooser();
				int answer = fc.showOpenDialog(null);
				if(answer==JFileChooser.APPROVE_OPTION){
					f=fc.getSelectedFile();
					BufferedReader br;
					try {
						br = new BufferedReader(new FileReader(f.getPath()));
						while(true) {
							String line = br.readLine();
							if (line==null) break;
							String[] sp=line.split("#");
							int codeNum = bList.size()+1;
							String code = String.format("a%04d", codeNum);
							bList.add(new Book(code,sp[0],sp[1],Integer.parseInt(sp[2]),sp[3],sp[4],sp[5],Integer.parseInt(sp[6])));
						}
						br.close();
						jTableRefresh();
						fw.writeObject(sum);
			            fw.reset();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		JMenuItem menuExit = new JMenuItem("종료");
		menuExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null, "종료합니다", "종료경고", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		});
		menu1.add(menuOpen);
		menu1.add(menuExit);
		JMenu menu2 = new JMenu("편집");
		JMenuItem menuAdd = new JMenuItem("추가");
		menuAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String[] items = {"★", "★★", "★★★", "★★★★", "★★★★★"};
					int codeNum = bList.size()+1;
					String code = String.format("a%04d", codeNum);
					JComboBox combo = new JComboBox(items);
					JTextField field1 = new JTextField(code);
					JTextField field2 = new JTextField();
					JTextField field3 = new JTextField();
					NumberField field4 = new NumberField();
					NumberField field5 = new NumberField();
					JTextField field6 = new JTextField();
					NumberField field7 = new NumberField();
					field1.setEditable(false);
					field5.setDocument(new JTextFieldLimit(4));
					JPanel panel = new JPanel(new GridLayout(0, 1));
					panel.add(new JLabel("ISBN:"));
					panel.add(field1);
					panel.add(new JLabel("제목:"));
					panel.add(field2);
					panel.add(new JLabel("저자:"));
					panel.add(field3);
					panel.add(new JLabel("가격:"));
					panel.add(field4);
					panel.add(new JLabel("출판년도:"));
					panel.add(field5);
					panel.add(new JLabel("출판사:"));
					panel.add(field6);
					panel.add(new JLabel("평가:"));
					panel.add(combo);
					panel.add(new JLabel("재고:"));
					panel.add(field7);
					int result = JOptionPane.showConfirmDialog(null, panel, "추가",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						bList.add(new Book(field1.getText(),field2.getText(),field3.getText(),Integer.parseInt(field4.getText()),field5.getText(),field6.getText(),(String)combo.getSelectedItem(),Integer.parseInt(field7.getText())));
						jTableRefresh();
						fw.writeObject(sum);
			            fw.reset();
						JOptionPane.showMessageDialog(null, "추가되었습니다.", "추가 완료", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "취소되었습니다.", "추가 취소", JOptionPane.INFORMATION_MESSAGE);
					}
				}catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "내용이 부족합니다.", "입력 불충분", JOptionPane.WARNING_MESSAGE);
					// TODO Auto-generated catch block
				}
			}
		});
		JMenuItem menuModify = new JMenuItem("수정");
		menuModify.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String[] items = {"★", "★★", "★★★", "★★★★", "★★★★★"};
					JComboBox combo = new JComboBox(items);
					JTextField field1 = new JTextField();
					JTextField field2 = new JTextField();
					JTextField field3 = new JTextField();
					NumberField field4 = new NumberField();
					NumberField field5 = new NumberField();
					JTextField field6 = new JTextField();
					field1.setEditable(false);
					field2.setEditable(false);
					field5.setDocument(new JTextFieldLimit(4));
					NumberField field7 = new NumberField();
					JPanel panel = new JPanel(new GridLayout(0, 1));
					panel.add(new JLabel("ISBN:"));
					panel.add(field1);
					panel.add(new JLabel("제목:"));
					panel.add(field2);
					panel.add(new JLabel("저자:"));
					panel.add(field3);
					panel.add(new JLabel("가격:"));
					panel.add(field4);
					panel.add(new JLabel("출판년도:"));
					panel.add(field5);
					panel.add(new JLabel("출판사:"));
					panel.add(field6);
					panel.add(new JLabel("평가:"));
					panel.add(combo);
					panel.add(new JLabel("재고:"));
					panel.add(field7);
					JTextField field = new JTextField();
					JPanel panel2 = new JPanel(new GridLayout(0,1));
					panel2.add(new JLabel("수정할 서적의 ISBN 이나 이름을 입력하세요"));
					panel2.add(field);
					boolean chk = true;
					int result1 = JOptionPane.showConfirmDialog(null, panel2, "수정",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(result1==JOptionPane.OK_OPTION){
						String[] sList = new String[2];
						for(int i=0;i<bList.size();i++){
							sList[0]=bList.get(i).getIsbn();
							sList[1]=bList.get(i).getTitle();
							for(int j=0;j<sList.length;j++){
								if(sList[j].equals(field.getText())){
									field1.setText(bList.get(i).getIsbn());
									field2.setText(bList.get(i).getTitle());
									field3.setText(bList.get(i).getAuthor());
									field4.setText(String.valueOf(bList.get(i).getPrice()));
									field5.setText(bList.get(i).getYear());
									field6.setText(bList.get(i).getPublish());
									combo.setSelectedIndex(bList.get(i).getPopul().length()-1);
									field7.setText(String.valueOf(bList.get(i).getStock()));
									int result2 = JOptionPane.showConfirmDialog(null, panel, "수정",
											JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
									if (result2 == JOptionPane.OK_OPTION) {
										bList.get(i).setAuthor(field3.getText());
										bList.get(i).setPrice(Integer.parseInt(field4.getText()));
										bList.get(i).setYear(field5.getText());
										bList.get(i).setPublish(field6.getText());
										bList.get(i).setPopul((String)combo.getSelectedItem());
										bList.get(i).setStock(Integer.parseInt(field7.getText()));
										jTableRefresh();
										fw.writeObject(sum);
							            fw.reset();
										JOptionPane.showMessageDialog(null, "수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
									} else {
										JOptionPane.showMessageDialog(null, "취소되었습니다.", "수정 취소", JOptionPane.INFORMATION_MESSAGE);
									}
									chk=false;
								}
							}
						}
						
					}
					else{
						JOptionPane.showMessageDialog(null, "취소되었습니다.", "수정 취소", JOptionPane.INFORMATION_MESSAGE);
						chk=false;
					}
					if(chk){
						JOptionPane.showMessageDialog(null, "입력된 서적이 존재하지 않습니다.", "서적 찾기 실패", JOptionPane.WARNING_MESSAGE);
					}
				}catch (Exception ex) {
					// TODO Auto-generated catch block
				}
			}
		});
		JMenuItem menuDelete = new JMenuItem("삭제");
		menuDelete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JTextField field = new JTextField();
					JPanel panel = new JPanel(new GridLayout(0,1));
					panel.add(new JLabel("삭제할 서적의 ISBN 이나 이름을 입력하세요"));
					panel.add(field);
					boolean chk = true;
					int result1 = JOptionPane.showConfirmDialog(null, panel, "삭제",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(result1==JOptionPane.OK_OPTION){
						String[] sList = new String[2];
						for(int i=0;i<bList.size();i++){
							sList[0]=bList.get(i).getIsbn();
							sList[1]=bList.get(i).getTitle();
							for(int j=0;j<sList.length;j++){
								if(sList[j].equals(field.getText())){
									bList.remove(i);
									chk=false;
									jTableRefresh();
									fw.writeObject(sum);
						            fw.reset();
									JOptionPane.showMessageDialog(null, "삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
								}
							}
						}
						
					}
					else{
						JOptionPane.showMessageDialog(null, "취소되었습니다.", "삭제 취소", JOptionPane.INFORMATION_MESSAGE);
						chk=false;
					}
					if(chk){
						JOptionPane.showMessageDialog(null, "입력된 서적이 존재하지 않습니다.", "서적 찾기 실패", JOptionPane.WARNING_MESSAGE);
					}
				}catch (Exception ex) {
					// TODO Auto-generated catch block
				}
			}
		});
		JMenuItem pMenuModify = new JMenuItem("수정");
		pMenuModify.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = mi.getSelectedRow();
					String[] items = {"★", "★★", "★★★", "★★★★", "★★★★★"};
					JComboBox combo = new JComboBox(items);
					JTextField field1 = new JTextField();
					JTextField field2 = new JTextField();
					JTextField field3 = new JTextField();
					NumberField field4 = new NumberField();
					NumberField field5 = new NumberField();
					JTextField field6 = new JTextField();
					field1.setEditable(false);
					field2.setEditable(false);
					field5.setDocument(new JTextFieldLimit(4));
					NumberField field7 = new NumberField();
					JPanel panel = new JPanel(new GridLayout(0, 1));
					panel.add(new JLabel("ISBN:"));
					panel.add(field1);
					panel.add(new JLabel("제목:"));
					panel.add(field2);
					panel.add(new JLabel("저자:"));
					panel.add(field3);
					panel.add(new JLabel("가격:"));
					panel.add(field4);
					panel.add(new JLabel("출판년도:"));
					panel.add(field5);
					panel.add(new JLabel("출판사:"));
					panel.add(field6);
					panel.add(new JLabel("평가:"));
					panel.add(combo);
					panel.add(new JLabel("재고:"));
					panel.add(field7);
					String ISBN;
					for(int i=0;i<bList.size();i++){
						ISBN=bList.get(i).getIsbn();
						if(ISBN.equals(mi.getValueAt(row,0))){
							field1.setText(bList.get(i).getIsbn());
							field2.setText(bList.get(i).getTitle());
							field3.setText(bList.get(i).getAuthor());
							field4.setText(String.valueOf(bList.get(i).getPrice()));
							field5.setText(bList.get(i).getYear());
							field6.setText(bList.get(i).getPublish());
							combo.setSelectedIndex(bList.get(i).getPopul().length()-1);
							field7.setText(String.valueOf(bList.get(i).getStock()));
							int result2 = JOptionPane.showConfirmDialog(null, panel, "수정",
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
							if (result2 == JOptionPane.OK_OPTION) {
								bList.get(i).setAuthor(field3.getText());
								bList.get(i).setPrice(Integer.parseInt(field4.getText()));
								bList.get(i).setYear(field5.getText());
								bList.get(i).setPublish(field6.getText());
								bList.get(i).setPopul((String)combo.getSelectedItem());
								bList.get(i).setStock(Integer.parseInt(field7.getText()));
								jTableRefresh();
								fw.writeObject(sum);
							    fw.reset();
								JOptionPane.showMessageDialog(null, "수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "취소되었습니다.", "수정 취소", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
						
				}catch (Exception ex) {
					// TODO Auto-generated catch block
				}
			}
		});
		JMenuItem pMenuDelete = new JMenuItem("삭제");
		pMenuDelete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = mi.getSelectedRow();
					String ISBN;
					int result1 = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "삭제",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(result1==JOptionPane.OK_OPTION){
						for(int i=0;i<bList.size();i++){
							ISBN=bList.get(i).getIsbn();
							if(ISBN.equals(mi.getValueAt(row,0))){
								bList.remove(i);
								jTableRefresh();
								fw.writeObject(sum);
						        fw.reset();
						        JOptionPane.showMessageDialog(null, "삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}	
					else JOptionPane.showMessageDialog(null, "취소되었습니다.", "삭제 취소", JOptionPane.INFORMATION_MESSAGE);
				}catch (Exception ex) {
					// TODO Auto-generated catch block
				}
			}
		});
		menu2.add(menuAdd);
		menu2.add(menuModify);
		menu2.add(menuDelete);
		popup.add(pMenuModify);
		popup.add(pMenuDelete);
		mi.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseReleased(MouseEvent e) {
	            	if (e.getClickCount() == 2) {
	            		try {
	    					int row = mi.getSelectedRow();
	    					NumberField field = new NumberField();
	    					field.setText("1");
	    					String ISBN;
	    					JPanel panel = new JPanel(new GridLayout(0,1));
	    					panel.add(new JLabel("판매할 수량을 입력하세요"));
	    					panel.add(field);
	    					int result1 = JOptionPane.showConfirmDialog(null, panel, "판매",
	    							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    					if(result1==JOptionPane.OK_OPTION){
	    						for(int i=0;i<bList.size();i++){
	    							ISBN=bList.get(i).getIsbn();
	    							if(ISBN.equals(mi.getValueAt(row,0))){
	    								int count = Integer.parseInt(field.getText());
	    								if(count<=bList.get(i).getStock()){
	    									bList.get(i).setStock(bList.get(i).getStock()-count);
	    									state.add(new SellBook(getDate(),bList.get(i).getIsbn(),bList.get(i).getTitle(),bList.get(i).getPrice(),count));
	    									sales.setResult(sales.getResult()+bList.get(i).getPrice()*count);
	    									sales.setsList(state);
	    									jTableRefresh();
	    									fw.writeObject(sum);
	    									fw.reset();
	    									JOptionPane.showMessageDialog(null, "판매되었습니다.", "판매 완료", JOptionPane.INFORMATION_MESSAGE);
	    								}
	    								else JOptionPane.showMessageDialog(null, "재고가 부족합니다.", "재고 부족", JOptionPane.WARNING_MESSAGE);
	    							}
	    						}
	    					}	
	    					else JOptionPane.showMessageDialog(null, "취소되었습니다.", "판매 취소", JOptionPane.INFORMATION_MESSAGE);
	    				}catch (Exception ex) {
	    					// TODO Auto-generated catch block
	    				}
	            	}
	                int r = mi.rowAtPoint(e.getPoint());
	                if (r >= 0 && r < mi.getRowCount()) {
	                    mi.setRowSelectionInterval(r, r);
	                } else {
	                    mi.clearSelection();
	                }

	                int rowindex = mi.getSelectedRow();
	                if (rowindex < 0)
	                    return;
	                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
	                    popup.show(e.getComponent(), e.getX(), e.getY());
	                }
	            }
	        });
		mb.add(menu1);
		mb.add(menu2);
		menuAll.add(mb);
		menuAll.add(new JLabel("Serch"));
		menuAll.add(serch);
		serch.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			public void keyReleased(KeyEvent e) {
				String[] sList = new String[7];
				Vector sdata = new Vector();
				for(int i=0;i<bList.size();i++){
					sList[0]=bList.get(i).getIsbn();
					sList[1]=bList.get(i).getTitle();
					sList[2]=bList.get(i).getAuthor();
					sList[3]=String.valueOf(bList.get(i).getPrice());
					sList[4]=bList.get(i).getYear();
					sList[5]=bList.get(i).getPublish();
					sList[6]=bList.get(i).getPopul();
					for(int j=0;j<sList.length;j++){
						if(sList[j].indexOf(serch.getText())!=-1){
							Vector row = new Vector();
							row.add(bList.get(i).getIsbn());
							row.add(bList.get(i).getTitle());
							row.add(bList.get(i).getAuthor());
							row.add(bList.get(i).getPrice());
							row.add(bList.get(i).getYear());
							row.add(bList.get(i).getPublish());
							row.add(bList.get(i).getPopul());
							if(bList.get(i).getStock()==0) row.add("매진");
							else row.add(bList.get(i).getStock());
							sdata.add(row);
							break;
						}
					}
				}
				jTableRefresh(sdata);
			}

			public void keyTyped(KeyEvent e) {
				// Get the current character you typed...
			}
		});
		menuAll.add(sell = new JButton("판매"));
		sell.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JTextField field = new JTextField();
					NumberField field2 = new NumberField();
					JPanel panel = new JPanel(new GridLayout(0,1));
					panel.add(new JLabel("판매할 서적의 ISBN 이나 이름을 입력하세요"));
					panel.add(field);
					panel.add(new JLabel("판매할 수량을 입력하세요."));
					panel.add(field2);
					field2.setText("1");
					boolean chk = true;
					int result1 = JOptionPane.showConfirmDialog(null, panel, "판매",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(result1==JOptionPane.OK_OPTION){
						String[] sList = new String[2];
						for(int i=0;i<bList.size();i++){
							sList[0]=bList.get(i).getIsbn();
							sList[1]=bList.get(i).getTitle();
							for(int j=0;j<sList.length;j++){
								if(sList[j].equals(field.getText())){
									int count = Integer.parseInt(field2.getText());
									chk=false;
									if(count<=bList.get(i).getStock()){
										bList.get(i).setStock(bList.get(i).getStock()-count);
    									state.add(new SellBook(getDate(),bList.get(i).getIsbn(),bList.get(i).getTitle(),bList.get(i).getPrice(),count));
    									sales.setResult(sales.getResult()+bList.get(i).getPrice()*count);
    									sales.setsList(state);
										jTableRefresh();
										fw.writeObject(sum);
										fw.reset();
										JOptionPane.showMessageDialog(null, "판매되었습니다.", "판매 완료", JOptionPane.INFORMATION_MESSAGE);
									}
									else JOptionPane.showMessageDialog(null, "재고가 부족합니다.", "재고 부족", JOptionPane.WARNING_MESSAGE);
								}
							}
						}
						
					}
					else{
						JOptionPane.showMessageDialog(null, "취소되었습니다.", "삭제 취소", JOptionPane.INFORMATION_MESSAGE);
						chk=false;
					}
					if(chk){
						JOptionPane.showMessageDialog(null, "입력된 서적이 존재하지 않습니다.", "서적 찾기 실패", JOptionPane.WARNING_MESSAGE);
					}
				}catch (Exception ex) {
					// TODO Auto-generated catch block	
				}
			}
		});
		menuAll.add(recash = new JButton("환불"));
		recash.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JTextField field = new JTextField();
					NumberField field2 = new NumberField();
					JPanel panel = new JPanel(new GridLayout(0,1));
					panel.add(new JLabel("환불할 서적의 ISBN 이나 이름을 입력하세요"));
					panel.add(field);
					panel.add(new JLabel("환불할 수량을 입력하세요."));
					panel.add(field2);
					field2.setText("1");
					boolean chk = true;
					JOptionPane.showMessageDialog(null, "당일자 환불만 가능합니다", "환불", JOptionPane.WARNING_MESSAGE);
					int result1 = JOptionPane.showConfirmDialog(null, panel, "환불",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(result1==JOptionPane.OK_OPTION){
						String[] sList1 = new String[2];
						String[] sList2 = new String[2];
						int count = Integer.parseInt(field2.getText());
						for(int i=0;i<state.size();i++){
							sList1[0]=state.get(i).getIsbn();
							sList1[1]=state.get(i).getTitle();
							for(int j=0;j<sList1.length;j++){
								if(sList1[j].equals(field.getText())&&state.get(i).getCount()==count&&chk){
									chk=false;
    								state.get(i).setCount(0);
    								sales.setResult(sales.getResult()-state.get(i).getPrice()*count);
    								//sales.setsList(state);
    								for(int k=0;k<bList.size();k++){
    									sList2[0]=bList.get(k).getIsbn();
    									sList2[1]=bList.get(k).getTitle();
    									if(sList2[j].equals(field.getText())){
    										bList.get(k).setStock(bList.get(k).getStock()+count);
    									}
    								}
    								jTableRefresh();
									fw.writeObject(sum);
									fw.reset();
									JOptionPane.showMessageDialog(null, "환불되었습니다.", "환불 완료", JOptionPane.INFORMATION_MESSAGE);
								}
							}
						}
						
					}
					else{
						JOptionPane.showMessageDialog(null, "취소되었습니다.", "삭제 취소", JOptionPane.INFORMATION_MESSAGE);
						chk=false;
					}
					if(chk){
						JOptionPane.showMessageDialog(null, "환불 정보가 없습니다.", "환불 정보 오류", JOptionPane.WARNING_MESSAGE);
					}
				}catch (Exception ex) {
					// TODO Auto-generated catch block
				}
			}
		});
		menuAll.add(new JLabel("매출"));
		con.setEditable(false);
		menuAll.add(con);
		add(menuAll,BorderLayout.NORTH);
		setSize(800, 600);
		setVisible(true);
		setResizable(false);
		addWindowListener(new WindowAdapter(){	
			public void windowClosing(WindowEvent e){
				try{	
					sock.close();
				}catch(Exception ex){}	
				JOptionPane.showMessageDialog(null, "종료합니다", "종료경고", JOptionPane.WARNING_MESSAGE);//팝업창 처리	
				System.exit(0);	
			}
		});
	}
	
	static String getDate(){
		String today = f.format(new Date());//현재 시간 설정
		if(today.substring(1,3).equalsIgnoreCase("PM"))today.replaceAll("PM", "오후");
		else today.replaceAll("AM", "오전");//AM,PM 한글화
		return today;
	}
	
	static Vector getData(){
		Vector data = new Vector();
		Iterator iter = bList.iterator();
		while(iter.hasNext()){
			Book cList=(Book)iter.next();
			Vector row = new Vector();
			row.add(cList.getIsbn());
			row.add(cList.getTitle());
			row.add(cList.getAuthor());
			row.add(cList.getPrice());
			row.add(cList.getYear());
			row.add(cList.getPublish());
			row.add(cList.getPopul());
			if(cList.getStock()==0) row.add("매진");
			else row.add(cList.getStock());
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
        mi.setModel(model);
        jTableSet();
    }
	static void jTableRefresh(Vector sList) {
        DefaultTableModel model = new DefaultTableModel(sList, col) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        mi.setModel(model);
        jTableSet();
    }
	
	static void jTableSet() {
        mi.getTableHeader().setReorderingAllowed(false);
        mi.getTableHeader().setResizingAllowed(false);
        mi.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
         
        DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
        celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer celAlignRight = new DefaultTableCellRenderer();
        celAlignRight.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer celAlignLeft = new DefaultTableCellRenderer();
        celAlignLeft.setHorizontalAlignment(JLabel.LEFT);
        
        mi.getColumn("ISBN").setPreferredWidth(10);
        mi.getColumn("ISBN").setCellRenderer(celAlignCenter);
        mi.getColumn("제목").setPreferredWidth(150);
        mi.getColumn("제목").setCellRenderer(celAlignLeft);
        mi.getColumn("저자").setPreferredWidth(50);
        mi.getColumn("저자").setCellRenderer(celAlignCenter);
        mi.getColumn("가격").setPreferredWidth(10);
        mi.getColumn("가격").setCellRenderer(celAlignRight);
        mi.getColumn("출판년도").setPreferredWidth(10);
        mi.getColumn("출판년도").setCellRenderer(celAlignCenter);
        mi.getColumn("출판사").setPreferredWidth(30);
        mi.getColumn("출판사").setCellRenderer(celAlignCenter);
        mi.getColumn("평가").setPreferredWidth(10);
        mi.getColumn("평가").setCellRenderer(celAlignCenter);
        mi.getColumn("재고").setPreferredWidth(5);
        mi.getColumn("재고").setCellRenderer(celAlignCenter);
    }
	
	public class NumberField extends JTextField implements KeyListener {
		private static final long serialVersionUID = 1;
		
		public NumberField() {
			addKeyListener(this);
		}
		
		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
			// Get the current character you typed...
			char c = e.getKeyChar();

			if (!Character.isDigit(c)) {
				e.consume();
				return;
			}
		}
	}
	
	
	public class JTextFieldLimit extends PlainDocument {
		private int limit;
		public JTextFieldLimit(int limit){
			super();
			this.limit=limit;
		}
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException{
			if(str == null) return;
			if(getLength()+str.length()<=limit){
				super.insertString(offset, str,  attr);
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 1){
			JOptionPane.showMessageDialog(null, "사용법 : java ChatClient ip", "경고", JOptionPane.WARNING_MESSAGE);
			System.exit(1);	
		}	
		new BookManager(args[0]);
	}
	class WinInputThread extends Thread{		
		private Socket sock = null;			
		private ObjectInputStream oi = null;			
		public WinInputThread(Socket sock, ObjectInputStream oi){			
			this.sock = sock;		
			this.oi = oi;
		}			
		public void run(){			
			try{
				Object line = null;
				while((line = oi.readObject()) != null){
			       	sum=(Packit)line;
			       	bList=(ArrayList)sum.getbList();
			       	sales=(Sales)sum.getSales();
			       	state=(ArrayList)sales.getsList();
			       	jTableRefresh();
			       	con.setText(String.valueOf(sales.getResult()));
				}
			}catch(Exception ex){
			}finally{
				try{
					if(oi != null)
						oi.close();
				}catch(Exception ex){
				}		
				try{		
					if(sock != null)	
						sock.close();
				}catch(Exception ex){}
			}			
		}				
	}
}
