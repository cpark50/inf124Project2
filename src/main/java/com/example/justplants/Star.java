package com.example.justplants;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Star {
    private String id;
	private int rate;	// 1 ~ 5
	private int left;
	private int top;
	private int size;	// The PX size of each star
	private int number;	// The number of stars
	private boolean absolute;
	
	Star(String id, int number, int initialRate, int left, int top, int size, boolean absolute) {
		this.id = id;
		this.number = number;
		this.rate = initialRate;
		this.left = left;
		this.top = top;
		this.size = size;
		this.absolute = absolute;
	}
	
	Star(String id, int initialRate) {
		this.id = id;
		this.number = 5;
		this.rate = initialRate;
		this.left = 0;
		this.top = 0;
		this.size = 32;
		this.absolute = false;
	}
	
	private String getHTML() {
		StringBuilder sb = new StringBuilder();
		//<table>
		//Setup position
		sb.append("<table style='margin-left: auto; margin-right: auto; position:");
		if(absolute)
			sb.append("absolute; ");
		else
			sb.append("relative; ");
		//Setup left/top
		sb.append("left:");
		sb.append(this.left);
		sb.append("px; top:");
		sb.append(this.top);
		sb.append("px;'><tr>\n");
		//<td>
		for(int i=1; i<=this.number; i++) {
			//Beginning of cell
			sb.append("<td>");
			
			//IMPORTANT!!!
			//The id of <div> MUST be set to '<PRODUCT NAME>_star_<STAR INDEX>'
			sb.append("<div id='");
			sb.append(id);	//Product Name
			sb.append("_star_"); 	
			sb.append(i);	//Star Index
			
			//Size
			sb.append("' style='height:");			
			sb.append(this.size);
			sb.append("px; width:");			
			sb.append(this.size);	
			sb.append("px;'");
			
			//There are two CSS class, star-gray and star-red
			sb.append(" class='star-");
			if(i<=this.rate)
				sb.append("red'");
			else
				sb.append("gray'");
			
			//When the mouse move over these cells, it will call JS 'onMouseOver()' function
			sb.append(" onmouseover='onMouseOver(this)'");
			
			//End of this cell
			sb.append(" /></td>\n");
		}
		//</tr></table>
		sb.append("</tr>");
		
		sb.append("</table>\n");
		return sb.toString();
	}
	
	
	public void appendStarHTML(HttpServletResponse response) throws IOException {
		response.getWriter().append(getHTML());
	}
}

