package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import agent.ActionBasedAgent;
import agent.InputBasedAgent;
import agent.StateBasedAgent;

import sandbox.Direction;
import sandbox.MovementAction;
import sandbox.Sandbox;

public class SandboxTraceGUI {
	
	public static void main(String args[]){
		@SuppressWarnings("unused")
		SandboxTraceGUI gui = new SandboxTraceGUI();
	}
	
	private JFrame frame;
	private JTextArea recordOutput;
	
	private JButton forwardButton;
	private JButton lTurnButton;
	private JButton rTurnButton;
	private JButton backButton;
	
	private JTextArea saveLocal;
	
	private JTextArea xArea;
	private JTextArea yArea;
	private JComboBox<Direction> dirArea;
	private JComboBox<Integer> gridSize;
	private JComboBox<String> agentSelect;
	private JTextArea iterArea;
	
	public static final int MAX_SIZE = 9;
	public static final int DEFAULT_PADDING = 5;
	public static final int DEFAULT_X_Y = 5;
	public static final String DEFAULT_DELIMITER = "|";
	
	public SandboxTraceGUI(){
		frame = new JFrame("");
		frame.setLayout(new BorderLayout());
		
		frame.add(locationPanel(), BorderLayout.NORTH);
		
//		JPanel panel = new JPanel();
//		panel.setLayout(new SpringLayout());
//		panel.add(movePanel());
//		panel.add(recordPanel());
//		SpringUtilities.makeGrid(panel, 1, 2, 0, 0, 0, 0);
//		frame.add(panel, BorderLayout.CENTER);
		
		frame.add(confimPanel(), BorderLayout.SOUTH);
		
		frame.setJMenuBar(buildMenu());
		
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private JMenuBar buildMenu(){
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenuItem openItem = new JMenuItem("Open");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		openItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
				int returnVal = chooser.showOpenDialog((Component) e.getSource());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       load(chooser.getSelectedFile());
			    }
			}
		});
		menu.add(openItem);
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		saveItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		menu.add(saveItem);
		
		return menuBar;
	}
	
	private void load(File file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			if (line != null){
				String tokens[] = line.split("\\|");
				
				int x = Integer.parseInt(tokens[0]);
				xArea.setText("" + x);
				int y = Integer.parseInt(tokens[1]);
				yArea.setText("" + y);
				
				Direction d = Direction.valueOf(tokens[2]);
				dirArea.setSelectedItem(d);
				
				int size = Integer.parseInt(tokens[3]);
				gridSize.setSelectedIndex(new Integer(size));
			}else{
				br.close();
				return;
			}
			
			recordOutput.setText("");
			while(line != null){
				if (line.isEmpty()){
					continue;
				}
				
				String tokens[] = line.split("\\|");
				
				MovementAction action = MovementAction.valueOf(tokens[5]);
				recordOutput.append(action.toString() + "\n");
				
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		saveLocal.setText(file.getName());
		
		setEnableMovePanel(true);
		setEnableInitalStatPanel(false);
		
	}
	
	private void save(){
		String saveFile = this.saveLocal.getText();
		if (saveFile.isEmpty()){
			return;
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(saveFile));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		int x = Integer.parseInt(this.xArea.getText());
		int y = Integer.parseInt(this.yArea.getText());
		Direction dir = this.dirArea.getItemAt(this.dirArea.getSelectedIndex());
		int size = this.gridSize.getItemAt(this.gridSize.getSelectedIndex()).intValue();
		int grid[][] = new int[size][size];
		
		Scanner s = new Scanner(recordOutput.getText());
		while(s.hasNext()){
			String line = s.nextLine();
			MovementAction action = MovementAction.valueOf(line);
			
			String out = x + DEFAULT_DELIMITER + y + DEFAULT_DELIMITER + dir.name() + DEFAULT_DELIMITER + size + DEFAULT_DELIMITER;
			for (int i = 0; i < grid.length; i++){
				for (int j = 0; j < grid[0].length; j++){
					if (i == 3){
						grid[i][j] = 1;
					}
					out += grid[i][j] + ",";
				}
			}
			out += DEFAULT_DELIMITER + action.name();
			switch(action){
			case FORWARD:
				switch(dir){
				case NORTH:
					if (grid[Math.max(0, x - 1)][y] == 0){
						x = Math.max(0, x - 1);
					}
					break;
				case SOUTH:
					if (grid[Math.min(grid.length - 1, x + 1)][y] == 0){
						x = Math.min(grid.length - 1, x + 1);
					}
					break;
				case EAST:
					if (grid[x][Math.min(grid[0].length - 1, y + 1)] == 0){
						y = Math.min(grid[0].length - 1, y + 1);
					}
					break;
				case WEST:
					if (grid[x][Math.max(0, y - 1)] == 0){
						y = Math.max(0, y - 1);
					}
					break;
				}
				break;
			case BACKWARD:
				switch(dir){
				case SOUTH:
					if (grid[Math.max(0, x - 1)][y] == 0){
						x = Math.max(0, x - 1);
					}
					break;
				case NORTH:
					if (grid[Math.min(grid.length - 1, x + 1)][y] == 0){
						x = Math.min(grid.length - 1, x + 1);
					}
					break;
				case WEST:
					if (grid[x][Math.min(grid[0].length - 1, y + 1)] == 0){
						y = Math.min(grid[0].length - 1, y + 1);
					}
					break;
				case EAST:
					if (grid[x][Math.max(0, y - 1)] == 0){
						y = Math.max(0, y - 1);
					}
					break;
				}
				break;
			case TURN_LEFT:
				dir = Direction.values()[(dir.ordinal() - 1) % Direction.values().length];
				break;
			case TURN_RIGHT:
				dir = Direction.values()[(dir.ordinal() + 1) % Direction.values().length];
				break;
			}
			
			try {
				writer.append(out + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		s.close();
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void run(){
		int size = this.gridSize.getItemAt(this.gridSize.getSelectedIndex()).intValue();
		String agent = this.agentSelect.getItemAt(this.agentSelect.getSelectedIndex());
		int iterations = Integer.parseInt(this.iterArea.getText());
		StateBasedAgent a = null;
		if (agent.equals(ActionBasedAgent.class.getSimpleName())){
			a = new ActionBasedAgent(size);
		}else if (agent.equals(InputBasedAgent.class.getSimpleName())){
			a = new InputBasedAgent(size);
		}
		if (a == null){
			return;
		}
		String saveFile = this.saveLocal.getText();
		if (saveFile == null || saveFile.isEmpty()){
			return;
		}
		a.runAgent(iterations);
		a.saveTrace(saveFile);
		System.out.println("DONE");
	}
	
	@SuppressWarnings("unused")
	private JPanel recordPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());
		recordOutput = new JTextArea("");
		recordOutput.setEditable(false);
		panel.add(new JScrollPane(recordOutput, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
		SpringUtilities.makeGrid(panel, 1, 1, 0, 0, DEFAULT_PADDING * 2, DEFAULT_PADDING * 2);
		return panel;
	}
	
	@SuppressWarnings("unused")
	private JPanel movePanel(){
		JPanel panel = new JPanel();
		
		panel.setLayout(new SpringLayout());
		
		panel.add(new JPanel()); // Top Left
		
		forwardButton = new JButton("Forward");
		panel.add(forwardButton);
		forwardButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (forwardButton.isEnabled()){
					recordOutput.append(MovementAction.FORWARD.toString() + "\n");
				}
			}
		});
		
		panel.add(new JPanel()); // Top Right
		
		lTurnButton = new JButton("L-Turn");
		panel.add(lTurnButton);
		lTurnButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lTurnButton.isEnabled()){
					recordOutput.append(MovementAction.TURN_LEFT.toString() + "\n");
				}
			}
		});
		
		panel.add(new JPanel()); // Middle
		
		rTurnButton = new JButton("R-Turn");
		panel.add(rTurnButton);
		rTurnButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rTurnButton.isEnabled()){
					recordOutput.append(MovementAction.TURN_RIGHT.toString() + "\n");
				}
			}
		});
		
		panel.add(new JPanel()); // Bottom Left
		
		backButton = new JButton("Back");
		panel.add(backButton);
		backButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (backButton.isEnabled()){
					recordOutput.append(MovementAction.BACKWARD.toString() + "\n");
				}
			}
		});
		
		panel.add(new JPanel()); // Bottom Right
		
		SpringUtilities.makeGrid(panel, 3, 3, DEFAULT_X_Y, DEFAULT_X_Y, DEFAULT_PADDING, DEFAULT_PADDING);
		setEnableMovePanel(false);
		return panel;
	}
	
	private void setEnableMovePanel(boolean isEnabled){
		forwardButton.setEnabled(isEnabled);
		lTurnButton.setEnabled(isEnabled);
		rTurnButton.setEnabled(isEnabled);
		backButton.setEnabled(isEnabled);
	}
	
	private void setEnableInitalStatPanel(boolean isEnabled){
		xArea.setEnabled(isEnabled);
		yArea.setEnabled(isEnabled);
		dirArea.setEnabled(isEnabled);
		gridSize.setEnabled(isEnabled);
	}
	
	private JPanel confimPanel(){
		JPanel panel = new JPanel();
		
		saveLocal = new JTextArea();
		JButton runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (saveLocal.getText() != null || !saveLocal.getText().equals("")){
					run();
				}
			}
		});
		
		panel.setLayout(new SpringLayout());
		
		panel.add(runButton);
		panel.add(saveLocal);
		
		SpringUtilities.makeGrid(panel, 1, 2, DEFAULT_X_Y, DEFAULT_X_Y, DEFAULT_PADDING, DEFAULT_PADDING);
		
		return panel;
	}
	
	private JPanel locationPanel(){
		JPanel panel = new JPanel();
		
		panel.setLayout(new SpringLayout());
		
		xArea = new JTextArea("0");
		yArea = new JTextArea("0");
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
		iterArea = new JTextArea("1");
		panel.add(iterArea);
		
		JButton initButton = new JButton("Init Agent");
		panel.add(initButton);
		initButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnableInitalStatPanel(false);
				setEnableMovePanel(true);
			}
		});
		initButton.setEnabled(false);
		
		JButton clearButton = new JButton("Clear");
		panel.add(clearButton);
		clearButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				xArea.setText("0");
				yArea.setText("0");
				dirArea.setSelectedIndex(0);
				gridSize.setSelectedIndex(0);
				
				setEnableInitalStatPanel(true);
				setEnableMovePanel(false);
				
				recordOutput.setText("");
			}
		});
		clearButton.setEnabled(false);
		
		SpringUtilities.makeGrid(panel, 7, 2, DEFAULT_X_Y, DEFAULT_X_Y, DEFAULT_PADDING, DEFAULT_PADDING);
		return panel;
	}
}
