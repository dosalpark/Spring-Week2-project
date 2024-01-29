DROP TABLE IF EXISTS `Schdule`;

CREATE TABLE `Schdule` (
	`ID`	Long	NOT NULL,
	`titleSchedule`	Long	NULL,
	`bodySchedule`	Long	NULL,
	`user`	Long	NULL,
	`pw`	varchar(500)	NULL,
	`createdAt`	datetime(6)	NULL,
	`modifiedAt`	datetime(6)	NULL
);

