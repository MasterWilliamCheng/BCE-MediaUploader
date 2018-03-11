<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.io.FileUtils" %>
<%@ page import="com.snh.common.CommonConfig" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type", "text/html");
	
	String rootPath = application.getRealPath( "/" );
	
	out.write(new ActionEnter(request, rootPath).exec());

	File folder = new File(rootPath + File.separator + CommonConfig.COLDJOKE_RELATIVE_PARENT_PATH);
	File[] files = folder.listFiles();
	String filePath = "";
	for (File tmpFile : files) {
		filePath = CommonConfig.COLDJOKE_RELATIVE_PARENT_PATH + File.separator + tmpFile.getName();
		FileUtils.moveFile(new File(rootPath + filePath), new File(CommonConfig.COMMON_FILE_ROOT_PATH + filePath));
	}
%>