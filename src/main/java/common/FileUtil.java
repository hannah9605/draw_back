package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    private static final Logger LOG =  LoggerFactory.getLogger(FileUtil.class);


    /**
     * 첨부파일을 다운로드한다.
     * @param file
     * @param orgnlFileNm
     * @param request
     * @param response
     * @throws Exception
     */
    public void download(File file, String orgnlFileNm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int fSize = (int)file.length();

        if (fSize > 0) {
            FileInputStream fis = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            try {
                response.setContentType("application/octet-stream");
                response.setContentLength(fSize);

                setDisposition(orgnlFileNm, request, response);
                response.setHeader("Set-Cookie", "fileDownload=true; path=/");

                FileCopyUtils.copy(fis, out);

                out.flush();
            } catch (FileNotFoundException ex){
                LOG.debug("IGNORED: " + ex.getMessage());
            } catch (IOException ex){
                LOG.debug("IGNORED: " + ex.getMessage());
            } catch (Exception ex) {
                LOG.debug("IGNORED: " + ex.getMessage());
            } finally {
                if (fis != null) {
                    try { fis.close(); } catch (IOException e) { LOG.error("■■■■■■■■■■■■■■■ 첨부파일다운 요청 SQL 오류 : {}", e.getMessage()); }
                }
                if( out != null ) {
                    try { out.close(); } catch(IOException e) {	LOG.error("■■■■■■■■■■■■■■■ 첨부파일다운 요청 SQL 오류 : {}", e.getMessage()); }
                }
            }
        } else {
            response.setHeader("Set-Cookie", "fileDownload=false; path=/");
            if (FileUtil.getBrowser(request).equals("MSIE")) {
                response.setContentType("application/x-msdownload");
                response.setCharacterEncoding("utf-8");

                PrintWriter printwriter = response.getWriter();
                printwriter.println("<html>");
                printwriter.println("fail");
                printwriter.println("</html>");
                printwriter.flush();
                printwriter.close();
            }
        }

    }

    /**
     * 해당 경로의 파일을 다운로드한다.
     * @param filePath
     * @param fileRename
     * @param request
     * @param response
     * @throws Exception
     */
    public void download(String filePath, String fileRename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = new File(filePath);
        download(file, fileRename, request, response);
    }

    /**
     * Disposition 지정하기.
     *
     * @param filename
     * @param request
     * @param response
     * @throws Exception
     */
    public static void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String browser = getBrowser(request);

        String dispositionPrefix = "attachment; filename=";
        String encodedFilename;

        if ("MSIE".equals(browser) || "Trident".equals(browser)) {
            // IE 및 IE11을 위한 처리
            encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        } else {
            // Chrome, Firefox, Opera 등 기타 브라우저를 위한 처리
            // RFC 5987 인코딩 사용
            encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        }

        if ("MSIE".equals(browser) || "Trident".equals(browser) || "Chrome".equals(browser)) {
            // Chrome 포함 대부분의 브라우저에서는 다음 형태로 설정
            response.setHeader("Content-Disposition", dispositionPrefix + "\"" + encodedFilename + "\"");
        } else {
            // Firefox 및 Opera에서 RFC 5987 인코딩을 지원하므로, 다음과 같이 설정
            response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);
        }
    }


    /**
     * 브라우저 구분 얻기.
     *
     * @param request
     * @return
     */
    public static String getBrowser( HttpServletRequest request ) {
        String header = request.getHeader( "User-Agent" );

        if( header.indexOf("MSIE") > -1 )           return "MSIE";
        else if( header.indexOf("Trident") > -1 )   return "Trident";
        else if( header.indexOf("Chrome") > -1 )    return "Chrome";
        else if( header.indexOf("Opera") > -1 )     return "Opera";

        return "Firefox";
    }


    /**
     * Charset 변경
     *
     * @param fileName
     * @return
     */
    public static String convCharset( String fileName ) throws Exception{
        if ( fileName == null || "".equals(fileName) )
            return fileName;

        return new String( fileName.getBytes("euc-kr"), "8859_1" );
    }
}
