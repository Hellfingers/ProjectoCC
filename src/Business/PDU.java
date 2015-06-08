/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;


import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 *
 * @author User
 */
public class PDU 
{
    public static final int MAX_PACOTE_SIZE=48000-8;
    public PDU(){}
    
    public static short getShortValue(byte[] data) 
    {
        short value = data[1];
        value = (short) ((value << 8) | data[0]);

        return value;
    }
    
    public static int getIntValue(byte[] data) 
    {
        int value = ((0xFF & data[0]) << 24) | ((0xFF & data[1]) << 16) |
            ((0xFF & data[2]) << 8) | (0xFF & data[3]);

        return value;
    }    
    
    
    private static byte[] formataPDU()
    {
        byte[] pdu = new byte[8];
        short num;
        Random rng = new Random();
        
        num = (short)rng.nextInt(100);
        while(num == 0) num = (short)rng.nextInt(100);
        pdu[0] = 0; //Versao
        pdu[1] = 0; //segurança
        pdu[2] = (byte)(num & 0xff);
        pdu[3] = (byte)((num >> 8) & 0xff);
        
        return pdu;
    }
    
    public static byte[] quitChallPDU(String username, String desafio){
        String infoRegisto = username + '\0' + desafio + '\0';
        byte[] formato = formataPDU();
        byte[] str = infoRegisto.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[4] = 5;
        formato[5] = 2;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1024];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        
                
