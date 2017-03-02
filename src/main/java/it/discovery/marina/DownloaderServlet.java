package it.discovery.marina;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="/get/*")
public class DownloaderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PATH_ON_DRIVE = "C:\\base";
	private static final int PAGE_SIZE = 20000;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	{
			
		Path newPath = Paths.get(PATH_ON_DRIVE, Optional.ofNullable(request.getPathInfo()).orElse(""));

		if (Files.isDirectory(newPath)) 
		{
			showDirectoryContent(response, newPath);
		}

		if (Files.isRegularFile(newPath))
		{
			showFileContent(response, newPath);
		}
	}

	private void showDirectoryContent(HttpServletResponse response, Path newPath) {
		
		try(PrintWriter writer = response.getWriter();)
		{
			writer.append("Directory Content: \n");
		
			DirectoryStream<Path> stream = Files.newDirectoryStream(newPath);
			for (Path item : stream) {
				if (Files.isDirectory(item)) {
					writer.append("<Directory> ");
				}
				writer.append(item.getFileName().toString()).append("\n");
			}
		}
		catch(Exception ex)
		{
			
		}
	}
	
	private void showFileContent(HttpServletResponse response, Path newPath) 
	{
		try (ServletOutputStream servletOut = response.getOutputStream();
				BufferedOutputStream bufferedOut = new BufferedOutputStream(servletOut);
				FileInputStream fileIn = new FileInputStream(newPath.toFile());
				BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);) 
		{
			int size = fileIn.available();
			while (size > 0) {
				int bufSize = (size > PAGE_SIZE) ? PAGE_SIZE : size;
				byte[] bytes = new byte[bufSize];
				int read = bufferedIn.read(bytes, 0, bufSize);
				if(read!=-1)
				{
					bufferedOut.write(bytes, 0, read);
				}
				size = fileIn.available();
			
			}
		} catch (Exception ex) {
		
		}
	}
}