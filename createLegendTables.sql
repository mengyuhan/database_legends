CREATE TABLE Enemy (
       ename CHAR(64) PRIMARY KEY,
       ehealth INTEGER,
       epower INTEGER,
       CHECK (ehealth >=0 AND ehealth <=100),
       CHECK (epower >=0 AND epower <=100)
);

CREATE TABLE Weapon (
       wname CHAR(64) PRIMARY KEY,
       damage INTEGER,
       CHECK (damage >=0 AND damage <=100)
);

CREATE TABLE Person (
       uname CHAR(64) PRIMARY KEY
);

CREATE TABLE Player (
       uname CHAR(64) PRIMARY KEY,
       pscore INTEGER,
       phealth INTEGER,
       wname CHAR(64) NOT NULL,
       FOREIGN KEY (uname) REFERENCES Person(uname)
       	       ON DELETE CASCADE,
       FOREIGN KEY (wname) REFERENCES Weapon(wname)
       	       ON DELETE CASCADE,
       CHECK (pscore >=0 AND pscore <=100),
       CHECK (phealth >=0 AND phealth <=100)    
);

CREATE TABLE Master (
       uname CHAR(64) PRIMARY KEY,
       mscore INTEGER,
       FOREIGN KEY (uname) REFERENCES Person(uname) ON DELETE CASCADE,
       CHECK (mscore >=0 AND mscore <=100)
);

CREATE TABLE Fight (
       uname CHAR(64),
       ename CHAR(64),
       result CHAR(64),
       PRIMARY KEY (uname, ename),
       FOREIGN KEY (uname) REFERENCES Player(uname)
       	       ON DELETE CASCADE,
       FOREIGN KEY (ename) REFERENCES Enemy(ename)
       	       ON DELETE CASCADE
);

CREATE TABLE Quest_DesignedBy_Rewards (
       title CHAR(64) PRIMARY KEY,
       num_attempts INTEGER,
       num_success INTEGER,
       uname CHAR(64) NOT NULL,
       wname CHAR(64),
       FOREIGN KEY (uname) REFERENCES Master(uname)
       	       ON DELETE CASCADE,
       FOREIGN KEY (wname) REFERENCES Weapon(wname)
       	       ON DELETE CASCADE,
       CHECK (num_attempts >= num_success)
);

CREATE TABLE Consists_of (
       title CHAR(64), 
       ename CHAR(64),
       PRIMARY KEY (title, ename),
       FOREIGN KEY (title) REFERENCES Quest_DesignedBy_Rewards(title)
       	       ON DELETE CASCADE,
       FOREIGN KEY (ename) REFERENCES Enemy(ename)
       	       ON DELETE CASCADE
);

CREATE TABLE Selects ( 
       uname CHAR(64), 
       title CHAR(64),
       startDate DATE,
       PRIMARY KEY(uname, title),
       FOREIGN KEY(uname) REFERENCES Player(uname)
       	       ON DELETE CASCADE,
       FOREIGN KEY(title) REFERENCES Quest_DesignedBy_Rewards(title)
       	       ON DELETE CASCADE
);

CREATE TABLE Completes (
       uname CHAR(64), 
       title CHAR(64),
       endDate DATE,
       success INTEGER,
       PRIMARY KEY(uname, title),
       FOREIGN KEY(uname) REFERENCES Player(uname)
       	       ON DELETE CASCADE,
       FOREIGN KEY(title) REFERENCES Quest_DesignedBy_Rewards(title)
       	       ON DELETE CASCADE,
       CHECK (success = 0 OR success = 1)
);

CREATE VIEW EnemyTotalFights(ename, totalFights) AS 
SELECT f.ename, count(f.result)
FROM Fight f
GROUP BY f.ename;

CREATE VIEW EnemySuccessFights(ename, successFights) AS
SELECT f.ename, count(f.result)
FROM Fight f
GROUP BY f.ename, f.result
HAVING f.result='Defeat'


CREATE VIEW QuestSuccess(title, success_rate) AS
       SELECT Q.title, Q.num_success/Q.num_attempts AS success_rate
       FROM Quest_DesignedBy_Rewards Q
       WHERE Q.num_attempts > 0;

CREATE VIEW SuccessRate AS
       SELECT uname, ((SUM(C.success)/COUNT(C.success)) * 100) AS SuccessRate
       FROM Completes C
       GROUP BY uname;

CREATE VIEW SuccessRateWithWeapons AS
       SELECT uname, wname, damage, SuccessRate
       FROM SuccessRate NATURAL JOIN Player NATURAL JOIN Weapon;

