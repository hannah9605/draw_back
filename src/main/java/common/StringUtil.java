package common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtil {

    private static final String[] CHECK_FILTER_SPECIAL_CHAR={"<", ">", "\'", "\""};
    private static final String[] CHECK_FILTER_SPECIAL_CHAR_CONVERT={"&#60;", "&#62;", "&#39;", "&#34;"};

    public static String[] getCheckFilterSpecialChar(){
        return CHECK_FILTER_SPECIAL_CHAR;
    }

    public static String[] getCheckFilterSpecialCharConvert(){
        return CHECK_FILTER_SPECIAL_CHAR_CONVERT;
    }

    public static String num2Han(String strNum) {

        String num=strNum;

        if(strNum ==null){
            return "";
        }

        // , 제거
        num=eraseComma(num);

        int jarisu=0;
        String[] resultValue=new String[2];

        int zero=0;
        String result="";

        if(num.indexOf('.') !=-1){
            jarisu=num.indexOf('.');
            resultValue[0]=num.substring(0,jarisu);// 정수부분
            resultValue[1]=num.substring(jarisu+1);// 소수부분
        }else{
            jarisu=num.length();
            resultValue[0]=num;
            resultValue[1]=null;
        }

        for(int i=0; i < resultValue[0].length() ; i++) {

            if(resultValue[0].charAt(i) == '0') {
                zero++;
                if((jarisu-1)%4 == 0 && zero < 4){
                    if(!checkUnit(result, jarisu-1)){
                        result = result + addDan(jarisu-1);
                    }
                }
            } else {
                if(!checkUnit(result, jarisu-1)){
                    result = result + outNum(resultValue[0].charAt(i));
                    result = result + addDan(jarisu-1);
                }else{
                    result = result + outNum(resultValue[0].charAt(i));
                }

                zero = 0;
            }
            jarisu --;
        }

        result += "원";

        if(resultValue[1] != null) {
            for(int i = 0 ;  i < resultValue[1].length() ; i ++ ) {
                if(i == 0 ){
                    if(resultValue[1].charAt(i) != '0'){
                        result += outNum(resultValue[1].charAt(i)) + "십";
                    }
                }
                if(i == 1){
                    if(resultValue[1].charAt(i) != '0'){
                        result += outNum(resultValue[1].charAt(i)) + "전";
                    }
                }
                if(i == 2){
                    if(resultValue[1].charAt(i) != '0'){
                        result += outNum(resultValue[1].charAt(i)) + "리";
                    }
                }
            }

            if(resultValue[1].length() < 2){
                result += "전";   //  jeon
            }
        }

        return result;
    }

    public static String eraseComma(String  strMoney) {

        String result = "";

        if( strMoney==null || strMoney.equals("")){
            result="";
        }else{
            for(int i=0;i<strMoney.length();i++) {
                char c = strMoney.charAt(i);
                if ( c != ',' ) {
                    result += c;
                }
            }
        }

        return result.trim();
    }

    private static String addDan(int argJarisu) {

        int jarisu=argJarisu;

        String temp=null;

        switch(jarisu) {
            case 1 :
            case 5 :
            case 9 :
            case 13 :	temp="십";    //  10
                break;

            case 2 :
            case 6 :
            case 10 :
            case 14 :	temp="백";    //  100
                break;

            case 3 :
            case 7 :
            case 11 :
            case 15 :	temp="천";   //  1,000
                break;

            case 4 : 	temp="만";   //  10,000
                break;

            case 8 :	temp="억";   //  100,000,000
                break;

            case 12 :	temp="조";   //  1,000,000,000,000
                break;
            default : 	temp= "";
        }

        return temp;
    }


    private static String outNum(char number) {

        String temp=null;

        switch(number) {

            case '1' :	temp= "일";
                break;

            case '2' :	temp= "이";
                break;

            case '3' :	temp= "삼";
                break;

            case '4':	temp= "사";
                break;

            case '5' :	temp= "오";
                break;

            case '6' :	temp= "육";
                break;

            case '7':	temp= "칠";
                break;

            case '8' :	temp= "팔";
                break;

            case '9' :	temp= "구";
                break;

            case '0' :	temp= "영";
                break;

            default  : temp="";
        }

        return temp;
    }

    private static boolean checkUnit(String result, int jarisu){

        boolean isExists=false;

        if(result !=null){
            if(	jarisu ==4){
                if(result.indexOf("만") != -1){
                    isExists=true;
                }
            }else if(jarisu==8){
                if(result.indexOf("억") != -1){
                    isExists=true;
                }
            }else if(jarisu==12){
                if(result.indexOf("조") != -1){
                    isExists=true;
                }
            }
        }

        return isExists;
    }

    public static String makeComma(String strMoney) {
        String result = " ";
        NumberFormat nf = NumberFormat.getInstance(Locale.KOREAN);

        if(strMoney == null || strMoney.equals("")){
            result ="";
        }else{
            result = nf.format(Double.parseDouble(strMoney));
        }

        return result.trim();
    }

    public static String makeComma(double strMoney) {
        String result = " ";
        NumberFormat nf = NumberFormat.getInstance(Locale.KOREAN);

        result = nf.format(strMoney);
        return result.trim();
    }

    /**
     * @comment_ko source : "{0} 은/는 {0} 자리 이상, {0}자리 이하로 입력하여 주십시오.";
     * delemeter : {0}
     * s : {0} 개수 만큼 replace하려는 값 입력
     * 사용방법 : StringUtil.replace(source,"{0}","01","10", "40")
     * 결과 : 01 은/는 10 자리 이상, 40자리 이하로 입력하여 주십시오.
     * @param source replace
     * @param delemeter
     * @param s
     * @return
     */
    public static String replace(String strSource, String delemeter, String ...s){

        String source=strSource;


        if (source == null || source.length()==0 || s == null || delemeter == null || delemeter.length()==0 ){
            return null;
        }

        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        int i = 0;
        try{
            while ( source.indexOf(delemeter) >= 0 ) {
                preStr = source.substring(0, source.indexOf(delemeter));
                nextStr = source.substring(source.indexOf(delemeter)+delemeter.length(), source.length());
                source = nextStr;
                rtnStr.append(preStr).append(CommUtil.retSpace(s[i]));
                i++;
            }
        }catch(Exception ex){}

        rtnStr.append(nextStr);

        return rtnStr.toString();
    }

    /**
     * @comment_ko source : "{0} 은/는 {0} 자리 이상, {0}자리 이하로 입력하여 주십시오.";
     * delemeter : {0}
     * s : {0} 개수 만큼 replace하려는 값 입력
     * 사용방법 : StringUtil.replace(source,"{0}","01")
     * 결과 : 01 은/는 01 자리 이상, 01자리 이하로 입력하여 주십시오.
     * @param   String arg
     * @return  String
     */
    public static String replace(String strSource, String subject, String object) {

        String source=strSource;

        if (source == null || source.length()==0 || subject == null || subject.length()==0 || object == null){
            return null;
        }
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        while ( source.indexOf(subject) >= 0 ) {
            preStr = source.substring(0, source.indexOf(subject));
            nextStr = source.substring(source.indexOf(subject)+subject.length(), source.length());
            source = nextStr;
            rtnStr.append(preStr).append(object);
        }
        rtnStr.append(nextStr);
        return rtnStr.toString();
    }

    /**
     * 특수문자를 toToken문자열로 바꾸어서 리턴한다.<br/>
     * 싱클 쿼테이션(') -> 더블쿼테이션('')
     * @param source
     * @param token
     * @param toToken
     * @return
     */
    public static String replace(String source, char token, String toToken) {

        if(source == null){
            return null;
        }

        String tot_str = "";

        for(int i = 0; i < source.length(); i++) {
            if(source.charAt(i) == token) {
                tot_str += toToken;
            } else {
                tot_str += source.charAt(i);
            }
        }

        return tot_str;
    }

    /**
     * @comment_ko source 중 첫번째 subject 를 object로 변환
     * @param   String arg
     * @return  String
     */
    public static String replaceOnce(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        if ( source.indexOf(subject) >= 0 ) {
            preStr = source.substring(0, source.indexOf(subject));
            nextStr = source.substring(source.indexOf(subject)+subject.length(), source.length());
            rtnStr.append(preStr).append(object).append(nextStr);
            return rtnStr.toString();
        } else {
            return source;
        }
    }

    /**
     * null 인지 "" 인지를 체크하여 Null 을 리턴
     * @param str
     * @return
     */
    public static String rtnNull(String str){
        try{
            if(str == null || str.equals("")){
                return null;
            }
        }catch(Exception e){
        }
        return str.trim();
    }



    /**
     * @comment_ko 넘겨받은 데이타중 xml 에서 허용하지 않는 문자를 치환한다.
     * @param sData
     * @return
     */
    public static String encodeXML(String sData){

        String arg_sData=CommUtil.retSpace(sData);

        if(arg_sData.length()==0){
            return sData;
        }

        String[] before = {"&", "<", ">"};
        String[] after = {"&amp;","&lt;", "&gt;"};

        for(int i = 0; i<before.length; i++){

            arg_sData = replace(arg_sData, before[i], after[i]);
        }

        return arg_sData;
    }

    public static String replaceMidFirst(String source, String firStr, String secondStr, String appendStr){

        String result=CommUtil.retSpace(source);

        if(result.equals("")){
            return source;
        }

        int firstIndex=result.indexOf(firStr)+firStr.length();
        int secondIndex=source.indexOf(secondStr);

        if(firstIndex == -1 || secondIndex== -1){
            return source;
        }

        result=source.substring(0,firstIndex)+appendStr+source.substring(secondIndex);

        return result;
    }

    public static String removeMidFirst(String source, String firStr, String secondStr){

        String result=CommUtil.retSpace(source);

        if(result.equals("")){
            return source;
        }

        int firstIndex=result.indexOf(firStr);
        int secondIndex=source.indexOf(secondStr)+secondStr.length();

        if(firstIndex == -1 || secondIndex== -1){
            return source;
        }

        result=source.substring(0,firstIndex)+source.substring(secondIndex);

        return result;

    }


    public static String extractMidFirst(String source, String firStr, String secondStr){

        String result=CommUtil.retSpace(source);

        if(result.equals("")){
            return source;
        }

        int firstIndex=result.indexOf(firStr)+firStr.length();
        int secondIndex=source.indexOf(secondStr);

        if(firstIndex == -1 || secondIndex== -1){
            return source;
        }

        result=source.substring(firstIndex, secondIndex);

        return result;
    }

    /**
     * @comment_ko "\r\n", "\r", "\n" 을 <br>로 변환
     * @param   String arg
     * @return  String
     */
    public static String n2br(String arg){

        if(CommUtil.retNull(arg)==null){
            return arg;
        }

        String rtnValue="";
        rtnValue = arg.replaceAll("\r\n", "<br>");
        rtnValue = rtnValue.replaceAll("\r", "<br>");
        rtnValue = rtnValue.replaceAll("\n", "<br>");

        return rtnValue;
    }

    /**
     * 엔터에 해당하는 부분 (<br>, \r, \n, \r\n 등을 \\n 으로 변환 한다.
     * javascript 의 alert 창에 해당하는 부분으로 치환
     * @param arg
     * @return
     */
    public static String convertToSriptAlertFormat(String arg){

        if(CommUtil.retNull(arg)==null){
            return arg;
        }

        String rtnValue="";
        rtnValue = arg.replaceAll("\r\n", "\\\\r\\\\n");
        rtnValue = rtnValue.replaceAll("\r", "\\\\r\\\\n");
        rtnValue = rtnValue.replaceAll("\n", "\\\\r\\\\n");
        rtnValue = rtnValue.replaceAll("<br>", "\\\\r\\\\n");

        return rtnValue;
    }

    /**
     * 넘겨받은 String 에서 CRLF 값을 제거한다.
     * @param targetStr
     * @return
     */
    public static String removeCrlf(String targetStr){

        String resultStr=targetStr;

        if(CommUtil.retNull(resultStr)==null){
            return resultStr;
        }

        resultStr=resultStr.replaceAll("\n", "");
        resultStr=resultStr.replaceAll("\r", "");
        resultStr=resultStr.replaceAll("\n\r", "");

        return resultStr;
    }



    /**
     * @comment_ko html 구분자를 parse한다.
     * @param strText
     * @return
     */
    public static String parseHtmlString(String strText){

        String strTextTemp=strText;

        if(strTextTemp == null){
            strTextTemp = "";
        }

        strTextTemp = replace(strTextTemp, "&", "&amp;");
        strTextTemp = replace(strTextTemp, " ", "&nbsp;");
        strTextTemp = replace(strTextTemp, "#", "&#35;");
        strTextTemp = replace(strTextTemp, "'", "&#39;");
        strTextTemp = replace(strTextTemp, "%", "&#37;");
        strTextTemp = replace(strTextTemp, ">", "&gt;");
        strTextTemp = replace(strTextTemp, "<", "&lt;");
        strTextTemp = replace(strTextTemp, "\r\n", "<br/>");
        strTextTemp = replace(strTextTemp, "\n", "<br/>");
        strTextTemp = replace(strTextTemp, "\"", "&quot;");

        return CommUtil.retSpace(strTextTemp);
    }

    /**
     * @comment_ko html 구분자를 parse한 결과를 reverse한다.
     * @param strText
     * @return
     */
    public static String reverseHtmlString(String strText){

        String strTextTemp=strText;

        if(strTextTemp == null){
            strTextTemp = "";
        }

        strTextTemp = replace(strTextTemp, "&lt;", "<");
        strTextTemp = replace(strTextTemp, "&gt;", ">");
        strTextTemp = replace(strTextTemp, "&amp;", "&");
        strTextTemp = replace(strTextTemp, "&#37;", "%");
        strTextTemp = replace(strTextTemp, "&quot;", "\"");
        strTextTemp = replace(strTextTemp, "&#39;", "'");
        strTextTemp = replace(strTextTemp, "&#35;", "#");
        strTextTemp = replace(strTextTemp, "<br/>", "\n");
        strTextTemp = replace(strTextTemp, "&nbsp;", " ");

        return CommUtil.retSpace(strTextTemp);
    }


    public static String reverseFilterHtmlString(String strText){

        String strTextTemp=strText;

        if(strTextTemp == null){
            strTextTemp = "";
        }

        String[] fromStrArray=getCheckFilterSpecialCharConvert();
        String[] toStrArray=getCheckFilterSpecialChar();

        for(int i=0,n=fromStrArray.length;i<n;i++){
            strTextTemp = replace(strTextTemp, fromStrArray[i], toStrArray[i]);
        }

        return CommUtil.retSpace(strTextTemp);
    }

    /**
     * 파일 사이즈를 MB, KB, B로 표기
     * @author 한우선
     * @param s
     * @return
     */
    public static String getFileSize(String s) {

        String fSize = "0 Bytes";

        if (s == null || s.equals("")) {
            return fSize;
        }

        try {
            int filesize = Integer.parseInt(s);
            DecimalFormat df = new DecimalFormat(".##");

            if ((filesize > 1024) && (filesize < 1024 * 1024)) {
                fSize = df.format((float) filesize / 1024).toString() + " KB";
            } else if (filesize >= 1024 * 1024) {
                fSize = df.format((float) filesize / (1024 * 1024)).toString() + " MB";
            } else {
                fSize = Long.toString(filesize) + " Bytes";
            }
        } catch (NumberFormatException e) {
            return fSize;
        }
        return fSize;
    }

    public static String getSubstrTooltip(String s, String len){
        int offset = Integer.parseInt(len);
        if(s.length() > offset){
            return "<span onmouseover=\"javascript:substrToolTip('"+s+"');\" onmouseout=\"javascript:substrToolTip('');\">"+s.substring(0, offset)+"...</span>";
        } else {
            return s;
        }
    }

    /**
     * byte 단위로 글자 자르기
     * @param text
     * @param temp
     * @param cut
     * @return
     */
    public static String ByteStr(String text, int temp, int cut){
        String str = "";
        int num = text.getBytes().length;
        if(num > temp){
            str = cropByte(text,cut,"...");
            num = str.getBytes().length;
        } else {
            str = text;
        }
        return str;
    }

    public static String cropByte(String str, int i, String trail){
        if(str == null) return "";

        String tmp = str;
        int slen = 0, blen = 0;
        char c;
        try{
            if(tmp.getBytes("MS949").length > i){
                while(blen+1 < i){
                    c = tmp.charAt(slen);
                    blen++;
                    slen++;
                    if(c > 127) blen++;
                }
                tmp = tmp.substring(0,slen)+trail;
            }
        } catch(Exception e){}
        return tmp;
    }


    public static String dateToStr(String formatStr) {
        //"yyyy-MM-dd HH:mm:ss"
        Date today = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(formatStr);
        return sf.format(today);
    }


    public static boolean isEmpty(String str) {
        boolean rtn = false;
        if (str == null || str.trim().length() == 0) {
            rtn = true;
        }
        return rtn;
    }

}