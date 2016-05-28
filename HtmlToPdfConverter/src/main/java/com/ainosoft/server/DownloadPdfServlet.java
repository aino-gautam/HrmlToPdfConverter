/**
 * 
 */
package com.ainosoft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.eti.mertz.wkhtmltopdf.wrapper.Pdf;
import br.eti.mertz.wkhtmltopdf.wrapper.page.PageType;

public class DownloadPdfServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		performTask(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		performTask(request, response);
	}

	private void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		try{
			String sitePdf = "siteDownload.pdf";

			String path = DownloadPdfServlet.class.getClassLoader().getResource("").getPath()+sitePdf;

			String siteToDownload = request.getParameter("urlToParse");

			{
				File oldFile = new File(path);
				if(oldFile.exists())
					oldFile.delete();
			}

			// using the Runtime exec method:
			//	Process p = Runtime.getRuntime().exec("wkhtmltopdf "+siteToDownload+ " " +path);

			Pdf pdfGen = new Pdf();

			pdfGen.addPage(siteToDownload, PageType.url);

			pdfGen.saveAs(path);

			//Wait for process to complete
			//	p.waitFor();

			File pdfFile = new File(path);

			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=" + sitePdf);
			response.setContentLength((int) pdfFile.length());

			FileInputStream fileInputStream = new FileInputStream(pdfFile);
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}

			fileInputStream.close();
			responseOutputStream.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}