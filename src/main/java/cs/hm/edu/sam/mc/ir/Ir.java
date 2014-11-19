package cs.hm.edu.sam.mc.ir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import cs.hm.edu.sam.mc.ir.enum_interfaces.ColorEnum;
import cs.hm.edu.sam.mc.ir.enum_interfaces.DynamicGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.GUIInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.StaticGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.TasksEnum;
import cs.hm.edu.sam.mc.ir.main_ground.DynamicIRComponent;
import cs.hm.edu.sam.mc.ir.main_ground.StaticIRComponent;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

/**
 * @author Roland Widmann
 *
 */
public class Ir extends JInternalFrame implements GUIInterface {


	private static final long serialVersionUID = 1L;
	private JTextField txtIRStaticLongitude;
	private JTextField txtIRStaticLatitude;
	private JTextField txtIRDynamicLongitude;
	private JTextField txtIRDynamicLatitude;
	private DefaultListModel<String> listModelConsole = new DefaultListModel<>();
	private JPanel pnlMiddleNorth;
	private JScrollPane scrollPaneConsole;
	private JButton btnOnboardUnit = new JButton("Onboard Unit");
	private JList<String> lstConsole = new JList<String>(listModelConsole);
	
	private  StaticGuiInterface staticIRTask =  new StaticIRComponent(this);
	private  DynamicGuiInterface dynamicIRTask = new DynamicIRComponent(this);


	private final JPanel panel = new JPanel();
	
	
	public void printConsole(String text){
		listModelConsole.addElement(text);
		lstConsole.ensureIndexIsVisible(listModelConsole.size() - 1);
	}
	
	private boolean isEmpty(String txt){
		return txt.equals("");
	}
	
	
	private void waypointOperation(TasksEnum task) {
		double longitude = 0;
		double latitude = 0;
		switch (task){
		case IRSTATIC:
			if(!isEmpty(txtIRStaticLongitude.getText().trim()) && !isEmpty(txtIRStaticLatitude.getText().trim())){
				longitude= Double.parseDouble(txtIRStaticLongitude.getText().trim());
				latitude= Double.parseDouble(txtIRStaticLatitude.getText().trim());
				staticIRTask.calcWaypoints(longitude , latitude);
			}
			break;
		case IRDYNAMIC:
			if(!isEmpty(txtIRDynamicLongitude.getText().trim()) && !isEmpty(txtIRDynamicLatitude.getText().trim())){
				longitude= Double.parseDouble(txtIRDynamicLongitude.getText().trim());
				latitude= Double.parseDouble(txtIRDynamicLatitude.getText().trim());
				dynamicIRTask.calcWaypoints(longitude , latitude);
			}
		}
	}
	
	private void startTask(TasksEnum tastToStart){
		switch (tastToStart){
		case IRSTATIC:
			staticIRTask.startMission();
			break;
		case IRDYNAMIC:
			
		}
	}

	/**
	 * Create the frame.
	 */
	public Ir() {
		
		setTitle("TASK: INFRARED");
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setResizable(true);
       
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
		btnIrStatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startTask(TasksEnum.EMERGENT);
			}
		});
		
		JPanel pnlWest = new JPanel();
		getContentPane().add(pnlWest, BorderLayout.WEST);
		pnlWest.setLayout(new MigLayout("", "[]", "[][][][]"));
		
		//IR - Static
		JPanel panelTitleIRStatic = new JPanel();

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
		
		JPanel pnlNorth = new JPanel();
		getContentPane().add(pnlNorth, BorderLayout.NORTH);
		pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		//Button Dronestatus
		btnOnboardUnit.setHorizontalAlignment(SwingConstants.LEFT);
		btnOnboardUnit.setForeground(Color.BLACK);
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
		
		JScrollPane scrollPane = new JScrollPane();
		pnlMiddleNorth.add(scrollPane, BorderLayout.NORTH);
		
		pnlMiddleNorth.add(scrollPane, BorderLayout.NORTH);
		
		JPanel pnlMiddleSouth = new JPanel();
		pnlMiddle.add(pnlMiddleSouth, BorderLayout.SOUTH);
		pnlMiddleSouth.setLayout(new BoxLayout(pnlMiddleSouth, BoxLayout.X_AXIS));
		
		

		//Console
		scrollPaneConsole = new JScrollPane(lstConsole);		
		lstConsole.setForeground(Color.GREEN);
		lstConsole.setBackground(Color.BLACK);
		pnlMiddle.add(scrollPaneConsole, BorderLayout.CENTER);
	
	}

	@Override
	public void setDroneConnectionColor(ColorEnum setColor) {
		if(setColor == ColorEnum.COLORGREEN)
			btnOnboardUnit.setBackground(Color.GREEN);
		else
			btnOnboardUnit.setBackground(Color.RED);
	}


}
