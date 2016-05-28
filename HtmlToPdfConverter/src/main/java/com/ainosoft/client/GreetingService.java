package com.ainosoft.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
  String greetServer(String name) throws IllegalArgumentException;
  
  String getGeneratedPdfLinkSelenium(String siteToDownload)throws IllegalArgumentException;
  
  String getGeneratePdfLinkProcessCall(String siteToDownload) throws IllegalArgumentException;
  
  String getGeneratePdfLinkWkHtmlToPdf(String siteToDownload) throws IllegalArgumentException;

}
