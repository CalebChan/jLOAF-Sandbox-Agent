package oracle.lfo;

import oracle.lfo.expert.LfOFixedSequenceTest;
import oracle.lfo.expert.LfOSmartExplorerTest;
import oracle.lfo.expert.LfOSmartRandomTest;
import oracle.lfo.expert.LfOSmartStraightLineTest;
import oracle.lfo.expert.LfOZigZagTest;

import org.jLOAF.retrieve.kNNUtil;
import org.jLOAF.retrieve.sequence.weight.WeightFunction;
import org.jLOAF.retrieve.sequence.weight.DecayWeightFunction;
import org.jLOAF.retrieve.sequence.weight.FixedWeightFunction;
import org.jLOAF.retrieve.sequence.weight.GaussianWeightFunction;
import org.jLOAF.retrieve.sequence.weight.LinearWeightFunction;

import util.ParameterList;
import util.ParameterNameEnum;

// GUI imports: 




import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class LFOTestRunner implements ItemListener, ActionListener, PropertyChangeListener, ListSelectionListener{

	private ParameterList list;
	
	private Integer[] intKValues; //will turn TestConfiguration.K_VALUES from an int to an Integer for the jComboBox
	// GUI Components: 
	private JFrame frame;
	private JCheckBox randKNN;
	private JComboBox<Integer> diffKValue;
	private JComboBox<String> reasoningType;
	private JComboBox<String> agentType;
	private JComboBox<String> weightFunction;
	private JFormattedTextField/* traceFolder, exportRunFolder,*/ maxRuns, iterNum;
	private JTable table;	// selected weight function values will be transfered into this table
	private JList<Integer> diffKValueList; //when testing multiple configurations, JLists allow for multiple selections while JComboBoxes do not
	private JList<String> reasoningTypeList;
	private JList<String> agentTypeList;
	private JList<String> weightFunctionList;
	
	private JButton singleTest, multiTest, start, findTraceFolder, findExportRunFolder;
	private JFileChooser fileChooser;
	private String traceFolderLocation, exportRunFolderLocation;
	
	private JLabel diffKValueLabel, reasoningTypeLabel, iterNumLabel, agentTypeLabel, weightFunctionLabel,	// labels above most components
			traceFolderLabel, exportRunFolderLabel, maxRunsLabel;									// to indicate configuration type
	
	private String[] columnHeaders = {"Weight Function", "Parameter 1", "Parameter 2"};		// headers for the selected weight function table
	
	private JScrollPane scroll1, scroll2, scroll3, scroll4, scroll5;	//scrollers for the JLists and the JTable
	
	private tableModel tableModel;
	
	private boolean randomKNN = false; 	// determines if kNN is random or set
	private boolean multiTests = false;	// determines if there is a single test or multiple
	
	// Different weight function types:
	private String[] weights = {"Decay Weight Function"
						,"Fixed Weight Function"
						,"Gaussian Weight Function"
						,"Linear Weight Function"
//						,"Time Varying Function"
						};
	
	private String[] agentTypeOptions = {"LfOSmartRandomTest", 
								"LfOSmartStraightLineTest", 
								"LfOZigZagTest", 
								"LfOFixedSequenceTest", 
								"LfOSmartExplorerTest"
								};
	
	public LFOTestRunner(ParameterList list){
		this.list = list;
	}
	
	public void setParameterList(ParameterList list){
		this.list = list;
	}
	
	public void testAll(LfOAbstractCreatureTest tests[]){
		
		if(multiTests == false){
			LfOAbstractCreatureTest inst;
			try {
				inst = tests[0].getClass().newInstance();
				inst.setParamters(list);
				inst.initSetting();
				inst.testRun();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				
				// had to take out "continue;" statement due to it not being within a loop anymore
			}
		}
		else{
			for(int t = 0; t < agentTypeList.getSelectedIndices().length; t ++){
				LfOAbstractCreatureTest inst;
				try {
					inst = TestConfiguration.test[agentTypeList.getSelectedIndices()[t]].getClass().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}
				inst.setParamters(list);
				inst.initSetting();
				inst.testRun();
			}
		}
	}
	
	/*
	 * This function creates the GUI for the program. It sets up all the components for the
	 * multiple configurations
	 * */
	public void guiSetup(){

		frame = new JFrame("Testing Screen");
		frame.setLayout(null);
		frame.setSize(810,700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		randKNN = new JCheckBox("Random kNN");
		randKNN.setFont(new Font("Arial", Font.PLAIN, 10));
		randKNN.setBounds(25, 25, 100, 25);
		randKNN.addItemListener(this);
		randKNN.setVisible(true);
		frame.add(randKNN);

		intKValues = new Integer[(TestConfiguration.K_VALUES).length];
		for(int i=0; i<TestConfiguration.K_VALUES.length; i++){
			intKValues[i] = Integer.valueOf(TestConfiguration.K_VALUES[i]);
		}
		
		diffKValue = new JComboBox<Integer>(intKValues);
		diffKValue.setFont(new Font("Arial", Font.PLAIN, 10));
		diffKValue.setBounds(25, 125, 250, 25);
		diffKValue.setBackground(Color.white);
		diffKValue.addActionListener(this);
		diffKValue.setVisible(true);
		frame.add(diffKValue);
		
		diffKValueList = new JList<Integer>(intKValues);
		diffKValueList.setFont(new Font("Arial", Font.PLAIN, 10));
		diffKValueList.setBackground(Color.white);
		diffKValueList.setBorder(BorderFactory.createLineBorder(Color.black));
		diffKValueList.setSelectedIndex(0);
		diffKValueList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		scroll1 = new JScrollPane(diffKValueList);
		scroll1.setPreferredSize(new Dimension(250, 75));
		scroll1.setBounds(25,125,250,60);
		scroll1.setVisible(false);
		frame.add(scroll1,BorderLayout.CENTER);
		
		reasoningType = new JComboBox<String>(TestConfiguration.REASONINGS);		
		reasoningType.setFont(new Font("Arial", Font.PLAIN, 10));
		reasoningType.setBounds(25, 225, 250, 25);
		reasoningType.setBackground(Color.white);
		reasoningType.addActionListener(this);
		reasoningType.setVisible(true);
		frame.add(reasoningType);
		
		reasoningTypeList = new JList<String>(TestConfiguration.REASONINGS);		
		reasoningTypeList.setFont(new Font("Arial", Font.PLAIN, 10));
		reasoningTypeList.setBackground(Color.white);
		reasoningTypeList.setBorder(BorderFactory.createLineBorder(Color.black));
		reasoningTypeList.setSelectedIndex(0);
		reasoningTypeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		scroll2 = new JScrollPane(reasoningTypeList);
		scroll2.setPreferredSize(new Dimension(250, 75));
		scroll2.setBounds(25,225,250,60);
		scroll2.setVisible(false);
		frame.add(scroll2,BorderLayout.CENTER);
			
		iterNum = new JFormattedTextField("1");
		iterNum.setFont(new Font("Arial", Font.PLAIN, 10));
		iterNum.setBounds(25, 325, 250, 50);
		iterNum.setBackground(Color.white);
		iterNum.addPropertyChangeListener("value", this);
		iterNum.setEnabled(false);
		iterNum.setVisible(true);
		frame.add(iterNum);
			
		findTraceFolder = new JButton("Find a Trace Folder");
		findTraceFolder.setFont(new Font("Arial", Font.PLAIN, 10));
		findTraceFolder.setBounds(325, 25, 250, 50);
		findTraceFolder.setBackground(Color.white);
		findTraceFolder.addActionListener(this);
		findTraceFolder.setVisible(true);
		frame.add(findTraceFolder);
		
		findExportRunFolder = new JButton("Find an Export Run Folder");
		findExportRunFolder.setFont(new Font("Arial", Font.PLAIN, 10));
		findExportRunFolder.setBounds(325, 125, 250, 50);
		findExportRunFolder.setBackground(Color.white);
		findExportRunFolder.addActionListener(this);
		findExportRunFolder.setVisible(true);
		frame.add(findExportRunFolder);
		
		agentType = new JComboBox<String>(agentTypeOptions);
		agentType.setFont(new Font("Arial", Font.PLAIN, 10));
		agentType.setBounds(325, 225, 250, 25);
		agentType.setBackground(Color.white);
		agentType.setSelectedIndex(0);
		agentType.addActionListener(this);
		agentType.setVisible(true);
		frame.add(agentType);
		
		agentTypeList = new JList<String>(agentTypeOptions);		
		agentTypeList.setFont(new Font("Arial", Font.PLAIN, 10));
		agentTypeList.setBackground(Color.white);
		agentTypeList.setBorder(BorderFactory.createLineBorder(Color.black));
		agentTypeList.setSelectedIndex(0);
		agentTypeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		scroll3 = new JScrollPane(agentTypeList);
		scroll3.setPreferredSize(new Dimension(250, 75));
		scroll3.setBounds(325,225,250,60);
		scroll3.setVisible(false);
		frame.add(scroll3,BorderLayout.CENTER);
		
		weightFunction = new JComboBox<String>(weights);
		weightFunction.setFont(new Font("Arial", Font.PLAIN, 10));
		weightFunction.setBounds(325, 325, 250, 25);
		weightFunction.setSelectedIndex(0);
		weightFunction.setBackground(Color.white);
		weightFunction.addActionListener(this);
		weightFunction.setVisible(true);
		frame.add(weightFunction);
		
//		weightFunctionList = new JList<String>(weights);
//		weightFunctionList.setFont(new Font("Arial", Font.PLAIN, 10));
//		weightFunctionList.setBackground(Color.white);
//		weightFunctionList.setBorder(BorderFactory.createLineBorder(Color.black));
//		weightFunctionList.setSelectedIndex(0);
//		weightFunctionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		weightFunctionList.addListSelectionListener(this);
		
		JPanel p = buildWeightPanel();
		scroll4 = new JScrollPane(p);
		scroll4.setPreferredSize(new Dimension(250, 75));
		scroll4.setBounds(325,325,250,60);
		scroll4.setVisible(false);
		
		frame.add(scroll4,BorderLayout.CENTER);
		
		maxRuns = new JFormattedTextField("1");
		maxRuns.setFont(new Font("Arial", Font.PLAIN, 10));
		maxRuns.setBounds(625, 25, 150, 50);
		maxRuns.setBackground(Color.white);
		maxRuns.addPropertyChangeListener("value", this);
		maxRuns.setVisible(true);
		frame.add(maxRuns);
		
//		Object[][] initial= {{weightFunction.getSelectedItem(), "1.0", ""}};
		Object[][] initial= new Object[0][0];;
		
		tableModel =new tableModel(initial, columnHeaders);
		table = new JTable();
		table.setModel(tableModel);
		table.setVisible(true);
		table.addPropertyChangeListener("value", this);
		table.setBounds(325, 420, 370, 90);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		
		String line[] = {"","",""};
		line = new String[3];
		line[2] = "";
		line[0] = "Linear Weight Function";
		line[1] = "0.5";
		tableModel.addRow(line);
		line = new String[3];
		line[2] = "";
		line[0] = "Linear Weight Function";
		line[1] = "0.2";
		tableModel.addRow(line);
		line = new String[3];
		line[2] = "";
		line[0] = "Linear Weight Function";
		line[1] = "0.1";
		tableModel.addRow(line);
		line = new String[3];
		line[2] = "";
		line[0] = "Linear Weight Function";
		line[1] = "0.05";
		tableModel.addRow(line);
		
		line = new String[3];
		line[2] = "";
		line[0] = "Decay Weight Function";
		line[1] = "10";
		tableModel.addRow(line);
		line = new String[3];
		line[2] = "";
		line[0] = "Decay Weight Function";
		line[1] = "1";
		tableModel.addRow(line);
		line = new String[3];
		line[2] = "";
		line[0] = "Decay Weight Function";
		line[1] = "0.1";
		tableModel.addRow(line);
		line = new String[3];
		line[2] = "";
		line[0] = "Decay Weight Function";
		line[1] = "0.01";
		tableModel.addRow(line);
		
		line = new String[3];
		line[0] = "Gaussian Weight Function";
		line[1] = "0.15";
		line[2] = "0";
		tableModel.addRow(line);
		line = new String[3];
		line[0] = "Gaussian Weight Function";
		line[1] = "0.15";
		line[2] = "1";
		tableModel.addRow(line);
		line = new String[3];
		line[0] = "Gaussian Weight Function";
		line[1] = "0.15";
		line[2] = "2";
		tableModel.addRow(line);
		line = new String[3];
		line[0] = "Gaussian Weight Function";
		line[1] = "0.15";
		line[2] = "5";
		tableModel.addRow(line);
		line = new String[3];
		line[0] = "Gaussian Weight Function";
		line[1] = "0.15";
		line[2] = "10";
		tableModel.addRow(line);
		
		scroll5 = new JScrollPane(table);
		scroll5.setPreferredSize(new Dimension(250, 75));
		scroll5.setBounds(325,420,370,90);
		scroll5.setVisible(true);
		frame.add(scroll5,BorderLayout.CENTER);
		
		singleTest = new JButton("Single Test");
		singleTest.setFont(new Font("Arial", Font.PLAIN, 10));
		singleTest.setBounds(25, 550, 150, 75);
		singleTest.setBackground(Color.green);
		singleTest.setEnabled(false);
		singleTest.addActionListener(this);
		singleTest.setVisible(true);
		frame.add(singleTest);
		
		multiTest = new JButton("Batch of Tests");
		multiTest.setFont(new Font("Arial", Font.PLAIN, 10));
		multiTest.setBounds(200, 550, 150, 75);
		multiTest.setBackground(Color.white);
		multiTest.setEnabled(true);
		multiTest.addActionListener(this);
		multiTest.setVisible(true);
		frame.add(multiTest);
		
		start = new JButton("Start");
		start.setFont(new Font("Arial", Font.PLAIN, 10));
		start.setBounds(600, 550, 150, 75);
		start.setBackground(Color.white);
		start.addActionListener(this);
		start.setVisible(true);
		frame.add(start);
		
		diffKValueLabel = new JLabel("K Values:");
		diffKValueLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		diffKValueLabel.setBounds(25, 100, 150, 25);
		frame.add(diffKValueLabel);
		
		reasoningTypeLabel = new JLabel("Reasoning Type:");
		reasoningTypeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		reasoningTypeLabel.setBounds(25, 200, 150, 25);
		frame.add(reasoningTypeLabel);
		
		iterNumLabel = new JLabel("Iteration Number: (Enter Integer Only)");
		iterNumLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		iterNumLabel.setBounds(25, 300, 200, 25);
		frame.add(iterNumLabel);
		
		traceFolderLabel = new JLabel("Trace Folder:");
		traceFolderLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		traceFolderLabel.setBounds(325, 0, 150, 25);
		frame.add(traceFolderLabel);
		
		exportRunFolderLabel = new JLabel("Export Run Folder:");
		exportRunFolderLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		exportRunFolderLabel.setBounds(325, 100, 150, 25);
		frame.add(exportRunFolderLabel);
		
		agentTypeLabel = new JLabel("Agent Type:");
		agentTypeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		agentTypeLabel.setBounds(325, 200, 150, 25);
		frame.add(agentTypeLabel);
		
		weightFunctionLabel = new JLabel("Weight Function:");
		weightFunctionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		weightFunctionLabel.setBounds(325, 300, 150, 25);
		frame.add(weightFunctionLabel);
		
		maxRunsLabel = new JLabel("Max Runs: (Enter Integer Only)");
		maxRunsLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		maxRunsLabel.setBounds(625, 0, 200, 25);
		frame.add(maxRunsLabel);
		
		frame.setVisible(true);
	}
	
	private JPanel buildWeightPanel(){
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 5, 5));
		
		for (int i = 0; i < weights.length; i++){
			JButton b = new JButton(weights[i].split(" ")[0]);
			b.addActionListener(this);
			b.setActionCommand("Weight");
			panel.add(b);
		}
		
		JButton b = new JButton("Clear");
		b.setActionCommand("Clear");
		b.addActionListener(this);
		panel.add(b);
		
		b = new JButton("");
		b.setEnabled(false);
		panel.add(b);
		
		return panel;
	}
	
	/*
	 * This function is executed when the "Single Test" button is pressed. The function alters the GUI to
	 * limit the user to selecting only one value for each configuration 
	 */
	public void guiSetupSingle(){
		diffKValue.setVisible(true);
		reasoningType.setVisible(true);
		agentType.setVisible(true);
		weightFunction.setVisible(true);
		scroll1.setVisible(false);
		scroll2.setVisible(false);
		scroll3.setVisible(false);
		scroll4.setVisible(false);
		
		singleTest.setEnabled(false);
		singleTest.setBackground(Color.green);
		multiTest.setEnabled(true);
		multiTest.setBackground(Color.white);
		
		Object[][] weightTab = {{weightFunction.getSelectedItem(), "1.0", ""}};

		tableModel model = new tableModel(weightTab, columnHeaders);
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
	}
	
	/*
	 * This function is executed when the "Batch of Tests" button is pressed. The function alters the GUI
	 * to allow the user to select multiple different values for several of the configurations
	 */
	public void guiSetupMulti(){
		diffKValue.setVisible(false);
		reasoningType.setVisible(false);
		agentType.setVisible(false);
		weightFunction.setVisible(false);
		scroll1.setVisible(true);
		scroll2.setVisible(true);
		scroll3.setVisible(true);
		scroll4.setVisible(true);
		
		//weightFunctionList.setSelectedIndex(0);
		
		singleTest.setEnabled(true);
		singleTest.setBackground(Color.white);
		multiTest.setEnabled(false);
		multiTest.setBackground(Color.green);
	}
	
	/*
	 * Originally, the main function housed all of the test code. After the GUI was implemented, the code that
	 * was previously present here has been moved to an action listener. 
	 */
	public static void main(String args[]){	
		LFOTestRunner program = new LFOTestRunner(null);
		program.guiSetup();
	}
	
	/*
	 * This function was implemented mainly to prevent the user from editing certain cells in the table
	 * that is populated with the user's selected weight function(s). This function allows the user
	 * to enter only one parameter except if the selected weight function is "Gaussian Weight Function",
	 * in which case the user is capable of entering two parameters
	 */
	public class tableModel extends DefaultTableModel{
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public tableModel(Object[][] a, String[] b){
			 super(a, b);
		 }
		 @Override
		 public boolean isCellEditable(int row, int col) {
			 if(table.getModel().getValueAt(row, 0).equals("Gaussian Weight Function")){
				 if(col!=0)
					 return true;
			 }
			 return(col == 1);
		 }
		 
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 * This function mainly ensures that the values entered in the text boxes are valid (i.e. if a
	 * text box is meant to accept only integers, this function prevents the user from begin testing
	 * if letters or words are entered
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		
		try{
			Integer.parseInt(maxRuns.getText());
			Integer.parseInt(iterNum.getText());
			
//			for(int i = 0; i<weightFunctionList.getSelectedIndices().length; i++){ // making sure only integers are being entered as the parameters for the weight function table
//				Double.parseDouble((String)(table.getModel().getValueAt(i, 1)));
//				if(weights[weightFunctionList.getSelectedIndices()[i]] == "Gaussian Weight Function"){
//					Double.parseDouble((String)(table.getModel().getValueAt(i,2)));
//				}
//			}
			start.setEnabled(true);
		}
		catch(NumberFormatException f){
			start.setEnabled(false);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 * 
	 * This Item Listener function is present to allow different cases for the Random KNN Check box
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getStateChange() == ItemEvent.SELECTED){
			randomKNN = true;
			iterNum.setEnabled(true);
		}
		if (e.getStateChange() == ItemEvent.DESELECTED){
			randomKNN = false;
			iterNum.setEnabled(false);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 * This function is present to allow the user to select multiple weight functions
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == weightFunctionList){			
			table.setModel(new DefaultTableModel(weightFunctionList.getSelectedIndices().length, 3));
			Object[][] weightTable = new Object[weightFunctionList.getSelectedIndices().length][3];
			
			for(int i = 0; i < weightFunctionList.getSelectedIndices().length; i++){
				weightTable[i][0] = weights[weightFunctionList.getSelectedIndices()[i]];
				weightTable[i][1] = "1.0";
				if(weightTable[i][0] == "Gaussian Weight Function"){
					weightTable[i][2] = "1.0";
				}
				else{
					weightTable[i][2] = "";
				}				
			}
			//tableModel model = new tableModel(weightTable, columnHeaders);
			//table.setModel(model);
			//table.getColumnModel().getColumn(0).setPreferredWidth(150);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * This function is present to allow certain actions to occur after certain actions to occur after
	 * the user has interacted with several components, such as:
	 *  - 	Allows user to select their desired weight function (only for single tests)
	 *  - 	Changes testing from single test or multiple
	 *  - 	Starts the test when the "Start" button is pressed, regardless of whether single test or batch of
	 *  	tests is enabled 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == weightFunction){
			Object[][] weightTab = {{weightFunction.getSelectedItem(), "1", ""}};
			if(weightTab[0][0] == "Gaussian Weight Function"){
				weightTab[0][2] = "1";
				System.out.println("reached");
			}
			tableModel model = new tableModel(weightTab, columnHeaders);
			table.setModel(model);
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
		}else if (e.getSource() instanceof JButton && e.getActionCommand().equals("Weight")){
			JButton b = (JButton) e.getSource();
			String line[] = {b.getText() + " Weight Function", "1.0", "1.0"};
			if (!b.getText().equals("Gaussian")){
				line[2] = "";
			}
			tableModel.addRow(line);
		}else if (e.getSource() instanceof JButton && e.getActionCommand().equals("Clear")){
			tableModel = new tableModel(new Object[0][0], columnHeaders);
			table.setModel(tableModel);
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
		}else if(e.getSource() == singleTest){
			multiTests = false;
			guiSetupSingle();
		}else if(e.getSource() == multiTest){
			multiTests = true;
			guiSetupMulti();
		}else if(e.getSource() == findTraceFolder){
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.showOpenDialog(frame);
			try{
				traceFolderLocation = fileChooser.getSelectedFile().toString() +"/";
				findTraceFolder.setText(traceFolderLocation);
			}
			catch(Exception NullPointerException){}
		}else if(e.getSource() == findExportRunFolder){
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.showOpenDialog(frame);
			try{
				exportRunFolderLocation = fileChooser.getSelectedFile().toString() + "/";
				findExportRunFolder.setText(exportRunFolderLocation);
			}
			catch(Exception NullPointerException){}
		}else if(e.getSource() == start){
			
			if(table.isEditing() == true){
				table.getCellEditor().stopCellEditing();
			}
			
			if(multiTests == false){
				ParameterList list = new ParameterList();

				System.out.println("Reasoning Method : " + reasoningType.getSelectedItem());					
				if(randomKNN == true){
					list.addParameter(ParameterNameEnum.ITER_NUM.name(), iterNum.getText());
				}
				else {
					list.addParameter(ParameterNameEnum.ITER_NUM.name(), 1);
				}
				list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), maxRuns.getText());
				list.addParameter(ParameterNameEnum.K_VALUE.name(), diffKValue.getSelectedItem());
				list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), randomKNN);
				list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), traceFolderLocation + "Expert/Run " + maxRuns.getText());
				if(randomKNN == true){
					list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), exportRunFolderLocation +  "Agent " + ("Random") + "/Run " + maxRuns.getText() + "/" + iterNum.getText());
				}
				else {
					list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), exportRunFolderLocation +  "Agent " + ("NonRandom") + "/Run " + maxRuns.getText() + "/" + (1));
				}				
				list.addParameter(ParameterNameEnum.REASONING.name(), reasoningType.getSelectedItem());
				System.out.println();
						
				LfOAbstractCreatureTest[] newTest = new LfOAbstractCreatureTest[1];
				
				if(agentType.getSelectedItem() == "LfOSmartRandomTest"){
					newTest[0] = new LfOSmartRandomTest();
				}
				if(agentType.getSelectedItem() == "LfOSmartStraightLineTest"){
					newTest[0] = new LfOSmartStraightLineTest();
				}
				if(agentType.getSelectedItem() == "LfOZigZagTest"){
					newTest[0] = new LfOZigZagTest();
				}
				if(agentType.getSelectedItem() == "LfOFixedSequenceTest"){
					newTest[0] = new LfOFixedSequenceTest();
				}
				if(agentType.getSelectedItem() == "LfOSmartExplorerTest"){
					newTest[0] = new LfOSmartExplorerTest();
				}
				
				if (reasoningType.getSelectedItem().equals("SEQ")){
					if(randomKNN == true){
						System.out.println("Random, k : " + diffKValue.getSelectedItem() + " Iter " + maxRuns.getText());
					}
					else {
						System.out.println("Non Random, k : " + diffKValue.getSelectedItem());
					}
					
					setParameterList(list);
					testAll(newTest);	
				}
				else {
					WeightFunction w = new FixedWeightFunction(0.1);
					if(weightFunction.getSelectedItem() == "Decay Weight Function"){
						w = new DecayWeightFunction(Double.parseDouble((String)table.getValueAt(0, 1)));
					}
					if(weightFunction.getSelectedItem() == "Fixed Weight Function"){
						w = new FixedWeightFunction(Double.parseDouble((String)table.getValueAt(0, 1)));
					}
					if(weightFunction.getSelectedItem() == "Gaussian Weight Function"){
						w = new GaussianWeightFunction(Double.parseDouble((String)table.getValueAt(0, 1)), Double.parseDouble((String)table.getValueAt(0, 2)));
					}
					if(weightFunction.getSelectedItem() == "Linear Weight Function"){
						w = new LinearWeightFunction(Double.parseDouble((String)table.getValueAt(0, 1)));
					}
					if(weightFunction.getSelectedItem() == "Time Varying Function"){
//						w = new TimeVaryingWeightFunction(Double.parseDouble((String)table.getValueAt(0, 1)));
					}
					
					kNNUtil.setWeightFunction(w);
					if(randomKNN == true){
						System.out.println("Random, k : " + diffKValue.getSelectedItem() + " Iter " + iterNum.getText());
					}
					else {
						System.out.println("Non Random, k : " + diffKValue.getSelectedItem());
					}
					
					System.out.println("Weight Function : " + w.toString());
					setParameterList(list);
					testAll(newTest);
				}
				System.out.println();
			} else {
				int[] r = reasoningTypeList.getSelectedIndices(); // user's
																	// selected
																	// reasoning
																	// types.
				int[] l = diffKValueList.getSelectedIndices(); // user's
																// selected k
																// values.

				for (int m = 0; m < r.length; m++) {
					System.out.println("Reasoning Method : " + TestConfiguration.REASONINGS[r[m]]);
					for (int j = 0; j < l.length; j++) {
						for (int i = 0; i < Integer.parseInt(maxRuns.getText()); i++) {
							ParameterList list = new ParameterList();

							list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), i + 1);
							list.addParameter(ParameterNameEnum.K_VALUE.name(), intKValues[l[j]]);
							list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), randomKNN);
							list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(),traceFolderLocation + "Expert/Run "	+ (i + 1));
							if (randomKNN == true) {
								list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(),exportRunFolderLocation + "Agent "	+ ("Random") + "/Run " + (i + 1) + "/" + (1));
							} else {
								list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(),exportRunFolderLocation + "Agent "	+ ("NonRandom") + "/Run " + (i + 1) + "/" + (1));
							}

							list.addParameter(ParameterNameEnum.REASONING.name(), TestConfiguration.REASONINGS[r[m]]);

							LfOAbstractCreatureTest[] newTest = new LfOAbstractCreatureTest[agentTypeList.getSelectedIndices().length];
							for (int t = 0; t < newTest.length; t++) {
								if (agentType.getSelectedItem() == "LfOSmartRandomTest") {
									newTest[t] = new LfOSmartRandomTest();
									t++;
								}
								if (agentType.getSelectedItem() == "LfOSmartStraightLineTest") {
									newTest[t] = new LfOSmartStraightLineTest();
									t++;
								}
								if (agentType.getSelectedItem() == "LfOZigZagTest") {
									newTest[t] = new LfOZigZagTest();
									t++;
								}
								if (agentType.getSelectedItem() == "LfOFixedSequenceTest") {
									newTest[t] = new LfOFixedSequenceTest();
									t++;
								}
								if (agentType.getSelectedItem() == "LfOSmartExplorerTest") {
									newTest[t] = new LfOSmartExplorerTest();
									t++;
								}
							}

							if (TestConfiguration.REASONINGS[r[m]].equals("SEQ")) {
								if (randomKNN == true) {
									System.out.println("Random, k : " + intKValues[l[j]] + " Iter " + (i + 1));
								} else {
									System.out.println("Non Random, k : " + intKValues[l[j]]);
								}

								setParameterList(list);
								testAll(newTest);
							} else {
								WeightFunction[] selectedWeights = new WeightFunction[table.getRowCount()];
								for (int n = 0; n < table.getRowCount(); n++) {
									if (table.getValueAt(n, 0).equals("Decay Weight Function")) {
										selectedWeights[n] = new DecayWeightFunction(Double.parseDouble((String) table.getValueAt(n, 1)));
									}
									if (table.getValueAt(n, 0).equals("Fixed Weight Function")) {
										selectedWeights[n] = new FixedWeightFunction(Double.parseDouble((String) table.getValueAt(n, 1)));
									}
									if (table.getValueAt(n, 0).equals("Gaussian Weight Function")) {
										selectedWeights[n] = new GaussianWeightFunction(Double.parseDouble((String) table.getValueAt(n, 1)),Double.parseDouble((String) table.getValueAt(n, 2)));
									}
									if (table.getValueAt(n, 0).equals("Linear Weight Function")) {
										selectedWeights[n] = new LinearWeightFunction(Double.parseDouble((String) table.getValueAt(n, 1)));
									}
								}
								for (WeightFunction w : selectedWeights) {
									kNNUtil.setWeightFunction(w);
									if (randomKNN == true) {
										System.out.println("Random, k : " + intKValues[l[j]] + " Iter " + (i + 1));
									} else {
										System.out.println("Non Random, k : " + intKValues[l[j]]);
									}

									System.out.println("Weight Function : " + w.toString());
									setParameterList(list);
									testAll(newTest);
								}

								System.out.println();
							}
						}
					}
				}
			}
			//System.out.println("end reached");
		}
	}	
}