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
INSERT INTO Player VALUES('Achilles', 15, 100, 'club');
INSERT INTO Player VALUES('Theseus', 20, 100, 'spear');
INSERT INTO Player VALUES('Perseus', 25, 100, 'cross-bow');

INSERT INTO Master VALUES('Jason', 20);
INSERT INTO Master VALUES('Bellerophon', 15);
INSERT INTO Master VALUES('Orpheus', 60);
INSERT INTO Master VALUES('Cadmus', 35);
INSERT INTO Master VALUES('Atalanta', 15);

INSERT INTO Fight VALUES('Herakles', 'Zeus', 'Victory');
INSERT INTO Fight VALUES('Odysseus', 'Zeus', 'Victory');
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
INSERT INTO Completes VALUES('Odysseus', 'Kill the Harpies', '2013-03-01', 0);
INSERT INTO Completes VALUES('Achilles', 'Retrieve the Golden Fleece', '2016-04-18', 1);
INSERT INTO Completes VALUES('Theseus', 'Slay Medusa', '2011-04-21', 0);
INSERT INTO Completes VALUES('Perseus', 'Steal Fire from the Gods', '2001-09-15', 1);

