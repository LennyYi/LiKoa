package com.aiait.eflow.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.aiait.framework.mvc.util.Global;

public class MultipartRequest implements HttpServletRequest {

    protected static String TEMP_PATH = "/upload/temp/";

	protected String userId_;

	protected HttpServletRequest req_;

	protected File dir_ = new File(TEMP_PATH);

	protected HashMap parameters_ = new HashMap();

	protected HashMap files_ = new HashMap();

	protected HashMap exactFileNameTable_ = new HashMap();

	static {
		TEMP_PATH = Global.WEB_ROOT_PATH + TEMP_PATH;
	}

	public static boolean isMultipart(HttpServletRequest req) {
		String type = null;
		String type1 = req.getHeader("Content-Type");
		String type2 = req.getContentType();
		if (type1 == null && type2 != null) {
			type = type2;
		} else if (type2 == null && type1 != null) {
			type = type1;
		} else if (type1 != null && type2 != null) {
			type = (type1.length() > type2.length() ? type1 : type2);
		}
		if (type == null
				|| !type.toLowerCase().startsWith("multipart/form-data")) {
			return false;
		} else {
			return true;
		}
	}

	public MultipartRequest(HttpServletRequest req, String userId)
			throws IOException {
		if (req == null)
			throw new IllegalArgumentException("request cannot be null");
		if (!dir_.isDirectory())
			dir_.mkdir();
		if (!dir_.canWrite())
			throw new IllegalArgumentException("服务器上 " + TEMP_PATH
					+ " 这个目录没有写的权限");
		req_ = req;
		userId_ = userId;
		readRequest();
	}

	protected void readRequest() throws IOException {
		int length = req_.getContentLength();

		String type = null;
		String type1 = req_.getHeader("Content-Type");
		String type2 = req_.getContentType();
		if (type1 == null && type2 != null) {
			type = type2;
		} else if (type2 == null && type1 != null) {
			type = type1;
		} else if (type1 != null && type2 != null) {
			type = (type1.length() > type2.length() ? type1 : type2);
		}
		if (type == null
				|| !type.toLowerCase().startsWith("multipart/form-data")) {
			throw new IOException(
					"Posted content type isn't multipart/form-data");
		}
		String boundary = extractBoundary(type);
		if (boundary == null) {
			throw new IOException("Separation boundary was not specified");
		}
		MultipartInputStreamHandler in = new MultipartInputStreamHandler(req_
				.getInputStream(), length);
		String line = in.readLine();
		if (line == null) {
			throw new IOException("Corrupt form data: premature ending");
		}
		if (!line.startsWith(boundary)) {
			throw new IOException("Corrupt form data: no leading boundary");
		}
		boolean done = false;
		while (!done) {
			done = readNextPart(in, boundary);
		}
	}

	private String extractBoundary(String line) {
		int index = line.lastIndexOf("boundary=");
		if (index == -1) {
			return null;
		}
		String boundary = line.substring(index + 9);
		boundary = "--" + boundary;
		return boundary;
	}

	protected boolean readNextPart(MultipartInputStreamHandler in,
			String boundary) throws IOException {
		String line = in.readLine();
		if (line == null) {
			return true;
		} else if (line.length() == 0) {
			return true;
		}

		String[] dispInfo = extractDispositionInfo(line);
		String strHttpParameterName = dispInfo[1];
		String strExactFileName = dispInfo[2];

		line = in.readLine();
		if (line == null) {
			return true;
		}

		String contentType = extractContentType(line);
		if (contentType != null) {
			line = in.readLine();
			if (line == null || line.length() > 0) {
				throw new IOException("Malformed line after content type: "
						+ line);
			}
		} else {
			contentType = "application/octet-stream";
		}

		if (strExactFileName == null) {
			String value = readParameter(in, boundary);
			Vector existingValues = (Vector) parameters_
					.get(strHttpParameterName);
			if (existingValues == null) {
				existingValues = new Vector();
				parameters_.put(strHttpParameterName, existingValues);
			}
			existingValues.addElement(value);
			//System.out.println("strHttpParameterName: " + strHttpParameterName);
			//System.out.println("value: " + value);
		} else {
			File temporaryFile;
			if (!strExactFileName.equals("unknown")) {
				String tempfilename = userId_ + strHttpParameterName
						+ System.currentTimeMillis() + ".bin";
				temporaryFile = new File(dir_, tempfilename);
			} else {
				temporaryFile = null;
			}

			readAndSaveFile(in, boundary, temporaryFile, contentType);

			if (!strExactFileName.equals("unknown")) {
				files_.put(strHttpParameterName, new UploadedFile(
						temporaryFile, contentType));
			}
			exactFileNameTable_.put(temporaryFile, strExactFileName);
		}
		return false;
	}

