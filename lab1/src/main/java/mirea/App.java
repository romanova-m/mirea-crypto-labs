package mirea;

public class App {
    public static void main(String args[]){
        String in = "lab1//piece.txt";
        String enc = "lab1//enc.txt";
        String out = "lab1//out.txt";
        String out1 = "lab1//out1.txt";
        String fulltext = "lab1//war.txt";

        FileHelper f = new FileHelper();
        CryptoHelper c = new CryptoHelper();

        String inpData;
        String encData;
        String outData;
        inpData = f.read(in);
        encData = c.encrypt(inpData);
        String sampleData = f.read(fulltext);
        outData = c.decrypt(encData, sampleData);

        f.write(encData,enc);
        f.write(outData, out);
        c.checkPercentage("1 method: ", inpData, outData);

        outData = c.bgDecrypt(encData,sampleData);
        f.write(outData, out1);
        c.checkPercentage("Bigram method: ", inpData, outData);
    }
}
