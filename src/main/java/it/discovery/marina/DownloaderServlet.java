package it.discovery.marina;


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/get/*")
public class DownloaderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PATH_ON_DRIVE = "C:\\base";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
			
		Path newPath = Paths.get(PATH_ON_DRIVE, Optional.ofNullable(request.getPathInfo()).orElse(""));

		if (Files.isDirectory(newPath)) 
		{
			showDirectoryContent(response, newPath, request.getRequestURL().toString());
		}

		if (Files.isRegularFile(newPath))
		{
			showFileContent(response, newPath);
			
		}
	}

	private void showDirectoryContent(HttpServletResponse response, Path newPath, String servletPath) 
	{
		response.setContentType("text/html");
		try(PrintWriter writer = response.getWriter();)
		{
			
			writer.append("Directory Content: <br/>");
			DirectoryStream<Path> stream = Files.newDirectoryStream(newPath);
			for (Path item : stream) {
				if (Files.isDirectory(item)) {
					 writer.append("Directory ");
				}
				writer.append("<a href=\"");
				writer.append(servletPath +"/"+item.getFileName());
				writer.append("\">");
				
				writer.append(item.getFileName().toString());
				writer.append("</a>").append("<br/>");
			}
		}
		catch(Exception ex)
		{
			
		}
	}
	
	private void showFileContent(HttpServletResponse response, Path newPath)
	{
		try{
			response.getOutputStream().write(Files.readAllBytes(newPath));
		}
		catch(Exception e)
		{
			
		}
	}
	
}