	protected void readAndSaveFile(MultipartInputStreamHandler in,
			String boundary, File temporaryFile, String contentType)
			throws IOException {
		OutputStream out = null;

		if (temporaryFile == null) {
			out = new ByteArrayOutputStream();
		} else {
			if (contentType.equals("application/x-macbinary")) {
				out = new MacBinaryDecoderOutputStream(
						new BufferedOutputStream(new FileOutputStream(
								temporaryFile), 8 * 1024));
			} else {
				out = new BufferedOutputStream(new FileOutputStream(
						temporaryFile), 8 * 1024);
			}
		}

		byte[] bbuf = new byte[16384]; //16k
		int result;
		String line;
		boolean rnflag = false;
		while ((result = in.readLine(bbuf, 0, bbuf.length)) != -1) {
			if (result > 2 && bbuf[0] == '-' && bbuf[1] == '-') {
				line = new String(bbuf, 0, result, "GBK");
				if (line.startsWith(boundary))
					break;
			}
			if (rnflag) {
				out.write('\r');
				out.write('\n');
				rnflag = false;
			}
			if (result >= 2 && bbuf[result - 2] == '\r'
					&& bbuf[result - 1] == '\n') {
				out.write(bbuf, 0, result - 2);
				rnflag = true;
			} else {
				out.write(bbuf, 0, result);
			}
		}
		out.flush();
		out.close();
	}

	private String[] extractDispositionInfo(String line) throws IOException {
		String[] retval = new String[3];

		String origline = line;
		line = origline.toLowerCase();

		int start = line.indexOf("content-disposition: ");
		int end = line.indexOf(";");
		if (start == -1 || end == -1) {
			throw new IOException("Content disposition corrupt: " + origline);
		}
		String disposition = line.substring(start + 21, end);
		if (!disposition.equals("form-data")) {
			throw new IOException("Invalid content disposition: " + disposition);
		}

		start = line.indexOf("name=\"", end);
		end = line.indexOf("\"", start + 7);
		if (start == -1 || end == -1) {
			throw new IOException("Content disposition corrupt: " + origline);
		}
		String name = origline.substring(start + 6, end);

		String filename = null;
		start = line.indexOf("filename=\"", end + 2);
		end = line.indexOf("\"", start + 10);
		if (start != -1 && end != -1) {
			filename = origline.substring(start + 10, end);
			int slash = Math.max(filename.lastIndexOf('/'), filename
					.lastIndexOf('\\'));
			if (slash > -1) {
				filename = filename.substring(slash + 1);
			}
			if (filename.equals(""))
				filename = "unknown";
		}

		retval[0] = disposition;
		retval[1] = name;
		retval[2] = filename;
		return retval;
	}

	private String extractContentType(String line) throws IOException {
		String contentType = null;

		String origline = line;
		line = origline.toLowerCase();

		if (line.startsWith("content-type")) {
			int start = line.indexOf(" ");
			if (start == -1) {
				throw new IOException("Content type corrupt: " + origline);
			}
			contentType = line.substring(start + 1);
		} else if (line.length() != 0) {
			throw new IOException("Malformed line after disposition: "
					+ origline);
		}

		return contentType;
	}

