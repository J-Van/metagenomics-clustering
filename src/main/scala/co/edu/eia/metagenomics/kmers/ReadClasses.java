package co.edu.eia.metagenomics.kmers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadClasses {

    private ArrayList <String> id;
    private ArrayList <String> cat;
    private ArrayList <String> phylum;
    private ArrayList <String> specie;
    private ArrayList <Integer> cant;
    private ArrayList <Integer> cantPhyDif;
    private ArrayList <Integer> cantPhyTotal;
    private ArrayList <Double> porcPhy;
    private ArrayList <String> catDif;
    private ArrayList <String> phylumDif;
    private ArrayList <String> specieDif;

    public ReadClasses() {
        super();
        this.id = new ArrayList<String>();
        this.cat = new ArrayList<String>();
        this.phylum = new ArrayList<String>();
        this.specie = new ArrayList<String>();
        this.cant = new ArrayList<Integer>();
        this.cantPhyDif = new ArrayList<Integer>();
        this.cantPhyTotal = new ArrayList<Integer>();
        this.porcPhy = new ArrayList<Double>();
        this.catDif = new ArrayList<String>();
        this.phylumDif = new ArrayList<String>();
        this.setSpecieDif(new ArrayList<String>());
    }

    public ArrayList<String> getCatDif() {
        return catDif;
    }

    public void setCatDif(ArrayList<String> catDif) {
        this.catDif = catDif;
    }

    public ArrayList<String> getPhylumDif() {
        return phylumDif;
    }

    public void setPhylumDif(ArrayList<String> phylumDif) {
        this.phylumDif = phylumDif;
    }

    public ArrayList<String> getId() {
        return id;
    }

    public void setId(ArrayList<String> id) {
        this.id = id;
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

    public void setSpecie(ArrayList <String> specie) {
        this.specie = specie;
    }

    public ArrayList <String> getSpecie() {
        return specie;
    }

    public ArrayList<Integer> getCant() {
        return cant;
    }

    public void setCant(ArrayList<Integer> cant) {
        this.cant = cant;
    }

    public ArrayList<Integer> getCantPhyDif() {
        return cantPhyDif;
    }

    public void setCantPhyDif(ArrayList<Integer> cantPhyDif) {
        this.cantPhyDif = cantPhyDif;
    }

    public ArrayList<Integer> getCantPhyTotal() {
        return cantPhyTotal;
    }

    public void setCantPhyTotal(ArrayList<Integer> cantPhyTotal) {
        this.cantPhyTotal = cantPhyTotal;
    }

    public ArrayList<Double> getPorcPhy() {
        return porcPhy;
    }

    public void setPorcPhy(ArrayList<Double> porcPhy) {
        this.porcPhy = porcPhy;
    }

    public void setSpecieDif(ArrayList <String> specieDif) {
        this.specieDif = specieDif;
    }

    public ArrayList <String> getSpecieDif() {
        return specieDif;
    }

    public ArrayList<String> leer_old(String name,int tipo) throws IOException{
        BufferedReader buffer = new BufferedReader(new FileReader(new File(name)));
        String strLinea= buffer.readLine();// encabezado
        strLinea= buffer.readLine();
        // Leer el archivo l�nea por l�nea(1ra l�nea informaci�n)
        while (strLinea!=null) {
            String [] campos = strLinea.split("\\t+");
            id.add(campos[0]);
            cat.add(campos[1]);
            phylum.add(campos[2]);
            cant.add(Integer.parseInt(campos[3]));
            cantPhyDif.add(Integer.parseInt(campos[4]));
            cantPhyTotal.add(Integer.parseInt(campos[5]));
            porcPhy.add(Double.parseDouble(campos[6]));
            strLinea= buffer.readLine();
        }
        catDif=difArray(cat);
        phylumDif=difArray(phylum);
        return (tipo==1)? cat: phylum;
    }

    public ArrayList<String> leer(String name,int tipo) throws IOException{
        BufferedReader buffer = new BufferedReader(new FileReader(new File(name)));
        String strLinea= buffer.readLine();// encabezado
        strLinea= buffer.readLine();
        // Leer el archivo l�nea por l�nea(1ra l�nea informaci�n)
        while (strLinea!=null) {
            String [] campos = strLinea.split("\\t+");
            id.add(campos[0]);
            cat.add(campos[1]);
            phylum.add(campos[2]);
            specie.add(campos[3]);//Este fue a�adido para la nueva base
			/*cant.add(Integer.parseInt(campos[3]));
			cantPhyDif.add(Integer.parseInt(campos[4]));
			cantPhyTotal.add(Integer.parseInt(campos[5]));
			porcPhy.add(Double.parseDouble(campos[6]));*/
            strLinea= buffer.readLine();
        }
        catDif=difArray(cat);
        phylumDif=difArray(phylum);
        specieDif= difArray(specie);
        if (tipo==1)
            return cat;
        else
        if (tipo==2)
            return phylum;
        else return specie;
    }

    public ArrayList<String> difArray(ArrayList<String> a) {
        ArrayList<String> aDif= new ArrayList<String>();
        aDif.add(a.get(0)); // adicionar el primero
        int i=1;
        while (i<a.size()){
            if (aDif.indexOf(a.get(i))<0)
                aDif.add(a.get(i));
            i++;
        }
        return aDif;
    }
}
