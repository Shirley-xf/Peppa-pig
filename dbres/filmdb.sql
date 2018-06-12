
DROP TABLE IF EXISTS `film`;
CREATE TABLE `film` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`name`	TEXT NOT NULL UNIQUE,
	`duration`	TEXT,
  `year` INTEGER,
  `country` TEXT,
  `type` TEXT,
	`intro_url`	TEXT,
	`media_url`	TEXT,
	`img_url`	TEXT
);

DROP TABLE IF EXISTS film_actor;
CREATE TABLE `film_actor` (
	`id` INTEGER,
	`actor`	TEXT,
	foreign key(`id`)	references film(`id`)
);

DROP TABLE IF EXISTS film_director;
CREATE TABLE `film_director` (
	`id` INTEGER,
  `director`	TEXT,
	foreign key(`id`)	references film(`id`)
);