	protected String readParameter(MultipartInputStreamHandler in,
			String boundary) throws IOException {
		StringBuffer sbuf = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			if (line.startsWith(boundary))
				break;
			sbuf.append(line + "\r\n");
		}
		if (sbuf.length() == 0) {
			return "";
		}
		sbuf.setLength(sbuf.length() - 2);
		return sbuf.toString();
	}

	public File getFile(String strParameterName) {
		UploadedFile file = (UploadedFile) files_.get(strParameterName);

		if (file != null) {
			return file.getFile();
		} else {
			return null;
		}
	}

	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getDateHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIntHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter(String name) {
        Vector values = (Vector) this.parameters_.get(name);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return (String) values.get(0);
    }

	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRemotePort() {
		return 0;
	}

	public String getLocalName() {
		return null;
	}

	public java.lang.String getLocalAddr() {
		return null;
	}

	public int getLocalPort() {
		return 0;
	}

	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub

	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		return req_.getRequestDispatcher(arg0);
	}

	public void deleteTemporaryFile() {
		Set files = exactFileNameTable_.keySet();
		Iterator iteratorOfFiles = files.iterator();
		while (iteratorOfFiles.hasNext()) {
			File fileToDel = (File) iteratorOfFiles.next();
			if (fileToDel != null) {
				fileToDel.delete();
			}
		}
	}

	public String getFileName(File file) throws UnsupportedEncodingException {
		return (String) exactFileNameTable_.get(file);
	}

	public StringBuffer getRequestURL() {
		return req_.getRequestURL();
	}

	public Map getParameterMap() {
		return req_.getParameterMap();
	}

	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	public String getRealPath(String arg0) {
		return null;
	}
}

class MultipartInputStreamHandler {

	ServletInputStream in;

	int totalExpected;

	int totalRead = 0;

	byte[] buf = new byte[8 * 1024];

	MultipartInputStreamHandler(ServletInputStream in, int totalExpected) {
		this.in = in;
		this.totalExpected = totalExpected;
	}

	String readLine() throws IOException {
		StringBuffer sbuf = new StringBuffer();
		int result;
		do {
			result = this.readLine(buf, 0, buf.length);
			if (result != -1) {
				sbuf.append(new String(buf, 0, result, "GBK"));
			}
		} while (result == buf.length);
		if (sbuf.length() == 0) {
			return null;
		}
		sbuf.setLength(sbuf.length() - 2);
		return sbuf.toString();
	}

	int readLine(byte b[], int off, int len) throws IOException {
		if (totalRead >= totalExpected) {
			return -1;
		} else {
			if (len > (totalExpected - totalRead)) {
				len = totalExpected - totalRead;
			}
			int result = in.readLine(b, off, len);
			if (result > 0) {
				totalRead += result;
			}
			return result;
		}
	}
}

class UploadedFile {

	private File temporaryFile_;

	private String type_;

	UploadedFile(File temporaryFile, String type) {
		temporaryFile_ = temporaryFile;
		type_ = type;
	}

	String getContentType() {
		return type_;
	}

	File getFile() {
		return temporaryFile_;
	}

}

class MacBinaryDecoderOutputStream extends FilterOutputStream {

	private int bytesFiltered_ = 0;

	private int dataForkLength_ = 0;

	MacBinaryDecoderOutputStream(OutputStream out) {
		super(out);
	}

	public void write(int b) throws IOException {
		if (bytesFiltered_ <= 86 && bytesFiltered_ >= 83) {
			int leftShift = (86 - bytesFiltered_) * 8;
			dataForkLength_ = dataForkLength_ | (b & 0xff) << leftShift;
		} else if (bytesFiltered_ < (128 + dataForkLength_)
				&& bytesFiltered_ >= 128) {
			out.write(b);
		}
		bytesFiltered_++;
	}

	public void write(byte[] b, int off, int len) throws IOException {
		if (bytesFiltered_ >= (128 + dataForkLength_)) {
			bytesFiltered_ += len;
		} else if (bytesFiltered_ >= 128
				&& (bytesFiltered_ + len) <= (128 + dataForkLength_)) {
			out.write(b, off, len);
			bytesFiltered_ += len;
		} else {
			for (int i = 0; i < len; i++) {
				write(b[off + i]);
			}
		}
	}

}
