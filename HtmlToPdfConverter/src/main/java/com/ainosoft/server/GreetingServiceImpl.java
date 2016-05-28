package com.ainosoft.server;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.eti.mertz.wkhtmltopdf.wrapper.Pdf;
import br.eti.mertz.wkhtmltopdf.wrapper.page.PageType;

import com.ainosoft.client.GreetingService;
import com.ainosoft.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 *
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
				">", "&gt;");
	}

	@Override
	public String getGeneratedPdfLinkSelenium(String siteToDownload)throws IllegalArgumentException {
		WebDriver driver = null ;
		String path= "";
		try {
			final File screenShot = new File("screenshot.png").getAbsoluteFile();

			driver = new FirefoxDriver();

			driver.get(siteToDownload);

			TimeUnit.SECONDS.sleep(5);

			final File outputFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(outputFile, screenShot);

			path = screenShot.getCanonicalPath();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}
		return path;
	}

	@Override
	public String getGeneratePdfLinkWkHtmlToPdf(String siteToDownload) throws IllegalArgumentException {
		String path= null;
		try{
			path = GreetingServiceImpl.class.getClassLoader().getResource("").getPath()+"download.pdf";
		
			Pdf pdfGen = new Pdf();
			
			pdfGen.addPage(siteToDownload, PageType.url);

			pdfGen.saveAs(path);
		} catch (Exception e){
			e.printStackTrace();
		}
		return path;
	}

	@Override
	public String getGeneratePdfLinkProcessCall(String siteToDownload)	throws IllegalArgumentException {
		String path= null;
		try{
			path = GreetingServiceImpl.class.getClassLoader().getResource("").getPath()+"download.pdf";
		
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec("wkhtmltopdf "+siteToDownload+ " " +path);
			
           // Process p = Runtime.getRuntime().exec("wkhtmltopdf.exe " + reqURL + "?" + reqQuery + " c:/PDF/" + folderName + "/" + id + "/" + folderName + ".pdf");

			
			//Wait for process to complete
			p.waitFor();
		} catch (Exception e){
			e.printStackTrace();
		}
		return path;
	} 
}