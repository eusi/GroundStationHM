package cs.hm.edu.sam.mc.emergent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import cs.hm.edu.sam.mc.ir.enum_interfaces.ColorEnum;
import cs.hm.edu.sam.mc.ir.enum_interfaces.EmergentGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.GUIInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.TasksEnum;
import cs.hm.edu.sam.mc.ir.main_ground.EmergentComponent;
import cs.hm.edu.sam.mc.misc.CONSTANTS;
import cs.hm.edu.sam.mc.misc.Location;
import cs.hm.edu.sam.mc.report.ReportSheet;

/**
 * @author Roland Widmann
 *
 */
public class Emergent extends JInternalFrame implements GUIInterface {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtEmergentLongitude; private JTextField txtEmergentLatitude;
	private JTextField txtLongSA;
	private JTextField txtLatSA;
	private DefaultListModel<String> listModel = new DefaultListModel<>();
	private DefaultListModel<String> listModelConsole = new DefaultListModel<>();
	private JList<String> lstSearchAreaWP = new JList<String>(listModel);
	private JList<String> lstConsole = new JList<String>(listModelConsole);
	private JPanel pnlMiddleNorth;
	private JScrollPane scrollPaneConsole;
	private JButton btnOnboardUnit = new JButton("Onboard Unit");
	private  EmergentGuiInterface emergentTask = new EmergentComponent(this);
	private final JPanel panel = new JPanel();
	
	
	public void printConsole(String text){
		listModelConsole.addElement(text);
		lstConsole.ensureIndexIsVisible(listModelConsole.size() - 1);
	}
	
	private void waypointOperation(TasksEnum task) {
		double longitude = 0;
		double latitude = 0;
		switch (task){
		case EMERGENT:
			if(!isEmpty(txtEmergentLongitude.getText().trim()) && !isEmpty(txtEmergentLatitude.getText().trim())){
				longitude= Double.parseDouble(txtEmergentLongitude.getText().trim());
				latitude= Double.parseDouble(txtEmergentLatitude.getText().trim());
				emergentTask.calcWaypoints(longitude , latitude);
			}
		}
	}
	
	private void addWPtoSearchArea() {
		if(!isEmpty(txtLongSA.getText().trim()) && !isEmpty(txtLatSA.getText().trim())){
			double longitude = Double.parseDouble(txtLongSA.getText().trim());
			double latitude = Double.parseDouble(txtLatSA.getText().trim());
			emergentTask.addEmergentSearchAreaWP(longitude, latitude);	
			refreshEmergentSearchAreaList();
			System.out.println(listModel.getSize()-1);
			printConsole("BlaaaaBlubb");
		}
	}
	
	private void startTask(TasksEnum tastToStart){
		switch (tastToStart){
			case EMERGENT:{
			
			}
		}
		/*ToDo:  */

	}
	
	
	private boolean isEmpty(String txt){
		return txt.equals("");
	}
	
	private void refreshEmergentSearchAreaList(){
		List<Location> saList = emergentTask.getEmergentSearchArea();
		listModel.clear();
		for(Location wp : saList){
			listModel.addElement(wp.toString());
		}
		lstSearchAreaWP = new JList<>(listModel);

	}
	

	/**
	 * Create the frame.
	 */
	public Emergent() {
		
		setTitle("TASK: EMERGENT TARGET");
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setResizable(true);
       
		setBounds(10, 10, 830, 649);
        setFrameIcon(new ImageIcon(ReportSheet.class.getResource(CONSTANTS.ICON_DIR
                + "emergent_icon_mini.png")));
		
		JPanel pnlSouth = new JPanel();
		getContentPane().add(pnlSouth, BorderLayout.SOUTH);
		
		JPanel pnlEast = new JPanel();
		getContentPane().add(pnlEast, BorderLayout.EAST);
		pnlEast.setLayout(new BorderLayout(0, 0));
		pnlEast.add(panel, BorderLayout.NORTH);
		
		pnlSouth.setLayout(new MigLayout("", "[][][][][][][][][][][][][][][][]", "[]"));
		
		//Emergent Task Mission start
		JButton btnEmergent = new JButton("Emergent");
		pnlSouth.add(btnEmergent, "cell 10 0");
		
		JPanel pnlWest = new JPanel();
		getContentPane().add(pnlWest, BorderLayout.WEST);
		pnlWest.setLayout(new MigLayout("", "[]", "[][][][]"));
		
		
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
		pnlMiddleSouth.setLayout(new BoxLayout(pnlMiddleSouth, BoxLayout.X_AXIS));
		
		
		
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
