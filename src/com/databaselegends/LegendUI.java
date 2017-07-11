package com.databaselegends;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LegendUI {

	//database connection
	LegendDB legendDB;
	
	//constants
	private static final Integer APPWIDTH = 1100;
	private static final Integer APPHEIGHT = 750;
	private static final Integer HEADERFONTSIZE = 24;
	private static final Integer FONTSIZE = 14;
	private static final Integer LABELHEIGHT = 35;
	private static final Integer LABELWIDTH = 200;
	private static final Integer TEXTHEIGHT = 24;
	private static final Integer TEXTWIDTH = 500;
	private static final Integer NOT_SIGNED_IN = 0;
	private static final Integer SIGNED_IN_MASTER = 1;
	private static final Integer SIGNED_IN_PLAYER = 2;
	
	//control variables
	private Integer userStatus = NOT_SIGNED_IN;
	private String uName = "";


	// GUI components
	//application
	private JFrame applicationFrame;
	private JLayeredPane layeredPane;
	private JPanel signinView;
	private JPanel masterView;
	private JPanel playerView;
	
	//sign in view
	private JTextField userNameTextField;
	private JButton masterButton;
	private JButton playerButton;
	private JButton signinButton;
	private JTextArea signinOutput;

	//master view
	private JTextField masterNameTextField;
	private JTextField questTitleTextField;
	private JTextField questWeaponTextField;
	private JTextField questEnemyTextField;
	private JComboBox<String> getQuestsDropDown;
	private JButton getWeaponsButton;
	private JComboBox<String> getEnemiesDropDown;
	private JComboBox<String> getPlayersDropDown;
	private JButton updateMasterButton;
	private JButton bestMasterButton;
	private JComboBox<String> createRemoveQuestDropDown;
	private JComboBox<String> addRemoveEnemyDropDown;
	private JTextArea masterOutput;
	private JButton logoutButtonMaster;
	private JLabel masterLabel;

	
	//player view
	private JTextField playerNameTextField;
	private JTextField playerInputTextField;
	private JComboBox<String> playerQueriesDropDown;
	private JTextArea playerOutput;
	private JButton logoutButtonPlayer;
	private JButton restButton;
	private JButton updatePlayerButton;
	private JButton selectQuestButton;
	private JButton joinAllAvailableQuestsButton;
	private JLabel playerLabel;
	
	// Launch the application.
	public static void main(String[] args) {
		LegendUI window = new LegendUI();
		window.applicationFrame.setVisible(true);
	}
	
	//create the application and connect to database
	public LegendUI() {
		
		String databaseUsername;
		String databasePassword;
		
		// Create the wrapper for the application
		initializeApplicationFrame();

		//input dialog to get username and password for database connection
		databaseUsername = (String) JOptionPane.showInputDialog(
				applicationFrame, 
				"Enter username for connecting to database.",
				"Database Login", JOptionPane.PLAIN_MESSAGE, null, null, "username");
		databasePassword = (String) JOptionPane.showInputDialog(
				applicationFrame, 
				"Enter password for connecting to database.",
				"Database Login", JOptionPane.PLAIN_MESSAGE, null, null, "password");
		
		//instantiate database, make connection
	    legendDB = new LegendDB(databaseUsername, databasePassword);
	}
	
	
	private void initializeApplicationFrame() {
		//set up application wrapper
		applicationFrame = new JFrame();
		applicationFrame.setSize(new Dimension(APPWIDTH, APPHEIGHT));
		applicationFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//center the frame
	    Dimension d = applicationFrame.getToolkit().getScreenSize();
	    Rectangle r = applicationFrame.getBounds();
	    applicationFrame.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

		//on window close
		applicationFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame) e.getSource();
				System.out.println("exiting");
				legendDB.closeConnection();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
		
		//set up the different views
		initSigninView();
		initMasterView();
		initPlayerView();
		
		layeredPane = new JLayeredPane();
		layeredPane.add(signinView, 1);
		layeredPane.add(masterView, 2);
		layeredPane.add(playerView, 3);
		
		applicationFrame.getContentPane().add(layeredPane);
		applicationFrame.repaint();
	}
	
	//methods to layout components of each view
	
	private void initSigninView() {
		// make top box
		signinView = new JPanel();
		signinView.setSize(new Dimension(APPWIDTH, APPHEIGHT));
		signinView.setBackground(new Color(64,64,64));
		signinView.setVisible(true);
		
		JPanel signinContainer = new JPanel();
		signinContainer.setSize(new Dimension(APPWIDTH, APPHEIGHT));
		signinContainer.setLayout(new BoxLayout(signinContainer, BoxLayout.Y_AXIS));
		
		JLabel signinLabel = new JLabel("LEGENDS: Sign In ", JLabel.CENTER);
		signinLabel.setPreferredSize(new Dimension(LABELWIDTH, LABELHEIGHT));
		signinLabel.setFont(new Font("Times New Roman", Font.BOLD, HEADERFONTSIZE));
		signinLabel.setForeground(new Color(64, 64, 64));
		signinLabel.setBorder(new EmptyBorder(15, 5, 15, 5));
		signinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		signinContainer.add(signinLabel);
		
		userNameTextField = new JTextField("");
		userNameTextField.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		userNameTextField.setPreferredSize(new Dimension(TEXTWIDTH, TEXTHEIGHT));
		signinContainer.add(userNameTextField);
		
		JPanel createContainer = new JPanel();
		createContainer.setLayout(new BoxLayout(createContainer, BoxLayout.X_AXIS));
		masterButton = new JButton("Create New Master");
		masterButton.setForeground(new Color(32, 32, 96));
		playerButton = new JButton("Create New Player");
		playerButton.setForeground(new Color(96, 32, 32));
		signinButton = new JButton("Sign In");
		signinButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createContainer.add(masterButton);
		createContainer.add(playerButton);
		signinContainer.add(createContainer);
		
		signinContainer.add(signinButton);
		signinView.add(signinContainer);

		JPanel areaImageContainer = new JPanel();
		areaImageContainer.setLayout(new BoxLayout(areaImageContainer, BoxLayout.X_AXIS));
		JLabel imageLabelLeft = 
				new JLabel(new ImageIcon("src/com/databaselegends/dragon-battle_left.jpg"));
		areaImageContainer.add(imageLabelLeft);

		signinOutput = new JTextArea(30, 55);
		signinOutput.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(signinOutput);
		areaImageContainer.add(scrollPane);
		
		JLabel imageLabelRight = 
				new JLabel(new ImageIcon("src/com/databaselegends/dragon-battle_right.jpg"));
		areaImageContainer.add(imageLabelRight);
		
		signinContainer.add(areaImageContainer);

		masterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get user name that was entered and validate
				uName = userNameTextField.getText().trim();
                masterLabel.setText("Master - " + uName);

				String message = legendDB.registerMaster(uName);

				if (message.equals("success")) {
					//if valid and change to corresponding view
					signinView.setVisible(false);
					masterView.setVisible(true);
					userStatus = SIGNED_IN_MASTER;
				} else {
					signinOutput.setText(message);
				}
			}
		});

		playerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get user name that was entered and validate
				uName = userNameTextField.getText().trim();
                playerLabel.setText("Player - " + uName);

				String message = legendDB.registerPlayer(uName);

				if (message.equals("success")) {
					//if valid change to corresponding view
					signinView.setVisible(false);
					playerView.setVisible(true);
					userStatus = SIGNED_IN_PLAYER;
				} else {
					signinOutput.setText(message);
				}
			}
		});

		signinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get user name that was entered and validate
				uName = userNameTextField.getText().trim();

				String message = legendDB.signinUser(uName);

				if (!message.equals("error")) {
					//if valid and change to corresponding view
					String[] parts = message.split(",");
					if (parts.length < 2) {
						signinOutput.setText("Error - credentials not valid, please try again.\n");
						return;
					}
					
					signinView.setVisible(false);

					uName = parts[1];
					

					if (parts[0].equals("player")) {
						playerLabel.setText("Player - " + uName);
						playerView.setVisible(true);
					} else {
						masterLabel.setText("Master - " + uName);
						masterView.setVisible(true);
					}
					userStatus = SIGNED_IN_MASTER;
				} else {
					signinOutput.setText("Error - credentials not valid, please try again.\n");
				}
			}
		});
	}
	
	private void initMasterView() {
		// make top box
		masterView = new JPanel();
		masterView.setSize(new Dimension(APPWIDTH, APPHEIGHT));
		masterView.setBackground(new Color(32,32,96));
		masterView.setVisible(false);
		
		JPanel masterContainer = new JPanel();
		masterContainer.setLayout(new BoxLayout(masterContainer, BoxLayout.Y_AXIS));
		masterContainer.setBackground(new Color(120,120,250));;

		masterLabel = new JLabel("Master", JLabel.CENTER);

		masterLabel.setFont(new Font("Helvetica", Font.BOLD, HEADERFONTSIZE));
		masterLabel.setForeground(new Color(32,32,96));
		masterLabel.setBorder(new EmptyBorder(15, 5, 15, 5));
		masterContainer.add(masterLabel);

		//top row of text fields
		JPanel topTextFieldContainer = new JPanel();
		topTextFieldContainer.setLayout(new BoxLayout(topTextFieldContainer,
                                          BoxLayout.X_AXIS));
		
		JPanel masterNameContainer = new JPanel();
		masterNameContainer.setLayout(new BoxLayout(masterNameContainer,
				                        BoxLayout.Y_AXIS));
		JLabel masterNameLabel = new JLabel("New Master Name", JLabel.CENTER);
		masterNameLabel.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		masterNameTextField = new JTextField();
		masterNameTextField.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		masterNameContainer.add(masterNameLabel);
		masterNameContainer.add(masterNameTextField);
		
		JPanel questTitleContainer = new JPanel();
		questTitleContainer.setLayout(new BoxLayout(questTitleContainer,
				                        BoxLayout.Y_AXIS));
		JLabel questTitleLabel = new JLabel("Quest Title", JLabel.CENTER);
		questTitleLabel.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		questTitleTextField = new JTextField();
		questTitleTextField.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		questTitleContainer.add(questTitleLabel);
		questTitleContainer.add(questTitleTextField);
		
		topTextFieldContainer.add(masterNameContainer);
		topTextFieldContainer.add(questTitleContainer);
		masterContainer.add(topTextFieldContainer);
		
	    //bottom row of text fields
		JPanel bottomTextFieldContainer = new JPanel();
		bottomTextFieldContainer.setLayout(new BoxLayout(bottomTextFieldContainer,
                                          BoxLayout.X_AXIS));
		
		JPanel questEnemyContainer = new JPanel();
		questEnemyContainer.setLayout(new BoxLayout(questEnemyContainer,
				                        BoxLayout.Y_AXIS));
		JLabel questEnemyLabel = new JLabel("Quest Enemy", JLabel.CENTER);
		questEnemyLabel.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		questEnemyTextField = new JTextField();
		questEnemyTextField.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		questEnemyContainer.add(questEnemyLabel);
		questEnemyContainer.add(questEnemyTextField);
		
		JPanel questWeaponContainer = new JPanel();
		questWeaponContainer.setLayout(new BoxLayout(questWeaponContainer,
				                        BoxLayout.Y_AXIS));
		JLabel questWeaponLabel = new JLabel("Quest Weapon", JLabel.CENTER);
		questWeaponLabel.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		questWeaponTextField = new JTextField();
		questWeaponTextField.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		questWeaponContainer.add(questWeaponLabel);
		questWeaponContainer.add(questWeaponTextField);
		
		bottomTextFieldContainer.add(questEnemyContainer);
		bottomTextFieldContainer.add(questWeaponContainer);
		masterContainer.add(bottomTextFieldContainer);
	
		//top row of combo box and buttons
		JPanel masterButtonContainer = new JPanel();
		masterButtonContainer.setLayout(new BoxLayout(masterButtonContainer,
				                       BoxLayout.X_AXIS));
		
		String[] questQueries = {"Show All Quests",
                                 "Show Average Quest Success Rate",
                                 "Show All Quests With Weapon",
				                 "Show All Quests With Enemy Count",
				                 "Show Quests That Have All Enemies",
				                 "Show Masters Quests"
				                 };
		getQuestsDropDown = new JComboBox<String>(questQueries);
		getQuestsDropDown.setSelectedIndex(0);
		
		getWeaponsButton = new JButton("Show All Weapons");
		String[] enemyQueries = {"Show All Enemies" ,
				                 "Show Best Enemies",
				                 "Show Enemies For Quest",
				                 "Show Total Enemy Power"};
		getEnemiesDropDown = new JComboBox<String>(enemyQueries);
		
		String[] playerQueries = {
								"Show All Players",
								"Show Players Who Completed All Quests",
								"Show Best Players",
								"Heal All Players"
									
		};
		getPlayersDropDown = new JComboBox<String>(playerQueries);
		

		masterButtonContainer.add(getQuestsDropDown);
		masterButtonContainer.add(getWeaponsButton);
		masterButtonContainer.add(getEnemiesDropDown);
		masterButtonContainer.add(getPlayersDropDown);
		
		masterContainer.add(masterButtonContainer);
		
		//2nd row of buttons and drop downs
		JPanel masterButtonContainer2 = new JPanel();
		masterButtonContainer2.setLayout(new BoxLayout(masterButtonContainer2,
                BoxLayout.X_AXIS));
		updateMasterButton = new JButton("Update Master");
		bestMasterButton = new JButton("Show Best Master");
		String [] createRemoveQuestQueries = {"Create Quest",
				                              "Remove Quest"};
		createRemoveQuestDropDown = new JComboBox<String>(createRemoveQuestQueries);
		String [] addRemoveEnemyQueries = {"Add Enemy To Quest",
                                           "Remove Enemy From Quest"};
		addRemoveEnemyDropDown = new JComboBox<String>(addRemoveEnemyQueries);
		masterButtonContainer2.add(updateMasterButton);
		masterButtonContainer2.add(bestMasterButton);
		masterButtonContainer2.add(createRemoveQuestDropDown);
		masterButtonContainer2.add(addRemoveEnemyDropDown);
		
		masterContainer.add(masterButtonContainer2);

		//output text area
		masterOutput = new JTextArea(22,95);
		masterOutput.setEditable(false);
		masterOutput.setFont(new Font("Courier", Font.PLAIN, FONTSIZE));
		JScrollPane masterScrollPane = new JScrollPane(masterOutput);
		masterContainer.add(masterScrollPane);
		
		//logout button
		logoutButtonMaster = new JButton("Logout");
		masterContainer.add(logoutButtonMaster);

		masterView.add(masterContainer);
		
		// action listeners - calls to corresponding DB method
		getQuestsDropDown.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String questQueryName = (String)cb.getSelectedItem();
		        String output;
		        
		        switch (questQueryName) {
		            case "Show All Quests":
		            	output = legendDB.getQuests();
		            	masterOutput.append(output);
		            	break;
		            case "Show Average Quest Success Rate":
		            	output = legendDB.getAvgQuestSuccessRate();
		            	masterOutput.append(output);
		            	break;
		            case "Show All Quests With Weapon":
		            	output = legendDB.getQuestsWithWeapon();
		            	masterOutput.append(output);
		            	break;
		            case "Show All Quests With Enemy Count":
		            	output = legendDB.getQuestsWithEnemyCount();
		            	masterOutput.append(output);
		            	break;
		            case "Show Quests That Have All Enemies":
		            	output = legendDB.getQuestsThatHaveAllEnemies();
		            	masterOutput.append(output);
		            	break;
		            case "Show Masters Quests":
		            	output = legendDB.getMastersQuests(uName.trim());
		            	masterOutput.append(output);
		            	break;
		        }
		    }
		});
		
		getWeaponsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				String weapons = legendDB.getWeapons();
				masterOutput.append(weapons);
			}
		});
		
		getEnemiesDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String enemyQueryName = (String)cb.getSelectedItem();
		        String output;
		        String questTitle;
		        
		        switch (enemyQueryName) {
		            case "Show All Enemies":
		            	output = legendDB.getEnemies();
						masterOutput.append(output);
		        	    break;
		            case "Show Best Enemies":
		            	output = legendDB.getBestEnemies();
		            	masterOutput.append(output);
		            	break;
		            case "Show Enemies For Quest":
				        questTitle = questTitleTextField.getText().trim();
				        if (questTitle.length() == 0) {
				        	masterOutput.append("Make sure the Quest Title " +          
                			                "is entered in the text field\n\n");
                	        return;
				        }
		            	output = legendDB.getEnemiesForQuest(questTitle);
		            	masterOutput.append(output);
		            	break;
		            case "Show Total Enemy Power":
				        questTitle = questTitleTextField.getText().trim();
				        if (questTitle.length() == 0) {
				        	masterOutput.append("Make sure the Quest Title " +          
                			                "is entered in the text field\n\n");
                	        return;
				        }
		            	output = legendDB.totalEnemyPower(questTitle);
		            	masterOutput.append(output);
		            	break;
		        }
			}
		});
		
		getPlayersDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String playerQueryName = (String)cb.getSelectedItem();
		        String output;
		        
		        switch (playerQueryName) {
		            case "Show All Players":
		            	output = legendDB.getPlayers();
						masterOutput.append(output);
		        	    break;
		            case "Show Players Who Completed All Quests":
		            	output = legendDB.getPlayersWhoCompleteAllQuests();
		            	masterOutput.append(output);
		            	break;	
		            case "Heal All Players":
		            	output = legendDB.updateAllPlayerHealth();
		            	masterOutput.append(output);
		            	break; 
		            case "Show Best Players":
		            	output = legendDB.getBestPlayers();
						masterOutput.append(output);
		        	    break;
		        }
			}
		});
		

		updateMasterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String newMasterName = masterNameTextField.getText().trim();
				if (newMasterName.length() == 0) {
					masterOutput.append("Enter a new Master name in the text field\n\n");
				    return;
				}
				
	            String output = legendDB.Master_UpdateName(uName.trim(), newMasterName);
	            if (output.length() == 0) {
	            	//ok
	            	masterOutput.append("Master name " + uName.trim() + 
	            			 " was updated successfully to " + newMasterName + "!\n\n");
	     	        uName = newMasterName;
	            } else {
	                //error message
	                masterOutput.append(output);
	            }
			}
		});
		
		bestMasterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String output = legendDB.bestMaster();
				masterOutput.append(output);
			}
		});
		
		createRemoveQuestDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		    	JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String questQueryName = (String)cb.getSelectedItem();
		        String questTitle;
		        String questWeapon;
		        String questEnemy;
		        String response;
		        
		        switch (questQueryName) {
		            case "Create Quest":
	            	    
				        questTitle = questTitleTextField.getText().trim();
				        questWeapon = questWeaponTextField.getText().trim();
				        questEnemy = questEnemyTextField.getText().trim();

                        if ((questTitle.length() == 0) || 
                	        (questWeapon.length() == 0) ||
                	        (questEnemy.length() == 0)) {
                	        masterOutput.append("Make sure the Quest Title, " +
                	                "Quest Weapon, and Quest Enemy \n"+ 
                			        "are entered in the text fields\n\n");
                	        return;
                        }
                
                        response = legendDB.createQuest(uName.trim(), 
                		             questTitle, questWeapon, questEnemy);
				        masterOutput.append(response);  
				        break;
		            case "Remove Quest":
				        questTitle = questTitleTextField.getText().trim();
				        if (questTitle.length() == 0) {
				        	 masterOutput.append("Make sure the Quest Title " +
	                			        "is entered in the text field\n\n");
				        }
				        response = legendDB.removeQuest(uName.trim(),questTitle);
			            masterOutput.append(response);
		            	break;
		        }
			}
		});
		
		addRemoveEnemyDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		    	JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String questQueryName = (String)cb.getSelectedItem();
		        String questTitle;
		        String questEnemy;
		        String response;
		        
		        switch (questQueryName) {
		            case "Add Enemy To Quest":
				        questTitle = questTitleTextField.getText().trim();
				        questEnemy = questEnemyTextField.getText().trim();

                        if ((questTitle.length() == 0) || 
                	        (questEnemy.length() == 0)) {
                	        masterOutput.append("Make sure the Quest Title " +
                	                "and Quest Enemy \n"+ 
                			        "are entered in the text fields\n\n");
                	        return;
                        }
                
                        response = legendDB.addEnemy(uName.trim(),
                        		                     questTitle, questEnemy);
				        masterOutput.append(response);  
		            	break;
		            case "Remove Enemy From Quest":
				        questTitle = questTitleTextField.getText().trim();
				        questEnemy = questEnemyTextField.getText().trim();

                        if ((questTitle.length() == 0) || 
                	        (questEnemy.length() == 0)) {
                	        masterOutput.append("Make sure the Quest Title " +
                	                "and Quest Enemy \n"+ 
                			        "are entered in the text fields\n\n");
                	        return;
                        }
                
                        response = legendDB.removeEnemy(uName.trim(),
                        		                        questTitle, questEnemy);
				        masterOutput.append(response);  
		            	break;
		        }
			}
		});

		logoutButtonMaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//change to corresponding view
				signinView.setVisible(true);
				masterView.setVisible(false);
				userStatus = NOT_SIGNED_IN;
				uName = "";
				masterOutput.setText("");
				masterNameTextField.setText("");
				questTitleTextField.setText("");
				questWeaponTextField.setText("");
				questEnemyTextField.setText("");
			}
		});
	}
	
	private void initPlayerView() {
		// make top box
		playerView = new JPanel();
		playerView.setSize(new Dimension(APPWIDTH, APPHEIGHT));
		playerView.setBackground(new Color(96,32,32));
		playerView.setVisible(false);
		
		JPanel playerContainer = new JPanel();
		playerContainer.setLayout(new BoxLayout(playerContainer, BoxLayout.Y_AXIS));
		playerContainer.setBackground(new Color(220,160,160));;

		playerLabel = new JLabel("Player", JLabel.CENTER);

		playerLabel.setFont(new Font("Helvetica", Font.BOLD, HEADERFONTSIZE));
		playerLabel.setForeground(new Color(96,32,32));
		playerLabel.setBorder(new EmptyBorder(15, 5, 15, 5));
		playerContainer.add(playerLabel);

		//top row of text fields
		JPanel topTextFieldContainer = new JPanel();
		topTextFieldContainer.setLayout(new BoxLayout(topTextFieldContainer,
                                          BoxLayout.X_AXIS));
		
		JPanel playerNameContainer = new JPanel();
		playerNameContainer.setLayout(new BoxLayout(playerNameContainer,
				                        BoxLayout.Y_AXIS));
		JLabel playerNameLabel = new JLabel("New Player Name", JLabel.CENTER);
		playerNameLabel.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		playerNameTextField = new JTextField();
		playerNameTextField.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		playerNameContainer.add(playerNameLabel);
		playerNameContainer.add(playerNameTextField);
		
		JPanel playerInputContainer = new JPanel();
		playerInputContainer.setLayout(new BoxLayout(playerInputContainer,
				                        BoxLayout.Y_AXIS));
		JLabel playerInputLabel = new JLabel("Player Input: ", JLabel.CENTER);
		playerInputLabel.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		playerInputTextField = new JTextField();
		playerInputTextField.setFont(new Font("Helvetica", Font.PLAIN, FONTSIZE));
		playerInputContainer.add(playerInputLabel);
		playerInputContainer.add(playerInputTextField);
		
		topTextFieldContainer.add(playerNameContainer);
		topTextFieldContainer.add(playerInputContainer);
		playerContainer.add(topTextFieldContainer);
	
		//top row of combo box and buttons
		JPanel playerButtonContainer = new JPanel();
		playerButtonContainer.setLayout(new BoxLayout(playerButtonContainer,
				                       BoxLayout.X_AXIS));
		
		String[] playerQueries = {	"Show Self",
									"Show Equipped Weapon",
									"Show Past Quests",
									"Show Past Fights",
									"Show Available Quests",
									"Show Easiest Quests",
									"Show Hardest Quests",
				                 	"Show Success of Similar Heroes"};
		playerQueriesDropDown = new JComboBox<String>(playerQueries);
		playerQueriesDropDown.setSelectedIndex(0);
		
		restButton = new JButton("Rest");
		selectQuestButton = new JButton("Select Quest");
		joinAllAvailableQuestsButton = new JButton("Join All Available Quests");
		updatePlayerButton = new JButton("Update Player");
		
		playerButtonContainer.add(updatePlayerButton);
		playerButtonContainer.add(playerQueriesDropDown);
		playerButtonContainer.add(restButton);
		playerButtonContainer.add(selectQuestButton);
		playerButtonContainer.add(joinAllAvailableQuestsButton);
		
		playerContainer.add(playerButtonContainer);

		//output text area
		playerOutput = new JTextArea(22,95);
		playerOutput.setEditable(false);
		playerOutput.setFont(new Font("Courier", Font.PLAIN, FONTSIZE));
		JScrollPane playerScrollPane = new JScrollPane(playerOutput);
		playerContainer.add(playerScrollPane);
		
		//logout button
		logoutButtonPlayer = new JButton("Logout");
		playerContainer.add(logoutButtonPlayer);

		playerView.add(playerContainer);
		
		// action listeners - calls to corresponding DB method
		playerQueriesDropDown.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String playerQueryName = (String)cb.getSelectedItem();
		        String output;
		        
		        switch (playerQueryName) {
		            case "Show Past Quests":
		            	output = legendDB.Player_ViewPastQuests(uName);
		            	playerOutput.append(output);
		            	break;
		            case "Show Past Fights":
		            	output = legendDB.Player_ViewPastFights(uName);
		            	playerOutput.append(output);
		            	break;
		            case "Show Equipped Weapon":
		            	output = legendDB.Player_ViewWeapon(uName);
		            	playerOutput.append(output);
		            	break;
		            case "Show Available Quests":
		            	output = legendDB.Player_ViewAvailableQuests(uName);
		            	playerOutput.append(output);
		            	break;
		            case "Show Self":
		            	output = legendDB.Player_ViewSelf(uName);
		            	playerOutput.append(output);
		            	break;
		            case "Show Easiest Quests":
		            	output = legendDB.Player_ViewEasiestAvailableQuests(uName);
		            	playerOutput.append(output);
		            	break;
		            case "Show Hardest Quests":
		            	output = legendDB.Player_ViewHardestAvailableQuests(uName);
		            	playerOutput.append(output);
		            	break;
		            case "Show Success of Similar Heroes":
		            	output = legendDB.Player_ViewSuccessOfSimilarHeroes(uName);
		            	playerOutput.append(output);
		            	break;
		        }
		    }
		});
		
		
		updatePlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

                // update player name
                String newPlayerName = playerNameTextField.getText().trim();
				if (newPlayerName.length() == 0) {
					playerOutput.append("Enter a new Player name in the text field!\n\n");
				    return;
				}

				String output = legendDB.Player_UpdateName(uName.trim(), newPlayerName);
				if (output.length() == 0) {
					//ok
	            	playerOutput.append("Player name " + uName.trim() + 
	            			 " was updated successfully to " + newPlayerName + "!\n\n");
	     	        uName = newPlayerName;				
	     	    } else {
					//error message
	                playerOutput.append(output);
				}		
			}
		});
		
		restButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ret = legendDB.Player_Rest(uName);
				playerOutput.append(ret);
			}
		});
		
		selectQuestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = playerInputTextField.getText();
				String ret = legendDB.Player_SelectQuest(uName, input);
				playerOutput.append(ret);
			}
		});
		
		joinAllAvailableQuestsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ret = legendDB.Player_JoinAllAvailableQuests(uName);
				playerOutput.append(ret);
			}
		});
		

		logoutButtonPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//change to corresponding view
				signinView.setVisible(true);
				playerView.setVisible(false);
				userStatus = NOT_SIGNED_IN;
				uName = "";
				playerOutput.setText("");
				playerNameTextField.setText("");
				playerInputTextField.setText("");
			}
		});
	}
	
}
