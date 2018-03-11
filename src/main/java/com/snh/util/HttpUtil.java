package com.snh.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by beauty on 2015/8/24.
 */
public class HttpUtil {

	private static final String LINE_FEED = "\r\n";//System.lineSeparator();

	private static final int CONNECTION_TIMEOUT = 30000;

	private static final int READ_TIMEOUT = 30000;

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static JsonParser parser = new JsonParser();

	/**
	 * Send a get request
	 *
	 * @param url
	 * @return response
	 * @throws IOException
	 */
	static public String get( String url ) throws IOException {
		return get( url, null );
	}

	/**
	 * Send a get request
	 *
	 * @param url     Url as string
	 * @param headers Optional map with headers
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String get( String url,
	                          Map<String, String> headers ) throws IOException {
		return fetch( "GET", url, null, headers );
	}

	/**
	 * Send a post request
	 *
	 * @param url     Url as string
	 * @param body    Request body as string
	 * @param headers Optional map with headers
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String post( String url, String body,
	                           Map<String, String> headers ) throws IOException {
		return fetch( "POST", url, body, headers );
	}

	/**
	 * Send a post request
	 *
	 * @param url  Url as string
	 * @param body Request body as string
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String post( String url, String body ) throws IOException {
		return post( url, body, null );
	}

	/**
	 * Post a form with parameters
	 *
	 * @param url    Url as string
	 * @param params map with parameters/values
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String postForm( String url, Map<String, String> params )
			throws IOException {
		return postForm( url, params, null );
	}

	/**
	 * Post a form with parameters
	 *
	 * @param url     Url as string
	 * @param params  Map with parameters/values
	 * @param headers Optional map with headers
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String postForm( String url, Map<String, String> params,
	                               Map<String, String> headers ) throws IOException {
		// set content type
		if (headers == null) {
			headers = new HashMap<>();
		}
		headers.put( "Content-Type", "application/x-www-form-urlencoded;charset=utf-8" );

		// parse parameters
		String body = "";
		if (params != null) {
			boolean first = true;
			for (String param : params.keySet()) {
				if (first) {
					first = false;
				} else {
					body += "&";
				}
				String value = params.get( param );
				body += URLEncoder.encode( param, DEFAULT_CHARSET ) + "=";
				body += URLEncoder.encode( value, DEFAULT_CHARSET );
			}
		}

		return post( url, body, headers );
	}

	/**
	 * Send a put request
	 *
	 * @param url     Url as string
	 * @param body    Request body as string
	 * @param headers Optional map with headers
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String put( String url, String body,
	                          Map<String, String> headers ) throws IOException {
		return fetch( "PUT", url, body, headers );
	}

	/**
	 * Send a put request
	 *
	 * @param url Url as string
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String put( String url, String body ) throws IOException {
		return put( url, body, null );
	}

	/**
	 * Send a delete request
	 *
	 * @param url     Url as string
	 * @param headers Optional map with headers
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String delete( String url,
	                             Map<String, String> headers ) throws IOException {
		return fetch( "DELETE", url, null, headers );
	}

	/**
	 * Send a delete request
	 *
	 * @param url Url as string
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String delete( String url ) throws IOException {
		return delete( url, null );
	}

	/**
	 * Append query parameters to given url
	 *
	 * @param url    Url as string
	 * @param params Map with query parameters
	 * @return url        Url with query parameters appended
	 * @throws IOException
	 */
	static public String appendQueryParams( String url,
	                                        Map<String, String> params ) throws IOException {
		String fullUrl = url;

		if (params != null) {
			boolean first = ( fullUrl.indexOf( '?' ) == -1 );
			for (String param : params.keySet()) {
				if (first) {
					fullUrl += '?';
					first = false;
				} else {
					fullUrl += '&';
				}
				String value = params.get( param );
				fullUrl += URLEncoder.encode( param, DEFAULT_CHARSET ) + '=';
				fullUrl += URLEncoder.encode( value, DEFAULT_CHARSET );
			}
		}

		return fullUrl;
	}

	/**
	 * Retrieve the query parameters from given url
	 *
	 * @param url Url containing query parameters
	 * @return params     Map with query parameters
	 * @throws IOException
	 */
	static public Map<String, String> getQueryParams( String url )
			throws IOException {
		Map<String, String> params = new HashMap<String, String>();

		int start = url.indexOf( '?' );
		while (start != -1) {
			// read parameter name
			int equals = url.indexOf( '=', start );
			String param = "";
			if (equals != -1) {
				param = url.substring( start + 1, equals );
			} else {
				param = url.substring( start + 1 );
			}

			// read parameter value
			String value = "";
			if (equals != -1) {
				start = url.indexOf( '&', equals );
				if (start != -1) {
					value = url.substring( equals + 1, start );
				} else {
					value = url.substring( equals + 1 );
				}
			}

			params.put( URLDecoder.decode( param, DEFAULT_CHARSET ),
					URLDecoder.decode( value, DEFAULT_CHARSET ) );
		}

		return params;
	}

	/**
	 * Returns the url without query parameters
	 *
	 * @param url Url containing query parameters
	 * @return url        Url without query parameters
	 * @throws IOException
	 */
	static public String removeQueryParams( String url )
			throws IOException {
		int q = url.indexOf( '?' );
		if (q != -1) {
			return url.substring( 0, q );
		} else {
			return url;
		}
	}

