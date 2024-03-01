DROP TABLE MATCH IF EXISTS;
DROP TABLE TEAM IF EXISTS;

CREATE TABLE MATCH  (
    id  BIGINT  NOT NULL,
    city VARCHAR(50),
    date DATE,
    player_of_match VARCHAR(50),
    venue VARCHAR(100),
    neutral_venue VARCHAR(10),
    team1 VARCHAR(50),
    team2  VARCHAR(50),
    toss_winner  VARCHAR(50),
    toss_decision  VARCHAR(40),
    match_winner  VARCHAR(50),
    result VARCHAR(50),
    result_margin VARCHAR(10),
    eliminator VARCHAR(10),
    method VARCHAR(10),
    umpire1 VARCHAR(30),
    umpire2 VARCHAR(30),
    PRIMARY KEY (id) 
    
);

CREATE TABLE TEAM(
    id  BIGINT  NOT NULL,
    teamName VARCHAR(50),
    totalWins BIGINT,
    totalMatches BIGINT,        
    PRIMARY KEY (id)
);
