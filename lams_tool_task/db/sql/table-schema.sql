alter table tl_latask10_attachment drop foreign key FK281134C2994F51CE;
alter table tl_latask10_item_log drop foreign key FK6CFEC3773324488D;
alter table tl_latask10_item_log drop foreign key FK6CFEC377B20A10E1;
alter table tl_latask10_item_attachment drop foreign key FK_tl_latask10_item_attachment_1;
alter table tl_latask10_item_attachment drop foreign key FK_tl_latask10_item_attachment_2;
alter table tl_latask10_item_comment drop foreign key FK_tl_latask10_item_comment_3;
alter table tl_latask10_item_comment drop foreign key FK_tl_latask10_item_comment_2;
alter table tl_latask10_session drop foreign key FK3DC26357994F51CE;
alter table tl_latask10_taskList drop foreign key FKE544722E5916D7F;
alter table tl_latask10_taskList_item drop foreign key FK2DB1121025521843;
alter table tl_latask10_taskList_item drop foreign key FK2DB11210E5916D7F;
alter table tl_latask10_taskList_item drop foreign key FK2DB11210994F51CE;
alter table tl_latask10_user drop foreign key FKFE49AAAA25521843;
alter table tl_latask10_user drop foreign key FKFE49AAAA994F51CE;
drop table if exists tl_latask10_attachment;
drop table if exists tl_latask10_item_instruction;
drop table if exists tl_latask10_item_log;
drop table if exists tl_latask10_item_attachment;
drop table if exists tl_latask10_item_comment;
drop table if exists tl_latask10_session;
drop table if exists tl_latask10_taskList;
drop table if exists tl_latask10_taskList_item;
drop table if exists tl_latask10_user;
create table tl_latask10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, taskList_uid bigint, primary key (uid));
create table tl_latask10_item_instruction (uid bigint not null auto_increment, description varchar(255), sequence_id integer, primary key (uid));
create table tl_latask10_item_log (uid bigint not null auto_increment, access_date datetime, taskList_item_uid bigint, user_uid bigint, complete bit, session_id bigint, primary key (uid));
create table tl_latask10_item_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, taskList_uid bigint, create_by bigint, primary key (uid));
create table tl_latask10_item_comment (uid bigint not null auto_increment, comment varchar(255), taskList_item_uid bigint, create_by bigint, create_date datetime, primary key (uid));
create table tl_latask10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, taskList_uid bigint, session_id bigint, session_name varchar(250), primary key (uid));
create table tl_latask10_taskList (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), run_offline bit, instructions text, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, content_id bigint unique, lock_when_finished bit, allow_contribute_tasks bit, is_monitor_verification_required bit, is_sequential_order bit, reflect_instructions varchar(255), reflect_on_activity bit, primary key (uid));
create table tl_latask10_taskList_item (uid bigint not null auto_increment, description varchar(255), init_item varchar(255), organization_xml text, title varchar(255), create_by bigint, create_date datetime, create_by_author bit, sequence_id integer, is_required bit, is_comments_allowed bit, show_comments_to_all bit, is_child_task bit, parent_task_name varchar(255), taskList_uid bigint, session_uid bigint, primary key (uid));
create table tl_latask10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), first_name varchar(255), login_name varchar(255), session_uid bigint, taskList_uid bigint, session_finished bit, primary key (uid));
alter table tl_latask10_attachment add index FK281134C2994F51CE (taskList_uid), add constraint FK281134C2994F51CE foreign key (taskList_uid) references tl_latask10_taskList (uid);
alter table tl_latask10_item_log add index FK6CFEC3773324488D (taskList_item_uid), add constraint FK6CFEC3773324488D foreign key (taskList_item_uid) references tl_latask10_taskList_item (uid);
alter table tl_latask10_item_log add index FK6CFEC377B20A10E1 (user_uid), add constraint FK6CFEC377B20A10E1 foreign key (user_uid) references tl_latask10_user (uid);
alter table tl_latask10_item_attachment add index FK_tl_latask10_item_attachment_1 (taskList_item_uid), add constraint FK_tl_latask10_item_attachment_1 foreign key (taskList_item_uid) references tl_latask10_taskList_item (uid);
alter table tl_latask10_item_attachment add index FK_tl_latask10_item_attachment_2 (create_by), add constraint FK_tl_latask10_item_attachment_2 foreign key (create_by) references tl_latask10_user (uid);
alter table tl_latask10_item_comment add index FK_tl_latask10_item_comment_3 (taskList_item_uid), add constraint FK_tl_latask10_item_comment_3 foreign key (taskList_item_uid) references tl_latask10_taskList_item (uid);
alter table tl_latask10_item_comment add index FK_tl_latask10_item_comment_2 (create_by), add constraint FK_tl_latask10_item_comment_2 foreign key (create_by) references tl_latask10_user (uid);
alter table tl_latask10_session add index FK3DC26357994F51CE (taskList_uid), add constraint FK3DC26357994F51CE foreign key (taskList_uid) references tl_latask10_taskList (uid);
alter table tl_latask10_taskList add index FKE544722E5916D7F (create_by), add constraint FKE544722E5916D7F foreign key (create_by) references tl_latask10_user (uid);
alter table tl_latask10_taskList_item add index FK2DB1121025521843 (session_uid), add constraint FK2DB1121025521843 foreign key (session_uid) references tl_latask10_session (uid);
alter table tl_latask10_taskList_item add index FK2DB11210E5916D7F (create_by), add constraint FK2DB11210E5916D7F foreign key (create_by) references tl_latask10_user (uid);
alter table tl_latask10_taskList_item add index FK2DB11210994F51CE (taskList_uid), add constraint FK2DB11210994F51CE foreign key (taskList_uid) references tl_latask10_taskList (uid);
alter table tl_latask10_user add index FKFE49AAAA25521843 (session_uid), add constraint FKFE49AAAA25521843 foreign key (session_uid) references tl_latask10_session (uid);
alter table tl_latask10_user add index FKFE49AAAA994F51CE (taskList_uid), add constraint FKFE49AAAA994F51CE foreign key (taskList_uid) references tl_latask10_taskList (uid);
