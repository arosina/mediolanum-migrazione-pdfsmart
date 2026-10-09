function getWaitHtmlString(){
	return "<div style='position:absolute;top:35%;left:35%;z-index:1001;'>"+
			"<div style='z-index:2001;position:relative;width:300;height:80;'>"+
			"<table width='100%' height='100%' bgcolor='#F0F0F0' style='border:solid 1px #1A458F;'>"+
				"<tr>"+
					"<td align='center' valign='middle' style='font-family:Arial;font-size:11pt;color: #1A458F;'>"+
						"<img src='/wfemlayout/images/wait.gif'/>&nbsp;&nbsp;Attendere prego"+
					"</td>"+
				"</tr>"+
			"</table>"+
			"</div>"+
			"<div style='filter:alpha(opacity=60);z-index:2000;background-color:silver;position:absolute;left:10;top:10;width:300;height:80;'></div>"+
			"</div>";
}
