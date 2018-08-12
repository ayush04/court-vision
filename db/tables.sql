users 

create table users (user_id bigint not null primary key generated always as identity, first_name varchar(100), last_name varchar(100), email_address varchar(100), username varchar(20) not null, password varchar(1000) not null, is_player smallint default 1, is_loggedin smallint default 0, is_deleted smallint default 0, when_created bigint, when_modified bigint, last_accessed bigint)

session

create table session (user_id bigint references users(user_id), session_id varchar(200) primary key, http_session_id varchar(250), is_valid smallint default 1, session_start_time bigint, session_end_time bigint) 


team

create table team (team_id bigint not null primary key generated always as identity, team_name varchar(200), when_created bigint, is_deleted smallint default 0, when_deleted bigint)

game

create table game (game_id bigint not null primary key generated always as identity, start_time bigint not null, end_time bigint, team_1 bigint references team(team_id), team_2 bigint references team(team_id))

event_type

create table event_type (event_type bigint not null primary key generated always as identity, event_name varchar(200) not null)

events

create table events (event_id bigint not null primary key generated always as identity, game_id bigint references game(game_id), user_id bigint default -1, event_time bigint, event_type bigint references event_type(event_type), event_value varchar(100), event_value_2 varchar(100), event_value_3 varchar(100), event_value_4 varchar(1000))

player_game_mapping

create table player_game_mapping (id bigint not null primary key generated always as identity, game_id bigint references game(game_id), user_id bigint references users(user_id))

attachment

create table attachment (attachment_id bigint not null primary key generated always as identity, user_id bigint references users(user_id), attachment_name varchar(100) not null, content blob, attachment_format varchar(10), when_created bigint, is_deleted smallint, when_deleted bigint)


player_profile

create table player_profile (user_id bigint not null primary key references users(user_id), height float, weight float, baskets_scored smallint, total_games smallint, fitbit_enabled smallint default 0, fitbit_client_id varchar(20), role varchar(20), is_deleted smallint default 0, when_modified bigint, when_deleted bigint)


user_role

create table user_role (role_id bigint not null primary key generated always as identity, role_name varchar(200))


user_role_mapping

create table user_role_mapping (id bigint not null primary key generated always as identity, user_id bigint references users(user_id), role_id bigint references user_role(role_id))

player_team_mapping

create table player_team_mapping (id bigint not null primary key generated always as identity, user_id bigint references users(user_id), team_id bigint references team(team_id))

notification

create table notification (notification_id bigint not null primary key generated always as identity, created_by bigint references users(user_id), created_for bigint, notification_type smallint, when_created bigint, title varchar(500), content varchar(5000), is_read smallint default 0, when_read bigint)

calendar

create table calendar (event_id bigint not null primary key generated always as identity, created_by bigint references users(user_id), title varchar(500), start_time bigint, end_time bigint, location varchar(300), when_created bigint, all_day smallint default 0, details varchar(1000))