/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pccontrol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author paulo
 */
public class PcControl {
    private static String sourceFile = "c:\\pccontrol\\hosts";
    private static String destinationFile = "c:\\windows\\system32\\drivers\\etc\\hosts";
    //private static String sourceFile = "/pccontrol/hosts";
    //private static String destinationFile = "/pccontrol/hosts2";
    //private static String remoteConfigUrl = "http://192.168.1.9/PcControl/getConfig.php";
    //private static String remoteStatusUrl = "http://192.168.1.9/PcControl/getStatus.php";
    private static String remoteConfigUrl = "http://www.paulotrentin.com.br/PcControl/getConfig.php";
    private static String remoteStatusUrl = "http://www.paulotrentin.com.br/PcControl/getStatus.php";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String status = getStatus();

            if (status.equals("livre")) {
                System.out.println("livre");
                writeHostConfig("127.0.0.1 domain.com\n");
            } else {
                System.out.println("bloqueado");

                if (needsToClose()) {
                    closeWindowsBrowsers();
                    writeHostConfig(getConfig());
                }
            }
        } catch (Exception e) {
            System.out.println("Falha ao executar: " + e.getMessage());
        }
    }

    /**
     * Fecha navegadores do windows
     * @throws IOException 
     */
    private static void closeWindowsBrowsers() throws IOException{
        Runtime rt = Runtime.getRuntime();
        rt.exec("Taskkill /F /IM chrome.exe");
        rt.exec("Taskkill /F /IM firefox.exe");
        rt.exec("Taskkill /F /IM iexplore.exe");
        rt.exec("Taskkill /F /IM opera.exe");
        rt.exec("Taskkill /F /IM safari.exe");
    }
    
    /**
     * Verifica se precisa fechar o browser O browser só precisa ser fechado ao
     * mudar o arquivo de hosts
     *
     * @return
     * @throws IOException
     */
    private static boolean needsToClose() throws IOException {
        writeConfigOnLocalFile(getConfig());

        // Verifica se o arquivo de hosts baixado é igual ao existente
        String data1 = readFile(sourceFile);
        String data2 = readFile(destinationFile);
        
        if (data1.equals(data2)){
            return false;
        }
        
        return true;
    }

    /**
     * Lê um arquivo de texto e retorna string completa que o representa
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static String readFile(String file) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String everything;
        
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }
        
        return everything;
    }

    /**
     * Grava configurações no arquivo de hosts local (temporário)
     * @param config 
     */
    private static void writeConfigOnLocalFile(String config){
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(sourceFile), "utf-8"));
            writer.write(config);
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }
    
    /**
     * Copia configuração do arquivo local para arquivo remoto
     * @param config 
     */
    private static void writeHostConfig(String config) {
        writeConfigOnLocalFile(config);

        // Copia arquivo de configuração
        File source = new File(sourceFile);
        File dest = new File(destinationFile);
        try {
            FileUtils.copyFile(source, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getConfig() throws IOException {
        return getRemoteUrl(remoteConfigUrl);
    }

    private static String getStatus() throws IOException {
        return getRemoteUrl(remoteStatusUrl);
    }
    
    /**
     * Obtém dados de uma URL remota
     * @param urlToGet
     * @return
     * @throws IOException 
     */
    private static String getRemoteUrl(String urlToGet) throws IOException{
        URL url = new URL(urlToGet);
        URLConnection conn = url.openConnection();

        InputStream stream = conn.getInputStream();

        StringBuilder stringBuilder = new StringBuilder();

        if (stream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        }

        return stringBuilder.toString();
    }

}
