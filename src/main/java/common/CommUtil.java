package common;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtil{

    private static final String[] HEADER_CHCEK_CHAR={"%0d","\r","%0a","\n"};

    /**
     * @comment_ko 넘겨받은 데이타에  HEADER_CHCEK_CHAR Array 에 해당되는 값이 있는지를 체크한다.
     *             웹보안에서 reqeust 를 분리 할 수 있음 ( 파일다운로드시에 적용)
     * @param targetString
     * @return
     */
    public static boolean isHeaderCheckOk(String targetString){

        if(targetString==null){
            return true;
        }

        boolean result=true;

        String checkString=targetString.toLowerCase();

        for(int i=0,n=HEADER_CHCEK_CHAR.length;i<n;i++){

            if(checkString.indexOf(HEADER_CHCEK_CHAR[i]) != -1){
                result=false;
                break;
            }
        }

        return result;

    }

    /**
     *	넘겨받은 String Array 에 Null 이 하나라도 존재하면 Exception을 던진다.
     */
    public static String[] retNullParameterCheck(String ...s) throws Exception{

        if(s ==null) return null;

        for(int i=0,n=s.length;i<n;i++){
            if(s[i] == null || s[i].equals("")){
                //error
            }
        }

        return s;
    }

    /**
     *	null 인지 "" 인지를 체크하여 Null 을 리턴
     */
    public static String retNull(String s){

        try{
            if(s == null || s.equals("")) return null;
        }catch(Exception e){}

        return s.trim();
    }

    /**
     *	null 인지 "" 인지를 체크하여 Null 을 리턴 - 배열인 경우
     */
    public static String[] retNulls(String ...s){

        String[] strArray=s;

        if(strArray ==null) return null;

        try{
            for(int i=0;i<strArray.length;i++){
                if(strArray[i] == null || strArray[i].equals("")) strArray[i]=null;
            }
        }catch(Exception e){}

        return strArray;
    }

    /**
     *	null 인지 "" 인지를 체크하여 "" 을 리턴
     */
    public static String retSpace(String argStr){

        String s=argStr;

        try{
            if(s == null || s.equals("") || s.equals("null")) return "";
        }catch(Exception e){}

        return s.trim();
    }

    public static String ascToksc(String str) throws UnsupportedEncodingException	{
        if(str==null){ return null;}
        return new String(str.getBytes("8859_1"),"KSC5601");
    }

    public static String kscToasc(String str) throws UnsupportedEncodingException	{
        if(str==null){ return null;}
        return new String(str.getBytes("KSC5601"),"8859_1");
    }

    public static String[] convertToArray(Vector<String> paraVec){

        String[] parameter=null;

        if(paraVec !=null && paraVec.size() > 0){
            parameter= new String[paraVec.size()];
            paraVec.copyInto(parameter);
        }

        return parameter;
    }

    /**
     * String을 Hashtable 로 변환하여 리턴
     * 예) String asis="A=a#B=b" 일때 , firstDelimeter =>"#", secondDelimeter => "="
     * @param source
     * @param firstDelimeter
     * @param secondDelimeter
     * @return
     */
    public static Hashtable<String, String> convertStringToHash(String source, String firstDelimeter, String secondDelimeter){

        Hashtable<String, String> resultHash=new Hashtable<String, String>();

        if(CommUtil.retNull(source)==null) return resultHash;

        String[] mainArray=source.split(firstDelimeter);
        String[] contentsArray=null;

        if(mainArray.length==0) return resultHash;

        for(int i=0,n=mainArray.length;i<n;i++){

            contentsArray=mainArray[i].split(secondDelimeter);
            if(contentsArray.length==2){
                resultHash.put(contentsArray[0],contentsArray[1]);
            }
        }

        return resultHash;
    }

    /**
     * Hashtable 에 담겨 있는 것을 원하는 String 형태로 리턴
     * 예)
     * @param sourceHash
     * @param firstDelimeter
     * @param secondDelimeter
     * @return
     */
    public static String convertHashToString(Hashtable<String, String> sourceHash, String firstDelimeter, String secondDelimeter){

        if(sourceHash==null || sourceHash.isEmpty()) return "";

        String resultStr="";

        Enumeration<String> enu=sourceHash.keys();

        String key=null;
        String value=null;

        while(enu.hasMoreElements()){
            key=(String)enu.nextElement();
            value=(String)sourceHash.get(key);

            resultStr=resultStr+firstDelimeter+key+secondDelimeter+value;
        }

        return resultStr;
    }

    /**
     * @comment_ko 입력받은 url의 유효성을 체크하여 그 여부를 반환하는 메소드
     *             예)
     *             테스트 url [http://g2b.go.kr], 검증결과 [true]
     *             테스트 url [http://www.g2b.go.kr], 검증결과 [true]
     *             테스트 url [http://www.www.g2b.go.kr], 검증결과 [false]
     *             테스트 url [http://www.daum.net?url=g2b.go.kr], 검증결과 [false]
     *             테스트 url [http://aefefaeg2b.go.kr], 검증결과 [false]
     *             테스트 url [http://g2b.go.krwfefew], 검증결과 [false]
     *             테스트 url [https://www.g2b.go.kr], 검증결과 [true]
     *             테스트 url [g2b.go.kr], 검증결과 [false]
     *             테스트 url [http://www.g2b.go.kr/], 검증결과 [true]
     *             테스트 url [http://www.g2b.go.kr:8091/], 검증결과 [true]
     *             테스트 url [http://www.g2b.go.kr:8091/aefaf/aefasfgae/fa], 검증결과 [true]
     *             테스트 url [http://www.g2b.go.kr:80111/], 검증결과 [false]
     * @param _url
     * @return boolean (유효여부)
     */
    public static boolean checkG2BUrl(String _url){

        String url = retSpace(_url);

        String urlTemp=url.toLowerCase();

        if("".equals(url)){
            return true;
        }

        if(urlTemp.startsWith("http://")==false && urlTemp.startsWith("/")){
            return true;
        }

        Pattern p = Pattern.compile("^(?i)http(s)?://([a-z0-9]+\\.)?(g2b.go.kr)(:\\d{2,4})?(/.*$)?");
        Matcher m = p.matcher(url);

        return m.matches();
    }

    /**
     * URL 검증을 하기 위해, String 문구 체크와 URL 정합성 체크를 함
     * @param _url
     * @return
     */
    public static boolean checkUrlValid(String _url){

        if(isHeaderCheckOk(_url)==false){
            return false;
        }

        return checkG2BUrl(_url);
    }
}

