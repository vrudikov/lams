alter table lams_text_search_condition drop foreign key FK69B884B21E70F543;
alter table tl_lasurv11_answer drop foreign key FK6DAAFE3B25F3BB77;
alter table tl_lasurv11_answer drop foreign key FK6DAAFE3BB1423DC1;
alter table tl_lasurv11_attachment drop foreign key FKD92A9120D14146E5;
alter table tl_lasurv11_condition_questions drop foreign key FK4DB9B7CCC5224800;
alter table tl_lasurv11_condition_questions drop foreign key FK4DB9B7CC25F3BB77;
alter table tl_lasurv11_conditions drop foreign key FK16B76EB5E23B2C84;
alter table tl_lasurv11_conditions drop foreign key FK16B76EB53E8DC190;
alter table tl_lasurv11_option drop foreign key FK85AB46F225F3BB77;
alter table tl_lasurv11_question drop foreign key FK872D4F23D14146E5;
alter table tl_lasurv11_question drop foreign key FK872D4F23E4C99A5F;
alter table tl_lasurv11_session drop foreign key FKF08793B9D14146E5;
alter table tl_lasurv11_survey drop foreign key FK8CC465D7E4C99A5F;
alter table tl_lasurv11_user drop foreign key FK633F2588D14146E5;
alter table tl_lasurv11_user drop foreign key FK633F25884F803F63;
drop table if exists lams_branch_condition;
drop table if exists lams_text_search_condition;
drop table if exists tl_lasurv11_answer;
drop table if exists tl_lasurv11_attachment;
drop table if exists tl_lasurv11_condition_questions;
drop table if exists tl_lasurv11_conditions;
drop table if exists tl_lasurv11_option;
drop table if exists tl_lasurv11_question;
drop table if exists tl_lasurv11_session;
drop table if exists tl_lasurv11_survey;
drop table if exists tl_lasurv11_user;
create table lams_branch_condition (condition_id bigint not null auto_increment, condition_ui_id integer, order_id integer, name varchar(255), display_name varchar(255), type varchar(255), start_value varchar(255), end_value varchar(255), exact_match_value varchar(255), primary key (condition_id));
create table lams_text_search_condition (condition_id bigint not null, text_search_all_words varchar(255), text_search_phrase varchar(255), text_search_any_words varchar(255), text_search_excluded_words varchar(255), primary key (condition_id));
create table tl_lasurv11_answer (uid bigint not null auto_increment, question_uid bigint, user_uid bigint, answer_choices varchar(255), udpate_date datetime, answer_text varchar(255), primary key (uid));
create table tl_lasurv11_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, survey_uid bigint, primary key (uid));
create table tl_lasurv11_condition_questions (condition_id bigint not null, question_uid bigint not null, primary key (condition_id, question_uid));
create table tl_lasurv11_conditions (condition_id bigint not null, content_uid bigint, primary key (condition_id));
create table tl_lasurv11_option (uid bigint not null auto_increment, description longtext, sequence_id integer, question_uid bigint, primary key (uid));
create table tl_lasurv11_question (uid bigint not null auto_increment, description longtext, create_by bigint, create_date datetime, question_type smallint, append_text bit, optional bit, allow_multiple_answer bit, sequence_id integer, survey_uid bigint, primary key (uid));
create table tl_lasurv11_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, survey_uid bigint, session_id bigint, session_name varchar(250), primary key (uid));
create table tl_lasurv11_survey (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), run_offline bit, lock_on_finished bit, instructions longtext, online_instructions longtext, offline_instructions longtext, content_in_use bit, define_later bit, content_id bigint unique, reflect_instructions varchar(255), reflect_on_activity bit, show_questions_on_one_page bit, answer_submit_notify bit, submission_deadline datetime, primary key (uid));
create table tl_lasurv11_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), first_name varchar(255), login_name varchar(255), session_uid bigint, survey_uid bigint, session_finished bit, primary key (uid));
alter table lams_text_search_condition add index FK69B884B21E70F543 (condition_id), add constraint FK69B884B21E70F543 foreign key (condition_id) references lams_branch_condition (condition_id);
alter table tl_lasurv11_answer add index FK6DAAFE3B25F3BB77 (question_uid), add constraint FK6DAAFE3B25F3BB77 foreign key (question_uid) references tl_lasurv11_question (uid);
alter table tl_lasurv11_answer add index FK6DAAFE3BB1423DC1 (user_uid), add constraint FK6DAAFE3BB1423DC1 foreign key (user_uid) references tl_lasurv11_user (uid);
alter table tl_lasurv11_attachment add index FKD92A9120D14146E5 (survey_uid), add constraint FKD92A9120D14146E5 foreign key (survey_uid) references tl_lasurv11_survey (uid);
alter table tl_lasurv11_condition_questions add index FK4DB9B7CCC5224800 (condition_id), add constraint FK4DB9B7CCC5224800 foreign key (condition_id) references tl_lasurv11_conditions (condition_id);
alter table tl_lasurv11_condition_questions add index FK4DB9B7CC25F3BB77 (question_uid), add constraint FK4DB9B7CC25F3BB77 foreign key (question_uid) references tl_lasurv11_question (uid);
alter table tl_lasurv11_conditions add index FK16B76EB5E23B2C84 (content_uid), add constraint FK16B76EB5E23B2C84 foreign key (content_uid) references tl_lasurv11_survey (uid);
alter table tl_lasurv11_conditions add index FK16B76EB53E8DC190 (condition_id), add constraint FK16B76EB53E8DC190 foreign key (condition_id) references lams_text_search_condition (condition_id);
alter table tl_lasurv11_option add index FK85AB46F225F3BB77 (question_uid), add constraint FK85AB46F225F3BB77 foreign key (question_uid) references tl_lasurv11_question (uid);
alter table tl_lasurv11_question add index FK872D4F23D14146E5 (survey_uid), add constraint FK872D4F23D14146E5 foreign key (survey_uid) references tl_lasurv11_survey (uid);
alter table tl_lasurv11_question add index FK872D4F23E4C99A5F (create_by), add constraint FK872D4F23E4C99A5F foreign key (create_by) references tl_lasurv11_user (uid);
alter table tl_lasurv11_session add index FKF08793B9D14146E5 (survey_uid), add constraint FKF08793B9D14146E5 foreign key (survey_uid) references tl_lasurv11_survey (uid);
alter table tl_lasurv11_survey add index FK8CC465D7E4C99A5F (create_by), add constraint FK8CC465D7E4C99A5F foreign key (create_by) references tl_lasurv11_user (uid);
alter table tl_lasurv11_user add index FK633F2588D14146E5 (survey_uid), add constraint FK633F2588D14146E5 foreign key (survey_uid) references tl_lasurv11_survey (uid);
alter table tl_lasurv11_user add index FK633F25884F803F63 (session_uid), add constraint FK633F25884F803F63 foreign key (session_uid) references tl_lasurv11_session (uid);
