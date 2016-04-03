CREATE TABLE USER
(
  Real_name VARCHAR(50) NOT NULL,
  Profile_name VARCHAR(25) NOT NULL,
  Credit_card CHAR(16),
  Level INT NOT NULL,
  Phone CHAR(10),
  Id INT NOT NULL,
  PRIMARY KEY (Id),
  CONSTRAINT chk_LevelNotLowerThan1 CHECK(Level > 0)
);

CREATE TABLE GAME
(
  Id INT NOT NULL,
  Name VARCHAR(25) NOT NULL,
  Price FLOAT NOT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE DEVELOPER
(
  Name VARCHAR(50) NOT NULL,
  Website VARCHAR(75) NOT NULL,
  PRIMARY KEY (Name)
);

CREATE TABLE CRITIC_REVIEW
(
  Company VARCHAR(100) NOT NULL,
  Link VARCHAR(100) NOT NULL,
  Rating FLOAT NOT NULL,
  Text TEXT NOT NULL,
  Game_id INT NOT NULL,
  PRIMARY KEY (Link),
  FOREIGN KEY (Game_id) REFERENCES GAME(Id)
);

CREATE TABLE USER_GROUP
(
  Name VARCHAR(25) NOT NULL,
  Id INT NOT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE FRIEND
(
  User1 INT NOT NULL,
  User2 INT NOT NULL,
  PRIMARY KEY (User1, User2),
  FOREIGN KEY (User1) REFERENCES USER(Id),
  FOREIGN KEY (User2) REFERENCES USER(Id),
  CONSTRAINT chk_CantFriendSelf CHECK(User1 <> User2),
  CONSTRAINT chk_User1HasLowerId CHECK(User1 < User2)
);

CREATE TABLE MEMBERSHIP
(
  Group_id INT NOT NULL,
  User_id INT NOT NULL,
  PRIMARY KEY (Group_id, User_id),
  FOREIGN KEY (Group_id) REFERENCES USER_GROUP(Id),
  FOREIGN KEY (User_id) REFERENCES USER(Id)
);

CREATE TABLE OWNS
(
  Game_id INT NOT NULL,
  User_id INT NOT NULL,
  PRIMARY KEY (Game_id, User_id),
  FOREIGN KEY (Game_id) REFERENCES GAME(Id),
  FOREIGN KEY (User_id) REFERENCES USER(Id)
);

CREATE TABLE DEVELOPS
(
  Name VARCHAR(50) NOT NULL,
  Id INT NOT NULL,
  PRIMARY KEY (Name, Id),
  FOREIGN KEY (Name) REFERENCES DEVELOPER(Name),
  FOREIGN KEY (Id) REFERENCES GAME(Id)
);

CREATE TABLE USER_REVIEW
(
  Rating FLOAT NOT NULL,
  Text TEXT NOT NULL,
  Game_id INT NOT NULL,
  User_id INT NOT NULL,
  PRIMARY KEY (Game_id, User_id),
  FOREIGN KEY (Game_id) REFERENCES GAME(Id),
  FOREIGN KEY (User_id) REFERENCES USER(Id)
);

CREATE TABLE CATEGORY
(
  Id INT NOT NULL,
  Name VARCHAR(25) NOT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE GAME_CATEGORY
(
  Category_id INT NOT NULL,
  Game_id INT NOT NULL,
  PRIMARY KEY (Category_id, Game_id),
  FOREIGN KEY (Category_id) REFERENCES CATEGORY(Id),
  FOREIGN KEY (Game_id) REFERENCES GAME(Id)
);

.mode csv
.import initial-data/game.csv game
.import initial-data/user_group.csv user_group
.import initial-data/developer.csv developer
.import initial-data/membership.csv membership
.import initial-data/user.csv user
.import initial-data/category.csv category
.import initial-data/develops.csv develops
.import initial-data/friend.csv friend
.import initial-data/game_category.csv game_category
