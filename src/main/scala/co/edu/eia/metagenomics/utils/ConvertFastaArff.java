package co.edu.eia.metagenomics.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ConvertFastaArff {
    private ArrayList<Integer> error= new ArrayList<Integer>();
    private  String nameCodon[]=new String[65];
    private String nameKmers[];
    private String nucleotido[]=new String[4];
    private Double nucCant[]= new Double[4];
    private double gc;
    private double nA;
    private double nC;
    private double nT;
    private double nG;
    private double[] kmerN;
    private double[] codN;
    private ArrayList<String> classes;
    private ArrayList<String> clasDif;
    private ArrayList<String> id;
    private ArrayList<String> cat;
    private ArrayList<String> phylum;
    private ArrayList<String> specie;

    public Double[] getNucCant() {
        return nucCant;
    }

    public void setNucCant(Double[] nucCant) {
        this.nucCant = nucCant;
    }

    public ArrayList<String> getCat() {
        return cat;
    }

    public void setCat(ArrayList<String> cat) {
        this.cat = cat;
    }

    public ArrayList<String> getPhylum() {
        return phylum;
    }

    public void setPhylum(ArrayList<String> phylum) {
        this.phylum = phylum;
    }

    public double getnA() {
        return nA;
    }

    public void setnA(double nA) {
        this.nA = nA;
    }

    public double getnC() {
        return nC;
    }

    public void setnC(double nC) {
        this.nC = nC;
    }

    public double getnT() {
        return nT;
    }

    public void setnT(double nT) {
        this.nT = nT;
    }

    public double getnG() {
        return nG;
    }

    public void setnG(double nG) {
        this.nG = nG;
    }

    public ArrayList<String> getClasDif() {
        return clasDif;
    }

    public void setClasDif(ArrayList<String> clasDif) {
        this.clasDif = clasDif;
    }

    public ArrayList<String> getId() {
        return id;
    }

    public void setId(ArrayList<String> id) {
        this.id = id;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public double getGc() {
        return gc;
    }

    public void setGc(double gc) {
        this.gc = gc;
    }

    public double[] getKmerN() {
        return kmerN;
    }

    public void setKmerN(double[] kmerN) {
        this.kmerN = kmerN;
    }

    public double[] getCodN() {
        return codN;
    }

    public void setCodN(double[] codN) {
        this.codN = codN;
    }

    public void setError(ArrayList<Integer> error) {
        this.error = error;
    }

    public ArrayList<Integer> getError() {
        return error;
    }

    public String[] getNameKmers() {
        return nameKmers;
    }

    public void setNameKmers(String[] nameKmers) {
        this.nameKmers = nameKmers;
    }

    public String[] getNucleotido() {
        return nucleotido;
    }

    public void setNucleotido(String[] nucleotido) {
        this.nucleotido = nucleotido;
    }

    public ConvertFastaArff() {
        nucleotido[0]="A";
        nucleotido[1]="T";
        nucleotido[2]="G";
        nucleotido[3]="C";
    }

    public String[] getNameCodon() {
        return nameCodon;
    }

    public void setNameCodon(String[] nameCodon) {
        this.nameCodon = nameCodon;
    }

    /*
     * String seq: secuencia
     * Rasgos posibles:
     * Boolean f_gc: cantidad de g+c
     * Boolean f_cod: frecuencia de codones (64 rasgos)
     * Boolean f_k: frecuencia de k-mers
     * int k: si anterior verdadero valor de k
     */
    public void procesarSeq(String seq, Boolean f_nucl, Boolean f_gc, Boolean f_cod, Boolean f_k, int k){
        //contar GC y frecuencia de nucleotidos
        gc= cantidadGC(seq);
        //contar codones
        //crearCodon();
        if (f_cod){
            int[] cod=contarCodons(seq);
            codN=Normalizar(cod);
        }

        //contar k-mers
        //crearKmers4();
        //contar k-mers 4
        if (f_k){
            int[] kmer=contarKmer(seq, 4);
            kmerN=Normalizar(kmer);
        }
    }

    public String crearCaso(int index,String indexS, Boolean f_gc, Boolean f_nucl, Boolean f_cod, Boolean f_k, int k, Boolean c){
        //DecimalFormat format = new DecimalFormat("####.###");

        BigDecimal b=new BigDecimal(gc);
        b=b.setScale(3, RoundingMode.HALF_UP);
        String caso= (f_gc)? (b+","):"";

        if (f_nucl){
            for (int i=0;i<nucCant.length; i++){
                b=new BigDecimal(nucCant[i]);
                b=b.setScale(3, RoundingMode.HALF_UP);
                caso= caso+ b+",";
            }
        }

        if (f_cod){
            for (int i=0;i<codN.length-1; i++){
                b=new BigDecimal(codN[i]);
                b=b.setScale(3, RoundingMode.HALF_UP);
                caso= caso+ b+",";
            }

        }

        if (f_k)
            for (int i=0;i<kmerN.length-1;i++){
                b=new BigDecimal(kmerN[i]);
                b=b.setScale(3, RoundingMode.HALF_UP);
                caso+= b+",";
            }

        //Adicionar la clase
        //String iS=(String) indexS.subSequence(1, indexS.length());
        String iS=indexS;
        int aux=id.indexOf(iS);
		/*if (aux!=index){
			//System.out.println("Error en index: " + iS);
			caso="";
		} else {*/
        if (c)
            //caso+=classes.get(aux);
            caso+=iS;
        //}
        return caso;
    }


    public void crearCodon(){
        String a[]=new String[4];
        a[0]="A";
        a[1]="T";
        a[2]="G";
        a[3]="C";
        int index=0;
        for (int i=0;i<a.length;i++)
            for (int j=0;j<a.length;j++)
                for (int k=0;k<a.length;k++)
                    nameCodon[index++]=a[i]+a[j]+a[k];
        nameCodon[64]="Unknown";

		/*for (int i=0;i<nameCodon.length;i++){
			System.out.println(nameCodon[i]);
		}*/

    }

    public void crearKmers4(){
        int k=4;
        int cKmer=(int) Math.pow(4, k);
        nameKmers=new String[cKmer+1];
        int[] Nk=new int[k];

        int index=0;
        for (int i=0;i<nucleotido.length;i++)
            for (int j=0;j<nucleotido.length;j++)
                for (int l=0;l<nucleotido.length;l++)
                    for (int m=0;m<nucleotido.length;m++)
                        nameKmers[index++]=nucleotido[i]+nucleotido[j]+nucleotido[l]+nucleotido[m];
        nameKmers[cKmer]="Unknown";
    }

    public void imprimirVector(String[] v){
        for (int i=0;i<v.length;i++)
            System.out.println(v[i]);
    }

    public void imprimirVectorInt(int[] v){
        for (int i=0;i<v.length;i++)
            System.out.println(v[i]);
    }

    public void imprimirVectorDouble(double[] v){
        for (int i=0;i<v.length;i++)
            System.out.println(v[i]);
    }

    public int sumaVector(int[] v){
        int suma=0;
        for (int i=0;i<v.length;i++)
            suma+=v[i];
        return suma;
    }

    //Cuenta cantidad de codones
    public int[] contarCodons(String seq){
        int [] countCodons=new int[65];
        for (int i=0;i<seq.length()-2;i=i+3){
            String c=seq.substring(i, i+3);
            int j=0;
            while (j<nameCodon.length && nameCodon[j].compareTo(c)!=0) j++;
            if (j<nameCodon.length)
                countCodons[j]++;
            else
                countCodons[64]++;
        }
        return countCodons;

    }

    //Normaliza un vector, dividir por la cantidad
    public double[] Normalizar(int[] v){
        double[] result=new double[v.length];
        double s=0;
        for (int i=0;i<v.length;i++){
            s+=v[i];
        }
        for (int i=0;i<v.length;i++){
            result[i]=(double)v[i]/(double)s;
        }

        return result;
    }

    //Normaliza un vector, llevar al intervalo de 0 a 1
    public double[] Normalizar01(int[] v){
        double[] result=new double[v.length];
        double s=v[0];
        double max=v[0];
        double min=v[0];
        for (int i=1;i<v.length;i++){
            s+=v[i];
            //minimo
            if (v[i]<min)
                min=v[i];
            if (v[i]>max)
                max=v[i];
            //maximo
        }
        for (int i=0;i<v.length;i++){
            result[i]=((double)v[i]-min)/(max-min);
        }

        return result;
    }

    //Cuenta la cantidad de G y C (porciento)
    public double cantidadGC(String seq){
        int countErrorInt=0;
        nG = 0;
        nC = 0;
        nA = 0;
        nT = 0;
        int tam=0;
        for (int i=0;i<seq.length();i++){
            char n= seq.charAt(i);
            if (n=='G')
                nG++;
            else if (n=='C')
                nC++;
            else if ((n=='T'))
                nT++;
            else if ((n=='A'))
                nA++;
            else
                countErrorInt++;
        }
        tam+=seq.length();
        error.add(countErrorInt);
		/*
		 * nucleotido[0]="A";
		nucleotido[1]="T";
		nucleotido[2]="G";
		nucleotido[3]="C";
		 */
        double total= (double)(nA+nG+nC+nT);
        nucCant[0]=nA/total;
        nucCant[1]=nT/total;
        nucCant[2]=nG/total;
        nucCant[3]=nC/total;
        return ((double)(nG+nC)/(double)(nA+nG+nC+nT))*100;

    }
    /*
     * fileCclases: fichero que tiene las clases
     * tipoClase: tipo de clase: 1-cat o 2-phylum
     */
    public void crearPrincipal(String nameFile, String fileClases,int tipoClase, Boolean f_gc, Boolean f_nucl, Boolean f_cod, Boolean f_k, int k) throws IOException{
        //Primero leer las clases para agregarlas al final
        ReadClasses c= new ReadClasses();
        classes=c.leer(fileClases, tipoClase);
        cat=c.getCat();
        phylum=c.getPhylum();
        specie= c.getSpecie();
        if (tipoClase==1)
            clasDif=c.getCatDif();
            //clasDif=c.getCat();
        else
        if (tipoClase==2)
            clasDif=c.getPhylumDif();
            //clasDif=c.getPhylum();
        else
            clasDif=c.getSpecieDif();
        id=c.getId();
        if (f_cod)
            crearCodon();
        if (f_k)
            crearKmers4();
        //Crear encabezado de Arff
        BufferedWriter bufferArff =crearEncArff(nameFile,f_gc, f_nucl,f_cod,f_k,k, tipoClase);
        //Adicionar los datos a la base (mismo orden)
        leerFichero(nameFile, bufferArff,f_gc, f_nucl, f_cod, f_k, k, tipoClase);

    }

    public String buscarClase(String cadena, int tipo){
        int i=0;
        while (i<id.size() && cadena.indexOf(id.get(i))==-1) i++;
        String clase= "";
        if (i>=phylum.size()) return clase;
        return (tipo==2)? phylum.get(i): cat.get(i);
		/*while (i<clasDif.size() && cadena.indexOf(clasDif.get(i))==-1) i++;
		return (i<clasDif.size())? clasDif.get(i): "";*/
    }

    public void leerFichero(String nameFile,BufferedWriter fileArff, Boolean f_gc, Boolean f_nucl, Boolean f_cod, Boolean f_k, int k, int tipoclase) throws IOException{
        BufferedReader buffer = new BufferedReader(new FileReader(new File(nameFile)));
        String strLinea= buffer.readLine();
        String index="";
        String seq="";
        int ind=0;
        Boolean c=true; // adicionar clase
        // Leer el archivo l�nea por l�nea(1ra l�nea informaci�n)
        while (strLinea!=null) {
            if (strLinea.indexOf(">")!=-1){//L�nea de informaci�n
                int ind1=strLinea.indexOf(">");
                //index=strLinea.substring(ind1, strLinea.length());//buscar nombre
                //Buscar que aparezca en nombre en el c�digo como una subcadena
                index = buscarClase(strLinea, tipoclase);
                seq="";//secuencia

                while (((strLinea = buffer.readLine()) != null)&& strLinea.indexOf(">")==-1){
                    seq+=strLinea;
                }

                if (seq.compareTo("")!=0){
                    seq= seq.toUpperCase();
                    //Procesar contigs
                    procesarSeq(seq, f_nucl, f_gc, f_cod, f_k, k);
                    String seqProc=crearCaso(ind,index, f_gc, f_nucl, f_cod, f_k, k, c);
                    ind++;
                    if (seqProc.compareTo("")!=0){
                        fileArff.write(seqProc);
                        fileArff.newLine();
                        fileArff.flush();
                        System.out.println(ind);
                    }
                }
            }
        }
        fileArff.close();
        buffer.close();
    }

    public int buscarKmer(String kmer){
        int i=0;
        while((i<nameKmers.length-1) && (nameKmers[i].compareTo(kmer)!=0)) i++;
        return (i<nameKmers.length-1)?i:nameKmers.length-1;
    }

    public int[] contarKmer(String seq, int k){
        int[] kmer= new int[nameKmers.length];
        int index=k;
        while (index<=seq.length()){
            String s=seq.substring(index-k, index);
            int indexK=buscarKmer(s);
            kmer[indexK]++;
            index++;
        }
        return kmer;
    }


    public BufferedWriter crearEncArff(String namefile, Boolean f_gc, Boolean f_nucl, Boolean f_cod, Boolean f_k, int k, int tipoClase) throws IOException{
        ///Crear arff
        String name=((f_gc)?"_gc":"" )+ ((f_nucl)?"_nucl":"")+ ((f_cod)?"_cod":"")+ ((f_k)?"_4mer":"");
        if (tipoClase==2)
            name+="phylum";
        String nameArff=namefile+name+".arff";
        BufferedWriter bufferArff = new BufferedWriter(new FileWriter(nameArff));
        bufferArff.write("@relation "+ name);
        bufferArff.newLine();

        if (f_gc){
            bufferArff.write("@attribute "+ "gc "+ "numeric ");
            bufferArff.newLine();
        }

        if (f_nucl){
            for (int i=0;i<nucleotido.length;i++){
                bufferArff.write("@attribute "+ nucleotido[i]+ " numeric ");
                bufferArff.newLine();
            }
        }

        if (f_cod){
            for (int i=0;i<nameCodon.length-1;i++){
                bufferArff.write("@attribute "+ nameCodon[i]+ " numeric ");
                bufferArff.newLine();
            }
        }

        if (f_k){
            for (int i=0;i<nameKmers.length-1;i++){
                bufferArff.write("@attribute "+ nameKmers[i]+ " numeric ");
                bufferArff.newLine();
            }
        }

        bufferArff.write("@attribute class {");
        for (int i=0;i<clasDif.size()-1;i++)
            bufferArff.write(clasDif.get(i)+", ");
        bufferArff.write(clasDif.get(clasDif.size()-1));
        bufferArff.write("}");
        bufferArff.newLine();
        bufferArff.write("@data");
        bufferArff.newLine();

        return bufferArff;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ConvertFastaArff rf=new ConvertFastaArff();
        try {

            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, false, true, false, false, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, false, false, true, false, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, true, false, false, false, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, true, true, true, false, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, true, true, false, true, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, true, false, true, true, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, false, true, true, true, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, true, false, false, true, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, true, false, true, false, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, true, true, false, false, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, false, true, false, true, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, false, true, true, false, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, false, false, true, true, 4);
            rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, false, false, false, true, 4);
            rf.crearPrincipal("D:/Isis/Invesigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, true, true, true, true, 4);

            //rf.crearPrincipal("D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/test_isis_controlado_esepecies.fasta", "D:/Isis/Investigaci�n/SIU/Bases_MIKE/Ultimas_Alzate/codigos.txt", 2, false, true, false, false, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSpecie(ArrayList<String> specie) {
        this.specie = specie;
    }

    public ArrayList<String> getSpecie() {
        return specie;
    }

}

