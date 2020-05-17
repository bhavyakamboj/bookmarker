package com.sivalabs.bookmarker.utils;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class EncryptionUtil {

    public static void main(String[] args) throws IOException {
        //encodePasswords();
        encryptValues();
    }

    static void encodePasswords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String[] secrets = { "admin", "siva"};
        for (String secret : secrets) {
            System.out.println(secret+":"+encoder.encode(secret));
        }
    }

    static void encryptValues() throws IOException {
        Properties properties = new Properties();
        properties.load(EncryptionUtil.class.getResourceAsStream("/secure.properties"));
        String password = properties.getProperty("jasypt.encryptor.password");
        StringEncryptor encryptor = stringEncryptor(password);

        @SuppressWarnings("unchecked")
        Enumeration<String> enumeration = (Enumeration<String>) properties.propertyNames();

        while(enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String plain = properties.getProperty(key);
            String encText = encryptor.encrypt(plain);
            String decText = encryptor.decrypt(encText);
            System.out.println(plain+":"+encText+":"+decText);
        }

    }

    public static StringEncryptor stringEncryptor(String password) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
