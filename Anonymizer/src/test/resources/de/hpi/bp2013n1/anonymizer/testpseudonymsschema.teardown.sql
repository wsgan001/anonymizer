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
DROP TABLE ORIGINAL.VISIT_MOVIE;
DROP TABLE ORIGINAL.VISITOR_ZIPCODE;
DROP TABLE ORIGINAL.VISITOR_SURNAME;
DROP TABLE ORIGINAL.VISITOR_BIRTHDATE_CHARACTERS;
DROP TABLE ORIGINAL.CINEMA_COMPANY;
DROP TABLE ORIGINAL.CINEMA_ADDRESS;
DROP TABLE ORIGINAL.PRODUCTSALES_RETAINED;
DROP SCHEMA ORIGINAL RESTRICT;
