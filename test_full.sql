DROP TABLE contacts;

CREATE TABLE contacts(id INT AUTO_INCREMENT, name CHAR(30) NOT NULL, email CHAR(30));

INSERT INTO contacts(name, email) VALUES('Rem', 'Rem.collier@ucd.ie');

SELECT * FROM contacts WHERE id < 2;

UPDATE contacts SET email = 'Rem@gmail.com' WHERE name = 'Rem';

SELECT * FROM contacts WHERE id < 2;

INSERT INTO contacts(name, email) VALUES('John', 'john.murphy@ucd.ie');

SELECT * FROM contacts WHERE id <= 2;

INSERT INTO contacts(name, email) VALUES('Liam', 'liam.murphy@ucd.ie');
INSERT INTO contacts(name, email) VALUES('Pat', 'pat.burke@ucd.ie');

SELECT * FROM contacts where id > 1;

SELECT * FROM contacts where id >= 1;

DELETE FROM contacts WHERE name = 'Pat';

SELECT * FROM contacts where name = 'Pat';

SELECT * FROM contacts where name LIKE '%m';

SELECT * FROM contacts WHERE name LIKE 'J%';

SELECT * FROM contacts WHERE name LIKE '%a%';

INSERT INTO contacts(email) VALUES('Rem.collier@ucd.ie');
