package cs.hm.edu.sam.mc.ir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import cs.hm.edu.sam.mc.ir.enum_interfaces.DynamicGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.EmergentGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.GUIInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.StaticGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.TasksEnum;
import cs.hm.edu.sam.mc.ir.main_ground.DynamicIRComponent;
import cs.hm.edu.sam.mc.ir.main_ground.EmergentComponent;
import cs.hm.edu.sam.mc.ir.main_ground.StaticIRComponent;
import cs.hm.edu.sam.mc.misc.Location;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Ir extends JInternalFrame implements GUIInterface {

	private JTextField txtIRStaticLongitude;
	private JTextField txtIRStaticLatitude;
	private JTextField txtIRDynamicLongitude;
	private JTextField txtIRDynamicLatitude;
	private JTextField txtEmergentLongitude;
	private JTextField txtEmergentLatitude;
	private JTextField txtLongSA;
	private JTextField txtLatSA;
	private DefaultListModel<String> listModel = new DefaultListModel<>();
	private DefaultListModel<String> listModelConsole = new DefaultListModel<>();
	private JList lstSearchAreaWP = new JList(listModel);
	private JList lstConsole = new JList(listModelConsole);
	private JPanel pnlMiddleNorth;
	private JScrollPane scrollPaneConsole;
	
	private  StaticGuiInterface staticIRTask =  new StaticIRComponent();
	private  DynamicGuiInterface dynamicIRTask = new DynamicIRComponent();
	private  EmergentGuiInterface emergentTask = new EmergentComponent();
	private final JPanel panel = new JPanel();
	

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					JFrame frameG = new JFrame();
//					
//					Ir frame = new GUI();
//					frame.setVisible(true);
//					frameG.getContentPane().add(frame);
//					frameG.setVisible(true);
//					frameG.pack();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	public void printConsole(String text){
		listModelConsole.addElement(text);
		lstConsole.ensureIndexIsVisible(listModelConsole.size() - 1);
	}
	
	private void waypointOperation(TasksEnum task) {
		double longitude = 0;
		double latitude = 0;
		switch (task){
		case IRSTATIC:
			longitude= Double.parseDouble(txtIRStaticLongitude.getText().trim());
			latitude= Double.parseDouble(txtIRStaticLatitude.getText().trim());
			staticIRTask.calcWaypoints(longitude , latitude);
			break;
		case IRDYNAMIC:
			longitude= Double.parseDouble(txtIRDynamicLongitude.getText().trim());
			latitude= Double.parseDouble(txtIRDynamicLatitude.getText().trim());
			dynamicIRTask.calcWaypoints(longitude , latitude);
			break;
		case EMERGENT:
			longitude= Double.parseDouble(txtEmergentLongitude.getText().trim());
			latitude= Double.parseDouble(txtEmergentLatitude.getText().trim());
			emergentTask.calcWaypoints(longitude , latitude);
		}
	}
	
	private void addWPtoSearchArea() {
		double longitude = Double.parseDouble(txtLongSA.getText().trim());
		double latitude = Double.parseDouble(txtLatSA.getText().trim());
		emergentTask.addEmergentSearchAreaWP(longitude, latitude);	
		refreshEmergentSearchAreaList();
	    printConsole("blaaaaaaaaaaaaaaaaa");
	}
	
	private void startTask(TasksEnum tastToStart){
		switch (tastToStart){
		case IRSTATIC:
			staticIRTask.startMission();
			break;
		case IRDYNAMIC:
			
			break;
		case EMERGENT:
			
		}
	}
	
	
	private void refreshEmergentSearchAreaList(){
		List<Location> saList = emergentTask.getEmergentSearchArea();
		
		listModel.clear();
		for(Location wp : saList){
			listModel.addElement(wp.toString());
		}
		lstSearchAreaWP = new JList<>(listModel);
		lstSearchAreaWP.ensureIndexIsVisible(listModel.getSize()-1);
	}
	

	/**
	 * Create the frame.
	 */
	public Ir() {
		
		setTitle("IR AND EMERGENT TASKS");
        setClosable(true);
        setResizable(true);
        
 
        lstSearchAreaWP.setAutoscrolls(true);
        
		setBounds(100, 100, 830, 649);
		
		JPanel pnlSouth = new JPanel();
		getContentPane().add(pnlSouth, BorderLayout.SOUTH);
		
		JPanel pnlEast = new JPanel();
		getContentPane().add(pnlEast, BorderLayout.EAST);
		pnlEast.setLayout(new BorderLayout(0, 0));
		pnlEast.add(panel, BorderLayout.NORTH);
		
		pnlSouth.setLayout(new MigLayout("", "[][][][][][][][][][][][][][][][]", "[]"));
		
		//Static Missio start
		JButton btnIrStatic = new JButton("IR Static");
		btnIrStatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startTask(TasksEnum.IRSTATIC);
			}
		});
		pnlSouth.add(btnIrStatic, "cell 0 0");
		
		//Dynamic Mission start
		JButton btnIrDynamic = new JButton("IR Dynamic");
		btnIrStatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startTask(TasksEnum.IRDYNAMIC);
			}
		});
		pnlSouth.add(btnIrDynamic, "cell 9 0");
		
		//Emergent Task Mission start
		JButton btnEmergent = new JButton("Emergent");
		btnIrStatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startTask(TasksEnum.EMERGENT);
			}
		});
		pnlSouth.add(btnEmergent, "cell 15 0");
		
		JPanel pnlWest = new JPanel();
		getContentPane().add(pnlWest, BorderLayout.WEST);
		pnlWest.setLayout(new MigLayout("", "[]", "[][][][]"));
		
		//IR - Static
		JPanel panelTitleIRStatic = new JPanel();
		panelTitleIRStatic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("Frame Static Click");
				pnlMiddleNorth.setVisible(false);
			}
		});
		panelTitleIRStatic.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "IR - Static", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlWest.add(panelTitleIRStatic, "cell 0 0,grow");
		panelTitleIRStatic.setLayout(new MigLayout("", "[][]", "[][]"));
		
		JLabel lblIRStaticLongitude = new JLabel("Longitude:");
		panelTitleIRStatic.add(lblIRStaticLongitude, "cell 0 0");
		
		txtIRStaticLongitude = new JTextField();
		panelTitleIRStatic.add(txtIRStaticLongitude, "cell 1 0");
		txtIRStaticLongitude.setColumns(10);
		
		JLabel lblIRStaticLatitude = new JLabel("Latitude:");
		panelTitleIRStatic.add(lblIRStaticLatitude, "cell 0 1");
		
		txtIRStaticLatitude = new JTextField();
		panelTitleIRStatic.add(txtIRStaticLatitude, "cell 1 1");
		txtIRStaticLatitude.setColumns(10);
		
		
		JButton btnRStaticCalculate = new JButton("Calculate Waypoints");
		//ActionListener IRSTATIC
		btnRStaticCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				waypointOperation(TasksEnum.IRSTATIC);
			}


		});
		panelTitleIRStatic.add(btnRStaticCalculate,"cell 1 2");
		
		
		//IR - Dynamic
		JPanel panelTitleIRDynamic = new JPanel();
		panelTitleIRDynamic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("Frame Dynamic Click");
				pnlMiddleNorth.setVisible(false);
			}
		});
		panelTitleIRDynamic.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "IR - Dynamic", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlWest.add(panelTitleIRDynamic, "cell 0 1,grow");
		panelTitleIRDynamic.setLayout(new MigLayout("", "[][]", "[][]"));
		
		JLabel lblIRDynamicLongitude = new JLabel("Longitude:");
		panelTitleIRDynamic.add(lblIRDynamicLongitude, "cell 0 0");
		
		txtIRDynamicLongitude = new JTextField();
		panelTitleIRDynamic.add(txtIRDynamicLongitude, "cell 1 0");
		txtIRDynamicLongitude.setColumns(10);
		
		JLabel lblIRDynamicLatitude = new JLabel("Latitude:");
		panelTitleIRDynamic.add(lblIRDynamicLatitude, "cell 0 1");
		
		txtIRDynamicLatitude = new JTextField();
		panelTitleIRDynamic.add(txtIRDynamicLatitude, "cell 1 1");
		txtIRDynamicLatitude.setColumns(10);
		
		
		JButton btnIRDynamicCalculate = new JButton("Calculate Waypoints");
		//ActionListener IRDYNAMIC
		btnIRDynamicCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					waypointOperation(TasksEnum.IRDYNAMIC);
			}
		});
		panelTitleIRDynamic.add(btnIRDynamicCalculate,"cell 1 2");
		
		
		//Emergent
		JPanel panelTitleEmergent = new JPanel();
		panelTitleEmergent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("Frame Emergent Click");
				pnlMiddleNorth.setVisible(true);
			}
		});
		panelTitleEmergent.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Emergent", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlWest.add(panelTitleEmergent, "cell 0 2,grow");
		panelTitleEmergent.setLayout(new MigLayout("", "[][]", "[][]"));
		
		JLabel lblEmergentLongitude = new JLabel("Longitude:");
		panelTitleEmergent.add(lblEmergentLongitude, "cell 0 0");
		
		txtEmergentLongitude = new JTextField();
		panelTitleEmergent.add(txtEmergentLongitude, "cell 1 0");
		txtEmergentLongitude.setColumns(10);
		
		JLabel lblEmergentLatitude = new JLabel("Latitude:");
		panelTitleEmergent.add(lblEmergentLatitude, "cell 0 1");
		
		txtEmergentLatitude = new JTextField();
		panelTitleEmergent.add(txtEmergentLatitude, "cell 1 1");
		txtEmergentLatitude.setColumns(10);
		
		JButton btnEmergentCalculate = new JButton("Calculate Waypoints");
		//ActionListener CALCEMERGENT
		btnEmergentCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				waypointOperation(TasksEnum.EMERGENT);
			}
		});
		panelTitleEmergent.add(btnEmergentCalculate,"cell 1 2");
		
		JPanel pnlNorth = new JPanel();
		getContentPane().add(pnlNorth, BorderLayout.NORTH);
		pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnOnboardUnit = new JButton("Onboard Unit");
		btnOnboardUnit.setHorizontalAlignment(SwingConstants.LEFT);
		btnOnboardUnit.setForeground(Color.RED);
		btnOnboardUnit.setBackground(Color.RED);
		pnlNorth.add(btnOnboardUnit);
		
		JPanel pnlMiddle = new JPanel();
		pnlMiddle.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		getContentPane().add(pnlMiddle, BorderLayout.CENTER);
		pnlMiddle.setLayout(new BorderLayout(0, 0));
		
		pnlMiddleNorth = new JPanel();
		pnlMiddle.add(pnlMiddleNorth, BorderLayout.NORTH);
		pnlMiddleNorth.setLayout(new BorderLayout(0, 0));
		
		
		
		
		//pnlListButtonSearchArea.add(lstSearchAreaWP, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(lstSearchAreaWP);
		pnlMiddleNorth.add(scrollPane, BorderLayout.NORTH);
		
		JPanel pnlLongLatSA = new JPanel();
		scrollPane.setColumnHeaderView(pnlLongLatSA);
		pnlLongLatSA.setBorder(new TitledBorder(null, "Edit SearchArea waypoints", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblLongSA = new JLabel("Long:");
		pnlLongLatSA.add(lblLongSA);
		
		txtLongSA = new JTextField();
		pnlLongLatSA.add(txtLongSA);
		txtLongSA.setColumns(10);
		
		JLabel lblLatSA = new JLabel("Lat:");
		pnlLongLatSA.add(lblLatSA);
		
		txtLatSA = new JTextField();
		pnlLongLatSA.add(txtLatSA);
		txtLatSA.setColumns(10);
		
		JButton btnAddSA = new JButton("add");
		//Add waypoint to SearchArea list
		btnAddSA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addWPtoSearchArea();
			}
		});
		pnlLongLatSA.add(btnAddSA);
		
		pnlMiddleNorth.add(scrollPane, BorderLayout.NORTH);
		
		JPanel pnlMiddleSouth = new JPanel();
		pnlMiddle.add(pnlMiddleSouth, BorderLayout.SOUTH);
		
		
		
		scrollPaneConsole = new JScrollPane(lstConsole);
		pnlMiddleSouth.add(scrollPaneConsole);
//		pnlMiddleSouth.add(lstConsole);
	
		
		
	}


}
