package com.atosorigin.wfem.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.atosorigin.wfem.util.Tools;

/************************************************************
 * @author: Ricotti Corrado
 *************************************************************/
public class HiddenServletResponse {
	
	transient private static Pattern patternStartBody = Pattern.compile("\\<body[^\\>]*\\>(.*)",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public ByteArrayOutputStream getInnerHtmlStream(RequestManager requestManager,ByteArrayOutputStream htmlStream, 
													String elId, String charEncoding) throws IOException{
		try{
	        String styleSheet = "<?xml version='1.0' encoding='"+charEncoding+"'?>\n";
	        
	        boolean asHtmlBody = false;
			if(requestManager.getResponse().containsHeader("WHSReplaceAsBody"))
				asHtmlBody = true;
			
			if(elId.equalsIgnoreCase("body") || asHtmlBody){ 
				String html = htmlStream.toString(charEncoding);
				Matcher mat = patternStartBody.matcher(html);
				if(mat.find())
					html = mat.group(1);
				else
					throw new RuntimeException("Body tag not correctly formatted");
				String htmlLower = html.toLowerCase();
				int endBodyIdx = htmlLower.lastIndexOf("</body>");
				if(endBodyIdx < 0)
					throw new RuntimeException("Body tag not correctly formatted");
				html = html.substring(0,endBodyIdx);
				htmlStream.close();
		        htmlStream = new ByteArrayOutputStream();
		        if(asHtmlBody){
			        htmlStream.write(html.toString().getBytes(charEncoding));
		        }else{
			        StringBuffer inHtml = new StringBuffer(styleSheet+"<whsRoot><whsId>"+elId+"</whsId><whsHtml><![CDATA["+html.toString()+"]]></whsHtml></whsRoot>");
			        htmlStream.write(inHtml.toString().getBytes(charEncoding));
		        }
				return htmlStream;
			}
			
			ByteArrayInputStream responseInputStream = new ByteArrayInputStream(htmlStream.toByteArray());
			InputSource source = new InputSource(responseInputStream);
			DOMParser parser = new DOMParser();
			parser.setProperty("http://cyberneko.org/html/properties/names/attrs","no-change");
			parser.setProperty("http://cyberneko.org/html/properties/default-encoding",charEncoding);
			parser.parse(source);
	        Document doc = parser.getDocument();
	        String[] ids = elId.split(",");
	        StringBuffer inHtml = new StringBuffer();
	        for(int i=0;i<ids.length;i++){
				Node e = doc.getElementById(ids[i]);
				if(e == null)
					return null;
		        StringBuffer idInHtml = new StringBuffer();
		        visitHtmlNode(e,idInHtml);
		        inHtml.append("<whsHtml><![CDATA["+idInHtml.toString()+"]]></whsHtml>");
	        }
	        inHtml = new StringBuffer(styleSheet+"<whsRoot><whsId>"+elId+"</whsId>"+inHtml.toString()+"</whsRoot>");
			htmlStream.close();
	        htmlStream = new ByteArrayOutputStream();
	        htmlStream.write(inHtml.toString().getBytes(charEncoding));
			return htmlStream;
		}catch(Exception e){
			throw new RuntimeException(e.toString());
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void visitHtmlNode(Node node, StringBuffer inHtml){
		NodeList nl = node.getChildNodes();
		for(int i=0, cnt=nl.getLength(); i<cnt; i++){
			Node n = nl.item(i);
			short nodeType = n.getNodeType();
			switch(nodeType){
			
				case Node.ELEMENT_NODE:
					String nodeName = n.getNodeName();
					if(nodeName.equalsIgnoreCase("br")){
						inHtml.append("<br/>");
						break;
					}
					StringBuffer nodeAttr = new StringBuffer();
					NamedNodeMap nodeAttrMap = n.getAttributes();
					
					// Look for input text tags
					boolean isInputText = false;
					if(nodeName.equalsIgnoreCase("input")){
						for(int j=0;j<nodeAttrMap.getLength();j++){
							String attrName = nodeAttrMap.item(j).getNodeName();
							String attrValue = nodeAttrMap.item(j).getNodeValue();
							if( attrName.equalsIgnoreCase("type") && 
							   (attrValue.equalsIgnoreCase("button") || attrValue.equalsIgnoreCase("text") || attrValue.equalsIgnoreCase("password"))){
								isInputText = true;
								break;
							}
						}
					}
					
					// Create tag 
					for(int j=0;j<nodeAttrMap.getLength();j++){
						String attrName = nodeAttrMap.item(j).getNodeName();
						String attrValue = null;
						if(isInputText && attrName.equalsIgnoreCase("value"))
							attrValue = Tools.stringToHTMLString(nodeAttrMap.item(j).getNodeValue());
						else
							attrValue = nodeAttrMap.item(j).getNodeValue();
						String quot = "\"";
						if(attrValue.indexOf('\"') >= 0)
							quot = "'";
						nodeAttr.append(" "+attrName+"="+quot+attrValue+quot);
					}
					if(nodeAttr.length() > 0)
						inHtml.append("<"+nodeName+nodeAttr+">");
					else
						inHtml.append("<"+nodeName+">");
					visitHtmlNode(nl.item(i),inHtml);
					inHtml.append("</"+nodeName+">");
					break;
					
				case Node.TEXT_NODE:
					Node parent = n.getParentNode();
					if(parent != null && parent.getNodeName().equalsIgnoreCase("TEXTAREA"))
						inHtml.append(Tools.stringToHTMLString(n.getNodeValue()));
					else
						inHtml.append(Tools.convertSpecialChars(n.getNodeValue()));
					break;
			}
		}
	}
	
}
