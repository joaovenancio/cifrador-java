/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrador;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Cifrador {

    private Cipher cipher;
    private Key key;
    private GCMParameterSpec gcm;
    private Scanner in;
    private String salt;

    public void iniciar(String senha) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeySpecException {
        cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] bsalt = new byte[96];
        random.nextBytes(bsalt);
        salt = Hex.encodeHexString(bsalt);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        SecretKey derivedKey = factory.generateSecret(new PBEKeySpec(senha.toCharArray(), salt.getBytes(), 10000, 256));
        key = new SecretKeySpec(derivedKey.getEncoded(), "AES");
    }

    public byte[] cifrar(byte[] frase, String senha) throws NoSuchProviderException, IllegalBlockSizeException, BadPaddingException {
        try {
            try {
                iniciar(senha);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException
                    | InvalidKeySpecException ex) {
                System.out.println("Não tem provedor");
            }
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            gcm = new GCMParameterSpec(128, salt.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, key, gcm);
            return cipher.doFinal(frase);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
        }

        return null;
    }

}
