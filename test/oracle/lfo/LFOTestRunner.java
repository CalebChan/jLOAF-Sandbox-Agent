package oracle.lfo;

import org.jLOAF.retrieve.kNNUtil;
import org.jLOAF.retrieve.sequence.weight.WeightFunction;
import org.jLOAF.retrieve.sequence.weight.DecayWeightFunction;
import org.jLOAF.retrieve.sequence.weight.FixedWeightFunction;
import org.jLOAF.retrieve.sequence.weight.GaussianWeightFunction;
import org.jLOAF.retrieve.sequence.weight.LinearWeightFunction;
import org.jLOAF.retrieve.sequence.weight.TimeVaryingWeightFunction;

import oracle.Config;
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


public class LFOTestRunner extends JFrame implements ItemListener, ActionListener, PropertyChangeListener, ListSelectionListener{

	private ParameterList list;
	
	Integer[] intKValues; //will turn TestConfiguration.K_VALUES from an int to an Integer for the jComboBox
	Integer[] intIterNums; //will turn TestConfiguration.MAX_REPEATED_RUNS from an int to an Integer
	
	// GUI Components: 
	JFrame frame;
	JCheckBox randKNN;
	JComboBox diffKValue, reasoningType, agentType, weightFunction;
	JFormattedTextField traceFolder, exportRunFolder, maxRuns, iterNum;
	JTable table;	// selected weight function values will be transfered into this table
	JList diffKValueList, reasoningTypeList, agentTypeList, weightFunctionList; //when testing multiple configurations, JLists allow for multiple selections while JComboBoxes do not
	JButton singleTest, multiTest, start;
	
	JLabel diffKValueLabel, reasoningTypeLabel, iterNumLabel, agentTypeLabel, weightFunctionLabel,	// labels above most components
			traceFolderLabel, exportRunFolderLabel, maxRunsLabel;									// to indicate configuration type
	
	String[] columnHeaders = {"Weight Function", "Parameter 1", "Parameter 2"};		// headers for the selected weight function table
	
	JScrollPane scroll1, scroll2, scroll3, scroll4, scroll5;	//scrollers for the JLists and the JTable
	
	boolean randomKNN = false; 	// determines if kNN is random or set
	boolean multiTests = false;	// determines if there is a single test or multiple
	
	// Different weight function types:
	String[] weights = {"Decay Weight Function"
						,"Fixed Weight Function"
						,"Gaussian Weight Function"
						,"Linear Weight Function"
//						,"Time Varying Function"
						};
	
