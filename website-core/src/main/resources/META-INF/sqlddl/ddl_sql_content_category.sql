create table `CONTENT_CATEGORY` (
	`id`			          BIGINT			  NOT NULL AUTO_INCREMENT	COMMENT '分类ID（PK，自增）',
	`user_id`		        BIGINT				NOT NULL				        COMMENT '分类所属用户ID',

	`name`		          VARCHAR(150)	NOT NULL				        COMMENT '名称',
  `order_index`			  INT						NOT NULL			    	    COMMENT '顺序',
	`status`			  		INT						NOT NULL			    	    COMMENT '状态',
	`futures`	          TEXT	        NULL                    COMMENT '扩展信息(json格式)',
	
	`create_time`	      DATETIME		  NOT NULL			    	    COMMENT '创建时间',
	`modify_time`	      DATETIME		  NOT NULL					      COMMENT '修改时间',
	
	PRIMARY KEY (`id`),
	index(`order_index`),
	index(`user_id`,`status`)
);