create table `PROJECT_INFO` (
	`id`			          BIGINT			  NOT NULL AUTO_INCREMENT	COMMENT '项目ID（PK，自增）',
	`owner_id`		      BIGINT				NOT NULL				        COMMENT '项目owner id',
  `owner_type`		    INT	          NOT NULL				        COMMENT '归属组织 id',
  `parent_id`		      BIGINT	      NULL				            COMMENT '归属父项目 id',
  `status`		        INT	          NOT NULL				        COMMENT '状态',

  `name`		          VARCHAR(50)	  NOT NULL				        COMMENT '项目名称',
  `subtitle`		      VARCHAR(150)	NOT NULL				        COMMENT '小标题',
  `present`		  		  MEDIUMTEXT	  NOT NULL				        COMMENT '介绍正文',
  `present_format`		INT	          NOT NULL				        COMMENT '介绍内容格式',

  `home_page`		      VARCHAR(200)	NOT NULL				        COMMENT '项目主页',
  `down_page`		      VARCHAR(200)	NOT NULL				        COMMENT '下载连接 or 页面',
  `type`			        INT 	        NOT NULL					      COMMENT '项目类型(开源项目 or 闭源)',
  `language`		  		VARCHAR(50)	  NOT NULL				        COMMENT '主要使用语言',
  `license`		  		  VARCHAR(50)	  NULL				            COMMENT '授权协议',
	`futures`	          TEXT	        NULL                    COMMENT '扩展信息(json格式)',

	`create_time`	      DATETIME		  NOT NULL			    	    COMMENT '创建时间',
	`modify_time`	      DATETIME		  NOT NULL					      COMMENT '修改时间',
	PRIMARY KEY (`id`),
  index(`name`),
  index(`owner_id`,`owner_type`),
  index(`parent_id`)
);