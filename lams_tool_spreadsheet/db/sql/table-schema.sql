alter table tl_lasprd10_attachment drop foreign key FK880769383F87A469;
alter table tl_lasprd10_session drop foreign key FK3B9F72A13F87A469;
alter table tl_lasprd10_spreadsheet drop foreign key FK26864E57BAA77371;
alter table tl_lasprd10_user drop foreign key FK4E45EFA0332FF511;
alter table tl_lasprd10_user drop foreign key FK4E45EFA03F87A469;
alter table tl_lasprd10_user drop foreign key FK4E45EFA042839493;
alter table tl_lasprd10_user_modified_spreadsheet drop foreign key FK632B0695C8FF0BC6;
drop table if exists tl_lasprd10_attachment;
drop table if exists tl_lasprd10_session;
drop table if exists tl_lasprd10_spreadsheet;
drop table if exists tl_lasprd10_spreadsheet_mark;
drop table if exists tl_lasprd10_user;
drop table if exists tl_lasprd10_user_modified_spreadsheet;
create table tl_lasprd10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, spreadsheet_uid bigint, primary key (uid));
create table tl_lasprd10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, spreadsheet_uid bigint, session_id bigint, session_name varchar(250), primary key (uid));
create table tl_lasprd10_spreadsheet (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), run_offline bit, is_learner_allowed_to_save bit, is_marking_enabled bit, lock_on_finished bit, instructions longtext, code longtext, online_instructions longtext, offline_instructions longtext, content_in_use bit, define_later bit, content_id bigint unique, reflect_instructions varchar(255), reflect_on_activity bit, primary key (uid));
create table tl_lasprd10_spreadsheet_mark (uid bigint not null auto_increment, marks varchar(255), comments longtext, date_marks_released datetime, primary key (uid));
create table tl_lasprd10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), first_name varchar(255), login_name varchar(255), session_uid bigint, spreadsheet_uid bigint, user_modified_spreadsheet_uid bigint, session_finished bit, primary key (uid));
create table tl_lasprd10_user_modified_spreadsheet (uid bigint not null auto_increment, user_modified_spreadsheet longtext, mark_id bigint, primary key (uid));
alter table tl_lasprd10_attachment add index FK880769383F87A469 (spreadsheet_uid), add constraint FK880769383F87A469 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_session add index FK3B9F72A13F87A469 (spreadsheet_uid), add constraint FK3B9F72A13F87A469 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_spreadsheet add index FK26864E57BAA77371 (create_by), add constraint FK26864E57BAA77371 foreign key (create_by) references tl_lasprd10_user (uid);
alter table tl_lasprd10_user add index FK4E45EFA0332FF511 (session_uid), add constraint FK4E45EFA0332FF511 foreign key (session_uid) references tl_lasprd10_session (uid);
alter table tl_lasprd10_user add index FK4E45EFA03F87A469 (spreadsheet_uid), add constraint FK4E45EFA03F87A469 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_user add index FK4E45EFA042839493 (user_modified_spreadsheet_uid), add constraint FK4E45EFA042839493 foreign key (user_modified_spreadsheet_uid) references tl_lasprd10_user_modified_spreadsheet (uid);
alter table tl_lasprd10_user_modified_spreadsheet add index FK632B0695C8FF0BC6 (mark_id), add constraint FK632B0695C8FF0BC6 foreign key (mark_id) references tl_lasprd10_spreadsheet_mark (uid);
