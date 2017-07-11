package com.databaselegends;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LegendDB {
	//constants for output formating
	private static final String FORMATSTRING = "%-15.15s";
	private static final String LONGFORMATSTRING = "%-35.35s";
	private static final String UNDERLINE = String.format("%-15.15s",
			                                 "============");
	private static final String LONGUNDERLINE = String.format("%-35.35s",
			                                   "======================");

	//constants for different url (local machine vs cs machine)
	private static final String localUrl = "jdbc:oracle:thin:@localhost:1522:ug";
	private static final String csUrl = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";

    private Connection con;

	private Statement statement;
  
    public LegendDB(String username, String password)
    {
        String connectURL = localUrl;;
        
        try {
	        // Load the Oracle JDBC driver
	        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		    con = DriverManager.getConnection(connectURL, username, password);

		    System.out.println("\nConnected to Oracle!");
		    
        } catch (SQLException ex) {
	        System.out.println("Message: " + ex.getMessage());
	        System.exit(-1);
        }
    }
    
    public void closeConnection() {
    	try {
    	    con.close();
    	    System.out.println("Closed connection to Oracle.");
    	} catch (SQLException ex) {
	        System.out.println("Message: " + ex.getMessage());
        }
    }

	public String registerMaster(String username) {
        if (username.equals("")) {
            return "Error - no input!\n";
        }

		String personQuery = "INSERT INTO Person VALUES ('" + username + "')";
		String masterQuery = "INSERT INTO Master VALUES ('" + username + "', 0)";

		try {
			statement = con.createStatement();

			ResultSet personResultSet = statement.executeQuery(personQuery);
			ResultSet masterResultSet = statement.executeQuery(masterQuery);

			return "success";
		} catch (SQLException e) {
            StringBuilder error = new StringBuilder("Error");

            int errorCode = e.getErrorCode();
            System.out.println(errorCode);
            switch(errorCode) {
                case (1):
                    error.append(" - master with that name already exists! Please enter a unique name and try again.");
                    break;
            }

			return error.toString();
		}
	}

	public String registerPlayer(String username) {
        if (username.equals("")) {
            return "Error - no input!";
        }

		String personQuery = "INSERT INTO Person VALUES ('" + username + "')";
		String playerQuery = "INSERT INTO Player VALUES ('" + username + "', 0, 100, 'club')";
		try {
			statement = con.createStatement();
			ResultSet personResultSet = statement.executeQuery(personQuery);
			ResultSet playerResultSet = statement.executeQuery(playerQuery);

			return "success";
		} catch (SQLException e) {
            StringBuilder error = new StringBuilder("Error");

            int errorCode = e.getErrorCode();
            System.out.println(errorCode);
            switch(errorCode) {
                case (1):
                    error.append(" - player with that name already exists! Please enter a unique name and try again.");
                    break;
            }

            return error.toString();
		}
	}

	public String signinUser(String username) {
        if (username.equals("")) {
            return "Error - no input!";
        }

		String playerQuery = "SELECT uname FROM Player WHERE uname='" + username + "'";
		String masterQuery = "SELECT uname FROM Master WHERE uname='" + username + "'";

		try {
			statement = con.createStatement();

			ResultSet playerResultSet = statement.executeQuery(playerQuery);

			StringBuilder message = new StringBuilder("player,");
			while (playerResultSet.next()) {
				message.append(playerResultSet.getString("uname"));
			}
			statement.close();

			if (message.toString().equals("player,")) {
				statement = con.createStatement();

				ResultSet masterResultSet = statement.executeQuery(masterQuery);
				message = new StringBuilder("master,");
				while (masterResultSet.next()) {
					message.append(masterResultSet.getString("uname"));
				}
			}

			return message.toString();
		} catch (SQLException e) {
			return "error";
		}
	}
	
	
	//generic method to take a query string and return the result
	public String runQuery(String queryString) {
    	Statement  stmt;
  	    ResultSet  rs;
  	    String field, attributeName;
  	    int numCols;
  	    StringBuilder output = new StringBuilder();
  	    ArrayList<String> attributes = new ArrayList<String>();
 
  	    try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        ResultSetMetaData rsmd = rs.getMetaData();

  	        // get number of columns
  	        numCols = rsmd.getColumnCount();
  	        
	        // add column names to first line of string, get list of attribute names
  	        for (int i = 0; i < numCols; i++) {
  	  	        attributeName = rsmd.getColumnName(i+1);
  	            attributes.add(attributeName);
  	            field = String.format(FORMATSTRING, attributeName);
  	            output.append(field); 
  	        }
  	        output.append('\n');
  	        
  	        //underline attribute names in output
	        for (int i = 0; i < numCols; i++) {
	  	            output.append(UNDERLINE);   
	  	    }
	  	    output.append('\n');  	        
  	        
	        while(rs.next()) {
  	            //add all attributes to a line in the string
	        	//format title differently because it is longer
	        	
	        	for (int i = 0; i < numCols; i++) {
  	                field = String.format(FORMATSTRING, rs.getString(attributes.get(i)));
  	                output.append(field);
	        	}
  	  	        output.append('\n');
  	        }
	        output.append('\n');
	        
  	        // close the statement; 
	        // the ResultSet will also be closed
	        stmt.close();
	    } catch (SQLException ex) {
	        output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
	    }	
  	  
  	  	return output.toString();
	}

    //generic method to take a query string and return the result with a custom message
    public String runQuery(String queryString, String customMessage) {
        Statement  stmt;
        ResultSet  rs;
        String field, attributeName;
        int numCols;
        StringBuilder output = new StringBuilder();
        ArrayList<String> attributes = new ArrayList<String>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(queryString);
            ResultSetMetaData rsmd = rs.getMetaData();

            // get number of columns
            numCols = rsmd.getColumnCount();

            // add column names to first line of string, get list of attribute names
            for (int i = 0; i < numCols; i++) {
                attributeName = rsmd.getColumnName(i+1);
                attributes.add(attributeName);
                field = String.format(FORMATSTRING, attributeName);
                output.append(field);
            }
            output.append('\n');

            //underline attribute names in output
            for (int i = 0; i < numCols; i++) {
                output.append(UNDERLINE);
            }
            output.append('\n');

            while(rs.next()) {
                //add all attributes to a line in the string
                //format title differently because it is longer

                for (int i = 0; i < numCols; i++) {
                    field = String.format(FORMATSTRING, rs.getString(attributes.get(i)));
                    output.append(field);
                }
                output.append('\n');
            }
            output.append('\n');

            output.append(customMessage);
            output.append('\n');

            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        } catch (SQLException ex) {
            output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
        }

        return output.toString();
    }
	
	//another generic method to take a query string and run it, but with
	//different formating for outputs with a long quest title at the start
	public String runQueryLong(String queryString) {
    	Statement  stmt;
  	    ResultSet  rs;
  	    String field, attributeName;
  	    int numCols;
  	    StringBuilder output = new StringBuilder();
  	    ArrayList<String> attributes = new ArrayList<String>();
  	    
  	    try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        ResultSetMetaData rsmd = rs.getMetaData();

  	        // get number of columns
  	        numCols = rsmd.getColumnCount();
  	        
	        // add column names to first line of string, get list of attribute names
  	        attributeName = rsmd.getColumnName(1);
 
  	        for (int i = 0; i < numCols; i++) {
  	  	        attributeName = rsmd.getColumnName(i+1);
  	            attributes.add(attributeName);
  	            field = String.format(LONGFORMATSTRING, attributeName);
  	            output.append(field); 
  	        }
  	        output.append('\n');

  	        //underline attribute names in output 
	        for (int i = 0; i < numCols; i++) {
	  	            output.append(LONGUNDERLINE);
	  	    }
	  	    output.append('\n');  	        
  	        
	        while(rs.next()) {
  	            //add all attributes to a line in the string
	        	for (int i = 0; i < numCols; i++) {
  	                field = String.format(LONGFORMATSTRING, rs.getString(attributes.get(i)));
  	                output.append(field);
	        	}
  	  	        output.append('\n');
  	        }
	        output.append('\n');
	        
  	        // close the statement; 
	        // the ResultSet will also be closed
	        stmt.close();
	    } catch (SQLException ex) {
	        output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
	    }	
  	  
  	  	return output.toString();
	}

    //another generic method to take a query string and run it, but with
    //different formating for outputs with a long quest title at the start
    //and do so with a custom message
    public String runQueryLong(String queryString, String customMessage) {
        Statement  stmt;
        ResultSet  rs;
        String field, attributeName;
        int numCols;
        StringBuilder output = new StringBuilder();
        ArrayList<String> attributes = new ArrayList<String>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(queryString);
            ResultSetMetaData rsmd = rs.getMetaData();

            // get number of columns
            numCols = rsmd.getColumnCount();

            // add column names to first line of string, get list of attribute names
            attributeName = rsmd.getColumnName(1);
            for (int i = 0; i < numCols; i++) {
                attributeName = rsmd.getColumnName(i+1);
                attributes.add(attributeName);
                field = String.format(LONGFORMATSTRING, attributeName);
                output.append(field);
            }
            output.append('\n');

            //underline attribute names in output
            for (int i = 0; i < numCols; i++) {
                output.append(LONGUNDERLINE);
            }
            output.append('\n');

            while(rs.next()) {
                //add all attributes to a line in the string
                //format title differently because it is longer
                for (int i = 0; i < numCols; i++) {
                    field = String.format(LONGFORMATSTRING, rs.getString(attributes.get(i)));
                    output.append(field);
                }
                output.append('\n');
            }
            output.append('\n');

            output.append(customMessage);
            output.append('\n');

            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        } catch (SQLException ex) {
            output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
        }

        return output.toString();
    }
    
    public String runQueryMedium(String queryString) {
        Statement  stmt;
        ResultSet  rs;
        String field, attributeName;
        int numCols;
        StringBuilder output = new StringBuilder();
        ArrayList<String> attributes = new ArrayList<String>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(queryString);
            ResultSetMetaData rsmd = rs.getMetaData();

            // get number of columns
            numCols = rsmd.getColumnCount();

            // add column names to first line of string, get list of attribute names
            //format title differently because it is longer
            attributeName = rsmd.getColumnName(1);
            attributes.add(attributeName);
            field = String.format(LONGFORMATSTRING, attributeName);
            output.append(field);
            for (int i = 1; i < numCols; i++) {
                attributeName = rsmd.getColumnName(i+1);
                attributes.add(attributeName);
                field = String.format(LONGFORMATSTRING, attributeName);
                output.append(field);
            }
            output.append('\n');

            //underline attribute names in output
            output.append(LONGUNDERLINE);
            for (int i = 1; i < numCols; i++) {
                output.append(LONGUNDERLINE);
            }
            output.append('\n');

            while(rs.next()) {
                //add all attributes to a line in the string
                //format title differently because it is longer
                field = String.format(LONGFORMATSTRING, rs.getString(attributes.get(0)));
                output.append(field);
                for (int i = 1; i < numCols; i++) {
                    field = String.format(LONGFORMATSTRING, rs.getString(attributes.get(i)));
                    output.append(field);
                }
                output.append('\n');
            }
            output.append('\n');
            
            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        } catch (SQLException ex) {
            output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
        }

        return output.toString();
    }

    //generic method to take a query string and return the result
    public String runUpdateInsertDelete(String[] queryStrings) {
        Statement  stmt;
        StringBuilder output = new StringBuilder();

        try {
            stmt = con.createStatement();
            for (int i=0; i<queryStrings.length; i++) {
                stmt.addBatch(queryStrings[i]);
            }
            stmt.executeBatch();
            stmt.close();

            output.append("");  //callers test for empty string for success!!
        } catch (SQLException ex) {
            output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
        }

        return output.toString();
    }

    //generic method to take a query string and return the result
    public String runUpdateInsertDelete(String[] queryStrings, String customMessage) {
        Statement  stmt;
        StringBuilder output = new StringBuilder();

        try {
            stmt = con.createStatement();
            for (int i=0; i<queryStrings.length; i++) {
                stmt.addBatch(queryStrings[i]);
            }
            stmt.executeBatch();
            stmt.close();

            output.append(customMessage);
            output.append("\n\n");
        } catch (SQLException ex) {
            output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
        }

        return output.toString();
    }
   
	//methods for setting queries called by UI actions
	
	// Master Functionality
	
    public String getQuests() {
  	    String queryString = "SELECT title, num_attempts, num_success "+
  	                         "FROM Quest_DesignedBy_Rewards";
  	    return runQueryMedium(queryString);
    }
    
    public String getAvgQuestSuccessRate() {
    	String queryString = "SELECT AVG(rate) AS avg_pct_success FROM " +
                             "(SELECT round(100*num_success/num_attempts) AS rate "+
    		                        "FROM Quest_DesignedBy_Rewards "+
    			                    "WHERE num_attempts <> 0)";
    	return runQuery(queryString);
    }
    
    public String getQuestsWithEnemyCount() {
    	String queryString = "SELECT title, count(ename) as enemy_count "+
                             "FROM Consists_of "+
    			             "GROUP BY title";
  	    return runQueryMedium(queryString);
    }
    
    public String getQuestsWithWeapon() {
    	String queryString = "SELECT q.title, w.wname " +
    			             "FROM Quest_DesignedBy_Rewards q, Weapon w " +
    			             "WHERE q.wname=w.wname";
  	    return runQueryMedium(queryString);
    }
    
    public String getQuestsThatHaveAllEnemies() {
    	String queryString = "SELECT q.title FROM Quest_DesignedBy_Rewards q "+
    			             "WHERE NOT EXISTS " +
                             "(SELECT e.ename FROM Enemy e " +
    			             "MINUS " +
                             "SELECT c.ename FROM Consists_of c " +
    			             "WHERE c.title = q.title)";
  	    return runQueryMedium(queryString);
    }
    
    public String getMastersQuests(String uName) {
    	if (!validateMaster(uName)) {
    		return "\n master name not recognized\n\n";
    	}
    	String queryString = "SELECT title, num_attempts, num_success "+
  	                         "FROM Quest_DesignedBy_Rewards " +
    			             "WHERE uname = '"+uName+"'";
  	    return runQueryMedium(queryString);
    }
    
    public String getWeapons() {
 
  	    String queryString = "SELECT * FROM Weapon";
  	    return runQuery(queryString);
    }
    
    public String getEnemies() {
    	
  	    String queryString = "SELECT * FROM Enemy";
  	    return runQuery(queryString);
    }
    public String getBestEnemies() {
    	String queryString = 
    	    "SELECT ename, successRate FROM "+
    			"(SELECT t.ename, ROUND(100*(s.successFights/t.totalFights)) AS successRate "+
    	        "FROM EnemyTotalFights t, EnemySuccessFights s "+
    		    "WHERE t.ename = s.ename "+
    		    "UNION "+
    	        "SELECT t.ename, 0 AS successRate " +
    		    "FROM EnemyTotalFights t WHERE t.ename NOT IN "+
    		    "(SELECT s.ename FROM EnemySuccessFights s)) "+
    	    "ORDER BY successRate DESC";
    	return runQuery(queryString);
                           
    }
    
    public String getEnemiesForQuest(String questTitle) {
    	if (!validateQuest(questTitle)) {
    		return "\n quest name not recognized\n\n";
    	}
    	String queryString = "SELECT e.ename, ehealth, epower "+
    	                     "FROM Consists_of c, Enemy  e "+
    	                     "WHERE c.ename=e.ename AND title='"+questTitle+"'";
    	return runQuery(queryString);
    }
    
    public String getPlayers() {
    	
  	    String queryString = "SELECT * FROM Player order by pscore desc";
  	    return runQuery(queryString);
    }
    
    public String createQuest(String uName, String questTitle, 
    		                   String questWeapon, String questEnemy) {
    	
    	if (!validateMaster(uName)) {
    		return "\n master name not recognized, quest not created\n\n";
    	}
       	if (!validateWeapon(questWeapon)) {
    		return "\n weapon not recognized, quest not created\n\n";
    	}
      	if (!validateEnemy(questEnemy)) {
    		return "\n enemy not recognized, quest not created\n\n";
    	}

        String[] queryStrings = new String[2];
    	queryStrings[0] = "INSERT INTO Quest_DesignedBy_Rewards VALUES ('"+
            questTitle.trim()+"', 0, 0, '"+uName+"', '"+questWeapon+"')";
    	queryStrings[1] = "INSERT INTO Consists_of VALUES ('"+
    			              questTitle+"', '"+questEnemy+"')";
    	
        return runUpdateInsertDelete(queryStrings, "Quest created succsfully!");
    }
    
    public String removeQuest(String uName, String questTitle) {
    	if (!validateMaster(uName)) {
    		return "\n master name not recognized, nothing deleted\n\n";
    	}
    	if (!validateQuest(questTitle)) {
    		return "\n quest name not recognized, nothing deleted\n\n";
    	}
    	if (!validateQuestsMaster(uName, questTitle)) {
    		return "\n you did not create this quest, cannot delete it\n\n";
    	}
    	
       	Statement  stmt;
  	    StringBuilder output = new StringBuilder();
    	
    	String queryString = "DELETE FROM Quest_DesignedBy_Rewards "+
    	                     "WHERE title = '"+questTitle+"'";  
 	    try {
	        stmt = con.createStatement();
	        int count = stmt.executeUpdate(queryString);
	        output.append(String.valueOf(count)+" rows deleted\n\n.");
 	    	stmt.close();
 	    } catch (SQLException ex) {
	        output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
 	    }
  	  	return output.toString();
    }
    
    public String addEnemy(String uName, String questTitle, String questEnemy) {
    	if (!validateMaster(uName)) {
    		return "\n master name not recognized, enemy not added to quest\n\n";
    	}
    	if (!validateQuest(questTitle)) {
    		return "\n quest name not recognized, enemy not added to quest\n\n";
    	}
      	if (!validateEnemy(questEnemy)) {
    		return "\n enemy not recognized, enemy not added to quest\n\n";
    	}
    	if (!validateQuestsMaster(uName, questTitle)) {
    		return "\n you did not create this quest, cannot add enemy\n\n";
    	}
    	if (enemyExists(questTitle, questEnemy)) {
    		return "\n enemy already exists for this quest, cannot add it\n\n";
    	}
    	
    	String queryString = "INSERT INTO Consists_of VALUES ('"+
	                         questTitle+"', '"+questEnemy+"')";

        Statement  stmt;
        StringBuilder output = new StringBuilder();
        
        try {
 	    	 stmt = con.createStatement();
 	         int count = stmt.executeUpdate(queryString);
 	         output.append(String.valueOf(count)+" rows inserted\n\n.");
 	    	 stmt.close();
        } catch (SQLException ex) {
            output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
        }
    	return output.toString();
    }
    
    public String removeEnemy(String uName, String questTitle, String questEnemy) {
       	if (!validateMaster(uName)) {
    		return "\n master name not recognized, enemy not removed from quest\n\n";
    	}
    	if (!validateQuest(questTitle)) {
    		return "\n quest name not recognized, enemy not removed from quest\n\n";
    	}
      	if (!validateEnemy(questEnemy)) {
    		return "\n enemy not recognized, enemy not removed from quest\n\n";
    	}
    	if (!validateQuestsMaster(uName, questTitle)) {
    		return "\n you did not create this quest, cannot remove enemy\n\n";
    	}
    	if (!validateNumEnemies(questTitle)) {
    		return "\n only one enemy left in quest, cannot remove enemy\n\n";
    	}
    	
       	String queryString = "DELETE FROM Consists_of "+
                             "WHERE title = '"+questTitle+"' AND " +
       			                   "ename = '"+questEnemy+"'";  
       	
       	Statement  stmt;
  	    StringBuilder output = new StringBuilder();
  	    
        try {
            stmt = con.createStatement();
            int count = stmt.executeUpdate(queryString);
            output.append(String.valueOf(count)+" rows deleted\n\n.");
            stmt.close();
        } catch (SQLException ex) {
            output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
        }
        return output.toString();
    }

    public String bestMaster() {
        String queryString = "SELECT MD.uname, MD.difficulty, MC.num_complete "+
                             "FROM MasterDifficulty MD, Master_Complete MC "+
        		             "WHERE MD.uname=MC.uname ORDER BY MD.difficulty DESC, MC.num_complete DESC";

        return runQueryMedium(queryString);
    }

    public String totalEnemyPower(String questName) {
        String queryString = "SELECT C.title, SUM(epower) AS totalEnemyPower "+
                             "FROM Enemy E, Consists_of C "+
        		             "WHERE C.title='" + questName + "' AND C.ename=E.ename "+
                             "GROUP BY C.title";

        return runQueryMedium(queryString);
    }
    
    // Update Master name
    public String Master_UpdateName(String currentMasterName, String newMasterName)
    {
    	  //first check if new name already exists
  	    if (validateMaster(newMasterName) || validatePlayer(newMasterName)) {
  	    	return "\n person of that name already exists, cannot update\n\n";
  	    }

    	String[] queryStrings = new String[4];
    	queryStrings[0] = "INSERT INTO Person Values('"+newMasterName+"')";
    	queryStrings[1] = "INSERT INTO Master SELECT '"+newMasterName+"', mscore "+
    	                  "FROM Master WHERE uname='"+currentMasterName+"'";
        queryStrings[2] = "UPDATE Quest_DesignedBy_Rewards SET uname = '" + newMasterName + 
    			          "' WHERE uname = '" + currentMasterName + "'";
    	queryStrings[3] = "DELETE FROM Person WHERE uname = '" + currentMasterName + "'";

        return runUpdateInsertDelete(queryStrings);
    }
    
    public String getPlayersWhoCompleteAllQuests() {
//       	String queryString = "SELECT * FROM Player p"
//       			+ "WHERE NOT EXISTS "
//       			//all quests
//       			+ "(SELECT q.title FROM Quest_DesignedBy_Rewards q WHERE NOT EXISTS "
//       			//quests completed by player p
//       			+ "(SELECT c.uname FROM Completes c WHERE c.uname=p.uname AND c.title=q.title))";
    	
    	String queryString = "SELECT * FROM Player p WHERE NOT EXISTS (SELECT q.title FROM Quest_DesignedBy_Rewards q WHERE NOT EXISTS (SELECT c.uname FROM Completes c WHERE c.uname=p.uname AND c.title=q.title))";
       	return runQuery(queryString);
    } 
    
    public String getBestPlayers() {
        String queryString = "SELECT uname, sum(success) as num_of_success FROM Completes group by uname order by sum(success) desc";
   	return runQuery(queryString);
} 
    
    public String updateAllPlayerHealth() {
       	String queryString = "UPDATE Player SET phealth = 100";  
       	
       	Statement  stmt;
  	    StringBuilder output = new StringBuilder();
  	    
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(queryString);
            output.append(" All players Healed!\n\n.");
            stmt.close();
        } catch (SQLException ex) {
            output.append("\n\n Error Message: " + ex.getMessage()+"\n\n");
        }
        return output.toString();
    }
    
    // Player Functionality

    // Update Player name
    public String Player_UpdateName(String currentPlayerName, String newPlayerName)
	{
  	    if (validateMaster(newPlayerName) || validatePlayer(newPlayerName)) {
  	    	return "\n person of that name already exists, cannot update\n\n";
  	    }

  	   	String[] queryStrings = new String[6];
    	queryStrings[0] = "INSERT INTO Person Values('"+newPlayerName+"')";
    	queryStrings[1] = "INSERT INTO Player "+
    	                  "SELECT '"+newPlayerName+"', pscore, phealth, wname "+
    	                  "FROM Player WHERE uname='"+currentPlayerName+"'";
        queryStrings[2] = "UPDATE Fight SET uname = '" + newPlayerName + 
    			          "' WHERE uname = '" + currentPlayerName + "'";
        queryStrings[3] = "UPDATE Selects SET uname = '" + newPlayerName + 
		                  "' WHERE uname = '" + currentPlayerName + "'";
        queryStrings[4] = "UPDATE Completes SET uname = '" + newPlayerName + 
		                  "' WHERE uname = '" + currentPlayerName + "'";
    	queryStrings[5] = "DELETE FROM Person WHERE uname = '" + currentPlayerName + "'";

        return runUpdateInsertDelete(queryStrings);
    }
    
    //Resets Player Health to full (100)
    public String Player_Rest(String playerName)
    {
  	    String[] queryString = new String[1];
        queryString[0] = "UPDATE Player SET phealth = 100 WHERE uname = '" + playerName + "'";

  	    return runUpdateInsertDelete(queryString, "Rest Complete -- Health at 100");
    }

    //Returns past Quests the Player has already completed whether they succeeded or failed
    public String Player_ViewPastQuests(String playerName)
    {
        String queryString = "SELECT title, endDate, success FROM Completes WHERE uname = '" + playerName + "'";
        
        return runQueryLong(queryString);
    }
    
    //Returns past Fights the Player has completed and whether they succeeded or failed
    public String Player_ViewPastFights(String playerName)
    {
        String queryString = "SELECT ename, result FROM Fight WHERE uname = '" + playerName + "'";
        
        return runQueryLong(queryString);
    }
    
    //Returns the Player's current Weapon and it's damage statistic
    public String Player_ViewWeapon(String playerName)
    {
        String queryString = "SELECT W.wname, W.damage FROM Weapon W, Player P WHERE P.uname = '" + playerName + "' AND P.wname = W.wname";
        
        return runQueryLong(queryString);
    }
    
    //Returns a list of Quests that the Player has not attempted yet
    public String Player_ViewAvailableQuests(String playerName)
    {
        String queryString = "SELECT Q.title, Q.wname AS REWARD FROM Quest_DesignedBy_Rewards Q WHERE Q.title NOT IN (SELECT C.title FROM Completes C WHERE uname = '" + playerName + "')";
        
        return runQueryLong(queryString);
    }
    
    //Given a valid quest title, adds a new tuple to Selects with the given Player and Quest
    public String Player_SelectQuest(String playerName, String questName)
    {
        if (questName.equals("")) {
            return "Error - no input!\n\n";
        }
      if(!validateQuest(questName))
      {
        return "Invalid Quest title -- please enter a valid Quest title.\n\n";
      }

        if (validQuestPreviouslySelected(questName, playerName)) {
            return "Error - quest already previously selected!\n\n";
        }
      
      String[] queryStrings = new String[2];
      queryStrings[0] = "INSERT INTO Selects VALUES ('" + playerName.trim() +"', '" +
            questName.trim() + "', DATE '"+ getDate() +"')";
      queryStrings[1] = "UPDATE Quest_DesignedBy_Rewards SET num_attempts = num_attempts + 1 WHERE title = '" + questName.trim() +"'";
      
        return runUpdateInsertDelete(queryStrings, "Quest -- " + questName.trim() + " -- selected on " + getDate() + ".  Good luck, " + playerName.trim() + "!");
    }
    
    //Finds all available Quests for the given Player and joins all of them, adding new tuples to Selects
    public String Player_JoinAllAvailableQuests(String playerName)
    {
    	Statement  stmt;
        ResultSet  rs;
        ArrayList<String> quests = new ArrayList<String>();
        StringBuilder output = new StringBuilder();
        
        String queryString = "SELECT Q.title, Q.wname AS REWARD FROM Quest_DesignedBy_Rewards Q WHERE Q.title NOT IN (SELECT C.title FROM Completes C WHERE uname = '" + playerName + "')";
        
        try 
        {
          stmt = con.createStatement();
          rs = stmt.executeQuery(queryString);
          ResultSetMetaData rsmd = rs.getMetaData();

          while(rs.next()) 
          {
            quests.add(String.format(LONGFORMATSTRING, rs.getString(rsmd.getColumnName(1))));
            }

          stmt.close();
        } 
        catch (SQLException ex) 
        {
        	return "\n\n Error Message: " + ex.getMessage()+"\n\n";
        }
        
        for (int i = 0, max = quests.size(); i < max; ++i)
        {
        	output.append(Player_SelectQuest(playerName, quests.get(i)));
        	output.append("\n");
        }
      
        return output.toString();
    }
    
    //Returns information on current Player, including their Quest success rate
    public String Player_ViewSelf(String playerName)
    {
    	StringBuilder output = new StringBuilder();
    	String queryStringPlayer = "SELECT * FROM Player WHERE uname = '" + playerName + "'";
    	String queryStringSuccessRate = "SELECT ((SUM(C.success)/COUNT(C.success)) * 100) AS SuccessRate FROM Completes C WHERE C.uname = '" + playerName + "'";
      
    	output.append(runQuery(queryStringPlayer));
    	output.append(runQuery(queryStringSuccessRate));
    	
    	return output.toString();
    }
    
    public String Player_ViewEasiestAvailableQuests(String playerName)
    {
        String queryString = "SELECT title, S.success_rate AS difficulty FROM Quest_DesignedBy_Rewards Q NATURAL JOIN QuestSuccess S WHERE title NOT IN (SELECT S.title FROM Selects S WHERE S.uname = '" + playerName + "') ORDER BY difficulty ASC";

        return runQueryLong(queryString);
    }
    
    public String Player_ViewHardestAvailableQuests(String playerName)
    {
        String queryString = "SELECT title, S.success_rate AS difficulty FROM Quest_DesignedBy_Rewards Q NATURAL JOIN QuestSuccess S WHERE title NOT IN (SELECT S.title FROM Selects S WHERE S.uname = '" + playerName + "') ORDER BY difficulty DESC";

        return runQueryLong(queryString);
    }
    
    public String Player_ViewSuccessOfSimilarHeroes(String playerName)
    {
    	String queryString = "SELECT uname, wname, SuccessRate FROM SuccessRateWithWeapons WHERE damage <= (SELECT damage*1.15 FROM Player NATURAL JOIN Weapon WHERE uname='" + playerName + "') AND damage >= (SELECT damage*0.85 FROM Player NATURAL JOIN Weapon WHERE uname='" + playerName + "')";
    	
    	return runQueryLong(queryString);
    }
    
    //helper methods for validating requests
    
    public boolean validateMaster(String uName) {
    	Statement  stmt;
  	    ResultSet  rs;
  	    String queryString = "SELECT uname FROM master WHERE uname='"+uName+"'";
  	    boolean uNamePresent = false;
  	    
  	    try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        if (rs.next()) {
	        	uNamePresent = true;
	        }
  	    } catch(SQLException ex) {
  	    	uNamePresent = false;
	        System.out.println("Error Message: " + ex.getMessage()+"\n\n");
  	    }
  	    return uNamePresent;
    }
    
    public boolean validatePlayer(String uName) {
    	Statement  stmt;
  	    ResultSet  rs;
  	    String queryString = "SELECT uname FROM player WHERE uname='"+uName+"'";
  	    boolean uNamePresent = false;
  	    
  	    try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        if (rs.next()) {
	        	uNamePresent = true;
	        }
  	    } catch(SQLException ex) {
  	    	uNamePresent = false;
	        System.out.println("Error Message: " + ex.getMessage()+"\n\n");
  	    }
  	    return uNamePresent;
    }
    
    public boolean validateWeapon(String wName) {
       	Statement  stmt;
  	    ResultSet  rs;
  	    String queryString = "SELECT wname FROM weapon WHERE wname='"+wName+"'";
  	    boolean wNamePresent = false;
  	    
  	    try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        if (rs.next()) {
	        	wNamePresent = true;
	        }
  	    } catch(SQLException ex) {
  	    	wNamePresent = false;
	        System.out.println("Error Message: " + ex.getMessage()+"\n\n");
  	    }
  	    return wNamePresent;
    }
    
    public boolean validateEnemy(String eName) {
       	Statement  stmt;
  	    ResultSet  rs;
  	    String queryString = "SELECT ename FROM enemy WHERE ename='"+eName+"'";
  	    boolean eNamePresent = false;
  	    
  	    try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        if (rs.next()) {
	        	eNamePresent = true;
	        }
  	    } catch(SQLException ex) {
  	    	eNamePresent = false;
	        System.out.println("Error Message: " + ex.getMessage()+"\n\n");
  	    }
  	    return eNamePresent;
    }	
    
    public boolean validateQuest(String title) {
       	Statement  stmt;
  	    ResultSet  rs;
  	    String queryString = "SELECT title FROM Quest_DesignedBy_Rewards "+
  	                         "WHERE title='"+title+"'";
  	    boolean titlePresent = false;
  	    
  	    try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        if (rs.next()) {
	        	titlePresent = true;
	        }
  	    } catch(SQLException ex) {
  	    	titlePresent = false;
	        System.out.println("Error Message: " + ex.getMessage()+"\n\n");
  	    }
  	    return titlePresent;
    }

    public boolean validQuestPreviouslySelected(String title, String playerName) {
        Statement  stmt;
        ResultSet  rs;
        String queryString = "SELECT title FROM Selects "+
                "WHERE title='"+title+"' AND uname='" + playerName + "'";
        boolean titlePresent = false;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(queryString);
            if (rs.next()) {
                titlePresent = true;
            }
        } catch(SQLException ex) {
            
            System.out.println("Error Message: " + ex.getMessage()+"\n\n");
        }
        return titlePresent;
    }
    
    public boolean validateQuestsMaster(String uName, String title) {
       	Statement  stmt;
  	    ResultSet  rs;
  	    String queryString = "SELECT title FROM Quest_DesignedBy_Rewards "+
  	                         "WHERE title='"+title+"' AND uname='"+uName+"'";
  	    boolean valid = false;
  	    
  	    try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        if (rs.next()) {
	        	valid = true;
	        }
  	    } catch(SQLException ex) {
  	    	valid = false;
	        System.out.println("Error Message: " + ex.getMessage()+"\n\n");
  	    }
  	    return valid;
    }	
    
    public boolean validateNumEnemies(String questTitle) {
    	Statement  stmt;
  	    ResultSet  rs;
  	    
  	    //get result set of enemies
        String queryString = "SELECT ename "+
                             "FROM Consists_of WHERE title='"+questTitle+"'";
  	    boolean enemyPresent = false;

    	try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        //get first enemy, then see if there is another
	        rs.next();
	        if (rs.next()) {
	        	enemyPresent = true;
	        }
  	    } catch(SQLException ex) {
  	    	enemyPresent = false;
	        System.out.println("Error Message: " + ex.getMessage()+"\n\n");
  	    }
  	    return enemyPresent;
    }
    
    public boolean enemyExists(String questTitle, String questEnemy) {
       	Statement  stmt;
  	    ResultSet  rs;
  	    
  	    //get result set of enemies
    	String queryString = "SELECT ename "+
                             "FROM Consists_of WHERE title='"+questTitle+"'";
  	    boolean enemyPresent = false;
  	    String ename;

    	try {
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        //see if enemy to be added is in list of enemies for this quest
	        while (rs.next()) {
	        	ename = rs.getString("ename");
	        	if (ename.toLowerCase().equals(questEnemy)) {
	        	    enemyPresent = true;
	        	    break;
	        	}
	        }
  	    } catch(SQLException ex) {
  	    	enemyPresent = false;
	        System.out.println("Error Message: " + ex.getMessage()+"\n\n");
  	    }
  	    return enemyPresent;
    }

    public String getDate()
    {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, 1);
      SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

      return formatDate.format(cal.getTime());
    }
}

