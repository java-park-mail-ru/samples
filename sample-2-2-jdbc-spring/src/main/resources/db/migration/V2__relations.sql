create table house_relation(
    relation_subject_id bigint references house(id), 
    relation_object_id bigint references house(id),
    relation text,
   	primary key(relation_subject_id, relation_object_id)
);

create table person_relation(
    relation_subject_id bigint references person(id), 
    relation_object_id bigint references person(id),
    relation text,
   	primary key(relation_subject_id, relation_object_id)
);