create table `PROJECT_VERSION` (
	`id`			          BIGINT			  NOT NULL AUTO_INCREMENT	COMMENT 'ID（PK，自增）',
	`project_id`			  BIGINT	      NULL			    	        COMMENT '归属项目ID',

  `release_time`	    DATETIME		  NOT NULL			    	    COMMENT '发布时间',
  `version`	          VARCHAR(50)		NOT NULL			    	    COMMENT '版本号',
	`status`	          INT	          NOT NULL                COMMENT '状态',
  `subtitle`		      VARCHAR(150)	NULL				            COMMENT '小标题',
	`changelog`	        TEXT	        NOT NULL                COMMENT '更新内容',
	`changelog_format`	INT	          NOT NULL                COMMENT '更新内容格式',
	`futures`	          TEXT	        NULL                    COMMENT '扩展信息(json格式)',
	
	`create_time`	      DATETIME		  NOT NULL			    	    COMMENT '创建时间',
	`modify_time`	      DATETIME		  NULL					          COMMENT '修改时间',
	PRIMARY KEY (`id`),
	index(`project_id`),
	unique index(`project_id`,`version`)
);