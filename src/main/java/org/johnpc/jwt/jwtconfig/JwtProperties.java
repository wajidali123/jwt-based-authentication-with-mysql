package org.johnpc.jwt.jwtconfig;

public class JwtProperties {
    public static final String SECRET = "OrgDotJohnPc";
    public static final int EXPIRATION_TIME = 864_000_000; // about 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
