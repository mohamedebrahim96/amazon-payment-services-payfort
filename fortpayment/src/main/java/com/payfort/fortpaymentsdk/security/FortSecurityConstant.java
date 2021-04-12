package com.payfort.fortpaymentsdk.security;

import java.util.regex.Pattern;

public class FortSecurityConstant {

    public static final Pattern[] xSSPatterns = new Pattern[] {

            // Avoid anything between script tags
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),

            // Avoid anything in a src='...' type of expression
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n ]*=[\r\n]*(.*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // Remove any lonesome </script> tag
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),

            // Remove any lonesome <script ...> tag
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // Avoid eval(...) expressions
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // Avoid expression(...) expressions
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // Avoid javascript:... expressions
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),

            // Avoid vbscript:... expressions
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),

            // Avoid onload= expressions
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid html tags
            Pattern.compile("<[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL) };

}