        return pduFinal;
    }
    
    public static byte[] logoutPDU(String username){
        String alcunhaTer = username+'\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[4] = 4;
        formato[5] = 1;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
                       
        return pduFinal;
    }
    
    public static byte[] ReplistChallPDU(List<Desafio> lista){
        String infoRegisto=new String();
        for(Desafio d: lista)
            infoRegisto=infoRegisto+d.getNome()+'\0';
        byte[] formato = formataPDU();
        byte[] str = infoRegisto.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[4] = 0;
        formato[5] = (byte)lista.size();
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1024];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        
                
        return pduFinal;  
    }
    
    public static byte[] endChallPDU(String username, String desafio){
        String infoRegisto = username + '\0' + desafio + '\0';
        byte[] formato = formataPDU();
        byte[] str = infoRegisto.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[4] = 6;
        formato[5] = 2;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1024];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        
                
        return pduFinal;
    }
    public static byte[] registerPDU(String nome, String alcunha, byte[] SecInfo)
    {
        String infoRegisto = nome + '\0' + alcunha + '\0';
        byte[] formato = formataPDU();
        byte[] str = infoRegisto.getBytes();
        short tamanhoCampos = (short)(str.length + SecInfo.length + nome.length());
        
        formato[4] = 2;
        formato[5] = 3;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1024];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        System.arraycopy(SecInfo, 0, pduFinal, formato.length + str.length, SecInfo.length);
                
        return pduFinal;
    }
    public static byte[] requestPergunta(String desafio, int nPergunta) {
        String alcunhaTer = desafio + '\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short) (str.length + 1);

        formato[4] = 15;
        formato[5] = 2;
        formato[6] = (byte) (tamanhoCampos & 0xff);
        formato[7] = (byte) ((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length + 1];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        pduFinal[formato.length + str.length] = (byte) nPergunta;

        return pduFinal;
    }
    
    public static byte[] respostaRanking(TreeSet<ParUsernamePontos> treeSet){
        String username=new String();
        for(ParUsernamePontos pup:treeSet)
            username=username+pup.getUsername()+'\0'+pup.getPontos()+'\0';
        byte[] formato = formataPDU();
        byte[] str=username.getBytes();
        short tamanhoCampos = (short) (str.length);
        
        formato[4] = 0;
        formato[5] = (byte)(treeSet.size()*2);
        formato[6] = (byte) (tamanhoCampos & 0xff);
        formato[7] = (byte) ((tamanhoCampos >> 8) & 0xff);
                
        return formato;
        
    }
    
    public static byte[] getMusicaPDU(String nomeDesafio,byte index){
        String infoRegisto = nomeDesafio + '\0';
        byte[] formato = formataPDU();
        byte[] str = infoRegisto.getBytes();
        short tamanhoCampos = (short)(str.length+1);
        
        formato[4] = 16;
        formato[5] = 2;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1024];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        pduFinal[pduFinal.length-1]=index;
        
                
        return pduFinal;
    }
    
    public static byte[] getImagemPDU(String nomeDesafio,byte index){
        String infoRegisto = nomeDesafio + '\0';
        byte[] formato = formataPDU();
        byte[] str = infoRegisto.getBytes();
        short tamanhoCampos = (short)(str.length+1);
        
        formato[4] = 17;
        formato[5] = 2;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1024];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        pduFinal[pduFinal.length-1]=index;
        
                
        return pduFinal;
    }
    public static byte[] respostaRqPerguntaPDU(Pergunta p,short[] label){
        String enunciadoter = p.getEnunciado() + '\0';
        String op1= p.getOpcao1()+'\0';
        String op2= p.getOpcao2()+'\0';
        String op3= p.getOpcao3()+'\0';
        byte[] formato = formataPDU();
        byte[] str = enunciadoter.getBytes();
        byte[] str1=op1.getBytes();
        byte[] str2=op2.getBytes();
        byte[] str3=op3.getBytes();
        short tamanhoCampos = (short) (str.length+str1.length+str2.length+str3.length);
        formato[2] = (byte)label[0];
        formato[3] = (byte)label[1];
        formato[4] = 0;
        formato[5] = 3;
        formato[6] = (byte) (tamanhoCampos & 0xff);
        formato[7] = (byte) ((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length +str1.length+str2.length+str3.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        System.arraycopy(str1,0, pduFinal, formato.length+ str.length, str1.length);
        System.arraycopy(str2, 0, pduFinal, formato.length + str.length + str1.length, str2.length);
        System.arraycopy(str3, 0, pduFinal, formato.length + str.length + str1.length + str2.length, str3.length);

        return pduFinal;
    }
            
    public static byte[] loginPDU(String alcunha, byte[] SecInfo)
    {
        String alcunhaTer = alcunha + '\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length + SecInfo.length);
        
        formato[4] = 3;
        formato[5] = 2;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + SecInfo.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        System.arraycopy(SecInfo, 0, pduFinal, formato.length + str.length, SecInfo.length);
                
        return pduFinal;
    }

    public static byte[] helloPDU()
    {
        byte[] formato = formataPDU();
        
        formato[4] = 1;
        formato[5] = 0;
        formato[6] = 0;
        formato[7] = 0;
                
        return formato;
    }
    
    public static byte[] logoutPDU()
    {
        byte[] formato=formataPDU();
        formato[4] = 4;
        formato[5] = 0;
        formato[6] = 0;
        formato[7] = 0;
        return formato;
    }
    
    public static byte[] quitPDU()
    {
    byte[] formato=formataPDU();
        formato[4] = 5;
        formato[5] = 0;
        formato[6] = 0;
        formato[7] = 0;
        return formato;    
    }
    
    public static byte[] endPDU()
    {
      byte[] formato=formataPDU();
        formato[4] = 6;
        formato[5] = 0;
        formato[6] = 0;
        formato[7] = 0;
        return formato;   
    }
    
    public static byte[] listChallengesPDU()
    {
        byte[] formato=formataPDU();
        formato[4] = 7;
        formato[5] = 0;
        formato[6] = 0;
        formato[7] = 0;
        return formato;  
    }
    
    public static byte[] makeChallPDU(String userN,String desN){
        String alcunhaTer = userN + '\0'+desN+'\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[4] = 8;
        formato[5] = 3;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
                
        return pduFinal;
    }
    
    public static byte[] makeChallengePDU(String name, byte[] data,byte[] hora)
    {
        String alcunhaTer = name + '\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length+data.length+hora.length);
        
        formato[4] = 8;
        formato[5] = 3;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length+data.length+hora.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        System.arraycopy(data, 0, pduFinal, formato.length+str.length, data.length);
        System.arraycopy(hora, 0, pduFinal, formato.length+str.length+data.length, hora.length);
                
        return pduFinal;
    }
    
    public static byte[] acceptChallengePDU(String username,String name)
    {
        String alcunhaTer = username+'\0'+name + '\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[4] = 9;
        formato[5] = 1;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
                       
        return pduFinal;
    }
    
    public static byte[] deleteChallengePDU(String name)
    {
        String alcunhaTer = name + '\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[4] = 10;
        formato[5] = 1;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
                       
        return pduFinal;
    }
    
    public static byte[] answerPDU(String desafio,byte escolha,String userName,byte nQuestao)
    {
        String alcunhaTer = userName + '\0'+desafio+'\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length+2);
        
        formato[4] = 11;
        formato[5] = 3;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length+2];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        pduFinal[formato.length+str.length]=escolha;
        
        
        pduFinal[formato.length+str.length+1]=nQuestao;
                                     
        return pduFinal;
    }
    
    public static byte[] retransmitPDU(String name,byte nQuestao, byte nBloco)
    {
        String alcunhaTer = name + '\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length+2);
        
        formato[4] = 12;
        formato[5] = 3;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length+2];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        pduFinal[formato.length+str.length]=nQuestao;
        pduFinal[formato.length]=nBloco; 
        
        return pduFinal;
    }
    
    public static byte[] listRankingPDU()
    {
        byte[] formato=formataPDU();
        formato[4] = 13;
        formato[5] = 0;
        formato[6] = 0;
        formato[7] = 0;
        return formato;  
    }

    public static byte[] respostaLogin(String nome, int pontos){
        String stringTer = nome + '\0';
        byte[] formato = formataPDU();
        byte[] str = stringTer.getBytes();
        short tamanhoCampos = (short)(str.length+1);
        
        formato[2] = 0;
        formato[3] = 6;
        formato[4] = 0;
        formato[5] = 0;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        pduFinal[pduFinal.length-1]=(byte)pontos;
        return pduFinal;
    }
    
    public static byte[] respostaErro(String erro, short label)
    {
        String erroTer = erro + '\0';
        byte[] formato = formataPDU();
        byte[] str = erroTer.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 0;
        formato[5] = 1;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
                
        return pduFinal;
    }
    
    public static byte[] respostaString(String string, short label, byte tipo)
    {
        String stringTer = string + '\0';
        byte[] formato = formataPDU();
        byte[] str = stringTer.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 0;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
                
        return pduFinal;
    }
    
    public static byte[] respostaByteArr(byte[] res,short label,byte tipo)
    {
        
        byte[] formato = formataPDU();
        
        short tamanhoCampos = (short)(res.length);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 0;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[res.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(res, 0, pduFinal, formato.length, res.length);
                
        return pduFinal;
    }
    
    public static byte[] respostaByte(byte res, short label, byte tipo)
    {
        byte[] formato = formataPDU();
        
        short tamanhoCampos = (short)(1);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 0;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1 + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        pduFinal[formato.length]=res;
                
        return pduFinal;
    }
    
    public static byte[] respostaByte(byte res, byte tipo)
    {
        byte[] formato = formataPDU();
        
        short tamanhoCampos = (short)(1);
        
        formato[4] = 0;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1 + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        pduFinal[formato.length]=res;
                
        return pduFinal;
    }
    
    public static byte[] resposta2Bytes(int res,short label,byte tipo)
    {
        byte[] formato = formataPDU();
        
        short tamanhoCampos = (short)(2);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 0;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[2 + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        pduFinal[formato.length]=(byte)(res &  0xff);
        pduFinal[formato.length+1]=(byte)((res >> 8) & 0xff);
                
        return pduFinal;
    }
    
    public static byte[] infoTCPString(String nome, short label, byte tipo)
    {
        String nomeTer = nome + '\0';
        byte[] formato = formataPDU();
        byte[] str = nomeTer.getBytes();
        short tamanhoCampos = (short)(str.length);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 14;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
                
        return pduFinal;
    }
     
    public static byte[] infoTCPByteArr(byte[] nome, short label, byte tipo)
    {
       
        byte[] formato = formataPDU();
        
        short tamanhoCampos = (short)(nome.length);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 14;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[nome.length + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        System.arraycopy(nome, 0, pduFinal, formato.length, nome.length);
                
        return pduFinal;
    }
    
    public static byte[] infoTCPByte(byte res, short label, byte tipo)
    {
        byte[] formato = formataPDU();
        
        short tamanhoCampos = (short)(1);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 14;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[1 + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        pduFinal[formato.length]=res;
                
        return pduFinal;
    }
    
    public static byte[] infoTCP2Bytes(int res, short label, byte tipo)
    {
        byte[] formato = formataPDU();
        
        short tamanhoCampos = (short)(2);
        
        formato[2] = (byte)(label & 0xff);
        formato[3] = (byte)((label >> 8) & 0xff);
        formato[4] = 14;
        formato[5] = tipo;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[2 + formato.length];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        pduFinal[formato.length]=(byte)(res &  0xff);
        pduFinal[formato.length+1]=(byte)((res >> 8) & 0xff);
                
        return pduFinal;
    }
    
    public static short[] UndoPDU(byte[] PDU)
    {
        short[] cabecalho = new short[]{0,0,0,0,0,0};
        
        cabecalho[0] = PDU[0];
        cabecalho[1] = PDU[1];
        
        byte[] label = new byte[2];
        label[0] = PDU[2]; label[1] = PDU[3];
        cabecalho[2] = getShortValue(label);
        
        cabecalho[3] = (short) PDU[4];
        if(PDU.length > 5)
        {
            cabecalho[4] = (short) PDU[5];

            byte[] tamanhoCampos = new byte[2];
            tamanhoCampos[0] = PDU[6]; tamanhoCampos[1] = PDU[7];
            cabecalho[5] = getShortValue(tamanhoCampos);
        }
                
        return cabecalho;
    }
    
    public static byte[][] FileToPacoteArray(String filename)throws IOException
    {
        int sizeTotal = 0, nPacotes;
        byte[] resAux = IOUtils.toByteArray(new FileInputStream(filename));
        sizeTotal = resAux.length;
        
        nPacotes = (sizeTotal / MAX_PACOTE_SIZE);
        if(sizeTotal%MAX_PACOTE_SIZE!=0) nPacotes++;
        byte pacotes[][] = new byte[nPacotes][MAX_PACOTE_SIZE + 8];
        for (int i = 0; i < nPacotes; i++) {
            pacotes[i] = PDU.formataPDU();
            pacotes[i][4] = 0;
            pacotes[i][5] = 2;
            pacotes[i][6] = (byte)((MAX_PACOTE_SIZE+1) &  0xff);
            pacotes[i][7] = (byte)(((MAX_PACOTE_SIZE+1) >> 8) & 0xff);
            pacotes[i][8] = (byte) i;
            System.arraycopy(resAux, i*MAX_PACOTE_SIZE, pacotes[i], 9, MAX_PACOTE_SIZE);
           
        }
        return pacotes;
    }
    
    
    
}
