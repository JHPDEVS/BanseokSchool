package com.jhp.banseok.bap.tool;

import com.jhp.banseok.xor.SecurityXor;

import java.math.BigDecimal;


/**
 * Created by whdghks913 on 2015-12-20.
 */
public class HiddenCode {
    /**
     * PrivateCode.java 파일은 공개하지 않으며 단순히 int형 숫자를 선언해둔 파일이고,
     * 원당고 앱의 공지사항등을 임의로 게시할 수 없도록 설계되었습니다.
     * 빌드시 PrivateCode.java파일을 찾을 수 없다는 오류가 발생됩니다.
     * PrivateCode.MY_HIDDEN_FIRST_CODE, PrivateCode.MY_HIDDEN_SECOND_CODE을 임의의 상수로 바꾸세요.
     */
    public static final int HIDDEN_FIRST_CODE = 1;
    public static final int HIDDEN_SECOND_CODE = 1;

    public static String getHiddenCode() {
        BigDecimal bigDecimal = new BigDecimal(getFirstHiddenCode())
                .multiply(new BigDecimal(getSecondHiddenCode()));
        return bigDecimal.toString();
    }

    private static int getFirstHiddenCode() {
        // private code
        SecurityXor securityXor = new SecurityXor();
        int num = securityXor.getSecurityXor(securityXor.getSecurityXor(456, securityXor.getSecurityXor(HIDDEN_FIRST_CODE, 123)), 456);
        return num - 89;
    }

    private static int getSecondHiddenCode() {
        // 562
        SecurityXor securityXor = new SecurityXor();
        int num = securityXor.getSecurityXor(securityXor.getSecurityXor(768, securityXor.getSecurityXor(HIDDEN_SECOND_CODE, 2013)), 126);
        return num + 1121;
    }
}