	String[] agentTypeOptions = {"LfOSmartRandomTest", 
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
				inst = tests[agentType.getSelectedIndex()].getClass().newInstance();
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
	



	// Original Code for this function:	
/*		for (LfOAbstractCreatureTest t : tests){
			LfOAbstractCreatureTest inst;
			try {
				inst = t.getClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}
			inst.setParamters(list);
			inst.initSetting();
			inst.testRun(); 
		}
	*/
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
		
		diffKValue = new JComboBox(intKValues);
		diffKValue.setFont(new Font("Arial", Font.PLAIN, 10));
		diffKValue.setBounds(25, 125, 250, 25);
		diffKValue.setBackground(Color.white);
		diffKValue.addActionListener(this);
		diffKValue.setVisible(true);
		frame.add(diffKValue);
		
		diffKValueList = new JList(intKValues);
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
		
		reasoningType = new JComboBox(TestConfiguration.REASONINGS);		
		reasoningType.setFont(new Font("Arial", Font.PLAIN, 10));
		reasoningType.setBounds(25, 225, 250, 25);
		reasoningType.setBackground(Color.white);
		reasoningType.addActionListener(this);
		reasoningType.setVisible(true);
		frame.add(reasoningType);
		
		reasoningTypeList = new JList(TestConfiguration.REASONINGS);		
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
		
		traceFolder = new JFormattedTextField("Enter a Trace Folder");
		traceFolder.setFont(new Font("Arial", Font.PLAIN, 10));
		traceFolder.setBounds(325, 25, 250, 50);
		traceFolder.setBackground(Color.white);
		traceFolder.addPropertyChangeListener("value", this);
		traceFolder.setVisible(true);
		frame.add(traceFolder);
		
		exportRunFolder = new JFormattedTextField("Enter an Export Run Folder");
		exportRunFolder.setFont(new Font("Arial", Font.PLAIN, 10));
		exportRunFolder.setBounds(325, 125, 250, 50);
		exportRunFolder.setBackground(Color.white);
		exportRunFolder.addPropertyChangeListener("value", this);
		exportRunFolder.setVisible(true);
		frame.add(exportRunFolder);
		
		agentType = new JComboBox(agentTypeOptions);
		agentType.setFont(new Font("Arial", Font.PLAIN, 10));
		agentType.setBounds(325, 225, 250, 25);
		agentType.setBackground(Color.white);
		agentType.setSelectedIndex(0);
		agentType.addActionListener(this);
		agentType.setVisible(true);
		frame.add(agentType);
		
		agentTypeList = new JList(agentTypeOptions);		
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
		
		weightFunction = new JComboBox(weights);
		weightFunction.setFont(new Font("Arial", Font.PLAIN, 10));
		weightFunction.setBounds(325, 325, 250, 25);
		weightFunction.setSelectedIndex(0);
		weightFunction.setBackground(Color.white);
		weightFunction.addActionListener(this);
		weightFunction.setVisible(true);
		frame.add(weightFunction);
		
		weightFunctionList = new JList(weights);
		weightFunctionList.setFont(new Font("Arial", Font.PLAIN, 10));
		weightFunctionList.setBackground(Color.white);
		weightFunctionList.setBorder(BorderFactory.createLineBorder(Color.black));
		weightFunctionList.setSelectedIndex(0);
		weightFunctionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		weightFunctionList.addListSelectionListener(this);
		
		scroll4 = new JScrollPane(weightFunctionList);
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
		
		Object[][] initial= {{weightFunction.getSelectedItem(), "1", ""}};
		
		tableModel model=new tableModel(initial, columnHeaders);
		table = new JTable();
		table.setModel(model);
		table.setVisible(true);
		table.addPropertyChangeListener("value", this);
		
		table.setBounds(325, 420, 370, 90);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		//table.setFillsViewportHeight(true);
		//frame.add(table);
		
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
		
		Object[][] weightTab = {{weightFunction.getSelectedItem(), "1", ""}};

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
		
		weightFunctionList.setSelectedIndex(0);
		
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
		
		
		// Original Code without GUI:
		
/*		LFOTestRunner runner = new LFOTestRunner(null);
		for (String r : TestConfiguration.REASONINGS){
			System.out.println("Reasoning Method : " + r);
			for (int i = 0; i < TestConfiguration.MAX_RUNS; i++){
				for (int j = 0; j < TestConfiguration.K_VALUES.length; j++){
					ParameterList list = new ParameterList();
					
					for (int k = 0; k < TestConfiguration.MAX_REPEATED_RUNS; k++){
						list.addParameter(ParameterNameEnum.ITER_NUM.name(), k + 1);
						list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), i + 1);
						list.addParameter(ParameterNameEnum.K_VALUE.name(), TestConfiguration.K_VALUES[j]);
						list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), true);
						list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + (i + 1));
						list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ("Random") + "/Run " + (i + 1) + "/" + (k + 1));
						
						list.addParameter(ParameterNameEnum.REASONING.name(), r);
						if (r.equals("SEQ")){
							//	
						}
						
						runner.setParameterList(list);
						runner.testAll(TestConfiguration.test);
					}
					if (TestConfiguration.USE_NON_RANDOM){
						list.addParameter(ParameterNameEnum.ITER_NUM.name(), 1);
						list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), i + 1);
						list.addParameter(ParameterNameEnum.K_VALUE.name(), TestConfiguration.K_VALUES[j]);
						list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), false);
						list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + (i + 1));
						list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ((false) ? "Random" : "NonRandom") + "/Run " + (i + 1) + "/" + (1));
						
						list.addParameter(ParameterNameEnum.REASONING.name(), r);
						if (r.equals("SEQ")){
							//	
						}
						
						runner.setParameterList(list);
						runner.testAll(TestConfiguration.test);
						System.out.println("loop complete");
					}
				}
			}
			System.out.println();
		}
*/		
		
		//Updated version of original code: 
		
/*		LFOTestRunner runner = new LFOTestRunner(null);
		for (String r : TestConfiguration.REASONINGS){
			System.out.println("Reasoning Method : " + r);
			for (int i = 0; i < TestConfiguration.MAX_RUNS; i++){
				for (int j = 0; j < TestConfiguration.K_VALUES.length; j++){
						ParameterList list = new ParameterList();
						for (int k = 0; k < TestConfiguration.MAX_REPEATED_RUNS; k++){
							list.addParameter(ParameterNameEnum.ITER_NUM.name(), k + 1);
							list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), i + 1);
							list.addParameter(ParameterNameEnum.K_VALUE.name(), TestConfiguration.K_VALUES[j]);
							list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), true);
							list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + (i + 1));
							list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ("Random") + "/Run " + (i + 1) + "/" + (k + 1));
							
							list.addParameter(ParameterNameEnum.REASONING.name(), r);
							if (r.equals("SEQ")){
								System.out.println("Random, k : " + TestConfiguration.K_VALUES[j] + " Iter " + i);
								runner.setParameterList(list);
								runner.testAll(TestConfiguration.test);	
							}else{
								for (WeightFunction w : TestConfiguration.WEIGHT_FUNCTION){
									kNNUtil.setWeightFunction(w);
									System.out.println("Random, k : " + TestConfiguration.K_VALUES[j] + " Iter " + (k + 1));
									System.out.println("Weight Function : " + w.toString());
									runner.setParameterList(list);
									runner.testAll(TestConfiguration.test);
								}
							}
							System.out.println();
						}
						if (TestConfiguration.USE_NON_RANDOM){
							list.addParameter(ParameterNameEnum.ITER_NUM.name(), 1);
							list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), i + 1);
							list.addParameter(ParameterNameEnum.K_VALUE.name(), TestConfiguration.K_VALUES[j]);
							list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), false);
							list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + (i + 1));
							list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ("NonRandom") + "/Run " + (i + 1) + "/" + (1));
							
							list.addParameter(ParameterNameEnum.REASONING.name(), r);
							if (r.equals("SEQ")){
								System.out.println("Non Random, k : " + TestConfiguration.K_VALUES[j]);
								runner.setParameterList(list);
								runner.testAll(TestConfiguration.test);	
							}else{
								for (WeightFunction w : TestConfiguration.WEIGHT_FUNCTION){
									kNNUtil.setWeightFunction(w);
									System.out.println("Non Random, k : " + TestConfiguration.K_VALUES[j]);
									System.out.println("Weight Function : " + w.toString());
									runner.setParameterList(list);
									runner.testAll(TestConfiguration.test);
								}
							}
							System.out.println();
						}
					}	
				}
			}
			
*/
	}
	
