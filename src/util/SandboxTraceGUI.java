package util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import agent.AbstractSandboxAgent;
import agent.backtracking.ActionBasedAgent;
import agent.backtracking.InputBasedAgent;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.StateBasedCreature;

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
	private JComboBox<String> agentSelect;
	private JTextArea iterArea;
	
	public static final int MAX_SIZE = 99;
	public static final int DEFAULT_PADDING = 5;
	public static final int DEFAULT_X_Y = 5;
	public static final String DEFAULT_DELIMITER = "|";
	
	public static final int DEFAULT_X_Y_COOR = 2;
	public static final int DEFAULT_CYCLES = 100;
	
	private static final String DEFAULT_TRACE_EXTENSION = ".trace";
	private static final String DEFAULT_CASEBASE_EXTENSION = ".cb";
	
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

	private void run(){
		int x = Integer.parseInt(this.xArea.getText());
		int y = Integer.parseInt(this.yArea.getText());
		Direction d = this.dirArea.getItemAt(this.dirArea.getSelectedIndex());
		
		Creature c = new StateBasedCreature(x, y, d);
		
		int size = this.gridSize.getItemAt(this.gridSize.getSelectedIndex()).intValue();
		String agent = this.agentSelect.getItemAt(this.agentSelect.getSelectedIndex());
		int iterations = Integer.parseInt(this.iterArea.getText());
		AbstractSandboxAgent a = null;
		if (agent.equals(ActionBasedAgent.class.getSimpleName())){
			a = new ActionBasedAgent(size, c);
		}else if (agent.equals(InputBasedAgent.class.getSimpleName())){
			a = new InputBasedAgent(size, c);
		}
		if (a == null){
			return;
		}
		String saveFile = this.saveLocal.getText();
		if (saveFile == null || saveFile.isEmpty()){
			return;
		}
		a.runAgent(iterations);
		a.saveTrace(saveFile + DEFAULT_TRACE_EXTENSION);
		System.out.println("DONE");
	}
	
	private JPanel confimPanel(){
		JPanel panel = new JPanel();
		
		saveLocal = new JTextArea();
		convertLocal = new JTextArea();
		
		JButton runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (saveLocal.getText() != null && !saveLocal.getText().equals("")){
					run();
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
					BackForthAgent bf = new BackForthAgent(saveLocal.getText() + DEFAULT_TRACE_EXTENSION);
					if (bf.parseFile()){
						bf.saveCaseBase(convertLocal.getText() + DEFAULT_CASEBASE_EXTENSION);
						System.out.println("DONE");
					}
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
		
		String agents[] = {ActionBasedAgent.class.getSimpleName(), InputBasedAgent.class.getSimpleName()};
		agentSelect = new JComboBox<String>(agents);
		panel.add(new JLabel("Agent:"));
		panel.add(agentSelect);
		
		panel.add(new JLabel("Cycles:"));
		iterArea = new JTextArea("" + DEFAULT_CYCLES);
		panel.add(iterArea);
		
		SpringUtilities.makeGrid(panel, 6, 2, DEFAULT_X_Y, DEFAULT_X_Y, DEFAULT_PADDING, DEFAULT_PADDING);
		return panel;
	}
}
