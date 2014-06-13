package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.SpringLayout;

import agent.AbstractSandboxAgent;
import sandbox.Direction;
import util.expert.ExpertConfig;
import util.expert.ExpertStrategy;

public class SandboxTraceGUI {
	
	public static void main(String args[]){
		@SuppressWarnings("unused")
		SandboxTraceGUI gui = new SandboxTraceGUI();
	}
	
	private JFrame frame;
	
	private JTextArea saveLocal;
	private JTextArea convertLocal;
	
	private JTextArea xArea;
	private JTextArea yArea;
	private JComboBox<Direction> dirArea;
	private JComboBox<Integer> gridSize;
	private JComboBox<ExpertStrategy> agentSelect;
	private JTextArea cycleArea;
	
	private JTextArea iterArea;
	private JCheckBox randomBox;
	
	public static final int MAX_SIZE = 9;
	public static final int DEFAULT_PADDING = 5;
	public static final int DEFAULT_X_Y = 5;
	public static final String DEFAULT_DELIMITER = "|";
	
	public static final int DEFAULT_X_Y_COOR = 2;
	public static final int DEFAULT_CYCLES = 100;
	
	public static final String DEFAULT_TRACE_EXTENSION = ".trace";
	public static final String DEFAULT_CASEBASE_EXTENSION = ".cb";
	
	private static final String DEFAULT_TRACE_NAME = "Test1";
	private static final String DEFAULT_CASEBASE_NAME = "casebase1";
	
	public SandboxTraceGUI(){
		frame = new JFrame("");
		frame.setLayout(new BorderLayout());
		
		frame.add(locationPanel(), BorderLayout.NORTH);
		
		frame.add(confimPanel(), BorderLayout.SOUTH);
		
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void run(int x, int y, Direction d, String saveFile){
		int size = this.gridSize.getItemAt(this.gridSize.getSelectedIndex()).intValue();
		ExpertStrategy agent = this.agentSelect.getItemAt(this.agentSelect.getSelectedIndex());
		int iterations = Integer.parseInt(this.cycleArea.getText());
		AbstractSandboxAgent a = agent.getAgent(size, x, y, d);

		if (a == null){
			return;
		}
		
		if (saveFile == null || saveFile.isEmpty()){
			return;
		}
		a.runAgent(iterations);
		a.saveTrace(saveFile + DEFAULT_TRACE_EXTENSION);
		System.out.println("DONE");
	}
	
	private JPanel confimPanel(){
		JPanel panel = new JPanel();
		
		saveLocal = new JTextArea(DEFAULT_TRACE_NAME);
		convertLocal = new JTextArea(DEFAULT_CASEBASE_NAME);
		
		JButton runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (saveLocal.getText() != null && !saveLocal.getText().equals("")){
					int x = Integer.parseInt(xArea.getText());
					int y = Integer.parseInt(yArea.getText());
					Direction d = dirArea.getItemAt(dirArea.getSelectedIndex());
					String saveFile = saveLocal.getText();
					if (iterArea.getText() == null || iterArea.getText().isEmpty()){
						run(x, y, d, saveFile);
					}else{
						int iter = Integer.parseInt(iterArea.getText());
						int size = gridSize.getItemAt(gridSize.getSelectedIndex()).intValue();
						Random r = new Random(0);
						try {
							BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile + DEFAULT_TRACE_EXTENSION));
							for (int i = 0; i < iter; i++){
								run(x, y, d, "TMP");
								if (randomBox.isSelected()){
									x = r.nextInt(size - 2) + 1;
									y = r.nextInt(size - 2) + 1;
									d = Direction.values()[r.nextInt(Direction.values().length)];
								}
								BufferedReader reader = new BufferedReader(new FileReader("TMP" + DEFAULT_TRACE_EXTENSION));
								String line = reader.readLine();
								while(line != null){
									writer.append(line + "\n");
									line = reader.readLine();
								}
								reader.close();
							}
							writer.close();
							File f = new File("TMP" + DEFAULT_TRACE_EXTENSION);
							f.delete();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}					
				}
			}
		});
		
		panel.setLayout(new SpringLayout());
		
		panel.add(runButton);
		panel.add(saveLocal);
		
		JButton convertButton = new JButton("Convert");
		convertButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (convertLocal.getText() != null && !convertLocal.getText().equals("") && saveLocal.getText() != null && !saveLocal.getText().equals("")){
					ExpertStrategy agent = agentSelect.getItemAt(agentSelect.getSelectedIndex());
					agent.parseFile(saveLocal.getText(), convertLocal.getText());
				}
			}
		});
		
		panel.add(convertButton);
		panel.add(convertLocal);
		
		SpringUtilities.makeGrid(panel, 2, 2, DEFAULT_X_Y, DEFAULT_X_Y, DEFAULT_PADDING, DEFAULT_PADDING);
		
		return panel;
	}
	
	private JPanel locationPanel(){
		JPanel panel = new JPanel();
		
		panel.setLayout(new SpringLayout());
		
		xArea = new JTextArea("" + DEFAULT_X_Y_COOR);
		yArea = new JTextArea("" + DEFAULT_X_Y_COOR);
		dirArea = new JComboBox<Direction>(Direction.values());
		
		Integer size[] = new Integer[MAX_SIZE];
		for (int i = 0; i < size.length; i++){
			size[i] = i + 2;
		}
		gridSize = new JComboBox<Integer>(size);
		gridSize.setSelectedIndex(size.length - 1);
		
		panel.add(new JLabel("Starting X:"));
		panel.add(xArea);
		
		panel.add(new JLabel("Starting Y:"));
		panel.add(yArea);
		
		panel.add(new JLabel("Starting Direction:"));
		panel.add(dirArea);
		
		panel.add(new JLabel("Grid Size:"));
		panel.add(gridSize);
		
		agentSelect = new JComboBox<ExpertStrategy>(ExpertConfig.STRATEGY);
		agentSelect.setRenderer(new ComboBoxRenderer());
		panel.add(new JLabel("Agent:"));
		panel.add(agentSelect);
		
		panel.add(new JLabel("Cycles:"));
		cycleArea = new JTextArea("" + DEFAULT_CYCLES);
		panel.add(cycleArea);
		
		panel.add(new JLabel("Iterations:"));
		iterArea = new JTextArea();
		panel.add(iterArea);
		
		panel.add(new JLabel("Random Start Pos:"));
		randomBox = new JCheckBox();
		panel.add(randomBox);
		
		SpringUtilities.makeGrid(panel, 8, 2, DEFAULT_X_Y, DEFAULT_X_Y, DEFAULT_PADDING, DEFAULT_PADDING);
		return panel;
	}
}

class ComboBoxRenderer extends JLabel implements ListCellRenderer<ExpertStrategy> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList<? extends ExpertStrategy> list, ExpertStrategy value, int index, boolean isSelected, boolean cellHasFocus) {
		this.setText(value.getAgentName());
		return this;
	}


}
