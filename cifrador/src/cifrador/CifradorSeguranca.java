/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author joaov
 */
public class CifradorSeguranca {
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        System.out.println("############################################################");
        System.out.println("#                      CIFRADOR AES                        #");
        System.out.println("############################################################");
        
        File file = new File(args[0]);
        String fileName = file.getName().substring(0, file.getName().indexOf("."));
        String senha = "";
        
        try {
            senha = args[1];
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            System.out.println("- ERRO: Parâmetro senha não inserido.");
            return;
        }
        
        if (!file.exists()) {
            System.out.println("- ERRO: arquivo não encontrado.");
            return;
        } else {
            System.out.println("- Cifrando arquivo " + file.getAbsolutePath() + " de tamanho " + file.length());
        }

        //Criar as streams para serializar os dados do arquivo:
        FileInputStream inputStream = new FileInputStream(file);
        byte[] inputBytes = new byte[(int) file.length()];
        inputStream.read(inputBytes);
        //Instanciar o cifrador:
        Cifrador q5 = new Cifrador();
        byte[] outputBytes = null;
        try {
            outputBytes = q5.cifrar(inputBytes, senha);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(CifradorSeguranca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(CifradorSeguranca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(CifradorSeguranca.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (outputBytes == null) {
            System.out.println("- ERRO: problemas na cifragem.");
            inputStream.close();
            return;
        }
        
        File fileEncryptOut = new File(fileName + ".aes");
        FileOutputStream outputStream = new FileOutputStream(fileEncryptOut);
        outputStream.write(outputBytes);
        
        System.out.println("- OUTPUT: " + fileEncryptOut.getAbsolutePath());
        
        inputStream.close();
        outputStream.close();

    }

}