	/*
	 * This function was implemented mainly to prevent the user from editing certain cells in the table
	 * that is populated with the user's selected weight function(s). This function allows the user
	 * to enter only one parameter except if the selected weight function is "Gaussian Weight Function",
	 * in which case the user is capable of entering two parameters
	 */
	public class tableModel extends DefaultTableModel{
		 public tableModel(Object[][] a, String[] b){
			 super(a, b);
		 }
		 @Override
		 public boolean isCellEditable(int row, int col) {
			 if(table.getModel().getValueAt(row, 0) == "Gaussian Weight Function"){
				 if(col!=0)
					 return true;
			 }
			 return(col == 1);
		 }
		 
		 /*
		  * (non-Javadoc)
		  * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		  * 
		  * The following class prevents users from entering non-interger values into the parameters of the table
		  * If non-integers are entered into any cell, the program will take that cell's last integer value
		  */
		 @Override
		 public Class<?> getColumnClass(int columnIndex) {	 
			 return Integer.class;
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
			
			for(int i = 0; i<weightFunctionList.getSelectedValues().length; i++){ // making sure only integers are being entered as the parameters for the weight function table
				Integer.parseInt((String)(table.getModel().getValueAt(i, 1)));
				if(weightFunctionList.getSelectedValues()[i] == "Gaussian Weight Function"){
					Integer.parseInt((String)(table.getModel().getValueAt(i,2)));
				}
			}
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
			table.setModel(new DefaultTableModel(weightFunctionList.getSelectedValues().length, 3));
			Object[][] weightTable = new Object[weightFunctionList.getSelectedValues().length][3];
			
			for(int i = 0; i < weightFunctionList.getSelectedValues().length; i++){
				weightTable[i][0] = weightFunctionList.getSelectedValues()[i];
				weightTable[i][1] = "1";
				if(weightTable[i][0] == "Gaussian Weight Function"){
					weightTable[i][2] = "1";
				}
				else{
					weightTable[i][2] = "";
				}				
			}
			tableModel model = new tableModel(weightTable, columnHeaders);
			table.setModel(model);
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
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
		}
		if(e.getSource() == singleTest){
			multiTests = false;
			guiSetupSingle();
		}
		if(e.getSource() == multiTest){
			multiTests = true;
			guiSetupMulti();
		}
		if(e.getSource() == start){
			
			//To improve the system to prevent user from entering non-integers into the JTable 
			/*
			if(table.isEditing() == true){
				table.getCellEditor().stopCellEditing();
			}
			*/
			
			
			
//			LFOTestRunner runner = new LFOTestRunner(null);
			if(multiTests == false){
				ParameterList list = new ParameterList();
				
/* Test Output: */
				System.out.println();
	 			System.out.println("Reasoning Method: " + reasoningType.getSelectedItem());				//only for testing
	 			if(randomKNN == true){
					System.out.println("Iteration Number: " + iterNum.getText());						//only for testing
				}
				else{
					System.out.println("Iteration Number: " + (1));										//only for testing
				}
	 			System.out.println("Run Number: " + Integer.parseInt((maxRuns.getText())));						//only for testing
				System.out.println("K Value: " + diffKValue.getSelectedItem());									//only for testing
				System.out.println("Random kNN: " + randomKNN);													//only for testing
				System.out.println("Trace Folder: " + traceFolder.getText() + "Expert/Run " + maxRuns.getText());	//only for testing
				if(randomKNN == true){
					System.out.println("Export Run Folder: " + exportRunFolder.getText() +  "Agent " + ("Random") + "/Run " + maxRuns.getText() + "/" + iterNum.getText());	//only for testing	
				}
				else{
					System.out.println("Export Run Folder: " + exportRunFolder.getText() +  "Agent " + ((false) ? "Random" : "NonRandom") + "/Run " + maxRuns.getText() + "/" + (1));	//only for testing					
				}				
				System.out.println("Reasoning Method: " + reasoningType.getSelectedItem());						//only for testing
				//System.out.println();																			//only for testing
/* End of Test output*/			
				
				System.out.println("Reasoning Method : " + reasoningType.getSelectedItem());					
				if(randomKNN == true){
					list.addParameter(ParameterNameEnum.ITER_NUM.name(), iterNum.getText());
				}
				else{
					list.addParameter(ParameterNameEnum.ITER_NUM.name(), 1);
				}
				list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), maxRuns.getText());
				list.addParameter(ParameterNameEnum.K_VALUE.name(), diffKValue.getSelectedItem());
				list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), randomKNN);
				list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), traceFolder.getText() + "Expert/Run " + maxRuns.getText());
				if(randomKNN == true){
					list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), exportRunFolder.getText() +  "Agent " + ("Random") + "/Run " + maxRuns.getText() + "/" + iterNum.getText());
				}
				else{
					list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), exportRunFolder.getText() +  "Agent " + ((false) ? "Random" : "NonRandom") + "/Run " + maxRuns.getText() + "/" + (1));
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
					else{
						System.out.println("Random, k : " + diffKValue.getSelectedItem());
					}
					
					setParameterList(list);
					testAll(newTest);	
				}else{
					WeightFunction w = new FixedWeightFunction(0.1);
					if(weightFunction.getSelectedItem() == "Decay Weight Function"){
						w = new DecayWeightFunction(Integer.parseInt((String)table.getValueAt(0, 1)));
					}
					if(weightFunction.getSelectedItem() == "Fixed Weight Function"){
						System.out.println(table.getValueAt(0, 1));
						w = new FixedWeightFunction(Integer.parseInt((String)table.getValueAt(0, 1)));
					}
					if(weightFunction.getSelectedItem() == "Gaussian Weight Function"){
						w = new GaussianWeightFunction(Integer.parseInt((String)table.getValueAt(0, 1)), Integer.parseInt((String)table.getValueAt(0, 2)));
					}
					if(weightFunction.getSelectedItem() == "Linear Weight Function"){
						w = new LinearWeightFunction(Integer.parseInt((String)table.getValueAt(0, 1)));
					}
					if(weightFunction.getSelectedItem() == "Time Varying Function"){
//						w = new TimeVaryingWeightFunction(Integer.parseInt((String)table.getValueAt(0, 1)));
					}
						kNNUtil.setWeightFunction(w);
						if(randomKNN == true){
							System.out.println("Random, k : " + diffKValue.getSelectedItem() + " Iter " + iterNum.getText());
						}
						else{
							System.out.println("Random, k : " + diffKValue.getSelectedItem());
						}
						
						System.out.println("Weight Function : " + w.toString());
						setParameterList(list);
						testAll(newTest);
						System.out.println("end reached");
				}
			}
			else{
				int[] r = reasoningTypeList.getSelectedIndices();	// user's selected reasoning types.
				int[] l = diffKValueList.getSelectedIndices();		// user's selected k values.
				
				int valueOfMaxRuns;
				if(randomKNN == true){
					valueOfMaxRuns = Integer.parseInt(iterNum.getText());
				}
				else{
					valueOfMaxRuns = 1;
				}

				for (int m = 0; m<r.length; m++){
					System.out.println("Reasoning Method: " + TestConfiguration.REASONINGS[r[m]]);
					for (int i = 0; i < Integer.parseInt(maxRuns.getText()); i++){
						for (int j = 0; j < l.length; j++){
							ParameterList list = new ParameterList();
							for (int k = 0; k<valueOfMaxRuns; k++){
/* Test Output*/							
								if(randomKNN == true){
									System.out.println("Iteration Number: " + (k+1));							//only for testing
								}
								else{
									System.out.println("Iteration Number: " + 1);									// only for testing
								}
								
								System.out.println("Run Number: " + (i+1));									//only for testing
								System.out.println("K Value: " + intKValues[l[j]]);							//only for testing
								System.out.println("Random kNN: " + randomKNN);								//only for testing
								System.out.println("Trace Folder: " + traceFolder.getText() + "Expert/Run " + (i + 1));					//only for testing
								if(randomKNN == true){
									System.out.println("Export Run Folder: " + exportRunFolder.getText() + "Agent" + ("Random") + "/Run " + (i+1) + "/" + (k+1));	//only for testing
								}
								else{
									System.out.println("Export Run Folder: " + exportRunFolder.getText() + "Agent " + ((false) ? "Random" : "NonRandom") +"/Run " + (i+1) + "/" + (1));	// only for testing
								}
								System.out.println("Reasoning Method: " + TestConfiguration.REASONINGS[r[m]]);				//only for testing
								System.out.println();														//only for testing	
/* End of test output */
								if(randomKNN == true){
									list.addParameter(ParameterNameEnum.ITER_NUM.name(), k + 1);
								}
								else{
									list.addParameter(ParameterNameEnum.ITER_NUM.name(), 1);
								}
								list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), i + 1);
								list.addParameter(ParameterNameEnum.K_VALUE.name(), intKValues[l[j]]);
								list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), randomKNN);
								list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), traceFolder.getText() + "Expert/Run " + (i + 1));
								if(randomKNN == true){
								list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), exportRunFolder.getText() +  "Agent " + ("Random") + "/Run " + (i+1) + "/" + (k+1));
								}
								else{
									list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ((false) ? "Random" : "NonRandom") + "/Run " + (i+1) + "/" + (1));
								}
								
								list.addParameter(ParameterNameEnum.REASONING.name(), TestConfiguration.REASONINGS[r[m]]);
								
								
								LfOAbstractCreatureTest[] newTest = new LfOAbstractCreatureTest[agentTypeList.getSelectedValues().length];
								for(int t = 0; t < newTest.length; t++){
									if(agentType.getSelectedItem() == "LfOSmartRandomTest"){
										newTest[t] = new LfOSmartRandomTest();
										t++;
									}
									if(agentType.getSelectedItem() == "LfOSmartStraightLineTest"){
										newTest[t] = new LfOSmartStraightLineTest();
										t++;
									}
									if(agentType.getSelectedItem() == "LfOZigZagTest"){
										newTest[t] = new LfOZigZagTest();
										t++;
									}
									if(agentType.getSelectedItem() == "LfOFixedSequenceTest"){
										newTest[t] = new LfOFixedSequenceTest();
										t++;
									}
									if(agentType.getSelectedItem() == "LfOSmartExplorerTest"){
										newTest[t] = new LfOSmartExplorerTest();
										t++;
									}
								}
								
								
								if (TestConfiguration.REASONINGS[r[m]].equals("SEQ")){
									if(randomKNN == true){
										System.out.println("Random, k : " + diffKValue.getSelectedItem() + " Iter " + (i + 1));
									}
									else{
										System.out.println("Random, k : " + diffKValue.getSelectedItem());
									}
									
									setParameterList(list);
									testAll(newTest);	
								}else{
									
									WeightFunction[] selectedWeights = new WeightFunction[weightFunctionList.getSelectedIndices().length];
									for(int n = 0; n < selectedWeights.length; n++){
										if(weightFunction.getSelectedItem() == "Decay Weight Function"){
											selectedWeights[n] = new DecayWeightFunction((Integer)table.getValueAt(1, 1));
											n++;
										}
										if(weightFunction.getSelectedItem() == "Fixed Weight Function"){
											selectedWeights[n] = new FixedWeightFunction((Integer)table.getValueAt(1, 1));
											n++;
										}
										if(weightFunction.getSelectedItem() == "Gaussian Weight Function"){
											selectedWeights[n] = new GaussianWeightFunction((Integer)table.getValueAt(1, 1), (Integer)table.getValueAt(1, 2));
											n++;
										}
										if(weightFunction.getSelectedItem() == "Linear Weight Function"){
											selectedWeights[n] = new LinearWeightFunction((Integer)table.getValueAt(1, 1));
											n++;
										}
										if(weightFunction.getSelectedItem() == "Time Varying Function"){
//											selectedWeights[n] = new TimeVaryingWeightFunction((Integer)table.getValueAt(1, 1));
//											n++;
										}
									}
									for(WeightFunction w:selectedWeights){
										kNNUtil.setWeightFunction(w);
										if(randomKNN == true){
											System.out.println("Random, k : " + diffKValue.getSelectedItem() + " Iter " + iterNum.getText());
										}
										else{
											System.out.println("Random, k : " + diffKValue.getSelectedItem());
										}
										
										System.out.println("Weight Function : " + w.toString());
										setParameterList(list);
										testAll(newTest);
									}
								}
							}
						}
					}
				}
			}
		}
	}	
}