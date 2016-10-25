# HTMLToXML
A tool I created for Pentaho to reliably parse dirty html as Jsoup is buggy. Call the constructor with html, null,strip(true/false for strong,br,bdo,bdi.&amp;nbsp;) and then getStringRep(). This tool is one of the cleaners
available in JScrape where Jsoup is mandatory. A bug has been filed to make Jsoup more appropriate.

https://github.com/jhy/jsoup/issues/777

# License


  A program for converting Html to XML and then back to an Html string. This is useful for cleaning and an initial run prior to JSoup.
   
  Copyright (C) 2014- Andrew Evans
  
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for moree details.
  
  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
