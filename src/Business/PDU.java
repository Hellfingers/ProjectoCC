/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author User
 */
public class PDU 
{
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
    
    public static byte[] acceptChallengePDU(String name)
    {
        String alcunhaTer = name + '\0';
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
    
    public static byte[] answerPDU(byte escolha,String name,byte nQuestao)
    {
        String alcunhaTer = name + '\0';
        byte[] formato = formataPDU();
        byte[] str = alcunhaTer.getBytes();
        short tamanhoCampos = (short)(str.length+2);
        
        formato[4] = 11;
        formato[5] = 3;
        formato[6] = (byte)(tamanhoCampos & 0xff);
        formato[7] = (byte)((tamanhoCampos >> 8) & 0xff);
        byte[] pduFinal = new byte[str.length + formato.length+2];
        System.arraycopy(formato, 0, pduFinal, 0, formato.length);
        pduFinal[formato.length]=escolha;
        System.arraycopy(str, 0, pduFinal, formato.length, str.length);
        pduFinal[formato.length+str.length]=nQuestao;
                                     
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
}
