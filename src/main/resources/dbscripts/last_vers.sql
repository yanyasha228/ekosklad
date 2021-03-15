alter table products add column presence varchar(255);

create table supplier_sources
(
    id         integer not null
        constraint supplier_sources_pkey
            primary key,
    source_url varchar(255)
        constraint uk_cioayun99fl49mbpcysyqkef8
            unique,
    name       varchar(255)
);

alter table supplier_sources
    owner to postgres;

create table presence_matchers
(
    id                   integer not null
        constraint presence_matchers_pkey
            primary key,
    contain_string       varchar(255),
    presence             varchar(255),
    presence_css_query   varchar(255),
    supplier_resource_id integer
        constraint fk7cml98rslf5wxbok0ivga6oa8
            references supplier_sources
);

alter table presence_matchers
    owner to postgres;