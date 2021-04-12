alter table presence_matchers add column query_type varchar(255);
update presence_matchers set query_type = 'X_PATH';