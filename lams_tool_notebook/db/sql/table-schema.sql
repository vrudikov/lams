alter table lams_text_search_condition drop foreign key FK69B884B21E70F543;
alter table tl_lantbk11_attachment drop foreign key FK12090F57FC940906;
alter table tl_lantbk11_conditions drop foreign key FK4F95ECEC60201CA4;
alter table tl_lantbk11_conditions drop foreign key FK4F95ECEC3E8DC190;
alter table tl_lantbk11_session drop foreign key FKB7C198E2FC940906;
alter table tl_lantbk11_user drop foreign key FKCB8A58FFA3B0FADF;
drop table if exists lams_branch_condition;
drop table if exists lams_text_search_condition;
drop table if exists tl_lantbk11_attachment;
drop table if exists tl_lantbk11_conditions;
drop table if exists tl_lantbk11_notebook;
drop table if exists tl_lantbk11_session;
drop table if exists tl_lantbk11_user;
create table lams_branch_condition (condition_id bigint not null auto_increment, condition_ui_id integer, order_id integer, name varchar(255), display_name varchar(255), type varchar(255), start_value varchar(255), end_value varchar(255), exact_match_value varchar(255), primary key (condition_id));
create table lams_text_search_condition (condition_id bigint not null, text_search_all_words varchar(255), text_search_phrase varchar(255), text_search_any_words varchar(255), text_search_excluded_words varchar(255), primary key (condition_id));
create table tl_lantbk11_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, notebook_uid bigint, primary key (uid));
create table tl_lantbk11_conditions (condition_id bigint not null, content_uid bigint, primary key (condition_id));
create table tl_lantbk11_notebook (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions longtext, run_offline bit, lock_on_finished bit, allow_rich_editor bit, online_instructions longtext, offline_instructions longtext, content_in_use bit, define_later bit, submission_deadline datetime, tool_content_id bigint, primary key (uid));
create table tl_lantbk11_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), notebook_uid bigint, primary key (uid));
create table tl_lantbk11_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, notebook_session_uid bigint, entry_uid bigint, primary key (uid));
alter table lams_text_search_condition add index FK69B884B21E70F543 (condition_id), add constraint FK69B884B21E70F543 foreign key (condition_id) references lams_branch_condition (condition_id);
alter table tl_lantbk11_attachment add index FK12090F57FC940906 (notebook_uid), add constraint FK12090F57FC940906 foreign key (notebook_uid) references tl_lantbk11_notebook (uid);
alter table tl_lantbk11_conditions add index FK4F95ECEC60201CA4 (content_uid), add constraint FK4F95ECEC60201CA4 foreign key (content_uid) references tl_lantbk11_notebook (uid);
alter table tl_lantbk11_conditions add index FK4F95ECEC3E8DC190 (condition_id), add constraint FK4F95ECEC3E8DC190 foreign key (condition_id) references lams_text_search_condition (condition_id);
alter table tl_lantbk11_session add index FKB7C198E2FC940906 (notebook_uid), add constraint FKB7C198E2FC940906 foreign key (notebook_uid) references tl_lantbk11_notebook (uid);
alter table tl_lantbk11_user add index FKCB8A58FFA3B0FADF (notebook_session_uid), add constraint FKCB8A58FFA3B0FADF foreign key (notebook_session_uid) references tl_lantbk11_session (uid);
