package com.koala.emm.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptUtil {
	
	/** 
     * 初始化 AES Cipher 
     * @param sKey 
     * @param cipherMode 
     * @return 
     */  
    public static Cipher initAESCipher(String sKey, int cipherMode) {  
        //创建Key gen  
        KeyGenerator keyGenerator = null;  
        Cipher cipher = null;  
        try {  
            keyGenerator = KeyGenerator.getInstance("AES");  
            keyGenerator.init(128, new SecureRandom(sKey.getBytes()));  
            SecretKey secretKey = keyGenerator.generateKey();  
            byte[] codeFormat = secretKey.getEncoded();  
            SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");  
            cipher = Cipher.getInstance("AES");  
            //初始化  
            cipher.init(cipherMode, key);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        }  
        return cipher;  
    }  
    
    /** 
     * 对文件进行AES加密 
     * @param sourceFile 
     * @param fileType 
     * @param sKey 
     * @return 
     */  
    public static File encryptFile(File sourceFile,String fileType, String sKey){  
        //新建临时加密文件  
        File encrypfile = null;  
        InputStream inputStream = null;  
        OutputStream outputStream = null;  
        try {  
            inputStream = new FileInputStream(sourceFile);  
            encrypfile = File.createTempFile(sourceFile.getName(), fileType);  
            outputStream = new FileOutputStream(encrypfile);  
            Cipher cipher = initAESCipher(sKey,Cipher.ENCRYPT_MODE);  
            //以加密流写入文件  
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);  
            byte[] cache = new byte[1024];  
            int nRead = 0;  
            while ((nRead = cipherInputStream.read(cache)) != -1) {  
                outputStream.write(cache, 0, nRead);  
                outputStream.flush();  
            }  
            cipherInputStream.close();  
        }  catch (FileNotFoundException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        }  catch (IOException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        } finally {  
            try {  
                inputStream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
            }  
            try {  
                outputStream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
            }  
        }  
        return encrypfile;  
    }  
    
    /** 
     * AES方式解密文件 
     * @param sourceFile 
     * @return 
     */  
    public static File decryptFile(File sourceFile,String fileType,String sKey){  
        File decryptFile = null;  
        InputStream inputStream = null;  
        OutputStream outputStream = null;  
        try {  
            decryptFile = File.createTempFile(sourceFile.getName(),fileType);  
            Cipher cipher = initAESCipher(sKey,Cipher.DECRYPT_MODE);  
            inputStream = new FileInputStream(sourceFile);  
            outputStream = new FileOutputStream(decryptFile);  
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);  
            byte [] buffer = new byte [1024];  
            int r;  
            while ((r = inputStream.read(buffer)) >= 0) {  
                cipherOutputStream.write(buffer, 0, r);  
            }  
            cipherOutputStream.close();  
        } catch (IOException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        }finally {  
            try {  
                inputStream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
            }  
            try {  
                outputStream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
            }  
        }  
        return decryptFile;  
    }
	
	
	
	
	/** 
	 * 加密 
	 *  
	 * @param content 需要加密的内容 
	 * @param encKey  加密密码 
	 * @return 
	 */  
	public static byte[] encryptString(String content, String encKey) {  
	        try {             
	                byte[] byteContent = content.getBytes("utf-8");  
	        	 	Cipher cipher = initAESCipher(encKey,Cipher.ENCRYPT_MODE);
	                byte[] result = cipher.doFinal(byteContent);  
	                return result; // 加密   
	        } catch (UnsupportedEncodingException e) {  
	                e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	                e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	                e.printStackTrace();  
	        }  
	        return null;  
	}  
	
	
	/**解密 
	 * @param content  待解密内容 
	 * @param decKey 解密密钥 
	 * @return 
	 */  
	public static byte[] decryptString(byte[] content, String decKey) {  
	        try {  
	        	Cipher cipher = initAESCipher(decKey,Cipher.DECRYPT_MODE);
                byte[] result = cipher.doFinal(content);  
                return result; // 加密   
	        } catch (IllegalBlockSizeException e) {  
	                e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	                e.printStackTrace();  
	        }  
	        return null;  
	}  
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	}  
	
	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}  
	
	
	



	
	public static void main(String[] args) throws IOException {
		String s = "C:/Users/mingsheng/Desktop/啦啦啦啦啦.txt";
		String s2 = "C:/Users/mingsheng/Desktop/啦啦啦啦啦2.txt";
		
		File f= new File(s);
//		File f2 = AESEncryptUtil.encryptFile(f, "txt",
//				"11");
//		FileUtil.saveFile(f.getPath(),f2);
		File f2 = AESEncryptUtil.decryptFile(f, "txt",
				"11");
		FileUtil.saveFile(f.getPath(),f2);
	}
	
	
	

}
