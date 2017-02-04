create table `CONTENT_INFO` (
	`id`			          BIGINT			  NOT NULL AUTO_INCREMENT	COMMENT '内容ID（PK，自增）',
	`category_id`		    BIGINT				NULL				            COMMENT '分类ID',
  `user_id`		        BIGINT				NOT NULL				        COMMENT '帖子所属用户ID',
  
  `type`		          INT	          NOT NULL				        COMMENT '文章类型(首发,原创,转载)',
  `modifier`		      VARCHAR(150)	NOT NULL				        COMMENT '公开类型json(所有人,指定范围,私有的)',
  
  `title`		          VARCHAR(100)	NOT NULL				        COMMENT '标题',
  `brief`		          VARCHAR(200)	NOT NULL				        COMMENT '摘要',
  `content_body`		  MEDIUMTEXT	  NOT NULL				        COMMENT '内容正文',
  `content_format`		INT	          NOT NULL				        COMMENT '文章格式',
  
  `futures`	          TEXT	        NULL                    COMMENT '扩展信息(json格式)',
  `status`			  		INT						NOT NULL			    	    COMMENT '状态',
  `allow_comment`			INT						NOT NULL			    	    COMMENT '是否允许评论',
  `ontop`			        INT						NOT NULL			    	    COMMENT '置顶',
  `publish_time`	    DATETIME		  NOT NULL			    	    COMMENT '文章发布时间',

  `create_time`	      DATETIME		  NOT NULL			    	    COMMENT '创建时间',
	`modify_time`	      DATETIME		  NOT NULL					      COMMENT '修改时间',
  
	PRIMARY KEY (`id`),
  index(`category_id`,`user_id`),
  index(`user_id`),
  index(`status`)
);