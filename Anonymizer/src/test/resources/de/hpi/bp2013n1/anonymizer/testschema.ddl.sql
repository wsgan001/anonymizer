---
-- #%L
-- Anonymizer
-- %%
-- Copyright (C) 2013 - 2014 HPI Bachelor's Project N1 2013
-- %%
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--      http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
-- #L%
---
-- <ScriptOptions statementTerminator=";" />
CREATE SCHEMA ORIGINAL;
CREATE TABLE ORIGINAL.VISITOR ( NAME VARCHAR(20) NOT NULL, SURNAME VARCHAR(20) NOT NULL, BIRTHDATE CHAR(8) NOT NULL, ZIPCODE CHAR(5), ADDRESS VARCHAR(50),PRIMARY KEY ( NAME, SURNAME, BIRTHDATE ) );
CREATE TABLE ORIGINAL.CINEMA ( COMPANY VARCHAR(20) NOT NULL, ADDRESS VARCHAR(50) NOT NULL, SALESREVENUE_LASTYEAR DECIMAL(10,2),PRIMARY KEY ( COMPANY, ADDRESS ) );
CREATE TABLE ORIGINAL.GREATMOVIES ( MOVIE VARCHAR(30) NOT NULL, COMPANY VARCHAR(20) NOT NULL, DIRECTOR VARCHAR(40),PRIMARY KEY ( MOVIE, COMPANY ) );
CREATE TABLE ORIGINAL.VISIT ( VISITORNAME VARCHAR(20), VISITORSURNAME VARCHAR(20), VISITORBIRTHDATE CHAR(8), CINEMACOMPANY VARCHAR(20), CINEMAADDRESS VARCHAR(50), DATE TIMESTAMP(0), MOVIE VARCHAR(30) NOT NULL );
ALTER TABLE ORIGINAL.VISIT ADD CONSTRAINT VISIT_VISITOR_FK FOREIGN KEY ( VISITORSURNAME, VISITORNAME, VISITORBIRTHDATE ) REFERENCES ORIGINAL.VISITOR ( SURNAME, NAME, BIRTHDATE ) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE ORIGINAL.VISIT ADD CONSTRAINT VISIT_CINEMA_FK FOREIGN KEY ( CINEMACOMPANY, CINEMAADDRESS ) REFERENCES ORIGINAL.CINEMA ( COMPANY, ADDRESS ) ON DELETE CASCADE ON UPDATE NO ACTION;

CREATE TABLE ORIGINAL.PRODUCTSALES ( SALEID INT NOT NULL, PRODUCTID INT, PRIMARY KEY(SALEID) );
CREATE TABLE ORIGINAL.PRODUCTBUYER ( SALEID INT NOT NULL, BUYERNAME VARCHAR(60), PRIMARY KEY(SALEID) );
CREATE TABLE ORIGINAL.BUYERDETAILS ( SALEID INT NOT NULL, BUYERNAME VARCHAR(60) NOT NULL, CUSTOMERNO INT, PRIMARY KEY(SALEID, BUYERNAME) );
ALTER TABLE ORIGINAL.PRODUCTBUYER ADD CONSTRAINT BUYER_SALE_FK FOREIGN KEY ( SALEID ) REFERENCES ORIGINAL.PRODUCTSALES ( SALEID ) ON DELETE RESTRICT ON UPDATE NO ACTION;
ALTER TABLE ORIGINAL.BUYERDETAILS ADD CONSTRAINT BUYER_DETAILS_FK FOREIGN KEY ( SALEID, BUYERNAME ) REFERENCES ORIGINAL.PRODUCTBUYER ( SALEID, BUYERNAME ) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE TABLE ORIGINAL.DISTRIBUTIONTEST1 ( A INT );
CREATE TABLE ORIGINAL.DISTRIBUTIONTEST2 ( A INT );
