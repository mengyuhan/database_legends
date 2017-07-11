DROP TABLE Fight;
DROP TABLE Consists_of;
DROP TABLE Completes;
DROP TABLE Selects;
DROP TABLE Quest_DesignedBy_Rewards;
DROP TABLE Player;
DROP TABLE Master;
DROP TABLE Person;
DROP TABLE Enemy;
DROP TABLE Weapon;
DROP VIEW EnemyTotalFights;
DROP VIEW EnemySuccessFights;
DROP VIEW QuestSuccess;
DROP VIEW SuccessRate;
DROP VIEW SuccessRateWithWeapons;
DROP VIEW MasterDifficulty;
DROP VIEW Quest_Complete;
DROP VIEW Master_Complete;

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
HAVING f.result='Defeat';

CREATE VIEW QuestSuccess(title, success_rate) AS
       SELECT Q.title, ROUND(100*(Q.num_success/Q.num_attempts)) AS success_rate
       FROM Quest_DesignedBy_Rewards Q
       WHERE Q.num_attempts > 0;

CREATE VIEW SuccessRate AS
       SELECT uname, ((SUM(C.success)/COUNT(C.success)) * 100) AS SuccessRate
       FROM Completes C
       GROUP BY uname;

CREATE VIEW SuccessRateWithWeapons AS
       SELECT uname, wname, damage, SuccessRate
       FROM SuccessRate NATURAL JOIN Player NATURAL JOIN Weapon;

CREATE VIEW MasterDifficulty(uname, difficulty) AS
       SELECT uname, ROUND(AVG(100*num_success/num_attempts))
       FROM Quest_DesignedBy_Rewards
       WHERE num_attempts > 0
       GROUP BY uname;

CREATE VIEW Quest_Complete(title, num_complete) AS
       SELECT title, COUNT(*)
       FROM Completes
       GROUP BY title;

CREATE VIEW Master_Complete(uname, num_complete) AS
       SELECT Q.uname, SUM(QC.num_complete)
       FROM Quest_DesignedBy_Rewards Q, Quest_Complete QC
       WHERE Q.title=QC.title
       GROUP BY Q.uname;

INSERT INTO Enemy VALUES('Apollo', 80, 34);
INSERT INTO Enemy VALUES('Ares', 91, 21);
INSERT INTO Enemy VALUES('Zeus', 83, 34);
INSERT INTO Enemy VALUES('Poseidon', 98, 21);
INSERT INTO Enemy VALUES('Hermes', 83, 39);

INSERT INTO Weapon VALUES ('dagger', 20);
INSERT INTO Weapon VALUES ('sword', 50);
INSERT INTO Weapon VALUES ('club', 10);
INSERT INTO Weapon VALUES ('spear', 70);
INSERT INTO Weapon VALUES ('cross-bow', 90);

INSERT INTO Person VALUES('Herakles');
INSERT INTO Person VALUES('Odysseus');
INSERT INTO Person VALUES('Achilles');
INSERT INTO Person VALUES('Theseus');
INSERT INTO Person VALUES('Perseus');
INSERT INTO Person VALUES('Jason');
INSERT INTO Person VALUES('Bellerophon');
INSERT INTO Person VALUES('Orpheus');
INSERT INTO Person VALUES('Cadmus');
INSERT INTO Person VALUES('Atalanta');

INSERT INTO Player VALUES('Herakles', 5, 100, 'dagger');
INSERT INTO Player VALUES('Odysseus', 10, 100, 'sword');
INSERT INTO Player VALUES('Achilles', 15, 80, 'club');
INSERT INTO Player VALUES('Theseus', 20, 100, 'spear');
INSERT INTO Player VALUES('Perseus', 25, 100, 'cross-bow');

INSERT INTO Master VALUES('Jason', 20);
INSERT INTO Master VALUES('Bellerophon', 15);
INSERT INTO Master VALUES('Orpheus', 60);
INSERT INTO Master VALUES('Cadmus', 35);
INSERT INTO Master VALUES('Atalanta', 15);

INSERT INTO Fight VALUES('Herakles', 'Zeus', 'Victory');
INSERT INTO Fight VALUES('Odysseus', 'Zeus', 'Defeat');
INSERT INTO Fight VALUES('Achilles', 'Poseidon', 'Defeat');
INSERT INTO Fight VALUES('Theseus', 'Hermes', 'Victory');
INSERT INTO Fight VALUES('Perseus', 'Apollo', 'Defeat');

INSERT INTO Quest_DesignedBy_Rewards VALUES('Slay the Cyclops', 5, 4, 'Jason', 'dagger');
INSERT INTO Quest_DesignedBy_Rewards VALUES('Kill the Harpies', 4, 4, 'Orpheus', 'sword');
INSERT INTO Quest_DesignedBy_Rewards VALUES('Retrieve the Golden Fleece', 6, 3, 'Jason', 'sword');
INSERT INTO Quest_DesignedBy_Rewards VALUES('Slay Medusa', 2, 2, 'Atalanta', 'club');
INSERT INTO Quest_DesignedBy_Rewards VALUES('Steal Fire from the Gods', 7, 5, 'Bellerophon', 'spear');

INSERT INTO Consists_of VALUES('Slay the Cyclops', 'Apollo');
INSERT INTO Consists_of VALUES('Kill the Harpies', 'Poseidon');
INSERT INTO Consists_of VALUES('Retrieve the Golden Fleece', 'Zeus');
INSERT INTO Consists_of VALUES('Slay Medusa', 'Ares');
INSERT INTO Consists_of VALUES('Steal Fire from the Gods', 'Hermes');
INSERT INTO Consists_of VALUES('Slay the Cyclops', 'Poseidon');
INSERT INTO Consists_of VALUES('Slay the Cyclops', 'Zeus');
INSERT INTO Consists_of VALUES('Slay the Cyclops', 'Ares');
INSERT INTO Consists_of VALUES('Slay the Cyclops', 'Hermes');


INSERT INTO Selects VALUES('Herakles', 'Slay the Cyclops', '2015-06-8');
INSERT INTO Selects VALUES('Odysseus', 'Kill the Harpies', '2013-02-15');
INSERT INTO Selects VALUES('Achilles', 'Retrieve the Golden Fleece', '2016-04-2');
INSERT INTO Selects VALUES('Theseus', 'Slay Medusa', '2011-04-17');
INSERT INTO Selects VALUES('Perseus', 'Steal Fire from the Gods', '2001-07-27');

INSERT INTO Completes VALUES('Herakles', 'Slay the Cyclops', '2015-06-17', 1);
INSERT INTO Completes VALUES('Herakles', 'Kill the Harpies', '2015-06-17', 1);
INSERT INTO Completes VALUES('Herakles', 'Retrieve the Golden Fleece', '2015-06-17', 1);
INSERT INTO Completes VALUES('Herakles', 'Slay Medusa', '2015-06-17', 1);
INSERT INTO Completes VALUES('Herakles', 'Steal Fire from the Gods', '2015-06-17', 1);
INSERT INTO Completes VALUES('Odysseus', 'Kill the Harpies', '2013-03-01', 0);
INSERT INTO Completes VALUES('Achilles', 'Retrieve the Golden Fleece', '2016-04-18', 1);
INSERT INTO Completes VALUES('Theseus', 'Slay Medusa', '2011-04-21', 0);
INSERT INTO Completes VALUES('Perseus', 'Steal Fire from the Gods', '2001-09-15', 1);