	/**
	 * Send a request
	 *
	 * @param method  HTTP method, for example "GET" or "POST"
	 * @param url     Url as string
	 * @param body    Request body as string
	 * @param headers Optional map with headers
	 * @return response   Response as string
	 * @throws IOException
	 */
	static public String fetch( String method, String url, String body,
	                            Map<String, String> headers ) throws IOException {
		// connection
		URL u = new URL( url );
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setConnectTimeout( CONNECTION_TIMEOUT );
		conn.setReadTimeout( READ_TIMEOUT );
		// method
		if (method != null) {
			conn.setRequestMethod( method );
		}
		// headers
		if (headers != null) {
			for (String key : headers.keySet()) {
				conn.addRequestProperty( key, headers.get( key ) );
			}
		}
		// body
		if (body != null) {
			conn.setDoOutput( true );
			OutputStream os = conn.getOutputStream();
			os.write( body.getBytes( DEFAULT_CHARSET ) );
			os.flush();
			os.close();
		}
		// response
		String response;
		InputStream is;
		int responseCode = conn.getResponseCode();
		if (String.valueOf( responseCode ).startsWith( "2" )) {
			is = conn.getInputStream();
		} else {
			// handle redirects
			if (conn.getResponseCode() == 301) {
				String location = conn.getHeaderField( "Location" );
				return fetch( method, location, body, headers );
			} else {
				is = conn.getErrorStream();
			}
		}
		response = streamToString( is );
		is.close();
		return response;
	}

	/**
	 * Read an input stream into a string
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	static public String streamToString( InputStream in ) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[ 4096 ];
		for (int n; ( n = in.read( b ) ) != -1; ) {
			out.append( new String( b, 0, n, DEFAULT_CHARSET ) );
		}
		return out.toString();
	}


	/**
	 * post请求
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static int postToGetStatus( String url, String param ) {
		String rst;
		try {
			rst = post( url, param, createHeader() );
		} catch (Exception e) {
			return 500;
		}

		JsonObject rstJson = parser.parse( rst ).getAsJsonObject();
		int status = 0;
		if (!org.springframework.util.StringUtils.isEmpty( rstJson )) {
			status = rstJson.has( "status" ) ? rstJson.get( "status" ).getAsInt() : 500;
			if (status != 200) {
				LogUtil.error("url :" + url + "\trstJson : " + rstJson);
			}
		}
		return status;
	}

	public static String postFile( String url, Map<String, String> params, Map<String, File> files, Map<String, String> headers ) throws IOException {
		// connection
		URL u = new URL( url );
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setConnectTimeout( CONNECTION_TIMEOUT );
		conn.setReadTimeout( CONNECTION_TIMEOUT );
		// headers
		if (headers != null) {
			for (String key : headers.keySet()) {
				conn.addRequestProperty( key, headers.get( key ) );
			}
		}
		// 发送POST请求必须设置如下两行
		conn.setDoOutput( true );
		conn.setDoInput( true );
		conn.setUseCaches( false );
		conn.setRequestMethod( "POST" );
		String boundary = "---------" + System.currentTimeMillis(); // 定义数据分隔线
		conn.setRequestProperty( "Content-Type", "multipart/form-data; boundary=" + boundary );
		OutputStream outputStream = conn.getOutputStream();
		PrintWriter writer = new PrintWriter( new OutputStreamWriter( outputStream, "UTF-8" ), true );

		if (params != null) {
			for (String param : params.keySet()) {
				String value = params.get( param );
				writer.append( "--" ).append( boundary ).append( LINE_FEED );
				writer.append( "Content-Disposition: form-data; name=\"" ).append( param ).append( "\"" ).append( LINE_FEED );
				writer.append( "Content-Type: text/plain; charset=UTF-8" ).append( LINE_FEED );
				writer.append( LINE_FEED ).append( value ).append( LINE_FEED ).flush();
			}
		}
		Set<String> names = files.keySet();
		for (String name : names) {
			File file = files.get( name );
			addFilePart( outputStream, writer, boundary, name, file );
		}
//		writer.append( LINE_FEED ).flush();
		writer.append( "--" ).append( boundary ).append( "--" ).append( LINE_FEED );
		writer.close();
		outputStream.flush();
		outputStream.close();

		// response
		InputStream is = conn.getInputStream();
		String response = streamToString( is );
		is.close();

		return response;
	}

	/**
	 * Adds a upload file section to the request
	 *
	 * @param fieldName  name attribute in <input type="file" name="..." />
	 * @param uploadFile a File to be uploaded
	 * @throws IOException
	 */
	private static void addFilePart( OutputStream outputStream, PrintWriter writer, String boundary, String fieldName, File uploadFile )
			throws IOException {
		String fileName = uploadFile.getName();
		writer.append( "--" ).append( boundary ).append( LINE_FEED );
		writer.append( "Content-Disposition: form-data; name=\"" ).append( fieldName ).append( "\"; filename=\"" ).append( fileName ).append( "\"" )
				.append( LINE_FEED );
		writer.append( "Content-Type: " ).append( URLConnection.guessContentTypeFromName( fileName ) )
				.append( LINE_FEED );
		writer.append( "Content-Transfer-Encoding: binary" ).append( LINE_FEED );
		writer.append( LINE_FEED );
		writer.flush();

		FileInputStream inputStream = new FileInputStream( uploadFile );
		byte[] buffer = new byte[ 4096 ];
		int bytesRead;
		while (( bytesRead = inputStream.read( buffer ) ) != -1) {
			outputStream.write( buffer, 0, bytesRead );
			outputStream.flush();
		}
		inputStream.close();
		writer.append( LINE_FEED );
		writer.flush();
	}

	public static Map<String, String> createHeader() {
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put( "Content-Type", "application/json" );
		return requestHeader;
	}
}
