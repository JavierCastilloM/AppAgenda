package appagenda;


import entidades.Provincia;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author JavierCastilloM
 */
public class ConsultaProvincias {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em = emf.createEntityManager();

        Query queryProvincias = em.createNamedQuery("Provincia.findAll");
        List<Provincia> listProvincias = queryProvincias.getResultList();

        for (Provincia provincia : listProvincias) {
            System.out.println(provincia.getNombre());
        }

          // Si quieres buscar con un parametro en especifico
//        Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
//        queryProvinciaCadiz.setParameter("nombre", "Cádiz");
//        List<Provincia> listProvinciasCadiz = queryProvinciaCadiz.getResultList();
//        
//        for (Provincia provinciaCadiz : listProvinciasCadiz) {
//            System.out.println(provinciaCadiz.getId() + ":");
//            System.out.println(provinciaCadiz.getNombre());
//        }

        // TODO code application logic here
    }

}
