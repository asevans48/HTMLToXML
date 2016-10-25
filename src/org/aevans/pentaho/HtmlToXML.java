package org.aevans.pentaho;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * A program for converting Html to XML and then back to an Html string. This is useful for cleaning and an initial run prior to JSoup.
 *
 * Copyright (C) 2016- Andrew Evans
 * *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for moree details.
 * *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class HtmlToXML {
    protected ArrayList<String> tags = new ArrayList();
    protected ArrayList<String> validtags = new ArrayList();
    protected ArrayList<String> content = new ArrayList();
    protected ArrayList<Element> tag_levels = new ArrayList();
    protected ArrayList<String> close_tags = new ArrayList();
    protected ArrayList<String> attributes = new ArrayList();
    Document doc = null;


    public HtmlToXML(String html, String inpath, Boolean strip) throws ParserConfigurationException {
        if(strip.booleanValue()) {
            html.replace("<strong>", "");
            html.replace("</strong>", "");
            html.replace("</ strong>", "");
            html.replace("<b>", "");
            html.replace("</b>", "");
            html.replace("</ b>", "");
            html.replace("<i>", "");
            html.replace("</i>", "");
            html.replace("</ i>", "");
            html.replace("<em>", "");
            html.replace("</em>", "");
            html.replace("</ em>", "");
            html.replace("<bdo>", "");
            html.replace("</bdo>", "");
            html.replace("</ bdo>", "");
            html.replace("<bdi>", "");
            html.replace("</bdi>", "");
            html.replace("</ bdi>", "");
            html.replace("<br>", "");
            html.replace("</br>", "");
            html.replace("</ br>", "");
            html.replace("&nbsp;", "");
        }

        this.initags();
        this.initattr();
        this.createContentArr(html);
        this.doc = this.createXMLDoc(html);
    }

    public String getStringRep() throws TransformerException {
        return this.writeXMLtoString(this.doc);
    }

    public Document getDocument() {
        return this.doc;
    }

    public void setDocument(Document indoc) {
        this.doc = indoc;
    }

    private void initattr() {
        this.attributes.add("name");
        this.attributes.add("src");
        this.attributes.add("onclick");
        this.attributes.add("onClick");
        this.attributes.add("onMouseOver");
        this.attributes.add("onmouseover");
        this.attributes.add("onMouseOut");
        this.attributes.add("onmouseout");
        this.attributes.add("onkeypress");
        this.attributes.add("onKeyPress");
        this.attributes.add("content");
        this.attributes.add("type");
        this.attributes.add("rel");
        this.attributes.add("property");
        this.attributes.add("id");
        this.attributes.add("href");
        this.attributes.add("onload");
        this.attributes.add("onLoad");
        this.attributes.add("value");
    }

    public String getVars() {
        String alias = "";
        String last = "";
        String first = "";
        String birth = "";
        String daNum = "";
        boolean i = false;
        boolean j = false;
        NodeList els = this.doc.getElementsByTagName("table");

        for(int var9 = 2; var9 < els.item(2).getChildNodes().getLength(); ++var9) {
            for(int var10 = 0; var10 < els.item(2).getChildNodes().item(var9).getChildNodes().getLength(); ++var10) {
                if(els.item(2).getChildNodes().item(var9).getChildNodes().item(3).getTextContent().contains("Y")) {
                    if(var10 == 3) {
                        alias = alias + "/" + els.item(2).getChildNodes().item(var9).getChildNodes().item(0).getTextContent() + " " + els.item(2).getChildNodes().item(var9).getChildNodes().item(1).getTextContent();
                    }
                } else if(var10 == 0) {
                    first = els.item(2).getChildNodes().item(var9).getChildNodes().item(0).getTextContent();
                    last = els.item(2).getChildNodes().item(var9).getChildNodes().item(1).getTextContent();
                }

                if(var10 == 2 & (els.item(2).getChildNodes().item(var9).getChildNodes().item(2).getTextContent().contains("1") | els.item(2).getChildNodes().item(var9).getChildNodes().item(2).getTextContent().contains("2")) & birth.length() == 0) {
                    birth = els.item(2).getChildNodes().item(var9).getChildNodes().item(2).getTextContent();
                }

                if(var10 == 4 & !daNum.contains(els.item(2).getChildNodes().item(var9).getChildNodes().item(4).getTextContent())) {
                    daNum = els.item(2).getChildNodes().item(var9).getChildNodes().item(4).getTextContent();
                }
            }
        }

        if(daNum.length() == 0) {
            daNum = "none";
        }

        if(alias.length() == 0) {
            alias = "none";
        }

        if(last.length() == 0) {
            last = "none";
        }

        if(birth.length() == 0) {
            birth = "none";
        }

        return (daNum.trim() + "," + alias.trim() + "," + last.trim() + "," + first.trim() + "," + birth.trim()).trim();
    }

    private void initags() {
        this.validtags.add("a");
        this.validtags.add("audio");
        this.validtags.add("img");
        this.validtags.add("dl");
        this.validtags.add("aside");
        this.validtags.add("title");
        this.validtags.add("canvas");
        this.validtags.add("datalist");
        this.validtags.add("iframe");
        this.validtags.add("IFRAME");
        this.validtags.add("html");
        this.validtags.add("head");
        this.validtags.add("article");
        this.validtags.add("header");
        this.validtags.add("object");
        this.validtags.add("style");
        this.validtags.add("summary");
        this.validtags.add("li");
        this.validtags.add("select");
        this.validtags.add("ol");
        this.validtags.add("ul");
        this.validtags.add("figure");
        this.validtags.add("meta");
        this.validtags.add("form");
        this.validtags.add("input");
        this.validtags.add("meta");
        this.validtags.add("option");
        this.validtags.add("");
        this.validtags.add("menu");
        this.validtags.add("span");
        this.validtags.add("map");
        this.validtags.add("body");
        this.validtags.add("caption");
        this.validtags.add("footer");
        this.validtags.add("script");
        this.validtags.add("p");
        this.validtags.add("div");
        this.validtags.add("code");
        this.validtags.add("samp");
        this.validtags.add("dfn");
        this.validtags.add("table");
        this.validtags.add("tr");
        this.validtags.add("td");
        this.validtags.add("nav");
        this.validtags.add("footer");
        this.validtags.add("area");
        this.validtags.add("!Doctype");
        this.close_tags.add("!Doctype");
        this.validtags.add("!--");
        this.close_tags.add("!--");
        this.validtags.add("param");
        this.close_tags.add("param");
        this.validtags.add("meta");
        this.close_tags.add("meta");
        this.validtags.add("source");
        this.close_tags.add("source");
        this.validtags.add("img");
        this.close_tags.add("img");
        this.validtags.add("link");
        this.close_tags.add("link");
    }

    private void createContentArr(String html) {
        int i = 0;
        int startindex = 0;
        int endindex = 0;
        String sub = null;
        String pattern = "(<).*?(>)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(html);

        while(m.find()) {
            sub = m.group();
            if(this.istag(sub)) {
                this.tags.add(sub);
            }
        }

        for(sub = null; i < this.tags.size(); ++i) {
            if(i + 1 < this.tags.size()) {
                endindex = html.indexOf((String)this.tags.get(i + 1), startindex + ((String)this.tags.get(i)).length());
            }

            if(i < this.tags.size() - 1) {
                sub = html.substring(startindex + ((String)this.tags.get(i)).length(), endindex);
            } else {
                sub = html.substring(startindex + ((String)this.tags.get(i)).length());
            }

            sub = sub + "[//skip//] \n";
            sub = sub.trim();
            this.content.add(sub);
            startindex = endindex;
        }

    }

    private boolean istag(String sub) {
        boolean tag = false;
        String check_string = null;
        String pattern = "(?<=<)\\w+";
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(sub);
        if(mat.find()) {
            check_string = mat.group().toString();
            if(this.validtags.indexOf(check_string) > -1) {
                tag = true;
            }
        } else {
            pattern = "(?<=<\\/)\\w+";
            pat = Pattern.compile(pattern);
            mat = pat.matcher(sub);
            if(mat.find()) {
                check_string = mat.group().trim().toLowerCase();
                if(this.validtags.indexOf(check_string) > -1) {
                    tag = true;
                }
            }
        }

        return tag;
    }

    private String getTag(String intag) {
        String tag = null;
        String pattern = "(?<=<)\\w+";
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(intag);
        if(mat.find()) {
            tag = mat.group().toString();
        } else {
            pattern = "(?<=<\\/)\\w+";
            pat = Pattern.compile(pattern);
            mat = pat.matcher(intag);
            if(mat.find()) {
                tag = mat.group().trim().toLowerCase();
            }
        }

        return tag;
    }

    public Document createXMLDoc(String inhtml) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        boolean i = false;
        int j = 0;
        Element root = doc.createElement("Document");
        doc.appendChild(root);

        for(int var11 = 0; var11 < this.tags.size(); ++var11) {
            if(((String)this.tags.get(var11)).substring(0, 3).contains("/")) {
                this.tag_levels.remove(this.tag_levels.size() - 1);
            } else if(!((String)this.tags.get(var11)).substring(0, 3).contains("/")) {
                this.tag_levels.add(doc.createElement(this.getTag((String)this.tags.get(var11))));
                String attribute = this.getid((String)this.tags.get(var11), "id");
                if(attribute != null) {
                    ((Element)this.tag_levels.get(this.tag_levels.size() - 1)).setAttribute("id", attribute);
                }

                ((Element)this.tag_levels.get(this.tag_levels.size() - 1)).setAttribute("numid", Integer.toString(j));
                ++j;
                Iterator var10 = this.attributes.iterator();

                while(var10.hasNext()) {
                    String attr = (String)var10.next();
                    attribute = this.getid((String)this.tags.get(var11), attr);
                    if(attribute != null) {
                        ((Element)this.tag_levels.get(this.tag_levels.size() - 1)).setAttribute(attr, attribute);
                    }
                }

                attribute = (String)this.content.get(var11);
                attribute = attribute.replace("[//skip//]", "");
                ((Element)this.tag_levels.get(this.tag_levels.size() - 1)).setTextContent(attribute);
                if(this.tag_levels.size() > 1) {
                    this.appendtoOpen((Element)this.tag_levels.get(this.tag_levels.size() - 1));
                } else {
                    root.appendChild((Node)this.tag_levels.get(this.tag_levels.size() - 1));
                }

                if(this.close_tags.indexOf(this.getTag((String)this.tags.get(var11))) > -1) {
                    this.tag_levels.remove(this.tag_levels.size() - 1);
                }
            }
        }

        return doc;
    }

    private void appendtoOpen(Element inElement) {
        ((Element)this.tag_levels.get(this.tag_levels.size() - 2)).appendChild(inElement);
    }

    private String getid(String tag, String attr) {
        String attribute = null;
        String pattern = "(?<=" + attr.trim() + "\\=).*";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tag);
        if(m.find()) {
            attribute = m.group().split(" ")[0];
            attribute.replace("\'", "");
            attribute = attribute.trim();
            attribute = attribute.replace("\"", "");
        }

        return attribute;
    }

    public String writeXMLtoString(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("omit-xml-declaration", "yes");
        StringWriter writer = new StringWriter();
        String output = null;
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, new StreamResult(writer));
        output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

    public void writeXML(String inpath, Document doc) throws TransformerConfigurationException, IOException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("method", "xml");
        DOMSource source = new DOMSource(doc);
        StreamResult result;
        if(inpath == null | inpath.length() == 0) {
            result = new StreamResult(System.out);
        } else {
            result = new StreamResult(new File(inpath));
        }

        transformer.transform(source, result);
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException {
    }
}
